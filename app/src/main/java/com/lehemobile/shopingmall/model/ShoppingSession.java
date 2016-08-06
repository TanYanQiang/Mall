package com.lehemobile.shopingmall.model;

/**
 * Created by tanyq on 6/8/16.
 */
public class ShoppingSession {
    private Goods goods;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingSession)) return false;

        ShoppingSession that = (ShoppingSession) o;

        if (isSelected != that.isSelected) return false;
        return goods.equals(that.goods);

    }

    @Override
    public int hashCode() {
        return goods.hashCode();
    }
}
