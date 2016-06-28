package com.tgh.devkit.core.utils;

import com.tgh.devkit.core.encrypt.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化和反序列化对象
 * Created by albert on 16/1/15.
 */
public class Serialize {

    public static String toString(Serializable o) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(o);
            so.flush();
            return Base64.encode(bo.toByteArray());
        } catch (Exception e) {
            DebugLog.e(e.getMessage(),e);
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T fromString(String s, Class<T> tClass) {
        try {
            byte b[] = Base64.decode(s.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            Object o = si.readObject();
            return tClass.cast(o);
        } catch (Exception e) {
            DebugLog.e(e.getMessage(), e);
            return null;
        }
    }
}
