package com.lehemobile.shopingmall.api.base;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lehemobile.shopingmall.MyApplication;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

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
    public Map<String, String> getParams() {
        Logger.i("params:" + params);
        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            Logger.i("request url = %s \n response = %s", this.getUrl(), json);

            JSONObject base = new JSONObject(json);

            Logger.i(base.toString());
            //TODO
            boolean error = base.optBoolean("error", false);
            if (error) {
                return Response.error(new AppServerError(response));
            }
            /*int code = base.optInt("code", -1);
            if (code != 0) {
                return Response.error(new AppServerError(response));
            }*/
            return Response.success(treatResponse(base), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new AppServerError(response));
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
