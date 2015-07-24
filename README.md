# fig

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
```
