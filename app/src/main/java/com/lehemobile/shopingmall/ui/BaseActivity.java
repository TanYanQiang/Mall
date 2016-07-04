package com.lehemobile.shopingmall.ui;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lehemobile.shopingmall.utils.DialogUtils;

/**
 * Created by tanyq on 28/6/16.
 */
public class BaseActivity extends AppCompatActivity {

    public void confirm(String title, String message, View.OnClickListener rightClickListener) {
        DialogUtils.alert(this, title, message, android.R.string.cancel, null, android.R.string.ok, rightClickListener);
    }

    public void complain(String message) {
        DialogUtils.alert(this, message);
    }

    public void complani(@StringRes int message) {
        DialogUtils.alert(this, message);
    }
}
