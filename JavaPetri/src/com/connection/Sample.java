package com.connection;

import com.cpn.CPNTools;
import com.messagehelper.Decode;

import java.io.IOException;
import java.net.SocketException;

public class Sample {
    private boolean debug = false;
    private CPNTools cpnTools = new CPNTools();
    private boolean connectedToCPN = false;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void init() throws Exception {
        System.out.println("Waiting for a new connection...");
        try {
            int port = 9000;
            this.cpnTools.accept(port);
            this.connectedToCPN = true;
            System.out.println("Connection found...\nCommunication established.");
        } catch (IOException e) {
            if (this.debug) {
                e.printStackTrace();
            }
        }

        while (this.connectedToCPN) {
            String result;
            try {
                result = Decode.decodeString(this.cpnTools.receive());
                System.out.println("Message: " + result);
            } catch (SocketException e) {
                this.cpnTools.disconnect();
                this.connectedToCPN = false;
                System.out.println("Connection lost.");
                if (this.debug) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        /*
        This sample only prints the results sent from CPNTools.
        If you actually want to see the graphical interface, you need
        to compile and execute the binaries.
        This sample is for debug purposes only, it will not affect
        how this application works.
         */
        Sample sample = new Sample();
        sample.setDebug(false);
        sample.init();
    }
}