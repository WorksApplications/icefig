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

    /**
     * Append string of the given CharSeq to this CharSeq
     *
     * @param   another   the given CharSeq
     * @return  appended result
     */
    public CharSeq concat(CharSeq another){
        return new CharSeq(str + another.str);
    }

    /**
     * Append the given string to this CharSeq
     * @param   another   the given string
     * @return  appended result
     */
    public CharSeq concat(String another){
        return new CharSeq(str + another);
    }

    /**
     * Prepend string of the given CharSeq to this CharSeq
     * @param   another   the given string
     * @return  prepended result
     */
    public CharSeq prepend(CharSeq another){
        return new CharSeq(another.str + str);
    }

    /**
     * Prepend the given string to this CharSeq
     * @param   another   the given string
     * @return  prepended result
     */
    public CharSeq prepend(String another){
        return new CharSeq(another + str);
    }

    /**
     * Returns the length of string of this CharSeq.
     * The length is equal to the number of Unicode
     * code units in the string.
     *
     * @return  the length
     */
    public int length(){
        return str.length();
    }

    /**
     * Returns {@code true} if, and only if, {@link #length()} is {@code 0}.
     *
     * @return {@code true} if {@link #length()} is {@code 0}, otherwise
     * {@code false}
     */
    public boolean isEmpty(){
        return str.isEmpty();
    }

    /**
     * Returns a copy of CharSeq with the string's first character
     * converted to uppercase and the remainder to lowercase.
     *
     * @return  CharSeq with capitalized string
     */
    public CharSeq capitalize() {
        return isEmpty() ? this : this.subSeq(0, 1).toUpperCase().concat(this.subSeq(1).toLowerCase());
    }

    /**
     * Returns a copy of CharSeq with the string's characters all
     * converted to uppercase.
     *
     * @return
     */
    public CharSeq toUpperCase() {
        return new CharSeq(str.toUpperCase());
    }

    /**
     * Returns a copy of this CharSeq with characters all
     * converted to lowercase.
     *
     * @return
     */
    public CharSeq toLowerCase() {
        return new CharSeq(str.toLowerCase());
    }

    /**
     * Splits this CharSeq around matches of the given regular expression.
     *
     * @param regex Regular expression
     * @return
     */
    public Seq<CharSeq> split(String regex) {
        return Seq.of(str.split(regex)).map(CharSeq::new);
    }

    /**
     * Construct a new CharSeq with the given string
     *
     * @param str the given string
     * @return
     */
    public static CharSeq of(String str) {
        return new CharSeq(str);
    }

    /**
     * Construct a new CharSeq with the given char array
     *
     * @param charArr the given char array
     * @return
     */
    public static CharSeq of(char[] charArr) {
        return new CharSeq(new String(charArr));
    }

    /**
     * Return a new CharSeq with the characters from
     * this CharSeq in reverse order.
     *
     * <p>
     * Examples:
     * <blockquote><pre>
     * CharSeq.of("stressed").reverse() returns CharSeq.of("desserts")
     * </pre></blockquote>
     * </p>
     *
     * @return  A new Seq
     */
    public CharSeq reverse() {
        return CharSeq.of(new StringBuilder(str).reverse().toString());
    }

    /**
     * Return a new CharSeq with all characters' cases toggled.
     *
     * <p>
     * Examples:
     * <blockquote><pre>
     * CharSeq.of("sTrEsSed").swapcase() returns CharSeq.of("StReSsED")
     * </pre></blockquote>
     * </p>
     *
     * @return  A new CharSeq
     */
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

    /**
     * Tests whether this CharSeq ends with the specified suffix
     *
     * @param   suffix   suffix
     * @return  A boolean
     */
    public boolean endsWith(CharSeq suffix) {
        return this.endsWith(suffix.str);
    }

    /**
     * Tests whether this CharSeq ends with the specified suffix
     *
     * @param   suffix   suffix
     * @return  A boolean
     */
    public boolean endsWith(String suffix) {
        return str.endsWith(suffix);
    }

    /**
     * Tests whether this CharSeq starts with the specified prefix
     *
     * @param   prefix   prefix
     * @return  A boolean
     */
    public boolean startsWith(CharSeq prefix) {
        return str.startsWith(prefix.str);
    }

    /**
     * Return the Character at the index i of this CharSeq
     * @param   i the index
     * @return  The specified Character
     */
    public Character charAt(int i) {
        return str.charAt(i);
    }

    /**
     * Returns a CharSeq whose value is this CharSeq, with any leading and trailing
     * whitespace removed.
     *
     * @return A new CharSeq with leading and trailing whitespace removed
     */
    public CharSeq trim() {
        return CharSeq.of(str.trim());
    }

    /**
     * Scan through this CharSeq iteratively, generate a Seq of CharSeq
     * with all the matching subStrings.
     *
     * @param   regex The regular expression
     * @return  A Seq of CharSeq
     */
    public Seq<CharSeq> scan(String regex) {
        Pattern pat = Pattern.compile(regex);
        Matcher m = pat.matcher(str);
        List<CharSeq> charSeqList = new ArrayList<>();
        while (m.find()) {
            charSeqList.add(CharSeq.of(m.group()));
        }
        return Seq.of(charSeqList);
    }

    /**
     * Performs the given action for each character of the CharSeq.
     *
     * @param action Consumer with single parameter of Character
     * @return  Self
     */
    public CharSeq eachChar(Consumer<Character> action) {
        Objects.requireNonNull(action);
        getCharacterSequence().forEach(action);
        return this;
    }

    /**
     * Performs the given action for each character of the CharSeq,
     * with additional parameter "index" as the second parameter.
     *
     * @param action BiConsumer with parameters of Character and the index
     * @return  Self
     */
    public CharSeq eachCharWithIndex(BiConsumer<Character, Integer> action) {
        Objects.requireNonNull(action);
        getCharacterSequence().forEachWithIndex(action);
        return this;
    }

    /**
     * Performs the given action for each byte of the CharSeq.
     *
     * @param action Consumer with single parameter of Byte
     * @return  Self
     */
    public CharSeq eachByte(Consumer<Byte> action) {
        Objects.requireNonNull(action);
        getByteSequence().forEach(action);
        return this;
    }

    /**
     * Performs the given action for each byte of the CharSeq,
     * with additional parameter "index" as the second parameter.
     *
     * @param action BiConsumer with parameters of Byte and the index
     * @return  Self
     */
    public CharSeq eachByteWithIndex(BiConsumer<Byte, Integer> action) {
        Objects.requireNonNull(action);
        getByteSequence().forEachWithIndex(action);
        return this;
    }

    /**
     * Performs the given action for each line of the CharSeq.
     *
     * @param action Consumer with single parameter of CharSeq
     * @return  Self
     */
    public CharSeq eachLine(Consumer<CharSeq> action) {
        Objects.requireNonNull(action);
        Seq<CharSeq> lines = this.split("\n");
        lines.forEach(action);
        return this;
    }

    /**
     * Performs the given action for each line of the CharSeq,
     * with additional parameter "index" as the second parameter.
     *
     * @param action BiConsumer with parameters of CharSeq and the index
     * @return  Self
     */
    public CharSeq eachLineWithIndex(BiConsumer<CharSeq, Integer> action) {
        Objects.requireNonNull(action);
        Seq<CharSeq> lines = this.split("\n");
        lines.forEachWithIndex(action);
        return this;
    }

    /**
     * Split the CharSeq by the newline character and return the
     * result as a Seq of CharSeq.
     *
     * @return A Seq of CharSeq
     */
    public Seq<CharSeq> getLines() {
        return this.split("\n");
    }

    /**
     * Tells whether or not this CharSeq matches the given regular expression.
     *
     * @return A boolean
     */
    public boolean matches(String regex) {
        return this.str.matches(regex);
    }


    /**
     * Return a new CharSeq by replacing the first substring of this CharSeq
     * that matches the given regular expression with the given CharSeq replacement.
     *
     * @param regex         The regular expression
     * @param replacement   The replacement CharSeq
     * @return  A new CharSeq
     */
    public CharSeq replaceFirst(String regex, CharSeq replacement) {
        return this.replaceFirst(regex, replacement.str);
    }

    /**
     * Return a new CharSeq by replacing the first substring of this CharSeq
     * that matches the given regular expression with the given String replacement.
     *
     * @param regex         The regular expression
     * @param replacement   The replacement String
     * @return  A new CharSeq
     */
    public CharSeq replaceFirst(String regex, String replacement) {
        return CharSeq.of(str.replaceFirst(regex, replacement));
    }

    /**
     * Return a new CharSeq by replacing each substring of this
     * CharSeq that matches the given regular expression with
     * the given CharSeq replacement.
     *
     * @param regex         The regular expression
     * @param replacement   The replacement CharSeq
     * @return  A new CharSeq
     */
    public CharSeq replaceAll(String regex, CharSeq replacement) {
        return this.replaceAll(regex, replacement.str);
    }

    /**
     * Return a new CharSeq by replacing each substring of this
     * CharSeq that matches the given regular expression with
     * the given String replacement.
     *
     * @param regex         The regular expression
     * @param replacement   The replacement String
     * @return  A new CharSeq
     */
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

    /**
     * Compares two CharSeqs lexicographically.

     * @return  the value {@code 0} if the argument CharSeq is equal to
     *          this CharSeq; a value less than {@code 0} if this CharSeq
     *          is lexicographically less than the CharSeq argument; and a
     *          value greater than {@code 0} if this CharSeq is
     *          lexicographically greater than the CharSeq argument.
     */
    public int compareTo(CharSeq another) {
        return str.compareTo(another.str);
    }

    /**
     * Compares two CharSeqs lexicographically ignore case differences.
     *
     * @return  a negative integer, zero, or a positive integer as the
     *          specified String is greater than, equal to, or less
     *          than this String, ignoring case considerations.
     */
    public int compareToIgnoreCase(CharSeq another) {
        return str.compareToIgnoreCase(another.str);
    }

    /**
     * Searches pattern (regex) in the CharSeq and returns
     * a Seq of CharSeq consists of the part before it,
     * the match, and the part after it.
     *
     * @param regex Regular Expression
     * @return A Seq of CharSeq
     */
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

    /**
     * Converts this CharSeq to a new Character Seq.
     *
     * @return  A Seq of Character
     */
    public Seq<Character> getCharacterSequence() {
        char[] chars = str.toCharArray();
        Character[] characters = new Character[str.length()];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = chars[i];
        }
        return Seq.of(characters);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the
     * platform's default charset, storing the result into a new Byte Seq.
     *
     * @return  A Seq of Byte
     */
    public Seq<Byte> getByteSequence() {
        byte[] rawBytes = str.getBytes();
        Byte[] bytes = new Byte[rawBytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = rawBytes[i];
        }
        return Seq.of(bytes);
    }
}
