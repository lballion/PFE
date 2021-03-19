package com.domain.evernet.controller;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class NetworkCoding {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject generateTopology() throws JSONException {

        /*
        InetAddress i = null;
        try {
            i = InetAddress.getByName("109.215.55.162");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Client c = new Client(i, 50000);
        c.openSocket();

        HashMap<String, String> phoneNumList;

        try {
            phoneNumList = c.getPhoneNumList("8");
            c.closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */

        // on simule la réponce du serveur ici
        HashMap<String, String> phoneNumList = new HashMap<>();
        phoneNumList.put("0761375067", "cert1");
        phoneNumList.put("0761375002", "cert2");
        phoneNumList.put("0761375063", "cert3");
        phoneNumList.put("0761375064", "cert4");

        // creation de la topologie

        ArrayList<String> phones = new ArrayList<>();
        for (String key : phoneNumList.keySet()) {
            phones.add(key);
        }

        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < phones.size()-1; i++) {
            ArrayList<String> links = new ArrayList<>();
            
            for (int j = i+1; j < phones.size(); j++) {
                links.add(phones.get(j));
            }
            
            jsonObject.put(phones.get(i), links);
        }

        return jsonObject;
    }
}
