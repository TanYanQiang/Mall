package com.lehemobile.shopingmall.api.base;

import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;

import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.config.IPConfig;
import com.lehemobile.shopingmall.model.UploadImage;
import com.tgh.devkit.core.encrypt.HMAC;
import com.tgh.devkit.core.encrypt.Md5;
import com.tgh.devkit.core.utils.DeviceId;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tgh.devkit.core.utils.IO.silentURLEncode;

/**
 * 所有Api都需要的功能
 * Created by albert on 16/3/8.
 */
public class ApiUtils {


    public interface ParseJsonObject<T> {
        T parse(JSONObject jobj) throws Exception;
    }

    public static <T> ArrayList<T> parseJsonArray(JSONArray ja, ParseJsonObject<T> parseJsonObject)
            throws Exception {
        if (ja == null || ja.length() == 0) {
            return new ArrayList<>();
        }
        final int count = ja.length();
        ArrayList<T> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(parseJsonObject.parse(ja.getJSONObject(i)));
        }
        return results;
    }

    public static Map<String, String> quickParams(String... args) {
        if (args == null || args.length % 2 != 0) {
            throw new RuntimeException("bad args");
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            params.put(args[i], args[i + 1]);
        }
        return params;
    }

    public static Map<String, String> createCommonHeaders() {

        final Context context = MyApplication.getInstance();

        String deviceTime = String.valueOf(System.currentTimeMillis());
        String deviceIdentifier = DeviceId.getDeviceID(context);
        String appId = Utils.getPackageName(context);
        String userId = String.valueOf(ConfigManager.getUserId());

        Map<String, String> headers = new HashMap<>();
        headers.put("Device-Time", deviceTime);
        headers.put("App-Name", silentURLEncode(Utils.getAppName(context)));
        headers.put("Device-Name", silentURLEncode(Build.MODEL));
        headers.put("Device-Model", silentURLEncode(Build.MODEL));
        headers.put("System-Name", "Android");
        headers.put("System-Version", silentURLEncode(Build.VERSION.RELEASE));
        headers.put("Device-Identifier", deviceIdentifier);
        headers.put("App-Id", appId);
        headers.put("App-Version", Utils.getAppVersionName(context));
        headers.put("User-Platform", "android");
        headers.put("User-Id", userId);
        headers.put("Device-Product", silentURLEncode(Build.PRODUCT));
        headers.put("Device-Fingerprint", silentURLEncode(Build.FINGERPRINT));
        headers.put("Device-Hardware", silentURLEncode(Build.HARDWARE));
        return headers;
    }

    public static Map<String, String> createCommonParams() {
        final Context context = MyApplication.getInstance();
        String deviceIdentifier = DeviceId.getDeviceID(context);

        String userId = String.valueOf(ConfigManager.getUserId());

        Map<String, String> headers = new HashMap<>();

        headers.put("uid", userId);
        headers.put("token", ConfigManager.getUserToken());
        headers.put("appId", deviceIdentifier);
        return headers;
    }

    public static BaseMultiPartRequest<UploadImage> uploadImage(
            String imageFilePath, int type,
            final Response.Listener<UploadImage> listener, AppErrorListener errorListener) {
        BaseMultiPartRequest<UploadImage> request =
                new BaseMultiPartRequest<UploadImage>(
                        IPConfig.getAPIBaseUrl() + "Public/upload_img",
                        listener, errorListener) {
                    @Override
                    protected UploadImage treatResponse(JSONObject baseJson) throws Exception {
                        JSONObject result = baseJson.getJSONObject("result");
                        UploadImage uploadImage = new UploadImage();
                        uploadImage.rawUrl = result.getString("upload_file");
                        uploadImage.relativeUrl = result.getString("file_path");
                        return uploadImage;
                    }
                };
        request.addFile("file", imageFilePath);
        request.addMultipartParam("file_type", "application/text", String.valueOf(type));
        return request;
    }
}
