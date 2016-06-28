package com.lehemobile.shopingmall.api.base;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;

/**
 * 处理服务器业务错误
 * Created by tanyq on 27/6/16.
 */
public class AppServerError extends ServerError {
    public AppServerError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public AppServerError() {
        super();
    }
}
