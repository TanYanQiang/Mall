package com.tgh.devkit.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.tgh.devkit.core.R;

/**
 * Created by tanyq on 6/4/16.
 * 北京帅莱世纪科技有限公司
 */
public class PermissionHelper {


    public static final int REQUEST_PERMISSION_CODE = 1;

    public static final int PERMISSION_STORAGE = 1;
    public static final int PERMISSION_RECORD_AUDIO = 2;
    public static final int PERMISSION_PHONE_STATE = 3;

    public static boolean checkPermission(final Activity activity, int permissionType, final String[] permissions) {

        String msg;
        switch (permissionType) {
            case PERMISSION_STORAGE:
                msg = activity.getString(R.string.permission_storage);
                break;
            case PERMISSION_RECORD_AUDIO:
                msg = activity.getString(R.string.permission_record_audio);
                break;
            default:
                msg = activity.getString(R.string.permission_default);
                break;
        }

        return checkPermission(activity, msg, permissions);
    }

    public static boolean checkPermission(final Activity activity, String showRequestPermissionMsg, final String[] permissions) {
        if (PermissionUtils.hasSelfPermissions(activity, permissions)) {

            return true;
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(activity, permissions)) {
                showRationaleDialog(activity, showRequestPermissionMsg, new PermissionRequest() {
                    @Override
                    public void proceed() {
                        //app 设置权限界面
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);

                    }

                    @Override
                    public void cancel() {

                    }
                });
            } else {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE);
            }
        }
        return false;
    }


    public interface PermissionRequest {
        void proceed();

        void cancel();
    }


    public static void showRationaleDialog(final Context context, final String message, final PermissionRequest request) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {
                                request.proceed();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {
                                request.cancel();
                            }
                        })
                        .setCancelable(false)
                        .setMessage(message)
                        .show();
            }
        });

    }


}
