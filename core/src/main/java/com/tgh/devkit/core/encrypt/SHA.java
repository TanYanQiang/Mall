package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.Strings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tanyq on 15/8/16.
 */
public class SHA {

    private SHA() {
    }

    public static byte[] digest(String source, String algo) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algo);
            messageDigest.update(source.getBytes());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return source.getBytes();
        }
    }

    public static String sha1(String source) {
        byte[] digest = digest(source, "SHA-1");
        return Strings.toHex(digest);
    }

}
