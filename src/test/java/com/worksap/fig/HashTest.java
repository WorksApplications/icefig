package com.worksap.fig;

import com.worksap.fig.lang.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.worksap.fig.Helpers.assertThrows;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lijunxiao on 8/6/15.
 */
public class HashTest {
    @Test
    public void testPutGetRemoveSizeAndIsEmpty() {
        MutableHash<String, String> hash = Hashes.newMutableHash();
        assertTrue(hash.size() == 0);
        assertTrue(hash.isEmpty());

        hash.putInPlace("23", "Micheal Jordan");
        hash.putInPlace("24", "Kobe Bryant");
        assertEquals(hash.size(), 2);
        assertEquals(hash.get("24"), "Kobe Bryant");
        assertNull(hash.get("25"));

        hash.putInPlace("1", "Allen Iverson");
        assertEquals(hash.size(), 3);
        hash.putIfAbsentInPlace("1", "Tracy McGrady");
        assertEquals(hash.get("1"), "Allen Iverson");
        hash.putInPlace("1", "Tracy McGrady");
        assertEquals(hash.get("1"), "Tracy McGrady");

        hash.removeInPlace("23");
        assertEquals(hash.size(), 2);
        assertNull(hash.get("23"));
    }

    @Test
    public void testContainsAny() {
        MutableHash<Integer, Integer> hash = Hashes.newMutableHash();
        hash.putInPlace(1, 2).putInPlace(2, 4).putInPlace(5, 1);

        assertTrue(hash.containsAny((k, v) -> k + v == 3));
        assertTrue(hash.containsAny((k, v) -> k + v == 6));
        assertFalse(hash.containsAny((k, v) -> k + v == 5));
        assertThrows(NullPointerException.class, () -> hash.containsAny(null));
    }

    @Test
    public void testInvert() {
//        Hash<Integer, String> hash = Hashes.newHash();
//        hash.put(1, "a");
//        hash.put(2, "b");
//        hash.put(5, "c");
//        hash.put(3, "a");
//        Hash<String, Integer> inverted = Hashes.newHash();
//        inverted.put("a", 3);
//        inverted.put("b", 2);
//        inverted.put("c", 5);
//        assertTrue(inverted.equals(hash.invert()));
    }

    @Test
    public void testReject(){
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        hash.put(1, 2);
//        hash.put(2, 4);
//        hash.put(5, 1);
//        Hash<Integer, Integer> origin = Hashes.newHash();
//        assertEquals(origin, hash);
//
//        Hash<Integer, Integer> rejected = Hashes.newHash();
//        rejected.put(2, 4);
//        rejected.put(5, 1);
//        assertTrue(rejected.equals(hash.reject((k, v) -> k + v != 6)));
//        assertFalse(rejected.equals(hash));
//        assertEquals(origin, hash);
//        assertEquals(Hashes.newHash(), hash.reject((k, v) -> k != v));
//        assertNotEquals(Hashes.newHash(), hash);
//        assertEquals(origin, hash);
//
//        assertThrows(NullPointerException.class, () -> hash.reject(null));
    }


    @Test
    public void testFilter(){
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        hash.put(1, 2);
//        hash.put(2, 4);
//        hash.put(5, 1);
//        Hash<Integer, Integer> origin = Hashes.newHash();
//        assertEquals(origin, hash);
//
//        Hash<Integer, Integer> filtered = Hashes.newHash();
//        filtered.put(1, 2);
//        assertTrue(filtered.equals(hash.filter((k, v) -> k + v != 6)));
//        assertFalse(filtered.equals(hash));
//        assertEquals(origin, hash);
//        assertEquals(Hashes.newHash(), hash.filter((k, v) -> k == v));
//        assertNotEquals(Hashes.newHash(), hash);
//        assertEquals(origin, hash);
//        assertThrows(NullPointerException.class, () -> hash.filter(null));
    }

    @Test
    public void testReject$() {
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        hash.put(1, 2);
//        hash.put(2, 4);
//        hash.put(5, 1);
//        assertEquals(2, hash.rejectInPlace((k, v) -> k > 1 && v > 1).size());
//        assertEquals(null, hash.get(2));
//        assertEquals(2, hash.rejectInPlace((k, v) -> k == v).size());
//        assertEquals(Hashes.newHash(), hash.rejectInPlace((k, v) -> k != v));
//        assertThrows(NullPointerException.class, () -> hash.rejectInPlace(null));
    }

    @Test
    public void testFilter$() {
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        hash.put(1, 2);
//        hash.put(2, 4);
//        hash.put(5, 1);
//        assertEquals(2, hash.filter$((k, v) -> !(k > 1 && v > 1)).size());
//        assertEquals(null, hash.get(2));
//        assertEquals(2, hash.filter$((k, v) -> k != v).size());
//        assertEquals(Hashes.newHash(), hash.filter$((k, v) -> k == v));
//        assertThrows(NullPointerException.class, () -> hash.filter$(null));
    }

