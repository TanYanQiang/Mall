package com.tgh.devkit.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by albert on 4/27/15.
 */
public abstract class WeakRefHandler<T> extends Handler {
    private final WeakReference<T> wr;
    public WeakRefHandler(T t) {
        this.wr = new WeakReference<>(t);
    }

    public WeakRefHandler(T t,Looper looper) {
        super(looper);
        this.wr = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        T t = wr.get();
        if (t == null){
            return;
        }
        handleMessage(t,msg);
    }

    protected abstract void handleMessage(T t, Message msg);
}
