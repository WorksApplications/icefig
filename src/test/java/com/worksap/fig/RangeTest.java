package com.worksap.fig;


import com.worksap.fig.lang.Range;
import com.worksap.fig.lang.Seqs;
import org.junit.Test;

import static com.worksap.fig.Helpers.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RangeTest {
    @Test
    public void testFrom() {
        assertThat(new Range<>(1).from(2).getLower(), is(2));
        assertThrows(NullPointerException.class, () -> new Range<>("").from(null));
    }

    @Test
    public void testTo() {
        Range<Character> range = new Range<>('a').to('z');
        assertThat(range.getUpper(), is('z'));
        assertThat(range.isUpperInclusive(), is(true));
        assertThrows(NullPointerException.class, () -> new Range<>("").to(null));
    }

    @Test
    public void testUntil() {
        Range<Character> range = new Range<>('a').until('z');
        assertThat(range.getUpper(), is('z'));
        assertThat(range.isUpperInclusive(), is(false));
        assertThrows(NullPointerException.class, () -> new Range<>("").until(null));
    }

    @Test
    public void testNext() {
        assertThrows(NullPointerException.class, () -> new Range<>("").next(null));
    }

    @Test
    public void testToSeq() {
        assertThat(new Range<>(1).to(1).next(i -> i + 1).toSeq(), is(equalTo(Seqs.newSeq(1))));
        assertThat(new Range<>(1).until(1).next(i -> i + 1).toSeq(), is(equalTo(Seqs.newSeq())));
        assertThat(new Range<>('a').to('e').next(c -> (char) (c + 1)).toSeq(), is(equalTo(Seqs.newSeq('a', 'b', 'c', 'd', 'e'))));
        assertThat(new Range<>(6).until(0).next(i -> i - 2).toSeq(), is(equalTo(Seqs.newSeq(6, 4, 2))));
    }
}
