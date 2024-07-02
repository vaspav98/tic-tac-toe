package io.example.JavaFX;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static Image icon;

    public static void initializeStandardSettings(Stage stage) {
        if (icon == null) {
            icon = new Image("icon.jpg");
        }

        stage.getIcons().add(icon);
        stage.setTitle("Tic-Tac-Toe");
        stage.setResizable(false);
    }
}
