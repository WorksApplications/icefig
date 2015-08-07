package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by liuyang on 8/6/15.
 */
public class Seqs {

    public static <T> Seq<T> newSeq() {
        return new SeqImpl<>();
    }

    public static <T> Seq<T> newSeq(T value) {
        Collection<T> collection = new ArrayList<>();
        collection.add(value);
        return new SeqImpl<>(collection);
    }

    @SuppressWarnings({"varargs", "unchecked"})
    public static <T> Seq<T> newSeq(T... values) {
        return new SeqImpl<>(Arrays.asList(values));
    }

    public static <T> Seq<T> newSeq(Collection<T> values) {
        return new SeqImpl<>(values);
    }

    public static <T> MutableSeq<T> newMutableSeq() {
        return new SeqImpl<>();
    }

    public static <T> MutableSeq<T> newMutableSeq(T value) {
        Collection<T> collection = new ArrayList<>();
        collection.add(value);
        return new SeqImpl<>(collection);
    }

    @SuppressWarnings({"varargs", "unchecked"})
    public static <T> MutableSeq<T> newMutableSeq(T... values) {
        return new SeqImpl<>(Arrays.asList(values));
    }

    public static <T> MutableSeq<T> newMutableSeq(Collection<T> values) {
        return new SeqImpl<>(values);
    }
}
