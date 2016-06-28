package com.tgh.devkit.core.encrypt;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 *
 * Created by albert on 16/1/8.
 */
public class AESTest {

    private byte[] keyData;
    private String source;
    private byte[] ivData;

    @Before
    public void setUp() throws Exception {
        keyData = new byte[16];
        Random random = new Random();
        random.nextBytes(keyData);

        ivData = new byte[16];
        random.nextBytes(ivData);

        source = "你好啊，我是加密信息,hellow odd ada9asd__(089809-9-0";
    }

    @Test
    public void testAesEncryptAndDecrypt() throws Exception {
        byte[] bytes = AES.aesEncrypt(source.getBytes(), keyData);
        byte[] decrypt = AES.aesDecrypt(bytes, keyData);
        assertEquals(source,new String(decrypt));
    }

    @Test
    public void testAesEncryptAndDecryptWithIv() throws Exception {
        byte[] bytes = AES.aesEncrypt(source.getBytes(), keyData,ivData );
        byte[] decrypt = AES.aesDecrypt(bytes, keyData,ivData );
        assertEquals(source,new String(decrypt));
    }
}