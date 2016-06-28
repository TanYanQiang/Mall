package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.DebugLog;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Created by albert on 16/1/12.
 */
public class HMAC {

    private HMAC(){}

    public static byte[] md5(String msg, String key){
        return encrypt(msg,key,"HmacMD5");
    }

    public static byte[] sha1(String msg, String key){
        return encrypt(msg,key,"HmacSHA1");
    }

    public static byte[] sha256(String msg, String key){
        return encrypt(msg,key,"HmacSHA256");
    }

    public static byte[] sha384(String msg, String key){
        return encrypt(msg,key,"HmacSHA384");
    }

    public static byte[] sha512(String msg, String key){
        return encrypt(msg,key,"HmacSHA512");
    }

    private static byte[] encrypt(String msg, String key, String algo){
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec((key).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(secretKeySpec);
            return mac.doFinal(msg.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | InvalidKeyException
                | UnsupportedEncodingException e) {
            DebugLog.e(e.getMessage(),e);
            return null;
        }
    }
}
