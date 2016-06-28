package com.lehemobile.shopingmall.db;

import android.content.ContentValues;
import android.database.Cursor;


/**
 * Created by tanyq on 14-11-24.
 */
public interface DataBaseBuilder<T> {
    /**
     * Creates object out of cursor
     *
     * @param cursor
     * @return
     */
    public T build(Cursor cursor);

    public ContentValues deconstruct(T t);
}
