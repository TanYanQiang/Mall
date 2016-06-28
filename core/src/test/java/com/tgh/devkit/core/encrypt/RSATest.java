package com.tgh.devkit.core.encrypt;

import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Created by albert on 16/1/8.
 */
public class RSATest {

    private byte[] privateKey;
    private byte[] publicKey;

    @Before
    public void setUp() throws Exception {
        KeyPair keyPair = RSA.generateRSAKeyPair(1024);
        privateKey = RSA.getPrivateKey(keyPair);
        publicKey = RSA.getPublicKey(keyPair);
    }

    @Test
    public void testGetPublicKey() throws Exception {
        assertTrue(publicKey != null);
    }

    @Test
    public void testGetPrivateKey() throws Exception {
        assertTrue(privateKey != null);
    }

    @Test
    public void testFormatAndRe() throws Exception {
        String formatKey = RSA.formatKey(publicKey);
        System.out.println("publicKey="+formatKey);
        assertArrayEquals(RSA.reFormatKey(formatKey), publicKey);

        formatKey = RSA.formatKey(privateKey);
        System.out.println("privateKey="+formatKey);
        assertArrayEquals(RSA.reFormatKey(formatKey),privateKey);
    }

    @Test
    public void testRsaEncryptAndDecrypt() throws Exception {
        String value = "大家侃大山接单";
        byte[] bytes = RSA.rsaEncrypt(value.getBytes(),RSA.loadPublic(publicKey));
        byte[] bytes1 = RSA.rsaDecrypt(bytes, RSA.loadPrivate(privateKey));
        assertEquals(value,new String(bytes1));


        bytes = RSA.rsaEncrypt(value.getBytes(),RSA.loadPrivate(privateKey));
        bytes1 = RSA.rsaDecrypt(bytes, RSA.loadPublic(publicKey));
        assertEquals(value,new String(bytes1));
    }
}