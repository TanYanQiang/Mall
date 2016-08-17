package com.lehemobile.shopingmall.ui;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.config.AppConfig;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.tgh.devkit.core.utils.IO;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by tanyq on 21/7/16.
 */
@EActivity(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @ViewById
    TextView cacheSize;

    @ViewById
    CheckBox imageSwitch;
    @ViewById
    CheckBox notificationSwitch;
    @ViewById
    CheckBox saleReminderSwitch;

    @ViewById
    TextView version;

    @ViewById
    Button logoutBtn;

    @AfterViews
    void init() {
        updateLogoutBtn();
        calculateCacheSize();

        version.setText(Utils.getAppVersionName(this));

        imageSwitch.setChecked(ConfigManager.getImageSwitch());

        notificationSwitch.setChecked(ConfigManager.gettNotificationSwitch());
        saleReminderSwitch.setChecked(ConfigManager.getSaleReminderSwitch());


    }

    private File getPicassoImageCache() {
        String cachePath = getCacheDir().getPath();
        File dir = new File(cachePath + File.separator + AppConfig.IMAGE_CACHE_DIR);
        if (!IO.exist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    private void updateLogoutBtn() {
        logoutBtn.setVisibility(ConfigManager.isLogin() ? View.VISIBLE : View.GONE);
    }

    @Background
    void calculateCacheSize() {
        File cacheDir = getPicassoImageCache();
        long totalSpace = cacheDir.getTotalSpace();
        Logger.i("cache totalSpace:" + totalSpace);

        long size = IO.getSize(cacheDir);

        String cacheSize = Strings.doubleTrans(size / 1024.0d / 1024.0d);
        updateCacheSize(cacheSize + "M");
    }

    @UiThread
    void updateCacheSize(String cache) {
        cacheSize.setText(cache);
    }


    @Click
    void clearCache() {
        DialogUtils.alert(this, null, "确认要清除图片缓存？", android.R.string.cancel, null, android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCacheFile();
            }
        });
    }

    @Background
    void clearCacheFile() {
        File cacheDir = getPicassoImageCache();
        IO.delete(cacheDir);
        showClearCacheToast();
    }

    @UiThread
    void showClearCacheToast() {
        calculateCacheSize();
        showToast("清除成功");
    }

    @Click(R.id.imageSwitch)
    void imageSwitch() {
        ConfigManager.setImageSwitch(imageSwitch.isChecked());
    }

    @Click(R.id.notificationSwitch)
    void notificationSwitch() {
        ConfigManager.setNotificationSwitch(notificationSwitch.isChecked());
    }

    @Click(R.id.saleReminderSwitch)
    void saleReminderSwitch() {
        ConfigManager.setImageSwitch(saleReminderSwitch.isChecked());
    }

    @Click(R.id.versionLayout)
    void updateVersion() {
        showToast("已是最新版");
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
