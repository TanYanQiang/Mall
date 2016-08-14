package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by tanyq on 4/8/16.
 */
public class Region implements Serializable {
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
