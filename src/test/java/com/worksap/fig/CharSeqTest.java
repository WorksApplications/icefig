package com.worksap.fig;

import com.worksap.fig.lang.CharSeq;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by liuyang on 7/27/15.
 */
public class CharSeqTest {

    @Test
    public void testCapitalize(){
        assertEquals("", CharSeq.of("").capitalize().toString());
        assertEquals("Abc", CharSeq.of("abc").capitalize().toString());
        assertEquals("Ww ffffff bbbbbb", CharSeq.of("ww ffFFfff bbBBbb").capitalize().toString());
    }
}
