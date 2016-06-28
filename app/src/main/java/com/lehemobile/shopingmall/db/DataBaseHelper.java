package com.lehemobile.shopingmall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by tanyq on 14-11-24.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "mall.db";
    public static final int VERSION_1_0_1 = 3;
    public static final int DB_VERSION = VERSION_1_0_1; //

    private static DataBaseHelper mDataBaseHelper = null;

    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static DataBaseHelper getInstance(Context context) {
        if (mDataBaseHelper == null) {
            synchronized (DataBaseHelper.class) {
                mDataBaseHelper = new DataBaseHelper(context);
            }
        }
        return mDataBaseHelper;
    }

    /**
     * @param context
     * @return
     */
    public synchronized static SQLiteDatabase getWritableDatabase(Context context) {
        return getInstance(context).getWritableDatabase();
    }

    /**
     * @param context
     * @return
     */
    public synchronized static SQLiteDatabase getReadableDatabase(Context context) {
        return getInstance(context).getReadableDatabase();
    }

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TODO 创建数据库
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO 升级数据库
    }
}
