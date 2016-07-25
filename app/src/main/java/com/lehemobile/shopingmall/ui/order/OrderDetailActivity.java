package com.lehemobile.shopingmall.ui.order;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by tanyq on 24/7/16.
 */
@EActivity(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
    @Extra
    Order order;
}
