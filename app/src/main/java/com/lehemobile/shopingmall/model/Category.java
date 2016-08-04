package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by tanyq on 4/8/16.
 */
public class Category implements Serializable {
    private int categoryId;
    private String categoryName;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
