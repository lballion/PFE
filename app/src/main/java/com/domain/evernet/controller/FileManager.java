package com.domain.evernet.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Base64;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class FileManager {

    private Bitmap originalImage;
    private int width;
    private int height;
    private int newWidth;
    private int newHeight;
    private Matrix matrix;
    private Bitmap resizedBitmap;
    private float scaleWidth;
    private float scaleHeight;
    private ByteArrayOutputStream outputStream;
    private int currentChar;
    private int posOfThisFragment;
    private byte[] imageBytes;
    private String imageString;
    private int maxCharsToSendBySms;
    private int nbPackets;
    private String imageName;

    public FileManager() {

        newWidth = 200;
        newHeight = 200;
        currentChar = 0;
        posOfThisFragment = -1;
        maxCharsToSendBySms = 80;
        nbPackets = 0;
        imageName = null;
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
        Date currentTime = Calendar.getInstance().getTime();
        int heure = currentTime.getHours();
        int min = currentTime.getMinutes();
        int sec = currentTime.getSeconds();
        this.imageName = intToString(heure) + intToString(min) + intToString(sec);
        return resizedBitmap;
    }

    public String intToString(int value) {

        String val = "" + value;
        int size = val.length();//put the position on four digits
        for (int i = 0; i < 2 - size; i++){
            val = "0" + val;
        }
        return val;
    }

    public int getSizeOfBytesArray() { return this.imageBytes.length; }

    public int getSizeOfImageString() { return this.imageString.length(); }

    public void setMaxOfCharsToSendBySms(int max_chars) {

        this.maxCharsToSendBySms = max_chars;
        int modulo = (getSizeOfImageString()% max_chars) > 0 ? 1 : 0;
        this.nbPackets = (getSizeOfImageString() / max_chars) + modulo;
    }

    public String getnextFragment( ) {
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

    public int getposOfThisFragment() {
        this.posOfThisFragment += 1 ;
        return this.posOfThisFragment;
    }

    public int getNbPackets(){
        return this.nbPackets;
    }

    public boolean allFragmentsHaveBeenRecovered() {
        return this.currentChar >= getSizeOfImageString() - 1;
    }

    public String getNameOfPicture() { return this.imageName; }

    public void ForcedImage(String imageTest) {
        imageString = imageTest;
        Date currentTime = Calendar.getInstance().getTime();
        int heure = currentTime.getHours();
        int min = currentTime.getMinutes();
        int sec = currentTime.getSeconds();
        this.imageName = intToString(heure) + intToString(min) + intToString(sec);
    }
}