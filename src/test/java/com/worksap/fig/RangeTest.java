package com.worksap.fig;


import com.worksap.fig.lang.Range;
import com.worksap.fig.lang.Seqs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.worksap.fig.Helpers.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RangeTest {
    @Test
    public void testConstruct() {
        Range<Integer> range = new Range<>(1);
        assertThat(range.getFrom(), is(1));

        range = new Range<>(2, 10, i -> i + 2);
        assertThat(range.getFrom(), is(2));
        assertThat(range.getTo(), is(10));
        assertThat(range.isToIncluded(), is(true));
    }

    @Test
    public void testFrom() {
        assertThat(new Range<>(1).from(2).getFrom(), is(2));
        assertThrows(NullPointerException.class, () -> new Range<>("").from(null));
    }

    @Test
    public void testTo() {
        Range<Character> range = new Range<>('a').to('z');
        assertThat(range.getTo(), is('z'));
        assertThat(range.isToIncluded(), is(true));
        assertThrows(NullPointerException.class, () -> new Range<>("").to(null));
    }

    @Test
    public void testUntil() {
        Range<Character> range = new Range<>('a').until('z');
        assertThat(range.getTo(), is('z'));
        assertThat(range.isToIncluded(), is(false));
        assertThrows(NullPointerException.class, () -> new Range<>("").until(null));
    }

    @Test
    public void testNext() {
        Function<String, String> next = null;
        assertThrows(NullPointerException.class, () -> new Range<>("").next(next));

        BiFunction<String, Integer, String> biNext = null;
        assertThrows(NullPointerException.class, () -> new Range<>("").next(biNext));
    }

    @Test
    public void testForEach() {
        List<Integer> list = new ArrayList<>();
        new Range<>(1).to(64).next(i -> i + i).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(1, 2, 4, 8, 16, 32, 64)));

        list.clear();
        new Range<>(1).until(64).next(i -> i + i).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(1, 2, 4, 8, 16, 32)));

        list.clear();
        new Range<>(64).to(1).next(i -> i / 2).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(64, 32, 16, 8, 4, 2, 1)));

        list.clear();
        new Range<>(1).to(1).next(i -> i + i).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(1)));

        list.clear();
        new Range<>(1).until(1).next(i -> i + i).forEach(e -> list.add(e));
        assertThat(list, equalTo(Collections.emptyList()));

        list.clear();
        List<Integer> indices = new ArrayList<>();
        new Range<>(1).until(64).next(i -> i + i)
                .forEach((e, i) -> {
                    list.add(e);
                    indices.add(i);
                });
        assertThat(list, equalTo(Arrays.asList(1, 2, 4, 8, 16, 32)));
        assertThat(indices, equalTo(Arrays.asList(0, 1, 2, 3, 4, 5)));

        list.clear();
        new Range<>(1).to(720).next((c, i) -> c * (i + 2)).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(1, 2, 6, 24, 120, 720)));
    }

    @Test
    public void testToSeq() {
        assertThat(new Range<>(1).to(1).next(i -> i + 1).toSeq(), is(equalTo(Seqs.newSeq(1))));
        assertThat(new Range<>(1).until(1).next(i -> i + 1).toSeq(), is(equalTo(Seqs.newSeq())));
        assertThat(new Range<>('a').to('e').next(c -> (char) (c + 1)).toSeq(), is(equalTo(Seqs.newSeq('a', 'b', 'c', 'd', 'e'))));
        assertThat(new Range<>(6).until(0).next(i -> i - 2).toSeq(), is(equalTo(Seqs.newSeq(6, 4, 2))));
    }

    @Test
    public void testToMutableSeq() {
        assertThat(new Range<>(1).to(5).next(i -> i + 1).toMutableSeq(), is(equalTo(Seqs.newMutableSeq(1, 2, 3, 4, 5))));
    }
}
