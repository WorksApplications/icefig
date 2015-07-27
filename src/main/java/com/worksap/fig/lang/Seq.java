package com.worksap.fig.lang;

import java.util.*;
import java.util.function.*;

/**
 * Created by liuyang on 7/23/15.
 */
public interface Seq<T> extends List<T> {


    /**
     * Transform each element of the seq into another value using the same function, resulting a new seq without changing the original one.
     *
     * @return The new seq after transformation.
     */
    default <R> Seq<R> map(Function<T, R> func) {
        Seq<R> result = new SeqImpl<>();
        this.forEach(i -> result.add(func.apply(i)));
        return result;
    }

    /**
     * Similar to {@link #map(Function)}, with additional parameter "index" as the second parameter of the lambda expression.
     */
    default <R> Seq<R> mapWithIndex(BiFunction<T, Integer, R> func) {
        Seq<R> result = new SeqImpl<>();
        this.forEachWithIndex((s, i) -> result.add(func.apply(s, i)));
        return result;
    }

    /**
     * @return The beginning element of the seq.
     */
    default T first() {
        return get(0);
    }


    /**
     * @return The ending element of the seq.
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
        forEachWithIndex((t, i) -> {
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
    default Seq<T> sample(int n) {
        Seq<T> shuffled = shuffle();
        return shuffled.subSeq(0, Math.min(n, this.size()));
    }

    /**
     * Randomly shuffle the seq, resulting a new seq.
     */
    default Seq<T> shuffle() {
        Seq<T> copy = new SeqImpl<>(this);
        Collections.shuffle(copy);
        return copy;
    }

    /**
     * Randomly shuffle the seq itself.
     */
    default Seq<T> shuffle$() {
        Collections.shuffle(this);
        return this;
    }

