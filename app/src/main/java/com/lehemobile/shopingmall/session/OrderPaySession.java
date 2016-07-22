package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.Order;
import com.lehemobile.shopingmall.ui.order.pay.PaymentMode;

/**
 * 支付订单
 * Created by  on 22/7/16.
 */
public class OrderPaySession {
    private Order order;
    private PaymentMode currentPaymentMode;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentMode getCurrentPaymentMode() {
        return currentPaymentMode;
    }

    public void setCurrentPaymentMode(PaymentMode currentPaymentMode) {
        this.currentPaymentMode = currentPaymentMode;
    }
}
