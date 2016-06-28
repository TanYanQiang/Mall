package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.Strings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by albert on 16/1/8.
 */
public class Md5 {

    private Md5(){}

    public static byte[] digest(String source) {
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(source.getBytes());
            return  mDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            return source.getBytes();
        }
    }

    public static String toString(String key) {
        byte[] digest = digest(key);
        return Strings.toHex(digest);
    }

}
