package com.tgh.devkit.core.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.Random;

/**
 * 必须在UI线程调用本方法
 * Created by albert on 16/5/13.
 */
public class PermissionCheckHelper {

    public interface PermissionCheckCallback {
        void onHasPermission();

        void onNotGetPermission();
    }

    private final String[] permissions;
    private final Activity activity;
    private final PermissionCheckCallback permissionCheckCallback;
    private final String requestPermissionMessage;
    private final int REQUEST_CODE;

    private PermissionCheckHelper(Activity activity,
                                  String[] permissions,
                                  String requestPermissionMessage,
                                  PermissionCheckCallback permissionCheckCallback) {
        this.permissions = permissions;
        this.activity = activity;
        this.requestPermissionMessage = requestPermissionMessage;
        this.permissionCheckCallback = permissionCheckCallback;
        this.REQUEST_CODE = new Random().nextInt();
    }

    public static PermissionCheckHelper startCheckPermission(Activity activity,
                                                             String[] permissions,
                                                             String requestPermissionMessage,
                                                             PermissionCheckCallback permissionCheckCallback) {
        PermissionCheckHelper helper = new PermissionCheckHelper(activity, permissions,
                requestPermissionMessage, permissionCheckCallback);
        helper.checkPermission();
        return helper;
    }

    private void checkPermission() {
        if (!Utils.hasSelfPermissions(activity, permissions)) {
            if (Utils.shouldShowRequestPermissionRationale(activity, permissions)) {
                showRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
            }
        }else {
            permissionCheckCallback.onHasPermission();
        }
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(activity)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_CODE);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        permissionCheckCallback.onNotGetPermission();
                    }
                })
                .setCancelable(false)
                .setMessage(requestPermissionMessage)
                .show();
    }


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (Utils.hasSelfPermissions(activity, permissions)) {
                permissionCheckCallback.onHasPermission();
            } else {
                permissionCheckCallback.onNotGetPermission();
            }
            return true;
        }
        return false;
    }


    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            boolean success = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                permissionCheckCallback.onNotGetPermission();
            } else {
                permissionCheckCallback.onHasPermission();
            }
            return true;
        }
        return false;
    }
}
