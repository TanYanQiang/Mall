package com.lehemobile.shopingmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by  on 21/7/16.
 */
public class Order implements Serializable {

    public static final int STATUS_INIT = 0; //订单初始化
    public static final int STATUS_WATING_PAY = 1; //待付款
    public static final int STATUS_PAID = 2; //支付完成
    public static final int STATUS_WATING_DELIVER_GOODS = 3; //待发货  Deliver goods
    public static final int STATUS_DELIVER_GOODS_SUCCESS = 4; //发货完成
    public static final int STATUS_WATING_RECEIPT_GOODS = 5; //待收货  receipt
    public static final int STATUS_RECEIPT_GOODS_SUCCESS = 6; //确认收货  receipt
    public static final int STATUS_COMPLETED = 7;//完成
    public static final int STATUS_CANCELED = 8;//已取消


    private int id;  //订单ID
    private String orderNumber; //订单编号
    private int status;  //订单状态
    private String statusDesc;  //状态描述

    private int count; //商品数量
    private double totalPrice; //总价格

    private List<Goods> goodsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
