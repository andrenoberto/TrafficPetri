package com.trafficlights;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrafficLights {
    private JButton helpButton;
    private JPanel MainPanel;
    private JToolBar menuBar;
    private JLabel trafficLightOne;
    private JLabel trafficLightTwo;
    private boolean tfOneRed = true;
    private boolean tfTwoRed = true;
    private boolean tfOneYellow = false;
    private boolean tfTwoYellow = false;

    private TrafficLights() {
        helpButton.addActionListener(e -> {
            String address = "https://github.com/andrenoberto/TrafficPetri";
            String addressName = "TrafficPetri";
            String hyperLink = "<a href=\"" + address + "\" target=\"_blank\">" + addressName + "</a>";
            String htmlMessage = "<html>Check out project's github page " + hyperLink + " to get help.</html>";
            JOptionPane.showMessageDialog(null, new MessageWithLink(htmlMessage));
        });

        trafficLightOne.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String pathToIcon;
                if (tfOneRed) {
                    tfOneRed = false;
                    tfOneYellow = false;
                    pathToIcon = "images/greenIsOn.png";
                } else if (tfOneYellow) {
                    tfOneRed = true;
                    tfOneYellow = false;
                    pathToIcon = "images/redIsOn.png";
                } else {
                    tfOneRed = false;
                    tfOneYellow = true;
                    pathToIcon = "images/yellowIsOn.png";
                }
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
                trafficLightOne.setIcon(imageIcon);
            }
        });

        trafficLightTwo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String pathToIcon;
                if (tfTwoRed) {
                    tfTwoRed = false;
                    tfTwoYellow = false;
                    pathToIcon = "images/greenIsOn.png";
                } else if (tfTwoYellow) {
                    tfTwoRed = true;
                    tfTwoYellow = false;
                    pathToIcon = "images/redIsOn.png";
                } else {
                    tfTwoRed = false;
                    tfTwoYellow = true;
                    pathToIcon = "images/yellowIsOn.png";
                }
                ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
                trafficLightTwo.setIcon(imageIcon);
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