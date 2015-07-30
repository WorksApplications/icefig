package com.worksap.fig.lang;

import java.util.*;
import java.util.function.*;

/**
 * Elegant supplement for List in JDK
 */
public interface Seq<T> extends List<T> {


    /**
     * Transform each element of the seq into another value using the same function, resulting a new seq without changing the original one.
     *
     * @return The new seq after transformation.
     * @throws NullPointerException if func is null
     */
    default <R> Seq<R> map(Function<T, R> func) {
        Objects.requireNonNull(func);
        Seq<R> result = new SeqImpl<>();
        this.forEach(i -> result.add(func.apply(i)));
        return result;
    }

    /**
     * Similar to {@link #map(Function)}, with additional parameter "index" as the second parameter of the lambda expression.
     * @throws NullPointerException if func is null
     */
    default <R> Seq<R> map(BiFunction<T, Integer, R> func) {
        Objects.requireNonNull(func);
        Seq<R> result = new SeqImpl<>();
        this.forEach((s, i) -> result.add(func.apply(s, i)));
        return result;
    }

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
     * @throws NullPointerException if action is null
     */
    default void forEach(BiConsumer<? super T, Integer> action) {
        Objects.requireNonNull(action);
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
     * @throws NullPointerException if action is null
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

    /**
     * Sort the seq by the comparator, resulting a new seq, without changing the original seq.
     * @param comparator the comparator to determine the order of the seq. A
     *        {@code null} value indicates that the elements' <i>natural
     *        ordering</i> should be used.
     * @return A new seq sorted
     */
    default Seq<T> order(Comparator<? super T> comparator) {
        Seq<T> copy = new SeqImpl<>(this);
        Collections.sort(copy, comparator);
        return copy;
    }

    /**
     * Sort the seq itself by the comparator.
     * @param comparator the comparator to determine the order of the seq. A
     *        {@code null} value indicates that the elements' <i>natural
     *        ordering</i> should be used.
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
     * Push values into the seq at the end of it.
     * <p>
     * Note: This method <strong>does</strong> change the seq, which is different from {@link #concat(T...)}.
     * </p>
     *
     * @return The seq itself after the change
     * @throws NullPointerException if values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> push(T... values) {
        Objects.requireNonNull(values);
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
     * @throws NullPointerException if collection is null
     */
    default Seq<T> push(Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
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
     * @throws NullPointerException if values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> prepend(T... values) {
        Objects.requireNonNull(values);
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
     * @throws NullPointerException is collection is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> prepend(Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
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
     * @throws NullPointerException is values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> concat(T... values) {
        Objects.requireNonNull(values);
        return concat(Arrays.asList(values));
    }

    /**
     * Concat values to the seq at the end of it, resulting a new seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #push(Collection)}.
     * </p>
     *
     * @return A new seq after the change
     * @throws NullPointerException is collection is null
     */
    default Seq<T> concat(Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
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
     * @throws NullPointerException is values is null
     */
    @SuppressWarnings({"unchecked", "varargs"})
    default Seq<T> preConcat(T... values) {
        Objects.requireNonNull(values);
        return preConcat(Arrays.asList(values));
    }

    /**
     * Pre-concat values into the seq at the beginning of it. Values keep the order after being merged into the seq.
     * <p>
     * Note: This method does <strong>NOT</strong> change the seq, which is different from {@link #preConcat(Collection)}.
     * </p>
     *
     * @return The seq itself after the change.
     * @throws NullPointerException is collection is null
     */
    default Seq<T> preConcat(Collection<? extends T> collection) {
        Objects.requireNonNull(collection);
        Seq<T> copy = new SeqImpl<>(collection);
        copy.addAll(this);
        return copy;
    }

    /**
     * Create a new seq from values.
     * @throws NullPointerException if values is null
     */
    @SafeVarargs
    static <T> Seq<T> of(T... values) {
        Objects.requireNonNull(values);
        Collection<T> col = Arrays.asList(values);
        return new SeqImpl<>(col);
    }

    /**
     * Create a new seq from collection.
     * @throws NullPointerException if values is null
     */
    static <T> Seq<T> of(Collection<? extends T> values) {
        Objects.requireNonNull(values);
        return new SeqImpl<>(values);
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
     * Create a new seq which is the sub seq of the current one.
     *
     * @param fromIndex The start index, inclusive.
     * @param toIndex   The end index, exclusive.
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
     *         {@code (fromIndex < 0 || toIndex > size)}
     * @throws IllegalArgumentException if the endpoint indices are out of order
     *         {@code (fromIndex > toIndex)}
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
    default Seq<T> reject(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEach((e, i) -> {
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
    default Seq<T> reject$(BiPredicate<T, Integer> condition) {
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
    default Seq<T> filter(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        Seq<T> copy = new SeqImpl<>();
        this.forEach((e, i) -> {
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
    default int countIf(Predicate<T> condition) {
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
     *     Similar to {@link #countIf(Predicate)}, with additional parameter "index" as the second parameter of the lambda expression.
     * </p>
     * @param condition the condition used to filter elements by passing the element and its index,
     *               returns true if the element satisfies the condition, otherwise returns false.
     * @return the number of elements which satisfy the condition
     * @throws NullPointerException if condition is null
     */
    default int countIf(BiPredicate<T, Integer> condition) {
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

    /**
     * Slices this seq to construct some slices in the original order,
     * each of slice contains <tt>n</tt> elements(only the last slice can contain less than <tt>n</tt> elements).
     * @param n the number of elements in each slice except the last one
     * @return the collection of these slices
     * @throws IllegalArgumentException if <tt>n &lt;= 0</tt>
     */
    default Seq<Seq<T>> eachSlice(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be a positive number.");
        Seq<Seq<T>> result = new SeqImpl<>();
        int size = this.size();
        for (int i = 0; i < size; i += n) {
            result.add(this.subSeq(i, i + n > size ? size : i + n));
        }
        return result;
    }

    /**
     * Slices the seq to construct some slices and take action on each slice.
     * <p>
     *     Similar to {@link #eachSlice(int)}, but instead of returning the slice collection,
     *     it iterates the slice collection and takes action on each slice.
     * </p>
     * @param n the number of elements in each slice except the last one
     * @param action the action to take on each slice
     * @throws IllegalArgumentException if <tt>n &lt;= 0</tt>
     * @throws NullPointerException if action is null
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
     *     Similar to {@link #reduce(BinaryOperator)},
     *     with an additional parameter "init" as the initial value of the reduction
     * </p>
     * @param init the initial value for the accumulating function
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
     * @return the new seq with elements in reverse order
     */
    default Seq<T> reverse() {
        Seq<T> result = new SeqImpl<>();
        for (int i = size() - 1; i >= 0; i--)
            result.add(this.get(i));
        return result;
    }

    /**
     * Reverses the elements of this seq itself.
     * @return the seq itself with elements in reverse order
     */
    default Seq<T> reverse$() {
        int size = size();
        for (int i = 0; i < size / 2; i++) {
            T temp = this.get(i);
            this.set(i, this.get(size - 1 - i));
            this.set(size - 1 - i, temp);
        }
        return this;
    }
}
