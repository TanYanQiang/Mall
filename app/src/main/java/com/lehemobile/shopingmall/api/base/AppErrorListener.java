package com.lehemobile.shopingmall.api.base;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

/**
 * Created by tanyq on 27/6/16.
 */
public class AppErrorListener implements Response.ErrorListener {
    public static final int CODE_SERVER_ERROR = -1000;
    public static final int CODE_NO_INTENT = -1001;
    public static final int CODE_TIME_OUT = -1002;

    public static final String SERVER_ERROR_DESC = "网络龟速啊，请稍后再试";
    public static final String TIME_OUT_DESC = "网络不给力，请稍后再试";

    private final Context context;

    public AppErrorListener(Context context) {
        this.context = context;

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error == null) {
            onError(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
            return;
        }
        Logger.e("error:" + error.getMessage());
        if (error instanceof NoConnectionError) {
            onError(CODE_NO_INTENT, TIME_OUT_DESC);
        } else if (error instanceof NetworkError) {
            onError(CODE_NO_INTENT, TIME_OUT_DESC);
        } else if (error instanceof TimeoutError) {
            onError(CODE_TIME_OUT, TIME_OUT_DESC);
        } else if (error instanceof AppServerError) {
            treat(error); //处理业务错误
        } else {
            onError(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
        }
    }


    public void onError(int code, String msg) {
        showToast(msg);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    private void treat(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse == null) return;
        try {
            String response = new String(networkResponse.data);
            Logger.e("response: %s", response);

            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.optInt("code");
            String msg = jsonObject.optString("msg");
            onError(code, msg);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.fillInStackTrace(), "volleyError");
            onError(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
        }
    }
}
