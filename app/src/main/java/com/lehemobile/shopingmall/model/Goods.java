package com.lehemobile.shopingmall.model;

import com.tgh.devkit.core.utils.Strings;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyq on 21/7/16.
 */
public class Goods implements Serializable {
    private int id;
    private String name;
    private String thumbnail; //缩略图
    private List<String> images; //banner图
    private String detail; //商品描述 使用图片
    private List<String> detailImages;

    private double price; //价格
    private int stock;

    private boolean favorite;
    private int favoriteCount;

    private int buyCount; //购买数量

    private int tradingCount; //销售量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return Strings.doubleTrans(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<String> getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(List<String> detailImages) {
        this.detailImages = detailImages;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getTradingCount() {
        return tradingCount;
    }

    public void setTradingCount(int tradingCount) {
        this.tradingCount = tradingCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goods)) return false;

        Goods goods = (Goods) o;

        return id == goods.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