    /**
     * Similar to {@link #forEach(Consumer)}, with additional parameter "index" as the second parameter of the lambda expression.
     */
    default void forEachWithIndex(BiConsumer<? super T, Integer> action) {
        for (int i = 0; i < this.size(); i++) {
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
    default Seq<Seq<T>> eachCons(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        Seq<Seq<T>> result = new SeqImpl<>();
        for (int i = 0; i <= this.size() - n; i++) {
            result.add(this.subSeq(i, i + n));
        }
        return result;
    }

    /**
     * Similar with {@link #eachCons(int)}, but instead of to return the transformed seq, it iterates the transformed seq and do action.
     *
     * @throws IllegalArgumentException if n <= 0
     */
    default void forEachCons(int n, Consumer<Seq<T>> action) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        for (int i = 0; i <= this.size() - n; i++) {
            action.accept(this.subSeq(i, i + n));
        }
    }

    /**
     * Sort the seq by the comparator, resulting a new seq, without changing the original seq.
     *
     * @return A new seq sorted
     */
    default Seq<T> order(Comparator<? super T> comparator) {
        Seq<T> copy = new SeqImpl<>(this);
        Collections.sort(copy, comparator);
        return copy;
    }

    /**
     * Sort the seq itself by the comparator.
     *
     * @return The seq itself after sorting
     */
    default Seq<T> order$(Comparator<? super T> comparator) {
        Collections.sort(this, comparator);
        return this;
    }

    /**
     * Reduce duplicated elements, keeping only the first occurrence, resulting a new seq.
     *
     * @return A new seq reduced
     */
    default Seq<T> distinct() {
        return new SeqImpl<>(new LinkedHashSet<>(this));
    }

    /**
     * Reduce duplicated elements, keeping only the first occurrence.
     *
     * @return The seq itself after reduced
     */
    default Seq<T> distinct$() {
        Collection<T> collection = new LinkedHashSet<>(this);
        clear();
        addAll(collection);
        return this;
    }

    /**
     * Find the first element which satisfy the condition.
     *
     * @return The element, or null if no element found.
     */
    default T findFirst(Predicate<T> condition) {
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
     */
    default T findLast(Predicate<T> condition) {
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
     */
    default int findFirstIndex(Predicate<T> condition) {
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
     */
    default int findLastIndex(Predicate<T> condition) {
        for (int i = size() - 1; i >= 0; i--) {
            T t = get(i);
            if (condition.test(t)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Push values into the seq at the end of it.
     * <p>
     * Note: This method <strong>does</strong> change the seq, which is different from {@link #concat(T...)}.
     * </p>
     *
     * @return The seq itself after the change
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> push(T... values) {
        push(Arrays.asList(values));
        return this;
    }

    /**
     * Push values into the seq at the end of it.
     * <p>
     * Note: This method <strong>does</strong> change the seq, which is different from {@link #concat(Collection)}.
     * </p>
     *
     * @return The seq itself after the change
     */
    default Seq<T> push(Collection<? extends T> collection) {
        addAll(collection);
        return this;
    }

    /**
     * Prepend values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method <strong>does</strong> change the seq, which is different from {@link #preConcat(T...)}.
     * </p>
     *
     * @return The seq itself after the change
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> prepend(T... values) {
        for (int i = values.length - 1; i >= 0; i--) {
            add(0, values[i]);
        }
        return this;
    }

    /**
     * Prepend values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method <strong>does</strong> change the seq, which is different from {@link #preConcat(Collection)}.
     * </p>
     *
     * @return The seq itself after the change
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> prepend(Collection<? extends T> collection) {
        T[] array = (T[]) collection.toArray();
        return prepend(array);
    }

    /**
     * Concat values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #push(T...)}.
     * </p>
     *
     * @return A new seq after the change
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> concat(T... values) {
        return concat(Arrays.asList(values));
    }

    /**
     * Concat values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #push(Collection)}.
     * </p>
     *
     * @return A new seq after the change
     */
    default Seq<T> concat(Collection<? extends T> collection) {
        Seq<T> copy = new SeqImpl<>(this);
        copy.addAll(collection);
        return copy;
    }

    /**
     * Pre-concat values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #prepend(T...)}.
     * </p>
     *
     * @return The seq itself after the change
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> preConcat(T... values) {
        return preConcat(Arrays.asList(values));
    }

    /**
     * Pre-concat values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #preConcat(Collection)}.
     * </p>
     *
     * @return The seq itself after the change.
     */
    default Seq<T> preConcat(Collection<? extends T> collection) {
        Seq<T> copy = new SeqImpl<>(collection);
        copy.addAll(this);
        return copy;
    }

    /**
     * Create a new seq from values.
     */
    @SafeVarargs
    static <T> Seq<T> of(T... values) {
        Collection<T> col = Arrays.asList(values);
        return new SeqImpl<>(col);
    }

    /**
     * Create a empty new seq.
     */
    static <T> Seq<T> newSeq() {
        return new SeqImpl<>();
    }

    /**
     * Create a new seq with size null elements
     */
    static <T> Seq<T> newSeq(int size) {
        return new SeqImpl<>(size);
    }

    /**
     * Create a new seq with size defaultValue elements
     */
    static <T> Seq<T> newSeq(int size, T defaultValue) {
        return new SeqImpl<>(size, defaultValue);
    }

    /**
     * Create a new seq with size elements, each of elements is generated by a {@link Function}.
     */
    static <T> Seq<T> newSeq(int size, Function<Integer, T> func) {
        Objects.requireNonNull(func);
        Seq<T> seq = new SeqImpl<>(size);
        for (int i = 0; i < size; i++) {
            seq.set(i, func.apply(i));
        }
        return seq;
    }

    /**
     * Create a new seq from collection.
     */
    static <T> Seq<T> of(Collection<? extends T> values) {
        return new SeqImpl<>(values);
    }

    /**
     * Create a new seq which is the sub seq of the current one.
     *
     * @param fromIndex The start index, inclusive.
     * @param toIndex   The end index, exclusive.
     */
    default Seq<T> subSeq(int fromIndex, int toIndex) {
        return new SeqImpl<>(this.subList(fromIndex, toIndex));
    }

    /**
     * Remove elements which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @return The new seq after the change
     */
    default Seq<T> removeIf(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEachWithIndex((e, i) -> {
            if (!condition.test(e, i))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Remove elements which satisfy the condition at the original seq.
     *
     * @return The seq itself after the change
     */
    default Seq<T> removeIf$(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        final Iterator<T> each = iterator();
        int index = 0;
        while (each.hasNext()) {
            if (condition.test(each.next(), index)) {
                each.remove();
            }
            index++;
        }
        return this;
    }

    /**
     * Get elements which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @return The new seq
     */
    default Seq<T> getIf(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEach(e -> {
            if (condition.test(e))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Get elements which satisfy the condition, resulting a new seq without changing the original one.
     *
     * @return The new seq
     */
    default Seq<T> getIf(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEachWithIndex((e, i) -> {
            if (condition.test(e, i))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Get the element with index. If the index is illegal, return a default value
     * @param i index
     * @param defaultValue default value
     * @return the element or default value
     */
    default T get(int i, T defaultValue) {
        if (i < 0 || i >= this.size()) {
            return defaultValue;
        }
        return this.get(i);
    }

    /**
     * Returns a new {@link Seq} built by concatenating the times copies of self.
     * @return the new seq
     */
    default Seq<T> repeat(int times) {
        if (times < 0)
            throw new IllegalArgumentException("times must be a positive number.");
        Seq<T> result = new SeqImpl<>();
        while (times > 0) {
            result.addAll(this);
            times--;
        }
        return result;
    }
    /**
     * Change the original {@link Seq} by concatenating the times copies of self.
     * @return the seq itself after the change
     */
    default Seq<T> repeat$(int times) {
        if (times < 0)
            throw new IllegalArgumentException("times must be a positive number.");
        else if (times == 0)
            clear();
        else if (times == 1)
            ;
        else {
            times--;
            while (times > 0) {
                this.addAll(this);
                times--;
            }
        }
        return this;
    }

    /**
     * Returns the element at index. A negative index counts from the end of self.
     */
    default T at(int index) {
        int size = size();
        if (index >= size || index < - size)
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size + ", should be within [" + (-size) + ", " + size + ")");
        if (index >= 0)
            return this.get(index);
        else
            return this.get(size + index);
    }

    /**
     * Returns a copy of self with all null elements removed.
     * @return the new seq
     */
    default Seq<T> compact(){
        return this.getIf(e -> e != null);
    }

    /**
     * Returns self with all nil elements removed.
     * @return the seq itself after the change
     */
    default Seq<T> compact$(){
        removeIf(e -> e == null);
        return this;
    }

    /**
     * Returns the number of elements.
     */
    default int count(T element) {
        int count = 0;
        for (int i = 0; i < size(); i++){
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
     */
    default int count(Predicate<T> filter) {
        Objects.requireNonNull(filter);
        int count = 0;
        for (int i = 0; i < size(); i++){
            if (filter.test(this.get(i)))
                count++;
        }
        return count;
    }
    /**
     * Returns the number of elements which satisfy the condition.
     */
    default int count(BiPredicate<T, Integer> filter) {
        Objects.requireNonNull(filter);
        int count = 0;
        for (int i = 0; i < size(); i++){
            if (filter.test(this.get(i), i))
                count++;
        }
        return count;
    }
}
