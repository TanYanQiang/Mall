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

    public BaseRequest(int method, String url, Map<String, String> params, Response.Listener<T> listener, AppErrorListener errorListener) {
        super(method, url, errorListener == null ? fooErrorListener : errorListener);
        init(params, listener);
    }

    public BaseRequest(String operation, Map<String, String> params, Response.Listener<T> listener, AppErrorListener errorListener) {
        super(Method.POST, IPConfig.getApiUrl(operation), errorListener == null ? fooErrorListener : errorListener);
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
