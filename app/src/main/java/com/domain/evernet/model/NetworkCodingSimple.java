package com.domain.evernet.model;

public class NetworkCodingSimple {

    static public Packet mergeTwoPackets(Packet firstPacket, Packet secondPacket){

        String firstPacketFragment = firstPacket.getImageFragment();
        String secondPacketFragemnt =  secondPacket.getImageFragment();

        int[] firstPacketPos = firstPacket.getPosition();
        int[] secondPacketPos = secondPacket.getPosition();

        int[] mergePacketPos = {firstPacketPos[0],secondPacketPos[0]};

        byte[] firstPacketFragmentBytes = firstPacketFragment.getBytes();
        byte[] secondPacketFragmentBytes = secondPacketFragemnt.getBytes();

        byte[] mergedPacketsFragmentBytes = new byte[firstPacketFragmentBytes.length];

        for (int i=0; i < mergedPacketsFragmentBytes.length; i++){
            mergedPacketsFragmentBytes[i] = (byte) (firstPacketFragmentBytes[i] ^ secondPacketFragmentBytes[i]);
        }

        String mergedPacketsFragment = new String(mergedPacketsFragmentBytes);



        Packet output = new Packet(firstPacket.getSource(),
                firstPacket.getDestination(),
                mergePacketPos,
                firstPacket.getNbPackets(),
                firstPacket.getTtl(),
                firstPacket.getTimeStamp(),
                mergedPacketsFragment);
        return output;
    }

    static public Packet decodeMergedPacketWithOnePacket(Packet mergedPacket, Packet complementaryPacket){

        String mergedPacketFragment = mergedPacket.getImageFragment();
        String complementPacketFragment = complementaryPacket.getImageFragment();

        byte[] mergedPacketFragmentBytes = mergedPacketFragment.getBytes();
        byte[] complementPacketFragmentBytes = complementPacketFragment.getBytes();

        byte[] decodedPacketFragmentBytes = new byte[mergedPacketFragmentBytes.length];

        for (int i=0; i < mergedPacketFragmentBytes.length; i++){
            decodedPacketFragmentBytes[i] = (byte) (mergedPacketFragmentBytes[i] ^ complementPacketFragmentBytes[i]);
        }

        int[] mergedPacketPos = mergedPacket.getPosition();
        int[] complementPacketPos = complementaryPacket.getPosition();



        int[] decodedPacketPos = {complementPosInMerged(mergedPacketPos,complementPacketPos[0])};
        String decodedPacketFragment = new String(decodedPacketFragmentBytes);

        Packet decodedPacket = new Packet(complementaryPacket.getSource(),
                complementaryPacket.getDestination(),
                decodedPacketPos,
                complementaryPacket.getNbPackets(),
                complementaryPacket.getTtl(),
                complementaryPacket.getTimeStamp(),
                decodedPacketFragment);

        return decodedPacket;
    }

    private static int complementPosInMerged(int[] mergedPosTab, int complementNb){
        int pos = -1;
        for (int tabVal : mergedPosTab){
            pos ++;
            if(tabVal == complementNb){
                return pos;
            }
        }
        return -1;
    }
}
