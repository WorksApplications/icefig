# Fig [![Build Status](https://travis-ci.org/wapatesh/fig.svg?branch=master)](https://travis-ci.org/wapatesh/fig) [![Documentation](https://readthedocs.org/projects/svg-pottery/badge/?version=latest)](http://www.javadoc.io/doc/com.worksap/fig) [![codecov.io](http://codecov.io/github/wapatesh/fig/coverage.svg?branch=master)](http://codecov.io/github/wapatesh/fig?branch=master)

Java elegant supplement

Java 8 delivered lambda expressions, but without the enhancement of basic libraries like List, Map, String, which makes
lambda expression still not delightful.

Borrowered from Ruby basic library, Fig intends to supply the missing.

## Quick Scan

Elegant alternative to List: **Seq**
```java
Seq<Integer> seq = Seqs.newSeq(1,2,3);
seq.shuffle(); // copy to a new seq and shuffle it
seq.shuffleInPlace(); // shuffle the original seq
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
Hash<Integer, Integer> hash = Hashes.<Integer, Integer>newHash().put(1, 2).put(2, 3).put(3, 3);
hash.containsAny((k, v) -> k+v == 5 ); //true
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
    <version>0.1.2</version>
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

Basically, the vast majority operations on List, Map, String don't need a Stream. Stream brings great merits on big data processing, but when we're not facing performance problem (operating a list of about 10x elements), it is an over kill. 
Yet Stream brings the 2 additional steps "Stream()" and "collect()", which is sometimes annoying to write.

Fig targets on "small data" operations within application logic, to provide simple & beautiful code writing about String, List, Map operation & transformation.

### No utilities

In traditional Java way, we use a lot of utilities (StringUtils, FileUtils) for the missing methods in standard library. While in Fig, we make an object oriented and functional way to free you from tedious code.

### Zero runtime dependency

Fig has no external runtime dependency except JDK 8.

### Mutable & default interfaces

Fig firstly aggregates all operations which do not change the state into a default interface(e.x. Seq, Hash).
On the other hand, there are interfaces named "mutableXXX" extending the default ones with additional in-place operations, which are commonly named `xxxInPlace`.

If you don't want the ability to change the object, you can use the default interface to let compiler check it for you. And it is the recommended way.

Note that the default interface doesn't mean immutability of the object it is on, it only ensures "if outside only uses this interface on the object, the object will not be changed".

### Conventions

Fig uses conventions on method names similar with Ruby. If there is a pair of methods `name, nameInPlace`, method ends with `InPlace` means calling this method will change the object itself, while calling the other won't.


## License

[Apache License 2.0](LICENSE)

## Contribution

Feel free to submit issues & PRs
