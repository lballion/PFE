package com.example.evernet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

//https://stackoverflow.com/questions/11435354/receiving-sms-on-android-app
public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "Telephony.Sms.Intents.SMS_RECEIVED_ACTION";
    private String sms;

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
        }
    }

    public String getSms() {
        return new String(sms);
    }
}