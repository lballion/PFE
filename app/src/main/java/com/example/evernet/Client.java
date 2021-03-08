package com.example.evernet;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private Socket serverSocket;
    private InetAddress serverAddress;
    private int port;
    DataInputStream in;
    DataOutputStream out;


    public Client(InetAddress serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
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
        in.close();
        serverSocket.close();
    }

    public void sendDataToServer(String dataToSend) throws IOException {
        out = new DataOutputStream(serverSocket.getOutputStream());
        out.writeUTF(dataToSend);
    }

    public String receiveDataFromServer() throws IOException {
        String s = null;
        try {
            in = new DataInputStream(serverSocket.getInputStream());
            s = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
