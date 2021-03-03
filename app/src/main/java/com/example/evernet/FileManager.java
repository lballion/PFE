
package com.example.evernet;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import android.util.Base64;
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
        return resizedBitmap;
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
        return this.currentChar>=getSizeOfImageString()-1;
    }

}
