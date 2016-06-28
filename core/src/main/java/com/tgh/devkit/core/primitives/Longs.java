package com.tgh.devkit.core.primitives;

import java.util.Collection;

/**
 *
 * Created by albert on 4/16/15.
 */
public class Longs {

    private Longs() {
    }

    public static String join(String separator, long... array) {
        if (separator == null){
            separator = " ";
        }
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 10);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }


    public static long[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            Object o = boxedArray[i];
            if (o==null){
                continue;
            }
            array[i] = ((Number) o).longValue();
        }
        return array;
    }

}
