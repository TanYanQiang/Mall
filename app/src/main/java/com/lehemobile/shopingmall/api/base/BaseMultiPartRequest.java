package com.lehemobile.shopingmall.api.base;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lehemobile.shopingmall.utils.volley.MultiPartRequest;
import com.tgh.devkit.core.utils.DebugLog;
import com.tgh.devkit.core.utils.Strings;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by albert on 16/3/10.
 */
public abstract class BaseMultiPartRequest<T> extends MultiPartRequest<T> {

    private Response.Listener<T> listener;

    public BaseMultiPartRequest(
            String url,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, listener, errorListener);

//        setRetryPolicy(new DefaultRetryPolicy(5000, 5, 0));
        this.listener = listener;
    }


    @Override
    public Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            DebugLog.v("request url = " + getUrl());
            DebugLog.v("response json = " + json);
            JSONObject base = new JSONObject(json);
            int code = base.optInt("code", 0);
            if (code != 0) {
                return Response.error(new AppServerError(response));
            }
            return Response.success(
                    treatResponse(base),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new AppServerError(response));
        }
    }

    protected abstract T treatResponse(JSONObject baseJson) throws Exception;

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
