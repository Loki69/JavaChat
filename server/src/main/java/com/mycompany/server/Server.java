package com.mycompany.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Iterator;

public class Server implements Runnable {

    private final int port;
    private ServerSocketChannel serverSocet;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1050);
    private StorageMessages storeg;

    private Server(int port) throws IOException, ClassNotFoundException, SQLException {
        storeg = StorageMessages.init();
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
        } catch (IOException | SQLException e) {
            System.out.println("Exception\nStack trace:");
            e.printStackTrace();
        }
    }

    private final void listPort() throws IOException, SQLException {
        selector.select();
        Iterator<SelectionKey> stepKey;
        SelectionKey key;
        stepKey = this.selector.selectedKeys().iterator();
        while (stepKey.hasNext()) {
            key = stepKey.next();
            stepKey.remove();
            if (key.isAcceptable()) {
                this.handleAccept(key);
            }
            if (key.isReadable()) {;
                this.handleRead(key);
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_WRITE);
        ByteBuffer msgBuf=ByteBuffer.wrap(storeg.getAllMSG().getBytes());
        sc.write(msgBuf);
    }

    private void write(SelectionKey key, String message) throws IOException {
        if (key.isValid() && key.channel() instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(ByteBuffer.wrap(message.getBytes()));
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private void handleRead(SelectionKey key) throws IOException, SQLException {
        String msg = readMSG(key);
        System.out.println(msg);
        setMSG(msg);
        broadcast(msg);
    }

    private void setMSG(String msg) throws SQLException {
        String[] result = msg.split(":");
        if (result.length > 1) {
            storeg.addMSG(result[0], result[1]);
        } else {
            storeg.addMSG("anonim", msg);
        }

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

    public static Server init(int port) throws IOException, ClassNotFoundException, SQLException {
        return new Server(port);
    }
}
