package com.mycompany.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    private ConnectThread client = null;
    @FXML
    TextField ip;
    @FXML
    TextField port;
    @FXML
    TextField userName;
    @FXML
    TextArea board;

    @FXML
    private void connectToServer() {
        board.setScrollTop(Double.MAX_VALUE);
        try {
            client = ConnectThread.init(ip.getSelectedText(), Integer.parseInt(port.getText()), userName.getSelectedText());
        } catch (IOException ex) {
            board.setText("invalid server or port");
        }
    }

    private void autoUpdateTextArea() {
        Thread autoUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    updateTextArea();
                }
            }
        });
        autoUpdate.setName("autoUpdateTextArea");
        autoUpdate.start();
    }
    

    private void updateTextArea() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(client != null){
                    try {
                        board.appendText(client.read());
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        });
    }
}
