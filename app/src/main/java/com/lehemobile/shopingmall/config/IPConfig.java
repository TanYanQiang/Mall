package com.lehemobile.shopingmall.config;

import android.text.TextUtils;

import com.lehemobile.shopingmall.BuildConfig;
import com.lehemobile.shopingmall.MyApplication;
import com.tgh.devkit.core.config.KeyValueStore;
import com.tgh.devkit.core.config.KeyValueStoreInternal;

/**
 * Created by albert on 16/3/8.
 */
public class IPConfig {

    /**
     * 这6个值是写死的，在正式环境的时候使用
     * <p/>
     * 在 Gradle 中的
     * <p/>
     * <p/>
     * 中设置。每个productFlavor 可以指定这个值
     */
    private static final String _appHost = BuildConfig.appHost;
    private static final int _appPort = BuildConfig.appPort;


    private static final String _api_url = "http://" + _appHost + ":" + _appPort + "/";

    private static final String KEY_APP_HOST = "app_host";
    private static final String KEY_APP_PORT = "app_port";


    private static String appHost;
    private static int appPort = -1;


    private static String buildApiUrl(String host, int port) {
        /**
         * api 访问
         */
        if (port == 80 || port == 0) {
            return "http://" + host + "/";
        }
        return "http://" + host + ":" + port + "/";
    }

    public static String getAPIBaseUrl() {
        if (!isDebug()) {
            return buildApiUrl(_appHost, _appPort);
        }
        if (appHost == null || appPort == -1) {
            init();
        }
        return buildApiUrl(appHost, appPort);
    }

    static {
        init();
    }

    public static void init() {
        if (isDebug()) {
            if (appHost == null) {
                appHost = getInstance().getString(KEY_APP_HOST, _appHost);
            }
            if (appPort == -1) {
                appPort = getInstance().getInt(KEY_APP_PORT, _appPort);
            }

        }
    }

    public static String getAppHost() {
        if (!isDebug()) {
            return _appHost;
        }

        if (appHost == null) {
            init();
        }
        return appHost;
    }

    public static void setAppHost(String appHost) {
        if (!isDebug()) {
            return;
        }
        appHost = treatHost(appHost);
        if (TextUtils.isEmpty(appHost)) {
            return;
        }
        IPConfig.appHost = appHost;
        getInstance().save(KEY_APP_HOST, appHost);
    }

    public static int getAppPort() {
        if (!isDebug()) {
            return _appPort;
        }
        if (appPort == -1) {
            init();
        }
        return appPort;
    }

    public static void setAppPort(int appPort) {
        if (!isDebug()) {
            return;
        }
        if (appPort < 0) {
            return;
        }

        IPConfig.appPort = appPort;
        getInstance().save(KEY_APP_PORT, appPort);
    }


    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    private static KeyValueStoreInternal getInstance() {
        return KeyValueStore.getInstance(MyApplication.getInstance(), "appSetting", false);
    }

    private static String treatHost(String str) {
        str = str.toLowerCase();

        if (str.startsWith("http://")) {
            str = str.substring("http://".length(), str.length());
        }
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
