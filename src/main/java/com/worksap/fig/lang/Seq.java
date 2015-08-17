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

import java.util.*;
import java.util.function.*;

/**
 * Elegant supplement for List in JDK
 */
public interface Seq<T> {
    /**
     * Transform each element of the seq into another value using the same function, resulting a new seq without changing the original one.
     *
     * @return The new seq after transformation.
     * @throws NullPointerException if func is null
     */
    <R> Seq<R> map(Function<T, R> func);

    /**
     * Similar to {@link #map(Function)}, with additional parameter "index" as the second parameter of the lambda expression.
     *
     * @throws NullPointerException if func is null
     */
    <R> Seq<R> map(BiFunction<T, Integer, R> func);

    /**
     * Transform each element into a seq, and concat all seq together into a new seq.
     *
     * @return The new seq after transformation.
     * @throws NullPointerException if func is null
     */
    <R> Seq<R> flatMap(Function<T, Seq<R>> func);

    /**
     * Similar to {@link #flatMap(Function)}, with additional parameter "index" as the second parameter of the lambda expression.
     *
     * @throws NullPointerException if func is null
     */
    <R> Seq<R> flatMap(BiFunction<T, Integer, Seq<R>> func);

    /**
     * @return The beginning element of the seq.
     * @throws IndexOutOfBoundsException if the seq is empty
     */
    default T first() {
        return get(0);
    }

    /**
     * @return The ending element of the seq.
     * @throws IndexOutOfBoundsException if the seq is empty
     */
    default T last() {
        return get(size() - 1);
    }

    /**
     * Convert all elements into String, and connect each String together to a single String, following the same order of the seq.
     */
    default CharSeq join() {
        return join("");
    }

    /**
     * Convert all elements into String, and connect each String together to a single String, following the same order of the seq.
     * Insert a delimiter at each connection point.
     */
    default CharSeq join(CharSequence delimiter) {
        return join(delimiter, "", "");
    }

    /**
     * Convert all elements into String, and connect each String together to a single String, following the same order of the seq.
     * Insert a delimiter at each connection point. Add prefix and suffix to the final result.
     */
    default CharSeq join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        forEach((t, i) -> {
            if (i != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(t);
        });
        stringBuilder.append(suffix);
        return CharSeq.of(stringBuilder.toString());
    }

    /**
     * Randomly find an element in the seq.
     *
     * @return The selected element, or null if the seq is empty.
     */
    default T sample() {
        if (size() == 0) {
            return null;
        }
        Random rand = new Random();
        int randomNum = rand.nextInt(size());
        return get(randomNum);
    }

    /**
     * Randomly find n elements in the seq.
     *
     * @return A new seq of the selected elements. If the size of seq is lower than n, return all elements.
     * Return empty result if the seq is empty. The order of selected elements may be changed.
     */
    Seq<T> sample(int n);

    /**
     * Get the number of elements in this seq.
     */
    int size();

    boolean contains(T t);

    /**
     * Randomly shuffle the seq, resulting a new seq.
     */
    Seq<T> shuffle();

    ArrayList<T> toArrayList();


