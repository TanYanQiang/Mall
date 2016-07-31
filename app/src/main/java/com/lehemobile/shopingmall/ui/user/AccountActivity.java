package com.lehemobile.shopingmall.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.event.LoginEvent;
import com.lehemobile.shopingmall.event.LogoutEvent;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.SettingActivity_;
import com.lehemobile.shopingmall.ui.order.OrderListActivity_;
import com.lehemobile.shopingmall.ui.user.address.AddressListsActivity_;
import com.lehemobile.shopingmall.ui.user.distribution.UserQRCodeActivity_;
import com.lehemobile.shopingmall.ui.user.favorite.FavoriteActivity_;
import com.lehemobile.shopingmall.ui.user.login.LoginActivity_;
import com.lehemobile.shopingmall.ui.user.login.RegisterStep1Activity_;
import com.lehemobile.shopingmall.ui.view.glide.CropCircleTransformation;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

import de.greenrobot.event.EventBus;

/**
 * Created by tanyq on 17/7/16.
 */
@EActivity(R.layout.activity_account)
public class AccountActivity extends BaseActivity {

    @ViewById
    View profileLayout;

    @ViewById
    View loginLayout;

    @ViewById
    ImageView avatar;

    @ViewById
    TextView nick;

    @ViewById
    TextView userId;

    @ViewById
    TextView registerTime;

    @ViewById
    TextView parentName;


    @DimensionPixelOffsetRes(R.dimen.avatar_borderWidth)
    int avatar_borderWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @AfterViews
    void init() {
        updateUI();
    }

    private void updateUI() {
        if (ConfigManager.isLogin()) {
            loginLayout.setVisibility(View.GONE);
            updateUserInfo();
            profileLayout.setVisibility(View.VISIBLE);
        } else {
            Glide.with(this).load("").placeholder(R.mipmap.avatar_default).bitmapTransform(new CropCircleTransformation(this)).into(avatar);
            loginLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.GONE);
        }
    }

    private void updateUserInfo() {
        User user = ConfigManager.getUser();
        Glide.with(this).load(user.getAvatar())
                .placeholder(R.mipmap.avatar_default)
                .bitmapTransform(new CropCircleTransformation(this, getResources().getDimension(R.dimen.avatar_borderWidth), getResources().getColor(R.color.avatar_borderColor)))
                .into(avatar);

        nick.setText(user.getNick());
        userId.setText(getString(R.string.label_account_userID, "" + user.getUserId()));
        registerTime.setText(getString(R.string.label_account_user_registerTime, user.getRegisterTime()));
        parentName.setText(getString(R.string.label_account_user_parent_name, user.getParentName()));
    }

    private boolean isLogin() {

        if (!ConfigManager.isLogin()) {
            LoginActivity_.intent(this).start();
            return false;
        }
        return true;
    }

    @Click(R.id.homeAsUpIndicator)
    void onBack() {
        onBackPressed();
    }

    @Click(R.id.userQRCode)
    void userQRCode() {
        if (!isLogin()) return;
        UserQRCodeActivity_.intent(this).start();
    }

    @Click(R.id.avatar)
    void editProfile() {
        if (!isLogin()) return;
        ProfileActivity_.intent(this).start();
    }

    @Click(R.id.tv_register)
    void doRegister() {
        RegisterStep1Activity_.intent(this).start();
    }

    @Click(R.id.tv_login)
    void doLogin() {
        LoginActivity_.intent(this).start();
    }


    @Click(R.id.action_settings)
    void doSettings() {
        SettingActivity_.intent(this).start();
    }


    @Click({R.id.allOrder, R.id.watingPay, R.id.watingDeliver, R.id.watingReceipt, R.id.complete,R.id.comment})
    void goOrderList(View view) {
        if (!isLogin()) return;
        int type = 0;
        switch (view.getId()) {
            case R.id.watingPay:
                type = 1;
                break;
            case R.id.watingDeliver:
                type = 2;
                break;
            case R.id.watingReceipt:
                type = 3;
                break;
            case R.id.complete:
                type = 4;
                break;
            case R.id.comment:
                type =5;
                break;
            case R.id.allOrder:
            default:
                type = 0;
                break;
        }
        OrderListActivity_.intent(this).type(type).start();
    }

    @Click
    void favorite() {
        if (!isLogin()) return;
        FavoriteActivity_.intent(this).start();
    }

    @Click
    void address() {
        if (!isLogin()) return;
        AddressListsActivity_.intent(this).start();
    }


    public void onEventMainThread(LoginEvent event) {
        Logger.i("Login Success");
        updateUI();
    }

    public void onEventMainThread(LogoutEvent event) {
        Logger.i("Logout Success");
        updateUI();
    }
}
