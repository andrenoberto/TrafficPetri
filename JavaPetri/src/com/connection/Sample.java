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
    public static void main(String[] args) {
        int port = 9000;
        boolean connectedToCPN = false;
        CPNTools cpnTools = new CPNTools();
        try {
            cpnTools.accept(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        while (true) {
            try {
                result = Decode.decodeString(cpnTools.receive());
                System.out.println(result);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}
