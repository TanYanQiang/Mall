package com.tgh.devkit.core.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * Created by albert on 16/1/8.
 */
public class Utils {

    private Utils() {
    }


    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            DebugLog.e(e.getMessage(), e);
        }
        return 1;
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            DebugLog.e(e.getMessage(), e);
        }
        return "1";
    }

    public static String getAppName(Context context) {
        int labelRes = context.getApplicationInfo().labelRes;
        return context.getString(labelRes);
    }

    /**
     * 获取Manifest文件里的MetaData
     */
    public static String getMetaDataInManifest(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    public static boolean isAppForeground(Context context){
        String foregroundPackageName = getForegroundPackageName(context);
        String packageName = getPackageName(context);
        return packageName.equals(foregroundPackageName);
    }

    public static String getForegroundPackageName(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
        return foregroundTaskInfo .topActivity.getPackageName();
    }

    /**
     * 扩大视图的响应区域
     * @param bigView  大视图，通常可以用Space
     * @param smallView 小视图，比如很小icon的ImageView
     */
    public static void expandTouchArea(final View bigView,final View smallView){
        bigView.post(new Runnable() {
            @Override
            public void run() {
                int width = bigView.getWidth();
                int height = bigView.getHeight();
                Rect rect = new Rect(0, 0, width, height);
                bigView.setTouchDelegate(new TouchDelegate(rect, smallView));
            }
        });
    }


    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }


    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * Append given name/value pairs as query parameters to the base URL
     * <p>
     * The params argument is interpreted as a sequence of name/value pairs so the
     * given number of params must be divisible by 2.
     *
     * @param url
     * @param params
     *          name/value pairs
     * @return URL with appended query params
     */
    public static String appendUrl(final CharSequence url, final Object... params) {
        final String baseUrl = url.toString();
        if (params == null || params.length == 0)
            return baseUrl;

        if (params.length % 2 != 0)
            throw new IllegalArgumentException(
                    "Must specify an even number of parameter names/values");

        final StringBuilder result = new StringBuilder(baseUrl);

        addPathSeparator(baseUrl, result);
        addParamPrefix(baseUrl, result);

        Object value;
        result.append(params[0]);
        result.append('=');
        value = params[1];
        if (value != null)
            result.append(value);

        for (int i = 2; i < params.length; i += 2) {
            result.append('&');
            result.append(params[i]);
            result.append('=');
            value = params[i + 1];
            if (value != null)
                result.append(value);
        }

        return result.toString();
    }

    private static StringBuilder addPathSeparator(final String baseUrl,
                                                  final StringBuilder result) {
        // Add trailing slash if the base URL doesn't have any path segments.
        //
        // The following test is checking for the last slash not being part of
        // the protocol to host separator: '://'.
        if (baseUrl.indexOf(':') + 2 == baseUrl.lastIndexOf('/'))
            result.append('/');
        return result;
    }

    private static StringBuilder addParamPrefix(final String baseUrl,
                                                final StringBuilder result) {
        // Add '?' if missing and add '&' if params already exist in base url
        final int queryStart = baseUrl.indexOf('?');
        final int lastChar = result.length() - 1;
        if (queryStart == -1)
            result.append('?');
        else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&')
            result.append('&');
        return result;
    }


    /**
     * Encode the given URL as an ASCII {@link String}
     * <p>
     * This method ensures the path and query segments of the URL are properly
     * encoded such as ' ' characters being encoded to '%20' or any UTF-8
     * characters that are non-ASCII.
     * @param url
     * @return encoded URL
     */
    public static String encodeUrl(final CharSequence url)
            throws IOException {
        URL parsed = new URL(url.toString());

        String host = parsed.getHost();
        int port = parsed.getPort();
        if (port != -1)
            host = host + ':' + Integer.toString(port);

        try {
            String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(),
                    parsed.getQuery(), null).toASCIIString();
            int paramsStart = encoded.indexOf('?');
            if (paramsStart > 0 && paramsStart + 1 < encoded.length())
                encoded = encoded.substring(0, paramsStart + 1)
                        + encoded.substring(paramsStart + 1).replace("+", "%2B");
            return encoded;
        } catch (URISyntaxException e) {
            IOException io = new IOException("Parsing URI failed");
            io.initCause(e);
            throw io;
        }
    }

    /**
     * 检查权限
     */
    public static boolean checkPermission(Context context,String permission){
        return context.checkCallingOrSelfPermission(permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 启动组件
     */
    public static void enableComponent(Context context,ComponentName componentName){
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     */
    public static void disableComponent(Context context,ComponentName componentName){
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }

        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)){
            return true;
        }

        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }


    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }

        if (!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)){
            return true;
        }

        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isAvailable()){
            String typeName = networkInfo.getTypeName();
            if (typeName.equalsIgnoreCase("WIFI")){
                return true;
            }
        }
        return false;
    }

    /**
     * 返回运营商 需要加入权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/> <BR>
     */
    public static String getOperators(Context context) {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        if (!checkPermission(context,Manifest.permission.READ_PHONE_STATE)){
            return null;
        }

        String IMSI =  ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getSubscriberId();
        if (IMSI == null){
            return null;
        }
        // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            return "中国移动";
        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
            return "中国联通";
        } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
            return "中国电信";
        }
        return null;
    }

    public static void scoreApp(Activity activity,@StringRes int errorMsg){
        String packageName = activity.getPackageName();
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        if (intent.resolveActivity(activity.getPackageManager()) != null) { //可以接收
            activity.startActivity(intent);
        } else {
            //没有应用市场，我们通过浏览器跳转到Google Play
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
            if (intent.resolveActivity(packageManager) != null) { //有浏览器
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void hiddenKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus!=null){
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /**
     * 对指定手机号发短信
     *
     * @param phoneNumber 手机号
     * @param sms_body    短信内容
     */
    public static void smsToSpecialMobile(Context activity,
                                          String phoneNumber,
                                          String sms_body) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", sms_body);
        ComponentName name = it.resolveActivity(activity.getPackageManager());
        if (name != null) {
            activity.startActivity(it);
        } else {
            Toast.makeText(activity, "您的设备不支持发送短信", Toast.LENGTH_SHORT).show();
        }
    }
}
