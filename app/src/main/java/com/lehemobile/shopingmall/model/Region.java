package com.lehemobile.shopingmall.model;

import android.text.TextUtils;

import com.lehemobile.shopingmall.utils.PinyinUtils;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by tanyq on 4/8/16.
 */
public class Region implements Serializable {
    private int id;
    private String name;
    private String pinyin;

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
        if (!TextUtils.isEmpty(name)) {
            pinyin  = PinyinUtils.getPingYin(name);

        }
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
