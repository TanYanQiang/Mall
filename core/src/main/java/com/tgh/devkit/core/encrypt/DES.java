package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.DebugLog;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * Created by albert on 16/1/8.
 */
public class DES {

    private DES(){}


    /**
     * 使用对称加密DES方式对数据进行加密
     *
     * @param data    要加密的数据（字节数组）
     * @param keyData 加密用的密钥（字节数组）长度必须大于等于8个字节否则返回null
     * @return 返回加密过的数据（字节数组）
     */
    public static byte[] desEncrypt(byte[] data, byte[] keyData) {
        return des(Cipher.ENCRYPT_MODE, data, keyData);
    }

    /**
     * 解密使用对称加密DES方式加密过的数据
     *
     * @param data    加密过的数据（字节数组）
     * @param keyData 解密用的密钥（字节数组）长度必须大于等于8个字节否则返回null
     * @return 返回解密后的字节数组
     */
    public static byte[] desDecrypt(byte[] data, byte[] keyData) {
        return des(Cipher.DECRYPT_MODE, data, keyData);
    }

    private static byte[] des(int mode, byte[] data, byte[] keyData) {
        byte[] ret = null;
        if (data != null &&
                data.length > 0 &&
                keyData != null &&
                keyData.length >= 8) {
            try {
                Cipher cipher = Cipher.getInstance("DES");
                //3、准备Key对象
                //3.1、DES 使用DESKeySpec,内部构造指定8个字节密码即可
                DESKeySpec keySpec = new DESKeySpec(keyData);

                //3.2、DeSKeySpec 需要转换成Key对象，才可以继续使用
                //需要使用SecretKeyFactory来处理
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                //3.3 生成Key对象
                SecretKey key = keyFactory.generateSecret(keySpec);
                //2、设置Cipher是加密还是解密，模式
                //同时对于对称加密还需要设置密码Key对象
                //参数2使用Key对象
                cipher.init(mode, key);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                    IllegalBlockSizeException | BadPaddingException |
                    InvalidKeySpecException | InvalidKeyException e) {
                DebugLog.e(e.getMessage(),e);
            }
        }
        return ret;
    }
}
