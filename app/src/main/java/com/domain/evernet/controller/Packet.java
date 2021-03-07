package com.domain.evernet.controller;

public class Packet {

    private int idSource;
    private int idDestination;
    private int position;
    private int nbPackets;
    private int ttl;
    private String timeStamp;
    private String imageFragment;

    public Packet(int idSrc, int idDst, int pos, int nbPackets, int ttl, String timeStmp, String imgFrag) {
        if( idSrc < 0 || idDst < 0 || pos < 0 || nbPackets < 0 || !imgFrag.equals("") || ttl < 0){
            throw new IllegalArgumentException("Args must be positive integers.");
        }
        idSource = idSrc;
        idDestination = idDst;
        position = pos;
        this.nbPackets = nbPackets;
        this.ttl = ttl;
        timeStamp = timeStmp;
        imageFragment = imgFrag;
    }

    public String Packet() {
        String positon_String=""+position;
        int size=positon_String.length();//put the position on four digits
        for(int i=0;i<4-size;i++){
            positon_String="0"+positon_String;
        }
        String packet = ""
                + idSource
                + idDestination
                + positon_String
                + nbPackets
                + timeStamp
                + imageFragment
                + ttl
                ;
        return packet;
    }

    public void decreaseTTL() {
        ttl--;
    }

    public boolean isSameSrcAndDst(Packet p) {
        if(p instanceof Packet){
            return (idSource == p.getIdSource() && idDestination == p.getIdDestination());
        }
        return false;
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

    public String getImageFragment() {
        return new String(imageFragment);
    }

    public String getTimeStamp() {
        return new String(timeStamp);
    }

    public int getTtl() {
        return new Integer(ttl);
    }
}
