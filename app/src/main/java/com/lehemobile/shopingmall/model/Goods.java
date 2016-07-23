package com.lehemobile.shopingmall.model;

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
    private double price; //价格

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

    public void setPrice(double price) {
        this.price = price;
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
