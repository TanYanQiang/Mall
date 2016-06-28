package com.lehemobile.shopingmall.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tanyq on 28/6/16.
 */
public class UserDao extends BaseDao {

    protected static void onCreateTable(SQLiteDatabase db) {

    }

    protected static void onUpgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* //test
        if (oldVersion < 2) {  //老版本
            //execSQL
        }
        if (oldVersion < 3) {
            //execSql
        }*/
    }
}
