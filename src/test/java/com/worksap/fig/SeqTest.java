package com.worksap.fig;

import com.worksap.fig.lang.Seq;
import org.junit.Test;

/**
 * Created by liuyang on 7/23/15.
 */
public class SeqTest {

    @Test
    public void test(){
        Seq<Integer> seq = Seq.of(1,2,3);
        System.out.println(seq.shuffle());
        System.out.println(seq);
        seq.add(4);
        seq = seq.map(b -> b +1);
        System.out.println(seq.toString());
        System.out.println(seq.sample());
        System.out.println(seq.sample());
        System.out.println(seq.sample());
        System.out.println(seq.sample(1));
        System.out.println(seq.sample(2));
        System.out.println(seq.sample(3));
        System.out.println(seq.sample(5));

        seq.forEachWithIndex((value, idx) -> {
            System.out.println(value);
            System.out.println(idx);
        });

        seq.forEachCons(2, (values)->{
            System.out.println(values);
        });

    }
}
