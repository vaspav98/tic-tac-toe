package io.example.JavaFX.fxController;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.Application;
import io.example.JavaFX.BigGame;
import io.example.JavaFX.Utils;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController extends FXController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label youLabel;

    @FXML
    private Label timerLabel;

    private static final Integer START_TIME = 20;
    private Timeline timeline;
    private Integer timeSeconds = START_TIME;

    @FXML
    private VBox yourTurnNowLabel;

    @FXML
    private VBox vBox1;

    @FXML
    private VBox vBox2;

    @FXML
    private VBox vBox3;

    @FXML
    private VBox vBox4;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    private BigGame game;

    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            yourTurnNowLabel.setVisible(game.isMyTurn());

            if (vBox1.getId().equals(String.valueOf(game.getNowSym()))) {
/*                vBox1.getStyleClass().add("Vbox-red");
                vBox2.getStyleClass().add("Vbox-gray");
                vBox3.getStyleClass().add("Vbox-gray");
                vBox4.getStyleClass().add("Vbox-gray");*/
                vBox1.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-border-color: #000");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox4.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
            } else if (vBox2.getId().equals(String.valueOf(game.getNowSym()))) {
                vBox1.getStyleClass().add("Vbox-gray");
                vBox2.getStyleClass().add("Vbox-red");
                vBox3.getStyleClass().add("Vbox-gray");
                vBox4.getStyleClass().add("Vbox-gray");
                vBox2.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox4.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
            } else if (vBox3.getId().equals(String.valueOf(game.getNowSym()))) {
/*                vBox1.getStyleClass().add("Vbox-gray");
                vBox2.getStyleClass().add("Vbox-gray");
                vBox3.getStyleClass().add("Vbox-red");
                vBox4.getStyleClass().add("Vbox-gray");*/
                vBox3.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox4.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
            } else if (vBox4.getId().equals(String.valueOf(game.getNowSym()))) {
/*                vBox1.getStyleClass().add("Vbox-gray");
                vBox2.getStyleClass().add("Vbox-gray");
                vBox3.getStyleClass().add("Vbox-gray");
                vBox4.getStyleClass().add("Vbox-red");*/
                vBox4.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;" +
                        "-fx-border-color: #000");
            }
        }
    };

    @FXML
    void initialize() {
        game = new BigGame(SHARED_DATA.getRole(), SHARED_DATA.getNumberOfPlayers());
        youLabel.setText("You: " + SHARED_DATA.getUsername() + " (role - '" + SHARED_DATA.getRole() + "')");

        List<Map<String, Object>> players = SHARED_DATA.getOpponents();
        Map<String, Object> you = new HashMap<>();
        you.put("nick", SHARED_DATA.getUsername());
        you.put("role", SHARED_DATA.getRole());
        players.add(you);

        for (Map<String, Object> player : players) {
            if (player.get("role").equals('X')) {
                vBox1.setId("X");
                Label l = ((Label) vBox1.getChildren().get(0));
                l.setText("1. " + player.get("nick") + " (role - '" + "X" + "')");
            } else if (player.get("role").equals('0')) {
                vBox2.setId("0");
                Label l = ((Label) vBox2.getChildren().get(0));
                l.setText("2. " + player.get("nick") + " (role - '" + "0" + "')");
            } else if (player.get("role").equals('#')) {
                vBox3.setVisible(true);
                vBox3.setId("#");
                Label l = ((Label) vBox3.getChildren().get(0));
                l.setText("3. " + player.get("nick") + " (role - '" + "#" + "')");
            } else if (player.get("role").equals('$')) {
                vBox4.setVisible(true);
                vBox4.setId("$");
                Label l = ((Label) vBox4.getChildren().get(0));
                l.setText("4. " + player.get("nick") + " (role - '" + "$" + "')");
            }
        }

        animationTimer.start();

        timerLabel.setText(timeSeconds.toString() + " sec");
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timeSeconds--;

                            timerLabel.setText(
                                    timeSeconds + " sec");
                            if (timeSeconds <= 0) {
                                timeline.stop();

                                sendMsgToRemoteServer("skip", null);
                                game.switchPlayerTurn();
                                timerLabel.setVisible(false);
        /*                        Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Время на ход ситекло", ButtonType.OK);
                                    alert.showAndWait();
                                });*/
                            }
                        }));

        if (game.isMyTurn()) {
            timerLabel.setVisible(true);
            timeline.playFromStart();
        }
    }

    @FXML
    void btnClick(ActionEvent event) {
        Button btn = (Button) event.getSource();

        if (!game.isGame() || !btn.getText().isEmpty()) {
            return;
        }

        if (!game.isMyTurn()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Сейчас ход другого игрока", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        timeline.stop();
        timerLabel.setVisible(false);

        int x = GridPane.getColumnIndex(btn) == null ? 0 : GridPane.getColumnIndex(btn);
        int y = GridPane.getRowIndex(btn) == null ? 0 : GridPane.getRowIndex(btn);
        btn.setText(String.valueOf(game.getNowSym()));
        game.clickBtn(x, y);

        Map<String, Object> message = new HashMap<>();
        message.put("x", x);
        message.put("y", y);
        sendMsgToRemoteServer(message);

        if (!game.isGame()) {
            handleGameOver(x, y, btn);
        }
    }

    @FXML
    void sendSms(ActionEvent event) {
        String content = messageField.getText();
        if (!content.isEmpty()) {
            chatArea.appendText("Я: " + content + "\n");

            Map<String, Object> message = new HashMap<>();
            message.put("content", content);
            message.put("nick", SHARED_DATA.getUsername());
            sendMsgToRemoteServer("sms", message);

            messageField.clear();
        }
    }

    @FXML
    void exit(ActionEvent event) {
        sendMsgToRemoteServer("disconnection", null);
        SHARED_DATA.clean();
        changeScene("finish");
    }

    @Override
    public void updateInterface(String message) {
        Map<String, Object> msg;
        try {
            msg = OBJECT_MAPPER.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Используется в JavaFX для выполнения кода на главном потоке после того, как очередь обработки событий GUI
        // будет обработана. Это необходимо, потому что выполнение некоторых операций в JavaFX, таких как изменение
        // свойств элементов интерфейса или отображение анимаций, должно происходить на главном потоке.
        Platform.runLater(() -> {
            if (msg.containsKey("sms")) {
                Map<String, Object> contentAndNick = (Map<String, Object>) msg.get("sms");
                String content = (String) contentAndNick.get("content");
                String nick = (String) contentAndNick.get("nick");

                chatArea.appendText(nick + ": " + content + "\n");

            } else if (msg.containsKey("x") && msg.containsKey("y")) {
                handleOpponentMove((Integer) msg.get("x"), (Integer) msg.get("y"));
            } else if (msg.containsKey("disconnection")) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "One or more players have disconnected from the game", ButtonType.CLOSE);
                alert.getDialogPane().setPrefWidth(400);
                alert.showAndWait();
                SHARED_DATA.clean();
                changeScene("finish");
            } else if (msg.containsKey("skip")) {
                game.switchPlayerTurn();

                if (game.isMyTurn()) {
                    timeSeconds = START_TIME;
                    timerLabel.setVisible(true);
                    timerLabel.setText(timeSeconds.toString() + " sec");
                    timeline.playFromStart();
                }
            }
        });
    }

    public void changeScene(String type) {
        Stage stage = new Stage();
        Scene scene = null;
        FXMLLoader fxmlLoader = null;
        if (type.equals("finish")) {
            fxmlLoader = new FXMLLoader(Application.class.getResource("/chooseGame.fxml"));
            Application.setFxmlLoader(fxmlLoader);
        } else if (type.equals("playAgain")) {
            fxmlLoader = new FXMLLoader(Application.class.getResource("/waiting.fxml"));
            fxmlLoader.setController(new WaitingController());
        } else {
            return;
        }

        try {
            scene = new Scene(fxmlLoader.load(), 360, 420);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Utils.initializeStandardSettings(stage);
        stage.setScene(scene);
        stage.show();
        gridPane.getScene().getWindow().hide();
    }

    public void handleOpponentMove(Integer x, Integer y) {
        Button button = (Button) findNodeByCoordinates(x, y);
        button.setText(String.valueOf(game.getNowSym()));
        game.clickBtn(x, y);

        if (!game.isGame()) {
            handleGameOver(x, y, button);
        }

        if (game.isMyTurn()) {
            timeSeconds = START_TIME;
            timerLabel.setVisible(true);
            timerLabel.setText(timeSeconds.toString() + " sec");
            timeline.playFromStart();
        }
    }

    public void handleGameOver(int x, int y, Button btn) {
        sendMsgToRemoteServer("finish", null);

        ButtonType exit = new ButtonType("Exit");

        String winnerNick;
        if (game.isMyTurn()) {
            winnerNick = SHARED_DATA.getUsername();
        } else {
            winnerNick = (String) SHARED_DATA.getOpponents().stream()
                    .filter(map -> (String.valueOf(map.get("role"))).equals(btn.getText()))
                    .map(map -> map.get("nick"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException());
        }

        Alert alert = new Alert(Alert.AlertType.NONE, "Победитель - " + winnerNick + " (" + btn.getText() + ")!",
                exit);

        alert.setResultConverter(b -> {
            SHARED_DATA.clean();
            changeScene("finish");
            return b;
        });
        alert.showAndWait();
    }

    private Node findNodeByCoordinates(int x, int y) {
        for (Node node : gridPane.getChildren()) {
            int nodeX = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
            int nodeY = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
            if (nodeX == x && nodeY == y) {
                return node;
            }
        }
        return null;
    }
}
