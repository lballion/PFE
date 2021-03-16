package com.domain.evernet.controller;
import java.util.HashMap;

public class Handler {

    private HashMap<String, ReceivedFile> handler= new HashMap<String, ReceivedFile>();

    public Handler() { }

    public int getSize(){
        return handler.size();
    }

    public boolean contains(String name) { return handler.containsKey(name); }

    public void insertFile(String key,ReceivedFile receivedFile) { handler.put(key,receivedFile); }

    public ReceivedFile getFileByKey(String key) { return handler.get(key); }
}