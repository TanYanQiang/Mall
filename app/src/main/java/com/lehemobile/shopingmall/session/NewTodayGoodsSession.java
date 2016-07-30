package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.Goods;

import java.util.List;

/**
 * Created by tanyq on 30/7/16.
 */
public class NewTodayGoodsSession {

    private List<Goods> bannersData;
    private List<Goods> newData;

    public List<Goods> getBannersData() {
        return bannersData;
    }

    public void setBannersData(List<Goods> bannersData) {
        this.bannersData = bannersData;
    }

    public List<Goods> getNewData() {
        return newData;
    }

    public void setNewData(List<Goods> newData) {
        this.newData = newData;
    }
}
