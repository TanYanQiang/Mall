package com.lehemobile.shopingmall.ui.user;

import android.content.Intent;
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
import com.tgh.devkit.dialog.DialogBuilder;
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
    private User user;

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
        pickImageHelper.showPickDialog();
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
}
