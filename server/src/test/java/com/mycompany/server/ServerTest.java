package com.mycompany.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import sun.net.www.protocol.http.AuthCacheValue;

public class ServerTest {

    public ServerTest() {
    }

    @Test
    public void testInit() {
        try {
            Server.init(1054);
        } catch (IOException ex) {
            new AssertionError();
        }

    }

    @Test
    public void testMultiConnect() {
        try {
            new Thread(Server.init(1054)).start();
            Thread.sleep(5000);
            new Thread(new SimpleClient("arad")).start();
            multiConnect();
        } catch (IOException ex) {
            new AssertionError();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void multiConnect() throws IOException {
        while (true) {
            InetSocketAddress hostAddress = new InetSocketAddress(1054);
            SocketChannel client = SocketChannel.open(hostAddress);
            String[] messages = new String[]{"ara", "WTF?", "Bye."};
            for (int i = 0; i < messages.length; i++) {
                byte[] message = new String(messages[i]).getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(message);
                client.write(buffer);
                buffer.clear();
            }
            client.close();
        }
    }
}