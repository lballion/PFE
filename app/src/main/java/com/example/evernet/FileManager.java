
package com.example.evernet;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class FileManager {

    ImageView im;
    Bitmap originalImage;
    int width;
    int height;
    int newWidth = 200;
    int newHeight = 200;
    Matrix matrix;
    Bitmap resizedBitmap;
    float scaleWidth ;
    float scaleHeight;
    ByteArrayOutputStream outputStream;
    private int currentChar=0;
    private int posOfThisFragment =-1;
    private byte[] imageBytes;
    private String imageString;
    private int maxCharsToSendBySms=80;
    private int nbPackets=0;
    private String imageName=null;
    public FileManager(ImageView img){
        this.im=img;


    }
    public Bitmap getResizedBitmap( ContentResolver cr, Uri u) throws IOException {
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
        imageBytes=outputStream.toByteArray();
        newWidth = resizedBitmap.getWidth();
        newHeight = resizedBitmap.getHeight();
        imageString = Base64.encodeToString(this.imageBytes, Base64.DEFAULT);
        Date currentTime = Calendar.getInstance().getTime();
       int heure= currentTime.getHours();
       int min=currentTime.getMinutes();
       int sec=currentTime.getSeconds();
        this.imageName=intToString(heure)+intToString(min)+intToString(sec);
        return resizedBitmap;
    }
    public String intToString(int value){
        String val=value>9?""+value:"0"+value;
        return val;
    }
    public int getWidth(){return this.width;}

    public int getHeight(){return this.height;}

    public ImageView getImg(){ return this.im; }

    public int getSizeOfBytesArray(){ return this.imageBytes.length; }

    public int getSizeOfImageString(){return this.imageString.length();}



    public void setMaxOfCharsToSendBySms(int max_chars){
        this.maxCharsToSendBySms=max_chars;
        int modulo=(getSizeOfImageString()% max_chars)>0?1:0;
        this.nbPackets=(getSizeOfImageString()/max_chars) +modulo;

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
    public int getposOfThisFragment(){
        this.posOfThisFragment+=1;
        return this.posOfThisFragment;
    }
    public int getNbPackets(){
        return this.nbPackets;
    }
    public boolean allFragmentsHaveBeenRecovered(){
       // return this.currentChar>=getSizeOfImageString()-1;
        return this.currentChar>=200;
    }
    public String getNameOfPicture(){ return this.imageName;}


}
