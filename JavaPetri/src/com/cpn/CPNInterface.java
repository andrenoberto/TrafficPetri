package com.cpn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;

public interface CPNInterface {
    //Actively establishes a connection within an external process
    public void connect(String hostName, int port) throws IOException;

    //Passively establishes a connection within an external process
    public void accept(int port) throws IOException;

    //Sends data to an external process
    public void send(ByteArrayInputStream sendBytes) throws SocketException;

    //Receives data from an external process
    public ByteArrayOutputStream receive() throws SocketException;

    //Closes a connection to an external process
    public void disconnect() throws IOException;
}
