package com.worksap.fig.lang;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * Elegant supplement for Map in JDK
 */
public interface Hash<K, V> extends Map<K, V> {
    /**
     * Check whether this hash contains any key-value pair that satisfies the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return whether this hash contains any key-value pair that satisfies the condition
     * @throws NullPointerException if condition is null
     */
    default boolean containsAny(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        for (Entry<K, V> entry : entrySet()) {
            if (condition.test(entry.getKey(), entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    default Seq<Entry<K, V>> entrySeq() {
        return Seq.of(entrySet());
    }

    Seq<V> values();

    Seq<K> keys();

    /**
     * Returns a new hash created by using hash’s values as keys, and the keys as values.
     * If there are duplicated values, the last key is kept.
     * Since it is hash map, the order of keys is decided by hash table.
     *
     * @return an inverted hash
     */
    default Hash<V, K> invert() {
        Hash<V, K> result = newHash();
        forEach((k, v) -> result.put(v, k));
        return result;
    }

    /**
     * Return a new hash with the key-value pairs of the original Hash which don't satisfy the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return a new hash with key-value pairs which don't satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Hash<K, V> reject(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Hash<K, V> result = newHash();
        forEach((k, v) -> {
            if (!condition.test(k, v)) {
                result.put(k, v);
            }
        });
        return result;
    }

    /**
     * Return a new hash with the key-value pairs of the original Hash which satisfy the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return a new with only key-value pairs which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Hash<K, V> filter(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Hash<K, V> result = newHash();
        forEach((k, v) -> {
            if (condition.test(k, v)) {
                result.put(k, v);
            }
        });
        return result;
    }

    default Hash<K, V> set(K key, V value) {
        put(key, value);
        return this;
    }

    static <K, V> Hash<K, V> newHash() {
        return new HashImpl<>();
    }

    static <K, V> Hash<K, V> of(Map<? extends K, ? extends V> map) {
        return new HashImpl<>(map);
    }

    /**
     * Removes all key-value pairs which satisfy the condition on the hash itself.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return this hash itself with all key-value pairs which don't satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Hash<K, V> reject$(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Seq<K> toBeRemoved = Seq.newSeq();
        this.forEach((k, v) -> {
            if (condition.test(k, v)) {
                toBeRemoved.add(k);
            }
        });
        if (!toBeRemoved.isEmpty()) {
            toBeRemoved.forEach((Consumer<K>) this::remove);
        }
        return this;
    }

    /**
     * Keep only the key-value pairs of this Hash which satisfy the condition.
     *
     * @param condition the condition used to filter key-value pairs by passing the key and value of the pair,
     *                  returns true if the key-value pair satisfies the condition, otherwise returns false.
     * @return this hash itself with only key-value pairs which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Hash<K, V> filter$(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Seq<K> toBeRemoved = Seq.newSeq();
        this.forEach((k, v) -> {
            if (!condition.test(k, v)) {
                toBeRemoved.add(k);
            }
        });
        if (!toBeRemoved.isEmpty()) {
            toBeRemoved.forEach((Consumer<K>) this::remove);
        }
        return this;
    }

    /**
     * Returns the keys of the given value.
     *
     * @param value the value
     * @return the collection of keys whose value is the given value
     */
    default Seq<K> keysOf(V value) {
        Seq<K> result = Seq.newSeq();
        this.forEach((k, v) -> {
            if (value == null && v == null)
                result.add(k);
            else if (value != null && v != null && v.equals(value))
                result.add(k);
        });
        return result;
    }

    /**
     * Returns a new hash containing the mappings of the specified hash and this hash itself.
     * The value for entries with duplicate keys will be that of the specified hash.
     *
     * @param another the specified hash to be merged
     * @return the new hash containing all the mappings of the specified hash and this hash itself
     */
    default Hash<K, V> merge(Hash<? extends K, ? extends V> another) {
        Hash<K, V> result = Hash.of(this);
        if (another != null)
            result.putAll(another);
        return result;
    }

    /**
     * Copies all of the mappings from the specified hash to this hash.
     * The value for entries with duplicate keys will be that of the specified hash.
     * <p>
     *     Similar to {@link #merge(Hash)}, the difference is that this function takes effect on this hash itself.
     * </p>
     *
     * @param another the specified hash to be merged
     * @return the hash itself containing all the mappings of the specified hash
     */
    default Hash<K, V> merge$(Hash<? extends K, ? extends V> another) {
        if (another != null)
            this.putAll(another);
        return this;
    }
}