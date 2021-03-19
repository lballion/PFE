package com.domain.evernet;

import com.domain.evernet.model.Client;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
}