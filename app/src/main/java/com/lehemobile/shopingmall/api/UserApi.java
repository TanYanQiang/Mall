package com.lehemobile.shopingmall.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.config.IPConfig;
import com.lehemobile.shopingmall.model.User;
import com.tgh.devkit.core.encrypt.Md5;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关的接口
 * Created by tanyq on 7/7/16.
 */
public class UserApi {


    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_RESET_PASSWORD = 2;

    public static BaseRequest<User> login(String mobile, String password, Response.Listener<User> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", Md5.toString(password));
        return new BaseRequest<User>(Request.Method.POST, IPConfig.getAPIBaseUrl() + "/User/login", params, listener, errorListener) {
            @Override
            protected User treatResponse(JSONObject baseJson) throws Exception {
                User user = new User();
                //TODO 解析登录json 返回User信息
                return user;
            }
        };
    }

    /**
     * 请求短信验证码
     */
    public static BaseRequest<Map<String, Object>> requestSmsCode(
            String mobile, int type,
            Response.Listener<Map<String, Object>> listener,
            AppErrorListener errorListener) {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("mobile", mobile);
        parmas.put("source", "" + type);
        return new BaseRequest<Map<String, Object>>(Request.Method.GET, IPConfig.getAPIBaseUrl() + "ShortMessage/getMessage", parmas, listener, errorListener) {

            @Override
            protected Map<String, Object> treatResponse(JSONObject baseJson) throws Exception {
                return null;
            }
        };
    }

    /**
     * @param mobile
     * @param smsCode
     * @param password
     * @param listener
     * @param errorListener
     * @return
     */
    public static BaseRequest<User> register(String mobile, String smsCode, String password, Response.Listener<User> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("smsCode", smsCode);
        params.put("password", Md5.toString(password));
        return new BaseRequest<User>(Request.Method.POST, IPConfig.getAPIBaseUrl() + "/User/register", params, listener, errorListener) {
            @Override
            protected User treatResponse(JSONObject baseJson) throws Exception {
                User user = new User();
                return user;
            }
        };
    }
    /**
     * @param mobile
     * @param smsCode
     * @param password
     * @param listener
     * @param errorListener
     * @return
     */
    public static BaseRequest<User> restPassword(String mobile, String smsCode, String password, Response.Listener<User> listener, AppErrorListener errorListener) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("smsCode", smsCode);
        params.put("password", Md5.toString(password));
        return new BaseRequest<User>(Request.Method.POST, IPConfig.getAPIBaseUrl() + "/User/register", params, listener, errorListener) {
            @Override
            protected User treatResponse(JSONObject baseJson) throws Exception {
                User user = new User();
                return user;
            }
        };
    }
}
