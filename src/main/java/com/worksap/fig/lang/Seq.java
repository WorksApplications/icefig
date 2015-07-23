package com.worksap.fig.lang;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by liuyang on 7/23/15.
 */
public interface Seq<T> extends List<T> {

    default <R> Seq<R> map(Function<T, R> func) {
        Seq<R> result = new SeqImpl<>();
        this.forEach(i -> result.add(func.apply(i)));
        return result;
    }

    default Seq<T> sample() {
        return sample(1);
    }

    default Seq<T> sample(int n) {
        Seq<T> shuffled = shuffle();
        return shuffled.subList(0, Math.min(n, this.size()));
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

    default void forEachCons(int n, Consumer<List<T>> action) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        for (int i = 0; i <= this.size() - n; i++) {
            action.accept(this.subList(i, i + n));
        }
    }

    @SafeVarargs
    static <T> Seq<T> of(T... values) {
        Collection<T> col = Arrays.asList(values);
        return new SeqImpl<>(col);
    }

    static <T> Seq<T> of(Collection<? extends T> values) {
        return new SeqImpl<>(values);
    }

    //override return type
    Seq<T> subList(int fromIndex, int toIndex);
}
