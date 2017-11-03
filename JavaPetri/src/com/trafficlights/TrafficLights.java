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
    private String version;
    private String buildDate;
    private String dataReceivedResult;
    private String pathToTrafficLightOneIcon;
    private String pathToTrafficLightTwoIcon;
    private CPNTools cpnTools = null;
    private JPanel MainPanel;
    private JLabel trafficLightOne;
    private JLabel trafficLightTwo;
    private JLabel statusMessageLabel;
    private JLabel statusLabel;
    private JToolBar statusBar;
    private int syncCounter = 0;
    private boolean connectedToCPN = false;
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
    private JMenuItem _fOpenClose;
    private JMenuItem _fExit;
    private JMenuItem _hAbout;
    private JMenuItem _hContribute;
    private JMenuItem _hReportABug;
    private JMenuItem _hHelp;

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JMenuItem get_fOpenClose() {
        return _fOpenClose;
    }

    private void setStatusBarToolTipText() {
        this.statusBar.setToolTipText("Displays the current status of the connection.");
    }

    private void setStatusLabel() {
        this.statusLabel.setText("Status: ");
    }

    private void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    private String getVersion() {
        return version;
    }

    private void setVersion() {
        this.version = "v1.2.0";
    }

    private String getBuildDate() {
        return buildDate;
    }

    private void setBuildDate() {
        this.buildDate = "October 03, 2017";
    }

    private String getDataReceivedResult() {
        return dataReceivedResult;
    }

    private void setDataReceivedResult(String dataReceivedResult) {
        this.dataReceivedResult = dataReceivedResult;
    }

    public TrafficLights(boolean... addMouseListener) {
        this.setVersion();
        this.setBuildDate();
        this.setStatusLabel();
        this.setStatusBarToolTipText();
        this.cpnTools = new CPNTools();
        setTitle("TrafficPetri: Colored Petri Net - Traffic Lights");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Color bgColor = new Color(122, 178, 211);
        setBackground(bgColor);
        /*
            Toolbar section
         */
        this.addKeyListener(this);
        this.setMenuBar(new JMenuBar());
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
        this._fOpenClose = new JMenuItem("Open/Close a Connection");
        this._fOpenClose.setMnemonic(KeyEvent.VK_O);
        this._fOpenClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        this._fOpenClose.addActionListener(this);
        this.file.add(this._fOpenClose);

        this.file.addSeparator();

        this._fExit = new JMenuItem("Exit");
        this._fExit.setMnemonic(KeyEvent.VK_X);
        this._fExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        this._fExit.addActionListener(this);
        this.file.add(this._fExit);
        /*
            Help
         */
        this._hAbout = new JMenuItem("About");
        this._hAbout.setMnemonic(KeyEvent.VK_A);
        this._hAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        this._hAbout.addActionListener(this);
        this.help.add(this._hAbout);

        this._hContribute = new JMenuItem("Contribute");
        this._hContribute.setMnemonic(KeyEvent.VK_C);
        this._hContribute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        this._hContribute.addActionListener(this);
        this.help.add(this._hContribute);

        this._hReportABug = new JMenuItem("Report a Bug");
        this._hReportABug.setMnemonic(KeyEvent.VK_R);
        this._hReportABug.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        this._hReportABug.addActionListener(this);
        this.help.add(this._hReportABug);

        this.help.addSeparator();

        this._hHelp = new JMenuItem("Help");
        this._hHelp.setMnemonic(KeyEvent.VK_F1);
        this._hHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
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

    public CPNTools getCPNToolsObject() {
        return this.cpnTools;
    }

    private void increaseSyncCounter() {
        this.syncCounter++;
    }

    private int getSyncCounter() {
        return this.syncCounter;
    }

    private void resetSyncCounter() {
        this.syncCounter = 0;
    }

    public boolean getConnectedToCPN() {
        return this.connectedToCPN;
    }

    public void setConnectedToCPN(boolean connectedToCPN) {
        this.connectedToCPN = connectedToCPN;
    }

    private void closeCPNToolsConnection(boolean ... connectionLost) {
        /*
        Mouse listener section
         */
        boolean lostConnection;
        String statusMessage;
        if (connectionLost.length > 0) {
            lostConnection = connectionLost[0];
            System.out.println(connectionLost[0]);
            if (lostConnection) {
                statusMessage = "connection lost. Waiting for a new connection.";
            } else {
                statusMessage = "connection successfully closed. Waiting for a new connection.";
            }
        } else {
            statusMessage = "connection successfully closed. Waiting for a new connection.";
        }
        try {
            if (!this.getConnectedToCPN()) {
                return;
            }
            this.cpnTools.disconnect();
            this._fOpenClose.setEnabled(false);
            this.setConnectedToCPN(false);
            this.resetSyncCounter();
            this.setStatusMessageLabel(statusMessage);
            this.setTrafficLightsToDefault();
        } catch (IOException e1) {
            this.setStatusMessageLabel("could not close the socket. Please restart this application.");
        }
    }

    private void setTrafficLights(String pathToTrafficLightOneIcon, String pathToTrafficLightTwoIcon) {
        this.setTrafficLightOne(pathToTrafficLightOneIcon);
        this.setTrafficLightTwo(pathToTrafficLightTwoIcon);
    }

    public void communicateWithCPNTools() {
        if (this.cpnTools == null) {
            return;
        }
        try {
            this.setDataReceivedResult(Decode.decodeString(this.cpnTools.receive()));
            if (this.getSyncCounter() < 3) {
                this.increaseSyncCounter();
                this.setStatusMessageLabel("syncing traffic lights, please wait.");
            } else if (this.getSyncCounter() == 3) {
                this.increaseSyncCounter();
                this.setStatusMessageLabel("traffic lights synced.");
            } else {
                this.setStatusMessageLabel("receiving data.");
            }
            switch (this.getDataReceivedResult()) {
                case "green1":
                    this.pathToTrafficLightOneIcon = "images/greenIsOn.png";
                    break;
                case "yellow1":
                    this.pathToTrafficLightOneIcon = "images/yellowIsOn.png";
                    break;
                case "red1":
                    this.pathToTrafficLightOneIcon = "images/redIsOn.png";
                    break;
                case "green2":
                    this.pathToTrafficLightTwoIcon = "images/greenIsOn.png";
                    break;
                case "yellow2":
                    this.pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
                    break;
                case "red2":
                    this.pathToTrafficLightTwoIcon = "images/redIsOn.png";
                    break;
                default:
                    this.pathToTrafficLightOneIcon = "images/yellowIsOn.png";
                    this.pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
                    break;
            }
            if (this.pathToTrafficLightOneIcon != null && this.pathToTrafficLightTwoIcon != null) {
                this.setTrafficLights(this.pathToTrafficLightOneIcon, this.pathToTrafficLightTwoIcon);
            }
        } catch (SocketException e) {
            this.closeCPNToolsConnection(true);
        }
    }

    private void setTrafficLightOne(String pathToIcon) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
        trafficLightOne.setIcon(imageIcon);
    }

    private void setTrafficLightTwo(String pathToIcon) {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(pathToIcon));
        trafficLightTwo.setIcon(imageIcon);
    }

    public void setTrafficLightsToDefault() {
        String pathToTrafficLightOneIcon = "images/yellowIsOn.png";
        String pathToTrafficLightTwoIcon = "images/yellowIsOn.png";
        this.setTrafficLightOne(pathToTrafficLightOneIcon);
        this.setTrafficLightTwo(pathToTrafficLightTwoIcon);
    }

    public void setStatusMessageLabel(String statusMessageLabel) {
        this.statusMessageLabel.setText(statusMessageLabel);
    }

    private void aboutMessageDialog() {
        String message = "TrafficPetri " + this.getVersion() + ", built on " + this.getBuildDate() + ".";
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
        if (e.getSource().equals(this._fOpenClose)) {
            this.closeCPNToolsConnection(false);
        }
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
            case 'o':
                if (this.file.isSelected()) {
                    this.closeCPNToolsConnection(false);
                }
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