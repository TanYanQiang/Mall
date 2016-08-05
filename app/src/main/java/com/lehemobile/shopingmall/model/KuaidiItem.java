package com.lehemobile.shopingmall.model;

import java.io.Serializable;

/**
 * Created by tanyq on 5/8/16.
 */
public class KuaidiItem implements Serializable {
    private String time;
    private String desc;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
