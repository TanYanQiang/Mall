package com.lehemobile.shopingmall.ui.user;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.orhanobut.logger.Logger;
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
public class ProfileActivity extends BaseActivity {
    @ViewById
    ImageView avatar;

    @ViewById
    TextView nick;

    @ViewById
    TextView mobile;
    private PickImageHelper pickImageHelper;

    private void initPickImage() {
        pickImageHelper = new PickImageHelper(this);
        pickImageHelper.setCrop(true);
        pickImageHelper.setPickImageCallback(new PickImageHelper.PickImageCallback() {
            @Override
            public void onPickImageSuccess(String imageFile) {
                Logger.i("imageFile" + imageFile);
            }

            @Override
            public void onPickImageError() {

            }
        });

    }

    @AfterViews
    void init() {

        initPickImage();


        User user = ConfigManager.getUser();
        Glide.with(this).load(user.getAvatar()).bitmapTransform(new CropCircleTransformation(this)).into(avatar);
        nick.setText(user.getNick());
        mobile.setText(user.getMobile());
    }

    @Click(R.id.avatarLayout)
    void avatarLayout() {
        pickImageHelper.showPickDialog();
    }

    @Click(R.id.nickLayout)
    void nickLayout() {

    }

    @Click
    void resetPasswordLayout() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickImageHelper.onActivityResult(requestCode, resultCode, data);
    }
}
