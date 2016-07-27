package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.Goods;

/**
 * Created by tanyq on 25/7/16.
 */
public class GoodsDetailSession {
    private Goods goods;
    private boolean isFavorite;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
