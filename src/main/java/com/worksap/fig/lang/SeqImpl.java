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

    /**
     * Returns the element at index. A negative index counts from the end of self.
     * @param index index of the element to return
     * @return the element at the specified position in this seq
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &gt;= size() || index &lt; -size()</tt>)
     */
    @Override
    public T get(int index) {
        int size = size();
        if (index >= size || index < - size)
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size + ", should be within [" + (-size) + ", " + size + ")");
        if (index >= 0)
            return super.get(index);
        else
            return super.get(size + index);
    }
}
