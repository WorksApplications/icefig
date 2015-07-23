package com.worksap.fig.lang;

import java.util.*;
import java.util.function.*;

/**
 * Created by liuyang on 7/23/15.
 */
class SeqImpl<T> extends ArrayList<T> implements Seq<T> {
    SeqImpl(Collection<? extends T> c) {
        super(c);
    }

    SeqImpl(int initialCapacity) {
        super(initialCapacity);
    }

    SeqImpl() {
    }

    public Seq<T> subList(int fromIndex, int toIndex){
        return new SeqImpl<>(super.subList(fromIndex, toIndex));
    }
}
