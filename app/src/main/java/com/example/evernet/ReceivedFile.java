package com.example.evernet;

import java.util.ArrayList;

public class ReceivedFile {
    private String name;
    private ArrayList<Packet>packetLists=new ArrayList<>();
    public ReceivedFile(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Packet> getPacketLists() {
        return packetLists;
    }

    public void setPacketLists(ArrayList<Packet> packetLists) {
        packetLists = packetLists;
    }
    public void insertPacket(Packet packet){
        int pos=packet.getPosition();
        if(packetLists.size()==0){
            packetLists.add(packet);
        }else{
            int i=0;
            while (i<packetLists.size() && packetLists.get(i).getPosition()<pos  ){
                i+=1;

            }
            packetLists.add(i+1, packet);
        }

    }
    public boolean PacketInTheList(Packet packet){
        int position=packet.getPosition();
        for (Packet p:packetLists
             ) {
            if (p.getPosition()==position){
                return true;
            }

        }
        return false;
    }

    public boolean imageIsFull(){
        if(packetLists.size()==0){ return false; }

        return packetLists.get(0).getNbPackets()==packetLists.size();
    }
    public String toFragement(){
        String fragment="";
        for (Packet p:packetLists
             ) {
            fragment+=p.getImageFragment();
        }
        return fragment;
    }
}
