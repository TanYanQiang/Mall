package com.lehemobile.shopingmall;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

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
}
