package com.tgh.devkit.core.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 * Created by albert on 16/1/8.
 */
public class StringsTest {

    @Test
    public void testBytesToHexString() throws Exception {
        String text = "da搭撒sd的asd+as—d";
        System.out.println(Strings.toHex(text.getBytes("UTF-8")));
        String toHex = Strings.toHex(text.getBytes("ASCII"));
        System.out.println(toHex);
    }

    @Test
    public void testDoubleTrans() throws Exception {
        assertEquals(Strings.doubleTrans(98.0379), "98.04");
        assertEquals(Strings.doubleTrans(98.0979), "98.1");
        assertEquals(Strings.doubleTrans(98.0929), "98.09");
    }

    @Test
    public void testDoubleTrans1() throws Exception {
        assertEquals(Strings.doubleTrans(98.0379, 2), "98.04");
        assertEquals(Strings.doubleTrans(98.0979, 2), "98.1");
        assertEquals(Strings.doubleTrans(98.0929, 2), "98.09");
    }

    @Test
    public void testIsNullOrEmpty() throws Exception {
        assertTrue(Strings.isNullOrEmpty(null));
        assertTrue(Strings.isNullOrEmpty(""));
        assertTrue(Strings.isNullOrEmpty("     "));
        assertTrue(Strings.isNullOrEmpty("null"));
        assertTrue(Strings.isNullOrEmpty("nUll"));
    }

    @Test
    public void testEmptyToNull() throws Exception {
        assertNull(Strings.emptyToNull(""));
        assertNull(Strings.emptyToNull(" "));
        assertNull(Strings.emptyToNull("null"));
    }

    @Test
    public void testNullToEmpty() throws Exception {
        assertEquals(Strings.nullToEmpty(null), "");
        assertEquals(Strings.nullToEmpty(""), "");
        assertEquals(Strings.nullToEmpty("   "), "");
        assertEquals(Strings.nullToEmpty("null "), "");
    }


    @Test
    public void testInsert(){
        String demo = "啊。";
        String s = Strings.insertAfter(demo, "。", "￥#");
        assertEquals(s, "啊。￥#");

        demo = "啊。啊";
        s = Strings.insertAfter(demo, "。", "￥#");
        assertEquals(s, "啊。￥#啊");

        demo = "。啊";
        s = Strings.insertAfter(demo, "。", "￥#");
        assertEquals(s, "。￥#啊");

        demo = "啊。";
        s = Strings.insertAfterAll(demo, "。", "￥#");
        assertEquals(s, "啊。￥#");

        demo = "啊。啊";
        s = Strings.insertAfterAll(demo, "。", "￥#");
        assertEquals(s, "啊。￥#啊");

        demo = "。啊";
        s = Strings.insertAfterAll(demo, "。", "￥#");
        assertEquals(s, "。￥#啊");


        demo = "啊。";
        s = Strings.insertBefore(demo, "。", "￥#");
        assertEquals(s, "啊￥#。");

        demo = "啊。啊";
        s = Strings.insertBefore(demo, "。", "￥#");
        assertEquals(s, "啊￥#。啊");

        demo = "。啊";
        s = Strings.insertBefore(demo, "。", "￥#");
        assertEquals(s, "￥#。啊");


        demo = "啊。";
        s = Strings.insertBeforeAll(demo, "。", "￥#");
        assertEquals(s, "啊￥#。");

        demo = "啊。啊";
        s = Strings.insertBeforeAll(demo, "。", "￥#");
        assertEquals(s, "啊￥#。啊");

        demo = "。啊";
        s = Strings.insertBeforeAll(demo, "。", "￥#");
        assertEquals(s, "￥#。啊");
    }

    @Test
    public void testExtract() throws Exception {
        String demo ="a123《达到》d1312的1《飞翔的密码》d撒1大9907";
        String extract = Strings.extract(demo, "\\d+");
        System.out.println(extract);

        String[] extractAll = Strings.extractAll(demo, "\\d+");
        System.out.println(Arrays.toString(extractAll));


        extractAll = Strings.extractAll(demo,null);
        System.out.println(Arrays.toString(extractAll));
    }

    @Test
    public void testQuery() throws Exception {

        String str = "1$123#1das$4#2";
        String[] strings = Strings.extractAll(str, "\\$\\d+?#");
        System.out.println(Arrays.toString(strings));


        String demo ="a123《达到》d1312的1《飞翔的密码》d撒1大9907";
        int[] first = Strings.findFirst(demo, "《.*?》");
        System.out.println(Arrays.toString(first));

        System.out.println();
        LinkedList<int[]> all = Strings.findAll(demo, "《.*?》");
        for (int[] ints : all) {
            System.out.println(Arrays.toString(ints));
        }
        System.out.println();
        all = Strings.findAll(demo, "\\d+");
        for (int[] ints : all) {
            System.out.println(Arrays.toString(ints));
        }


        String query = "#1";
        System.out.println();
        all = Strings.findAll(str, query);
        for (int[] ints : all) {
            System.out.println(Arrays.toString(ints));
        }
    }
}