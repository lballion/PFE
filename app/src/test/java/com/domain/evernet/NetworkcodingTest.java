package com.domain.evernet;

import com.domain.evernet.controller.NetworkCoding;
import com.domain.evernet.model.FragmentEquationSystem;
import com.domain.evernet.model.Packet;

import org.junit.Test;
import org.la4j.Vector;

public class NetworkcodingTest {

    @Test
    public void packetFusion(){
        String testName = "testImage";

        long firstValue = Math.round(Math.random() * Long.MAX_VALUE);
        int[] firstPacketPos = {1,0};
        Packet firstPacket = new Packet("0692121212","0692131313",firstPacketPos,2,10,testName,String.valueOf(firstValue));

        long secondValue = Math.round(Math.random() * Long.MAX_VALUE);
        int[] secondPacketPos = {2,0};
        Packet secondPacket = new Packet("0692121212","0692131313",secondPacketPos,2,10,testName,String.valueOf(secondValue));

        NetworkCoding networkCoding = new NetworkCoding();
        Packet encodedPacket = networkCoding.encodeTwoPacket(firstPacket,secondPacket);

        FragmentEquationSystem packetDecoder = new FragmentEquationSystem(testName,firstPacket.getNbPackets());
        packetDecoder.addEquation(encodedPacket.getPosition(),Long.valueOf(encodedPacket.getImageFragment()));
        Vector result = Vector.zero(2);
        packetDecoder.gaussJordanElimination(result);
    }
}
