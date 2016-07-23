package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by tanyq on 23/7/16.
 */
public class Province implements Serializable {
    private int id;
    private String name;

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
}
