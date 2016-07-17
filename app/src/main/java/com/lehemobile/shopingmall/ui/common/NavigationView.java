package com.lehemobile.shopingmall.ui.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.user.ProfileActivity_;
import com.lehemobile.shopingmall.ui.user.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

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

    @AfterViews
    void initViews() {
        Glide.with(getContext())
                .load("http://s.cn.bing.net/az/hprichbg/rb/KenaiFjordsHumpback_ZH-CN10219728807_1920x1080.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(avatar);
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
        ProfileActivity_.intent(getContext()).start();
    }


    @Click(R.id.nav_message_layout)
    void goMessage(View view) {
        if (setSelected(view)) return;

    }

    @Click(R.id.nav_category)
    void goCategory(View view) {
        if (setSelected(view)) return;

    }

    @Click(R.id.nav_integral)
    void goIntegral(View view) {
        if (setSelected(view)) return;
    }

    @Click(R.id.nav_partner)
    void goPartner(View view) {
        if (setSelected(view)) return;

    }

    @Click(R.id.nav_contact)
    void goContact(View view) {
        if (setSelected(view)) return;
    }

    @Click(R.id.nav_setting)
    void goSetting(View view) {
        if (setSelected(view)) return;
    }

}
