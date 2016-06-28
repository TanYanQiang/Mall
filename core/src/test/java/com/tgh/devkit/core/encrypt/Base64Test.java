package com.tgh.devkit.core.encrypt;

import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 *
 * Created by albert on 16/1/8.
 */
public class Base64Test {

    private byte[] data;
    private String text;

    @Before
    public void setUp() throws Exception {
        text = "kasd*&(&&(啊送达是*(=+(mnsad达到";
        data = text.getBytes();
    }

    @Test
    public void testDecodeToString() throws Exception {
        String encode = Base64.encode(text);
        String string = Base64.decodeToString(encode);
        assertEquals(string,text);
    }

    @Test
    public void testDecode() throws Exception {
        String encode = Base64.encode(text);
        byte[] decode = Base64.decode(encode);
        assertArrayEquals(decode, data);
    }

    @Test
    public void testEncode() throws Exception {
        String encode = Base64.encode(text);
        String encode2 = Base64.encode(data);
        assertEquals(encode, encode2);
        System.out.println(encode);

        String notWebSafe = Base64.encode(data, false);
        String notWebSafe2 = Base64.encode(text, false);
        System.out.println(notWebSafe);
        assertEquals(notWebSafe, notWebSafe2);
    }

}