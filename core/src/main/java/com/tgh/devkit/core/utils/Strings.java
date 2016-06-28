package com.tgh.devkit.core.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by albert on 16/1/8.
 */
public class Strings {
    public static final String EMPTY = "";
    private Strings() {}

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String doubleTrans(double d) {
        DecimalFormat df = new DecimalFormat("0.##");//最多保留几位小数，就用几个#，最少位就用0来确定
        return df.format(d);
    }
    public static String doubleTrans(double d, int maxDigits) {
        //第二种方法:
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(maxDigits);  //最大小数位数
        return numberFormat.format(d);
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null){
            return true;
        }
        string = string.trim();
        return string.length() == 0 || "null".equalsIgnoreCase(string);
    }

    public static String emptyToNull(String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    public static String nullToEmpty(String string) {
        return isNullOrEmpty(string) ? EMPTY : string;
    }

    public static String extract(String info,String regularExpression){
        if (regularExpression == null){
            return null;
        }

        Matcher matcher = Pattern.compile(regularExpression).matcher(info);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    public static String[] extractAll(String info,String regularExpression){
        if (regularExpression == null){
            return new String[]{};
        }

        Matcher matcher = Pattern.compile(regularExpression).matcher(info);
        ArrayList<String> temp = new ArrayList<>();
        while (matcher.find()){
            temp.add(matcher.group());
        }
        return temp.toArray(new String[temp.size()]);
    }


    public static int[] findFirst(String info, String regularExpression){
        if (regularExpression == null){
            return null;
        }
        Matcher matcher = Pattern.compile(regularExpression).matcher(info);
        if (matcher.find()){
            int[] ints = new int[2];
            ints[0] = matcher.start();
            ints[1] = matcher.end();
            return ints;
        }else {
            return null;
        }
    }

    public static int[] findFirst(String info,int offset, CharSequence query){
        if (query==null){
            return null;
        }
        int startIndex = info.indexOf(query.toString(), offset);
        if (startIndex == -1){
            return null;
        }
        int endIndex = startIndex+query.length();
        return new int[]{startIndex,endIndex};
    }


    public static LinkedList<int[]> findAll(String info, String regularExpression){
        if (regularExpression == null){
            return new LinkedList<>();
        }
        Matcher matcher = Pattern.compile(regularExpression).matcher(info);
        LinkedList<int[]> result = new LinkedList<>();
        while (matcher.find()){
            int[] ints = new int[2];
            ints[0] = matcher.start();
            ints[1] = matcher.end();
            result.add(ints);
        }
        return result;
    }


    public static LinkedList<int[]> findAll(String info,CharSequence query){
        int index = 0;
        LinkedList<int[]> stack = new LinkedList<>();
        while (true){
            int[] ints = findFirst(info,index,query);
            if (ints == null){
                break;
            }
            index = ints[1];
            stack.addLast(ints);
        }
        return stack;
    }

    public static String insertBefore(String info, String beInsertedStr, String newInsertStr){
        int index = info.indexOf(beInsertedStr);
        if (index == -1){
            return info;
        }
        return info.substring(0, index)+newInsertStr+info.substring(index,info.length());
    }

    public static String insertBeforeAll(String info, String beInsertedStr, String newInsertStr){
        String[] split = info.split(beInsertedStr);
        if (split.length == 0){
            return info;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<split.length;i++){
            if (i!=split.length-1){
                sb.append(split[i]).append(newInsertStr).append(beInsertedStr);
            }else {
                sb.append(split[i]);
            }
        }
        if (info.endsWith(beInsertedStr)){
            sb.append(newInsertStr).append(beInsertedStr);
        }

        return sb.toString();
    }

    public static String insertAfter(String info, String beInsertedStr, String newInsertStr){
        int index = info.indexOf(beInsertedStr);
        if (index == -1){
            return info;
        }
        index = index + beInsertedStr.length();
        return info.substring(0, index)+newInsertStr+info.substring(index,info.length());
    }

    public static String insertAfterAll(String info, String beInsertedStr, String newInsertStr){
        String[] split = info.split(beInsertedStr);
        if (split.length == 0){
            return info;
        }

        StringBuilder sb = new StringBuilder();
        for(int i=0;i<split.length;i++){
            if (i!=split.length-1){
                sb.append(split[i]).append(beInsertedStr).append(newInsertStr);
            }else {
                sb.append(split[i]);
            }
        }

        if (info.endsWith(beInsertedStr)){
            sb.append(beInsertedStr).append(newInsertStr);
        }

        return sb.toString();
    }


    private static final SecureRandom secureRandom = new SecureRandom();

    public static String randomString(){
        return new BigInteger(130, secureRandom).toString(32);
    }


    public static String join(String separator, String ... array) {
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

}
