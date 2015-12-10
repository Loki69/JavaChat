package com.mycompany.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientChat implements Chat {

    private String name;
    private Connect connect;

    public ClientChat(String ip, int port, String name) throws IOException {
        connect = Connect.init(ip, port);
        this.name = name;
    }

    @Override
    public String read() throws IOException {
        return connect.read();
    }

    @Override
    public void write(String message) throws IOException {
        connect.write(new StringBuilder(name).append(":").append(message).toString());
    }

}
