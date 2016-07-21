package com.lehemobile.shopingmall.config;

import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.model.User;
import com.tgh.devkit.core.config.KeyValueStore;
import com.tgh.devkit.core.config.KeyValueStoreInternal;

/**
 * Created by tanyq on 28/6/16.
 */
public class ConfigManager {

    public static final String USER_ID = "user_id";
    public static final String USER = "user";

    private static User _user;

    private static KeyValueStoreInternal getDefaultBindVersionStore() {
        KeyValueStoreInternal instance = KeyValueStore.getInstance(MyApplication.getInstance());
        instance.setBindWithVersion(false);
        return instance;
    }

    private static String getKeyByUser(String key) {
        return "_" + getUserId() + "_" + key;
    }


    public static int getUserId() {
        return getDefaultBindVersionStore().getInt(USER_ID, 0);
    }

    public static void setUserId(int userId) {
        KeyValueStoreInternal storeInternal = getDefaultBindVersionStore();
        storeInternal.save(USER_ID, userId);
    }

    public static void saveUser(User user) {
        _user = user;
        KeyValueStoreInternal defaultBindVersionStore = getDefaultBindVersionStore();
        defaultBindVersionStore.saveObject(getKeyByUser(USER), user);
    }

    public static User getUser() {
        if (_user == null) {
            _user = getDefaultBindVersionStore().getObject(getKeyByUser(USER), User.class);
        }
        return _user;
    }

    public static boolean isLogin() {
        return getUser() != null;
    }


    public static class Device {
        public static final String LAST_MOBILE = "last_mobile";

        public static String getLastMobile() {
            return getDefaultBindVersionStore().getString(LAST_MOBILE, null);
        }

        public static void setLastMobile(String mobile) {
            getDefaultBindVersionStore().save(LAST_MOBILE, mobile);
        }
    }
}
