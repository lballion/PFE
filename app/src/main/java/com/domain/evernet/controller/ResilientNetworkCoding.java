package com.domain.evernet.controller;

import com.domain.evernet.model.EncodedPayload;

public class ResilientNetworkCoding implements NetworkEncoderDecoder{

    @Override
    public byte[][] decodePacket(EncodedPayload firstPacket, EncodedPayload secondPacket) {
        return new byte[0][];
    }

    @Override
    public EncodedPayload encodeTwoPacket(byte[] fisrtPayload, byte[] secondPayload) {
        return null;
    }
}
