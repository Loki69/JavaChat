package com.mycompany.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        if(args.length > 0){
        try {
            new Thread(Server.init(Integer.parseInt(args[0]))).start();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            System.err.println(ex.getMessage());
        }
        }else{
            System.out.println("port init wos not set");
        }
    }
}
