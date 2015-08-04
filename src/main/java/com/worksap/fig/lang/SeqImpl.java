package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

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
     *
     * @param index index of the element to return
     * @return the element at the specified position in this seq
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &gt;= size() || index &lt; -size()</tt>)
     */
    @Override
    public T get(int index) {
        int size = size();
        if (index >= size || index < -size)
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size + ", should be within [" + (-size) + ", " + size + ")");
        if (index >= 0)
            return super.get(index);
        else
            return super.get(size + index);
    }

    private Seq<T> indexToSeq(int[] idxs) {
        Seq<T> result = Seq.newSeq();
        for (int i : idxs) {
            result.add(get(i));
        }
        return result;
    }

    public void forEachCombination(int n, Consumer<Seq<T>> action) {
        Objects.requireNonNull(action);
        if (n <= 0) {
            throw new IllegalArgumentException("n should be a positive number");
        }
        if (n > size()) {
            return;
        }
        //Selected element indices of a valid combination
        int[] comb = new int[n];

        //initialize first combination by the first n elements
        for (int i = 0; i < n; i++) {
            comb[i] = i;
        }
        action.accept(indexToSeq(comb));

        while (comb[0] < size() - n) {
            for (int i = 0; i < n; i++) {
                if (i == n - 1 || comb[i + 1] - comb[i] > 1) { // find the first selected element that the next element of it is not selected
                    comb[i]++; // make the next element selected instead
                    // set all selected elements before i, to the beginning elements
                    for (int j = 0; j < i; j++) {
                        comb[j] = j;
                    }
                    action.accept(indexToSeq(comb));
                    break;
                }
            }
        }
    }

    public Seq<Seq<T>> eachCombination(int n) {
        Seq<Seq<T>> result = Seq.newSeq();
        forEachCombination(n, result::add);
        return result;
    }
}
