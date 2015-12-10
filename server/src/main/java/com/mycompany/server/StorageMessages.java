package com.mycompany.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageMessages {

    private Connection connect;
    PreparedStatement select;
    PreparedStatement insert;

    private StorageMessages() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        connect = DriverManager.getConnection(
                "jdbc:h2:file:/home/loki/workflow/Java/ClientServer/DB/DB",
                "sa",
                "");
        select = connect.prepareStatement("select DATE,NAME,MESSAGERS from \"PUBLIC\".MESSAGERS ORDER BY ID");
        insert = connect.prepareStatement("INSERT INTO MESSAGERS (NAME,MESSAGERS) VALUES (?,?)");
    }
    
    public String getAllMSG(){
        StringBuilder buildMSG = new StringBuilder("");
         try(ResultSet result =  select.executeQuery()){
             while(result.next()){
                 buildMSG.append("[").append(result.getString(1)).append("] ").
                        append(result.getString(2)).append(": ").
                        append(result.getString(3)).append("\n");
             }
         } catch (SQLException ex) {
            Logger.getLogger(StorageMessages.class.getName()).log(Level.SEVERE, null, ex);
        }
         return buildMSG.toString();
    }
    
    public void addMSG(String name,String msg) throws SQLException{
        insert.setString(1, name);
        insert.setString(2, msg);
        insert.executeUpdate();
    }
    
    public static StorageMessages init() throws ClassNotFoundException, SQLException{
        return new StorageMessages();    
    }

}
