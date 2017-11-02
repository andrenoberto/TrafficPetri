package com.connection;

import com.cpn.CPNTools;
import com.messagehelper.Decode;

import java.io.IOException;
import java.net.SocketException;

public class Sample {
    public CPNTools cpnTools = new CPNTools();
    public boolean connectedToCPN = false;
    public int port = 9000;

    public void init() throws Exception {
        System.out.println("Waiting for a new connection...");
        try {
            this.cpnTools.accept(this.port);
            this.connectedToCPN = true;
            System.out.println("Connection found...\nEstablishing communication...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (this.connectedToCPN) {
            String result = "";
            try {
                result = Decode.decodeString(this.cpnTools.receive());
                System.out.println("Message: " + result);
            } catch (SocketException e) {
                e.printStackTrace();
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
        sample.init();
    }
}


