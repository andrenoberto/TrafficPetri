package com.cpn;

import java.io.*;
import java.net.*;

public class CPNTools implements CPNInterface {
    /**
     * internal reference to the socket input stream
     */
    private InputStream input;
    /**
     * internal reference to the socket output stream
     */
    private OutputStream output;
    /**
     * Internal reference to the socket being used
     */
    private Socket socket;
    /**
     *  Server socket
     */
    private ServerSocket serverSocket;

    /**
     * Constructor to create a new JavaCPN object. Simply initialises the internal
     * references. In order to establish a connection either the <CODE>connect
     * </CODE> or the <CODE>accept</CODE> methods need to be called.
     */
    public CPNTools() {
        this.input = null;
        this.output = null;
        this.socket = null;
    }

    /**
     * Method to actively establish a connection. It takes a
     * host name and port number as arguments, and attempts to establish a
     * connection as a client to the given port on the given host.  Once the
     * connection has been established (i.e. the socket opened) input and
     * output streams are extracted from the socket to enable the transmission
     * and reception of bytes.
     *
     * @param hostName The host to attempt to connect to
     * @param port     The port number to attempt to connect to
     * @throws IOException          Thrown when there is a communication error
     * @throws UnknownHostException Thrown when the host name provided as the argument
     *                              cannot be resolved into an IP address
     */
    @Override
    public void connect(String hostName, int port) throws IOException {
        this.socket = new Socket(InetAddress.getByName(hostName), port);
        this.input = this.socket.getInputStream();
        this.output = this.socket.getOutputStream();
    }

    /**
     * Method to passively open a connection. It takes a port number as an
     * argument and, acting as a server, listens on the given port number for
     * an incoming connection request.  When received, it establishes the
     * connection.  Again, once the connection has been established, input
     * and output streams are extracted from the socket to enable the
     * transmission and reception of bytes.   The method will block until a
     * connection is established.
     *
     * @param port The port number to attempt to connect to
     * @throws IOException Thrown when there is a communication error
     */
    @Override
    public void accept(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socket = serverSocket.accept();
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
    }

    /**
     * Method used to send a ByteArrayInputStream via an established
     * connection. This method takes a ByteArrayInputStream object
     * as the argument.  The segmentation into packets occurs in this method.
     * Bytes are read from the ByteArrayInputStream object, a maximum
     * of 127 at a time, and a single byte header is added indicating the
     * number of payload bytes (header is 1 to 127) or that there is more data
     * in a following packet (header is 255). The data packets formed are then
     * transmitted to the external process through methods acting on the
     * output stream of the socket.
     *
     * @param sendBytes The byte stream to be sent to the receiving end of the connection
     * @throws SocketException Thrown if there is a problem sending the byte stream
     */
    @Override
    public void send(ByteArrayInputStream sendBytes) throws SocketException {
        //A byte array representing a data packet
        byte[] packet;
        //While there are more than 127 bytes still to send...
        while (sendBytes.available() > 127) {
            //...create a 128 byte packet...
            packet = new byte[128];
            //...set the header to 255...
            packet[0] = (byte) 255;
            //...read 127 bytes from the sequence of bytes to send...
            sendBytes.read(packet, 1, 127);
            //...and send the packet to the external process.
            try {
                this.output.write(packet);
                this.output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Create a packet for any remaining data
        packet = new byte[sendBytes.available() + 1];
        //Set the header appropriately
        packet[0] = (byte) (sendBytes.available());
        //Read the remaining bytes into the packet
        sendBytes.read(packet, 1, sendBytes.available());
        //Send the packet to the external process
        try {
            this.output.write(packet);
            this.output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to receive a ByteArrayOutputStream from an established
     * connection. This method has no arguments.  It uses methods that
     * act on the input stream of the socket to firstly receive a header
     * byte, and then receive the number of payload bytes specified in the
     * header, from the external process.  The payload bytes are stored in a
     * ByteArrayOutputStream object as each segment of payload data is
     * received. This process is repeated until all data has been received for
     * the current transmission.
     *
     * @return sendBytes The byte stream received from the other end of the connection
     * @throws SocketException Thrown if there is a problem sending the byte stream
     */
    @Override
    public ByteArrayOutputStream receive() throws SocketException {
        // The complete sequence of bytes received from the external process
        ByteArrayOutputStream receivedBytes = new ByteArrayOutputStream();
        // The header received from the external process
        int header = -1;
        //The number of payload bytes received from the external process for a packet
        int numberRead = 0;
        //The total number of payload bytes received from the external process for
        //a packet, if not all are received immediately.
        int totalNumberRead;
        //The payload received from the external process for each packet
        byte[] payload;
        //Sets timeout to 180 seconds if none response has been received
        //this.socket.setSoTimeout(180000);
        while (true) {
            //Read a header byte from the input stream
            try {
                //header = (int) this.input.read(); //this is probably a redundant cast
                header = this.input.read();
            } catch (SocketException e) {
                throw new SocketException("Socket closed while blocking to receive header.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //If the header shows that the socket has closed...
            if (header == -1) // ...throw a SocketException
            {
                throw new SocketException("Socket closed by external process.");
            } //If the header indicates another packet to follow...
            else if (header >= 127) {
                //...create 127 bytes of payload storage...
                payload = new byte[127];
            } //...else create storage of the appropriate size
            else {
                payload = new byte[header];
            }
            //Read the payload bytes from the input stream
            //Reset the total bytes received to 0 for this iteration
            totalNumberRead = 0;
            //Loop until all data has been read for this packet.
            while (totalNumberRead < payload.length && numberRead != -1) {
                try {
                    //Try to read all bytes in this packet
                    numberRead = this.input.read(payload, totalNumberRead, payload.length - totalNumberRead);

                    //If some bytes were read ...
                    if (numberRead != -1) // ... record this many bytes as having been read
                    {
                        totalNumberRead = totalNumberRead + numberRead;
                    }

                } catch (SocketException e) {
                    throw new SocketException("Socket closed while receiving data.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //If not all bytes could be read ...
            if ((totalNumberRead < header || numberRead == -1) && header != 255) // ... throw a SocketException
            {
                throw new SocketException("Error receiving data.");
            }
            // Write the payload data to the complete sequence of received bytes
            receivedBytes.write(payload, 0, payload.length);
            //If no more bytes to follow, break from the loop.
            if (header <= 127) {
                break;
            }
        }
        //Return the received bytes
        return receivedBytes;
    }

    /**
     * Method to disconnect the established connection. This method has no
     * arguments, and returns no value.  It closes the input and output
     * streams from the socket before closing the socket itself.
     *
     * @throws IOException if there is a problem closing the connection
     */
    @Override
    public void disconnect() throws IOException {
        this.input.close();
        this.output.close();
        this.socket.close();
        this.serverSocket.close();
    }
}
