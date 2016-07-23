package com.lehemobile.shopingmall.event;

import com.lehemobile.shopingmall.model.Goods;

/**
 * Created by tanyq on 23/7/16.
 */
public class CancelFavoriteEvent {
    private Goods goods;

    public CancelFavoriteEvent(Goods goods) {
        this.goods = goods;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
