package com.worksap.fig.lang;

import java.util.*;
import java.util.function.*;

/**
 * An interface extending {@link com.worksap.fig.lang.Seq} (which is immutable), with additional in-place methods to change the seq itself.
 * Those methods are generally named xxxInPlace
 */
public interface MutableSeq<T> extends Seq<T> {

    @Override
    <R> MutableSeq<R> map(Function<T, R> func);

    @Override
    <R> MutableSeq<R> map(BiFunction<T, Integer, R> func);

    @Override
    <R> MutableSeq<R> flatMap(Function<T, Seq<R>> func);

    @Override
    <R> MutableSeq<R> flatMap(BiFunction<T, Integer, Seq<R>> func);

    @Override
    MutableSeq<T> sample(int n);

    @Override
    MutableSeq<T> shuffle();

    @Override
    MutableSeq<MutableSeq<T>> eachCons(int n);

    @Override
    MutableSeq<T> sort(Comparator<? super T> comparator);

    @Override
    MutableSeq<T> distinct();

    @Override
    MutableSeq<T> append(T value);

    @Override
    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> append(T... values);

    @Override
    MutableSeq<T> append(Collection<? extends T> collection);

    @Override
    MutableSeq<T> append(Seq<? extends T> seq);


    MutableSeq<T> appendInPlace(T value);

    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> appendInPlace(T... values);

    MutableSeq<T> appendInPlace(Collection<? extends T> collection);

    MutableSeq<T> appendInPlace(Seq<? extends T> seq);

    @Override
    MutableSeq<T> prepend(T value);

    @Override
    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> prepend(T... values);

    @Override
    MutableSeq<T> prepend(Collection<? extends T> collection);

    @Override
    MutableSeq<T> prepend(Seq<? extends T> seq);

    MutableSeq<T> prependInPlace(T value);

    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> prependInPlace(T... values);

    MutableSeq<T> prependInPlace(Collection<? extends T> collection);

    MutableSeq<T> prependInPlace(Seq<? extends T> seq);

    @Override
    MutableSeq<T> subSeq(int fromIndex, int toIndex);

    @Override
    MutableSeq<T> reject(Predicate<T> condition);

    @Override
    MutableSeq<T> reject(BiPredicate<T, Integer> condition);

    @Override
    MutableSeq<T> filter(Predicate<T> condition);

    @Override
    MutableSeq<T> filter(BiPredicate<T, Integer> condition);

    @Override
    MutableSeq<T> repeat(int times);

    @Override
    default MutableSeq<T> compact() {
        return this.reject(e -> e == null);
    }

    @Override
    MutableSeq<MutableSeq<T>> eachSlice(int n);

    @Override
    MutableSeq<T> reverse();

    @Override
    MutableSeq<MutableSeq<T>> eachCombination(int n);

    MutableSeq<T> clear();

    MutableSeq<T> set(int i, T t);
}
