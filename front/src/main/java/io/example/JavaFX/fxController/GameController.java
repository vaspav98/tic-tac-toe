package io.example.JavaFX.fxController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.Application;
import io.example.JavaFX.BigGame;
import io.example.JavaFX.SharedData;
import io.example.JavaFX.Utils;
import io.example.WebSocketClient;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class GameController extends FXController {

    @FXML
    private TextField textField;

    @FXML
    private TextField textField2;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label opponentsLabel;

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
    private GridPane opponentsTable;

    @FXML
    private VBox vBox1;

    @FXML
    private VBox vBox2;

    @FXML
    private VBox vBox3;

    private BigGame game;

    private final ObjectMapper om = new ObjectMapper();

    private WebSocketClient webSocketClient = Application.getWebsocketClient();

    private SharedData sharedData = SharedData.getInstance();

    private Timer timer;

    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            yourTurnNowLabel.setVisible(game.isMyTurn());

            if (vBox1.getId().equals(String.valueOf(game.getNowSym()))) {
                vBox1.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
            } else if (vBox2.getId().equals(String.valueOf(game.getNowSym()))) {
                vBox2.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
            } else if (vBox3.getId().equals(String.valueOf(game.getNowSym()))) {
                vBox3.setStyle("-fx-background-color: #e30e0e; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
            } else {
                vBox1.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox2.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
                vBox3.setStyle("-fx-background-color: #a19f9f; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;");
            }

        }
    };

    @FXML
    void initialize() {
        game = new BigGame(sharedData.getRole(), sharedData.getNumberOfPlayers());
        youLabel.setText("You: " + sharedData.getUsername() + " (role - \"" + sharedData.getRole() + "\")");

        int index = 0;
        for (Map<String, Object> opponent : sharedData.getOpponents()) {
            index += 1;
            if (index == 1) {
                vBox1.setVisible(true);
                vBox1.setId(String.valueOf(opponent.get("role")));
                Label l = ((Label) vBox1.getChildren().get(0));
                l.setText("- " + opponent.get("nick") + " (role - \"" + opponent.get("role") + "\")");
            } else if (index == 2) {
                vBox2.setVisible(true);
                vBox2.setId(String.valueOf(opponent.get("role")));
                Label l = ((Label) vBox2.getChildren().get(0));
                l.setText("- " + opponent.get("nick") + " (role - \"" + opponent.get("role") + "\")");
            } else if (index == 3) {
                vBox3.setVisible(true);
                vBox3.setId(String.valueOf(opponent.get("role")));
                Label l = ((Label) vBox3.getChildren().get(0));
                l.setText("- " + opponent.get("nick") + " (role - \"" + opponent.get("role") + "\")");
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
                                    timeSeconds.toString() + " sec");
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
    void btnClick(ActionEvent event) throws JsonProcessingException {
        Button btn = (Button) event.getSource();

        if (!game.isGame() || !btn.getText().isEmpty()) {
            return;
        }

        if (!game.isMyTurn()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Сейчас ход другого игрока", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (game.isMyTurn()) {
            timeline.stop();
            timerLabel.setVisible(false);
        }

        int x = GridPane.getColumnIndex(btn) == null ? 0 : GridPane.getColumnIndex(btn);
        int y = GridPane.getRowIndex(btn) == null ? 0 : GridPane.getRowIndex(btn);
        btn.setText(String.valueOf(game.getNowSym()));
        game.clickBtn(x, y);

        sendMsgToRemoteServer(x, y);
        if (!game.isGame()) {
            handleGameOver(x, y, btn);
        }
    }
    @FXML
    void submit(ActionEvent event) throws JsonProcessingException {
        if (webSocketClient == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Некому посылать сообщение, идёт одиночная игра", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Map<String, String> message = new HashMap<>();
        message.put("sms", textField.getText());
        String json = om.writeValueAsString(message);
        
        webSocketClient.sendMessage(json);
        
        textField.clear();
    }

    @FXML
    void exit(ActionEvent event) {
        sendMsgToRemoteServer("disconnection", null);
        sharedData.clean();
        changeScene("finish");
    }

    @Override
    public void updateInterface(String message) {
        Map<String, Object> msg;
        try {
            msg = om.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Используется в JavaFX для выполнения кода на главном потоке после того, как очередь обработки событий GUI
        // будет обработана. Это необходимо, потому что выполнение некоторых операций в JavaFX, таких как изменение
        // свойств элементов интерфейса или отображение анимаций, должно происходить на главном потоке.
        Platform.runLater(() -> {
            if (msg.containsKey("sms")) {
                textField2.setText((String) msg.get("sms"));
            } else if (msg.containsKey("x") && msg.containsKey("y")) {
                handleOpponentMove((Integer) msg.get("x"), (Integer) msg.get("y"));
            } else if (msg.containsKey("disconnection")) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "One or more players have disconnected from the game", ButtonType.CLOSE);
                alert.getDialogPane().setPrefWidth(400);
                alert.showAndWait();
                sharedData.clean();
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
//        sendMsgToRemoteServer(x, y);
        sendMsgToRemoteServer("finish", null);

        ButtonType exit = new ButtonType("Exit");

        String winnerNick;
        if (game.isMyTurn()) {
            winnerNick = sharedData.getUsername();
        } else {
            winnerNick = (String) sharedData.getOpponents().stream()
                    .filter(map -> (String.valueOf(map.get("role"))).equals(btn.getText()))
                    .map(map -> map.get("nick"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException());
        }

        Alert alert = new Alert(Alert.AlertType.NONE, "Победитель - " + winnerNick + " (" + btn.getText() + ")!",
                exit);

        alert.setResultConverter(b -> {
            sharedData.clean();
            changeScene("finish");
/*            if (b.equals(exit)) {
                sharedData.clean();
                changeScene("finish");
            } else {
                String gameName = sharedData.getGameName();
                int numberOfPlayers = sharedData.getNumberOfPlayers();
                sharedData.clean();
                sharedData.setGameName(gameName);
                sharedData.setNumberOfPlayers(numberOfPlayers);

                sendMsgToRemoteServer("playAgain", gameName);
                changeScene("playAgain");
            }*/
            return b;
        });
        alert.showAndWait();
    }

    public void sendMsgToRemoteServer(int x, int y) {
        Map<String, Integer> message = new HashMap<>();
        message.put("x", x);
        message.put("y", y);

        String json = null;
        try {
            json = om.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        webSocketClient.sendMessage(json);
    }

    public void sendMsgToRemoteServer(String key, Object value) {
        Map<String, Object> message = new HashMap<>();

        message.put(key, null);

        String json = null;
        try {
            json = om.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        webSocketClient.sendMessage(json);
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
