package io.example;

import io.example.JavaFX.Utils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class Application extends javafx.application.Application {
    private static WebSocketClient webSocketClient;
    private static FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/connectionToServer.fxml"));

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(fxmlLoader.load(), 360, 420);
        Utils.initializeStandardSettings(stage);
        stage.setScene(scene);
        stage.show();

        connect(stage);
    }

    private void connect(Stage stage) {
        new Thread(() -> {
            try {
                webSocketClient = new WebSocketClient();
                webSocketClient.connect();
                // Если подключение успешно, можно безопасно переключиться на другой интерфейс
                Platform.runLater(() -> {
                    fxmlLoader = new FXMLLoader(Application.class.getResource("/chooseGame.fxml"));
                    Scene newScene = null;
                    try {
                        newScene = new Scene(fxmlLoader.load(), 360, 420);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setScene(newScene);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    ButtonType tryAgain = new ButtonType("Try again");
                    ButtonType exit = new ButtonType("Exit");

                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось установить соединение с" +
                            "\nудалённым сервером", tryAgain, exit);

                    alert.setResultConverter(b -> {
                        if (b.equals(tryAgain)) {
                            connect(stage);
                        } else {
                            System.exit(0);
                        }
                        return b;
                    });
                    alert.showAndWait();
                });
            }
        }).start();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static void setFxmlLoader(FXMLLoader fxmlLoader) {
        Application.fxmlLoader = fxmlLoader;
    }

    public static WebSocketClient getWebsocketClient() {
        if (webSocketClient == null) {
            webSocketClient = new WebSocketClient();
        }
        return webSocketClient;
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}