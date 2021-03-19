package com.domain.evernet;

import com.domain.evernet.model.Client;
import com.domain.evernet.model.Packet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests of the class PacketS
 */
public class PacketTest {

    @Test
    public void getPacketTest() {
        Packet p = new Packet("0000000000", "0", 0, 0, 0, "000000", "fragment");

        String s =  p.getPacket();
        System.out.println(s);
        assertEquals(s,"0000000000*********0000000000000000fragment");
    }

    @Test
    public void stuffStringTest() {
        Packet p = new Packet();
        String s = p.stuffString("AAAAAAA", '0', 15);
        assertEquals(s, "00000000AAAAAAA");
    }

    @Test
    public void extractSrcTest() {
        Packet p = new Packet();
        String s = p.extractSrc("06012345670601010101002600185000008fragment");
        assertEquals(s, "0601234567");
    }

    @Test
    public void extractDstTest() {
        Packet p = new Packet();
        String s = p.extractDst("06012345670601010101002600185000008fragment");
        assertEquals(s, "0601010101");
    }

    @Test
    public void extractPositionTest() {
        Packet p = new Packet();
        String s = p.extractPosition("06012345670601010101002600185000008fragment");
        assertEquals(s, "0026");
    }

    @Test
    public void extractNbPacketsTest() {
        Packet p = new Packet();
        String s = p.extractNBpackets("06012345670601010101002600185000008fragment");
        assertEquals(s, "0018");
    }

    @Test
    public void extractNameOfImTest() {
        Packet p = new Packet();
        String s = p.extractNameOfIm("06012345670601010101002600185000008fragment");

        assertEquals(s, "500000");
    }

    @Test
    public void extractTtlTest() {
        Packet p = new Packet();
        String s = p.extractTtl("06012345670601010101002600185000008fragment");
        assertEquals(s, "8");
    }

    @Test
    public void extractFragment() {
        Packet p = new Packet();
        String s = p.extractFragment("06012345670601010101002600185000008fragment");
        assertEquals(s, "fragment");
    }
}