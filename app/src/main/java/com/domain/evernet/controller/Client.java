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
    private DataOutputStream out;
    private InputStream input;
    private InputStreamReader reader;

    public Client(InetAddress serverAddress, int port) {

        this.serverAddress = serverAddress;
        this.port = port;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> signIn(String alias, String password, String phoneNum, String invitationKey) throws IOException {

        // Prepare data to send
        String dataToSend = String.join("_|_", alias, password, phoneNum, invitationKey);
        dataToSend = addMarkers(dataToSend, "signIn");
        HashMap<String, String> map = new HashMap<>();

        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");
        response = truncateMarkers(response);
        String[] responses = response.split("_\\|_");

        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            return map;
        } else {
            map.put("certificat_client", responses[0]);
            map.put("private_key_client", responses[1]);
            map.put("certificat_serveur", responses[2]);
        }

        System.out.println("debug : " + map.toString());
        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> logIn(String alias, String password) throws IOException {

        String dataToSend = String.join("_|_", alias, password);
        dataToSend = addMarkers(dataToSend, "logIn");
        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");
        response = truncateMarkers(response);
        String[] responses = response.split("_\\|_");

        HashMap<String, String> map = new HashMap<>();

        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            map.put("Authentified", responses[0]);
        }

        System.out.println("debug login : " + dataToSend);
        System.out.println("debug login : " + response);
        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String, String> getPhoneNb(String alias) throws IOException {

        String dataToSend = String.join("_|_", alias);
        dataToSend = addMarkers(dataToSend, "getPhoneNum");
        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");
        response = truncateMarkers(response);
        String[] responses = response.split("_\\|_");

        HashMap<String, String> map = new HashMap<>();

        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            return map;
        } else {
            map.put("number", responses[0]);
            map.put("certificat", responses[1]);
        }

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String,String> getPhoneNumList(String sizeList) throws IOException {

        String dataToSend = String.join("_|_", sizeList);
        dataToSend = addMarkers(dataToSend, "getPhoneNumList");
        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");
        response = truncateMarkers(response);
        String[] responses = response.split("_\\|_");
        HashMap<String,String> numbersList=new HashMap<>();
        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            return numbersList;
        } else {
            for (int i = 0; i < responses.length; i++) {
                if (i % 2 == 0) {
                    numbersList.put(responses[i],responses[i+1]);
                }
            }
        }

        return numbersList;
    }

    public String getInvitationKey() throws IOException {

        String dataToSend = "";
        dataToSend = addMarkers(dataToSend, "getInvitationKey");
        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");
        response = truncateMarkers(response);
        String[] responses = response.split("_\\|_");

        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            responses[0] = "";
            return responses[0];
        }

        return responses[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> getAllAlias(String adminPw) throws IOException {

        String dataToSend = String.join("_|_", adminPw);
        dataToSend = addMarkers(dataToSend, "getInvitationKey");
        sendDataToServer(dataToSend);
        String response = receiveDataFromServer();
        response = response.replace("*", "");

        String[] responses = response.split("_\\|_");
        ArrayList<String> list = new ArrayList<>();
        if (responses[0].contains("Invalid callBack") || responses[0].contains("ERROR")) {
            return list;
        } else {
            for (int i = 0; i < responses.length; i++) {
                list.add(i,responses[i]);
            }
        }

        return list;
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

    // Remove markers from server's response
    public String truncateMarkers(String s) {

        String truncatedStr = s.replace("_|_BEGIN_COMMUNICATION_|_", "");
        truncatedStr = truncatedStr.replace("_|_END_COMMUNICATION", "");

        return truncatedStr;
    }

    // Adding required markers from the server to understand the future request
    public String addMarkers(String s, String callback) {

        String markerBegin = "_|_BEGIN_COMMUNICATION_|_" + callback + "_|_";
        String markerEnd = "_|_END_COMMUNICATION";
        return markerBegin.concat(s.concat(markerEnd));
    }

    public Socket getSocket() {
        return serverSocket;
    }
}