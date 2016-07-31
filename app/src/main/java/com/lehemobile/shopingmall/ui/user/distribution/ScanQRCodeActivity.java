package com.lehemobile.shopingmall.ui.user.distribution;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerView;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.PermissionHelper;
import com.tgh.devkit.core.utils.PermissionUtils;


/**
 * Created by tanyq on 31/7/16.
 */
public class ScanQRCodeActivity extends BaseActivity implements OnScannerCompletionListener {

    private ScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    private void initViews() {
        setContentView(R.layout.activity_scan_qrcode);
        //扫描 https://github.com/mylhyl/Android-Zxing
        scannerView = (ScannerView) findViewById(R.id.scanner_view);
        if (scannerView != null) {
            scannerView.setLaserFrameBoundColor(getResources().getColor(R.color.colorAccent));
            scannerView.setLaserColor(getResources().getColor(R.color.colorAccent));
            scannerView.setLaserFrameTopMargin(100);
            scannerView.setOnScannerCompletionListener(this);
        }
    }

    @Override
    protected void onResume() {
        if (scannerView != null) {
            scannerView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    String[] permission = {Manifest.permission.CAMERA};
    public static final int REQUEST_CAMERA_PERMISSION_CODE = 1;

    private void checkPermission() {

        if (PermissionUtils.hasSelfPermissions(this, permission)) {
            initViews();
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permission)) {
                showRationaleFormStorage(new PermissionHelper.PermissionRequest() {
                    @Override
                    public void proceed() {
                        ActivityCompat.requestPermissions(ScanQRCodeActivity.this, permission, REQUEST_CAMERA_PERMISSION_CODE);
                    }

                    @Override
                    public void cancel() {
//                        finish();
                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(this, permission, REQUEST_CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (PermissionUtils.verifyPermissions(grantResults)) {
                initViews();
            } else {
                DialogUtils.alert(this, 0, R.string.permission_camera_msg, android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void showRationaleFormStorage(final PermissionHelper.PermissionRequest permissionRequest) {
        DialogUtils.alert(this, null, getString(R.string.permission_camera_msg), android.R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest.cancel();
            }
        }, android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionRequest.proceed();
            }
        });
    }

    @Override
    public void OnScannerCompletion(Result result, ParsedResult parsedResult, Bitmap bitmap) {
        Logger.i("result :" + result.getText());
        Intent intent = new Intent();
        intent.putExtra(AppConfig.Extra, result.getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}
