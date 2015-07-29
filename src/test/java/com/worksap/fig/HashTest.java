package com.worksap.fig;

import com.worksap.fig.lang.Hash;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liuyang on 7/27/15.
 */
public class HashTest {
    @Test
    public void testContainsAny() {
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);
        assertTrue(hash.containsAny((k, v) -> k + v == 3));
        assertTrue(hash.containsAny((k, v) -> k + v == 6));
        assertFalse(hash.containsAny((k, v) -> k + v == 5));
    }

    @Test
    public void testInvert() {
        Hash<Integer, String> hash = Hash.newHash();
        hash.put(1, "a");
        hash.put(2, "b");
        hash.put(5, "c");
        hash.put(3, "a");
        Hash<String, Integer> inverted = Hash.newHash();
        inverted.put("a", 3);
        inverted.put("b", 2);
        inverted.put("c", 5);
        assertTrue(inverted.equals(hash.invert()));
    }

    @Test
    public void testReject(){
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);

        Hash<Integer, Integer> rejected = Hash.newHash();
        rejected.put(2, 4);
        rejected.put(5, 1);
        assertTrue(rejected.equals(hash.reject((k, v) -> k + v != 6)));
    }


    @Test
    public void testFilter(){
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);

        Hash<Integer, Integer> filtered = Hash.newHash();
        filtered.put(1, 2);
        assertTrue(filtered.equals(hash.filter((k, v) -> k + v != 6)));
    }

    @Test(expected = NullPointerException.class)
    public void testReject$() {
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);
        assertEquals(2, hash.reject$((k, v) -> k > 1 && v > 1).size());
        assertEquals(null, hash.get(2));
        assertEquals(2, hash.reject$((k, v) -> k == v).size());
        assertEquals(Hash.newHash(), hash.reject$((k, v) -> k != v));
        hash.reject$(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilter$() {
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);
        assertEquals(2, hash.filter$((k, v) -> !(k > 1 && v > 1)).size());
        assertEquals(null, hash.get(2));
        assertEquals(2, hash.filter$((k, v) -> k != v).size());
        assertEquals(Hash.newHash(), hash.filter$((k, v) -> k == v));
        hash.filter$(null);
    }

}
