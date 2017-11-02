package com.trafficlights;

import com.cpn.CPNTools;
import com.messagehelper.Decode;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.SocketException;

public class TrafficLights {
    private JButton helpButton;
    private JPanel MainPanel;
    private JToolBar menuBar;
    private JLabel trafficLightOne;
    private JLabel trafficLightTwo;
    private JLabel statusMessageLabel;
    private JLabel statusLabel;
    private JToolBar statusBar;
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

    private void setTrafficLightOne(String pathToIcon) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
        trafficLightOne.setIcon(imageIcon);
    }

    private void setTrafficLightTwo(String pathToIcon) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
        trafficLightTwo.setIcon(imageIcon);
    }

    private void setTrafficLightsToDefault() {
        String pathToTrafficLightOneIcon = "images/yellowIsOn.png";
        String pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
        this.setTrafficLightOne(pathToTrafficLightOneIcon);
        this.setTrafficLightTwo(pathToTrafficLightTwoIcon);
    }

    public void setStatusMessageLabel(String statusMessageLabel) {
        this.statusMessageLabel.setText(statusMessageLabel);
    }

    public static void main(String[] args) {
        TrafficLights trafficLights = new TrafficLights();
        JFrame jFrame = new JFrame("Colored Petri Net - Traffic Lights");
        jFrame.setContentPane(trafficLights.MainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

        /*
        CPNTools communication stuff
         */

        CPNTools cpnTools = new CPNTools();
        boolean connectedToCPN = false;
        int port = 9000;

        String pathToTrafficLightOneIcon = "images/yellowIsOn.png";
        String pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
        trafficLights.setTrafficLightsToDefault();

        try {
            cpnTools.accept(port);
            connectedToCPN = true;
            trafficLights.setStatusMessageLabel("communication established.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result;
        int syncCounter = 0;
        while (connectedToCPN) {
            try {
                result = Decode.decodeString(cpnTools.receive());
                if (syncCounter < 3) {
                    syncCounter++;
                    trafficLights.setStatusMessageLabel("syncing traffic lights, please wait.");
                } else {
                    trafficLights.setStatusMessageLabel("receiving data.");
                }
                switch (result) {
                    case "green1":
                        pathToTrafficLightOneIcon = "images/greenIsOn.png";
                        break;
                    case "yellow1":
                        pathToTrafficLightOneIcon = "images/yellowIsOn.png";
                        break;
                    case "red1":
                        pathToTrafficLightOneIcon = "images/redIsOn.png";
                        break;
                    case "green2":
                        pathToTrafficLightTwoIcon = "images/greenIsOn.png";
                        break;
                    case "yellow2":
                        pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
                        break;
                    case "red2":
                        pathToTrafficLightTwoIcon = "images/redIsOn.png";
                        break;
                    default:
                        pathToTrafficLightOneIcon = "images/yellowIsOn.png";
                        pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
                        break;
                }
                trafficLights.setTrafficLightOne(pathToTrafficLightOneIcon);
                trafficLights.setTrafficLightTwo(pathToTrafficLightTwoIcon);
            } catch (SocketException e) {
                try {
                    cpnTools.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                connectedToCPN = false;
                //System.out.println("Connection lost.");
                trafficLights.setStatusMessageLabel("connection lost!");
                trafficLights.setTrafficLightsToDefault();
            }
        }
    }
}