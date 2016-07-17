package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by   on 14-11-29.
 */
public class Discount implements Serializable {

    private String title;
    private String key;
    private double value = 0;
    private long expire;
    private int type;
    private int id;

    private String desc;
    private boolean isExpire;

    /**
     * 范围描述
     */
    private String rangeDesc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean isExpire) {
        this.isExpire = isExpire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRangeDesc() {
        return rangeDesc;
    }

    public void setRangeDesc(String rangeDesc) {
        this.rangeDesc = rangeDesc;
    }
}
