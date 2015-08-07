package com.worksap.fig.lang;

import java.util.function.BiPredicate;

/**
 * An interface extending {@link com.worksap.fig.lang.Hash} (which is immutable), with additional in-place methods to change the hash itself.
 * Those methods are generally named xxxInPlace
 */
public interface MutableHash<K, V> extends Hash<K, V> {
    @Override
    MutableHash<K, V> put(K k, V v);

    @Override
    MutableHash<K, V> putIfAbsent(K k, V v);

    @Override
    MutableHash<K, V> filter(BiPredicate<K, V> condition);

    @Override
    MutableHash<K, V> reject(BiPredicate<K, V> condition);

    @Override
    MutableHash<V, K> invert();

    @Override
    MutableHash<K, V> merge(Hash<? extends K, ? extends V> another);

    @Override
    MutableHash<K, V> remove(K k);

    @Override
    Seq<K> keysOf(V v);

    MutableHash<K, V> putInPlace(K k, V v);

    MutableHash<K, V> putIfAbsentInPlace(K k, V v);

    MutableHash<K, V> removeInPlace(K k);

    MutableHash<K, V> filterInPlace(BiPredicate<K, V> condition);

    MutableHash<K, V> rejectInPlace(BiPredicate<K, V> condition);

    MutableHash<K, V> mergeInPlace(Hash<? extends K, ? extends V> another);
}
