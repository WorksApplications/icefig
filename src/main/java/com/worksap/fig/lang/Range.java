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

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Range is an element generator on the basis of start point,
 * end point and next element function.
 */
public class Range<C extends Comparable<C>> {
    private C from;
    private C to;
    private boolean toIncluded;
    private Function<C, C> next;
    private BiFunction<C, Integer, C> biNext;

    /**
     * @param from The start point.
     * @throws NullPointerException if from is null.
     */
    public Range(C from) {
        this.from(from);
    }

    /**
     * @param from The start point.
     * @param to   The end point. It is included in the range.
     * @param next The next element generator.
     * @throws NullPointerException if from, to or next is null.
     */
    public Range(C from, C to, Function<C, C> next) {
        this.from(from);
        this.to(to);
        this.next(next);
    }

    /**
     * Set start point
     *
     * @param from The start point.
     * @return Current range object.
     * @throws NullPointerException if from is null.
     */
    public Range<C> from(C from) {
        Objects.requireNonNull(from);
        this.from = from;
        return this;
    }

    /**
     * Set end point. The end point is included in this range.
     *
     * @param to The end point.
     * @return Current range object.
     * @throws NullPointerException if to is null.
     */
    public Range<C> to(C to) {
        Objects.requireNonNull(to);
        this.to = to;
        toIncluded = true;
        return this;
    }

    /**
     * Set end point. The end point is excluded in this range.
     *
     * @param to The end point.
     * @return Current range object.
     * @throws NullPointerException if to is null.
     */
    public Range<C> until(C to) {
        Objects.requireNonNull(to);
        this.to = to;
        toIncluded = false;
        return this;
    }

    /**
     * Set next element generator.
     *
     * @param next The next element generator
     * @return Current range object.
     * @throws NullPointerException if next is null.
     */
    public Range<C> next(Function<C, C> next) {
        Objects.requireNonNull(next);
        this.next = next;
        return this;
    }

    /**
     * Set next element generator.
     *
     * @param next The next element generator
     * @return Current range object.
     * @throws NullPointerException if next is null.
     */
    public Range<C> next(BiFunction<C, Integer, C> next) {
        Objects.requireNonNull(next);
        this.biNext = next;
        return this;
    }

    /**
     * @return The start point
     */
    public C getFrom() {
        return from;
    }

    /**
     * @return The end point
     */
    public C getTo() {
        return to;
    }

    /**
     * @return Whether the end point is included in this range.
     */
    public boolean isToIncluded() {
        return toIncluded;
    }

    /**
     * Iterate each element of the range.
     *
     * @throws NullPointerException if action, this.from, this.to or this.next is null.
     */
    public void forEach(Consumer<? super C> action) {
        forEach((e, i) -> action.accept(e));
    }

    /**
     * Similar to {@link #forEach(Consumer)}, with additional parameter "index" as the second parameter of the lambda expression.
     *
     * @throws NullPointerException if action is null
     */
    public void forEach(BiConsumer<? super C, Integer> action) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        if (Objects.isNull(biNext)) {
            Objects.requireNonNull(next);
        }

        /* orientation = 0 means to is equal to from;
         * orientation > 0 means to is greater than from;
         * otherwise to is less than from.
         */
        int orientation = from.compareTo(to);
        int i = 0;
        C current = from;
        int cmp = current.compareTo(to);
        while (cmp * orientation > 0 || toIncluded && cmp == 0) {
            action.accept(current, i);
            if (Objects.nonNull(next)) {
                current = next.apply(current);
            } else {
                current = biNext.apply(current, i);
            }
            Objects.requireNonNull(current);
            cmp = current.compareTo(to);
            ++i;
        }
    }

    public Seq<C> toSeq() {
        MutableSeq<C> seq = Seqs.newMutableSeq();
        forEach((Consumer<C>) seq::appendInPlace);
        return seq;
    }

    public MutableSeq<C> toMutableSeq() {
        MutableSeq<C> seq = Seqs.newMutableSeq();
        forEach((Consumer<C>) seq::appendInPlace);
        return seq;
    }
}
