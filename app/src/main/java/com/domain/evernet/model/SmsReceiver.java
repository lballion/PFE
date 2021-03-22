package com.domain.evernet.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.domain.evernet.controller.ClientDebug;
import com.domain.evernet.controller.DashboardActivity;

import static com.domain.evernet.controller.MainActivity.PHONE_NUMBER;
import static com.domain.evernet.controller.MainActivity.PREF_PSEUDO;
import static com.domain.evernet.controller.MainActivity.getDefaults;

public class SmsReceiver extends BroadcastReceiver {

    private String sms;
    private static Handler handler=new Handler ();
    private static DashboardActivity dashboardActivity;

    private String pseudo = getDefaults(PREF_PSEUDO, dashboardActivity.getApplicationContext());

    @RequiresApi(api = Build.VERSION_CODES.O)
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

            Packet packet=new Packet();
            packet.setPacket(sms);
            ClientDebug clientDebug = new ClientDebug(packet.getSource(), packet.getDestination(), 6, pseudo , packet.getTimeStamp());
            clientDebug.setImageFragment(sms);
            clientDebug.execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateHandler(Context context, String sms) {

        this.setPacketInHandler(context, sms);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setPacketInHandler(Context context, String stringPack) {

        Packet packet=new Packet();
        packet.setPacket(stringPack);
        ReceivedFile file;
        String key=packet.getSource()+packet.getDestination()+packet.getTimeStamp();
        String myPhoneNumber=getDefaults(PHONE_NUMBER,context);
        if(packet.getDestination().equals(myPhoneNumber)) {
            boolean contains = handler.contains(key);
            if (contains==false) {
                file = new ReceivedFile(key);
                file.insertPacket(packet.getPosition(),packet.getImageFragment());
                file.setNbPackets(packet.getNbPackets());
                handler.insertFile(key,file);
            } else {
                file=handler.getFileByKey(key);
                file.insertPacket(packet.getPosition(),packet.getImageFragment());
            }
            Toast.makeText(context,"message :" + file.getSize(), Toast.LENGTH_LONG).show();
            this.imageView(context, file, key);
        } else {
            dashboardActivity = DashboardActivity.instance();
            packet.decreaseTTL();
            String target=null;
            if( packet.getTtl() <=1 ) {
                target= packet.getDestination();
            }
            dashboardActivity.sendTo(packet.getPacket(),target);
        }
    }

    public void imageView(Context context, ReceivedFile file, String key) {

        if(file.allPacketReceived()) {
            byte [] bytes = file.stringToArrayBytes();
            Toast.makeText(context,"Image reçu :" + bytes.length, Toast.LENGTH_LONG).show();
            Bitmap bitmap = file.byteArrayToBitmap(bytes);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, key, "EvernetImage");
        }
    }
}