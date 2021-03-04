package com.example.evernet;

public class Packet {

    private int idSource;
    private int idDestination;
    private int position;
    private int nbPackets;
    private String imageFragment;
    private int ttl;

    public Packet(int idSrc, int idDst, int pos, int nbPackets, String imgFrag, int ttl){
        if( idSrc < 0 || idDst < 0 || pos < 0 || nbPackets < 0 || ttl < 0){
            throw new IllegalArgumentException("Args must be positive integers.");
        }
        idSource = idSrc;
        idDestination = idDst;
        position = pos;
        this.nbPackets = nbPackets;
        this.imageFragment = imgFrag;
        this.ttl = ttl;
    }

    public String Packet(String nameOfIm){
        String positon_String=""+position;
        int size=positon_String.length();
        for(int i=0;i<4-size;i++){
            positon_String="0"+positon_String;
        }

        String packet = ""
                + idSource
                + idDestination
                + positon_String
                + nbPackets
                + ttl
                +nameOfIm
                + imageFragment
                ;
        return packet;
    }

    public int getIdSource() {
        return new Integer(idSource);
    }

    public int getIdDestination() {
        return new Integer(idDestination);
    }

    public int getPosition() {
        return new Integer(position);
    }

    public int getNbPackets() {
        return new Integer(nbPackets);
    }

    public int getImageFragment() {
        return new Integer(imageFragment);
    }

    public int getTtl() {
        return new Integer(ttl);
    }

}
