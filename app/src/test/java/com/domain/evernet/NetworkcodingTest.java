package com.domain.evernet;

import com.domain.evernet.controller.NetworkCoding;
import com.domain.evernet.model.FragmentEquationSystem;
import com.domain.evernet.model.NetworkCodingSimple;
import com.domain.evernet.model.Packet;

import org.junit.Assert;
import org.junit.Test;
import org.la4j.Vector;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

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
        packetDecoder.addEquation(firstPacket.getPosition(),Long.valueOf(firstPacket.getImageFragment()));
        Vector result = packetDecoder.gaussJordanElimination();

        /*
        System.out.println("First Value : " + firstValue);
        System.out.println("Second Value : " + secondValue);
        System.out.println("Gauss elimintaion result : " + result);*/

    }

    @Test
    public void simplePacketFusion(){

        String testName = "testImage";

        long firstValue = Math.round(Math.random() * Long.MAX_VALUE);
        int[] firstPacketPos = {1,0};
        Packet firstPacket = new Packet("0692121212","0692131313",firstPacketPos,2,10,testName,String.valueOf(firstValue));

        long secondValue = Math.round(Math.random() * Long.MAX_VALUE);
        int[] secondPacketPos = {2,0};
        Packet secondPacket = new Packet("0692121212","0692131313",secondPacketPos,2,10,testName,String.valueOf(secondValue));

        Packet mergedPacket = NetworkCodingSimple.mergeTwoPackets(firstPacket, secondPacket);
        Packet recoveredPacket = NetworkCodingSimple.decodeMergedPacketWithOnePacket( mergedPacket, firstPacket);

        System.out.println("Simple First Value : " + firstValue);
        System.out.println("simple Second Value : " + secondValue);
        System.out.println("simple merged Value : " + mergedPacket.getImageFragment());
        System.out.println("simple Recovered Value : " + recoveredPacket.getImageFragment());

        Assert.assertEquals(secondPacket.getImageFragment(), recoveredPacket.getImageFragment());
    }
}
