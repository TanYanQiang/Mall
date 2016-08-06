package com.lehemobile.shopingmall.ui.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.shoppingCart.ShoppingCartActivity_;
import com.lehemobile.shopingmall.ui.user.AccountActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;

/**
 * Created by tanyq on 30/7/16.
 */
@EViewGroup(R.layout.view_user_shopping)
public class UserShoppingView extends LinearLayout {


    public UserShoppingView(Context context) {
        super(context);
    }

    public UserShoppingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserShoppingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserShoppingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Click(R.id.person)
    void goAccount() {
        AccountActivity_.intent(getContext()).start();
    }

    @Click(R.id.shopping)
    void goShoppingCart() {
        //TODO 去购物车
        ShoppingCartActivity_.intent(getContext()).start();
    }
}
