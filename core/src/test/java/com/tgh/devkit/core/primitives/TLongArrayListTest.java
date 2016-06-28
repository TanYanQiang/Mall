package com.tgh.devkit.core.primitives;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by albert on 16/1/8.
 */
public class TLongArrayListTest {

    private TLongArrayList data;

    @Before
    public void setUp() throws Exception {
        long[] array = new long[1024];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        data = new TLongArrayList(array);
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(data.size(),1024);
        data.ensureCapacity(1027);
        assertEquals(data.size(), 1024);
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertEquals(data.isEmpty(),false);
    }

    @Test
    public void testAdd() throws Exception {
        data.add(1024);
        assertEquals(data.size(), 1025);
        assertEquals(data.getQuick(1024),1024);
    }

    @Test
    public void testAdd1() throws Exception {
        data.add(new long[]{1024,1025});
        assertEquals(data.size(),1026);
    }

    @Test
    public void testAdd2() throws Exception {
        data.add(new long[]{1024,1025,1026},0,2);
        assertEquals(data.size(), 1026);
    }

    @Test
    public void testInsert() throws Exception {
        data.insert(3,9876);
        assertEquals(data.getQuick(3), 9876);
        assertEquals(data.getQuick(4),3);
    }

    @Test
    public void testInsert1() throws Exception {
        data.insert(3,new long[]{9876,9877,9878});
        assertEquals(data.size(), 1027);
        assertEquals(data.getQuick(4),9877);
    }

    @Test
    public void testInsert2() throws Exception {
        data.insert(3,new long[]{9876,9877,9878},1,2);
        assertEquals(data.size(),1026);
        assertEquals(data.getQuick(4),9878);
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(data.get(5),5);
    }

    @Test
    public void testGetQuick() throws Exception {
        assertEquals(data.getQuick(17), 17);
    }

    @Test
    public void testSet() throws Exception {
        data.set(7,7);
        assertEquals(data.size(), 1024);
        assertEquals(data.get(7), 7);
        assertEquals(data.get(8),8);
    }

    @Test
    public void testGetSet() throws Exception {
        long set = data.getSet(7, 19);
        assertEquals(set,7);
        assertEquals(data.get(7),19);
    }

    @Test
    public void testSet1() throws Exception {
        long[] longs = {9999, 10000};
        data.set(7,longs);
        assertEquals(data.get(7), 9999);
        assertEquals(data.get(8),10000);
        assertEquals(data.get(9),9);
    }

    @Test
    public void testSet2() throws Exception {
        data.set(7,new long[]{13,14,15,16},2,2);
        assertEquals(data.size(), 1024);
        assertEquals(data.get(7), 15);
        assertEquals(data.get(8), 16);
        assertEquals(data.get(9),9);
    }

    @Test
    public void testSetQuick() throws Exception {
        data.setQuick(7,98);
        assertEquals(data.getQuick(7), 98);
        assertEquals(data.getQuick(6), 6);
        assertEquals(data.getQuick(8),8);
    }

    @Test
    public void testClear() throws Exception {
        data.clear();
        assertEquals(data.isEmpty(),true);
    }

    @Test
    public void testClear1() throws Exception {
        data.clear(78);
        assertEquals(data.size(), 0);
        assertEquals(data.isEmpty(),true);
    }

    @Test
    public void testReset() throws Exception {
        data.set(0, 17);
        data.reset();
        assertEquals(data.size(), 0);
        assertEquals(data.getQuick(0),17);
    }

    @Test
    public void testResetQuick() throws Exception {
        data.set(2,19);
        data.resetQuick();
        assertEquals(data.size(), 0);
        assertEquals(data.getQuick(2), 19);
        data.add(12);
        assertEquals(data.getQuick(0), 12);
        assertEquals(data.getQuick(2),19);
    }

    @Test
    public void testRemove() throws Exception {
        data.remove(17);
        assertEquals(data.size(), 1023);
        assertEquals(data.get(17),18);
        assertEquals(data.get(1022),1023);
    }

    @Test
    public void testRemove1() throws Exception {
        data.remove(7,7);
        assertEquals(data.size(), 1024 - 7);
        assertEquals(data.getQuick(7),14);
    }

    @Test
    public void testReverse() throws Exception {
        data.reverse();
        assertEquals(data.get(0),1023);
    }

    @Test
    public void testReverse1() throws Exception {
        data.reverse(1, 8);
        assertEquals(data.getQuick(7), 1);
        assertEquals(data.getQuick(1),7);
    }

    @Test
    public void testShuffle() throws Exception {
        data.shuffle(new Random());
    }

    @Test
    public void testClone() throws Exception {
        TLongArrayList cloneList = data.clone();
        data.clear();
        assertEquals(data.isEmpty(), true);
        assertEquals(cloneList.isEmpty(),false);
    }

    @Test
    public void testToNativeArray() throws Exception {
        long[] longs = data.toNativeArray();
        assertEquals(longs.length,data.size());
    }

    @Test
    public void testToNativeArray1() throws Exception {
        long[] longs = data.toNativeArray(1, 10);
        assertEquals(longs.length,10);
        assertEquals(longs[9],10);
    }

    @Test
    public void testToNativeArray2() throws Exception {
        long[] dest = new long[15];
        data.toNativeArray(dest,9,3);
        assertEquals(dest.length, 15);
        assertEquals(dest[0], 9);
        assertEquals(dest[1], 10);
        assertEquals(dest[2],11);
        assertEquals(dest[3],0);

    }

    @Test
    public void testSort() throws Exception {
        data.shuffle(new Random());
        data.sort();
        assertEquals(data.getQuick(1),1);
    }

    @Test
    public void testSort1() throws Exception {
        data.shuffle(new Random());
        data.sort(1, 5);
        assertTrue(data.getQuick(1) < data.getQuick(2));
        assertTrue(data.getQuick(2) < data.getQuick(3));
        assertTrue(data.getQuick(3)<data.getQuick(4));
    }

    @Test
    public void testFill() throws Exception {
        data.fill(8);
        assertEquals(data.getQuick(18), 8);
        assertEquals(data.getQuick(11), 8);
        assertEquals(data.getQuick(100),8);
    }

    @Test
    public void testFill1() throws Exception {
        data.fill(1,9,1077);
        assertEquals(data.getQuick(1), 1077);
        assertEquals(data.getQuick(8), 1077);
        assertNotEquals(data.getQuick(9), 1077);
    }

    @Test
    public void testBinarySearch() throws Exception {
        int i = data.binarySearch(18);
        assertEquals(i,18);
    }

    @Test
    public void testBinarySearch1() throws Exception {
        int i = data.binarySearch(18, 0, 10);
        assertTrue(i<0);
        i = data.binarySearch(18, 0, 20);
        assertEquals(i,18);
    }

    @Test
    public void testIndexOf() throws Exception {
        assertEquals(data.indexOf(18),18);
    }

    @Test
    public void testIndexOf1() throws Exception {
        assertTrue(data.indexOf(20,18)<0);
        assertEquals(data.indexOf(3, 18), 18);
    }

    @Test
    public void testLastIndexOf() throws Exception {
        assertEquals(data.lastIndexOf(18),18);
    }

    @Test
    public void testLastIndexOf1() throws Exception {
        assertTrue(data.lastIndexOf(3, 18)<0);
        assertEquals(data.lastIndexOf(20, 18), 18);
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(data.contains(29));
        assertFalse(data.contains(-9));
    }

    @Test
    public void testMax() throws Exception {
        assertEquals(data.max(),1023);
    }

    @Test
    public void testMin() throws Exception {
        assertEquals(data.min(),0);
    }
}