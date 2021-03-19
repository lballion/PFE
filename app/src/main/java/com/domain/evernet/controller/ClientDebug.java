package com.domain.evernet.controller;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientDebug extends AsyncTask<Void, Void, Void> {
    private String previousId;

    //the id of the next destination of the message
    private String nextId;

    // Message to end the communication with the server ! Absolute need
    private String endCommunication = "Exit";

    //the type of message we want to send
    private int type;

    //the id of the node that send message
    private String myId;

    //the name of the image send
    private String imageId;

    public ClientDebug(String previousId, String nextId, int type, String myId, String imageId) {
        this.previousId = previousId;
        this.nextId = nextId;
        this.type = type;
        this.myId = myId;
        this.imageId = imageId;
    }

    public ClientDebug (int type, String myId){
        this(null, null, type, myId, null);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket s = new Socket("192.168.1.18",55555);

            DataInputStream input = new DataInputStream(s.getInputStream());
            DataOutputStream output = new DataOutputStream(s.getOutputStream());

            String toSend = messageType(this.type);
            output.writeUTF(toSend);

            String received = input.readUTF();
            if (!received.equals("null"))
            {
                String[] data = received.split("\\|");
                String dest = data[0];
                String  Image = data[1];
            }

            output.writeUTF(this.endCommunication);

            s.close();
            input.close();
            output.close();

        } catch (Exception e) {
            System.out.print("Client error " + e);
        }

        return null;
    }

    /**
     * @param type to choose what kind of message we send:
     *            - Id message when the client is starting to connect tot the server
     *            - Inactive message to remove the Id of the active node list on the server
     *            - Message if a message is received to inform the server
     *            - Ping to check the activity of the node
     * @return the complete message to send  to the debugger
     */
    public String messageType(int type){
        String toSend = "";

        switch(type){
            case 1:
                toSend = "Id" +";"+ myId;
                break;
            case 2:
                toSend = "Inactive" + ";" + myId;
                break;
            case 3:
                toSend = "Message" + ";" + myId + ";" + previousId + ";" + nextId + ";" + imageId;
                break;
            case 4:
                toSend = "Ping" + ";" + myId;
                break;
            default:
                break;
        }
        return toSend;
    }
}
