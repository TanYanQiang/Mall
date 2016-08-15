package com.tgh.devkit.core.utils;

import android.Manifest;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tgh.devkit.core.config.KeyValueStore;
import com.tgh.devkit.core.config.KeyValueStoreInternal;

import java.util.UUID;

/**
 * Created by   on 5/5/15.
 */
public final class DeviceId {
    public static String getDefaultDeviceID(Context context) {
        KeyValueStoreInternal internal = KeyValueStore.getInstance(context);
        String deviceID = internal.getString("DefaultDeviceID", null);
        if (TextUtils.isEmpty(deviceID)) {
            deviceID = UUID.randomUUID().toString();
            internal.save("DefaultDeviceID", deviceID);
        }
        return deviceID;
    }

    /**
     * 获取DeviceId
     * <p/>
     * 不赋予权限的情况下，自动生成UUID(只在第一次)返回
     * 获取IMEI,没有的情况下获取ANDROID_ID，都获取不到的情况下，返回默认的UUID
     *
     * @return deviceId
     */
    public static String getDeviceID(Context context) {
        if (!Utils.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return getDefaultDeviceID(context);
        }

        String imei = getIMEI(context);
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }

        String androidId = getAndroidId(context);
        if (TextUtils.isEmpty(androidId)) {
            return getDefaultDeviceID(context);
        }
        return androidId;
    }

    public static String getIMEI(Context context) {
        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            imei = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(imei)) {
                imei = "";
            }
        }
        return imei;
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            androidId = "";
        }
        return androidId;
    }

}

