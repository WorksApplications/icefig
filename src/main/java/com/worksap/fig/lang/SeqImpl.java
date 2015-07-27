package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by liuyang on 7/23/15.
 */
class SeqImpl<T> extends ArrayList<T> implements Seq<T> {
    SeqImpl(Collection<? extends T> c) {
        super(c);
    }

    SeqImpl() {
    }

    SeqImpl(int size) {
        this(size, null);
    }
    SeqImpl(int size, T defaultValue) {
        super(size);
        while (size > 0) {
            this.add(defaultValue);
            size--;
        }
    }
}
