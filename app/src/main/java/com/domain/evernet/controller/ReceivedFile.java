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
import java.util.HashMap;

public class ReceivedFile {
    private String name;
    private HashMap<Integer, String> packetLists=new HashMap<>();
    public ReceivedFile(String name){
        this.name=name;
    }
    private int nb_packets=0;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNbPackets(int nb_packets){ this.nb_packets=nb_packets; }


    public void insertPacket(int key,String fragment){
        if(packetLists.containsKey(key)==false){
          packetLists.put(key,fragment);
        }
    }
    public boolean allPacketReceived(){
        if(packetLists.size()==0){ return false; }

        return this.nb_packets==packetLists.size();
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
        int size=packetLists.size();
        for(int i=0;i<size;i++){
            fragment+=packetLists.get(i);
        }
        return fragment;
    }

    public int getSize(){ return packetLists.size(); }
}
