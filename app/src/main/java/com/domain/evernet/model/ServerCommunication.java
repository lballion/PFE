package com.domain.evernet.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerCommunication {
    private Socket serverSocket;
    private String serverAddress;
    private int port;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String signIn(String alias, String password, String phoneNum, String invitationKey) throws IOException {

        String dataToSend =  String.join("_|_", alias, password, phoneNum, invitationKey);
        //this.sendDataToServer(dataToSend);

        // on appelle la fonction receive() pour le renvoyer
        return "certificat_client_|_private_key_client_|_certificat_serveur_|_END_COMMUNICATION";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String logIn(String alias, String password) {

        String dataToSend =  String.join("_|_", alias, password);

        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getNb(String alias) {

        String dataToSend =  String.join("_|_", alias);

        return "";
    }

    public ArrayList<String> getPhoneNumList(int n) {

        ArrayList<String> numberList = new ArrayList<String>();
        return numberList;
    }

    public String getInvitationKey() {

        return "";
    }

    public ServerCommunication(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void openSocket() {
        try {
            this.serverSocket = new Socket(serverAddress, port);
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