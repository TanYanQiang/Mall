package com.lehemobile.shopingmall.ui.order;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.CropSquareTransformation;

/**
 * Created by tanyq on 22/7/16.
 */
@EViewGroup(R.layout.view_order_list_item)
public class OrderListItemView extends LinearLayout {

    @ViewById
    TextView orderNumber;
    @ViewById
    TextView orderStatus;
    @ViewById
    ImageView orderThumb;
    @ViewById
    TextView goodsPrice;
    @ViewById
    TextView goodsName;
    @ViewById
    TextView count;
    @ViewById
    TextView orderCountPrice;

    @ViewById
    View orderAction;

    @ViewById
    Button buttonLeft;
    @ViewById
    Button buttonRight;

    private Order order;

    public OrderListItemView(Context context) {
        super(context);
    }

    public void bindData(Order order) {
        if (order == null) return;
        this.order = order;

        orderNumber.setText(getResources().getString(R.string.label_order_number, order.getOrderNumber()));

        goodsName.setText(order.getGoods().getName());
        goodsPrice.setText("￥" + order.getGoods().getPrice());
        count.setText("x" + order.getCount());
        Glide.with(getContext()).load(order.getGoods().getThumbnail())
                .bitmapTransform(new CropSquareTransformation(getContext()))
                .into(orderThumb);
        orderCountPrice.setText(getResources().getString(R.string.label_order_count_price, order.getCount(), order.getTotalPrice()));

        orderStatus.setText(order.getStatusDesc());

        updateOrderAction();
    }

    private void updateOrderAction() {

        switch (order.getStatus()) {
            case Order.STATUS_WATING_PAY: //待付款
                orderAction.setVisibility(VISIBLE);

                buttonLeft.setText("取消订单");
                buttonLeft.setVisibility(VISIBLE);
                buttonRight.setText("待付款");
                buttonRight.setVisibility(VISIBLE);
                break;
            case Order.STATUS_WATING_RECEIPT_GOODS: //待收货
                orderAction.setVisibility(VISIBLE);
                buttonLeft.setText("查看物流");
                buttonLeft.setVisibility(VISIBLE);
                buttonRight.setText("确认收货");
                buttonRight.setVisibility(VISIBLE);
                break;
            case Order.STATUS_COMPLETED: //待收货
                orderAction.setVisibility(VISIBLE);
                buttonLeft.setVisibility(GONE);

                buttonRight.setText("申请退款");
                buttonRight.setVisibility(VISIBLE);
                break;
            default:
                orderAction.setVisibility(GONE);
                break;
        }

    }

    @Click
    void buttonLeft() {
        switch (order.getStatus()) {
            case Order.STATUS_WATING_PAY: //待付款
                cancelOrder();
                break;
            case Order.STATUS_WATING_RECEIPT_GOODS: //待收货
                showKuaidi();
                break;
        }
    }

    @Click
    void buttonRight() {
        switch (order.getStatus()) {
            case Order.STATUS_WATING_PAY: //待付款
                goPay();
                break;
            case Order.STATUS_WATING_RECEIPT_GOODS: //待收货
                confirmReceipt();
                break;
            case Order.STATUS_COMPLETED:
                applyForRefund();
                break;
        }
    }

    private void cancelOrder() {
        //TODO 取消订单
        Logger.i("取消订单");
    }

    private void showKuaidi() {
        //TODO 查看物流
        Logger.i("查看物流");
    }

    private void goPay() {
        //TODO 去付款
    }

    private void confirmReceipt() {
        //TODO 确认收货
    }

    private void applyForRefund() {
        //TODO 申请退款
    }
}
