package com.domain.evernet.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerCommunication {
    private Socket serverSocket;
    private String serverAddress;
    private int port;

    public String signIn(String alias, String password, String phoneNum, String invitationKey) {


        return "";
    }

    public String logIn(String alias, String password) {

        return "";
    }

    public String getNb(String alias) {

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
            serverSocket = new Socket(serverAddress, port);
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