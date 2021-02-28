package com.example.evernet;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.iceteck.silicompressorr.SiliCompressor;

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
import java.util.Date;

public class FileManager {

    private File f;
    private File srcFile;
    private File dstFile;
    private SimpleDateFormat dateFormatter;
    public int debugWidth;
    public int debugHeight;
    private static final String IMG_DIR = "resizedImg";
    public boolean exists = false;
    public Bitmap finalBitmap = null;
    private long sizeBefore = 0;
    private long sizeAfter = 0;

    public FileManager(Context context, Uri selectedImage) throws IOException {

        try{
            f = new File(context.getExternalFilesDir(IMG_DIR).toString());
            if (!f.exists()) {
                //f.createNewFile();
                f.mkdir();
            }
            srcFile = new File(selectedImage.getPath());
            dstFile = new File(f,"resizedImgDst.png");
            //if (dstFile != null) exists = true;
            this.copyFile();
            sizeBefore = dstFile.length();
            //finalBitmap = SiliCompressor.with(context).getCompressBitmap(selectedImage.get());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*
        File f = new File(selectedImage.getPath());
        if (!f.exists())
        {
            try {
                f.createNewFile();
                copyFile(new File(getRealPathFromURI(contect, f);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }



    // DEBUG

    public boolean getExists(){
        return exists;
    }

    public Bitmap getFinalBitmap(){
        return finalBitmap;
    }

    //https://www.mindbowser.com/image-compression-in-android/
    private void copyFile() throws IOException {
        if (!srcFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(srcFile).getChannel();
        destination = new FileOutputStream(dstFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public File getDstFile() {
        return dstFile;
    }


    public Bitmap resizeFile(ContentResolver cr, Uri u) {
        Bitmap tmp = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis;
        try {
            fis = new FileInputStream(dstFile);
            //if( fis != null) exists = true;
            //finalBitmap = BitmapFactory.decodeStream(fis, null, o);
            tmp = MediaStore.Images.Media.getBitmap(cr, u);
            finalBitmap = tmp.copy(tmp.getConfig(), true);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(dstFile);
            //finalBitmap = new Bitmap();
            //finalBitmap = BitmapFactory.decodeStream(fis, null, o2);
            tmp = MediaStore.Images.Media.getBitmap(cr, u);
            finalBitmap = tmp.copy(tmp.getConfig(), true);
            //if( b != null) exists = true;
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        try {
            FileOutputStream out = new FileOutputStream(dstFile);
            //debugHeight = b.getHeight();
            //debugWidth = b.getWidth();
            String s = dstFile.getAbsolutePath();
            String ext = s.substring(s.lastIndexOf("."));

            //String s = dstFile.get
            if (ext.equals("jpg") || ext.equals("jpeg")) {
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            } else if (ext.equals("png")){
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
            }
            sizeAfter = out.getChannel().size();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (finalBitmap != null) exists = true;
        return finalBitmap;
    }

    public int getDebugWidth(){
        return new Integer(debugWidth);
    }

    public int getDebugHeight(){
        return new Integer(debugHeight);
    }

    public File getSrcFile() {
        return srcFile;
    }

    public long getSizeBefore() {
        return sizeBefore;
    }

    public long getSizeAfter() {
        return sizeAfter;
    }
}

