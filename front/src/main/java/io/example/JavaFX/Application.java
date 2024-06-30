package io.example.JavaFX;

import io.example.WebSocketClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private static WebSocketClient webSocketClient;
    private static FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/chooseGame.fxml"));


    @Override
    public void start(Stage stage) throws IOException {
        webSocketClient = new WebSocketClient();
        webSocketClient.connect();

        Scene scene = new Scene(fxmlLoader.load(), 360, 420);
        stage.setTitle("Tic-Tac-Toe!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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