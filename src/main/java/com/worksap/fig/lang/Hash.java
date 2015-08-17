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

package com.worksap.fig.lang;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * Elegant supplement for Map in JDK
 */
public interface Hash<K, V> {
    /**
     * Check whether this hash contains any key-value pair that satisfies the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return whether this hash contains any key-value pair that satisfies the condition
     * @throws NullPointerException if condition is null
     */
    boolean containsAny(BiPredicate<K, V> condition);

    /**
     * Returns <tt>true</tt> if this hash contains a mapping for the specified
     * key. More formally, returns <tt>true</tt> if and only if
     * this hash contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this hash is to be tested
     * @return <tt>true</tt> if this hash contains a mapping for the specified
     *         key
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this map
     * @throws NullPointerException if the specified key is null and this map
     *         does not permit null keys
     */
    boolean containsKey(K key);

    boolean containsValue(V value);

    boolean isEmpty();

    int size();

    V get(K k);

    /**
     * Returns a {@link Seq} view of the values contained in this hash.
     *
     * @return a seq view of the values contained in this hash
     */
    Seq<V> values();

    /**
     * Returns a {@link Seq} view of the keys contained in this hash.
     *
     * @return a seq view of the keys contained in this hash
     */
    Seq<K> keys();

    /**
     * Returns a {@link Seq} view of the mappings contained in this hash.
     *
     * @return a seq view of the mappings contained in this hash
     */
    Seq<Map.Entry<K, V>> entrySeq();

    Hash<K, V> put(K k, V v);

    Hash<K, V> putIfAbsent(K k, V v);

    /**
     * Return a new hash with the key-value pairs of the original Hash which satisfy the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return a new with only key-value pairs which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Hash<K, V> filter(BiPredicate<K, V> condition);

    /**
     * Return a new hash with the key-value pairs of the original Hash which don't satisfy the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return a new hash with key-value pairs which don't satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Hash<K, V> reject(BiPredicate<K, V> condition);

    /**
     * Returns a new hash created by using hash’s values as keys, and the keys as values.
     * If there are duplicated values, the last key is kept.
     * Since it is hash map, the order of keys is decided by hash table.
     *
     * @return an inverted hash
     */
    Hash<V, K> invert();

    /**
     * Returns a new hash containing the mappings of the specified hash and this hash itself.
     * The value for entries with duplicate keys will be that of the specified hash.
     *
     * @param another the specified hash to be merged
     * @return the new hash containing all the mappings of the specified hash and this hash itself
     */
    Hash<K, V> merge(Hash<? extends K, ? extends V> another);

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation). More formally, if this hash contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed. (The map can contain at most one such mapping.)
     *
     * @param key key whose mapping is to be removed from the map
     * @return a new hash after the key is removed
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *         is not supported by this hash
     * @throws ClassCastException if the key is of an inappropriate type for
     *         this hash
     * @throws NullPointerException if the specified key is null and this
     *         hash does not permit null keys
     */
    Hash<K, V> remove(K key);

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value.
     *
     * @param key key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return a new hash after the entry is removed
     * @throws UnsupportedOperationException if the {@code remove} operation
     *         is not supported by this hash
     * @throws ClassCastException if the key or value is of an inappropriate
     *         type for this hash
     * @throws NullPointerException if the specified key or value is null,
     *         and this map does not permit null keys or values
     */
    Hash<K, V> remove(K key, V value);

    /**
     * Returns a Seq of keys of the given value.
     *
     * @param value the value
     * @return the collection of keys whose value is the given value
     */
    Seq<K> keysOf(V value);
}
