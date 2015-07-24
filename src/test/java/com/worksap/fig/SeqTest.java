package com.worksap.fig;

import com.worksap.fig.lang.Seq;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by liuyang on 7/23/15.
 */
public class SeqTest {

    @Test
    public void testConstruction() {
        assertArrayEquals(new Integer[]{1, 2, 3}, Seq.of(1, 2, 3).toArray());
        assertArrayEquals(new Integer[]{2}, Seq.of(2).toArray());
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        assertArrayEquals(new Integer[]{3, 4}, Seq.of(list).toArray());
    }

    @Test
    public void testMap() {
        assertArrayEquals(new Integer[]{2, 4, 6}, Seq.of(1, 2, 3).map(i -> i * 2).toArray());
        assertArrayEquals(new String[]{"x1", "x2", "x3"}, Seq.of(1, 2, 3).map(i -> "x" + i).toArray());
    }

    @Test
    public void testSample() {
        assertEquals(1, Seq.of(1, 2, 3).sample().size());
        assertEquals(2, Seq.of(1, 2, 3).sample(2).size());
        assertEquals(3, Seq.of(1, 2, 3).sample(3).size());
        assertEquals(3, Seq.of(1, 2, 3).sample(4).size());
        assertEquals(3, Seq.of(1, 2, 3).sample(5).size());
    }

    @Test
    public void testShuffle() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        for (int i = 0; i < 3; i++) {
            assertEquals(seq.shuffle().size(), 3);
            assertArrayEquals(new Integer[]{1, 2, 3}, seq.toArray());
        }
    }

    @Test
    public void testShuffle$() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        for (int i = 0; i < 3; i++) {
            Seq<Integer> shuffled = seq.shuffle$();
            assertEquals(shuffled.size(), 3);
            assertArrayEquals(shuffled.toArray(), seq.toArray());
        }
    }

    @Test
    public void testForEachWithIndex() {
        final Seq<String> seq = Seq.of("a", "b", "c");
        seq.forEachWithIndex((item, index) -> {
            assertEquals(seq.get(index), item);
        });
    }

    @Test
    public void testEachCons() {
        final Seq<String> seq = Seq.of("a", "b", "c", "d");
        assertArrayEquals(new Object[]{Seq.of("a", "b"), Seq.of("b", "c"), Seq.of("c", "d")}, seq.eachCons(2).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c"), Seq.of("b", "c", "d")}, seq.eachCons(3).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d")}, seq.eachCons(4).toArray());
        assertArrayEquals(new Object[]{}, seq.eachCons(5).toArray());
    }

    @Test
    public void testForEachCons() {
        final Seq<String> seq = Seq.of("a", "b", "c", "d");
        Seq<Seq<String>> result = Seq.newSeq();
        seq.forEachCons(2, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b"), Seq.of("b", "c"), Seq.of("c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(3, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c"), Seq.of("b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(4, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(5, result::add);
        assertArrayEquals(new Object[]{}, result.toArray());
    }

    @Test
    public void testDistinct() {
        assertArrayEquals(new Integer[]{1, 2, 3}, Seq.of(1, 2, 3).distinct().toArray());
        assertArrayEquals(new Integer[]{1, 2, 3}, Seq.of(1, 2, 3, 2, 3).distinct().toArray());
        assertArrayEquals(new Integer[]{3, 2, 1, 4}, Seq.of(3, 2, 1, 2, 3, 4).distinct().toArray());
    }

    @Test
    public void testOrder() {
        Seq<Integer> seq = Seq.of(2, 1, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.order((a, b) -> a - b).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.order((a, b) -> b - a).toArray());
        // original seq should remain unchanged
        assertArrayEquals(new Integer[]{2, 1, 3}, seq.toArray());
    }

    @Test
    public void testOrder$() {
        Seq<Integer> seq = Seq.of(2, 1, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.order$((a, b) -> a - b).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.order$((a, b) -> b - a).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.toArray());
    }
}
