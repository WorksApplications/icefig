/*
 * Copyright (C) 2015 The Fig Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worksap.icefig.lang;

import com.worksap.icefig.lang.*;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lijunxiao on 8/6/15.
 */
public class HashTest {
    @Test
    public void testConstruction() {
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
        assertEquals(0, hash.clear().size());
        assertEquals(Hashes.newMutableHash(), hash);

        Hash<String, String> hash1 = Hashes.<String, String>newHash().put("3", "Chris Paul").put("30", "Stephen Curry");
        assertEquals(2, hash1.size());
        assertEquals(3, hash1.put("7", "Carmelo Anthony").size());
        assertEquals(2, hash1.size());
        assertEquals("Chris Paul", hash1.putIfAbsent("3", "Li").get("3"));
        assertEquals(1, hash1.remove("3").size());
        assertEquals(null, hash1.remove("3").get("3"));
        assertEquals("Chris Paul", hash1.get("3"));

        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        Hash<String, String> hash2 = Hashes.<String, String>newHash().put("key", "value");
        assertEquals(map, hash2.toHashMap());
    }

    @Test
    public void testContainsAny() {
        MutableHash<Integer, Integer> hash = Hashes.newMutableHash(new HashMap<>());
        hash.putInPlace(1, 2).putInPlace(2, 4).putInPlace(5, 1);

        assertTrue(hash.containsAny((k, v) -> k + v == 3));
        assertTrue(hash.containsAny((k, v) -> k + v == 6));
        assertFalse(hash.containsAny((k, v) -> k + v == 5));
        Helpers.assertThrows(NullPointerException.class, () -> hash.containsAny(null));
    }

    @Test
    public void testInvert() {
        Hash<String, String> hash =
                Hashes.<String, String>newHash(new HashMap<>()).put("en", "British English").put("ja", "Japanese").
                        put("zh", "Chinese").put("zh-CN", "Chinese");

        Hash<String, String> invertedHash = hash.invert();
        assertEquals(invertedHash.size(), 3);
        assertEquals(invertedHash.get("British English"), "en");
        assertEquals(invertedHash.get("Japanese"), "ja");
        assertEquals(invertedHash.get("Chinese"), "zh");
    }

    @Test
    public void testReject(){
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4).put(5, 1);
        Hash<Integer, Integer> rejected = Hashes.<Integer, Integer>newHash().put(2, 4).put(5, 1);

