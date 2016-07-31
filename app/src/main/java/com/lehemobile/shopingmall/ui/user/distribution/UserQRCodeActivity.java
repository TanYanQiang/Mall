package com.lehemobile.shopingmall.ui.user.distribution;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.client.result.ParsedResultType;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.mylhyl.zxing.scanner.encode.QREncode;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.PermissionHelper;
import com.tgh.devkit.core.utils.PermissionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 31/7/16.
 */
@EActivity(R.layout.activity_user_qrcode)
public class UserQRCodeActivity extends BaseActivity {
    public static final int REQUEST_SCAN_QRCODE_CODE = 1;
    @ViewById
    ImageView QRCode;

    @AfterViews
    void init() {
        User user = ConfigManager.getUser();

        Bitmap bitmap = QREncode.encodeQR(this,
                new QREncode.Builder()
                        .setContents("" + user.getUserId())
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setParsedResultType(ParsedResultType.TEXT)
                        .build());

        QRCode.setImageBitmap(bitmap);

    }

    @Click
    void scanQRCode() {
        if (PermissionUtils.hasSelfPermissions(this, permission)) {
            startScanQRCode();
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permission)) {
                showRationaleFormStorage(new PermissionHelper.PermissionRequest() {
                    @Override
                    public void proceed() {
                        ActivityCompat.requestPermissions(UserQRCodeActivity.this, permission, REQUEST_CAMERA_PERMISSION_CODE);
                    }

                    @Override
                    public void cancel() {
                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(this, permission, REQUEST_CAMERA_PERMISSION_CODE);
        }


    }

    private void startScanQRCode() {
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        startActivityForResult(intent, REQUEST_SCAN_QRCODE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SCAN_QRCODE_CODE && resultCode == RESULT_OK) {
            String qrCode = data.getStringExtra(AppConfig.Extra);
            Logger.i("scan qrcode:" + qrCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    String[] permission = {Manifest.permission.CAMERA};
    public static final int REQUEST_CAMERA_PERMISSION_CODE = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (PermissionUtils.verifyPermissions(grantResults)) {

            } else {
                DialogUtils.alert(this, getString(R.string.permission_camera_msg));
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
}
