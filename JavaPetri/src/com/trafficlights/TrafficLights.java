package com.trafficlights;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrafficLights {
    private JButton helpButton;
    private JPanel MainPanel;

    public TrafficLights() {
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String address = "https://github.com/andrenoberto/TrafficPetri";
                String addressName = "TrafficPetri";
                String hyperLink = "<a href=\"" + address + "\" target=\"_blank\">" + addressName + "</a>";
                String htmlMessage = "<html>Check out project's github page " + hyperLink + " to get help.</html>";
                JOptionPane.showMessageDialog(null, new MessageWithLink(htmlMessage));
                //JOptionPane.showMessageDialog(null, htmlMessage);
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Colored Petri Net - Traffic Lights");
        jFrame.setContentPane(new TrafficLights().MainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}