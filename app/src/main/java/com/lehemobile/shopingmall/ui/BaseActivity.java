package com.lehemobile.shopingmall.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lehemobile.shopingmall.utils.DialogUtils;

/**
 * Created by tanyq on 28/6/16.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mLoadingDialog;

    public void showLoading(String message) {
        showLoading(message, null);
    }

    public void showLoading(@StringRes int message) {
        showLoading(getString(message), null);
    }

    public void showLoading(@StringRes int message, DialogInterface.OnCancelListener cancelListener) {
        showLoading(getString(message), cancelListener);
    }

    public void showLoading(String message, DialogInterface.OnCancelListener cancelListener) {
        if (isFinishing()) return;
        mLoadingDialog = ProgressDialog.show(this, null, message, true, true, cancelListener);
    }

    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void showToast(@StringRes int message) {
        showToast(getString(message));
    }

    public void showToast(String message) {
        if (TextUtils.isEmpty(message)) return;
        if (isFinishing()) return;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void confirm(String title, String message, View.OnClickListener rightClickListener) {
        DialogUtils.alert(this, title, message, android.R.string.cancel, null, android.R.string.ok, rightClickListener);
    }

    public void complain(String message) {
        DialogUtils.alert(this, message);
    }

    public void complani(@StringRes int message) {
        DialogUtils.alert(this, message);
    }

    @Override
    protected void onDestroy() {
        dismissLoading();
        super.onDestroy();
    }
}
