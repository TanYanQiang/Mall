package com.tgh.devkit.core.primitives;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

/**
 *
 * Created by albert on 16/1/8.
 */
public class IntsTest {

    @Test
    public void testJoin() throws Exception {
        String join = Ints.join(",", 1, 2, 3, 4, 5);
        assertEquals(join,"1,2,3,4,5");
    }

    @Test
    public void testToArray() throws Exception {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        int[] ints = Ints.toArray(list);
        assertArrayEquals(ints, new int[]{1, 2, 3, 4, 5, 6});
    }

    @Test
    public void testRandInt() throws Exception {
        Random random = new Random();
        for (int i=0;i<200;i++) {
            int number = Ints.randInt(9, 27, random);
            assertTrue(number>=9);
            assertTrue(number<=27);
        }
    }
}