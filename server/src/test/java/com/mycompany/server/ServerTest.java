package com.mycompany.server;

import java.io.IOException;
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
    
}
