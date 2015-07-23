package com.worksap.fig.lang;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by liuyang on 7/23/15.
 */
public class ArrayImpl<T> implements Array<T> {

    private final List<T> list;

    public ArrayImpl(List<T> list) {
        this.list = list;
    }

    @Override
    public <R> Array<R> map(Function<T, R> func) {
        return new ArrayImpl<>(list.stream().map(func).collect(Collectors.toList()));
    }

    @Override
    public List<T> toList() {
        return list;
    }

}
