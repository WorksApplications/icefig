package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by liuyang on 7/23/15.
 */
public class ArrayImpl1<T> implements Array<T> {

    private final List<T> list;

    public ArrayImpl1(List<T> list) {
        this.list = list;
    }

    @Override
    public <R> Array<R> map(Function<T, R> func) {
        List<R> result = new ArrayList<>();
        list.forEach(i -> result.add(func.apply(i)));
        return new ArrayImpl1<>(result);
    }

    @Override
    public List<T> toList() {
        return list;
    }
}
