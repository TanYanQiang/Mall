package com.tgh.devkit.core.encrypt;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 *
 * Created by albert on 16/1/8.
 */
public class DESTest {

    @Test
    public void testDesEncryptAndDecrypt() throws Exception {
        byte[] keyData = new byte[8];
        new Random().nextBytes(keyData);

        String source = "你好啊，我是加密信息,hellow odd ada9asd__(089809-9-0";

        byte[] bytes = DES.desEncrypt(source.getBytes(), keyData);
        byte[] desDecrypt = DES.desDecrypt(bytes, keyData);
        assertEquals(new String(desDecrypt),source);
    }

}