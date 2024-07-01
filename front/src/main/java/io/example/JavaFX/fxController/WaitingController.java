package io.example.JavaFX.fxController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.Application;
import io.example.JavaFX.SharedData;
import io.example.WebSocketClient;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WaitingController {

    @FXML
    private Label readyLabel;

    @FXML
    private Button exitButton;

    private SharedData sharedData = SharedData.getInstance();

    private WebSocketClient webSocketClient = Application.getWebsocketClient();

    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (sharedData.getConnections() == sharedData.getNumberOfPlayers()) {
                readyLabel.setText(sharedData.getOpponents().size() + 1 + "/" + sharedData.getNumberOfPlayers());
            } else {
                readyLabel.setText(sharedData.getConnections() + "/" + sharedData.getNumberOfPlayers());
            }
        }
    };

    @FXML
    void initialize() {
        animationTimer.start();
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        Map<String, Object> msg = new HashMap<>();
        if (Application.getFxmlLoader().getController() instanceof ChooseGameController) {
            msg.put("leave", null);
        } else {
            msg.put("disconnection", null);
        }
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(msg);
        webSocketClient.sendMessage(json);

        sharedData.clean();

        Stage stage = new Stage();
        Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/chooseGame.fxml")));
        Scene scene = new Scene(Application.getFxmlLoader().load(), 360, 420);
        exitButton.getScene().getWindow().hide();

        stage.setResizable(false);
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.show();
    }
}
