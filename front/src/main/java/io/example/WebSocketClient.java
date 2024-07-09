package io.example;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class WebSocketClient {
    private String url;

    private WebSocketSession session;

    public WebSocketClient() {
        String mode = System.getenv("MODE");
        if (mode != null && mode.equals("dev")) {
            url = "ws://localhost:8080/events";
        } else {
            url = "ws://tic-tac-toe-olr4.onrender.com//events";
        }
    }

    public void connect() throws ExecutionException, InterruptedException {

        org.springframework.web.socket.client.WebSocketClient client = new StandardWebSocketClient();
        session = client.doHandshake(new MyWebSocketHandler(), url).get();

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
