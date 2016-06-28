package com.tgh.devkit.core.utils;

import org.junit.Test;

/**
 * Created by albert on 16/1/13.
 */
public class UtilsTest {

    @Test
    public void testAppendUrl() throws Exception {
        String url = "http://www.baidu.com";
        url = Utils.appendUrl(url, "k", 100, "x", "iudax", "price", 17.983f);
        System.out.println(url);
    }

    @Test
    public void testEncodeUrl() throws Exception {
        String url = "http://www.baidu.com";
        url = Utils.appendUrl(url, "k", 100, "x", "搭撒", "price", 17.983f);
        System.out.println(url);

        url = Utils.encodeUrl(url);
        System.out.println(url);
    }
}