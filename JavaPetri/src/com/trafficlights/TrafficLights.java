package com.trafficlights;

import com.cpn.CPNTools;
import com.messagehelper.Decode;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;

public class TrafficLights extends JFrame implements KeyListener, ActionListener, MenuListener {
    private String version = "v1.0.2";
    private String buildDate = "October 03, 2017";
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
    private JMenuItem _hContribute;
    private JMenuItem _hReportABug;
    private JMenuItem _hHelp;

    private TrafficLights(boolean ... addMouseListener) {
        setTitle("TrafficPetri: Colored Petri Net - Traffic Lights");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Color bgColor = new Color(122, 178, 211);
        setBackground(bgColor);
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
        this._hAbout.setMnemonic(KeyEvent.VK_A);
        this._hAbout.addActionListener(this);
        this.help.add(this._hAbout);

        this._hContribute = new JMenuItem("Contribute");
        this._hContribute.setMnemonic(KeyEvent.VK_C);
        this._hContribute.addActionListener(this);
        this.help.add(this._hContribute);

        this._hReportABug = new JMenuItem("Report a Bug");
        this._hReportABug.setMnemonic(KeyEvent.VK_R);
        this._hReportABug.addActionListener(this);
        this.help.add(this._hReportABug);

        this._hHelp = new JMenuItem("Help");
        this._hHelp.setMnemonic(KeyEvent.VK_F1);
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
                        connectedToCPN = false;
                        syncCounter = 0;
                        trafficLights.setStatusMessageLabel("connection lost!");
                        trafficLights.setTrafficLightsToDefault();
                    } catch (IOException e1) {
                        trafficLights.setStatusMessageLabel("connection lost! Could not close the socket. Please restart this application.");
                    }
                }
            }
        }
    }

    private void aboutMessageDialog() {
        String message = "TrafficPetri " + this.version + ", built on " + this.buildDate + ".";
        JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void helpMessageDialog() {
        String address = "https://github.com/andrenoberto/TrafficPetri";
        String addressName = "TrafficPetri";
        String hyperLink = "<a href=\"" + address + "\" target=\"_blank\">" + addressName + "</a>";
        String htmlMessage = "<html>Check out project's github page " + hyperLink + " to get help.</html>";
        JOptionPane.showMessageDialog(null, new MessageWithLink(htmlMessage), "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openURI(String address) {
        URI uri = URI.create(String.valueOf(address));
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
            File
         */
        if (e.getSource().equals(this._fExit)) {
            System.exit(0);
        }
        /*
            Help
         */
        if (e.getSource().equals(this._hAbout)) {
            this.aboutMessageDialog();
        }
        if (e.getSource().equals(this._hContribute)) {
            this.openURI("https://github.com/andrenoberto/TrafficPetri/pulls");
        }
        if (e.getSource().equals(this._hReportABug)) {
            this.openURI("https://github.com/andrenoberto/TrafficPetri/issues");
        }
        if (e.getSource().equals(this._hHelp)) {
            this.helpMessageDialog();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                if (this.help.isSelected()) {
                    this.aboutMessageDialog();
                }
                break;
            case 'c':
                if (this.help.isSelected()) {
                    this.openURI("https://github.com/andrenoberto/TrafficPetri/pulls");
                }
                break;
            case KeyEvent.VK_F1:
                if (this.help.isSelected()) {
                    this.helpMessageDialog();
                }
                break;
            case 'f':
                this.file.doClick();
                break;
            case 'h':
                this.help.doClick();
                break;
            case 'r':
                if (this.help.isSelected()) {
                    this.openURI("https://github.com/andrenoberto/TrafficPetri/issues");
                }
                break;
            case 'x':
                if (this.file.isSelected()) {
                    System.exit(0);
                }
                break;
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