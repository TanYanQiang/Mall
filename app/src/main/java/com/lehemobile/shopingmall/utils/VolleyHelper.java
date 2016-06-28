package com.lehemobile.shopingmall.utils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lehemobile.shopingmall.MyApplication;

/**
 * Created by tanyq on 27/6/16.
 */
public class VolleyHelper {
    private static VolleyHelper instance;
    private RequestQueue requestQueue;
    private static final Object TAG = new Object();

    private VolleyHelper() {
        requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
    }

    public static synchronized VolleyHelper getInstance() {
        if (instance == null) {
            instance = new VolleyHelper();
        }
        return instance;
    }

    public static RequestQueue getRequestQueue() {
        return getInstance().requestQueue;
    }

    public static void execute(Request request) {
        execute(request, TAG);
    }

    public static void execute(Request request, Object tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public static void cancel(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public static void cancel() {
        cancel(TAG);
    }
}
