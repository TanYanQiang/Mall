package com.lehemobile.shopingmall.model;

import java.util.List;

/**
 * Created by tanyq on 4/8/16.
 */
public class CategoryDetailSession {
    private String tips;
    private List<CategoryDetail> details;

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<CategoryDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CategoryDetail> details) {
        this.details = details;
    }
}
