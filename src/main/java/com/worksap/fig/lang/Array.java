package com.worksap.fig.lang;

import java.util.List;
import java.util.function.Function;

/**
 * Created by liuyang on 7/23/15.
 */
public interface Array<T>{

    <R> Array<R> map(Function<T, R> func);

    List<T> toList();
}
