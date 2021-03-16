package com.domain.evernet.controller;

import com.domain.evernet.model.EncodedPayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;


public class ResilientNetworkCoding implements NetworkEncoderDecoder{

    private final int NOT_FOUND = -1;
    private final int FIRST = 0;
    private final int SECOND = 1;

    /**
     * @param firstPacket one packet that contain the fragment needed for the decoding of
     *                    the second packet or that is the encoded packet.
     * @param secondPacket one packet that contain the fragment needed for the decoding of
 *      *                  the first packet or that is the encoded packet.
     * @return An array of byte arrays. The first array is the the first fragment find in
     *         the firstPacketFormula.
     */
    @Override
    public byte[][] decodePacket(EncodedPayload firstPacket, EncodedPayload secondPacket) {

        if (firstPacket == secondPacket)
            throw new IllegalArgumentException("Given Packets can't be the same.");


        int[] firstPacketFormula = firstPacket.getFormula();
        int[] secondPacketFormula = secondPacket.getFormula();

        int encodedOne = 0;
        int clearOne = 0;

        if(firstPacketFormula.length > 2
                || secondPacketFormula.length > 2
                || firstPacketFormula.length == secondPacketFormula.length)
            throw new IllegalArgumentException("The size of one or two of the formula is invalid.");



        if(firstPacketFormula.length > secondPacketFormula.length){
            encodedOne = FIRST;
            clearOne = SECOND;

        }

        else {
            encodedOne = SECOND;
            clearOne = FIRST;
        }

        if(encodedOne == FIRST) {
            if (Arrays.binarySearch(firstPacketFormula, secondPacketFormula[0]) == NOT_FOUND)
                throw new IllegalArgumentException("The first packet formula doesn't contain " +
                        "the second packet fragment");
        }
        else if(encodedOne == SECOND){
            if( Arrays.binarySearch(secondPacketFormula, firstPacketFormula[0]) == NOT_FOUND)
                throw new IllegalArgumentException("The second packet formula doesn't contain " +
                        "the first packet fragment");
        }

        byte[] firstPacketPayload = firstPacket.getPayload();
        byte[] secondPacketPayload = secondPacket.getPayload();

        if(firstPacketPayload.length<1
                || secondPacketPayload.length<1
                || firstPacketPayload.length != secondPacketPayload.length)
            throw  new IllegalArgumentException("The size of packet's payloads doesn't match." +
                    "Please use packet's payloads of the same size");

        byte[] decodedFragment = new byte[firstPacketPayload.length];

        byte[][] decodedResult;
        byte decodedByte;

        if(encodedOne == FIRST){
            for (int i=0; i< firstPacketPayload.length; i++){
                decodedByte = (byte) (firstPacketPayload[i] ^ secondPacketPayload[i]);
                decodedFragment[i] = decodedByte;
            }

            if(firstPacketFormula[0] == secondPacketFormula[0]){
                decodedResult = new byte[][] {secondPacketPayload,decodedFragment};
            }
            else
                decodedResult = new byte[][] {decodedFragment,secondPacketPayload};
        }

        else{
            for (int i=0; i< secondPacketPayload.length; i++){
                decodedByte = (byte) (secondPacketPayload[i] ^ firstPacketPayload[i]);
                decodedFragment[i] = decodedByte;
            }

            if(secondPacketFormula[0] == firstPacketFormula[0])
                decodedResult = new byte[][] {firstPacketPayload,decodedFragment};
            else
                decodedResult = new byte[][] {decodedFragment,firstPacketPayload};
        }

        return decodedResult;
    }

    @Override
    public EncodedPayload encodeTwoPacket(byte[] fisrtPayload, byte[] secondPayload) {
        return null;
    }
}
