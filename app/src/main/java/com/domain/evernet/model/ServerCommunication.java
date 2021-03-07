package com.domain.evernet.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerCommunication {
    private Socket serverSocket;
    private String serverAddress;
    private int port;

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

    public ServerCommunication(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void openSocket() {

        System.out.println("je suis Socket");
        try {

            InetAddress serverAddr = InetAddress.getByName(serverAddress);
            System.out.println("je suis dans try Socket");
            this.serverSocket = new Socket(serverAddr, port);
            System.out.println("je Socket" + this.serverSocket.toString());
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() throws IOException {
        serverSocket.close();
    }

    public void sendDataToServer(String dataToSend) throws IOException {
        DataOutputStream data = new DataOutputStream(serverSocket.getOutputStream());
        data.write(1);
        data.writeUTF(dataToSend);
        data.flush();
        data.close();
    }
}