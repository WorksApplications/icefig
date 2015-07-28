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
     * Constructs an empty seq.
     * @return an empty seq
     */
    static <T> Seq<T> newSeq() {
        return new SeqImpl<>();
    }

    /**
     * Constructs a seq of the specified size. Each element in the seq is null.
     * @param size the initial size of the seq
     * @return the new seq
     */
    static <T> Seq<T> newSeq(int size) {
        return new SeqImpl<>(size);
    }

    /**
     * Constructs a seq of the specified size. Each element in the seq is defaultValue.
     * @param size the initial size of the seq
     * @param defaultValue the value of each element in the seq
     * @return the new seq
     */
    static <T> Seq<T> newSeq(int size, T defaultValue) {
        return new SeqImpl<>(size, defaultValue);
    }

    /**
     * Constructs a seq of the specified size.
     * Each element in this seq is created by passing the element’s index to the given {@link Function} and storing the return value.
     * @param size the initial size of the seq
     * @param func the {@link Function} used to create elements, accepting the element's index and returning a value to be stored into the seq.
     * @return the new seq
     * @throws NullPointerException if func is null
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
     * Removes elements which satisfy the condition, resulting a new seq without changing the original one.
     * @param condition the condition used to filter elements by passing the element,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> reject(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEach(e -> {
            if (!condition.test(e))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Removes elements which satisfy the condition on the seq itself.
     * @param condition the condition used to filter elements by passing the element,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the seq itself after removing elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> reject$(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        final Iterator<T> each = iterator();
        while (each.hasNext()) {
            if (condition.test(each.next())) {
                each.remove();
            }
        }
        return this;
    }

    /**
     * Removes elements which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     *     Similar to {@link #reject(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression
     * </p>
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the new seq without elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> rejectWithIndex(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEachWithIndex((e, i) -> {
            if (!condition.test(e, i))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Removes elements which satisfy the condition on the seq itself.
     * <p>
     *     Similar to {@link #reject$(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression
     * </p>
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false.
     * @return the seq itself after removing elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> rejectWithIndex$(BiPredicate<T, Integer> condition) {
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
     * Gets elements which satisfy the condition, resulting a new seq without changing the original one.
     * @param condition the condition used to filter elements by passing the element's index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> filter(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEach(e -> {
            if (condition.test(e))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Gets elements which satisfy the condition, resulting a new seq without changing the original one.
     * <p>
     *     Similar to {@link #filter(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     * @param condition the condition used to filter elements by passing the element and its index,
     *                  returns true if the element satisfies the condition, otherwise returns false
     * @return the new seq containing only the elements satisfying the condition
     * @throws NullPointerException if condition is null
     */
    default Seq<T> filterWithIndex(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEachWithIndex((e, i) -> {
            if (condition.test(e, i))
                copy.add(e);
        });
        return copy;
    }

    /**
     * Returns a new seq built by concatenating the <tt>times</tt> copies of this seq.
     * @param times times to repeat
     * @return the new seq
     * @throws IllegalArgumentException if <tt>times &lt;= 0</tt>
     */
    default Seq<T> repeat(int times) {
        if (times <= 0)
            throw new IllegalArgumentException("times must be a positive number.");
        Seq<T> result = new SeqImpl<>();
        while (times > 0) {
            result.addAll(this);
            times--;
        }
        return result;
    }

    /**
     * Returns the seq itself by concatenating the <tt>times</tt> copies of self.
     * @param times times to repeat
     * @return the seq itself after the change
     * @throws IllegalArgumentException if <tt>times &lt;= 0</tt>
     */
    default Seq<T> repeat$(int times) {
        if (times <= 0)
            throw new IllegalArgumentException("times must be a positive number.");
        else if (times >= 2) {
            times--;
            Seq<T> copy = Seq.of(this);
            while (times > 0) {
                this.addAll(copy);
                times--;
            }
        }
        return this;
    }
    
    /**
     * Returns a copy of the seq itself with all null elements removed.
     * @return the new seq with all null elements removed
     */
    default Seq<T> compact() {
        return this.filter(e -> e != null);
    }

    /**
     * Returns the seq itself with all null elements removed.
     * @return the seq itself with all null elements removed
     */
    default Seq<T> compact$() {
        removeIf(e -> e == null);
        return this;
    }

    /**
     * Returns the number of the specified element.
     * @param element the element to count
     * @return the number of the specified element
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
     * @param condition the condition used to filter elements by passing the element,
     *               returns true if the element satisfies the condition, otherwise returns false.
     * @return the number of elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default int countCondition(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        int count = 0;
        for (int i = 0; i < size(); i++){
            if (condition.test(this.get(i)))
                count++;
        }
        return count;
    }

    /**
     * Returns the number of elements which satisfy the condition.
     * <p>
     *     Similar to {@link #countCondition(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     * @param condition the condition used to filter elements by passing the element and its index,
     *               returns true if the element satisfies the condition, otherwise returns false.
     * @return the number of elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default int countConditionWithIndex(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        int count = 0;
        for (int i = 0; i < size(); i++){
            if (condition.test(this.get(i), i))
                count++;
        }
        return count;
    }

    /**
     * Returns the element at index. A negative index counts from the end of self.
     * If the index is out of range(<tt>index &lt; -size() || index &gt;= size()</tt>), a default value is returned.
     * <p>
     *     Similar to {@link #get(int)}. The difference between these two functions is how to solve the situation of illegal index.<br/>
     *     {@link #get(int, Object)} returns a default value when the index is illegal. <br/>
     *     {@link #get(int)} throws an exception({@link IndexOutOfBoundsException}) when the index is illegal.
     * </p>
     * @param index index of the element to return
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
     * Returns the element at index. A negative index counts from the end of self.
     * @param index index of the element to return
     * @return the element at the specified position in this seq
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &gt;= size() || index &lt; -size()</tt>)
     */
    T get(int index);


}
