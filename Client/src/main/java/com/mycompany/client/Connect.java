package com.mycompany.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Connect implements Chat {

    private static volatile Connect instance;
    private final SocketChannel client;

    private Connect(String ip, int port) throws IOException {
        InetSocketAddress hostAddress = new InetSocketAddress(ip, port);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);
    }

    public static Connect init(String ip, int port) throws IOException {
        if (instance != null) {
            instance.close();
        }
        instance = new Connect(ip, port);
        return instance;
    }

    @Override
    public String read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        buffer.clear();
        int read = 0;
        StringBuilder sb = new StringBuilder();
        while ((read = client.read(buffer)) > 0) {
            sb.append(byteBuffToString(buffer));
        }
        return sb.toString();
    }

    private String byteBuffToString(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        buffer.clear();
        return new String(bytes);
    }

    @Override
    public void write(String message) throws IOException {
        System.out.println(client.write(ByteBuffer.wrap(message.getBytes())));
    }

    private void close() {
        try {
            client.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
