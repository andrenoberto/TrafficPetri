package com.connection;

import com.cpn.CPNTools;
import com.messagehelper.Decode;
import com.messagehelper.Encode;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sample {
    private String hostName = "127.0.0.1";
    private int inputPort = 9000;
    private int outputPort = 59936;
    private CPNTools cpnInput = new CPNTools();
    private CPNTools cpnOutput = new CPNTools();
    private ArrayList<Packet> bag = new ArrayList<Packet>();
    private boolean sentAndReceived = true;
    private boolean connectedToCPN = false;

    public void init() {
        try {
            System.out.println("Trying to establish a new connection...");
            this.cpnInput.accept(inputPort);
            cpnOutput.connect(hostName, outputPort);
            System.out.println("Successfully connected!");
        } catch (IOException e) {
            Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void sendToCPN(ArrayList<Packet> bag) {
        this.sentAndReceived = false;

        try {
            System.out.println("Sending data...");
            for (Packet packet : bag) {
                this.cpnOutput.send(Encode.encodeString(String.valueOf(packet.id)));
                this.cpnOutput.send(Encode.encodeString(String.valueOf(packet.value)));
                System.out.println("Message sent\nid: {" + packet.id + "},\nvalue: {" + packet.value + "}");
            }
            System.out.println("Awaiting for data...");
            String statusNumber = Decode.decodeString(this.cpnInput.receive());
            System.out.println("Status: " + statusNumber);
        } catch (SocketException ex) {
            Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    public void messageReceived() {
        int id = 1;
        int value = 1;

        if (this.sentAndReceived && this.connectedToCPN) {
            Packet packet = new Packet(id, value);
            if (!bag.contains(packet)) {
                bag.add(packet);
            }
            if (bag.size() == 1) {
                new Thread() {
                    @Override
                    public void run() {
                        sendToCPN((ArrayList<Packet>) bag.clone());
                        bag.clear();
                    }
                }.start();
            }
        }
    }

    public static void main(String[] args) {
        Sample sample = new Sample();
        sample.init();
    }
}
