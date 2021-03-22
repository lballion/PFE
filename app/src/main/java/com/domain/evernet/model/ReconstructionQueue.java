package com.domain.evernet.model;

import java.util.HashMap;
import java.util.Set;

public class ReconstructionQueue {

    private int numberOfPacket;
    private String id;

    private HashMap<Integer, Packet> clearPacket;
    private HashMap<int[], Packet> mergedPacket;

    public  ReconstructionQueue(Packet anImagePacket){
        this.numberOfPacket = anImagePacket.getNbPackets();
        this.id = anImagePacket.getTimeStamp();

        clearPacket = new HashMap<>();
        mergedPacket = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public int getNumberOfPacket() {
        return numberOfPacket;
    }

    public void addAClearPacket(Packet ClearPacketToAdd){
        if(!clearPacket.containsKey(ClearPacketToAdd.getPosition()[0]))
            clearPacket.put(ClearPacketToAdd.getPosition()[0], ClearPacketToAdd);
    }

    public void addMergedPacket(Packet mergedPacketToAdd){
        if(!mergedPacket.containsKey(mergedPacketToAdd.getPosition()))
            mergedPacket.put(mergedPacketToAdd.getPosition(), mergedPacketToAdd);
    }

    public Packet getClearPacket(int key){
        return clearPacket.get(key);
    }

    public Packet getMergedPacket(int[] key){
        return mergedPacket.get(key);
    }

    public int sizeClearPacket(){
        return clearPacket.size();
    }

    public boolean isInMerged(Packet packet){
        return mergedPacket.containsKey(packet.getPosition());
    }

    public boolean isInClear(Packet packet){
        return clearPacket.containsKey(packet.getPosition()[0]);
    }


    public int[] getKeyOfWhichContainPacket(Packet packet){
        int[] keyValue = {0,0};
        Set<int[]> keySet = mergedPacket.keySet();
        for(int[] key : keySet){
            for (int tabVal : key){
                if(tabVal == packet.getPosition()[0]){
                    keyValue = key;
                }
            }
        }
        return keyValue;
    }

    public boolean  isfull(){
        return clearPacket.size() == numberOfPacket;
    }
}
