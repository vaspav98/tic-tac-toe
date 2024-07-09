package io.example.JavaFX.fxController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.example.Application;
import io.example.JavaFX.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseGameController extends FXController {

    @FXML
    private TextField gameName;

    @FXML
    private GridPane gamesTable;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio3;

    @FXML
    private RadioButton radio4;

    private ToggleGroup toggleGroup;

    private int freeRow = 0;
    private Stage newStage = new Stage();

    @FXML
    void createGame(ActionEvent event) throws IOException {
        if (gamesTable.getChildren().size() >= 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Уже создано максимум игр", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String gameName = this.gameName.getText();
        if (this.gameName.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Введите название игры", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        for (Node button : gamesTable.getChildren()) {
            String createdGameTitle = ((Button) button).getText().split(" ")[0];
            if (createdGameTitle.equals(gameName)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Игра с таким названием уже есть", ButtonType.OK);
                alert.showAndWait();
                return;
            }
        }

        int numberOfPlayers = Integer.parseInt(((RadioButton) toggleGroup.getSelectedToggle()).getText());

        SHARED_DATA.setNumberOfPlayers(numberOfPlayers);
        SHARED_DATA.setGameName(gameName);
        SHARED_DATA.setConnections(1);


        Map<String, Object> game = new HashMap<>();
        game.put("name", gameName);
        game.put("num", numberOfPlayers);

        sendMsgToRemoteServer("createGame", game);
        sendMsgToRemoteServer("connection", gameName);

        changeScene(1);
    }

    @FXML
    void initialize() {
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(radio2, radio3, radio4);
        toggleGroup.selectToggle(radio2);

        sendMsgToRemoteServer("getAllGames", null);
    }

    @Override
    public void updateInterface(String message) {
        Map<String, Object> msg;
        try {
            msg = OBJECT_MAPPER.readValue(message, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> {
            if (msg.containsKey("getAllGames")) {
                List<Map<String, Object>> games = (List<Map<String, Object>>) msg.get("getAllGames");
                gamesTable.getChildren().clear();
                freeRow = 0;
                for (Map<String, Object> game : games) {
                    createSelectGameButton(game);
                }
            } else if (msg.containsKey("createGame")) {
                Map<String, Object> game = (Map<String, Object>) msg.get("createGame");
                createSelectGameButton(game);
            } else if (msg.containsKey("startPreparation")) {
                try {
                    changeScene(2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (msg.containsKey("connection")) {
                SHARED_DATA.setConnections((Integer) msg.get("connection"));
            } else if (msg.containsKey("leave") || msg.containsKey("disconnection")) {
                SHARED_DATA.setConnections(SHARED_DATA.getConnections() - 1);
            }
        });
    }

    public void createSelectGameButton(Map<String, Object> game) {
        int connections = (int) game.getOrDefault("connections", 1);
        int numberOfPlayers = (Integer) game.get("num");
        Button button = new Button(game.get("name") + " (" + connections + "/" + numberOfPlayers + ")");
        button.setPrefWidth(gamesTable.getColumnConstraints().get(0).getPrefWidth());
        button.setPrefHeight(gamesTable.getRowConstraints().get(0).getPrefHeight());
        button.setOnAction(event -> {
            try {
                if (connections == numberOfPlayers) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Мест в игре уже нет", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                SHARED_DATA.setNumberOfPlayers((Integer) game.get("num"));
                SHARED_DATA.setGameName((String) game.get("name"));
                changeScene(1);

                Map<String, Object> connection = new HashMap<>();
                connection.put("connection", game.get("name"));
                sendMsgToRemoteServer(connection);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        gamesTable.add(button, 0, freeRow);
        freeRow += 1;
    }

    public void changeScene(int type) throws IOException {
        Scene scene = null;
        if (type == 1) {
            FXMLLoader waitingFXMLLoader = new FXMLLoader(Application.class.getResource("/waiting.fxml"));
            waitingFXMLLoader.setController(new WaitingController());
            scene = new Scene(waitingFXMLLoader.load(), 360, 420);
            radio2.getScene().getWindow().hide();
        } else if (type == 2) {
            Application.setFxmlLoader(new FXMLLoader(Application.class.getResource("/preparation.fxml")));
            scene = new Scene(Application.getFxmlLoader().load(), 360, 420);
        } else {
            return;
        }
        Utils.initializeStandardSettings(newStage);
        newStage.setScene(scene);
        newStage.show();
    }

}
