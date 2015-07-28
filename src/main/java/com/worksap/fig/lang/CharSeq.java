package com.worksap.fig.lang;

import java.util.Objects;

/**
 * Elegant supplement for String in JDK
 */
public class CharSeq{
    private final String str;

    CharSeq(String str) {
        this.str = Objects.requireNonNull(str);
    }

    public CharSeq subSeq(int fromIndex, int toIndex) {
        return new CharSeq(str.substring(fromIndex, toIndex));
    }
    public CharSeq subSeq(int fromIndex) {
        return new CharSeq(str.substring(fromIndex));
    }

    public CharSeq suffix(CharSeq another){
        return new CharSeq(str + another);
    }
    public CharSeq prefix(CharSeq another){
        return new CharSeq(another + str);
    }

    public int length(){
        return str.length();
    }

    public boolean isEmpty(){
        return str.isEmpty();
    }


    /**
     * Make the first letter upper case, and all other letters lower case.
     */
    public CharSeq capitalize() {
        return isEmpty() ? this : this.subSeq(0, 1).toUpperCase().suffix(this.subSeq(1).toLowerCase());
    }

    public CharSeq toUpperCase() {
        return new CharSeq(str.toUpperCase());
    }

    public CharSeq toLowerCase() {
        return new CharSeq(str.toLowerCase());
    }

    public Seq<CharSeq> split(String regex) {
        return Seq.of(str.split(regex)).map(CharSeq::new);
    }

    public static CharSeq of(String str) {
        return new CharSeq(str);
    }

    @Override
    public String toString() {
        return str;
    }
}
