package com.mycompany.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {

    private NetworkClient client = null;
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
        try {
            client = NetworkClient.init(ip.getSelectedText(), Integer.parseInt(port.getText()), userName.getSelectedText());
        } catch (IOException ex) {
            board.setText("invalid server or port");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    board.setText(board.getText() + "helo\n");
                }
            }
        }).start();
    }

//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.setText("halo");
    }
}
