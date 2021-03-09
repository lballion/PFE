package com.domain.evernet.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.domain.evernet.controller.Packet;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        if(this.packetInTheList(packet)==false){
            int pos=packet.getPosition();
            if(packetLists.size()==0){
                packetLists.add(packet);
            }else{
                int i=0;
                while (i<packetLists.size()&& packetLists.get(i).getPosition()<pos  ){
                    i+=1;

                }
                packetLists.add(i, packet);
            }
        }
    }
    public boolean packetInTheList(Packet packet){
        int position=packet.getPosition();
        boolean response=false;
        for (Packet p:packetLists
        ) {
            if (p.getPosition()==position){
                response= true;
            }

        }
        return response;
    }

    public boolean allPacketReceived(){
        if(packetLists.size()==0){ return false; }

        return packetLists.get(0).getNbPackets()==packetLists.size();
    }
    public byte [] stringToArrayBites(){
        String imageString=toImageString();
        byte [] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        return imageBytes;
    }
    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(is);
    }
    public String toImageString(){
        String fragment="";
        for (Packet p:packetLists
        ) {
            fragment+=p.getImageFragment();
        }
        return fragment;
    }

    public int getSize(){ return packetLists.size(); }
}
