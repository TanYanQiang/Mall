package com.tgh.devkit.core.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tgh.devkit.core.utils.Serialize;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.Utils;

import java.io.Serializable;

/**
 * SharedPreferences Utils
 * Created by albert on 16/1/12.
 */
public class KeyValueStoreInternal {

    public static final String DEFAULT_SP = "defaultSp";
    private final SharedPreferences sp;
    private Context context;
    private boolean bindWithVersion;
    private String configFile;
    private String suffix;
    private Editor edit;

    @SuppressLint("CommitPrefEdits")
    KeyValueStoreInternal(Context context,String config,boolean bindWithVersion) {
        this.context = context.getApplicationContext();
        this.configFile = config;
        this.bindWithVersion = bindWithVersion;
        int appVersion = Utils.getAppVersion(context);
        suffix = "_" + appVersion;
        sp = getSharedPreferences();
        edit = sp.edit();
    }

    public void setBindWithVersion(boolean bindWithVersion) {
        this.bindWithVersion = bindWithVersion;
    }

    public void save(String key, int value) {
        edit.putInt(getKey(key), value);
        edit.apply();
    }

    public void save(String key, long value) {
        edit.putLong(getKey(key), value);
        edit.apply();
    }

    public void save(String key, float value) {
        edit.putFloat(getKey(key), value);
        edit.apply();
    }

    public void save(String key, double value) {
        edit.putFloat(getKey(key), (float) value);
        edit.apply();
    }

    public void save(String key, String value) {
        edit.putString(getKey(key), value);
        edit.apply();
    }

    public void save(String key, boolean value) {
        edit.putBoolean(getKey(key), value);
        edit.apply();
    }

    public void saveObject(String key,Serializable obj){
        save(key, Serialize.toString(obj));
    }

    public int getInt(String key){
        return getInt(key, 0);
    }

    public int getInt(String key,int defaultValue){
        return sp.getInt(getKey(key), defaultValue);
    }

    public long getLong(String key){
        return getLong(key, 0l);
    }

    public long getLong(String key,long defaultValue){
        return sp.getLong(getKey(key), defaultValue);
    }

    public float getFloat(String key){
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key,float defaultValue){
        return sp.getFloat(getKey(key), defaultValue);
    }

    public double getDouble(String key){
        return getDouble(key, 0.0);
    }

    public double getDouble(String key,double defaultValue){
        return sp.getFloat(getKey(key), (float) defaultValue);
    }

    public String getString(String key){
        return getString(key, null);
    }

    public String getString(String key,String defaultValue){
        return sp.getString(getKey(key), defaultValue);
    }

    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key,boolean defaultValue){
        return sp.getBoolean(getKey(key), defaultValue);
    }

    public void remove(String key){
        edit.remove(getKey(key));
        edit.apply();
    }

    public <T extends Serializable> T getObject(String key,Class<T> clazz){
        try {
            String string = getString(key);
            if (Strings.isNullOrEmpty(string)) {
                return null;
            }
            return Serialize.fromString(string, clazz);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void clear(){
        edit.clear();
        edit.apply();
    }

    private String getKey(String key) {
        if (bindWithVersion){
            return key + suffix;
        }else {
            return key;
        }
    }

    private SharedPreferences getSharedPreferences() {
        if (Strings.isNullOrEmpty(configFile)){
            configFile = DEFAULT_SP;
        }
        return context.getSharedPreferences(configFile, Context.MODE_PRIVATE);
    }
}
