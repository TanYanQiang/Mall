package com.lehemobile.shopingmall.ui.order;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Goods;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.MainActivity_;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo_;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by tanyq on 14/8/16.
 */
@EActivity(R.layout.activity_order_pay_complete)
public class OrderPayCompleteActivity extends BaseActivity {

    @Extra
    Order order;
    @Extra
    boolean payStatus;

    @ViewById
    TextView tips;


    @ViewById
    TextView orderNumber;

    @ViewById(R.id.goodsContainer)
    LinearLayout goodsContainer;

    @ViewById
    TextView orderCountPrice;

    @AfterViews
    void init() {
        setTitle(payStatus ? "订单支付成功" : "订单支付失败");
        updateOrderInfo(order);
        updateTips(payStatus);
    }

    private void updateTips(boolean success) {
        String text = success ? "订单支付成功" : "订单支付失败";
        tips.setText(text);
        tips.setCompoundDrawablesWithIntrinsicBounds(0, success ? R.mipmap.ic_success : R.mipmap.ic_fail, 0, 0);
        tips.setTextColor(success ? Color.parseColor("#66cc22") : Color.parseColor("#fa4535"));
    }

    private void updateOrderInfo(Order order) {

        orderNumber.setText(getString(R.string.label_order_number, order.getOrderNumber()));
        updateGoods(order.getGoodsList());

        String price = Strings.doubleTrans(order.getTotalPrice());
        String info = getResources().getString(R.string.label_order_count_price, order.getCount(), price);
        new SpannableStringHelper(info)
                .relativeSize("￥" + price, 1.3f)
                .foregroundColor("￥" + price, getResources().getColor(R.color.text_color_lv1))
                .attachToTextView(orderCountPrice);
    }

    private void updateGoods(List<Goods> goodsList) {
        goodsContainer.removeAllViews();
        for (int i = 0; i < goodsList.size(); i++) {
            OrderGoodsInfo view = OrderGoodsInfo_.build(this);
            view.bindData(goodsList.get(i));
            goodsContainer.addView(view);
        }
    }

    @Click
    void goHome() {
        MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
    }
}
