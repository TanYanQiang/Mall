package com.tgh.devkit.core.config;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.tgh.devkit.core.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * Created by albert on 16/1/15.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class KeyValueStoreTest extends BaseTest{


    private KeyValueStoreInternal getInitInstance(String config,boolean flag){
        KeyValueStoreInternal instance = KeyValueStore.getInstance(context, config,flag);
        instance.clear();
        return instance;
    }

    @Test
    public void testSaveAndGetInt(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("id",97);
        int id = instance.getInt("id");
        assertEquals(id,97);

        instance.setBindWithVersion(false);
        id = instance.getInt("id");
        assertEquals(id, 0);
    }

    @Test
    public void testSaveAndGetLong(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        long timeMillis = System.currentTimeMillis();
        instance.save("timeStamp", timeMillis);
        long timeStamp = instance.getLong("timeStamp");
        assertEquals(timeStamp,timeMillis);

        instance.setBindWithVersion(false);
        timeStamp = instance.getLong("timeStamp");
        assertEquals(timeStamp, 0l);
    }

    @Test
    public void testSaveAndGetFloat(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("price",17.4f);
        float price = instance.getFloat("price");
        assertEquals(price, 17.4f, 0.0001f);

        instance.setBindWithVersion(false);
        price = instance.getFloat("price");
        assertEquals(price, 0f, 0.0001f);
    }

    @Test
    public void testSaveAndGetDouble(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("priced",17.4);
        double price = instance.getDouble("priced");
        assertEquals(price, 17.4, 0.0001);

        instance.setBindWithVersion(false);
        price = instance.getDouble("priced");
        assertEquals(price, 0, 0.0001);
    }

    @Test
    public void testSaveAndGetString(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("name","巨大");
        String name = instance.getString("name");
        assertEquals(name,"巨大");

        instance.setBindWithVersion(false);
        name = instance.getString("name");
        assertNull(name);
    }
    @Test
    public void testSaveAndGetBoolean(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("flag", true);
        boolean flag = instance.getBoolean("flag");
        assertTrue(flag);

        instance.setBindWithVersion(false);
        flag = instance.getBoolean("flag");
        assertFalse(flag);
    }

    @Test
    public void testSaveAndGetObject(){
        String[] obj = new String[]{"a","1","false","大搭撒"};

        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.saveObject("obj", obj);
        String[] obj1 = instance.getObject("obj", String[].class);
        assertArrayEquals(obj, obj1);

        instance.setBindWithVersion(false);
        obj1 = instance.getObject("obj", String[].class);
        assertNull(obj1);


        ArrayList<String> list = new ArrayList<>(Arrays.asList(obj));
        instance.setBindWithVersion(true);
        instance.saveObject("list", list);
        ArrayList<String> list1 = instance.getObject("list", ArrayList.class);
        assertEquals(list,list1);

        instance.setBindWithVersion(false);
        list1 = instance.getObject("list", ArrayList.class);
        assertNull(list1);
    }


    @Test
    public void testSaveAndGetStringFromConfig(){
        KeyValueStoreInternal instance = getInitInstance(null, true);
        instance.save("name111", "巨大");
        String name = instance.getString("name111");
        assertEquals(name, "巨大");

        instance.setBindWithVersion(false);
        name = instance.getString("name111");
        assertNull(name);
        KeyValueStoreInternal ks1 = getInitInstance("user",false);
        ks1.save("nick", "jack");
        KeyValueStoreInternal ks2 = getInitInstance("user2",false);
        ks2.save("nick", "jack2");
        assertEquals(ks1.getString("nick"),"jack");
        assertEquals(ks2.getString("nick"), "jack2");
    }

}
