package com.lehemobile.shopingmall.api.base;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lehemobile.shopingmall.BuildConfig;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.config.IPConfig;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.DebugLog;
import com.tgh.devkit.core.utils.IO;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanyq on 27/6/16.
 */
public abstract class BaseRequest<T> extends Request<T> {
    public static AppErrorListener fooErrorListener = new AppErrorListener(MyApplication.getInstance());
    private Map<String, String> headers;
    private Response.Listener<T> listener;
    private Map<String, String> params;

    private String operation;

    public BaseRequest(int method, String url, Map<String, String> params, Response.Listener<T> listener, AppErrorListener errorListener) {
        super(method, url, errorListener == null ? fooErrorListener : errorListener);
        init(params, listener);
    }

    public BaseRequest(String operation, Map<String, String> params, Response.Listener<T> listener, AppErrorListener errorListener) {
        super(Method.POST, IPConfig.getApiUrl(operation), errorListener == null ? fooErrorListener : errorListener);
        this.operation = operation;
        init(params, listener);
    }

    private void init(Map<String, String> params, Response.Listener<T> listener) {
        this.listener = listener;
        this.params = params;
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public Map<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        params.putAll(ApiUtils.createCommonParams());
        Logger.i("params:" + params);
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.putAll(ApiUtils.createCommonHeaders());
        if (BuildConfig.DEBUG) {
            Logger.i("request headers:" + headers);
        }
        return headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            //在测试版本中，只要在assets目录下放入和访问api路径同名的json文件，即可使用该文件的内容作为返回值，
            //比如请求useraddr(operation)接口，那么放入operation.json文件即可
            JSONObject mockResponse = foundMockResponse();
            if (mockResponse != null) {
                return Response.success(
                        treatResponse(mockResponse),
                        HttpHeaderParser.parseCacheHeaders(response));
            }


            String json = new String(response.data, "UTF-8");
            Logger.i("request url = %s \nresponse = %s", this.getUrl(), json);

            JSONObject base = new JSONObject(json);
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
        if (!BuildConfig.DEBUG) {
            return null;
        }
        try {
            if (TextUtils.isEmpty(operation)) return null;
            InputStream open = MyApplication.getInstance().getAssets().open(operation + ".json");
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
