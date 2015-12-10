package com.mycompany.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    private Chat client = null;

    @FXML
    TextField ip;
    @FXML
    TextField port;
    @FXML
    TextField userName;
    @FXML
    TextArea board;
    @FXML
    TextField message;

    @FXML
    private void send() {
        try {
            client.write(message.getText());
            message.clear();
        } catch (IOException ex) {
            System.out.println("IO");
        }catch(NullPointerException ex){
            System.out.println("null");
        }
    }

    @FXML
    private void connectToServer() {
        board.setScrollTop(Double.MAX_VALUE);
        try {
            client = new ClientChat(ip.getText(),
                    Integer.parseInt(port.getText()),
                    userName.getText());
        } catch (IOException ex) {
            board.setText("invalid server or port");
        }
        autoUpdate();
    }

    private void autoUpdate() {
        Thread autoUpdate = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {

                    }
                    update();
                }
            }
        });
        autoUpdate.setName("autoUpdateTextArea");
        autoUpdate.start();
    }

    private void update() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    try {
                        board.appendText(client.read());
                    } catch (IOException ex) {
                        System.out.println("her");
                    }
                }
            }
        });
    }
}
