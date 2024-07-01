package io.example;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;

public class WebSocketClient {

    private static final String URL = "ws://localhost:8080/events"; // URL вашего WebSocket сервера
/*    private static final String URL = "ws://tic-tac-toe-olr4.onrender.com//events";*/

    private WebSocketSession session;

    public void connect() {
        try {
            org.springframework.web.socket.client.WebSocketClient client = new StandardWebSocketClient();
            session = client.doHandshake(new MyWebSocketHandler(), URL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (session!= null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeConnection() throws IOException {
        if (session!= null) {
            session.close();
        }
    }



















/*    public static void main(String[] args) throws Exception {

        // Создаем экземпляр StandardWebSocketClient
        org.springframework.web.socket.client.WebSocketClient client = new StandardWebSocketClient();

        // Устанавливаем соединение с сервером
        WebSocketSession webSocketSession = client.doHandshake(new MyWebSocketHandler(), URL).get();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("На отправку: ");
            String msg = scanner.nextLine();
            webSocketSession.sendMessage(new TextMessage(msg));
        }
    }*/

}
