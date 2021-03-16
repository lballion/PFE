package com.domain.evernet.controller;

import com.domain.evernet.model.EncodedPayload;

public interface NetworkEncoderDecoder {

    public byte[][] decodePacket(EncodedPayload firstPacket, EncodedPayload secondPacket);
    public EncodedPayload encodeTwoPacket(byte[] fisrtPayload, byte[] secondPayload);
}
