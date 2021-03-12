package com.domain.evernet.controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.domain.evernet.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ImageReceivedView extends Activity implements OnItemClickListener {
    private Context context;
    private Handler handler=new Handler();
    private static ImageReceivedView inst=new ImageReceivedView();
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;

    public static ImageReceivedView instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);

        refreshSmsInbox();
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(Context context, String sms) {

        this.setPacketInHandler(context, sms);

    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPacketInHandler(Context context, String stringPack){
        Packet packet=new Packet();
        packet.setPacket(stringPack);
        ReceivedFile file;
        String key=packet.getSource()+packet.getDestination()+packet.getTimeStamp();
        boolean contains =handler.contains(key);

       if(contains==false){
            file=new ReceivedFile(key);
            file.insertPacket(packet);
            handler.insertFile(key,file);

        }else {
           file=handler.getFileByKey(key);
           file.insertPacket(packet);

        }
        Toast.makeText(context,"handlerSize :"+file.getSize(), Toast.LENGTH_LONG).show();
        this.imageView(context, file, key);
    }

    public void imageView(Context context, ReceivedFile file, String key){
        if(file.allPacketReceived()){
             byte [] bytes= file.stringToArrayBites();
            Toast.makeText(context,"message reçu :"+bytes.length, Toast.LENGTH_LONG).show();
            Bitmap bitmap=file.byteArrayToBitmap(bytes);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, key, "EvernetImage");

        }
    }



}