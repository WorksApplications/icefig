package com.worksap.fig.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public CharSeq subSeq(int fromIndex, int toIndex) {
        return new CharSeq(str.substring(fromIndex, toIndex));
    }
    public CharSeq subSeq(int fromIndex) {
        return new CharSeq(str.substring(fromIndex));
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
        char[] chars = str.toCharArray();
        Character[] characters = new Character[str.length()];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = chars[i];
        }
        Seq<Character> characterSeq = Seq.of(characters);
        characterSeq.forEach(action);
        return this;
    }

    public CharSeq eachByte(Consumer<Byte> action) {
        Objects.requireNonNull(action);
        byte[] rawBytes = str.getBytes();
        Byte[] bytes = new Byte[rawBytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = rawBytes[i];
        }
        Seq<Byte> byteSeq = Seq.of(bytes);
        byteSeq.forEach(action);
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
}