    @Test
    public void testKeysOf() {
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        hash.put(1, 2);
//        hash.put(2, 4);
//        hash.put(3, 2);
//        hash.put(4, null);
//        hash.put(5, 1);
//        Seq<Integer> keys = hash.keysOf(2);
//        keys.sort(Integer::compare);
//        assertEquals(Seqs.newSeq(1, 3), keys);
//        assertEquals(Seqs.newSeq(2), hash.keysOf(4));
//        assertEquals(Seqs.newSeq(), hash.keysOf(3));
//        assertEquals(Seqs.newSeq(4), hash.keysOf(null));
    }

    @Test
    public void testMerge() {
//        Hash<Integer, Integer> hash1 = Hashes.newHash();
//        hash1.put(1, 2);
//        hash1.put(2, 4);
//        Hash<Integer, Integer> origin1 = Hashes.newHash();
//        Hash<Integer, Integer> hash2 = Hashes.newHash();
//        hash2.put(3, 2);
//        hash2.put(2, 3);
//        Hash<Integer, Integer> origin2 = Hashes.newHash();
//
//        Hash<Integer, Integer> merged = Hashes.newHash();
//        merged.put(1, 2);
//        merged.put(2, 3);
//        merged.put(3, 2);
//
//        assertEquals(merged, hash1.merge(hash2));
//        assertEquals(origin1, hash1);
//        assertEquals(origin2, hash2);
//
//        merged.put(2, 4);
//        assertEquals(merged, hash2.merge(hash1));
//        assertEquals(origin1, hash1);
//        assertEquals(origin2, hash2);
//
//        assertEquals(origin1, hash1.merge(Hashes.newHash()));
//        assertEquals(origin1, Hashes.newHash().merge(hash1));
//
//        assertEquals(origin1, hash1.merge(null));
//
//        hash1.put(null, 1);
//        merged.put(null, 1);
//        merged.put(2, 3);
//        assertEquals(merged, hash1.merge(hash2));
    }

    @Test
    public void testMerge$() {
//        Hash<Integer, Integer> hash1 = Hashes.newHash();
//        hash1.put(1, 2);
//        hash1.put(2, 4);
//        Hash<Integer, Integer> origin1 = Hashes.newHash();
//        Hash<Integer, Integer> hash2 = Hashes.newHash();
//        hash2.put(3, 2);
//        hash2.put(2, 3);
//        Hash<Integer, Integer> origin2 = Hashes.newHash();
//
//        Hash<Integer, Integer> merged = Hashes.newHash();
//        merged.put(1, 2);
//        merged.put(2, 3);
//        merged.put(3, 2);
//
//        assertEquals(merged, hash1.merge(hash2));
//        assertEquals(merged, hash1);
//        assertEquals(origin2, hash2);
//
//        hash1 = Hashes.newHash();
//        merged.put(2, 4);
//        assertEquals(merged, hash2.merge(hash1));
//        assertEquals(origin1, hash1);
//        assertEquals(merged, hash2);
//
//        hash2 = Hashes.newHash();
//        assertEquals(origin1, hash1.merge(Hashes.newHash()));
//        assertEquals(origin1, Hashes.newHash().merge(hash1));
//
//        assertEquals(origin1, hash1.merge(null));
//
//        hash1.put(null, 1);
//        merged.put(null, 1);
//        merged.put(2, 3);
//        assertEquals(merged, hash1.merge(hash2));
    }

    @Test
    public void testConstructor() {
//        Hash<Integer, Integer> hash1 = Hashes.newHash();
//        hash1.put(1, 1);
//        hash1.put(2, 2);
//        Hash<Integer, Integer> hash2 = Hashes.newHash();
//        hash2.put(2, 2);
//        hash2.put(1, 1);
//        assertEquals(hash1, hash2);
//
//        Map<Integer, Integer> map = new HashMap<>();
//        map.put(1, 1);
//        map.put(2, 2);
//        Hash<Integer, Integer> hash3 = Hashes.newHash(map);
//        assertEquals(hash2, hash3);
//        assertThrows(NullPointerException.class, () -> Hashes.newHash(null));
    }

    @Test
    public void testKeysValues() {
//        Hash<Integer, Integer> hash = Hashes.newHash();
//        assertEquals(Seqs.newSeq(), hash.keys());
//        assertEquals(Seqs.newSeq(), hash.values());
//        hash.put(1, null);
//        hash.put(null, 1);
//        assertEquals(2, hash.keys().size());
//        assertTrue(hash.keys().contains(null));
//        assertTrue(hash.keys().contains(1));
//        assertEquals(2, hash.values().size());
//        assertTrue(hash.values().contains(null));
//        assertTrue(hash.values().contains(1));
    }
}
