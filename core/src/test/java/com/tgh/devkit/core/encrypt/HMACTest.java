package com.tgh.devkit.core.encrypt;

import com.tgh.devkit.core.utils.Strings;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by albert on 16/1/12.
 */
public class HMACTest {

    private static final String msg = "开机暗杀案送达啊啊送达是的";
    private static final String key = "xfn100";

    @Test
    public void testMd5() throws Exception {
        byte[] bytes = HMAC.md5(msg, key);
        System.out.println("md5 = "+ Strings.toHex(bytes));
    }

    @Test
    public void testSha1() throws Exception {
        byte[] bytes = HMAC.sha1(msg, key);
        System.out.println("sha1 = "+ Strings.toHex(bytes));
    }

    @Test
    public void testSha256() throws Exception {
        byte[] bytes = HMAC.sha256(msg, key);
        System.out.println("sha256 = "+ Strings.toHex(bytes));
    }

    @Test
    public void testSha384() throws Exception {
        byte[] bytes = HMAC.sha384(msg, key);
        System.out.println("sha384 = "+ Strings.toHex(bytes));
    }

    @Test
    public void testSha512() throws Exception {
        byte[] bytes = HMAC.sha512(msg, key);
        System.out.println("sha512 = "+ Strings.toHex(bytes));
    }
}