package com.lehemobile.shopingmall.ui.order;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.view.OrderGoodsInfo;
import com.lehemobile.shopingmall.utils.DialogUtils;
import com.orhanobut.logger.Logger;
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

/**
 * Created by tanyq on 22/7/16.
 */
@EViewGroup(R.layout.view_order_list_item)
public class OrderListItemView extends LinearLayout {

    @ViewById
    TextView orderNumber;
    @ViewById
    TextView orderStatus;

    @ViewById(R.id.orderGoodsInfo)
    OrderGoodsInfo orderGoodsInfo;

    @ViewById
    TextView orderCountPrice;

    @ViewById
    View orderAction;

    @ViewById
    TextView buttonLeft;
    @ViewById
    TextView buttonRight;

    private Order order;

    public OrderListItemView(Context context) {
        super(context);
    }

    public void bindData(Order order) {
        if (order == null) return;
        this.order = order;

        orderNumber.setText(getResources().getString(R.string.label_order_number, order.getOrderNumber()));

        orderGoodsInfo.bindData(order);

        String price = Strings.doubleTrans(order.getTotalPrice());
        String info = getResources().getString(R.string.label_order_count_price, order.getCount(), price);
        new SpannableStringHelper(info)
                .relativeSize("￥" + price, 1.3f)
                .foregroundColor("￥" + price, getResources().getColor(R.color.text_color_lv1))
                .attachToTextView(orderCountPrice);


        orderStatus.setText(order.getStatusDesc());

        updateOrderAction();
    }

    private void updateOrderAction() {

        switch (order.getStatus()) {
            case Order.STATUS_WATING_PAY: //待付款
                orderAction.setVisibility(VISIBLE);
                buttonLeft.setText("关闭订单");
                buttonLeft.setTextAppearance(getContext(), R.style.Button_GrayEmptyCorners_Cancel);
                buttonLeft.setBackgroundResource(R.drawable.btn_gray_empty_corners_cancel);

                buttonLeft.setVisibility(VISIBLE);
                buttonRight.setText("去付款");
                buttonRight.setVisibility(VISIBLE);
                buttonRight.setTextAppearance(getContext(), R.style.Button_NormalEmptyCorners2);
                buttonRight.setBackgroundResource(R.drawable.btn_normal_empty_corners2);
                break;
            case Order.STATUS_WATING_RECEIPT_GOODS: //待收货
                orderAction.setVisibility(VISIBLE);
                buttonLeft.setText("查看物流");
                buttonLeft.setVisibility(VISIBLE);
                buttonLeft.setTextAppearance(getContext(), R.style.Button_GrayEmptyCorners_Cancel);
                buttonLeft.setBackgroundResource(R.drawable.btn_gray_empty_corners_cancel);
                buttonRight.setText("确认收货");
                buttonRight.setVisibility(VISIBLE);
                buttonRight.setTextAppearance(getContext(), R.style.Button_NormalEmptyCorners);
                buttonRight.setBackgroundResource(R.drawable.btn_normal_empty_corners);
                break;
            case Order.STATUS_COMPLETED: //待收货
                orderAction.setVisibility(VISIBLE);
                buttonLeft.setVisibility(GONE);

                buttonRight.setText("申请退款");
                buttonRight.setVisibility(VISIBLE);
                buttonRight.setTextAppearance(getContext(), R.style.Button_GrayEmptyCorners_Cancel);
                buttonRight.setBackgroundResource(R.drawable.btn_gray_empty_corners_cancel);
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
                DialogUtils.alert((Activity) getContext(),
                        "关闭订单", "确定要关闭该订单？",
                        android.R.string.cancel, null,
                        android.R.string.ok, new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelOrder();
                            }
                        });
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

                DialogUtils.alert((Activity) getContext(),
                        "确认收货", "请您收到货后再点“确定”！",
                        android.R.string.cancel, null,
                        android.R.string.ok, new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirmReceipt();
                            }
                        });
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
        OrderKuaidiActivity_.intent(getContext()).order(order).start();
    }

    private void goPay() {
        //TODO 去付款
        OrderPayActivity_.intent(getContext()).order(order).start();
    }

    private void confirmReceipt() {
        //TODO 确认收货
    }

    private void applyForRefund() {
        //TODO 申请退款
    }
}
