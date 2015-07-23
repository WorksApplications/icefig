package com.worksap.fig;

import com.worksap.fig.lang.Array;
import com.worksap.fig.lang.ArrayImpl;
import com.worksap.fig.lang.ArrayImpl1;
import org.junit.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 7/23/15.
 */
public class ArrayTest {
    @Test
    public void test(){

        List<Integer> list = new ArrayList<>();
        for(int i=0; i<=10000000; i++) {
            list.add(i);
        }

        List<Integer> list2 = new ArrayList<>();
        for(int i=0; i<=10000000; i++) {
            list2.add(i);
        }

        System.out.println("finish init");

        LocalTime time = LocalTime.now();
        Array<Integer> array1 = new ArrayImpl<>(list);
        array1.map(i -> i + 1);
        System.out.println(ChronoUnit.MILLIS.between(time, LocalTime.now()));

        time = LocalTime.now();
        array1 = new ArrayImpl1<>(list2);
        array1.map(i -> i + 1);
        System.out.println(ChronoUnit.MILLIS.between(time, LocalTime.now()));
    }
}
