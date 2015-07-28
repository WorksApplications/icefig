package com.worksap.fig;

import com.worksap.fig.lang.CharSeq;
import com.worksap.fig.lang.Seq;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lijunxiao on 7/27/15.
 */
public class CharSeqTest {
    @Test
    public void testSubSeq() {
        CharSeq seq = CharSeq.of("Father Charles gets down and ends battle.");
        assertEquals(CharSeq.of("Father"), seq.subSeq(0, 6));
        assertEquals(CharSeq.of("Charles "), seq.subSeq(7, 15));
        assertEquals(CharSeq.of("Charles gets down and ends battle."), seq.subSeq(7, 41));
        assertEquals(CharSeq.of("."), seq.subSeq(40));
        assertEquals(CharSeq.of(""), seq.subSeq(41));
        assertEquals(CharSeq.of("Father Charles gets down and ends battle."), seq.subSeq(0));
    }

    @Test
    public void testLength() {
        CharSeq seq = CharSeq.of("The quick brown fox jumps over a lazy dog");
        assertEquals(41, seq.length());

        CharSeq emptySeq = CharSeq.of("");
        assertEquals(0, emptySeq.length());
    }

    @Test
    public void testCapitalize() {
        CharSeq seq = CharSeq.of("the quick brown fox jumps over a lazy dog");
        assertEquals(CharSeq.of("The quick brown fox jumps over a lazy dog"), seq.capitalize());

        assertEquals(CharSeq.of(""), CharSeq.of("").capitalize());
    }

    @Test
    public void testToLowerCase() {
        assertEquals(CharSeq.of("the quick brown fox jumps over a lazy dog"),
                CharSeq.of("THE QUICK BROWN FOX JUMPS OVER A LAZY DOG").toLowerCase());

        assertEquals(CharSeq.of(""), CharSeq.of("").toLowerCase());
    }

    @Test
    public void testConcatAndPrepend() {
        CharSeq seq = CharSeq.of("I'm a rich man");
        assertEquals(CharSeq.of("I'm a rich man, am I?"), seq.concat(CharSeq.of(", am I?")));
        assertEquals(CharSeq.of("I'm a rich man, am I? Of course!"), seq.concat(", am I? Of course!"));
        assertEquals(CharSeq.of("Do you think I'm a rich man?"), seq.concat("?").prepend(CharSeq.of("Do you think ")));
        assertEquals(CharSeq.of("Don't you think I'm a rich man?"), seq.concat("?").prepend("Don't you think "));
    }

    @Test
    public void testReverse() {
        assertEquals(CharSeq.of(""), CharSeq.of("").reverse());
        assertEquals(CharSeq.of("was i tac a ro rac a ti saw"), CharSeq.of("was it a car or a cat i saw").reverse());
    }

    @Test
    public void testSwapcase() {
        assertEquals(CharSeq.of(""), CharSeq.of("").swapcase());
        assertEquals(CharSeq.of("tHe QuIck bRown fOx jumpS ovEr a laZy DoG"),
                CharSeq.of("ThE qUiCK BrOWN FoX JUMPs OVeR A LAzY dOg").swapcase());
    }

    @Test
    public void testSplit() {
        assertArrayEquals(new CharSeq[]{CharSeq.of("021"), CharSeq.of("50242132")},
                CharSeq.of("021 50242132").split("[- ]").toArray());
        assertArrayEquals(new CharSeq[]{CharSeq.of("021"), CharSeq.of("50242132")},
                CharSeq.of("021-50242132").split("[- ]").toArray());
        assertArrayEquals(new CharSeq[]{CharSeq.of("021_50242132")},
                CharSeq.of("021_50242132").split("[- ]").toArray());
    }

    @Test
    public void testStartsWithAndEndsWith() {
        assertTrue(CharSeq.of("021 50242132").startsWith(CharSeq.of("021")));
        assertFalse(CharSeq.of("021 50242132").startsWith(CharSeq.of("021 502421321")));
        assertFalse(CharSeq.of("021 50242132").endsWith(CharSeq.of("232")));
    }

    @Test
    public void testTrim() {
        assertEquals(CharSeq.of("With prefix blanking"), CharSeq.of("   With prefix blanking").trim());
        assertEquals(CharSeq.of("With no blanking"), CharSeq.of("With no blanking").trim());
        assertEquals(CharSeq.of("With suffix blanking"), CharSeq.of("With suffix blanking   ").trim());
        assertEquals(CharSeq.of("With both side blanking"), CharSeq.of("   With both side blanking      ").trim());
    }

