package com.lehemobile.shopingmall.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.event.LoginEvent;
import com.lehemobile.shopingmall.event.LogoutEvent;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.SettingActivity_;
import com.lehemobile.shopingmall.ui.category.CategoryActivity_;
import com.lehemobile.shopingmall.ui.user.AccountActivity_;
import com.lehemobile.shopingmall.ui.user.distribution.team.TeamUserListActivity_;
import com.lehemobile.shopingmall.ui.user.login.LoginActivity_;
import com.lehemobile.shopingmall.ui.view.Picasso.CropCircleTransformation;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by tanyq on 17/7/16.
 */
@EViewGroup(R.layout.view_navigation)
public class NavigationView extends FrameLayout {

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(View view);
    }

    @ViewById
    ImageView avatar;
    private OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.onNavigationItemSelectedListener = onNavigationItemSelectedListener;
    }

    public NavigationView(Context context) {
        super(context);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @AfterViews
    void initViews() {
        updateUI();
    }

    private void updateUI() {
        if (ConfigManager.isLogin()) {
            User user = ConfigManager.getUser();
            Picasso.with(getContext()).load(user.getAvatar())
                    .placeholder(R.mipmap.avatar_default)
                    .transform(new CropCircleTransformation(getResources().getDimension(R.dimen.avatar_borderWidth), getResources().getColor(R.color.avatar_borderColor)))
                    .into(avatar);
        } else {
            Picasso.with(getContext()).load(R.mipmap.avatar_default).into(avatar);
        }
    }

    boolean setSelected(View view) {
        if (onNavigationItemSelectedListener != null) {
            return onNavigationItemSelectedListener.onNavigationItemSelected(view);
        }
        return false;
    }

    @Click(R.id.avatar)
    void goProfile(View view) {
        if (setSelected(view)) return;
        AccountActivity_.intent(getContext()).start();
    }


    @Click(R.id.nav_message_layout)
    void goMessage(View view) {
        if (setSelected(view)) return;

    }

    @Click(R.id.nav_category)
    void goCategory(View view) {
        if (setSelected(view)) return;
        CategoryActivity_.intent(getContext()).start();
    }

    @Click(R.id.nav_integral)
    void goIntegral(View view) {
        if (setSelected(view)) return;
        if (!isLogin()) return;
        WebViewActivity.intent(getContext()).url(AppConfig.INTEGRAL_MALL_URL).title("积分商城").start();
    }

    @Click(R.id.nav_partner)
    void goPartner(View view) {
        if (setSelected(view)) return;
        if (!isLogin()) return;
        TeamUserListActivity_.intent(getContext()).start();
    }

    @Click(R.id.nav_contact)
    void goContact(View view) {
        if (setSelected(view)) return;
    }

    @Click(R.id.nav_setting)
    void goSetting(View view) {
        if (setSelected(view)) return;
        SettingActivity_.intent(getContext()).start();
    }


    public void onEventMainThread(LoginEvent event) {
        Logger.i("Login Success");
        updateUI();
    }

    public void onEventMainThread(LogoutEvent event) {
        Logger.i("Logout Success");
        updateUI();
    }

    private boolean isLogin() {

        if (!ConfigManager.isLogin()) {
            LoginActivity_.intent(getContext()).start();
            return false;
        }
        return true;
    }
}
