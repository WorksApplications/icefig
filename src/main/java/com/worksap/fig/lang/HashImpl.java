package com.worksap.fig.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyang on 7/27/15.
 */
class HashImpl<K, V> extends HashMap<K, V> implements Hash<K, V> {

    public HashImpl() {
    }

    public HashImpl(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public Seq<V> values() {
        return Seq.of(super.values());
    }

    @Override
    public Seq<K> keys() {
        return Seq.of(keySet());
    }
}
