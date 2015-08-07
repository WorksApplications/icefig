# Fig [![Build Status](https://travis-ci.org/wapatesh/fig.svg?branch=master)](https://travis-ci.org/wapatesh/fig) [![Documentation](https://readthedocs.org/projects/svg-pottery/badge/?version=latest)](http://www.javadoc.io/doc/com.worksap/fig)[![codecov.io](http://codecov.io/github/wapatesh/fig/coverage.svg?branch=master)](http://codecov.io/github/wapatesh/fig?branch=master)

Java elegant supplement

Java 8 delivered lambda expressions, but without the enhancement of basic libraries like List, Map, String, which makes
lambda expression still not delightful.

Borrowered from Ruby basic library, Fig intends to supply the missing.

## Quick Scan

Elegant alternative to List: **Seq**
```java
Seq<Integer> seq = Seq.of(1,2,3);
seq.shuffle(); // copy to a new seq and shuffle it
seq.shuffle$(); // shuffle the original seq
seq.forEach((value, idx) -> { // with index
    // (1, 0)  (2, 1)  (3, 2)
});
seq.forEachCons(2, (values)->{
    // [1,2]  [2, 3]
});

seq.join("-"); //"1-2-3"

seq.map(a -> a+ 1).distinct().reverse().join()
```

Elegant alternative to Map: **Hash**
```java
Hash<Integer, Integer> hash = Hash.newHash();
hash.set(1, 2).set(2, 3).set(3, 3);
hash.containsAny((k, v)-> k+v == 5 ); //true
hash.keysOf(3); // [2, 3]
```

Elegant alternative to String: **CharSeq**
```java
CharSeq str = CharSeq.of("a b c d e f g");
str.split(" ").join("-").capitalize(); //"A-b-c-d-e-f-g"
str.partition("d e").map(CharSeq::trim);  //["a b c", "d e", "f g"]
```

[Full Javadoc](http://www.javadoc.io/doc/com.worksap/fig)

## Include it

```xml
<dependency>
    <groupId>com.worksap</groupId>
    <artifactId>fig</artifactId>
    <version>0.1.0</version>
</dependency>
```


## Concept

### Not stream

Fig is different from Stream, and implemented without Stream. While, it is simpler concept -- supplement methods on basic library.

Stream has several characteristics:

1. Trends to process each element independently
2. Infinite that we can not get the size
3. Designed for large data flow performance

Thus, Stream may not be able to support operations related with the size of it, nor operations involving multiple or even random elements.

Fig targets on "small data" operations within application logic, to provide simple & beautiful code writing about String, List, Map operation & transformation.

### No utilities

In traditional Java way, we use a lot of utilities (StringUtils, FileUtils) for the missing methods in standard library. While in Fig, we make an object oriented and functional way to free you from tedious code.

### Zero runtime dependency

Fig has no external runtime dependency except JDK 8.

### Conventions

Fig uses conventions on method names similar with Ruby. If there is a pair of methods `name, name$`, method ends with `$` means calling this method will change the object itself, while calling the other won't.


## License

[Apache License 2.0](LICENSE)

## Contribution

Feel free to submit issues & PRs
