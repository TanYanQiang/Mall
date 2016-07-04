package com.lehemobile.shopingmall.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lehemobile.shopingmall.utils.DialogUtils;

/**
 * Created by tanyq on 4/7/16.
 */
public class BaseFragment extends Fragment {

    public boolean isActive() {
        return getActivity() != null && !isDetached() && isAdded();
    }


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
        if (isActive()) return;
        mLoadingDialog = ProgressDialog.show(getActivity(), null, message, true, true, cancelListener);
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
        if (isActive()) return;
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    public void confirm(String title, String message, View.OnClickListener rightClickListener) {
        if (isActive()) return;
        DialogUtils.alert(getActivity(), title, message, android.R.string.cancel, null, android.R.string.ok, rightClickListener);
    }

    public void complain(String message) {
        if (isActive()) return;
        DialogUtils.alert(getActivity(), message);
    }

    public void complain(@StringRes int message) {
        if (isActive()) return;
        DialogUtils.alert(getActivity(), message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }
}
