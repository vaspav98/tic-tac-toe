package io.example;

import io.example.JavaFX.Application;
import io.example.JavaFX.fxController.FXController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class MyWebSocketHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Connection established");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            FXController controller = Application.getFxmlLoader().getController();
            if (controller == null) {
                return;
            }
            controller.updateInterface(((TextMessage) message).getPayload());
            System.out.println("Received: " + ((TextMessage) message).getPayload());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        System.out.println("Connection closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
