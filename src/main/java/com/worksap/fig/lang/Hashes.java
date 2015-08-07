package com.worksap.fig.lang;

import java.util.Map;

/**
 * Factory class for construct Hash and MutableHash
 */
public class Hashes {
    private Hashes() {

    }

    public static <K, V> Hash<K, V> newHash() {
        return new HashImpl<>();
    }

    public static <K, V> Hash<K, V> newHash(Map<K, V> map) {
        return new HashImpl<>(map);
    }

    public static <K, V> MutableHash<K, V> newMutableHash() {
        return new HashImpl<>();
    }

    public static <K, V> MutableHash<K, V> newMutableHash(Map<K, V> map) {
        return new HashImpl<>(map);
    }
}
