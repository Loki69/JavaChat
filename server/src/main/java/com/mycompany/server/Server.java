package com.mycompany.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {

    private final int port;
    private ServerSocketChannel serverSocet;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1050);

    private Server(int port) throws IOException {
        this.port = port;
        this.serverSocet = ServerSocketChannel.open();
        this.serverSocet.socket().bind(new InetSocketAddress(port));
        this.serverSocet.configureBlocking(false);
        this.selector = Selector.open();

        this.serverSocet.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        System.out.println("Server run on port " + this.port);
        try {
            while (this.serverSocet.isOpen()) {
                listPort();
            }
        } catch (IOException e) {
            System.out.println("IOException\nStack trace:");
            e.printStackTrace();
        }
    }

    private final void listPort() throws IOException {
        selector.select();
        Iterator<SelectionKey> stepKey;
        SelectionKey key;
        stepKey = this.selector.selectedKeys().iterator();
        while (stepKey.hasNext()) {
            key = stepKey.next();
            stepKey.remove();
            if (key.isAcceptable()) {
                this.handleAccept(key);
//                write(key, "Привет мир!\n");
            }
            if (key.isReadable()) {;
                this.handleRead(key);
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    private void write(SelectionKey key, String message) throws IOException {
        if (key.isValid() && key.channel() instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(ByteBuffer.wrap(message.getBytes()));
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        String msg = readMSG(key);
        broadcast(msg);
    }

    private String readMSG(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        buffer.clear();
        int read = 0;
        StringBuilder sb = new StringBuilder();
        while ((read = ch.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            sb.append(new String(bytes));
            buffer.clear();
        }
        key.interestOps(SelectionKey.OP_WRITE);
        if (read < 0) {
            sb.append("exit");
            ch.close();
        }
        return sb.toString();
    }

    private void broadcast(String msg) throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                write(key, msg);
            }
        }
    }

    public static Server init(int port) throws IOException {
        return new Server(port);
    }
}