    /**
     * Iterate each element of the seq.
     *
     * @throws NullPointerException if action is null
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < this.size(); i++) {
            action.accept(this.get(i));
        }
    }

    /**
     * Similar to {@link #forEach(Consumer)}, with additional parameter "index" as the second parameter of the lambda expression.
     *
     * @throws NullPointerException if action is null
     */
    default void forEach(BiConsumer<? super T, Integer> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < this.size(); i++) {
            action.accept(this.get(i), i);
        }
    }

    /**
     * Iterate each element of the seq from the end to the beginning.
     *
     * @throws NullPointerException if action is null
     */
    default void forEachReverse(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (int i = size() - 1; i >= 0; i--) {
            action.accept(this.get(i));
        }
    }

    /**
     * Similar to {@link #forEachReverse(Consumer)}, with additional parameter "index" as the second parameter of the lambda expression.
     *
     * @throws NullPointerException if action is null
     */
    default void forEachReverse(BiConsumer<? super T, Integer> action) {
        Objects.requireNonNull(action);
        for (int i = size() - 1; i >= 0; i--) {
            action.accept(this.get(i), i);
        }
    }


    /**
     * Transform the seq, to a seq of each continuous n elements starting from each element.
     * <p>
     * [1, 2, 3, 4] with n=2 will result to [[1, 2], [2, 3], [3, 4]]
     * </p>
     * If the size of seq is lower than n, result will be empty.
     *
     * @throws IllegalArgumentException if n <= 0
     */
    Seq<? extends Seq<T>> eachCons(int n);

    /**
     * Similar with {@link #eachCons(int)}, but instead of to return the transformed seq, it iterates the transformed seq and do action.
     *
     * @throws NullPointerException     if action is null
     * @throws IllegalArgumentException if n <= 0
     */
    default void forEachCons(int n, Consumer<Seq<T>> action) {
        Objects.requireNonNull(action);
        if (n <= 0) {
            throw new IllegalArgumentException("n should be a positive number!");
        }
        for (int i = 0; i <= this.size() - n; i++) {
            action.accept(this.subSeq(i, i + n));
        }
    }

    boolean isEmpty();

    Object[] toArray();

    /**
     * Sort the seq by the comparator, resulting a new seq, without changing the original seq.
     *
     * @param comparator the comparator to determine the order of the seq. A
     *                   {@code null} value indicates that the elements' <i>natural
     *                   ordering</i> should be used.
     * @return A new seq sorted
     */
    Seq<T> sort(Comparator<? super T> comparator);

    /**
     * Reduce duplicated elements, keeping only the first occurrence, resulting a new seq.
     *
     * @return A new seq reduced
     */
    Seq<T> distinct();

    /**
     * Find the first element which satisfy the condition.
     *
     * @return The element, or null if no element found.
     * @throws NullPointerException if condition is null
     */
    default T findFirst(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            T t = get(i);
            if (condition.test(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Find the last element which satisfy the condition.
     *
     * @return The element, or null if no element found.
     * @throws NullPointerException if condition is null
     */
    default T findLast(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = size() - 1; i >= 0; i--) {
            T t = get(i);
            if (condition.test(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Find the index of the first element which satisfy the condition.
     *
     * @return The index, or -1 if no element found.
     * @throws NullPointerException if condition is null
     */
    default int findFirstIndex(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            T t = get(i);
            if (condition.test(t)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the index of the last element which satisfy the condition.
     *
     * @return The index, or -1 if no element found.
     * @throws NullPointerException if condition is null
     */
    default int findLastIndex(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = size() - 1; i >= 0; i--) {
            T t = get(i);
            if (condition.test(t)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Append values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is values is null
     */
    Seq<T> append(T value);

    /**
     * Append values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    Seq<T> append(T... values);

    /**
     * Append values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is collection is null
     */
    Seq<T> append(Collection<? extends T> collection);

    /**
     * Append values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is collection is null
     */
    Seq<T> append(Seq<? extends T> seq);

    /**
     * Prepend values into the seq at the beginning of it. Values keep the parameter order after being prepended.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is values is null
     */
    Seq<T> prepend(T values);

    /**
     * Prepend values into the seq at the beginning of it. Values keep the parameter order after being prepended.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    Seq<T> prepend(T... values);

    /**
     * Prepend values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is collection is null
     */
    Seq<T> prepend(Collection<? extends T> collection);

    /**
     * Prepend values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is collection is null
     */
    Seq<T> prepend(Seq<? extends T> seq);

    /**
     * Create a new seq which is the sub seq of the current one.
     *
     * @param fromIndex The start index, inclusive.
     * @param toIndex   The end index, exclusive.
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
     *                                   {@code (fromIndex < 0 || toIndex > size)}
     * @throws IllegalArgumentException  if the endpoint indices are out of order
     *                                   {@code (fromIndex > toIndex)}
     */
    Seq<T> subSeq(int fromIndex, int toIndex);

    /**
     * Removes elements which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @param condition the condition used to filter elements by passing the element,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> reject(Predicate<T> condition);

    /**
     * Removes elements which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     * Similar to {@link #reject(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression
     * </p>
     *
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> reject(BiPredicate<T, Integer> condition);

    /**
     * Removes elements at the front of this seq which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @param condition the condition used to filter elements by passing the element,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> rejectWhile(Predicate<T> condition);

    /**
     * Removes elements at the front of this seq which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     * Similar to {@link #reject(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression
     * </p>
     *
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> rejectWhile(BiPredicate<T, Integer> condition);

    /**
     * Gets elements which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @param condition the condition used to filter elements by passing the element's index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> filter(Predicate<T> condition);

    /**
     * Gets elements which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     * Similar to {@link #filter(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     *
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> filter(BiPredicate<T, Integer> condition);

    /**
     * Gets elements at the front of this seq which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @param condition the condition used to filter elements by passing the element's index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> filterWhile(Predicate<T> condition);

    /**
     * Gets elements at the front of this seq which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     * Similar to {@link #filter(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     *
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    Seq<T> filterWhile(BiPredicate<T, Integer> condition);

    /**
     * Returns a new seq built by concatenating the <tt>times</tt> copies of this seq.
     *
     * @param times times to repeat
     * @return the new seq
     * @throws IllegalArgumentException if <tt>times &lt;= 0</tt>
     */
    Seq<T> repeat(int times);

    /**
     * Returns a copy of the seq itself with all null elements removed.
     *
     * @return the new seq with all null elements removed
     */
    Seq<T> compact();

    /**
     * Returns the number of the specified element.
     *
     * @param element the element to countIf
     * @return the number of the specified element
     */
    default int count(T element) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (element == null) {
                if (this.get(i) == null)
                    count++;
            } else {
                if (this.get(i) != null && this.get(i).equals(element))
                    count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of elements which satisfy the condition.
     *
     * @param condition the condition used to filter elements by passing the element,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the number of elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default int countIf(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (condition.test(this.get(i)))
                count++;
        }
        return count;
    }

    /**
     * Returns the number of elements which satisfy the condition.
     * <p>
     * Similar to {@link #countIf(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     *
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the number of elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default int countIf(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        int count = 0;
        for (int i = 0; i < size(); i++) {
            if (condition.test(this.get(i), i))
                count++;
        }
        return count;
    }

    /**
     * Returns the element at index. A negative index counts from the end of self.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this seq
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &gt;= size() || index &lt; -size()</tt>)
     */
    T get(int index);

    /**
     * Returns the element at index. A negative index counts from the end of self.
     * If the index is out of range(<tt>index &lt; -size() || index &gt;= size()</tt>), a default value is returned.
     * <p>
     * Similar to {@link #get(int)}. The difference between these two functions is how to solve the situation of illegal index.<br/>
     * {@link #get(int, Object)} returns a default value when the index is illegal. <br/>
     * {@link #get(int)} throws an exception({@link IndexOutOfBoundsException}) when the index is illegal.
     * </p>
     *
     * @param index        index of the element to return
     * @param defaultValue default value to return when the index is out of range
     * @return The element at the specified position in this seq, or default value if the index is out of range
     */
    default T get(int index, T defaultValue) {
        if (index < -this.size() || index >= this.size()) {
            return defaultValue;
        }
        return this.get(index);
    }

    /**
     * Slices this seq to construct some slices in the original order,
     * each of slice contains <tt>n</tt> elements(only the last slice can contain less than <tt>n</tt> elements).
     *
     * @param n the number of elements in each slice except the last one
     * @return the collection of these slices
     * @throws IllegalArgumentException if <tt>n &lt;= 0</tt>
     */
    Seq<? extends Seq<T>> eachSlice(int n);

    /**
     * Slices the seq to construct some slices and take action on each slice.
     * <p>
     * Similar to {@link #eachSlice(int)}, but instead of returning the slice collection,
     * it iterates the slice collection and takes action on each slice.
     * </p>
     *
     * @param n      the number of elements in each slice except the last one
     * @param action the action to take on each slice
     * @throws IllegalArgumentException if <tt>n &lt;= 0</tt>
     * @throws NullPointerException     if action is null
     */
    default void forEachSlice(int n, Consumer<Seq<T>> action) {
        Objects.requireNonNull(action);
        if (n <= 0)
            throw new IllegalArgumentException("n should be a positive number.");
        int size = this.size();
        for (int i = 0; i < size; i += n) {
            action.accept(this.subSeq(i, i + n > size ? size : i + n));
        }
    }

    /**
     * Performs a reduction on the elements of this seq, using the provided
     * binary operation, and returns the reduced value.
     *
     * @param accumulator the binary operation for combining two values
     * @return the result of the reduction, or null if the seq is empty
     * @throws NullPointerException if accumulator is null
     */
    default T reduce(BinaryOperator<T> accumulator) {
        Objects.requireNonNull(accumulator);
        boolean foundAny = false;
        T result = null;
        for (int i = 0; i < size(); i++) {
            if (!foundAny) {
                foundAny = true;
                result = this.get(i);
            } else
                result = accumulator.apply(result, this.get(i));
        }
        return result;
    }

    /**
     * Performs a reduction on the elements of this seq, using the provided initial value
     * and a binary function, and returns the reduced value.
     * <p>
     * Similar to {@link #reduce(BinaryOperator)},
     * with an additional parameter "init" as the initial value of the reduction
     * </p>
     *
     * @param init        the initial value for the accumulating function
     * @param accumulator the binary function for combining two values
     * @return the result of the reduction
     * @throws NullPointerException if accumulator is null
     */
    default <R> R reduce(R init, BiFunction<R, T, R> accumulator) {
        Objects.requireNonNull(accumulator);
        R result = init;
        for (int i = 0; i < size(); i++)
            result = accumulator.apply(result, this.get(i));
        return result;
    }

    /**
     * Constructs a new seq containing all the elements of this seq in reverse order.
     *
     * @return the new seq with elements in reverse order
     */
    Seq<T> reverse();

    /**
     * Similar with {@link #eachCombination(int)}, but instead of to return the seq, it iterates the seq and do action.
     */
    void forEachCombination(int n, Consumer<Seq<T>> action);


    /**
     * Return a new Seq of all combinations of length n of elements from this seq.
     * The implementation makes no guarantees about the order in which the combinations are returned.
     * This method uses the index as the identity of each element, thus it may return a combination with duplicated elements inside.
     * If you want to avoid this, use {@link #distinct()} before calling this method.
     */
    Seq<? extends Seq<T>> eachCombination(int n);


    /**
     * Check whether any element of the seq satisfies the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean any(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (condition.test(get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether any element of the seq satisfies the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean any(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (condition.test(get(i), i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    int indexOf(T t);

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    int lastIndexOf(T t);

    /**
     * Check whether this seq contains the sub seq, if the given seq is empty, always return true.
     *
     * @throws NullPointerException if seq is null
     */
    default boolean containsSubSeq(Seq<T> seq) {
        return indexOfSubSeq(seq) != -1;
    }

    /**
     * Return the first index of the given sub seq, or -1 if the given seq is not a sub seq.
     * If the given seq is empty, always return 0.
     *
     * @throws NullPointerException if seq is null
     */
    default int indexOfSubSeq(Seq<T> seq) {
        Objects.requireNonNull(seq);

        if (seq.isEmpty()) {
            return 0;
        }

        if (size() < seq.size()) {
            return -1;
        }

        //Sunday algorithm

        Map<T, Integer> lastIndex = new HashMap<>();
        seq.forEach(lastIndex::put);

        int startI = 0, size = size(), len = seq.size();
        while (size - startI >= len) {
            for (int i = 0; ; i++) {
                if (!get(startI + i).equals(seq.get(i))) {
                    if (startI + len >= size) {
                        return -1;
                    }
                    T next = get(startI + len);
                    Integer last = lastIndex.get(next);
                    if (last == null) {
                        startI += len + 1;
                    } else {
                        startI += len - last;
                    }
                    break;
                } else if (i == len - 1) {
                    return startI;
                }
            }
        }
        return -1;
    }

    /**
     * Return the last index of the given sub seq, or -1 if the given seq is not a sub seq.
     * If the given seq is empty, always return 0.
     *
     * @throws NullPointerException if seq is null
     */
    default int lastIndexOfSubSeq(Seq<T> seq) {
        Objects.requireNonNull(seq);

        if (seq.isEmpty()) {
            return 0;
        }

        if (size() < seq.size()) {
            return -1;
        }

        //Sunday algorithm

        Map<T, Integer> lastIndex = new HashMap<>();
        seq.forEachReverse(lastIndex::put);

        int size = size(), endI = size - 1, len = seq.size();
        while (endI >= len - 1) {
            for (int i = 0; ; i++) {
                if (!get(endI - i).equals(seq.get(len - 1 - i))) {
                    if (endI - len < 0) {
                        return -1;
                    }
                    T next = get(endI - len);
                    Integer last = lastIndex.get(next);
                    if (last == null) {
                        endI -= len + 1;
                    } else {
                        endI -= last + 1;
                    }
                    break;
                } else if (i == len - 1) {
                    return endI - len + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Computes the multiset intersection between this seq and another seq.
     *
     * @return A new seq which contains all elements of this seq which also appear in that, keeping the order of this seq.
     * If an element value x appears n times in that, then the first n occurrences of x will be retained in the result, but any following occurrences will be omitted.
     *
     * @throws NullPointerException if the parameter seq is null
     */
    Seq<T> intersect(Seq<T> seq);

    /**
     * Computes the multiset difference between this seq and another seq.
     *
     * @return A new seq which contains all elements of this seq except some of occurrences of elements that also appear in that, keeping the order of this seq.
     * If an element value x appears n times in that, then the first n occurrences of x will not form part of the result, but any following occurrences will.
     *
     * @throws NullPointerException if the parameter seq is null
     */
    Seq<T> difference(Seq<T> seq);

    /**
     * Check whether all elements of the seq satisfy the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean all(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (!condition.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether all elements of the seq satisfy the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean all(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (!condition.test(get(i), i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether no element of the seq satisfies the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean none(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (condition.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether no element of the seq satisfies the condition
     *
     * @throws NullPointerException if condition is null
     */
    default boolean none(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        for (int i = 0; i < size(); i++) {
            if (condition.test(get(i), i)) {
                return false;
            }
        }
        return true;
    }
}
