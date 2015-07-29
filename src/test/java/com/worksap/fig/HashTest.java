package com.worksap.fig;

import com.worksap.fig.lang.Hash;
import com.worksap.fig.lang.Seq;
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
        Hash<Integer, Integer> origin = Hash.of(hash);
        assertEquals(origin, hash);

        Hash<Integer, Integer> rejected = Hash.newHash();
        rejected.put(2, 4);
        rejected.put(5, 1);
        assertTrue(rejected.equals(hash.reject((k, v) -> k + v != 6)));
        assertFalse(rejected.equals(hash));
        assertEquals(origin, hash);
        assertEquals(Hash.newHash(), hash.reject((k, v) -> k != v));
        assertNotEquals(Hash.newHash(), hash);
        assertEquals(origin, hash);
    }


    @Test
    public void testFilter(){
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(5, 1);
        Hash<Integer, Integer> origin = Hash.of(hash);
        assertEquals(origin, hash);

        Hash<Integer, Integer> filtered = Hash.newHash();
        filtered.put(1, 2);
        assertTrue(filtered.equals(hash.filter((k, v) -> k + v != 6)));
        assertFalse(filtered.equals(hash));
        assertEquals(origin, hash);
        assertEquals(Hash.newHash(), hash.filter((k, v) -> k == v));
        assertNotEquals(Hash.newHash(), hash);
        assertEquals(origin, hash);
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

    @Test
    public void testGetKey() {
        Hash<Integer, Integer> hash = Hash.newHash();
        hash.put(1, 2);
        hash.put(2, 4);
        hash.put(3, 2);
        hash.put(4, null);
        hash.put(5, 1);
        Seq<Integer> keys = hash.getKey(2);
        keys.sort(Integer::compare);
        assertEquals(Seq.of(1, 3), keys);
        assertEquals(Seq.of(2), hash.getKey(4));
        assertEquals(Seq.of(), hash.getKey(3));
        assertEquals(Seq.of(4), hash.getKey(null));
    }

    @Test
    public void testMerge() {
        Hash<Integer, Integer> hash1 = Hash.newHash();
        hash1.put(1, 2);
        hash1.put(2, 4);
        Hash<Integer, Integer> origin1 = Hash.of(hash1);
        Hash<Integer, Integer> hash2 = Hash.newHash();
        hash2.put(3, 2);
        hash2.put(2, 3);
        Hash<Integer, Integer> origin2 = Hash.of(hash2);

        Hash<Integer, Integer> merged = Hash.newHash();
        merged.put(1, 2);
        merged.put(2, 3);
        merged.put(3, 2);

        assertEquals(merged, hash1.merge(hash2));
        assertEquals(origin1, hash1);
        assertEquals(origin2, hash2);

        merged.set(2, 4);
        assertEquals(merged, hash2.merge(hash1));
        assertEquals(origin1, hash1);
        assertEquals(origin2, hash2);

        assertEquals(origin1, hash1.merge(Hash.newHash()));
        assertEquals(origin1, Hash.newHash().merge(hash1));

        assertEquals(origin1, hash1.merge(null));

        hash1.put(null, 1);
        merged.put(null, 1);
        merged.set(2, 3);
        assertEquals(merged, hash1.merge(hash2));
    }

    @Test
    public void testMerge$() {
        Hash<Integer, Integer> hash1 = Hash.newHash();
        hash1.put(1, 2);
        hash1.put(2, 4);
        Hash<Integer, Integer> origin1 = Hash.of(hash1);
        Hash<Integer, Integer> hash2 = Hash.newHash();
        hash2.put(3, 2);
        hash2.put(2, 3);
        Hash<Integer, Integer> origin2 = Hash.of(hash2);

        Hash<Integer, Integer> merged = Hash.newHash();
        merged.put(1, 2);
        merged.put(2, 3);
        merged.put(3, 2);

        assertEquals(merged, hash1.merge$(hash2));
        assertEquals(merged, hash1);
        assertEquals(origin2, hash2);

        hash1 = Hash.of(origin1);
        merged.set(2, 4);
        assertEquals(merged, hash2.merge$(hash1));
        assertEquals(origin1, hash1);
        assertEquals(merged, hash2);

        hash2 = Hash.of(origin2);
        assertEquals(origin1, hash1.merge$(Hash.newHash()));
        assertEquals(origin1, Hash.newHash().merge$(hash1));

        assertEquals(origin1, hash1.merge$(null));

        hash1.put(null, 1);
        merged.put(null, 1);
        merged.set(2, 3);
        assertEquals(merged, hash1.merge$(hash2));
    }
}
