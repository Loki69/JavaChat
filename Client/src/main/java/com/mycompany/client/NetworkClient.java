package com.mycompany.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkClient {

    private static volatile NetworkClient instance;
    private final Socket socket;
    private final DataInputStream inputData;
    private final DataOutputStream outputData;
    private final String name;

    private NetworkClient(String ip, int port, String name) throws IOException {
        socket = new Socket(ip, port);
        inputData = (DataInputStream) socket.getInputStream();
        outputData = (DataOutputStream) socket.getOutputStream();
        this.name = name;
    }

    public static NetworkClient init(String ip, int port, String name) throws IOException {
        if (instance != null) {
            instance.close();
        }
        instance = new NetworkClient(ip, port, name);
        return instance;
    }

    public static NetworkClient init() throws IOException {
        if (instance != null) {
            return instance;
        }
        new IOException();
        return null;
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
