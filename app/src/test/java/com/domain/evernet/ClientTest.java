package com.domain.evernet;

import com.domain.evernet.model.Client;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests of the class Client
 */
public class ClientTest {

    @Test
    public void openSocketTest () throws UnknownHostException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        assertNotEquals(c.getSocket(), null);
    }

    @Test
    public void closeSocketTest () throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.closeSocket();
        assertEquals(c.getSocket().isClosed(), true);
    }

    @Test
    public void receiveDataFromServer () throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.sendDataToServer("test");
        String s = c.receiveDataFromServer();

        assertNotEquals(s, null);
    }

    @Test
    public void signInTest() throws IOException {

        Random r = new Random();
        char randomChar = (char)(r.nextInt(26) + 'a');
        long randomInt = (long) (r.nextDouble() * 9000000000L + 1000000000L);
        String user = "toto" + randomChar + randomInt;
        System.out.println(user);
        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        String s = c.signIn(user, user, "" + randomInt,"martin");
        assertNotEquals(s, null);
    }

    @Test
    public void signInWrongParamTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        String s = c.signIn(" ", "toto", "0000000000"," ");
        assertEquals(s, null);
    }

    @Test
    public void logInTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        HashMap<String, String> h = c.logIn("tototer", "tototer");
        assertNotEquals(h.isEmpty(), true);
    }

    @Test
    public void logInWrongParamTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        HashMap<String, String> h = c.logIn("uhejniojz", "poqkfa");
        c.closeSocket();
        assertEquals(h.isEmpty(), true);
    }

    @Test
    public void getPhoneNbTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("tototer", "tototer");
        HashMap<String, String> h = c.getPhoneNb("toto");
        c.closeSocket();
        assertNotEquals(h.isEmpty(), true);
    }

    @Test
    public void getPhoneNbWrongParamTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("totobis", "totobis");
        HashMap<String, String> h = c.getPhoneNb(" ");
        c.closeSocket();
        assertEquals(h.isEmpty(), true);
    }

    @Test
    public void getPhoneNumListTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("tototer", "tototer");
        HashMap<String, String> h = c.getPhoneNumList("5");
        c.closeSocket();
        assertNotEquals(h.isEmpty(), true);
    }

    @Test
    public void getPhoneNumListWrongParamTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("totobis", "totobis");
        HashMap<String, String> h = c.getPhoneNb("a");
        c.closeSocket();
        assertEquals(h.isEmpty(), true);
    }

    @Test
    public void getInvitationKeyTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("totobis", "totobis");
        String s = c.getInvitationKey();
        c.closeSocket();
        assertNotEquals(s, null);
    }

    @Test
    public void getAllAliasTest() throws IOException {

        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        c.openSocket();
        c.receiveDataFromServer();
        c.logIn("totobis", "totobis");
        ArrayList<String> a = c.getAllAlias("password");
        c.closeSocket();
        assertNotEquals(a.isEmpty(), true);
    }

    @Test
    public void truncateMarkersTest() throws UnknownHostException {
        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        String s = "_|_BEGIN_COMMUNICATION_|_Bonjour_|_salut_|_END_COMMUNICATION";
        assertEquals(c.truncateMarkers(s), "Bonjour_|_salut");
    }

    @Test
    public void addMarkersTest() throws UnknownHostException {
        InetAddress i = InetAddress.getByName("109.215.55.162");
        Client c = new Client(i, 50000);
        String s = c.addMarkers("argument", "getSleep");
        assertEquals(s, "_|_BEGIN_COMMUNICATION_|_getSleep_|_argument_|_END_COMMUNICATION");
    }


}