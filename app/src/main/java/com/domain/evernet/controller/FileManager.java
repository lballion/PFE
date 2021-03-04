package com.domain.evernet.controller;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;

public class FileManager {
    private ImageView im;
    private Bitmap originalImage;
    private int width;
    private int height;
    private int newWidth = 200;
    private int newHeight = 200;
    private Matrix matrix;
    private Bitmap resizedBitmap;
    private float scaleWidth;
    private float scaleHeight;
    private ByteArrayOutputStream outputStream;
    private int currentChar = 0;
    private int posOfThisFragment = -1;
    private byte[] imageBytes;
    private String imageString;
    private int maxCharsToSendBySms = 80;
    private int nbPackets = 0;

    public FileManager() {
    }

    public Bitmap getResizedBitmap(ContentResolver cr, Uri u) throws IOException {
        originalImage = MediaStore.Images.Media.getBitmap(cr,u);
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        matrix = new Matrix();
        scaleWidth = ((float) newWidth) / width;
        scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        resizedBitmap = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, true);
        outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
        imageBytes = outputStream.toByteArray();
        newWidth = resizedBitmap.getWidth();
        newHeight = resizedBitmap.getHeight();
        imageString = Base64.encodeToString(this.imageBytes, Base64.DEFAULT);
        return resizedBitmap;
    }

    public void setMaxOfCharsToSendBySms(int max_chars) {
        this.maxCharsToSendBySms = max_chars;
        int modulo = (getSizeOfImageString() % max_chars) > 0 ? 1 : 0;
        this.nbPackets = (getSizeOfImageString() / max_chars) + modulo;
    }

    public int getWidth() {
        return new Integer(this.width);
    }

    public int getHeight() {
        return new Integer(this.height);
    }

    public int getSizeOfBytesArray() {
        return new Integer(this.imageBytes.length);
    }

    public int getSizeOfImageString() {
        return new Integer(this.imageString.length());
    }

    public String getnextFragment() {
        String fragment = "";
        for (int i = this.currentChar; i < this.currentChar + this.maxCharsToSendBySms; i++) {
            if (i >= getSizeOfImageString()) {
                break;
            }
            fragment += imageString.charAt(i);
        }
        this.currentChar += maxCharsToSendBySms;
        return fragment;
    }

    public int getFragmentCurrentPos() {
        this.posOfThisFragment += 1;
        return this.posOfThisFragment;
    }

    public int getNbPackets() {
        return this.nbPackets;
    }

    public boolean allFragmentsHaveBeenRecovered() {
        return this.currentChar >= getSizeOfImageString() - 1;
    }
}
