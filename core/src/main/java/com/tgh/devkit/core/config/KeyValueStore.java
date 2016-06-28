package com.tgh.devkit.core.config;

import android.content.Context;
import android.text.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * SharedPreferences Utils
 * Created by albert on 16/1/12.
 */
public class KeyValueStore {

    private static Map<String,KeyValueStoreInternal> entityCaches = Collections.synchronizedMap(
            new HashMap<String, KeyValueStoreInternal>());


    public static synchronized KeyValueStoreInternal getInstance(
            Context context) {
        return getInstance(context,null,false);
    }
    public static synchronized KeyValueStoreInternal getInstance(
            Context context,String config) {
        return getInstance(context,config,false);
    }

    public static synchronized KeyValueStoreInternal getInstance(
            Context context,boolean bindWithVersion) {
        return getInstance(context,null,bindWithVersion);
    }

    public static synchronized KeyValueStoreInternal getInstance(
            Context context,String config,boolean bindWithVersion) {
        if (TextUtils.isEmpty(config)){
            config = KeyValueStoreInternal.DEFAULT_SP;
        }
        if (entityCaches.containsKey(config)){
            KeyValueStoreInternal internal = entityCaches.get(config);
            internal.setBindWithVersion(bindWithVersion);
            return internal;
        }
        KeyValueStoreInternal internal = new KeyValueStoreInternal(context.getApplicationContext()
                ,config,bindWithVersion);
        entityCaches.put(config,internal);
        return internal;
    }
}
