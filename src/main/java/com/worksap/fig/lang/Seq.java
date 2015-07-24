package com.worksap.fig.lang;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by liuyang on 7/23/15.
 */
public interface Seq<T> extends List<T> {

    default <R> Seq<R> map(Function<T, R> func) {
        Seq<R> result = new SeqImpl<>();
        this.forEach(i -> result.add(func.apply(i)));
        return result;
    }

    default <R> Seq<R> mapWithIndex(BiFunction<T, Integer, R> func) {
        Seq<R> result = new SeqImpl<>();
        this.forEachWithIndex((s, i) -> result.add(func.apply(s, i)));
        return result;
    }

    default String join() {
        return join("");
    }

    default T first() {
        return get(0);
    }

    default T second() {
        return get(1);
    }

    default T third() {
        return get(2);
    }

    default T last() {
        return get(size() - 1);
    }

    default String join(CharSequence delimiter) {
        return join(delimiter, "", "");
    }

    default String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        forEachWithIndex((t, i) -> {
            if (i != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(t);
        });
        stringBuilder.append(suffix);
        return stringBuilder.toString();
    }

    default Seq<T> sample() {
        return sample(1);
    }

    default Seq<T> sample(int n) {
        Seq<T> shuffled = shuffle();
        return shuffled.subSeq(0, Math.min(n, this.size()));
    }

    default Seq<T> shuffle() {
        Seq<T> copy = new SeqImpl<>(this);
        Collections.shuffle(copy);
        return copy;
    }

    default Seq<T> shuffle$() {
        Collections.shuffle(this);
        return this;
    }

    default void forEachWithIndex(BiConsumer<? super T, Integer> action) {
        for (int i = 0; i < this.size(); i++) {
            action.accept(this.get(i), i);
        }
    }

    default Seq<Seq<T>> eachCons(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        Seq<Seq<T>> result = new SeqImpl<>();
        for (int i = 0; i <= this.size() - n; i++) {
            result.add(this.subSeq(i, i + n));
        }
        return result;
    }

    default Seq<T> order(Comparator<? super T> comparator) {
        Seq<T> copy = new SeqImpl<>(this);
        Collections.sort(copy, comparator);
        return copy;
    }

    default Seq<T> order$(Comparator<? super T> comparator) {
        Collections.sort(this, comparator);
        return this;
    }

    default Seq<T> distinct() {
        return new SeqImpl<>(new LinkedHashSet<>(this));
    }

    default void forEachCons(int n, Consumer<Seq<T>> action) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        for (int i = 0; i <= this.size() - n; i++) {
            action.accept(this.subSeq(i, i + n));
        }
    }

    @SafeVarargs
    static <T> Seq<T> of(T... values) {
        Collection<T> col = Arrays.asList(values);
        return new SeqImpl<>(col);
    }

    static <T> Seq<T> newSeq() {
        return new SeqImpl<>();
    }

    static <T> Seq<T> of(Collection<? extends T> values) {
        return new SeqImpl<>(values);
    }

    //override return type
    default Seq<T> subSeq(int fromIndex, int toIndex) {
        return new SeqImpl<>(this.subList(fromIndex, toIndex));
    }
}
