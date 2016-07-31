package com.lehemobile.shopingmall.api.base;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lehemobile.shopingmall.BuildConfig;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.config.IPConfig;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.IO;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by tanyq on 27/6/16.
 */
public abstract class BaseRequest<T> extends Request<T> {
    public static AppErrorListener fooErrorListener = new AppErrorListener(MyApplication.getInstance());

    private Response.Listener<T> listener;
    private Map<String, String> params;

    public BaseRequest(int method, String url, Map<String, String> params, Response.Listener<T> listener, AppErrorListener errorListener) {
        super(method, url, errorListener == null ? fooErrorListener : errorListener);
        init(params, listener);
    }

    private void init(Map<String, String> params, Response.Listener<T> listener) {
        this.listener = listener;
        this.params = params;
    }

    @Override
    public String getUrl() {
//        if (BuildConfig.DEBUG) { //TODO 临时测试代码
//            return IPConfig.getAPIBaseUrl() + "/test.jsp";
//        }
        return super.getUrl();
    }

    @Override
    public Map<String, String> getParams() {
        Logger.i("params:" + params);
        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            //在测试版本中，只要在assets目录下放入和访问api路径同名的json文件，即可使用该文件的内容作为返回值，
            //比如请求Stat/career_stat接口，那么放入Stat_career_stat.json文件即可
            JSONObject mockResponse = foundMockResponse();
            if (mockResponse != null) {
                return Response.success(
                        treatResponse(mockResponse),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            String json = new String(response.data, "UTF-8");
            Logger.i("request url = %s \n response = %s", this.getUrl(), json);

            JSONObject base = new JSONObject(json);

            Logger.i(base.toString());
            int code = base.optInt("code", -1);
            if (code != 0) {
                return Response.error(new AppServerError(response));
            }
            return Response.success(treatResponse(base), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new AppServerError(response));
        }
    }
    private JSONObject foundMockResponse() {
        /*if (!BuildConfig.DEBUG) {
            return null;
        }*/
        try {
            URL url = new URL(getUrl());
            String path = url.getPath();
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            path = path.substring(1);
            String fileName = path.replaceAll("/", "_");
            InputStream open = MyApplication.getInstance().getAssets().open(fileName + ".json");
            String json = new String(IO.read(open));
            return new JSONObject(json);
        } catch (Exception e) {
            return null;
        }
    }

    protected abstract T treatResponse(JSONObject baseJson) throws Exception;

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

}
