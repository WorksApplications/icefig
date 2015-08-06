package com.worksap.fig.lang;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

/**
 * Created by liuyang on 7/23/15.
 */
class SeqImpl<T> implements MutableSeq<T> {

    private final ArrayList<T> list;

    public SeqImpl() {
        this.list = new ArrayList<>();
    }

    public SeqImpl(Collection<T> collection) {
        this.list = new ArrayList<>(collection);
    }

    /**
     * Returns the element at index. A negative index counts from the end of self.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this seq
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &gt;= size() || index &lt; -size()</tt>)
     */
    @Override
    public T get(int index) {
        int size = size();
        if (index >= size || index < -size)
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size + ", should be within [" + (-size) + ", " + size + ")");
        if (index >= 0)
            return list.get(index);
        else
            return list.get(size + index);
    }

    @Override
    public <R> MutableSeq<R> map(Function<T, R> func) {
        Objects.requireNonNull(func);
        MutableSeq<R> result = new SeqImpl<>();
        this.forEach(i -> result.append(func.apply(i)));
        return result;
    }

    @Override
    public <R> MutableSeq<R> map(BiFunction<T, Integer, R> func) {
        Objects.requireNonNull(func);
        MutableSeq<R> result = new SeqImpl<>();
        this.forEach((s, i) -> result.append(func.apply(s, i)));
        return result;
    }

    @Override
    public <R> MutableSeq<R> flatMap(Function<T, Seq<R>> func) {
        Objects.requireNonNull(func);
        MutableSeq<R> result = new SeqImpl<>();
        this.forEach(i -> result.append(func.apply(i)));
        return result;
    }

    @Override
    public <R> MutableSeq<R> flatMap(BiFunction<T, Integer, Seq<R>> func) {
        Objects.requireNonNull(func);
        MutableSeq<R> result = new SeqImpl<>();
        this.forEach((s, i) -> result.append(func.apply(s, i)));
        return result;
    }

    @Override
    public MutableSeq<T> sample(int n) {
        MutableSeq<T> shuffled = shuffle();
        return shuffled.subSeq(0, Math.min(n, this.size()));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public MutableSeq<T> shuffle() {
        List<T> newList = new ArrayList<>(list);
        Collections.shuffle(newList);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<MutableSeq<T>> eachCons(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be positive number!");
        }
        MutableSeq<MutableSeq<T>> result = new SeqImpl<>();
        for (int i = 0; i <= this.size() - n; i++) {
            result.append(this.subSeq(i, i + n));
        }
        return result;
    }

    @Override
    public MutableSeq<T> sort(Comparator<? super T> comparator) {
        List<T> newList = new ArrayList<>(list);
        Collections.sort(newList, comparator);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> distinct() {
        return new SeqImpl<>(new LinkedHashSet<>(list));
    }

    @Override
    public MutableSeq<T> append(T value) {
        List<T> newList = new ArrayList<>(list);
        list.add(value);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> append(T... values) {
        return append(Arrays.asList(values));
    }

    @Override
    public MutableSeq<T> append(Collection<? extends T> collection) {
        List<T> newList = new ArrayList<>(list);
        list.addAll(collection);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> append(Seq<? extends T> seq) {
        List<T> newList = new ArrayList<>(list);
        seq.forEach((Consumer<T>) newList::add);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> appendInPlace(T value) {
        list.add(value);
        return this;
    }

    @Override
    public MutableSeq<T> appendInPlace(T... values) {
        for(T t: values) {
            list.add(t);
        }
        return this;
    }

    @Override
    public MutableSeq<T> appendInPlace(Collection<? extends T> collection) {
        list.addAll(collection);
        return this;
    }

    @Override
    public MutableSeq<T> appendInPlace(Seq<? extends T> seq) {
        list.addAll(seq.toArrayList());
        return this;
    }

    @Override
    public MutableSeq<T> prepend(T value) {
        List<T> newList = new ArrayList<>();
        newList.add(value);
        newList.addAll(list);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> prepend(T... values) {
        return prepend(Arrays.asList(values));
    }

    @Override
    public MutableSeq<T> prepend(Collection<? extends T> collection) {
        List<T> newList = new ArrayList<>();
        newList.addAll(collection);
        newList.addAll(list);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> prepend(Seq<? extends T> seq) {
        List<T> newList = new ArrayList<>();
        seq.forEach((Consumer<T>) newList::add);
        newList.addAll(list);
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> prependInPlace(T value) {
        list.add(0, value);
        return this;
    }

    @Override
    public MutableSeq<T> prependInPlace(T... values) {
        return prependInPlace(Arrays.asList(values));
    }

    @Override
    public MutableSeq<T> prependInPlace(Collection<? extends T> collection) {
        list.addAll(0, collection);
        return this;
    }

    @Override
    public MutableSeq<T> prependInPlace(Seq<? extends T> seq) {
        list.addAll(0, seq.toArrayList());
        return this;
    }

    @Override
    public MutableSeq<T> subSeq(int fromIndex, int toIndex) {
        return new SeqImpl<>(list.subList(fromIndex, toIndex));
    }

    @Override
    public MutableSeq<T> reject(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        List<T> newList = new ArrayList<>();
        this.forEach(e -> {
            if (!condition.test(e))
                newList.add(e);
        });
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> reject(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        List<T> newList = new ArrayList<>();
        this.forEach((e, i) -> {
            if (!condition.test(e, i))
                newList.add(e);
        });
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> filter(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        List<T> newList = new ArrayList<>();
        this.forEach(e -> {
            if (condition.test(e))
                newList.add(e);
        });
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> filter(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        List<T> newList = new ArrayList<>();
        this.forEach((e, i) -> {
            if (condition.test(e, i))
                newList.add(e);
        });
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<T> repeat(int times) {
        if (times <= 0)
            throw new IllegalArgumentException("times must be a positive number.");
        List<T> newList = new ArrayList<>();
        while (times > 0) {
            newList.addAll(list);
            times--;
        }
        return new SeqImpl<>(newList);
    }

    @Override
    public MutableSeq<MutableSeq<T>> eachSlice(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be a positive number.");
        List<MutableSeq<T>> newList = new ArrayList<>();
        int size = this.size();
        for (int i = 0; i < size; i += n) {
            newList.add(subSeq(i, i + n > size ? size : i + n));
        }
        return new SeqImpl<>(newList);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public MutableSeq<T> reverse() {
        List<T> newList = new ArrayList<>();
        for (int i = size() - 1; i >= 0; i--)
            newList.add(this.get(i));
        return new SeqImpl<>(newList);
    }


    private Seq<T> indexToSeq(int[] idxs) {
        Seq<T> result = new SeqImpl<>();
        for (int i : idxs) {
            result.append(get(i));
        }
        return result;
    }

    public void forEachCombination(int n, Consumer<Seq<T>> action) {
        Objects.requireNonNull(action);
        if (n <= 0) {
            throw new IllegalArgumentException("n should be a positive number");
        }
        if (n > size()) {
            return;
        }
        //Selected element indices of a valid combination
        int[] comb = new int[n];

        //initialize first combination by the first n elements
        for (int i = 0; i < n; i++) {
            comb[i] = i;
        }
        action.accept(indexToSeq(comb));

        while (comb[0] < size() - n) {
            for (int i = 0; i < n; i++) {
                if (i == n - 1 || comb[i + 1] - comb[i] > 1) { // find the first selected element that the next element of it is not selected
                    comb[i]++; // make the next element selected instead
                    // set all selected elements before i, to the beginning elements
                    for (int j = 0; j < i; j++) {
                        comb[j] = j;
                    }
                    action.accept(indexToSeq(comb));
                    break;
                }
            }
        }
    }

    public MutableSeq<MutableSeq<T>> eachCombination(int n) {
        MutableSeq<MutableSeq<T>> result = new SeqImpl<>();
        forEachCombination(n, s -> {
            result.append((MutableSeq<T>) s);
        });
        return result;
    }

    @Override
    public MutableSeq<T> clear() {
        list.clear();
        return this;
    }

    @Override
    public boolean contains(T t){
        return list.contains(t);
    }

    @Override
    public MutableSeq<T> set(int i, T t) {
        list.set(i, t);
        return this;
    }

    @Override
    public ArrayList<T> toArrayList() {
        return list;
    }

    @Override
    public MutableSeq<T> shuffleInPlace(){
        Collections.shuffle(list);
        return this;
    }

    @Override
    public MutableSeq<T> distinctInPlace() {
        Collection<T> collection = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(collection);
        return this;
    }

    @Override
    public MutableSeq<T> sortInPlace(Comparator<? super T> comparator) {
        Collections.sort(list, comparator);
        return this;
    }

    @Override
    public MutableSeq<T> rejectInPlace(Predicate<T> condition) {
        Objects.requireNonNull(condition);
        final Iterator<T> each = list.iterator();
        while (each.hasNext()) {
            if (condition.test(each.next())) {
                each.remove();
            }
        }
        return this;
    }

    @Override
    public MutableSeq<T> rejectInPlace(BiPredicate<T, Integer> condition) {
        Objects.requireNonNull(condition);
        final Iterator<T> each = list.iterator();
        int index = 0;
        while (each.hasNext()) {
            if (condition.test(each.next(), index)) {
                each.remove();
            }
            index++;
        }
        return this;
    }

    @Override
    public MutableSeq<T> repeatInPlace(int times) {
        if (times <= 0)
            throw new IllegalArgumentException("times must be a positive number.");
        else if (times >= 2) {
            times--;
            Collection<T> copy = new ArrayList<>(list);
            while (times > 0) {
                list.addAll(copy);
                times--;
            }
        }
        return this;
    }

    @Override
    public MutableSeq<T> compactInPlace() {
        list.removeIf(e -> e == null);
        return this;
    }

    @Override
    public MutableSeq<T> reverseInPlace() {
        int size = size();
        for (int i = 0; i < size / 2; i++) {
            T temp = this.get(i);
            this.set(i, this.get(size - 1 - i));
            this.set(size - 1 - i, temp);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeqImpl<?> seq = (SeqImpl<?>) o;
        return Objects.equals(list, seq.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "SeqImpl{" +
                "list=" + list +
                '}';
    }
}
