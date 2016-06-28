package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.DebugLog;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * Created by albert on 16/1/8.
 */

public class RSA {


    private RSA() {
    }

    /**
     * 通过指定的密钥长度，生成非对称的密钥对
     *
     * @param keySize 推荐使用1024/2048 不允许低于1024
     * @return KeyPair 公私密钥对
     */
    public static KeyPair generateRSAKeyPair(int keySize) {
        KeyPair ret = null;
        try {
            //1.准备生成
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            //2.初始化，设置密钥长度
            generator.initialize(keySize);
            //3.生成，并且返回
            ret = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 载入公钥
     */
    public static PublicKey loadPublic(byte[] keyData) {
        PublicKey ret = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyData);
            ret = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            DebugLog.e(e.getMessage());
        }
        return ret;
    }

    /**
     * 载入私钥
     */
    public static PrivateKey loadPrivate(byte[] keyData) {
        PrivateKey ret = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyData);
            ret = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            DebugLog.e(e.getMessage(),e);
        }
        return ret;
    }


    /**
     * 使用非对称RSA加密
     *
     * @param data 要加密的原始数据（字节数组）
     * @param key  密钥（公钥或者私钥）
     * @return 返回加密过后的数据（字节数组）
     */
    public static byte[] rsaEncrypt(byte[] data, Key key) {
        return rsa(Cipher.ENCRYPT_MODE, data, key);
    }

    /**
     * 非对称RSA解密
     *
     * @param data 使用密钥加密过的字节数组
     * @param key  密钥（如果原始数据是用公钥加密过的，那么此处就要用私钥来解密；否则用公钥来解）
     * @return 返回解密后的数据（字节数组）
     */
    public static byte[] rsaDecrypt(byte[] data, Key key) {
        return rsa(Cipher.DECRYPT_MODE, data, key);
    }

    /**
     * 非对称RSA加密与解密
     *
     * @param mode 加密还是解密的模式
     * @param data 数据
     * @param key  密钥
     * @return 加密或解密之后的字节数组
     */
    private static byte[] rsa(int mode, byte[] data, Key key) {
        byte[] ret = null;
        if (data != null
                && data.length > 0
                && key != null
                ) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(mode, key);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException |
                    NoSuchPaddingException |
                    InvalidKeyException |
                    BadPaddingException |
                    IllegalBlockSizeException e) {
                DebugLog.e(e.getMessage(),e);
            }
        }
        return ret;
    }

    public static byte[] getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic().getEncoded();
    }

    public static byte[] getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate().getEncoded();
    }

    public static String formatKey(byte[] data){
        return Base64.encode(data);
    }

    public static byte[] reFormatKey(String key){
        return Base64.decode(key.getBytes());
    }
}
