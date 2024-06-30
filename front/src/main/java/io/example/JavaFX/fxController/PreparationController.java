package io.example.JavaFX.fxController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.JavaFX.Application;
import io.example.JavaFX.SharedData;
import io.example.JavaFX.fxController.FXController;
import io.example.WebSocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreparationController extends FXController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<Character> roleList;

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField username;

    private SharedData sharedData = SharedData.getInstance();

    private WebSocketClient webSocketClient = Application.getWebsocketClient();

    private ObjectMapper om = new ObjectMapper();

    private Stage newStage;

    @FXML
    void initialize() throws IOException {
        int numberOfPlayers = sharedData.getNumberOfPlayers();

        roleList.getItems().addAll('X', '0');
        if (numberOfPlayers >= 3) {
            roleList.getItems().add('#');
        }
        if (numberOfPlayers >= 4) {
            roleList.getItems().add('$');
        }
        if (numberOfPlayers == 5) {
            roleList.getItems().add('@');
        }
    }

    @FXML
    void startGame(ActionEvent event) throws IOException {
        List<Map<String, Object>> opponents = sharedData.getOpponents();

        boolean isRoleTaken = opponents.stream()
                .anyMatch(opponent -> opponent.get("role").equals(roleList.getValue()));
        if (isRoleTaken) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Эта роль уже занята", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (roleList.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Выберите роль", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        boolean isNickTaken = opponents.stream()
                .anyMatch(opponent -> opponent.get("nick").equals(this.username.getText()));
        if (isNickTaken) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Этот ник уже взяли", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Придумайте ник", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String nick = this.username.getText();
        char selectedRole = roleList.getValue();

        sendMsgToRemoteServer(selectedRole, nick);

        sharedData.setUsername(nick);
        sharedData.setRole(selectedRole);

        newStage = new Stage();
        changeScene();

        startButton.getScene().getWindow().hide();
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        sendMsgToRemoteServer("disconnection");
        sharedData.clean();
        changeScene();
    }

    public void sendMsgToRemoteServer(char role, String nick) throws JsonProcessingException {
        Map<String, Map<String, Object>> message = new HashMap<>();
        Map<String, Object> player = new HashMap<>();
        player.put("role", role);
        player.put("nick", nick);
        message.put("opponent", player);

        String json = om.writeValueAsString(message);
        webSocketClient.sendMessage(json);
    }

    public void sendMsgToRemoteServer(String key) throws JsonProcessingException {
        Map<String, Object> msg = new HashMap<>();
        msg.put(key, null);

        String json = om.writeValueAsString(msg);
        webSocketClient.sendMessage(json);
    }

    @Override
    public void updateInterface(String message) {
        try {
            Map<String, Object> rowMsg = om.readValue(message, new TypeReference<Map<String, Object>>() { });
            if (rowMsg.containsKey("disconnection")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "One or more players have disconnected from the game", ButtonType.CLOSE);
                    alert.getDialogPane().setPrefWidth(400);
                    alert.showAndWait();
                    sharedData.clean();
                    try {
                        changeScene();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            if (!rowMsg.containsKey("opponent")) {
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Map<String, Object>> msg;
        try {
            msg = om.readValue(message, new TypeReference<Map<String, Map<String, Object>>>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
            Map<String, Object> opponent = new HashMap<>();

            for (Map.Entry<String, Object> entry : msg.get("opponent").entrySet()) {
                if (entry.getKey().equals("role")) {
                    char role = ((String) entry.getValue()).charAt(0);
                    opponent.put("role", role);
                    if (newStage == null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Оппонет вбрыл - " + role, ButtonType.OK);
                        alert.showAndWait();
                    }
                } else if (entry.getKey().equals("nick")) {
                    String nick = (String) entry.getValue();
                    opponent.put("nick", nick);
                }
            }
            sharedData.setOpponent(opponent);
            try {
                changeScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void changeScene() throws IOException {
        Scene scene = null;

        if (sharedData.getOpponents().size() == sharedData.getNumberOfPlayers() - 1 && newStage != null) {
            Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/bigMain.fxml")));
            scene = new Scene(Application.getFxmlLoader().load(), 1100, 760);
        } else if (newStage != null && sharedData.getGameName() != null) {
            FXMLLoader waitingFXMLLoader = new FXMLLoader(Application.class.getResource("/waiting.fxml"));
            waitingFXMLLoader.setController(new WaitingController());
            scene = new Scene(waitingFXMLLoader.load(), 360, 420);
        } else if (sharedData.getGameName() == null) {
            if (newStage == null) {
                newStage = new Stage();
            }
            Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/chooseGame.fxml")));
            scene = new Scene(Application.getFxmlLoader().load(), 360, 420);
            startButton.getScene().getWindow().hide();
        } else {
            return;
        }
        newStage.setResizable(false);
        newStage.setTitle("Tic-Tac-Toe");
        newStage.setScene(scene);
        newStage.show();
    }



    @Override
    public String toString() {
        return this.getClass().toString();
    }
}
