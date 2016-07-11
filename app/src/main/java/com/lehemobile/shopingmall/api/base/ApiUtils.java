package com.lehemobile.shopingmall.api.base;

import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;

import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.tgh.devkit.core.encrypt.HMAC;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tgh.devkit.core.utils.IO.silentURLEncode;

/**
 * 所有Api都需要的功能
 * Created by albert on 16/3/8.
 */
public class ApiUtils {


    public interface ParseJsonObject<T> {
        T parse(JSONObject jobj) throws Exception;
    }

    public static <T> ArrayList<T> parseJsonArray(JSONArray ja, ParseJsonObject<T> parseJsonObject)
            throws Exception {
        if (ja == null || ja.length() == 0) {
            return new ArrayList<>();
        }
        final int count = ja.length();
        ArrayList<T> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(parseJsonObject.parse(ja.getJSONObject(i)));
        }
        return results;
    }

    public static Map<String, String> quickParams(String... args) {
        if (args == null || args.length % 2 != 0) {
            throw new RuntimeException("bad args");
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            params.put(args[i], args[i + 1]);
        }
        return params;
    }
}
