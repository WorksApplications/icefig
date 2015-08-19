/*
 * Copyright (C) 2015 The Fig Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worksap.icefig.lang;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>("").from(null));
    }

    @Test
    public void testTo() {
        Range<Character> range = new Range<>('a').to('z');
        assertThat(range.getTo(), is('z'));
        assertThat(range.isToIncluded(), is(true));
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>("").to(null));
    }

    @Test
    public void testUntil() {
        Range<Character> range = new Range<>('a').until('z');
        assertThat(range.getTo(), is('z'));
        assertThat(range.isToIncluded(), is(false));
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>("").until(null));
    }

    @Test
    public void testNext() {
        Function<String, String> next = null;
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>("").next(next));

        BiFunction<String, Integer, String> biNext = null;
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>("").next(biNext));
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

        list.clear();
        new Range<>(1).to(5).next(i -> i + 1).next((c, i) -> c * (i + 2)).forEach(e -> list.add(e));
        assertThat(list, equalTo(Arrays.asList(1, 2, 3, 4, 5)));

        list.clear();
        Helpers.assertThrows(NullPointerException.class, () -> new Range<>(1).to(10).forEach(e -> list.add(e)));
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

    @Test
    public void testIterator() {
        Iterator<Integer> itr = new Range<>(1).to(1).next(i -> i + 1).iterator();
        assertThat(itr.next(), is(1));
        assertThat(itr.hasNext(), is(false));
        final Iterator<Integer> it = itr;
        Helpers.assertThrows(NoSuchElementException.class, () -> it.next());

        itr = new Range<>(1).to(2).next(i -> i + 1).iterator();
        assertThat(itr.next(), is(1));
        assertThat(itr.next(), is(2));
        assertThat(itr.hasNext(), is(false));

        itr = new Range<>(1).next(i -> i + 1).iterator();
        assertThat(itr.hasNext(), is(true));
        for (int i = 0; i < 100; i++) {
            itr.next();
        }
        assertThat(itr.next(), is(101));
        assertThat(itr.hasNext(), is(true));

        List<Integer> list = new ArrayList<>();
        for (int d: new Range<>(1).next(i -> i + 1).until(5)) {
            list.add(d);
        }
        assertThat(list, equalTo(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void testSpliterator() {
        Helpers.assertThrows(IllegalArgumentException.class, () -> new Range<>(1).next(i -> i + 1).take(-1));
    }

    @Test
    public void testTake() {
        assertThat(new Range<>(1).next(i -> i + 1).take(0), equalTo(Seqs.newMutableSeq()));
        assertThat(new Range<>(1).next(i -> i + 1).take(5), equalTo(Seqs.newMutableSeq(1, 2, 3, 4, 5)));
        assertThat(new Range<>(1).next(i -> i + 1).to(3).take(5), equalTo(Seqs.newMutableSeq(1, 2, 3)));

        Helpers.assertThrows(UnsupportedOperationException.class, () -> new Range<>(1).spliterator());
    }

    @Test
    public void testTakeWhile() {
        assertThat(new Range<>(1).next(i -> i + 1).takeWhile(e -> e <= 5), equalTo(Seqs.newMutableSeq(1, 2, 3, 4, 5)));
        assertThat(new Range<>(1).next(i -> i + 1).takeWhile(e -> e < 1), equalTo(Seqs.newMutableSeq()));
        assertThat(new Range<>(1).next(i -> i + 1).until(1).takeWhile(e -> e <= 5), equalTo(Seqs.newMutableSeq()));
        assertThat(new Range<>(1).next(i -> i + 1).takeWhile((e, i) -> i < 5), equalTo(Seqs.newMutableSeq(1, 2, 3, 4, 5)));
        assertThat(new Range<>(1).next(i -> i + 1).takeWhile((e, i) -> i < 0), equalTo(Seqs.newMutableSeq()));
        assertThat(new Range<>(1).next(i -> i + 1).until(1).takeWhile((e, i) -> i < 5), equalTo(Seqs.newMutableSeq()));

        List<Integer> indices = new ArrayList<>();
        assertThat(new Range<>(1).next(i -> i + i)
                .takeWhile((e, i) -> {
                    indices.add(i);
                    return e < 64;
                }), equalTo(Seqs.newMutableSeq(1, 2, 4, 8, 16, 32)));
        assertThat(indices, equalTo(Arrays.asList(0, 1, 2, 3, 4, 5, 6)));
    }
}
