package com.lehemobile.shopingmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyq on 5/8/16.
 */
public class Kuaidi implements Serializable {
    private String number; //编号
    private String status; //状态
    private String lastUpdateTime; //最近更新时间
    private List<KuaidiItem> items;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<KuaidiItem> getItems() {
        return items;
    }

    public void setItems(List<KuaidiItem> items) {
        this.items = items;
    }
}
