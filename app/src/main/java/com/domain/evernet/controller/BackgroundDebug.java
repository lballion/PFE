package com.domain.evernet.controller;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundDebug {
    String myId;
    Timer timer;
    TimerTask timerTask;
    ClientDebug client;

    public BackgroundDebug(String myId) {
        this.myId = myId;
    }

    public void startTimerTask(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                client = new ClientDebug(4, myId);
                client.execute();
            }
        };
        timer.schedule(timerTask, 0, 10000);
    }

    public void stopTimerTask(){
        timer.purge();
    }

    public String toSend () {
        return client.toSend;
    }

}