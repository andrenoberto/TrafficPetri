package com.connection;

import com.cpn.CPNTools;
import com.messagehelper.Encode;

import java.io.IOException;

public class Server {
    public CPNTools cpnTools;
    public CPNTools cpnField;
    private String hostName;
    private int inputPort;
    private int outputPort;
    public String message;

    public Server(String hostName, int inputPort, int outputPort) {
        this.cpnTools = new CPNTools();
        this.cpnField = new CPNTools();
        this.hostName = hostName;
        this.inputPort = inputPort;
        this.outputPort = outputPort;
    }

    //In case there's no arguments when registering a new object we'll call default values
    public Server() {
        this.cpnTools = new CPNTools();
        this.cpnField = new CPNTools();
        this.hostName = "127.0.0.1";
        this.inputPort = 9000;
        this.outputPort = 9000;
    }

    public CPNTools getCpnTools() {
        return cpnTools;
    }

    public boolean connect() {
        try {
            System.out.println("Starting a new connection with " + this.hostName + " at " + this.outputPort);
            cpnTools.connect(this.hostName, this.outputPort);
            System.out.println("Connection successfully established!");
        } catch (IOException e) {
            System.err.println("Connection could not established!");
            return false;
        }
        try {
            System.out.println("Server running at " + this.outputPort);
            this.cpnField.accept(this.outputPort);
            System.out.println("Conexao Aberta com sucesso - Server");
            return true;
        } catch (IOException e) {
            System.err.println("Connection could not be established!");
            return false;
        }
    }
    /**
     * Send the message that corresponds to the activated key
     *
     * @param key String that holds panel's name and key's name
     */
    public void send(String key) {
        System.out.println("Sending message..");
        try {

            this.cpnTools.send(Encode.encodeString(key));
            System.out.println("Message sent: " + key);
        } catch (IOException e) {
            System.out.println("Connection interrupted...");
        }
    }
}
