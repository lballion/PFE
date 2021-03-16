package com.domain.evernet.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsMessage;
import android.widget.Toast;
public class SmsReceiver extends BroadcastReceiver {

    private String sms;
    private static Handler handler=new Handler ();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pduArray = (Object[]) bundle.get("pdus");
            if (pduArray.length == 0) {
                return;
            }
            // Reconstruct sms from pdu
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                sb.append(messages[i].getMessageBody());
            }
            String sender = messages[0].getOriginatingAddress();
            sms = sb.toString();
            this.updateHandler(context,sms);
        }
    }

    public String getSms() {
        return new String(sms);
    }

    public void updateHandler(Context context, String sms) {

        this.setPacketInHandler(context, sms);
    }

    public void setPacketInHandler(Context context, String stringPack) {

        Packet packet=new Packet();
        packet.setPacket(stringPack);
        ReceivedFile file;
        String key=packet.getSource()+packet.getDestination()+packet.getTimeStamp();
        boolean contains =handler.contains(key);

        if (contains==false) {
            file=new ReceivedFile(key);
            file.insertPacket(packet.getPosition(),packet.getImageFragment());
            file.setNbPackets(packet.getNbPackets());
            handler.insertFile(key,file);

        } else {
            file=handler.getFileByKey(key);
            file.insertPacket(packet.getPosition(),packet.getImageFragment());
        }
        Toast.makeText(context,"handlerSize :" + file.getSize(), Toast.LENGTH_LONG).show();
        this.imageView(context, file, key);
    }

    public void imageView(Context context, ReceivedFile file, String key) {

        if(file.allPacketReceived()) {
            byte [] bytes= file.stringToArrayBites();
            Toast.makeText(context,"message reçu :" + bytes.length, Toast.LENGTH_LONG).show();
            Bitmap bitmap=file.byteArrayToBitmap(bytes);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, key, "EvernetImage");
        }
    }
}