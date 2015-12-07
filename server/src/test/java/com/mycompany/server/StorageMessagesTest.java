package com.mycompany.server;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class StorageMessagesTest {
    
    public StorageMessagesTest() {
    }

    /**
     * Test of getAllMSG method, of class StorageMessages.
     */
    @Test
    public void testGetAllMSG() {
        System.out.println("getAllMSG");
        StorageMessages instance = null;
        try {
            instance = StorageMessages.init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StorageMessagesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(StorageMessagesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String result = instance.getAllMSG();
        System.out.println(result);
    }

    /**
     * Test of addMSG method, of class StorageMessages.
     */
    @Test
    public void testAddMSG() throws Exception {
        System.out.println("addMSG");
        String name = "Loki";
        String msg = "tratata";
        StorageMessages instance = StorageMessages.init();
        instance.addMSG(name, msg);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class StorageMessages.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        StorageMessages instance = null;
        try {
            instance = StorageMessages.init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StorageMessagesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(StorageMessagesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertNotNull(instance);
    }
    
}
