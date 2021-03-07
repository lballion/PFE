package com.example.evernet;

import java.util.HashMap;

public class Handler {
    private HashMap<String,ReceivedFile > handler= new HashMap<String, ReceivedFile>();

    public Handler() { }

    public boolean nameInTheHandler(String name){ return handler.containsKey(name);}
    public String extractSrc(String stringPack){return stringPack.substring(0,9);}

    public String extractDst(String stringPack){return stringPack.substring(9,18);}

    public String extractPosition(String stringPack){return stringPack.substring(18,22);}

    public String extractNBpackets(String stringPack){return stringPack.substring(22,26);}

    public String extractTtl(String stringPack){return stringPack.substring(26,27);}

    public String extractNameOfIm(String stringPack){return stringPack.substring(27,33);}

    public String extractFragment(String stringPack){return stringPack.substring(33,stringPack.length());}

    public void insertFile(String name,ReceivedFile receivedFile){ handler.put(name,receivedFile); }

    public void insertPacket(ReceivedFile receivedFile,Packet packet){ receivedFile.insertPacket(packet); }

    public ReceivedFile getFileByKey(String key){ return handler.get(key);}

}
