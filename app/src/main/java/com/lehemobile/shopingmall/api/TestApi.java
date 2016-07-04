package com.lehemobile.shopingmall.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

/**
 * Created by tanyq on 27/6/16.
 */
public class TestApi {
    public static Request test(Response.Listener<String> listener, AppErrorListener errorListener) {
        BaseRequest<String> request = new BaseRequest<String>(Request.Method.GET, "https://gank.io/api/data/Android/10/1", null, listener, errorListener) {

            @Override
            protected String treatResponse(JSONObject baseJson) throws Exception {
                return baseJson.toString();
            }
        };
        return request;
    }
}
