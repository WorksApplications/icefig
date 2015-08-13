package com.worksap.fig;

import com.worksap.fig.lang.MutableSeq;
import com.worksap.fig.lang.Seq;
import com.worksap.fig.lang.Seqs;
import org.junit.Test;

import java.util.*;
import java.util.function.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static com.worksap.fig.Helpers.*;

/**
 * Created by liuyang on 7/23/15.
 */
public class SeqTest {

    @Test
    public void testConstruction() {
        Integer[] ints = new Integer[]{1, 2, 3};
        assertEquals(3, Seqs.newMutableSeq(ints).size());
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        assertEquals(2, Seqs.newSeq(collection).size());

        assertArrayEquals(new Integer[]{1, 2, 3}, Seqs.newMutableSeq(1, 2, 3).toArray());
        assertArrayEquals(new Integer[]{2}, Seqs.newMutableSeq(2).toArray());
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        assertArrayEquals(new Integer[]{3, 4}, Seqs.newMutableSeq(list).toArray());
        Integer[] values = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(values));
        Collection<Integer> cols = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(cols));


        // for 100% coverage
        new Seqs();
    }

    @Test
    public void testEquals() {
        Integer[] ints = new Integer[]{1, 2, 3};
        assertFalse(Seqs.newMutableSeq(ints).equals(null));
        assertFalse(Seqs.newMutableSeq(ints).equals(new ArrayList<>()));
        assertTrue(Seqs.newMutableSeq(ints).equals(Seqs.newMutableSeq(1, 2, 3)));

        assertEquals(Arrays.asList(ints).toString(), Seqs.newMutableSeq(ints).toString());
    }

    @Test
    public void testMap() {
        assertArrayEquals(new Integer[]{2, 4, 6}, Seqs.newMutableSeq(1, 2, 3).map(i -> i * 2).toArray());
        assertArrayEquals(new String[]{"x1", "x2", "x3"}, Seqs.newMutableSeq(1, 2, 3).map(i -> "x" + i).toArray());
        Function<Integer, Integer> f = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).map(f));
    }


    @Test
    public void testFlatMap() {
        assertArrayEquals(new Integer[]{2, 3, 4, 3, 4, 5, 4, 5, 6}, Seqs.newMutableSeq(1, 2, 3).flatMap(i -> Seqs.newMutableSeq(i + 1, i + 2, i + 3)).toArray());
        assertArrayEquals(new Integer[]{1, 0, 2, 1, 3, 2}, Seqs.newMutableSeq(1, 2, 3).flatMap((i, idx) -> Seqs.newMutableSeq(i, idx)).toArray());
        Function<Integer, Seq<Integer>> f = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).flatMap(f));
    }


    @Test
    public void testMapWithIndex() {
        assertArrayEquals(new String[]{"a1", "b2", "c3"}, Seqs.newMutableSeq("a", "b", "c").map((s, i) -> s + (i + 1)).toArray());
        BiFunction<Integer, Integer, Integer> f = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).map(f));
    }

    @Test
    public void testSample() {
        assertNull(Seqs.newMutableSeq().sample());
        assertEquals((Integer) 1, Seqs.newMutableSeq(1).sample());
        assertEquals(0, Seqs.newMutableSeq().sample(1).size());
        assertEquals(2, Seqs.newMutableSeq(1, 2, 3).sample(2).size());
        assertEquals(3, Seqs.newMutableSeq(1, 2, 3).sample(3).size());
        assertEquals(3, Seqs.newMutableSeq(1, 2, 3).sample(4).size());
        assertEquals(3, Seqs.newMutableSeq(1, 2, 3).sample(5).size());
    }

    @Test
    public void testShuffle() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        for (int i = 0; i < 3; i++) {
            assertEquals(seq.shuffle().size(), 3);
            assertArrayEquals(new Integer[]{1, 2, 3}, seq.toArray());
        }
    }

    @Test
    public void testShuffle$() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        for (int i = 0; i < 3; i++) {
            MutableSeq<Integer> shuffled = seq.shuffleInPlace();
            assertEquals(shuffled.size(), 3);
            assertArrayEquals(shuffled.toArray(), seq.toArray());
        }
    }

    @Test
    public void testIndexOf() {
        assertEquals(1, Seqs.newSeq(1, 2, 2, 3).indexOf(2));
        assertEquals(2, Seqs.newSeq(1, 2, 2, 3).lastIndexOf(2));
        assertEquals(-1, Seqs.newSeq(1, 2, 2, 3).indexOf(4));
    }

    @Test
    public void testContainsAny() {
        assertThrows(NullPointerException.class, () -> Seqs.newSeq(1, 2, 3).containsAny((Predicate<Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newSeq(1, 2, 3).containsAny((BiPredicate<Integer, Integer>) null));
        assertTrue(Seqs.newSeq(1, 2, 3).containsAny(i -> i > 2));
        assertTrue(Seqs.newSeq(1, 2, 3).containsAny((e, i) -> e + i == 3));
        assertFalse(Seqs.newSeq(1, 2, 3).containsAny((e, i) -> e + i == 4));
        assertFalse(Seqs.newSeq(1, 2, 3).containsAny(i -> i > 3));
    }

    @Test
    public void testContainSubSeq() {
        assertTrue(Seqs.newSeq('B', 'B', 'C', ' ', 'A', 'B', 'C', 'D', 'A', 'B', ' ', 'A', 'B', 'C', 'D', 'A', 'B', 'C', 'D', 'A', 'B', 'D', 'E')
                .containsSubSeq(Seqs.newSeq('A', 'B', 'C', 'D', 'A', 'B', 'D')));
        assertFalse(Seqs.newSeq('B', 'B', 'C', ' ', 'A', 'B', 'C', 'D', 'A', 'B', ' ', 'A', 'B', 'C', 'D', 'A', 'B', 'C', 'D', 'A', 'B', 'D', 'E')
                .containsSubSeq(Seqs.newSeq('A', 'B', 'C', 'D', 'A', 'B', 'A')));
        assertFalse(Seqs.newSeq('A')
                .containsSubSeq(Seqs.newSeq('B')));
        assertFalse(Seqs.newSeq()
                .containsSubSeq(Seqs.newSeq('B')));
        assertTrue(Seqs.newSeq('A')
                .containsSubSeq(Seqs.newSeq()));
        assertEquals(-1, Seqs.newSeq().lastIndexOfSubSeq(Seqs.newSeq('A')));
        assertEquals(-1, Seqs.newSeq('A', 'B').lastIndexOfSubSeq(Seqs.newSeq('C', 'B')));
        assertEquals(-1, Seqs.newSeq('A', 'A', 'B').lastIndexOfSubSeq(Seqs.newSeq('C', 'B')));
        assertEquals(0, Seqs.newSeq('A').lastIndexOfSubSeq(Seqs.newSeq()));
        assertTrue(Seqs.newSeq('A', 'B', 'C')
                .containsSubSeq(Seqs.newSeq('A', 'B', 'C')));

        assertThrows(NullPointerException.class, () -> Seqs.newSeq('A', 'B', 'C').containsSubSeq(null));

        assertEquals(15, Seqs.newSeq('B', 'B', 'C', ' ', 'A', 'B', 'C', 'D', 'A', 'B', ' ', 'A', 'B', 'C', 'D', 'A', 'B', 'C', 'D', 'A', 'B', 'D', 'E')
                .indexOfSubSeq(Seqs.newSeq('A', 'B', 'C', 'D', 'A', 'B', 'D')));
        assertEquals(15, Seqs.newSeq('B', 'B', 'C', ' ', 'A', 'B', 'C', 'D', 'A', 'B', ' ', 'A', 'B', 'C', 'D', 'A', 'B', 'C', 'D', 'A', 'B', 'D', 'E')
                .lastIndexOfSubSeq(Seqs.newSeq('A', 'B', 'C', 'D', 'A', 'B', 'D')));

        assertEquals(1, Seqs.newSeq(3, 1, 2, 1, 2, 1, 2, 3)
                .indexOfSubSeq(Seqs.newSeq(1, 2, 1, 2)));
        assertEquals(3, Seqs.newSeq(3, 1, 2, 1, 2, 1, 2, 3)
                .lastIndexOfSubSeq(Seqs.newSeq(1, 2, 1, 2)));

        assertThrows(NullPointerException.class, () -> Seqs.newSeq('A', 'B', 'C').indexOfSubSeq(null));
        assertThrows(NullPointerException.class, () -> Seqs.newSeq('A', 'B', 'C').lastIndexOfSubSeq(null));
    }

    @Test
    public void testIntersect() {
        assertThrows(NullPointerException.class, () -> Seqs.newSeq(1).intersect(null));
        assertEquals(Seqs.newSeq(), Seqs.newSeq(1, 2, 3).intersect(Seqs.newSeq()));
        assertEquals(Seqs.newSeq(3, 6, 2, 3), Seqs.newSeq(3, 6, 2, 4, 3, 6, 2).intersect(Seqs.newSeq(6, 2, 3, 3, 8, 3)));
    }

    @Test
    public void testDifference() {
        assertThrows(NullPointerException.class, () -> Seqs.newSeq(1).difference(null));
        assertEquals(Seqs.newSeq(1, 2, 3), Seqs.newSeq(1, 2, 3).difference(Seqs.newSeq()));
        assertEquals(Seqs.newSeq(), Seqs.newSeq(3, 6, 2, 4, 3, 6, 2).difference(Seqs.newSeq(3, 6, 2, 4, 3, 6, 2)));
        assertEquals(Seqs.newSeq(4, 3, 6, 2), Seqs.newSeq(3, 6, 2, 4, 3, 6, 2).difference(Seqs.newSeq(3, 6, 2, 7)));
    }

    @Test
    public void testForEachWithIndex() {
        final MutableSeq<String> seq = Seqs.newMutableSeq("a", "b", "c");
        seq.forEach((item, index) -> {
            assertEquals(seq.get(index), item);
        });
        seq.forEach(item -> assertTrue(seq.contains(item)));
        BiConsumer<String, Integer> f = null;
        assertThrows(NullPointerException.class, () -> seq.forEach(f));
    }

    @Test
    public void testForEachReverse() {
        MutableSeq<Integer> result = Seqs.newMutableSeq();
        Seqs.newMutableSeq(1, 2, 3, 4, 5).forEachReverse(i -> {
            result.appendInPlace(i);
        });
        assertEquals(Seqs.newSeq(5, 4, 3, 2, 1), result);

        MutableSeq<Integer> result2 = Seqs.newMutableSeq();
        Seqs.newMutableSeq(1, 2, 3, 4, 5).forEachReverse((e, i) -> {
            result2.appendInPlace(e + i);
        });
        assertEquals(Seqs.newSeq(9, 7, 5, 3, 1), result2);
    }

    @Test
    public void testEachCons() {
        final MutableSeq<String> seq = Seqs.newMutableSeq("a", "b", "c", "d");
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b"), Seqs.newMutableSeq("b", "c"), Seqs.newMutableSeq("c", "d")}, seq.eachCons(2).toArray());
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c"), Seqs.newMutableSeq("b", "c", "d")}, seq.eachCons(3).toArray());
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c", "d")}, seq.eachCons(4).toArray());
        assertArrayEquals(new Object[]{}, seq.eachCons(5).toArray());
        assertThrows(IllegalArgumentException.class, () -> seq.eachCons(-1));
    }

    @Test
    public void testForEachCons() {
        final MutableSeq<String> seq = Seqs.newMutableSeq("a", "b", "c", "d");
        MutableSeq<Seq<String>> result = Seqs.newMutableSeq();
        seq.forEachCons(2, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b"), Seqs.newMutableSeq("b", "c"), Seqs.newMutableSeq("c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(3, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c"), Seqs.newMutableSeq("b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(4, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachCons(5, result::appendInPlace);
        assertArrayEquals(new Object[]{}, result.toArray());

        assertThrows(NullPointerException.class, () -> seq.forEachCons(0, null));
        assertThrows(IllegalArgumentException.class, () -> seq.forEachCons(0, result::appendInPlace));
    }

    @Test
    public void testDistinct() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.distinct().toArray());
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.distinctInPlace().toArray());
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);

        seq = Seqs.newMutableSeq(1, 2, 3, 2, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.distinct().toArray());
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 2, 3), seq);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.distinctInPlace().toArray());
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);

        seq = Seqs.newMutableSeq(3, 2, 1, 2, 3, 4);
        assertArrayEquals(new Integer[]{3, 2, 1, 4}, seq.distinct().toArray());
        assertEquals(Seqs.newMutableSeq(3, 2, 1, 2, 3, 4), seq);
        assertArrayEquals(new Integer[]{3, 2, 1, 4}, seq.distinctInPlace().toArray());
        assertEquals(Seqs.newMutableSeq(3, 2, 1, 4), seq);

        seq = Seqs.newMutableSeq(1, 1, 1, 1);
        assertEquals(Seqs.newMutableSeq(1), seq.distinct());
        assertEquals(Seqs.newMutableSeq(1, 1, 1, 1), seq);
        assertEquals(Seqs.newMutableSeq(1), seq.distinctInPlace());
        assertEquals(Seqs.newMutableSeq(1), seq);

        seq = Seqs.newMutableSeq(null, 1, null, 1, 2);
        assertEquals(Seqs.newMutableSeq(null, 1, 2), seq.distinct());
        assertEquals(Seqs.newMutableSeq(null, 1, null, 1, 2), seq);
        assertEquals(Seqs.newMutableSeq(null, 1, 2), seq.distinctInPlace());
        assertEquals(Seqs.newMutableSeq(null, 1, 2), seq);

        seq.clear();
        assertEquals(Seqs.newMutableSeq(), seq.distinct());
        assertEquals(Seqs.newMutableSeq(), seq);
        assertEquals(Seqs.newMutableSeq(), seq.distinctInPlace());
        assertEquals(Seqs.newMutableSeq(), seq);
    }

    @Test
    public void testSort() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(2, 1, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.sort((a, b) -> a - b).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.sort((a, b) -> b - a).toArray());
        // original seq should remain unchanged
        assertArrayEquals(new Integer[]{2, 1, 3}, seq.toArray());

        assertArrayEquals(new Integer[]{1, 2, 3}, seq.sort(null).toArray());
        assertEquals(Seqs.newMutableSeq(), Seqs.newMutableSeq().sort(null));
    }

    @Test
    public void testSortInPlace() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(2, 1, 3);
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.sortInPlace((a, b) -> a - b).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.sortInPlace((a, b) -> b - a).toArray());
        assertArrayEquals(new Integer[]{3, 2, 1}, seq.toArray());
        assertArrayEquals(new Integer[]{1, 2, 3}, seq.sortInPlace(null).toArray());
        assertEquals(Seqs.newMutableSeq(), Seqs.newMutableSeq().sortInPlace(null));
    }

    @Test
    public void testJoin() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        assertEquals("123", seq.join().toString());
        assertEquals("1,2,3", seq.join(",").toString());
        assertEquals("!1-2-3!", seq.join("-", "!", "!").toString());

    }

    @Test
    public void testFirstLast() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        assertEquals((Integer) 1, seq.first());
        assertEquals((Integer) 3, seq.last());
        seq.clear();
        assertThrows(IndexOutOfBoundsException.class, () -> seq.first());
        assertThrows(IndexOutOfBoundsException.class, () -> seq.last());
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
    static MutableSeq<TestObj> people = Seqs.newMutableSeq(Arrays.asList(new TestObj[]{jim, tom, kate, john}));

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
        assertThrows(NullPointerException.class, () -> people.findFirst(null));
        assertThrows(NullPointerException.class, () -> people.findLast(null));
        assertThrows(NullPointerException.class, () -> people.findFirstIndex(null));
        assertThrows(NullPointerException.class, () -> people.findLastIndex(null));
    }

    @Test
    public void testAppendPrependInPlace() {
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(1, 2, 3).appendInPlace(4, 5));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(1, 2, 3).appendInPlace(Arrays.asList(new Integer[]{4, 5})));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4), Seqs.newMutableSeq(1, 2, 3).appendInPlace(4));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(3, 4, 5).prependInPlace(1, 2));
        assertEquals(Seqs.newMutableSeq(1, 3, 4, 5), Seqs.newMutableSeq(3, 4, 5).prependInPlace(1));
        assertEquals(Seqs.newMutableSeq(2, 3, 4, 5), Seqs.newMutableSeq(3, 4, 5).prependInPlace(Seqs.newMutableSeq(2)));
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        seq.appendInPlace(4, 5);
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq);
        seq.prependInPlace(4, 5);
        assertEquals(Seqs.newMutableSeq(4, 5, 1, 2, 3, 4, 5), seq);
        Integer[] ints = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).appendInPlace(ints));
        Collection<Integer> cols = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).appendInPlace(cols));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Seqs.newSeq().isEmpty());
        assertFalse(Seqs.newSeq(1, 2, 3).isEmpty());
    }

    @Test
    public void testAppendPrepend() {
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(1, 2, 3).append(4, 5));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(1, 2, 3).append(Arrays.asList(new Integer[]{4, 5})));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4), Seqs.newMutableSeq(1, 2, 3).append(4));
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), Seqs.newMutableSeq(3, 4, 5).prepend(1, 2));
        assertEquals(Seqs.newMutableSeq(1, 3, 4, 5), Seqs.newMutableSeq(3, 4, 5).prepend(1));
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        seq.append(4, 5);
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        seq.prepend(4, 5);
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        Integer[] ints = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).prepend(ints));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).append(ints));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).prepend(ints));
        Collection<Integer> cols = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).prepend(cols));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).append(cols));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).prepend(cols));

        seq = Seqs.newMutableSeq(1, 2, 3);
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 1, 2, 3), seq.append(seq));
        assertEquals(Seqs.newMutableSeq(3, 2, 1, 1, 2, 3), seq.prepend(seq.reverse()));
    }

    @Test
    public void testReject() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        MutableSeq<Integer> seq1 = seq.reject((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        assertEquals(Seqs.newMutableSeq(1, 2), seq1);

        MutableSeq<Integer> seq$ = Seqs.newMutableSeq(1, 2, 3);
        MutableSeq<Integer> seq1$ = seq$.rejectInPlace((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seqs.newMutableSeq(1, 2), seq$);
        assertEquals(Seqs.newMutableSeq(1, 2), seq1$);
        assertEquals(seq$, seq1$);

        seq = Seqs.newMutableSeq(1, 2, 3);
        seq1 = seq.reject(e -> e > 1);
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        assertEquals(Seqs.newMutableSeq(1), seq1);

        seq$ = Seqs.newMutableSeq(1, 2, 3);
        seq1$ = seq$.rejectInPlace(e -> e > 1);
        assertEquals(Seqs.newMutableSeq(1), seq$);
        assertEquals(Seqs.newMutableSeq(1), seq1$);
        assertEquals(seq$, seq1$);

        Predicate<Integer> predicate = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).reject(predicate));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectInPlace(predicate));
        BiPredicate<Integer, Integer> biPredicate = null;
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).reject(biPredicate));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectInPlace(biPredicate));

    }

    @Test
    public void testRejectWhile() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 1);
        assertThat(seq.rejectWhile(e -> e < 2), is(equalTo(Seqs.newMutableSeq(2, 1))));
        assertThat(seq.rejectWhile(e -> e < 1), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(seq.rejectWhile(e -> e > 0), is(equalTo(Seqs.newMutableSeq())));
        assertThat(seq.rejectWhile((e, i) -> e < 2), is(equalTo(Seqs.newMutableSeq(2, 1))));
        assertThat(seq.rejectWhile((e, i) -> e < 1), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(seq.rejectWhile((e, i) -> e > 0), is(equalTo(Seqs.newMutableSeq())));

        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace(e -> e < 2), is(equalTo(Seqs.newMutableSeq(2, 1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace(e -> e < 1), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace(e -> e > 0), is(equalTo(Seqs.newMutableSeq())));
        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace((e, i) -> e < 2), is(equalTo(Seqs.newMutableSeq(2, 1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace((e, i) -> e < 1), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).rejectWhileInPlace((e, i) -> e > 0), is(equalTo(Seqs.newMutableSeq())));

        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectWhile((Predicate<Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectWhileInPlace((Predicate<Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectWhile((BiPredicate<Integer, Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).rejectWhileInPlace((BiPredicate<Integer, Integer>) null));
    }

    @Test
    public void testFilter() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3);
        MutableSeq<Integer> seq1 = seq.filter((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        assertEquals(Seqs.newMutableSeq(3), seq1);

        MutableSeq<Integer> seq2 = Seqs.newMutableSeq(1, 2, 3);
        MutableSeq<Integer> seq3 = seq2.filterInPlace((e, i) -> (e > 1 && i % 2 == 0));
        assertEquals(Seqs.newMutableSeq(3), seq2);
        assertEquals(Seqs.newMutableSeq(3), seq3);
        assertEquals(seq2, seq3);

        seq2 = Seqs.newMutableSeq(1, 2, 3);
        seq3 = seq2.filterInPlace(e -> e > 1);
        assertEquals(Seqs.newMutableSeq(2, 3), seq2);
        assertEquals(Seqs.newMutableSeq(2, 3), seq3);
        assertEquals(seq2, seq3);

        MutableSeq<Integer> seq1$ = seq.filter(e -> e > 1);
        assertEquals(Seqs.newMutableSeq(1, 2, 3), seq);
        assertEquals(Seqs.newMutableSeq(2, 3), seq1$);

        Predicate<Integer> predicate = null;
        assertThrows(NullPointerException.class, () -> seq.filter(predicate));
        assertThrows(NullPointerException.class, () -> seq.filterInPlace(predicate));
        BiPredicate<Integer, Integer> biPredicate = null;
        assertThrows(NullPointerException.class, () -> seq.filter(biPredicate));
        assertThrows(NullPointerException.class, () -> seq.filterInPlace(biPredicate));
    }

    @Test
    public void testFilterWhile() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 1);
        assertThat(seq.filterWhile(e -> e < 2), is(equalTo(Seqs.newMutableSeq(1))));
        assertThat(seq.filterWhile(e -> e < 1), is(equalTo(Seqs.newMutableSeq())));
        assertThat(seq.filterWhile(e -> e > 0), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(seq.filterWhile((e, i) -> e < 2), is(equalTo(Seqs.newMutableSeq(1))));
        assertThat(seq.filterWhile((e, i) -> e < 1), is(equalTo(Seqs.newMutableSeq())));
        assertThat(seq.filterWhile((e, i) -> e > 0), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));

        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace(e -> e < 2), is(equalTo(Seqs.newMutableSeq(1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace(e -> e < 1), is(equalTo(Seqs.newMutableSeq())));
        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace(e -> e > 0), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace((e, i) -> e < 2), is(equalTo(Seqs.newMutableSeq(1))));
        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace((e, i) -> e < 1), is(equalTo(Seqs.newMutableSeq())));
        assertThat(Seqs.newMutableSeq(1, 2, 1).filterWhileInPlace((e, i) -> e > 0), is(equalTo(Seqs.newMutableSeq(1, 2, 1))));

        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).filterWhile((Predicate<Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).filterWhileInPlace((Predicate<Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).filterWhile((BiPredicate<Integer, Integer>) null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).filterWhileInPlace((BiPredicate<Integer, Integer>) null));
    }

    @Test
    public void testGet() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3, 4, 5, 6);
        assertEquals(new Integer(1), seq.get(0));
        assertEquals(new Integer(6), seq.get(5));
        assertEquals(new Integer(6), seq.get(-1));
        assertEquals(new Integer(1), seq.get(-6));

        assertEquals(new Integer(2), seq.get(1, 100));
        assertEquals(new Integer(6), seq.get(-1, 100));
        assertEquals(new Integer(100), seq.get(6, 100));
        assertEquals(new Integer(100), seq.get(-7, 100));

        assertThrows(IndexOutOfBoundsException.class, () -> seq.get(6));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-7));
    }

    @Test
    public void testRepeat() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2);
        assertEquals(Seqs.newMutableSeq(1, 2, 1, 2, 1, 2, 1, 2), seq.repeat(4));
        assertEquals(Seqs.newMutableSeq(1, 2), seq);
        assertEquals(Seqs.newMutableSeq(1, 2), seq.repeatInPlace(1));
        assertEquals(Seqs.newMutableSeq(1, 2, 1, 2, 1, 2, 1, 2), seq.repeatInPlace(4));
        assertEquals(Seqs.newMutableSeq(1, 2, 1, 2, 1, 2, 1, 2), seq);
        assertThrows(IllegalArgumentException.class, () -> seq.repeat(0));
        assertThrows(IllegalArgumentException.class, () -> seq.repeatInPlace(0));
    }

    @Test
    public void testCompact() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, null, 3, 4, 5, null);
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq.compact());
        assertEquals(Seqs.newMutableSeq(1, 2, null, 3, 4, 5, null), seq);
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq.compactInPlace());
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq);
    }

    @Test
    public void testCount() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 1, null, 2, null, 3, 1, 4);
        assertEquals(3, seq.count(1));
        assertEquals(1, seq.count(2));
        assertEquals(2, seq.count(null));
        assertEquals(2, seq.countIf(e -> e == null));
        assertEquals(3, seq.countIf(e -> e != null && e > 1));
        assertEquals(2, seq.countIf((e, i) -> e != null && e > 1 && i < seq.size() - 1));

        Predicate<Integer> predicate = null;
        assertThrows(NullPointerException.class, () -> seq.countIf(predicate));
        BiPredicate<Integer, Integer> biPredicate = null;
        assertThrows(NullPointerException.class, () -> seq.countIf(biPredicate));
    }

    @Test
    public void testEachSlice() {
        final MutableSeq<String> seq = Seqs.newMutableSeq("a", "b", "c", "d", "e");
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a"), Seqs.newMutableSeq("b"), Seqs.newMutableSeq("c"), Seqs.newMutableSeq("d"), Seqs.newMutableSeq("e")}, seq.eachSlice(1).toArray());
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b"), Seqs.newMutableSeq("c", "d"), Seqs.newMutableSeq("e")}, seq.eachSlice(2).toArray());
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c"), Seqs.newMutableSeq("d", "e")}, seq.eachSlice(3).toArray());
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c", "d"), Seqs.newMutableSeq("e")}, seq.eachSlice(4).toArray());
        assertArrayEquals(new Object[]{seq}, seq.eachSlice(5).toArray());
        assertArrayEquals(new Object[]{seq}, seq.eachSlice(6).toArray());
        assertThrows(IllegalArgumentException.class, () -> seq.eachSlice(0));
    }

    @Test
    public void testForEachSlice() {
        final MutableSeq<String> seq = Seqs.newMutableSeq("a", "b", "c", "d");
        MutableSeq<Seq<String>> result = Seqs.newMutableSeq();
        seq.forEachSlice(2, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b"), Seqs.newMutableSeq("c", "d")}, result.toArray());
        result.clear();
        seq.forEachSlice(3, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c"), Seqs.newMutableSeq("d")}, result.toArray());
        result.clear();
        seq.forEachSlice(4, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c", "d")}, result.toArray());
        result.clear();
        seq.forEachSlice(5, result::appendInPlace);
        assertArrayEquals(new Object[]{Seqs.newMutableSeq("a", "b", "c", "d")}, result.toArray());
        seq.forEachSlice(1, s -> s.forEach(e -> System.out.println(e)));
        assertThrows(IllegalArgumentException.class, () -> seq.forEachSlice(0, result::appendInPlace));
        assertThrows(NullPointerException.class, () -> seq.forEachSlice(0, null));
    }

    @Test
    public void testReduce() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3, 4, 5);
        assertEquals(new Integer(15), seq.reduce(0, Integer::sum));
        assertEquals(new Integer(15), seq.reduce(Integer::sum));
        seq = Seqs.newMutableSeq();
        assertEquals(null, seq.reduce(Integer::sum));
        assertEquals(new Integer(1), seq.reduce(1, Integer::sum));
        seq.appendInPlace(1);
        assertEquals(new Integer(2), seq.reduce(1, Integer::sum));

        MutableSeq<TestObj> persons = Seqs.newMutableSeq(new TestObj("wang", 26), new TestObj("sun", 30));
        assertEquals(2, persons.size());
        Map<String, Integer> map = new HashMap<>();
        persons.reduce(map, (m, p) -> {
            m.put(p.getName(), p.getAge());
            return m;
        });
        assertEquals(2, map.size());
        assertEquals(new Integer(26), map.get("wang"));
        map.forEach((k, v) -> System.out.println(k + ":" + v));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).reduce(null));
        assertThrows(NullPointerException.class, () -> Seqs.newMutableSeq(1).reduce(1, null));
    }

    @Test
    public void testReverse() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3, 4, 5);
        assertEquals(Seqs.newMutableSeq(5, 4, 3, 2, 1), seq.reverse());
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq);
        assertEquals(Seqs.newMutableSeq(), Seqs.newMutableSeq().reverse());
        assertEquals(Seqs.newMutableSeq(1), Seqs.newMutableSeq(1).reverse());

        assertEquals(Seqs.newMutableSeq(5, 4, 3, 2, 1), seq.reverseInPlace());
        assertEquals(Seqs.newMutableSeq(5, 4, 3, 2, 1), seq);
        assertEquals(Seqs.newMutableSeq(), Seqs.newMutableSeq().reverse());
        assertEquals(Seqs.newMutableSeq(1), Seqs.newMutableSeq(1).reverse());
        assertEquals(Seqs.newMutableSeq(1, null), Seqs.newMutableSeq(null, 1).reverse());
    }

    @Test
    public void testSubSeq() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3, 4, 5);
        assertEquals(Seqs.newMutableSeq(1, 2), seq.subSeq(0, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.subSeq(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> seq.subSeq(0, 12));
        assertThrows(IllegalArgumentException.class, () -> seq.subSeq(2, 1));
    }

    @Test
    public void testCombination() {
        MutableSeq<Integer> seq = Seqs.newMutableSeq(1, 2, 3, 4, 5);
        assertEquals(0, seq.eachCombination(6).size());
        assertThrows(IllegalArgumentException.class, () -> seq.eachCombination(0));
        assertThrows(IllegalArgumentException.class, () -> seq.eachCombination(-1));
        assertEquals(1, seq.eachCombination(5).size());
        assertEquals(Seqs.newMutableSeq(1, 2, 3, 4, 5), seq.eachCombination(5).first());
        Set<MutableSeq<Integer>> resultSet = new HashSet<>(seq.eachCombination(3).toArrayList());
        Set<MutableSeq<Integer>> actual = new HashSet<>(Seqs.newMutableSeq(Seqs.newMutableSeq(1, 2, 3),
                Seqs.newMutableSeq(1, 2, 4),
                Seqs.newMutableSeq(1, 2, 5),
                Seqs.newMutableSeq(1, 3, 4),
                Seqs.newMutableSeq(1, 3, 5),
                Seqs.newMutableSeq(1, 4, 5),
                Seqs.newMutableSeq(2, 3, 4),
                Seqs.newMutableSeq(2, 3, 5),
                Seqs.newMutableSeq(2, 4, 5),
                Seqs.newMutableSeq(3, 4, 5)).toArrayList());
        assertEquals(actual, resultSet);
    }
}
