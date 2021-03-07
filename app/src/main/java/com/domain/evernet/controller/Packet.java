package com.domain.evernet.controller;

public class Packet {

    private String source;
    private String destination;
    private int position;
    private int nbPackets;
    private int ttl;
    private String timeStamp;
    private String imageFragment;

    public Packet(String src, String dst, int pos, int nbPackets, int ttl, String timeStmp, String imgFrag) {
        if( src.equals("") || dst.equals("") || pos < 0 || nbPackets < 0 || imgFrag.equals("") || ttl < 0){
            throw new IllegalArgumentException("Args must be positive integers.");
        }
        source = src;
        destination = dst;
        position = pos;
        this.nbPackets = nbPackets;
        this.ttl = ttl;
        timeStamp = timeStmp;
        imageFragment = imgFrag;
    }

    public String Packet() {
        String packet = ""
                +  stuffString(source,'*',9)
                +  stuffString(destination, '*', 9)
                +  stuffString(""+position, '0',4)
                + nbPackets
                + timeStamp
                + ttl
                + imageFragment
                ;
        return packet;
    }
    public String  stuffString(String stringToStuff, char c, int maxSize){
        int size=stringToStuff.length();//put the position on four digits
        for(int i=0;i<maxSize-size;i++){
            stringToStuff=c+stringToStuff;
        }
        return stringToStuff;
    }
    public void decreaseTTL() {
        ttl--;
    }

    public boolean isSameSrcAndDst(Packet p) {
        if(p instanceof Packet){
            return (source.equals(p.getSource())  && destination.equals(p.getDestination()));
        }
        return false;
    }

    public String getSource() { return source; }

    public String getDestination() { return destination; }

    public int getPosition() {
        return new Integer(position);
    }

    public int getNbPackets() {
        return new Integer(nbPackets);
    }

    public String getImageFragment() { return  imageFragment; }

    public String getTimeStamp() {
        return new String(timeStamp);
    }

    public int getTtl() {
        return new Integer(ttl);
    }
}
