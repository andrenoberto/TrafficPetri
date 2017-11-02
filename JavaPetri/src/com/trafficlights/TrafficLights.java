package com.trafficlights;

import com.cpn.CPNTools;
import com.messagehelper.Decode;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;
import java.io.IOException;
import java.net.SocketException;

public class TrafficLights extends JFrame implements KeyListener, ActionListener, MenuListener {
    private JPanel MainPanel;
    private JLabel trafficLightOne;
    private JLabel trafficLightTwo;
    private JLabel statusMessageLabel;
    private JLabel statusLabel;
    private JToolBar statusBar;
    private boolean tfOneRed = true;
    private boolean tfTwoRed = true;
    private boolean tfOneYellow = false;
    private boolean tfTwoYellow = false;
    /*
        Toolbar variables
     */
    private JMenuBar menuBar;
    private JMenu file;
    private JMenu help;
    private JMenuItem _fExit;
    private JMenuItem _hAbout;
    private JMenuItem _hHelp;

    private TrafficLights(boolean ... addMouseListener) {
        setTitle("Colored Petri Net - Traffic Lights");
        /*helpButton.addActionListener(e -> {
            String address = "https://github.com/andrenoberto/TrafficPetri";
            String addressName = "TrafficPetri";
            String hyperLink = "<a href=\"" + address + "\" target=\"_blank\">" + addressName + "</a>";
            String htmlMessage = "<html>Check out project's github page " + hyperLink + " to get help.</html>";
            JOptionPane.showMessageDialog(null, new MessageWithLink(htmlMessage));
        });*/
        /*
            Toolbar section
         */
        this.addKeyListener(this);
        this.menuBar = new JMenuBar();
        /*
            Dropdown menus
         */
        this.file = new JMenu("File");
        this.file.setMnemonic(KeyEvent.VK_F);
        this.file.addMenuListener(this);
        this.menuBar.add(file);

        this.help = new JMenu("Help");
        this.help.setMnemonic(KeyEvent.VK_H);
        this.help.addMenuListener(this);
        this.menuBar.add(help);
        /*
            File
         */
        this._fExit = new JMenuItem("Exit");
        this._fExit.setMnemonic(KeyEvent.VK_X);
        this._fExit.addActionListener(this);
        this.file.add(this._fExit);
        /*
            Help
         */
        this._hAbout = new JMenuItem("About");
        this._hAbout.addActionListener(this);
        this.help.add(this._hAbout);

        this._hHelp = new JMenuItem("Help");
        this._hHelp.addActionListener(this);
        this.help.add(this._hHelp);

        this.setJMenuBar(this.menuBar);
        /*
        Mouse listener section
         */
        boolean mouseListener;
        if (addMouseListener.length > 0) {
            mouseListener = addMouseListener[0];
            if (!mouseListener) {
                return;
            }
        } else {
            return;
        }
        this.trafficLightOne.addMouseListener(new MouseAdapter() {
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

        this.trafficLightTwo.addMouseListener(new MouseAdapter() {
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

    private void setStatusMessageLabel(String statusMessageLabel) {
        this.statusMessageLabel.setText(statusMessageLabel);
    }

    public static void main(String[] args) {
        TrafficLights trafficLights = new TrafficLights();
        trafficLights.setContentPane(trafficLights.MainPanel);
        trafficLights.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        trafficLights.pack();
        trafficLights.setVisible(true);
        /*
        CPNTools communication stuff
         */
        String pathToTrafficLightOneIcon = "images/yellowIsOn.png";
        String pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
        trafficLights.setTrafficLightsToDefault();

        String result;
        int port = 9000;
        int syncCounter = 0;
        boolean connectedToCPN = false;
        while (true) {
            CPNTools cpnTools = new CPNTools();
            try {
                cpnTools.accept(port);
                connectedToCPN = true;
                trafficLights.setStatusMessageLabel("connected to CPNTools.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (connectedToCPN) {
                try {
                    result = Decode.decodeString(cpnTools.receive());
                    if (syncCounter < 3) {
                        syncCounter++;
                        trafficLights.setStatusMessageLabel("syncing traffic lights, please wait.");
                    } else if (syncCounter == 3) {
                        syncCounter++;
                        trafficLights.setStatusMessageLabel("traffic lights synced.");
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
                    syncCounter = 0;
                    trafficLights.setStatusMessageLabel("connection lost!");
                    trafficLights.setTrafficLightsToDefault();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'x') {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void menuSelected(MenuEvent e) {
        if (e.getSource().equals(this._fExit)) {
            System.exit(0);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}