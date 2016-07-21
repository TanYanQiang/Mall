package com.lehemobile.shopingmall;

import android.app.Application;

import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.event.LoginEvent;
import com.lehemobile.shopingmall.event.LogoutEvent;
import com.lehemobile.shopingmall.model.User;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import de.greenrobot.event.EventBus;

/**
 * Created by tanyq on 27/6/16.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initLogger();

        Logger.i("MyApplication init");
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initLogger() {
        Logger.init("ShopingTAG").logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    public void onUserLogin(User user) {
        ConfigManager.setUserId(user.getUserId());
        ConfigManager.saveUser(user);
        EventBus.getDefault().post(new LoginEvent());
    }

    public void onUserLogout() {
        ConfigManager.setUserId(0);
        ConfigManager.deleteCacheUser();
        EventBus.getDefault().post(new LogoutEvent());
    }

}
