package com.example.assignment2;

import java.util.TimerTask;

public class ExecuteTask extends TimerTask {
    private Double sensorValue;

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }
    @Override
    public void run() {
        System.out.println(this.sensorValue);
        try {

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
