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

package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Range<C extends Comparable<C>> {
    private C lower;
    private C upper;
    private boolean upperInclusive;
    private Function<C, C> next;

    public Range(C lower) {
        this.from(lower);
    }

    public Range(C lower, C upper, Function<C, C> next) {
        this.from(lower);
        this.to(upper);
        this.next(next);
    }

    public Range<C> from(C lower) {
        Objects.requireNonNull(lower);
        this.lower = lower;
        return this;
    }

    public Range<C> to(C upper) {
        Objects.requireNonNull(upper);
        this.upper = upper;
        upperInclusive = true;
        return this;
    }

    public Range<C> until(C upper) {
        Objects.requireNonNull(upper);
        this.upper = upper;
        upperInclusive = false;
        return this;
    }

    public Range<C> next(Function<C, C> next) {
        Objects.requireNonNull(next);
        this.next = next;
        return this;
    }

    public C getLower() {
        return lower;
    }

    public C getUpper() {
        return upper;
    }

    public boolean isUpperInclusive() {
        return upperInclusive;
    }

    public Seq<C> toSeq() {
        Objects.requireNonNull(lower);
        Objects.requireNonNull(upper);
        Objects.requireNonNull(next);

        int dir = lower.compareTo(upper);
        if (dir == 0) {
            return upperInclusive ? Seqs.newSeq(lower) : Seqs.newSeq();
        }

        List<C> list = new ArrayList<>();
        C current = lower;
        while (true) {
            int cmp = current.compareTo(upper);
            if (upperInclusive && dir * cmp < 0
                    || !upperInclusive && dir * cmp <= 0) {
                break;
            }
            list.add(current);
            current = next.apply(current);
            Objects.requireNonNull(current);
        }

        return Seqs.newMutableSeq(list);
    }
}
