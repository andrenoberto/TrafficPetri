package com.trafficlights;

import javax.swing.*;

public class TrafficLights {
    private JButton button1;
    private JPanel MainPanel;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Colored Petri Net - Traffic Lights");
        jFrame.setContentPane(new TrafficLights().MainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}