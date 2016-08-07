package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.Address;
import com.lehemobile.shopingmall.model.Order;

/**
 * Created by tanyq on 7/8/16.
 */
public class ConfirmOrderSession {
    private Address address;
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
