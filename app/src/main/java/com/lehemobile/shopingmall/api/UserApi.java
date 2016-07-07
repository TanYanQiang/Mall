package com.lehemobile.shopingmall.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.config.IPConfig;
import com.lehemobile.shopingmall.model.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关的接口
 * Created by tanyq on 7/7/16.
 */
public class UserApi {

    public static BaseRequest<User> login(String mobile, String password, Response.Listener<User> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        return new BaseRequest<User>(Request.Method.POST, IPConfig.getAPIBaseUrl() + "/User/login", params, listener, errorListener) {
            @Override
            protected User treatResponse(JSONObject baseJson) throws Exception {
                User user = new User();
                //TODO 解析登录json 返回User信息
                return user;
            }
        };
    }
}
