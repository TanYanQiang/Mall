package com.lehemobile.shopingmall.ui.order;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 物流信息
 * Created by  on 22/7/16.
 */
@EActivity(R.layout.activity_order_kuaidi)
public class OrderKuaidiActivity extends BaseActivity {
    @Extra
    Order order;

    @ViewById
    TextView kuaidiNumber;
    @ViewById
    TextView kuaidiStatus;
    @ViewById
    TextView orderNumber;

    @ViewById
    OrderGoodsInfo orderGoodsInfo;

    @ViewById
    LinearLayout kuaidiInfo;

    private void loadData() {
        //TODO 通过接口获取快递信息
    }

    @AfterViews
    void init() {
        if (order == null) return;

        orderGoodsInfo.bindData(order);
        orderNumber.setText(getString(R.string.label_order_number, order.getOrderNumber()));
    }

}
