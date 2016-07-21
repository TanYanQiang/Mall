package com.lehemobile.shopingmall.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.tgh.devkit.core.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 21/7/16.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @ViewById
    TextView version;

    @ViewById
    Button logoutBtn;

    @AfterViews
    void init() {
        updateLogoutBtn();

        version.setText(Utils.getAppVersionName(this));
    }

    private void updateLogoutBtn() {
        logoutBtn.setVisibility(ConfigManager.isLogin() ? View.VISIBLE : View.GONE);
    }


    @Click
    @Background
    void clearCache() {
        Glide.get(this).clearDiskCache();
        showClearCacheToast();
    }


    @Click(R.id.versionLayout)
    void updateVersion() {
        showToast("已是最新版");
    }

    @UiThread
    void showClearCacheToast() {
        showToast("清除成功");
    }

    @Click(R.id.logoutBtn)
    void onLogout() {

        DialogUtils.alert(this, null, "确认退出登录吗？", android.R.string.cancel, null, android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().onUserLogout();
                updateLogoutBtn();
            }
        });

    }

}
