package de.embl.rieslab.microfpga.devices;

import org.junit.Test;

import static org.junit.Assert.*;

public class LaserTriggerTest {

    @Test
    public void testIsBits(){
        assertTrue(LaserTrigger.isBits("10001110"));
        assertTrue(LaserTrigger.isBits("1111"));
        assertTrue(LaserTrigger.isBits("00000"));

        assertFalse(LaserTrigger.isBits("10201110"));
        assertFalse(LaserTrigger.isBits("1a11a1"));
        assertFalse(LaserTrigger.isBits("000A100"));
    }

    @Test
    public void testFormatSequence(){
        assertEquals(-1, LaserTrigger.formatSequence("10201110"));
        assertEquals(-1, LaserTrigger.formatSequence("152,5"));
        assertEquals(-1, LaserTrigger.formatSequence("abifke"));

        assertEquals(-1, LaserTrigger.formatSequence("111111111111111"));
        assertEquals(-1, LaserTrigger.formatSequence("00000000000000000"));

        assertEquals(0, LaserTrigger.formatSequence("0000000000000000"));
        assertEquals(65535, LaserTrigger.formatSequence("1111111111111111"));
        assertEquals(43690, LaserTrigger.formatSequence("1010101010101010"));
        assertEquals(21845, LaserTrigger.formatSequence("0101010101010101"));
        assertEquals(52428, LaserTrigger.formatSequence("1100110011001100"));
        assertEquals(34952, LaserTrigger.formatSequence("1000100010001000"));
    }

    @Test
    public void testGetSequence(){
        assertEquals(0, LaserTrigger.stringSequence(-1).length());
        assertEquals(0, LaserTrigger.stringSequence(65536).length());

        assertEquals("0000000000000000", LaserTrigger.stringSequence(0));
        assertEquals("1111111111111111", LaserTrigger.stringSequence(65535));
        assertEquals("1010101010101010", LaserTrigger.stringSequence(43690));
        assertEquals("0101010101010101", LaserTrigger.stringSequence(21845));
        assertEquals("1100110011001100", LaserTrigger.stringSequence(52428));
        assertEquals("1000100010001000", LaserTrigger.stringSequence(34952));
    }

    @Test
    public void testInverse(){
        int i = 41445;
        assertEquals(i, LaserTrigger.formatSequence(LaserTrigger.stringSequence(i)));
    }
}
