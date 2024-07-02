package io.example.JavaFX.fxController;

import io.example.Application;
import io.example.WebSocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class WelcomeController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void chooseMode(ActionEvent event) throws IOException, ExecutionException, InterruptedException {
        Button button = (Button) event.getSource();

        if (button.getId().equals("multi")) {
            WebSocketClient webSocketClient = Application.getWebsocketClient();
            webSocketClient.connect();
        }

        Stage stage = new Stage();

        Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/chooseGame.fxml")));
//        Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/preparation.fxml")));

        Scene scene = new Scene(Application.getFxmlLoader().load(), 360, 420);
        stage.setScene(scene);

        stage.show();
        button.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {

    }

}
