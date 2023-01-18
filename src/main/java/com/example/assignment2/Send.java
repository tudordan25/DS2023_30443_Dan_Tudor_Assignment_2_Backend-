package com.example.assignment2;
import com.opencsv.CSVReader;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;

public class Send {
    private final static String QUEUE_NAME = "queue";
    private CSVReader csvReader;
    private FileReader filereader;
    private String timestamp;
    private Integer deviceId;
    private Double measurementValue;
    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;

    public Send() {
        try{
            this.factory = new ConnectionFactory();
            this.factory.setHost("localhost");
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);

            this.filereader = new FileReader("./sensor.csv");
            this.csvReader  = new CSVReader(this.filereader);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) throws Exception {

//        try (Connection connection = factory.newConnection();
//             Channel channel = connection.createChannel()) {
//                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//                String message = "Hello World!";
//                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//                System.out.println(" [x] Sent '" + message + "'");
//            }
    }



    public boolean readCSV() {
        try {
            String[] nextRecord;

            if ((nextRecord = csvReader.readNext()) != null) {
                timestamp = Timestamp.from(Instant.now()).toString();
                deviceId = 1;
                measurementValue = Double.parseDouble(nextRecord[0]);

                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public void read(String deviceId) {
        ExecuteTask executeTask = new ExecuteTask();
        Timer timer = new Timer();

        while(readCSV()){
            executeTask.setSensorValue(measurementValue);
            executeTask.run();
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("timestamp", timestamp)
                    .add("deviceId", Integer.parseInt(deviceId))
                    .add("measurementValue", measurementValue).build();

            try{
                channel.basicPublish("", QUEUE_NAME, null, jsonObject.toString().getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(jsonObject);
        }

        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date();
        timer.scheduleAtFixedRate(executeTask, executionDate, 1000);

    }

}
