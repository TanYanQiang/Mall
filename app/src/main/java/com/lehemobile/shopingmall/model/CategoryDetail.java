package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by tanyq on 4/8/16.
 */
public class CategoryDetail implements Serializable {
    private Category category;
    private int id;
    private String name;
    private String imageUrl;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
