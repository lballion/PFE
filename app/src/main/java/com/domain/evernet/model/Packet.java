package com.domain.evernet.model;

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

    public  Packet() { }

    public String getPacket() {

        String packet = ""
                + stuffString(source,'*',9)
                + stuffString(destination, '*', 9)
                + stuffString("" + position, '0',4)
                + stuffString("" + nbPackets, '0', 4)
                + timeStamp
                + ttl
                + imageFragment
                ;
        return packet;
    }

    public String  stuffString(String stringToStuff, char c, int maxSize) {

        int size = stringToStuff.length();//put the position on four digits
        for(int i = 0; i < maxSize - size; i++){
            stringToStuff = c + stringToStuff;
        }
        return stringToStuff;
    }

    public void decreaseTTL() {
        ttl--;
    }

    public boolean isSameSrcAndDst(Packet p) {

        if(p instanceof Packet) {
            return (source.equals(p.getSource())  && destination.equals(p.getDestination()));
        }
        return false;
    }

    public String getSource() { return source; }

    public String getDestination() { return destination; }

    public int getPosition() {
        return position;
    }

    public int getNbPackets() {
        return nbPackets;
    }

    public String getImageFragment() { return  imageFragment; }

    public String getTimeStamp() { return timeStamp; }

    public int getTtl() {
        return new Integer(ttl);
    }

    public String extractSrc(String stringPack) { return stringPack.substring(0,10); }

    public String extractDst(String stringPack) { return stringPack.substring(10,20); }

    public String extractPosition(String stringPack) { return stringPack.substring(20,24); }

    public String extractNBpackets(String stringPack) { return stringPack.substring(24,28); }

    public String extractNameOfIm(String stringPack) { return stringPack.substring(28,34); }

    public String extractTtl(String stringPack) { return stringPack.substring(34,35); }

    public String extractFragment(String stringPack) { return stringPack.substring(35,stringPack.length()); }

    public void setPacket(String stringPack) {

        this.source = this.extractSrc(stringPack);
        this.destination = this.extractDst(stringPack);
        this.position = Integer.parseInt(this.extractPosition(stringPack));
        this.nbPackets = Integer.parseInt(this.extractNBpackets(stringPack));
        this.ttl = Integer.parseInt(this.extractTtl(stringPack));
        this.timeStamp = this.extractNameOfIm(stringPack);
        this.imageFragment = this.extractFragment(stringPack);
    }
}
