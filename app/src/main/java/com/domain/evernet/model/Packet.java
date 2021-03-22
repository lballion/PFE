package com.domain.evernet.model;

public class Packet {

    private String source;
    private String destination;
    private int[] position;
    private int nbPackets;
    private int ttl;
    private String timeStamp;
    private String imageFragment;

    public Packet(String src, String dst, int[] pos, int nbPackets, int ttl, String timeStmp, String imgFrag) {

        if( src.equals("") || dst.equals("") || pos.length < 0 || pos.length > 2 || nbPackets < 0 || imgFrag.equals("") || ttl < 0){
            throw new IllegalArgumentException("One or several argumrnt are invalid.");
        }
        source = src;
        destination = dst;
        position = pos.clone();
        this.nbPackets = nbPackets;
        this.ttl = ttl;
        timeStamp = timeStmp;
        imageFragment = imgFrag;
    }

    public  Packet() { }

    public String getPacket() {

        String packet = ""
                + stuffString(source,'*',10)
                + stuffString(destination, '*', 10)
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

    public int[] getPosition() {
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

    public String extractPosition(String stringPack) { return stringPack.substring(20,28); }

    public String extractNBpackets(String stringPack) { return stringPack.substring(28,32); }

    public String extractNameOfIm(String stringPack) { return stringPack.substring(32,38); }

    public String extractTtl(String stringPack) { return stringPack.substring(38,39); }

    public String extractFragment(String stringPack) { return stringPack.substring(39,stringPack.length()); }

    public void setPacket(String stringPack) {

        this.source = this.extractSrc(stringPack);
        this.destination = this.extractDst(stringPack);
        String positions = this.extractPosition(stringPack);

        String[] parsePosition = positions.replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","").replaceAll("\\s","").split(",");
        if(parsePosition.length > 2)
            throw new IllegalArgumentException("The number of position in the packet is invalid");

        for (int i=0; i< parsePosition.length ; i++){this.position [i]= Integer.parseInt(parsePosition[i]);
        }
        this.nbPackets = Integer.parseInt(this.extractNBpackets(stringPack));
        this.ttl = Integer.parseInt(this.extractTtl(stringPack));
        this.timeStamp = this.extractNameOfIm(stringPack);
        this.imageFragment = this.extractFragment(stringPack);
    }
}
