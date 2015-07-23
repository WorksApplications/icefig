package com.worksap.fig;

import com.worksap.fig.lang.Seq;
import org.junit.Test;

/**
 * Created by liuyang on 7/23/15.
 */
public class SeqTest {

    @Test
    public void test(){
        Integer[] a= {1,2,3};
        Seq<Integer> seq = Seq.of(a);
        seq = seq.map(b -> b +1);
        System.out.println(seq.toString());
        System.out.println(seq.sample());
        System.out.println(seq.sample());
        System.out.println(seq.sample());
        System.out.println(seq.sample(1));
        System.out.println(seq.sample(2));
        System.out.println(seq.sample(3));
        System.out.println(seq.sample(5));
    }
}
