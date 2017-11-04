package com.trafficlights;

import com.cpn.CPNTools;
import com.messagehelper.Decode;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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
    private JPanel tfPanel;
    private int syncCounter = 0;
    private boolean applicationRunning = true;
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
    private JMenuItem _hCheckForUpdates;
    private JMenuItem _hHelp;

    public void setConnectedStatusBackground() {
        setBackground(new Color(52, 73, 94));
    }

    public boolean isApplicationRunning() {
        return this.applicationRunning;
    }

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
        this.version = "v1.4.0";
    }

    private String getBuildDate() {
        return buildDate;
    }

    private void setBuildDate() {
        this.buildDate = "October 04, 2017";
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
        this.tfPanel.setBackground(new Color(122, 178, 211));
        setBackground(new Color(231, 76, 60));
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
        this._fOpenClose = new JMenuItem("Open a New Connection");
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

        this._hCheckForUpdates = new JMenuItem("Check For Updates...");
        this._hCheckForUpdates.setMnemonic(KeyEvent.VK_U);
        this._hCheckForUpdates.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        this._hCheckForUpdates.addActionListener(this);
        this.help.add(this._hCheckForUpdates);

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

    private void closeCPNToolsConnection(boolean... connectionLost) {
        /*
        Mouse listener section
         */
        boolean lostConnection;
        String statusMessage;
        if (connectionLost.length > 0) {
            lostConnection = connectionLost[0];
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
        setBackground(new Color(231, 76, 60));
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
            if (this.getSyncCounter() == 0) {
                this.increaseSyncCounter();
                setBackground(new Color(241, 196, 15));
                this.setStatusMessageLabel("syncing traffic lights, please wait.");
            } else if (this.getSyncCounter() < 3) {
                this.increaseSyncCounter();
            } else if (this.getSyncCounter() == 3) {
                this.increaseSyncCounter();
                setBackground(new Color(26, 188, 156));
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

    private void checkForUpdatesFailedMessageDialog() {
        String message = "Failed to communicate with server.\n" +
                "Could not check for new updates.\n\n" +
                "Please check your internet connection.";
        JOptionPane.showMessageDialog(null, message, "Checking For Updates", JOptionPane.ERROR_MESSAGE);
    }

    private void nonModalDialog(String html, String title, int width, int height) {
        //Create the dialog.
        final JDialog dialog = new JDialog();
        dialog.setTitle(title);

        //Add contents to it. It must have a close button,
        //since some L&Fs (notably Java/Metal) don't provide one
        //in the window decorations for dialogs.
        JLabel label = new JLabel(html);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(label.getFont().deriveFont(Font.PLAIN,
                14.0f));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            dialog.setVisible(false);
            dialog.dispose();
        });
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new BoxLayout(closePanel,
                BoxLayout.LINE_AXIS));
        closePanel.add(Box.createHorizontalGlue());
        closePanel.add(closeButton);
        closePanel.setBorder(BorderFactory.
                createEmptyBorder(0,0,5,5));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(label, BorderLayout.CENTER);
        contentPane.add(closePanel, BorderLayout.PAGE_END);
        contentPane.setOpaque(true);
        dialog.setContentPane(contentPane);

        //Show it.
        dialog.setSize(new Dimension(width, height));
        dialog.setLocationRelativeTo(this.MainPanel);
        dialog.setVisible(true);
    }

    private void aboutMessageDialog() {
        String htmlMessage = "<html><p align=center>" + "TrafficPetri " + this.getVersion() + ".<br>" + "Built on " + this.getBuildDate() + ".<br>";
        this.nonModalDialog(htmlMessage, "About", 300, 150);
    }

    private void helpMessageDialog() {
        String address = "https://github.com/andrenoberto/TrafficPetri";
        String addressName = "TrafficPetri";
        String hyperLink = "<a href=\"" + address + "\" target=\"_blank\">" + addressName + "</a>";
        String htmlMessage = "<html>Check out project's github repo " + hyperLink + " to get help.</html>";
        this.nonModalDialog(htmlMessage, "Help", 400, 180);
        //JOptionPane.showMessageDialog(null, new MessageWithLink(htmlMessage), "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openURI(String address) {
        URI uri = URI.create(String.valueOf(address));
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            //StringBuffer buffer = new StringBuffer();
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private static File openFile(int fileSelectionMode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(fileSelectionMode);
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile();
    }

    public void checkForUpdates(boolean runInBackground) {
        JSONObject jsonObject;
        String directory;
        try {
            String repo = "https://api.github.com/repos/andrenoberto/TrafficPetri/releases/latest";
            jsonObject = new JSONObject(readUrl(repo));

            if (jsonObject.get("tag_name").equals(this.getVersion())) {
                if (!runInBackground) {
                    String message = "Everything is up to date.";
                    JOptionPane.showMessageDialog(null, message, "Checking For Updates", JOptionPane.INFORMATION_MESSAGE);
                }
                return;
            } else {
                Object[] options = {"Yes, please",
                        "No, thanks",
                        "No, take me to the website"};
                int n = JOptionPane.showOptionDialog(this.MainPanel,
                        "There's a newer version in our repo available to download.\n" +
                                "Do you want to download it now?",
                        "TrafficPetri Update",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);
                if (n == JOptionPane.YES_OPTION) {
                    try {
                        directory = openFile(JFileChooser.DIRECTORIES_ONLY).toString();
                    } catch (Exception e) {
                        String message = "You haven't selected a folder to save the updated files.\n\n" +
                                "Update canceled!";
                        JOptionPane.showMessageDialog(null, message, "Checking For Updates", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (n == JOptionPane.NO_OPTION) {
                    return;
                } else if (n == JOptionPane.CANCEL_OPTION) {
                    this.openURI("https://github.com/andrenoberto/TrafficPetri/releases/latest");
                    return;
                } else {
                    return;
                }
            }
            JSONArray arrayList = jsonObject.getJSONArray("assets");
            for (int i = 0; i < arrayList.length(); i++) {
                /*
                    Download update
                */
                URL url = new URL(arrayList.getJSONObject(i).getString("browser_download_url"));
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(directory + File.separator + arrayList.getJSONObject(i).getString("name"));
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            }
            String message = "You have successfully downloaded the latest version of TrafficPetri.";
            JOptionPane.showMessageDialog(null, message, "Download Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            if (!runInBackground) {
                this.checkForUpdatesFailedMessageDialog();
            }
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
            this.applicationRunning = false;
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
        if (e.getSource().equals(this._hCheckForUpdates)) {
            this.checkForUpdates(false);
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
            case 'u':
                if (this.help.isSelected()) {
                    this.checkForUpdates(false);
                }
                break;
            case 'x':
                if (this.file.isSelected()) {
                    this.applicationRunning = false;
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
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}