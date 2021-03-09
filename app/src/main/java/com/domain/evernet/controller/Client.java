package com.domain.evernet.controller;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import androidx.annotation.RequiresApi;

public class Client {
    private Socket serverSocket;
    private InetAddress serverAddress;
    private int port;
    DataOutputStream out;
    InputStream input;
    InputStreamReader reader;


    public Client(InetAddress serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> signIn(String alias, String password, String phoneNum, String invitationKey) throws IOException {

        // Préparaion des données pour le serveur
        String dataToSend =  String.join("_|_", alias, password, phoneNum, invitationKey);


        //this.sendDataToServer(dataToSend);
        // on appelle la fonction receive() pour le renvoyer

        String response = "certificat_client_|_private_key_client_|_certificat_serveur_|_END_COMMUNICATION";
        String[] responses = response.split("_\\|_");
        HashMap<String, String> map = new HashMap<>();
        map.put("certificat_client", responses[0]);
        map.put("private_key_client", responses[1]);
        map.put("certificat_serveur", responses[2]);

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> logIn(String alias, String password) {

        String dataToSend =  String.join("_|_", alias, password);

        String response = "Authentified_|_END_COMMUNICATION";
        String[] responses = response.split("_\\|_");

        HashMap<String, String> map = new HashMap<>();
        map.put("Authentified", responses[0]);

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> getNb(String alias) {

        String dataToSend =  String.join("_|_", alias);

        String response = "0761375067_|_certificat_|_END_COMMUNICATION";
        String[] responses = response.split("_\\|_");


        HashMap<String, String> map = new HashMap<>();
        map.put("number", responses[0]);
        map.put("certificat", responses[1]);

        return map;
    }

    public ArrayList<ArrayList<String>> getPhoneNumList(int n) {

        int dataToSend = n;


        String response = "*0761375067*_|_*certif1*_|_*0761375067*_|_*certif2*_|_END_COMMUNICATION";
        String[] responses = response.split("_\\|_");
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        for(int i=0; i<responses.length; i++) {
            if (i%2 == 0) {
                ArrayList<String> ele = new ArrayList<>();
                ele.add(responses[i]);
                ele.add(responses[i+1]);
                array.add(ele);
            }
        }
        return array;
    }

    public String getInvitationKey() {

        String response = "*invitation_key*_|_END_COMMUNICATION";
        String[] responses = response.split("_\\|_");

        return responses[0];
    }

    public void openSocket() {
        try {
            serverSocket = new Socket(serverAddress, port);

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void closeSocket() throws IOException {
        out.flush();
        out.close();
        input.close();
        serverSocket.close();
    }

    public void sendDataToServer(String dataToSend) throws IOException {
        out = new DataOutputStream(serverSocket.getOutputStream());
        out.writeUTF(dataToSend);
    }

    public String receiveDataFromServer() throws IOException {
        String s = null;
        try {
            int character;
            input = serverSocket.getInputStream();
            reader = new InputStreamReader(input);
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
                if (data.toString().contains("_|_END_COMMUNICATION")) break;
            }
            s = data.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}