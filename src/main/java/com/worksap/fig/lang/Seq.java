package com.worksap.fig.lang;

import java.util.*;
import java.util.function.Function;

/**
 * Created by liuyang on 7/23/15.
 */
public interface Seq<T> extends List<T>{

    default <R> Seq<R> map(Function<T, R> func){
        Seq<R> result = new SeqImpl<>();
        this.forEach(i -> result.add(func.apply(i)));
        return result;
    }

    default Seq<T> sample() {
        return sample(1);
    }

    default Seq<T> sample(int n){
        Seq<T> shuffled = shuffle();
        return shuffled.subList(0, Math.min(n, this.size()));
    }

    default Seq<T> shuffle(){
        Seq<T> copy = new SeqImpl<>(this);
        Collections.shuffle(copy);
        return copy;
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
