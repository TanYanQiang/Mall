package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.DebugLog;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Created by albert on 16/1/8.
 */
public class AES {

    private AES(){}

    //----------AES 加密与解密(其中一种设置方式，采用单一密码的情况)---------------

    /**
     * 使用对称AES加密方式对数据进行加密
     *
     * @param data    要加密的数据
     * @param keyData 加密使用的密钥，密钥的长度必须是16个字节否则加密不成功返回null
     * @return 返回加密后的数据
     */
    public static byte[] aesEncrypt(byte[] data, byte[] keyData) {
        return aesSingle(Cipher.ENCRYPT_MODE, data, keyData);
    }

    /**
     * 使用对称AES加密方式对数据进行解密
     *
     * @param data    加密过的数据
     * @param keyData 解密使用的密钥，密钥的长度必须是16个字节否则解密不成功返回null
     * @return 返回解密后的数据
     */
    public static byte[] aesDecrypt(byte[] data, byte[] keyData) {
        return aesSingle(Cipher.DECRYPT_MODE, data, keyData);
    }

    private static byte[] aesSingle(int mode, byte[] data, byte[] keyData) {
        byte[] ret = null;
        if (data != null &&
                data.length > 0 &&
                keyData != null &&
                keyData.length == 16  //126bit aes
                ) {

            try {
                Cipher cipher = Cipher.getInstance("AES");
                //AES 方式一 单一密码的情况 不同于 DES
                SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
                cipher.init(mode, keySpec);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException | IllegalBlockSizeException |
                    BadPaddingException | InvalidKeyException |
                    NoSuchPaddingException e) {
                DebugLog.e(e.getMessage(),e);
            }

        }
        return ret;
    }

    //--------------AES 带有加密模式的方法，形成的加密强度更高，需要iv参数----------------------------

    /**
     * @param mode    加解密的模式
     * @param data    数据
     * @param keyData 密钥
     * @param ivData  用于AES/CBC/PKCS5Padding 这个有加密模式的算法
     * @return
     */
    private static byte[] aesWithIv(int mode, byte[] data, byte[] keyData, byte[] ivData) {
        byte[] ret = null;
        if (data != null
                && data.length > 0
                && keyData != null
                && keyData.length == 16
                && ivData != null
                && ivData.length == 16
                ) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //密码部分设置成“AES”即可
                SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
                //准备Iv参数
                IvParameterSpec iv = new IvParameterSpec(ivData);
                //设置密码以及IV参数
                cipher.init(mode, keySpec, iv);
                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException | IllegalBlockSizeException |
                    BadPaddingException | InvalidKeyException |
                    InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                DebugLog.e(e.getMessage(),e);
            }
        }
        return ret;
    }

    /**
     * 使用超高强度的AES对称加密方式对数据进行加密
     *
     * @param data    要加密的数据
     * @param keyData 加密使用的密钥 长度必须为16字节
     * @param ivData  加强强度所使用的密钥 长度必须为16字节
     * @return 返回加密后的数据
     */
    public static byte[] aesEncrypt(byte[] data, byte[] keyData, byte[] ivData) {
        return aesWithIv(Cipher.ENCRYPT_MODE, data, keyData, ivData);
    }

    /**
     * 使用超高强度的AES对称加密方式对加密过的数据进行解密
     *
     * @param data    要解密的数据
     * @param keyData 加密时使用的密钥 长度必须为16字节
     * @param ivData  加强强度时所使用的密钥 长度必须为16字节
     * @return 返回解密后的数据
     */
    public static byte[] aesDecrypt(byte[] data, byte[] keyData, byte[] ivData) {
        return aesWithIv(Cipher.DECRYPT_MODE, data, keyData, ivData);
    }
}
