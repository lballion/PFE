package com.domain.evernet.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class EncodedPayload {

    private final int[] formula;
    private final byte[] payload;

    public EncodedPayload(int[] formula, byte[] payload){
        this.formula = formula;
        this.payload = payload;
    }

    public int[] getFormula() {
        return formula;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(payload) *
                Arrays.hashCode(formula) *
                17;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if(!(obj instanceof EncodedPayload))
            return false;
        if(obj == this)
            return true;

        EncodedPayload peer= (EncodedPayload) obj;

        Boolean result =
                Arrays.equals(this.getFormula(), peer.getFormula())
                && Arrays.equals(this.getPayload(), peer.getPayload())
                && this.hashCode() == peer.hashCode();

         return result;
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        int[] formulaOut = this.getFormula();
        byte[] payloadOut = this.getPayload();
        EncodedPayload returnedObj = new EncodedPayload(formulaOut, payloadOut);
        return returnedObj;
    }
}
