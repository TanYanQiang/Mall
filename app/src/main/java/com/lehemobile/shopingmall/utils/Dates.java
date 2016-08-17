package com.lehemobile.shopingmall.utils;

import android.content.Context;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by tanyq on 17/8/16.
 */
public class Dates {
    public static String getDate(long millis) {
        DateTime lastDate = DateTime.forInstant(millis * 1000, TimeZone.getDefault());
        return lastDate.format("YYYY-MM-DD");
    }
}
