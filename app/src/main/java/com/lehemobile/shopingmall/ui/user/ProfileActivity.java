package com.lehemobile.shopingmall.ui.user;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.utils.PermissionHelper;
import com.tgh.devkit.core.utils.PermissionUtils;
import com.tgh.devkit.pickimage.PickImageHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by  on 21/7/16.
 */
@EActivity(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final int REQUEST_STORAGE_PERMISSION_CODE = 1;
    @ViewById
    ImageView avatar;

    @ViewById
    TextView nick;

    @ViewById
    TextView mobile;
    private PickImageHelper pickImageHelper;
    private User user;

    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    void showChooseImage() {
        if (pickImageHelper == null) {
            initPickImage();
        }
        pickImageHelper.showPickDialog();
    }


    private void initPickImage() {
        pickImageHelper = new PickImageHelper(this);
        pickImageHelper.setCrop(true);
        pickImageHelper.setPickImageCallback(new PickImageHelper.PickImageCallback() {
            @Override
            public void onPickImageSuccess(String imageFile) {
                Logger.i("imageFile" + imageFile);
                uploadAvatar(imageFile);
            }

            @Override
            public void onPickImageError() {

            }
        });

    }

    @AfterViews
    void init() {

        user = ConfigManager.getUser();

        initPickImage();

        updateAvatar();

        updateNick();

        mobile.setText(user.getMobile());
    }


    private void updateAvatar() {
        Glide.with(this).load(user.getAvatar()).bitmapTransform(new CropCircleTransformation(this)).into(avatar);
    }

    private void updateNick() {
        User user = ConfigManager.getUser();
        nick.setText(user.getNick());
    }

    @Click(R.id.avatarLayout)
    void avatarLayout() {

        if (PermissionUtils.hasSelfPermissions(this, permission)) {
            showChooseImage();
        } else {
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permission)) {
                showRationaleFormStorage(new PermissionHelper.PermissionRequest() {
                    @Override
                    public void proceed() {
                        ActivityCompat.requestPermissions(ProfileActivity.this, permission, REQUEST_STORAGE_PERMISSION_CODE);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                return;
            }
            ActivityCompat.requestPermissions(this, permission, REQUEST_STORAGE_PERMISSION_CODE);
        }

    }

    @Click(R.id.nickLayout)
    void nickLayout() {
        View view = getLayoutInflater().inflate(R.layout.v_input_dialog, null);
        final EditText nickEidt = (EditText) view.findViewById(R.id.nickEdit);
        DialogUtils.alert(this, "修改昵称", view, android.R.string.cancel, null, android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nick = nickEidt.getText().toString().trim();
                Logger.i("nick:" + nick);
                user.setNick(nick);
                updateNick();
            }
        });
    }

    @Click
    void resetPasswordLayout() {
        UpdatePasswordActivity_.intent(this).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickImageHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadAvatar(String imagePath) {
        //TODO 上传图片
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION_CODE) {
            if (PermissionUtils.verifyPermissions(grantResults)) {
                showChooseImage();
            } else {
                DialogUtils.alert(this, getString(R.string.permission_storage_msg));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void showRationaleFormStorage(final PermissionHelper.PermissionRequest permissionRequest) {
        DialogUtils.alert(this, null, getString(R.string.permission_storage_msg), android.R.string.cancel, new View.OnClickListener() {
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
