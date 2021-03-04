package com.example.evernet;

public class Packet {

    private int idSource;
    private int idDestination;
    private int position;
    private int nbPackets;
    private String imageFragment;
    private int ttl;

    public Packet(int idSrc, int idDst, int pos, int nbPackets, String imgFrag, int ttl) {
        if( idSrc < 0 || idDst < 0 || pos < 0 || nbPackets < 0 || !imgFrag.equals("") || ttl < 0){
            throw new IllegalArgumentException("Args must be positive integers.");
        }
        idSource = idSrc;
        idDestination = idDst;
        position = pos;
        this.nbPackets = nbPackets;
        imageFragment = imgFrag;
        this.ttl = ttl;
    }

    public String Packet() {
        String packet = ""
                + idSource
                + idDestination
                + position
                + nbPackets
                + imageFragment
                + ttl;
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

    public int getTtl() {
        return new Integer(ttl);
    }
}
