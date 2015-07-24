# Fig

Java lambda supplement

Java 8 delivered lambda expressions, but without the enhancement of basic libraries like List, Map, String, which makes
lambda expression still not delightful.

Borrowered from Ruby basic library, Fig intends to supply the missing.

## Quick Scan

```java
Seq<Integer> seq = Seq.of(1,2,3);
seq.shuffle(); // copy to a new seq and shuffle it
seq.shuffle$(); // shuffle the original seq
seq.forEachWithIndex((value, idx) -> {
    // (1, 0)  (2, 1)  (3, 2)
});
seq.forEachCons(2, (values)->{
    // [1,2]  [2, 3]
});

seq.join("-"); //"1-2-3"

seq.map(a -> a+ 1).distinct().reverse().join()
```

## Concept

### Not stream

Fig is different from Stream, and implemented without Stream. While, it is simpler concept -- supplement methods on basic library.

Stream has several characteristics:

1. Trends to process each element independently
2. Infinite that we can not get the size
3. Designed for large data flow performance

Thus, Stream may not be able to support operations related with the size of it, nor operations involving multiple or even random elements.

Fig targets on "small data" operations within application logic, to provide simple & beaufitul code writing about String, List, Map operation & transformation.

### No utilities

In traditional Java way, we use a lot of utilities (StringUtils, FileUtils) for the missing methods in standard library. While in Fig, we make an object oriented and functional way to free you from tedious code.

### Zero dependency

Fig has no external dependency except JDK 8.
