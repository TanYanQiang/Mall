package com.lehemobile.shopingmall.utils;

import android.text.TextUtils;

import com.tgh.devkit.core.utils.DebugLog;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据验证工具
 *
 * @author tanyq
 * @version 1.0
 * @company 北京乐和万家信息技术有限公司
 * @date 2013-8-14 下午10:09:34
 */
public class Validation {


    private static char getVerify(String eightcardid) {
        char result = 'a';
        String str = eightcardid;
        int[] num = new int[17];
        for (int i = 0; i < num.length; i++) {
            if (Character.isDigit(str.charAt(i))) {

                num[i] = Integer.parseInt(str.substring(i, i + 1));
            } else {
                return result;
            }
        }
        DebugLog.i("num:" + num);
        int yushu = (num[0] * 7 + num[1] * 9 + num[2] * 10 + num[3] * 5
                + num[4] * 8 + num[5] * 4 + num[6] * 2 + num[7] * 1 + num[8]
                * 6 + num[9] * 3 + num[10] * 7 + num[11] * 9 + num[12] * 10
                + num[13] * 5 + num[14] * 8 + num[15] * 4 + num[16] * 2) % 11;
        DebugLog.i(eightcardid + "==>运算结果:" + yushu);
        switch (yushu) {
            case 0:
                result = '1';
                break;
            case 1:
                result = '0';
                break;
            case 2:
                result = 'X';
                break;
            case 3:
                result = '9';
                break;
            case 4:
                result = '8';
                break;
            case 5:
                result = '7';
                break;
            case 6:
                result = '6';
                break;
            case 7:
                result = '5';
                break;
            case 8:
                result = '4';
                break;
            case 9:
                result = '3';
                break;
            case 10:
                result = '2';
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * 15位转18位身份证
     *
     * @param fifteencardid
     * @return
     */
    private static String uptoeighteen(String fifteencardid) {
        String eightcardid = fifteencardid.substring(0, 6);
        eightcardid = eightcardid + "19";
        eightcardid = eightcardid + fifteencardid.substring(6, 15);
        DebugLog.i("15位转18 前17位:" + eightcardid);
        eightcardid = eightcardid + getVerify(eightcardid);
        return eightcardid;
    }

    /**
     * 判断身份证格式
     *
     * @param idCard
     * @return
     */
    public static boolean isIDcard(String idCard) {
        String eifhteencard;
        if (idCard.length() == 15) {
            eifhteencard = uptoeighteen(idCard);
        } else {
            eifhteencard = idCard;
        }
        eifhteencard = eifhteencard.toUpperCase(Locale.US);
        DebugLog.i("idcard:" + eifhteencard);
        if (eifhteencard.length() == 18) {
            char finalChar = eifhteencard.charAt(17);
            char result = getVerify(eifhteencard);
            return result == finalChar;
        } else {
            return false;
        }
    }

    /**
     * 验证字符串是否是email
     *
     * @param str
     * @return
     */

    public static boolean isEmail(String str) {
        // "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"
        String temp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern pattern = Pattern.compile(temp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();

    }

    /**
     * 判断手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) return false;
        Pattern p = Pattern
                .compile("^\\d{11}$");
        Matcher m = p.matcher(mobiles);
        DebugLog.i("是否是手机号码：" + (m.matches() + "---"));
        return m.matches();
    }

    /**
     * 判断电话号码
     *
     * @return
     */
    public static boolean isTelephone(String telephone) {
        String temp = "^\\d{3}-?\\d{8}|\\d{4}-?\\d{8}$";
        Pattern p = Pattern.compile(temp);
        Matcher m = p.matcher(telephone);
        DebugLog.i( "是否是电话号码：" + (m.matches() + "---"));
        return m.matches();
    }

   /* public static boolean isNumber(String number) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(number);
        Logger.d(tag, "是否是0-9数字：" + (m.matches() + "---"));
        return m.matches();
    }

    public static boolean isZipcode(String code) {

        Pattern p = Pattern.compile("[1-9]\\d{5}(?!\\d)");
        Matcher m = p.matcher(code);
        Logger.d(tag, "是否是中国邮政编码：" + (m.matches() + "---"));
        return m.matches();

    }*/


    public static boolean isAvatar(String avatar) {
        return !TextUtils.isEmpty(avatar) && avatar.toLowerCase(Locale.US).startsWith("http://");
    }

    public  static boolean isNetworkAddress(String url){
        return !TextUtils.isEmpty(url) && url.toLowerCase(Locale.US).startsWith("http://");
    }

    private static final Pattern numRegex = Pattern.compile(".*[0-9].*");
    private static final Pattern alphaRegex = Pattern.compile(".*[A-Za-z].*");

    /**
     * 业务要求密码必须包含数字和字母，以及长度在6~20位
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() < 6 || password.length() > 20) {
            return false;
        }
        return numRegex.matcher(password).matches() && alphaRegex.matcher(password).matches();
    }

}
