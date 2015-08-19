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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Factory class for construct Seq and MutableSeq
 */
public class Seqs {

    /**
     * Create an empty Seq
     */
    public static <T> Seq<T> newSeq() {
        return new SeqImpl<>();
    }

    /**
     * Create an Seq with the single value
     */
    public static <T> Seq<T> newSeq(T value) {
        Collection<T> collection = new ArrayList<>();
        collection.add(value);
        return new SeqImpl<>(collection);
    }

    /**
     * Create an Seq with the values
     */
    @SuppressWarnings({"varargs", "unchecked"})
    public static <T> Seq<T> newSeq(T... values) {
        return new SeqImpl<>(Arrays.asList(values));
    }

    /**
     * Create an Seq with the single values inside the collection
     */
    public static <T> Seq<T> newSeq(Collection<T> values) {
        return new SeqImpl<>(values);
    }

    /**
     * Create an empty MutableSeq
     */
    public static <T> MutableSeq<T> newMutableSeq() {
        return new SeqImpl<>();
    }

    /**
     * Create an MutableSeq with the single value
     */
    public static <T> MutableSeq<T> newMutableSeq(T value) {
        Collection<T> collection = new ArrayList<>();
        collection.add(value);
        return new SeqImpl<>(collection);
    }

    /**
     * Create an MutableSeq with the values
     */
    @SuppressWarnings({"varargs", "unchecked"})
    public static <T> MutableSeq<T> newMutableSeq(T... values) {
        return new SeqImpl<>(Arrays.asList(values));
    }

    /**
     * Create an MutableSeq with the single values inside the collection
     */
    public static <T> MutableSeq<T> newMutableSeq(Collection<T> values) {
        return new SeqImpl<>(values);
    }
}