    @Test
    public void testScan() {
        CharSeq seq = CharSeq.of("ATE@ShangHai Works Applications");
        assertArrayEquals(new CharSeq[]{CharSeq.of("ATE"), CharSeq.of("ShangHai"), CharSeq.of("Works"), CharSeq.of("Applications")}, seq.scan("\\w+").toArray());
        assertArrayEquals(new CharSeq[]{CharSeq.of("ATE@ShangHai"), CharSeq.of("Works"), CharSeq.of("Applications")}, seq.scan("[\\w@]+").toArray());
    }

    @Test
    public void testPartition() {
        CharSeq seq = CharSeq.of("var sample = 'Good good study, day day up.'");
        assertArrayEquals(new CharSeq[]{CharSeq.of("var sample "), CharSeq.of("="), CharSeq.of(" 'Good good study, day day up.'")},
                seq.partition("=").toArray());

        CharSeq seq1 = CharSeq.of("_word");
        assertArrayEquals(new CharSeq[]{CharSeq.of(""), CharSeq.of("_word"), CharSeq.of("")},
                seq1.partition("\\w+").toArray());
        assertArrayEquals(new CharSeq[]{CharSeq.of("_word"), CharSeq.of(""), CharSeq.of("")},
                seq1.partition("\\W+").toArray());
    }

    @Test
    public void testGetLines() {
        CharSeq seq = CharSeq.of("凤凰台上凤凰游，凤去台空江自流。\n" +
                "吴宫花草埋幽径，晋代衣冠成古丘。\n" +
                "三山半落青天外，二水中分白鹭洲。\n" +
                "总为浮云能蔽日，长安不见使人愁。");
        assertArrayEquals(new CharSeq[]{
                CharSeq.of("凤凰台上凤凰游，凤去台空江自流。"),
                CharSeq.of("吴宫花草埋幽径，晋代衣冠成古丘。"),
                CharSeq.of("三山半落青天外，二水中分白鹭洲。"),
                CharSeq.of("总为浮云能蔽日，长安不见使人愁。")
        }, seq.lines().toArray());
    }

    @Test
    public void testCompare() {
        assertTrue(CharSeq.of("Good Boy").compareTo(CharSeq.of("good boy")) < 0);
        assertTrue(CharSeq.of("Good Boy").compareToIgnoreCase(CharSeq.of("good boy")) == 0);
    }

    @Test
    public void testReplace() {
        CharSeq seq = CharSeq.of("PhoneNumbers: 021-55831183, 010-55131123, 020-11239901");
        assertEquals(CharSeq.of("PhoneNumbers: 025-55831183, 010-55131123, 020-11239901"),
                seq.replaceFirst("\\d{3}-", CharSeq.of("025-")));
        assertEquals(CharSeq.of("PhoneNumbers: 025-55831183, 025-55131123, 025-11239901"),
                seq.replaceAll("\\d{3}-", CharSeq.of("025-")));
    }

    @Test
    public void testMatches() {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*" +
                "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        assertTrue(CharSeq.of("ljxnegod@hotmail.com").matches(regex));
        assertFalse(CharSeq.of("ljxnegod@1").matches(regex));
        assertFalse(CharSeq.of("ljxnegod@1  ").matches(regex));
        assertFalse(CharSeq.of("ljxnegod@1..com.cn").matches(regex));
    }

    @Test
    public void testEachLines() {
        CharSeq seq = CharSeq.of("凤凰台上凤凰游，凤去台空江自流。\n" +
                "吴宫花草埋幽径，晋代衣冠成古丘。\n" +
                "三山半落青天外，二水中分白鹭洲。\n" +
                "总为浮云能蔽日，长安不见使人愁。");
        Seq<CharSeq> result = Seq.of();
        seq.forEachLine(result::add);
        assertEquals(seq.lines(), result);
    }

    @Test
    public void testEachChar() {
        CharSeq seq = CharSeq.of("Albert");
        Character[] increased = new Character[]{'B', 'm', 'c', 'f', 's', 'u'};
        List<Character> chars = new ArrayList<>();
        seq.forEachChar(ch -> chars.add((char) (ch + 1)));

        assertArrayEquals(increased, chars.toArray());
        seq.forEachCharWithIndex((ch, index) -> assertEquals(new Character((char) (ch + 1)), increased[index]));
    }

    @Test
    public void testEachByte() {
        String einstein = "Einstein";
        CharSeq seq = CharSeq.of(einstein);
        Seq<Byte> bytes = Seq.of();
        seq.forEachByte(bytes::add);
        seq.forEachByteWithIndex((b, i) -> assertEquals(new Character((char) (b.byteValue())), seq.charAt(i)));
    }
}
