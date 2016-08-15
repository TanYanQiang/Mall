package com.lehemobile.shopingmall;

import android.app.Application;

import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.event.LoginEvent;
import com.lehemobile.shopingmall.event.LogoutEvent;
import com.lehemobile.shopingmall.model.User;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;
import com.tgh.devkit.core.utils.IO;


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
        initPicasso();
        initLogger();
        initBugly();
        Logger.i("MyApplication init");
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initPicasso() {
        Picasso picasso = new Picasso.Builder(this)
                .loggingEnabled(BuildConfig.DEBUG)
                .build();
        Picasso.setSingletonInstance(picasso);

    }

    private void initLogger() {
        Logger.init("ShopingTAG").logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    private void initBugly() {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
        CrashReport.initCrashReport(this, BuildConfig.buglyAppId, BuildConfig.DEBUG);
    }

    public void onUserLogin(User user) {
        ConfigManager.setUserId(user.getUserId());
        ConfigManager.setUserToken(user.getToken());
        ConfigManager.saveUser(user);
        EventBus.getDefault().post(new LoginEvent());
    }

    public void onUserLogout() {
        ConfigManager.setUserId(0);
        ConfigManager.setUserToken(null);
        ConfigManager.deleteCacheUser();
        EventBus.getDefault().post(new LogoutEvent());
    }

}
