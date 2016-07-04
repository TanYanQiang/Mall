package com.lehemobile.shopingmall.ui.common;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

/**
 * Created by tanyq on 28/6/16.
 */
@EViewGroup(R.layout.v_dialog)
public class CommDialogView extends FrameLayout {
    public CommDialogView(Context context) {
        super(context);
    }


    @ViewById
    TextView tv_title;
    @ViewById
    TextView tv_message;
    @ViewById
    TextView tv_left;
    @ViewById
    TextView tv_right;
    @ViewById
    TextView tv_center;

    @ViewById
    View innerSpace;


    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;
    private View.OnClickListener centerListener;

    public CommDialogView setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(GONE);
            innerSpace.setVisibility(GONE);
        } else {
            tv_title.setVisibility(VISIBLE);
            innerSpace.setVisibility(VISIBLE);
            tv_title.setText(title);
        }
        return this;
    }

    public CommDialogView setTitle(@StringRes int title) {
        if (title == 0) {
            return setTitle(null);
        } else {
            return setTitle(getResources().getString(title));
        }

    }

    public CommDialogView setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            tv_message.setVisibility(GONE);
            innerSpace.setVisibility(GONE);
        } else {
            tv_message.setVisibility(VISIBLE);
            innerSpace.setVisibility(VISIBLE);
            tv_message.setText(message);
        }
        return this;
    }

    public CommDialogView setMessage(@StringRes int message) {
        if (message == 0) {
            return setMessage(null);
        } else {
            return setMessage(getResources().getString(message));
        }

    }

    public CommDialogView setLeftButton(String text, OnClickListener listener) {
        tv_left.setText(text);
        this.leftListener = listener;
        return this;
    }

    public CommDialogView setLeftButton(@StringRes int text, OnClickListener listener) {
        return setLeftButton(getResources().getString(text), listener);
    }

    public CommDialogView setLeftButtonVisible(boolean visible) {
        tv_left.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }

    public CommDialogView setRightButton(String text, OnClickListener listener) {
        tv_right.setText(text);
        this.rightListener = listener;
        return this;
    }


    public CommDialogView setRightButton(@StringRes int text, OnClickListener listener) {
        return setRightButton(getResources().getString(text), listener);
    }

    public CommDialogView setRightButtonVisible(boolean visible) {
        tv_right.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }

    public CommDialogView setCenterButton(String text, OnClickListener listener) {
        tv_center.setText(text);
        this.centerListener = listener;
        return this;
    }

    public CommDialogView setCenterButton(@StringRes int text, OnClickListener listener) {
        return setCenterButton(getResources().getString(text), listener);
    }

    public CommDialogView setCenterButtonVisible(boolean visible) {
        tv_center.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }

    @Click
    void tv_left() {
        if (leftListener != null) {
            leftListener.onClick(tv_left);
        }
    }

    @Click
    void tv_right() {
        Logger.i("right");
        if (rightListener != null) {
            rightListener.onClick(tv_right);
        }
    }

    @Click
    void tv_center() {
        if (centerListener != null) {
            centerListener.onClick(tv_center);
        }
    }

}
