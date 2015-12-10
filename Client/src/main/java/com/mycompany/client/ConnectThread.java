package com.mycompany.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectThread {

    private static volatile ConnectThread instance;
    private final Socket socket;
    private final DataInputStream inputData;
    private final DataOutputStream outputData;
    private final String name;

    private ConnectThread(String ip, int port, String name) throws IOException {
        socket = new Socket(ip, port);
        inputData = (DataInputStream) socket.getInputStream();
        outputData = (DataOutputStream) socket.getOutputStream();
        this.name = name;
    }

    public static ConnectThread init(String ip, int port, String name) throws IOException {
        if (instance != null) {
            instance.close();
        }
        instance = new ConnectThread(ip, port, name);
        return instance;
    }

    public String read() throws IOException{
         int length = inputData.available();
         byte[] buf = new byte[length];
         inputData.readFully(buf);
         return new String(buf);
    }
    public void write(String msg) throws IOException{
        outputData.writeUTF(msg);
    }
    
    private void close() {
        try {
            inputData.close();
            outputData.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
