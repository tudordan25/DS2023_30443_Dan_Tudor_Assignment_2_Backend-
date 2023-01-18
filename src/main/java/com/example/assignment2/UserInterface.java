package com.example.assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UserInterface {
    private final JButton startSimulation=new JButton("Start simulation");

    public UserInterface() {
        JFrame jFrame = new JFrame();
        jFrame.setSize(250,250);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel=new JPanel();

        panel.setBackground(Color.decode("#ccd9ff"));
        panel.setLayout(null);
        jFrame.add(panel);

        startSimulation.setBounds(27,87,170,35);
        panel.add(startSimulation);

        startSimulationActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Send send = new Send();

                List<String> allDeviceIds= new ArrayList<>();
                try {
                    File myObj = new File("devicesIds.txt");
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String deviceId = myReader.nextLine();
                        allDeviceIds.add(deviceId);
                    }
                    myReader.close();
                } catch (FileNotFoundException e1) {
                    System.out.println("An error occurred.");
                    e1.printStackTrace();
                }

                Random rand = new Random();
                int randomIndex = rand.nextInt(allDeviceIds.size());
                send.read(allDeviceIds.get(randomIndex));
            }

        });

        jFrame.setVisible(true);
    }

    public void startSimulationActionListener(final ActionListener actionListener)
    {
        startSimulation.addActionListener(actionListener);
    }



}
