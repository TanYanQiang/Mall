package com.lehemobile.shopingmall.event;

import com.lehemobile.shopingmall.model.Goods;

/**
 * Created by tanyq on 27/7/16.
 */
public class FavoriteEvent {
    private boolean isFavorite;
    private Goods goods;

    public FavoriteEvent(boolean isFavorite, Goods goods) {
        this.isFavorite = isFavorite;
        this.goods = goods;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
