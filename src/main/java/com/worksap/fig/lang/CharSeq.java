package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Elegant supplement for String in JDK
 */
public class CharSeq {
    private final String str;

    CharSeq(String str) {
        this.str = Objects.requireNonNull(str);
    }

    /**
     * Returns a CharSeq that contains a substring of this CharSeq's string.
     * The substring begins at the specified {@code beginIndex} and
     * extends to the character at index {@code endIndex - 1}.
     * Thus the length of the substring is {@code endIndex-beginIndex}.
     *
     * <p>
     * Examples:
     * <blockquote><pre>
     * CharSeq.of("hamburger").subSeq(4, 8) returns CharSeq.of("urge")
     * CharSeq.of("smiles").subSeq(1, 5) returns CharSeq.of("mile")
     * </pre></blockquote>
     * </p>
     *
     * @param   fromIndex   the beginning index, inclusive.
     * @param   toIndex     the ending index, exclusive.
     * @return  CharSeq with the specified substring.
     */
    public CharSeq subSeq(int fromIndex, int toIndex) {
        if (this.isEmpty()) {
            return this;
        }
        int from = fromIndex, to = toIndex;
        if (fromIndex < 0) {
            from = 0;
        } else if (fromIndex >= str.length()) {
            return CharSeq.of("");
        }

        if (toIndex < 0) {
            return CharSeq.of("");
        } else if (toIndex > str.length()) {
            to = str.length();
        }
        return new CharSeq(str.substring(from, to));
    }

    /**
     * Returns a CharSeq that contains a substring of this CharSeq's string.
     * The substring begins at the specified {@code beginIndex} and
     * extends to the end of this string.
     *
     * <p>
     * Examples:
     * <blockquote><pre>
     * CharSeq.of("unhappy").subSeq(-1) returns CharSeq.of("unhappy")
     * CharSeq.of("unhappy").subSeq(2) returns CharSeq.of("happy")
     * CharSeq.of("Harbison").subSeq(3) returns CharSeq.of("bison")
     * CharSeq.of("emptiness").subSeq(9) returns CharSeq.of("")
     * CharSeq.of("emptiness").subSeq(12) returns CharSeq.of("")
     * </pre></blockquote>
     * </p>
     *
     * @param   fromIndex   the beginning index, inclusive.
     * @return  CharSeq with the specified substring.
     */
    public CharSeq subSeq(int fromIndex) {
        return this.subSeq(fromIndex, str.length());
    }

    public CharSeq concat(CharSeq another){
        return new CharSeq(str + another.str);
    }

    public CharSeq concat(String another){
        return new CharSeq(str + another);
    }

    public CharSeq prepend(CharSeq another){
        return new CharSeq(another.str + str);
    }

    public CharSeq prepend(String another){
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
        return isEmpty() ? this : this.subSeq(0, 1).toUpperCase().concat(this.subSeq(1).toLowerCase());
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

    public static CharSeq of(char[] str) {
        return new CharSeq(new String(str));
    }

    public CharSeq reverse() {
        return CharSeq.of(new StringBuilder(str).reverse().toString());
    }

    public CharSeq swapcase() {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                chars[i] = Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                chars[i] = Character.toUpperCase(c);
            } else {
                chars[i] = c;
            }
        }
        return CharSeq.of(chars);
    }

    public boolean endsWith(CharSeq another) {
        return str.endsWith(another.str);
    }

    public boolean startsWith(CharSeq another) {
        return str.startsWith(another.str);
    }

    public Character charAt(int index) {
        return str.charAt(index);
    }

    public CharSeq trim() {
        return CharSeq.of(str.trim());
    }

    public Seq<CharSeq> scan(String regex) {
        Pattern pat = Pattern.compile(regex);
        Matcher m = pat.matcher(str);
        List<CharSeq> charSeqList = new ArrayList<>();
        while (m.find()) {
            charSeqList.add(CharSeq.of(m.group()));
        }
        return Seq.of(charSeqList);
    }

    public CharSeq eachChar(Consumer<Character> action) {
        Objects.requireNonNull(action);
        getCharacterSequence().forEach(action);
        return this;
    }

    public CharSeq eachCharWithIndex(BiConsumer<Character, Integer> action) {
        Objects.requireNonNull(action);
        getCharacterSequence().forEachWithIndex(action);
        return this;
    }

    public CharSeq eachByte(Consumer<Byte> action) {
        Objects.requireNonNull(action);
        getByteSequence().forEach(action);
        return this;
    }

    public CharSeq eachByteWithIndex(BiConsumer<Byte, Integer> action) {
        Objects.requireNonNull(action);
        getByteSequence().forEachWithIndex(action);
        return this;
    }

    public CharSeq eachLine(Consumer<CharSeq> action) {
        Objects.requireNonNull(action);
        Seq<CharSeq> lines = this.split("\n");
        lines.forEach(action);
        return this;
    }

    public Seq<CharSeq> getLines() {
        return this.split("\n");
    }

    public boolean matches(String regex) {
        return this.str.matches(regex);
    }

    public CharSeq replaceFirst(String regex, CharSeq replacement) {
        return this.replaceFirst(regex, replacement.str);
    }

    public CharSeq replaceFirst(String regex, String replacement) {
        return CharSeq.of(str.replaceFirst(regex, replacement));
    }

    public CharSeq replaceAll(String regex, CharSeq replacement) {
        return this.replaceAll(regex, replacement.str);
    }

    public CharSeq replaceAll(String regex, String replacement) {
        return CharSeq.of(str.replaceAll(regex, replacement));
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof CharSeq && str.equals(((CharSeq) another).str);
    }

    @Override
    public String toString() {
        return str;
    }

    public int compareTo(CharSeq another) {
        return str.compareTo(another.str);
    }

    public int compareToIgnoreCase(CharSeq another) {
        return str.compareToIgnoreCase(another.str);
    }

    public Seq<CharSeq> partition(String regex) {
        Matcher m = Pattern.compile(regex).matcher(str);
        if (m.find()) {
            return Seq.of(CharSeq.of(str.substring(0, m.start())),
                    CharSeq.of(m.group()),
                    CharSeq.of(str.substring(m.end())));
        } else {
            return Seq.of(CharSeq.of(str), CharSeq.of(""), CharSeq.of(""));
        }
    }

    public Seq<Character> getCharacterSequence() {
        char[] chars = str.toCharArray();
        Character[] characters = new Character[str.length()];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = chars[i];
        }
        return Seq.of(characters);
    }

    public Seq<Byte> getByteSequence() {
        byte[] rawBytes = str.getBytes();
        Byte[] bytes = new Byte[rawBytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = rawBytes[i];
        }
        return Seq.of(bytes);
    }
}
