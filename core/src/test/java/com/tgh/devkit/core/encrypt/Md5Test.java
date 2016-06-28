package com.tgh.devkit.core.encrypt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 * Created by albert on 16/1/8.
 */
public class Md5Test {

    private String text;

    @Before
    public void setUp() throws Exception {
        text = "13d+90123&**&^*sad21d大家啊送达123123";
    }

    @Test
    public void testDigest() throws Exception {
        byte[] bytes = Md5.digest(text);
        String encode = Base64.encode(bytes);
        System.out.println("Base64="+encode);
        assertArrayEquals(bytes, Base64.decode(encode.getBytes()));
    }

    @Test
    public void testToString() throws Exception {
        String encode = Md5.toString(text);
        System.out.println("toString=" + encode);
    }
}