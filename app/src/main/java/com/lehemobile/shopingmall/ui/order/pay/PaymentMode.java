package com.lehemobile.shopingmall.ui.order.pay;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by tanyq on 14-12-2.
 */
public class PaymentMode implements Serializable {

    public static final int TYPE_ALIPAY = 3;
    public static final int TYPE_WEIXINPAY = 2;
    public static final int TYPE_YEEPAY = 4;
    public static final int TYPE_YEEPAY_CREDIT = 5;
    public static final int TYPE_BALANCEPAY = 1;
    public static final int TYPE_BAIDUPAY = 6;

    private int type; // 2 支付宝 1:微信支付 3 易宝支付(储蓄卡支付) 4 易宝支付(信用卡支付) 0 余额支付
    private String title;
    private double balance; //余额，余额支付用
    private double discount;
    private double reduction;
    private String value;
    private double minAmt;// 参与优惠的最新金额

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDesc() {
        if (type == TYPE_BALANCEPAY) {
            return title + "(剩￥" + balance + ")";
        }
        return title;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(double minAmt) {
        this.minAmt = minAmt;
    }

    public double calculationAmt(double amt) {
        if (TextUtils.isEmpty(value)) return amt;
        if (amt < minAmt) return amt;
        if (discount != 0) {
            return amt * discount;
        }
        if (reduction != 0) {
            return amt - reduction;
        }

        return amt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentMode)) return false;

        PaymentMode that = (PaymentMode) o;

        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
