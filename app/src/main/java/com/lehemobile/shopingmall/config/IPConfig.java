package com.lehemobile.shopingmall.config;

import android.text.TextUtils;

import com.lehemobile.shopingmall.BuildConfig;
import com.lehemobile.shopingmall.MyApplication;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.config.KeyValueStore;
import com.tgh.devkit.core.config.KeyValueStoreInternal;
import com.tgh.devkit.core.utils.Strings;

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

    public static String getAPIBaseUrl() {

        return BuildConfig.appBaseUrl;
    }


    public static String getApiUrl(String operation) {
        String url = "%s/index.php?op=%s";
        url = String.format(url, getAPIBaseUrl(), operation);
        return url;
    }

    public static String getFullImageUrl(String url) {
        if (!Strings.isNullOrEmpty(url)) {
            if (!url.startsWith("http:") &&
                    !url.startsWith("https:") &&
                    !url.startsWith("file:")) {
                Logger.i("imageHost:"+BuildConfig.imageHost);
                return BuildConfig.imageHost + url;
            } else {
                return url;
            }
        } else {
            return null;
        }
    }
}