        assertTrue(rejected.equals(hash.reject((k, v) -> k + v != 6)));
        assertEquals(Hashes.newHash(), hash.reject((k, v) -> !Objects.equals(k, v)));
        assertNotEquals(Hashes.newHash(), hash);
        Helpers.assertThrows(NullPointerException.class, () -> hash.reject(null));
    }

    @Test
    public void testFilter(){
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4).put(5, 1);
        Hash<Integer, Integer> origin = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4).put(5, 1);
        assertEquals(origin, hash);

        Hash<Integer, Integer> filtered = Hashes.<Integer, Integer>newHash().put(1, 2);
        assertTrue(filtered.equals(hash.filter((k, v) -> k + v != 6)));
        assertFalse(filtered.equals(hash));
        assertEquals(origin, hash);
        assertEquals(Hashes.newHash(), hash.filter(Objects::equals));
        assertNotEquals(Hashes.newHash(), hash);
        assertEquals(origin, hash);
        Helpers.assertThrows(NullPointerException.class, () -> hash.filter(null));
    }

    @Test
    public void testRejectInplace() {
        MutableHash<Integer, Integer> hash = Hashes.newMutableHash();
        hash.putInPlace(1, 2).putInPlace(2, 4).putInPlace(5, 1);
        assertEquals(2, hash.rejectInPlace((k, v) -> k > 1 && v > 1).size());
        assertEquals(2, hash.size());
        assertEquals(null, hash.get(2));
        assertEquals(2, hash.rejectInPlace(Objects::equals).size());
        assertEquals(Hashes.newHash(), hash.rejectInPlace((k, v) -> !Objects.equals(k, v)));
        Helpers.assertThrows(NullPointerException.class, () -> hash.rejectInPlace(null));
    }

    @Test
    public void testFilterInplace() {
        MutableHash<Integer, Integer> hash = Hashes.<Integer, Integer>newMutableHash();
        hash.putInPlace(1, 2).putInPlace(2, 4).putInPlace(5, 1);
        assertEquals(3, hash.size());
        assertEquals(2, hash.filterInPlace((k, v) -> !(k > 1 && v > 1)).size());
        assertEquals(null, hash.get(2));
        assertEquals(2, hash.filterInPlace((k, v) -> !Objects.equals(k, v)).size());
        assertEquals(Hashes.newHash(), hash.filterInPlace(Objects::equals));
        Helpers.assertThrows(NullPointerException.class, () -> hash.filterInPlace(null));
    }

    @Test
    public void testKeysOf() {
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4).put(3, 2).put(4, null).put(5, 1);
        Seq<Integer> keys = hash.keysOf(2);
        keys.sort(Integer::compare);
        assertEquals(Seqs.newSeq(1, 3), keys);
        assertEquals(Seqs.newSeq(2), hash.keysOf(4));
        assertEquals(Seqs.newSeq(), hash.keysOf(3));
        assertEquals(Seqs.newSeq(4), hash.keysOf(null));
    }

    @Test
    public void testMerge() {
        Hash<Integer, Integer> hash1 = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4);
        Hash<Integer, Integer> origin1 = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 4);
        Hash<Integer, Integer> hash2 = Hashes.<Integer, Integer>newHash().put(3, 2).put(2, 3);
        Hash<Integer, Integer> origin2 = Hashes.<Integer, Integer>newHash().put(3, 2).put(2, 3);

        Hash<Integer, Integer> merged = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 3).put(3, 2);

        assertEquals(merged, hash1.merge(hash2));
        assertEquals(origin1, hash1);
        assertEquals(origin2, hash2);

        merged = merged.put(2, 4);
        assertEquals(merged, hash2.merge(hash1));
        assertEquals(origin1, hash1);
        assertEquals(origin2, hash2);
        assertEquals(origin1, hash1.merge(Hashes.newHash()));
        assertEquals(origin1, Hashes.newHash().merge(hash1));

        assertEquals(origin1, hash1.merge(null));

        hash1 = hash1.put(null, 1);
        merged = merged.put(null, 1);
        merged = merged.put(2, 3);
        assertEquals(merged, hash1.merge(hash2));
    }

    @Test
    public void testMergeInplace() {
        MutableHash<Integer, Integer> hash1 = Hashes.newMutableHash();
        hash1.putInPlace(1, 2).putInPlace(2, 4);
        MutableHash<Integer, Integer> origin1 = Hashes.newMutableHash();
        origin1.putInPlace(1, 2).putInPlace(2, 4);

        MutableHash<Integer, Integer> hash2 = Hashes.newMutableHash();
        hash2.putInPlace(3, 2).putInPlace(2, 3);
        MutableHash<Integer, Integer> origin2 = Hashes.newMutableHash();
        origin2.putInPlace(3, 2).putInPlace(2, 3);

        MutableHash<Integer, Integer> merged = Hashes.newMutableHash();
        merged.putInPlace(1, 2).putInPlace(2, 3).putInPlace(3, 2);

        assertEquals(merged, hash1.mergeInPlace(hash2));
        assertEquals(merged, hash1);
        assertEquals(origin2, hash2);

        hash1 = Hashes.newMutableHash();
        hash1.putInPlace(1, 2).putInPlace(2, 4);
        merged.putInPlace(2, 4);
        assertEquals(merged, hash2.mergeInPlace(hash1));
        assertEquals(origin1, hash1);
        assertEquals(merged, hash2);

        hash2 = Hashes.newMutableHash();
        hash2.putInPlace(3, 2).putInPlace(2, 3);
        assertEquals(origin1, hash1.merge(Hashes.newHash()));
        assertEquals(origin1, Hashes.newHash().merge(hash1));

        assertEquals(origin1, hash1.mergeInPlace(null));

        hash1.putInPlace(null, 1);
        merged.putInPlace(null, 1);
        merged.putInPlace(2, 3);
        assertEquals(merged, hash1.mergeInPlace(hash2));
    }

    @Test
    public void testKeysValues() {
        Hash<Integer, Integer> hash = Hashes.newHash();
        assertEquals(Seqs.newSeq(), hash.keys());
        assertEquals(Seqs.newSeq(), hash.values());

        hash = hash.put(1, null).put(null, 1);
        assertEquals(2, hash.keys().size());
        assertTrue(hash.keys().contains(null));
        assertTrue(hash.keys().contains(1));
        assertEquals(2, hash.values().size());
        assertTrue(hash.values().contains(null));
        assertTrue(hash.values().contains(1));
        assertTrue(hash.containsKey(1));
        assertTrue(hash.containsKey(null));
        assertTrue(hash.containsValue(1));
        assertTrue(hash.containsValue(null));
        assertFalse(hash.containsValue(2));
        assertFalse(hash.containsKey(2));
    }

    @Test
    public void testEquals() {
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(3, 4);
        Hash<Integer, Integer> hash2 = hash;
        Hash<Integer, Integer> hash3 = Hashes.<Integer, Integer>newHash().put(1, 2).put(3, 4);
        assertTrue(hash.equals(hash2));
        assertFalse(hash.equals(null));
        assertTrue(hash.equals(hash3));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor constructor = Hashes.class.getDeclaredConstructor();
        assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testRemove() {
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(3, 4);
        assertEquals(2, hash.size());

        Hash<Integer, Integer> another = hash.remove(1);
        assertEquals(2, hash.size());
        assertEquals(1, another.size());

        another = hash.remove(1, 3);
        assertEquals(2, hash.size());
        assertEquals(2, another.size());

        another = hash.remove(2, 2);
        assertEquals(2, hash.size());
        assertEquals(2, another.size());

        another = hash.remove(1, 2);
        assertEquals(2, hash.size());
        assertEquals(1, another.size());

        MutableHash<Integer, Integer> mutableHash = Hashes.<Integer, Integer>newMutableHash().putInPlace(1, 2).putInPlace(3, 4);
        assertEquals(2, mutableHash.size());

        MutableHash<Integer, Integer> anotherMutableHash = mutableHash.removeInPlace(1);
        assertEquals(1, mutableHash.size());
        assertEquals(1, anotherMutableHash.size());
        mutableHash.putInPlace(1, 2);

        anotherMutableHash = mutableHash.removeInPlace(1, 3);
        assertEquals(2, mutableHash.size());
        assertEquals(2, anotherMutableHash.size());

        anotherMutableHash = mutableHash.removeInPlace(2, 2);
        assertEquals(2, mutableHash.size());
        assertEquals(2, anotherMutableHash.size());

        anotherMutableHash = mutableHash.removeInPlace(1, 2);
        assertEquals(1, mutableHash.size());
        assertEquals(1, anotherMutableHash.size());
    }

    @Test
    public void testReplace() {
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(3, 4);
        Hash<Integer, Integer> another = hash.replace(1, 3);
        assertEquals(new Integer(2), hash.get(1));
        assertEquals(new Integer(3), another.get(1));

        another = hash.replace(4, 3);
        assertNull(hash.get(4));
        assertNull(another.get(4));

        another = hash.replace(1, 2, 10);
        assertEquals(new Integer(2), hash.get(1));
        assertEquals(new Integer(10), another.get(1));

        another = hash.replace(1, 3, 11);
        assertEquals(new Integer(2), hash.get(1));
        assertEquals(new Integer(2), another.get(1));

        another = hash.replaceAll((k, v) -> k + v);
        assertEquals(new Integer(2), hash.get(1));
        assertEquals(new Integer(3), another.get(1));

        hash = hash.put(null, null);
        another = hash.replace(null, 1);
        assertEquals(null, hash.get(null));
        assertEquals(new Integer(1), another.get(null));

        another = hash.replace(null, null, 1);
        assertEquals(null, hash.get(null));
        assertEquals(new Integer(1), another.get(null));

        MutableHash<Integer, Integer> mutableHash = Hashes.<Integer, Integer>newMutableHash().putInPlace(1, 2).putInPlace(3, 4);
        MutableHash<Integer, Integer> anotherMutableHash = mutableHash.replaceInPlace(1, 3);
        assertEquals(new Integer(3), mutableHash.get(1));
        assertEquals(new Integer(3), anotherMutableHash.get(1));

        anotherMutableHash = mutableHash.replaceInPlace(4, 3);
        assertNull(mutableHash.get(4));
        assertNull(anotherMutableHash.get(4));

        anotherMutableHash = mutableHash.replaceInPlace(1, 3, 10);
        assertEquals(new Integer(10), mutableHash.get(1));
        assertEquals(new Integer(10), anotherMutableHash.get(1));

        anotherMutableHash = mutableHash.replaceInPlace(1, 3, 11);
        assertEquals(new Integer(10), mutableHash.get(1));
        assertEquals(new Integer(10), anotherMutableHash.get(1));

        anotherMutableHash = mutableHash.replaceAllInPlace((k, v) -> k + v);
        assertEquals(new Integer(11), mutableHash.get(1));
        assertEquals(new Integer(11), anotherMutableHash.get(1));

        mutableHash.putInPlace(null, null);
        anotherMutableHash = mutableHash.replaceInPlace(null, 3);
        assertEquals(new Integer(3), mutableHash.get(null));
        assertEquals(new Integer(3), anotherMutableHash.get(null));

        anotherMutableHash = mutableHash.replaceInPlace(null, null, 3);
        assertEquals(new Integer(3), mutableHash.get(null));
        assertEquals(new Integer(3), anotherMutableHash.get(null));

    }

    @Test
    public void testCount() {
        Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 2).put(3, 4).put(null, null);
        assertEquals(1, hash.count(4));
        assertEquals(2, hash.count(2));
        assertEquals(0, hash.count(3));
        assertEquals(1, hash.count(null));
        assertEquals(2, hash.countIf((k, v) -> k != null && v < 3));

        MutableHash<Integer, Integer> mutableHash = Hashes.<Integer, Integer>newMutableHash().putInPlace(1, 2).putInPlace(2, 2).putInPlace(3, 4);
        assertEquals(1, mutableHash.count(4));
        assertEquals(2, mutableHash.count(2));
        assertEquals(0, mutableHash.count(3));
        assertEquals(2, mutableHash.countIf((k, v) -> v < 3));
    }
}
