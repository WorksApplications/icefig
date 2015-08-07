package com.worksap.fig.lang;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Created by lijunxiao on 8/6/15.
 */
class HashImpl<K, V> implements MutableHash<K, V> {
    private Map<K, V> hash;

    protected HashImpl() {
        this.hash = new HashMap<>();
    }

    protected HashImpl(Map<? extends K, ? extends V> m) {
        this.hash = new HashMap<>(m);
    }

    @Override
    public boolean containsAny(BiPredicate<K, V> condition) {
        for (Map.Entry<K, V> entry : hash.entrySet()) {
            if (condition.test(entry.getKey(), entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(K k) {
        return hash.containsKey(k);
    }

    @Override
    public boolean containsValue(V v) {
        return hash.containsValue(v);
    }

    @Override
    public boolean isEmpty() {
        return hash.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof HashImpl) {
            HashImpl<K, V> h = (HashImpl<K, V>)o;
            return hash.equals(h.hash);
        }
        return false;
    }

    @Override
    public int size() {
        return hash.size();
    }

    @Override
    public V get(K k) {
        return hash.get(k);
    }

    @Override
    public Seq<V> values() {
        return Seqs.newMutableSeq(hash.values());
    }

    @Override
    public Seq<K> keys() {
        return Seqs.newMutableSeq(hash.keySet());
    }

    @Override
    public Seq<Map.Entry<K, V>> entrySeq() {
        return Seqs.newMutableSeq(hash.entrySet());
    }

    @Override
    public MutableHash<K, V> put(K k, V v) {
        Map<K, V> newHash = new HashMap<>(hash);
        newHash.put(k, v);
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> putIfAbsent(K k, V v) {
        Map<K, V> newHash = new HashMap<>(hash);
        newHash.putIfAbsent(k, v);
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> filter(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Map<K, V> newHash = new HashMap<>();

        this.hash.forEach((k, v) -> {
            if (condition.test(k, v)) {
                newHash.put(k, v);
            }
        });
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> reject(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        Map<K, V> newHash = new HashMap<>();

        hash.forEach((k, v) -> {
            if (!condition.test(k, v)) {
                newHash.put(k, v);
            }
        });
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<V, K> invert() {
        Map<V, K> newHash = new HashMap<>();

        hash.forEach((k, v) -> newHash.put(v, k));
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> merge(Hash<? extends K, ? extends V> another) {
        Map<K, V> newHash = new HashMap<>(hash);
        if (another != null) {
            another.entrySeq().forEach(entry -> {
                newHash.put(entry.getKey(), entry.getValue());
            });
        }
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> remove(K k) {
        Map<K, V> newHash = new HashMap<>(hash);
        newHash.remove(k);
        return new HashImpl<>(newHash);
    }

    @Override
    public MutableHash<K, V> filterInPlace(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        final Iterator<Map.Entry<K, V>> each = hash.entrySet().iterator();
        while (each.hasNext()) {
            Map.Entry<K, V> nextEntry = each.next();
            if (!condition.test(nextEntry.getKey(), nextEntry.getValue())) {
                each.remove();
            }
        }
        return this;
    }

    @Override
    public MutableHash<K, V> rejectInPlace(BiPredicate<K, V> condition) {
        Objects.requireNonNull(condition);
        final Iterator<Map.Entry<K, V>> each = hash.entrySet().iterator();
        while (each.hasNext()) {
            Map.Entry<K, V> nextEntry = each.next();
            if (condition.test(nextEntry.getKey(), nextEntry.getValue())) {
                each.remove();
            }
        }
        return this;
    }

    @Override
    public MutableHash<K, V> mergeInPlace(Hash<? extends K, ? extends V> another) {
        if (another != null) {
            another.entrySeq().forEach(entry -> {
                hash.put(entry.getKey(), entry.getValue());
            });
        }
        return this;
    }

    @Override
    public MutableHash<K, V> clear() {
        hash.clear();
        return this;
    }

    @Override
    public MutableHash<K, V> putInPlace(K k, V v) {
        hash.put(k, v);
        return this;
    }

    @Override
    public MutableHash<K, V> putIfAbsentInPlace(K k, V v) {
        hash.putIfAbsent(k, v);
        return this;
    }

    @Override
    public MutableHash<K, V> removeInPlace(K k) {
        hash.remove(k);
        return this;
    }

    @Override
    public Seq<K> keysOf(V value) {
        MutableSeq<K> result = Seqs.newMutableSeq();
        hash.forEach((k, v) -> {
            if (value == null && v == null)
                result.appendInPlace(k);
            else if (value != null && v != null && v.equals(value))
                result.appendInPlace(k);
        });
        return result;
    }
}
