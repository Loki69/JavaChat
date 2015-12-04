package com.mycompany.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageMessages {

    private Connection connect;
    PreparedStatement select;
    PreparedStatement insert;

    private StorageMessages() throws ClassNotFoundException, SQLException {
        Class.forName("com.h2database");
        connect = DriverManager.getConnection(
                "jdbc:h2:file:/home/mur/NetBeansProjects/ClientServer/DB/DB",
                "sa",
                "");
        select = connect.prepareStatement("select DATA,NAME,MESSAGERS from \"PUBLIC\".MESSAGERS");
        insert = connect.prepareStatement("INSERT INTO MESSAGERS (DATA,NAME,MESSAGERS) VALUES (?,?,?)");
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
        long timeInMilliS= System.currentTimeMillis() % 1000;
        insert.setTime(1, new Time(timeInMilliS));
        insert.setString(3, msg);
        insert.setString(2, name);
        insert.executeUpdate();
    }
    
    public StorageMessages init() throws ClassNotFoundException, SQLException{
        return new StorageMessages();    
    }

}
