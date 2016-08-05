package com.lehemobile.shopingmall.session;

import com.lehemobile.shopingmall.model.Goods;

import java.util.List;

/**
 * Created by tanyq on 25/7/16.
 */
public class GoodsDetailSession {
    private Goods goods;
    private boolean isFavorite;
    private List<Goods> recommendGoods;

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

    public List<Goods> getRecommendGoods() {
        return recommendGoods;
    }

    public void setRecommendGoods(List<Goods> recommendGoods) {
        this.recommendGoods = recommendGoods;
    }
}
