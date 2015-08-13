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

    @Override
    MutableSeq<T> prepend(T value);

    @Override
    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> prepend(T... values);

    @Override
    MutableSeq<T> prepend(Collection<? extends T> collection);

    @Override
    MutableSeq<T> prepend(Seq<? extends T> seq);

    @Override
    MutableSeq<T> subSeq(int fromIndex, int toIndex);

    @Override
    MutableSeq<T> reject(Predicate<T> condition);

    @Override
    MutableSeq<T> reject(BiPredicate<T, Integer> condition);

    @Override
    MutableSeq<T> rejectWhile(Predicate<T> condition);

    @Override
    MutableSeq<T> rejectWhile(BiPredicate<T, Integer> condition);

    @Override
    MutableSeq<T> filter(Predicate<T> condition);

    @Override
    MutableSeq<T> filter(BiPredicate<T, Integer> condition);

    @Override
    MutableSeq<T> filterWhile(Predicate<T> condition);

    @Override
    MutableSeq<T> filterWhile(BiPredicate<T, Integer> condition);

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

    /**
     * In-place method of {@link #append(Object)}
     */
    MutableSeq<T> appendInPlace(T value);

    /**
     * In-place method of {@link #append(T...)}
     */
    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> appendInPlace(T... values);

    /**
     * In-place method of {@link #append(Collection)}
     */
    MutableSeq<T> appendInPlace(Collection<? extends T> collection);

    /**
     * In-place method of {@link #append(Seq)}
     */
    MutableSeq<T> appendInPlace(Seq<? extends T> seq);

    /**
     * In-place method of {@link #prepend(Object)}
     */
    MutableSeq<T> prependInPlace(T value);

    /**
     * In-place method of {@link #prepend(T...)}
     */
    @SuppressWarnings({"varargs", "unchecked"})
    MutableSeq<T> prependInPlace(T... values);

    /**
     * In-place method of {@link #prepend(Collection)}
     */
    MutableSeq<T> prependInPlace(Collection<? extends T> collection);

    /**
     * In-place method of {@link #prepend(Seq)}
     */
    MutableSeq<T> prependInPlace(Seq<? extends T> seq);


    /**
     * Remove all elements in the seq.
     *
     * @return The seq itself after changed.
     */
    MutableSeq<T> clear();

    /**
     * Update the element at the index.
     *
     * @return The seq itself after changed.
     */
    MutableSeq<T> set(int i, T t);

    /**
     * In-place method of {@link #shuffle()}
     */
    MutableSeq<T> shuffleInPlace();

    /**
     * In-place method of {@link #reverse()}
     */
    MutableSeq<T> reverseInPlace();

    /**
     * In-place method of {@link #distinct()}
     */
    MutableSeq<T> distinctInPlace();

    /**
     * In-place method of {@link #repeat(int)}
     */
    MutableSeq<T> repeatInPlace(int times);

    /**
     * In-place method of {@link #compact()}
     */
    MutableSeq<T> compactInPlace();

    /**
     * In-place method of {@link #sort(Comparator)}
     */
    MutableSeq<T> sortInPlace(Comparator<? super T> comparator);


    /**
     * In-place method of {@link #filter(Predicate)}
     */
    MutableSeq<T> filterInPlace(Predicate<T> condition);

    /**
     * In-place method of {@link #filter(BiPredicate)}
     */
    MutableSeq<T> filterInPlace(BiPredicate<T, Integer> condition);

    /**
     * In-place method of {@link #filterWhile(Predicate)}
     */
    MutableSeq<T> filterWhileInPlace(Predicate<T> condition);

    /**
     * In-place method of {@link #filterWhile(BiPredicate)}
     */
    MutableSeq<T> filterWhileInPlace(BiPredicate<T, Integer> condition);

    /**
     * In-place method of {@link #reject(Predicate)}
     */
    MutableSeq<T> rejectInPlace(Predicate<T> condition);

    /**
     * In-place method of {@link #reject(BiPredicate)}
     */
    MutableSeq<T> rejectInPlace(BiPredicate<T, Integer> condition);

    /**
     * In-place method of {@link #rejectWhile(Predicate)}
     */
    MutableSeq<T> rejectWhileInPlace(Predicate<T> condition);

    /**
     * In-place method of {@link #rejectWhile(BiPredicate)}
     */
    MutableSeq<T> rejectWhileInPlace(BiPredicate<T, Integer> condition);
}
