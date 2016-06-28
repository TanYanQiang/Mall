package com.tgh.devkit.core.primitives;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Random;
/**
 *
 * Created by albert on 4/16/15.
 */
public class Ints {
    
    private Ints(){}

    public static String join(String separator, int... array) {
        if (separator == null ){
            separator = " ";
        }
        if (array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            Object o = boxedArray[i];
            if (o==null){
                continue;
            }
            array[i] = ((Number) o).intValue();
        }
        return array;
    }

    /**
     * 返回[min,max]之间的随机数，包含min和max
     */
    public static int randInt(int min, int max, Random rand) {
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * child是否在parent之中，注意，child和parent的长度都为2
     * @return
     */
    public static boolean isIn(int[] child,int[] parent){
        if (child == null || parent ==null || child.length!=2
                || parent.length!=2){
            return false;
        }

        return child[0]>=parent[0] && child[1] <= parent[1];
    }

    /**
     * 将类似"1,2,3,4,5"这样的字符串转换为int数组
     * @param splitter 分隔符
     */
    public static int[] split(String str,String splitter){
        if (!TextUtils.isEmpty(str) && str.contains(splitter)){
            String[] split = str.split(splitter);
            int[] results = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                results[i] = Integer.parseInt(split[i]);
            }
            return results;
        }
        return null;
    }
}
