package com.lehemobile.shopingmall.utils;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;

import com.lehemobile.shopingmall.ui.common.CommDialogView;
import com.lehemobile.shopingmall.ui.common.CommDialogView_;
import com.tgh.devkit.dialog.DialogBuilder;

/**
 * Created by tanyq on 28/6/16.
 */
public class DialogUtils {

    private static Dialog dialog;

    public static Dialog alert(
            Activity activity,
            @StringRes int title, @StringRes int message,
            @StringRes int leftString, View.OnClickListener leftListener,
            @StringRes int rightString, View.OnClickListener rightListener) {
        String st = title == 0 ? null : activity.getString(title);
        String sm = message == 0 ? null : activity.getString(message);
        return alert(activity, st, sm, leftString, leftListener, rightString, rightListener);

    }
    public static Dialog alert(
            Activity activity,
            String message,
            @StringRes int centerRes, final View.OnClickListener centerListener){
        return alert(activity,0,message,centerRes,centerListener);
    }
    public static Dialog alert(Activity activity,
                               String title, View customeView,
                               @StringRes int leftString, final View.OnClickListener leftListener,
                               @StringRes int rightString, final View.OnClickListener rightListener) {

        CommDialogView view = CommDialogView_.build(activity)
                .setTitle(title).setCustomeView(customeView)
                .setLeftButton(leftString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (leftListener != null) {
                            leftListener.onClick(view);
                        }
                    }
                })
                .setRightButton(rightString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (rightListener != null) {
                            rightListener.onClick(view);
                        }
                    }
                });
        innerShow(activity, view);
        return dialog;
    }

    public static Dialog alert(Activity activity,
                               String title, String message,
                               @StringRes int leftString, final View.OnClickListener leftListener,
                               @StringRes int rightString, final View.OnClickListener rightListener) {

        CommDialogView view = CommDialogView_.build(activity)
                .setTitle(title).setMessage(message)
                .setLeftButton(leftString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (leftListener != null) {
                            leftListener.onClick(view);
                        }
                    }
                })
                .setRightButton(rightString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (rightListener != null) {
                            rightListener.onClick(view);
                        }
                    }
                });
        innerShow(activity, view);
        return dialog;
    }

    public static Dialog alert(
            Activity activity,
            @StringRes int title,
            @StringRes int message,
            @StringRes int centerRes,
            final View.OnClickListener centerListener) {
        String str = message == 0 ? null : activity.getString(message);
        return alert(activity, title, str, centerRes, centerListener);
    }

    public static Dialog alert(
            Activity activity,
            @StringRes int title,
            String message,
            @StringRes int centerRes,
            final View.OnClickListener centerListener) {

        CommDialogView commDialogView = CommDialogView_.build(activity)
                .setTitle(title).setMessage(message)
                .setLeftButtonVisible(false).setRightButtonVisible(false).setCenterButtonVisible(true)
                .setCenterButton(centerRes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (centerListener != null) {
                            centerListener.onClick(view);
                        }
                    }
                });
        innerShow(activity, commDialogView);
        return dialog;
    }

    public static Dialog alert(Activity activity, @StringRes int message) {
        return alert(activity, 0, message, android.R.string.ok, null);
    }

    public static Dialog alert(Activity activity, @StringRes int title, @StringRes int message) {
        return alert(activity, title, message, android.R.string.ok, null);
    }

    public static Dialog alert(Activity activity, String message) {
        return alert(activity, 0, message, android.R.string.ok, null);
    }

    private static void innerShow(Activity activity, View view) {
        dialog = new DialogBuilder(activity)
                .cancelable(false)
                .gravity(Gravity.CENTER)
                .contentView(view).build();
        dialog.show();
    }

}
