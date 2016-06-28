package com.lehemobile.shopingmall.config;

import com.lehemobile.shopingmall.MyApplication;
import com.tgh.devkit.core.config.KeyValueStore;
import com.tgh.devkit.core.config.KeyValueStoreInternal;

/**
 * Created by tanyq on 28/6/16.
 */
public class ConfigManager {

    public static final String USER_ID = "user_id";

    private static KeyValueStoreInternal getDefaultBindVersionStore() {
        KeyValueStoreInternal instance = KeyValueStore.getInstance(MyApplication.getInstance());
        instance.setBindWithVersion(false);
        return instance;
    }

    public static int getUserId() {
        return getDefaultBindVersionStore().getInt(USER_ID, 0);
    }

    public static void setUserId(int userId) {
        KeyValueStoreInternal storeInternal = getDefaultBindVersionStore();
        storeInternal.save(USER_ID, userId);
    }

}
