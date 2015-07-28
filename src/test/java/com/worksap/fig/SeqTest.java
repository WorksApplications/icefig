package com.worksap.fig;

import com.worksap.fig.lang.Seq;
import org.junit.Test;

import java.util.*;

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
    public void testMapWithIndex() {
        assertArrayEquals(new String[]{"a1", "b2", "c3"}, Seq.of("a", "b", "c").mapWithIndex((s, i) -> s + (i + 1)).toArray());
    }

    @Test
    public void testSample() {
        assertNull(Seq.newSeq().sample());
        assertEquals(0, Seq.newSeq().sample(1).size());
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

    @Test(expected = IllegalArgumentException.class)
    public void testEachCons() {
        final Seq<String> seq = Seq.of("a", "b", "c", "d");
        assertArrayEquals(new Object[]{Seq.of("a", "b"), Seq.of("b", "c"), Seq.of("c", "d")}, seq.eachCons(2).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c"), Seq.of("b", "c", "d")}, seq.eachCons(3).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d")}, seq.eachCons(4).toArray());
        assertArrayEquals(new Object[]{}, seq.eachCons(5).toArray());
        seq.eachCons(-1);
    }

    @Test(expected = IllegalArgumentException.class)
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
        seq.eachCons(0);
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

    @Test
    public void testJoin() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        assertEquals("123", seq.join().toString());
        assertEquals("1,2,3", seq.join(",").toString());
        assertEquals("!1-2-3!", seq.join("-", "!", "!").toString());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFirstLast() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        assertEquals((Integer) 1, seq.first());
        assertEquals((Integer) 3, seq.last());
        seq.clear();
        seq.first();
    }

    static class TestObj {
        private String name;
        private int age;

        public TestObj(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    static TestObj jim = new TestObj("Jim", 15);
    static TestObj tom = new TestObj("Tom", 16);
    static TestObj kate = new TestObj("Kate", 15);
    static TestObj john = new TestObj("John", 20);
    static Seq<TestObj> people = Seq.of(Arrays.asList(new TestObj[]{jim, tom, kate, john}));

    @Test
    public void testFind() {
        assertEquals(jim, people.findFirst(p -> p.age == 15));
        assertEquals(kate, people.findLast(p -> p.age == 15));
        assertEquals(0, people.findFirstIndex(p -> p.age == 15));
        assertEquals(2, people.findLastIndex(p -> p.age == 15));
        assertEquals(-1, people.findFirstIndex(p -> p.age == 1));
        assertEquals(-1, people.findLastIndex(p -> p.age == 1));
        assertNull(people.findFirst(p -> p.age == 1));
        assertNull(people.findLast(p -> p.age == 1));
    }

    @Test
    public void testPushPrepend() {
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(1, 2, 3).push(4, 5));
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(1, 2, 3).push(Arrays.asList(new Integer[]{4, 5})));
        assertEquals(Seq.of(1, 2, 3, 4), Seq.of(1, 2, 3).push(4));
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(3, 4, 5).prepend(1, 2));
        Seq<Integer> seq = Seq.of(1, 2, 3);
        seq.push(4, 5);
        assertEquals(Seq.of(1, 2, 3, 4, 5), seq);
        seq.prepend(4, 5);
        assertEquals(Seq.of(4, 5, 1, 2, 3, 4, 5), seq);
    }

    @Test
    public void testConcatPreConcat() {
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(1, 2, 3).concat(4, 5));
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(1, 2, 3).concat(Arrays.asList(new Integer[]{4, 5})));
        assertEquals(Seq.of(1, 2, 3, 4), Seq.of(1, 2, 3).concat(4));
        assertEquals(Seq.of(1, 2, 3, 4, 5), Seq.of(3, 4, 5).preConcat(1, 2));
        Seq<Integer> seq = Seq.of(1, 2, 3);
        seq.concat(4, 5);
        assertEquals(Seq.of(1, 2, 3), seq);
        seq.preConcat(4, 5);
        assertEquals(Seq.of(1, 2, 3), seq);
    }

    @Test(expected = NullPointerException.class)
    public void testReject() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        Seq<Integer> seq1 = seq.rejectWithIndex((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seq.of(1, 2, 3), seq);
        assertEquals(Seq.of(1, 2), seq1);

        Seq<Integer> seq$ = Seq.of(1, 2, 3);
        Seq<Integer> seq1$ = seq$.rejectWithIndex$((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seq.of(1, 2), seq$);
        assertEquals(Seq.of(1, 2), seq1$);
        assertEquals(seq$, seq1$);

        seq = Seq.of(1, 2, 3);
        seq1 = seq.reject(e -> e > 1);
        assertEquals(Seq.of(1, 2, 3), seq);
        assertEquals(Seq.of(1), seq1);

        seq$ = Seq.of(1, 2, 3);
        seq1$ = seq$.reject$(e -> e > 1);
        assertEquals(Seq.of(1), seq$);
        assertEquals(Seq.of(1), seq1$);
        assertEquals(seq$, seq1$);

        seq.rejectWithIndex(null);

    }

    @Test(expected = NullPointerException.class)
    public void testFilter() {
        Seq<Integer> seq = Seq.of(1, 2, 3);
        Seq<Integer> seq1 = seq.filterWithIndex((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seq.of(1, 2, 3), seq);
        assertEquals(Seq.of(3), seq1);

        Seq<Integer> seq1$ = seq.filter(e -> e > 1);
        assertEquals(Seq.of(1, 2, 3), seq);
        assertEquals(Seq.of(2, 3), seq1$);

        seq.filter(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5, 6);
        assertEquals(new Integer(1), seq.get(0));
        assertEquals(new Integer(6), seq.get(5));
        assertEquals(new Integer(6), seq.get(-1));
        assertEquals(new Integer(1), seq.get(-6));

        assertEquals(new Integer(2), seq.get(1, 100));
        assertEquals(new Integer(6), seq.get(-1, 100));
        assertEquals(new Integer(100), seq.get(6, 100));
        assertEquals(new Integer(100), seq.get(-7, 100));

        seq.get(6);
    }

    @Test(expected = NullPointerException.class)
    public void testNewSeq() {
        assertEquals(Seq.of(), Seq.newSeq());
        Seq<Integer> seq = Seq.newSeq();
        seq.add(null);
        assertEquals(seq, Seq.newSeq(1));
        assertEquals(Seq.of(1), Seq.newSeq(1, 1));
        assertEquals(Seq.of(0, 1, 4, 9), Seq.newSeq(4, i -> i * i));
        Seq<Seq<Integer>> seqs = Seq.newSeq(2, seq);
        assertEquals(null, seqs.get(1).get(0));
        seqs.get(0).set(0, 1);
        assertEquals(new Integer(1), seqs.get(0).get(0));
        assertEquals(new Integer(1), seqs.get(1).get(0));

        assertEquals(10, Seq.newSeq(10).size());
        assertEquals(10, Seq.newSeq(10, 1).size());
        assertEquals(10, Seq.newSeq(10, i -> i + 1).size());

        Seq.newSeq(10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeat() {
        Seq<Integer> seq = Seq.of(1, 2);
        assertEquals(Seq.of(1, 2, 1, 2, 1, 2, 1, 2), seq.repeat(4));
        assertEquals(Seq.of(1, 2), seq);
        assertEquals(Seq.of(1, 2), seq.repeat$(1));
        assertEquals(Seq.of(1, 2, 1, 2, 1, 2, 1, 2), seq.repeat$(4));
        assertEquals(Seq.of(1, 2, 1, 2, 1, 2, 1, 2), seq);
        assertEquals(Seq.of(), seq.repeat$(0));
    }

    @Test
    public void testCompact() {
        Seq<Integer> seq = Seq.of(1, 2, null, 3, 4, 5, null);
        assertEquals(Seq.of(1, 2, 3, 4, 5), seq.compact());
        assertEquals(Seq.of(1, 2, null, 3, 4, 5, null), seq);
        assertEquals(Seq.of(1, 2, 3, 4, 5), seq.compact$());
        assertEquals(Seq.of(1, 2, 3, 4, 5), seq);
    }

    @Test(expected = NullPointerException.class)
    public void testCount() {
        Seq<Integer> seq = Seq.of(1, 1, null, 2, null, 3, 1, 4);
        assertEquals(3, seq.count(1));
        assertEquals(1, seq.count(2));
        assertEquals(2, seq.count(null));
        assertEquals(2, seq.countCondition(e -> e == null));
        assertEquals(3, seq.countCondition(e -> e != null && e > 1));
        assertEquals(2, seq.countConditionWithIndex((e, i) -> e != null && e > 1 && i < seq.size() - 1));
        seq.countConditionWithIndex(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEachSlice() {
        final Seq<String> seq = Seq.of("a", "b", "c", "d", "e");
        assertArrayEquals(new Object[]{Seq.of("a"), Seq.of("b"), Seq.of("c"), Seq.of("d"), Seq.of("e")}, seq.eachSlice(1).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b"), Seq.of("c", "d"), Seq.of("e")}, seq.eachSlice(2).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c"), Seq.of("d", "e")}, seq.eachSlice(3).toArray());
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d"), Seq.of("e")}, seq.eachSlice(4).toArray());
        assertArrayEquals(new Object[]{seq}, seq.eachSlice(5).toArray());
        assertArrayEquals(new Object[]{seq}, seq.eachSlice(6).toArray());
        seq.eachSlice(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForEachSlice() {
        final Seq<String> seq = Seq.of("a", "b", "c", "d");
        Seq<Seq<String>> result = Seq.newSeq();
        seq.forEachSlice(2, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b"), Seq.of("c", "d")}, result.toArray());
        result.clear();
        seq.forEachSlice(3, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c"), Seq.of("d")}, result.toArray());
        result.clear();
        seq.forEachSlice(4, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachSlice(5, result::add);
        assertArrayEquals(new Object[]{Seq.of("a", "b", "c", "d")}, result.toArray());
        seq.forEachSlice(1, s -> s.forEach(e -> System.out.println(e)));
        seq.forEachSlice(0, result::add);
    }

    @Test(expected = NullPointerException.class)
    public void testReduce() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5);
        assertEquals(new Integer(15), seq.reduce(0, Integer::sum));
        assertEquals(new Integer(15), seq.reduce(Integer::sum));
        seq = Seq.of();
        assertEquals(null, seq.reduce(Integer::sum));

        Seq<TestObj> persons = Seq.of(new TestObj("wang", 26), new TestObj("sun", 30));
        assertEquals(2, persons.size());
        Map<String, Integer> map = new HashMap<>();
        persons.reduce(map, (m, p) -> {
            m.put(p.getName(), p.getAge());
            return m;
        });
        assertEquals(2, map.size());
        assertEquals(new Integer(26), map.get("wang"));
        map.forEach((k, v) -> System.out.println(k + ":" + v));
        seq.reduce(null);
    }

    @Test
    public void testReverse() {
        Seq<Integer> seq = Seq.of(1, 2, 3, 4, 5);
        assertEquals(Seq.of(5, 4, 3, 2, 1), seq.reverse());
        assertEquals(Seq.of(1, 2, 3, 4, 5), seq);
        assertEquals(Seq.of(), Seq.of().reverse());
        assertEquals(Seq.of(1), Seq.of(1).reverse());

        assertEquals(Seq.of(5, 4, 3, 2, 1), seq.reverse$());
        assertEquals(Seq.of(5, 4, 3, 2, 1), seq);
        assertEquals(Seq.of(), Seq.of().reverse());
        assertEquals(Seq.of(1), Seq.of(1).reverse());
        assertEquals(Seq.of(1, null), Seq.of(null, 1).reverse());
    }
}
