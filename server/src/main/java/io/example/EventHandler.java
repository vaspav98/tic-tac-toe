package io.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();
    private List<Game> games = new ArrayList<>();
    private ObjectMapper om = new ObjectMapper();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
        System.out.println("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (getSpecificMessageType(message).equals("finish")) {
            Game game = findGameBySession(session);
            if (game == null) {
                return;
            }
            games.remove(game);
            return;
        }

        if (getSpecificMessageType(message).equals("getAllGames")) {
            handleGetAllGames(session);
            return;
        }

        if (getSpecificMessageType(message).equals("connection")) {
            ifConnection(message, session);
            handleGetAllGames(session);
            return;
        }

        if (getSpecificMessageType(message).equals("createGame")) {
            String payload = message.getPayload();
            Map<String, Map<String, Object>> msg = om.readValue(payload,
                    new TypeReference<Map<String, Map<String, Object>>>() {
                    });
            Map<String, Object> game = msg.get("createGame");
            String gameName = (String) game.get("name");
            int numberOfPlayers = (int) game.get("num");
            games.add(new Game(gameName, numberOfPlayers));

            handleGetAllGames(session);
            return;
        }

/*        for (WebSocketSession s : sessions) {
            if (getSpecificMessageType(message).equals("createGames") && s.isOpen()) {
                String payload = message.getPayload();
                Map<String, Map<String, Object>> msg = om.readValue(payload,
                        new TypeReference<Map<String, Map<String, Object>>>() {
                        });
                Map<String, Object> game = msg.get("createGame");
                String gameName = (String) game.get("name");
                int numberOfPlayers = (int) game.get("num");
                games.add(new Game(gameName, numberOfPlayers));
                if (!s.equals(session)) {
                    s.sendMessage(message);
                }
            }
        }*/
        if (getSpecificMessageType(message).equals("disconnection")) {
            handleDisconnection(session, message);
            handleGetAllGames(session);
            return;
        }

        for (WebSocketSession s : getAllGameConnections(session)) {
            if (getSpecificMessageType(message).equals("leave")) {
                Game game = findGameBySession(session);
                if (game != null) {
                    game.removeConnection(session);
                }
                handleGetAllGames(session);
            }
            if (s.isOpen() && !s.equals(session)) { // Проверяем, что сессия еще открыта и не является сессией от которой сообщение
                s.sendMessage(message);
            }
        }
        System.out.println("Received " + message.toString());
    }

    public void handleDisconnection(WebSocketSession session, TextMessage message) throws IOException {
        List<WebSocketSession> sessions = getAllGameConnections(session);
        Game game = findGameBySession(session);
        System.out.println(game == null);
        if (game != null) {
            game.getConnectionList().clear();
        }
        for (WebSocketSession s : sessions) {
            if (s.isOpen() && !s.equals(session)) {
                s.sendMessage(message);
            }
        }
    }

    public void handleGetAllGames(WebSocketSession session) throws IOException {
        Map<String, Object> msg = new HashMap<>();
        List<Map<String, Object>> gamesInMap = games.stream()
                .map(g -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", g.getName());
                    map.put("num", g.getNumberOfPlayers());
                    map.put("connections", g.getConnectionList().size());
                    return map;
                })
                .collect(Collectors.toList());
        msg.put("getAllGames", gamesInMap);
        String json = om.writeValueAsString(msg);
        TextMessage textMessage = new TextMessage(json);
        for (WebSocketSession s : sessions) {
            s.sendMessage(textMessage);
        }
//        session.sendMessage(textMessage);
    }

    public void ifConnection(TextMessage message, WebSocketSession session) throws IOException {
        Game game = addConnectionToTheGame(message, session);
        Map<String, Object> msg = new HashMap<>();
        msg.put("connection", game.getConnectionList().size());
        String json = om.writeValueAsString(msg);
        TextMessage textMessage = new TextMessage(json);
        for (WebSocketSession s : getAllGameConnections(session)) {
            s.sendMessage(textMessage);

            if (game.getConnectionList().size() == game.getNumberOfPlayers()) {
                Map<String, Object> msg2 = new HashMap<>();
                msg2.put("startPreparation", null);
                String json2 = om.writeValueAsString(msg2);
                TextMessage textMessage2 = new TextMessage(json2);
                s.sendMessage(textMessage2);
            }
        }
    }

    public String getSpecificMessageType (TextMessage message) {
        String payload = message.getPayload();
        Map<String, Object> parsedMessage;
        try {
            parsedMessage = om.readValue(payload, new TypeReference<Map<String, Object>>() { });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (parsedMessage.containsKey("createGame")) {
            return "createGame";
        } else if (parsedMessage.containsKey("getAllGames")) {
            return "getAllGames";
        } else if (parsedMessage.containsKey("connection")) {
            return "connection";
        } else if (parsedMessage.containsKey("finish")) {
            return "finish";
        } else if (parsedMessage.containsKey("disconnection")) {
            return "disconnection";
        } else if (parsedMessage.containsKey("leave")) {
            return "leave";
        }
        return "simple";
    }

    public Game addConnectionToTheGame(TextMessage message, WebSocketSession session) throws IOException {
        String payload = message.getPayload();

        Map<String, Object> connection = om.readValue(payload, new TypeReference<Map<String, Object>>() { });
        String gameName = (String) connection.get("connection");

        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                game.setConnection(session);
                return game;
            }
        }
        return null;
    }

    public Game findGameBySession(WebSocketSession session) {
        for (Game game: games) {
            for (WebSocketSession s : game.getConnectionList()) {
                if (session.equals(s)) {
                    return game;
                }
            }
        }
        return null;
    }

    public List<WebSocketSession> getAllGameConnections(WebSocketSession session) {
        List<WebSocketSession> result = new ArrayList<>();
        for (Game game : games) {
            if (game.getConnectionList().contains(session)) {
                result.addAll(game.getConnectionList());
            }
        }
        return result;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);

        Map<String, Object> msg = new HashMap<>();
        msg.put("disconnection", null);
        String json = om.writeValueAsString(msg);
        TextMessage textMessage = new TextMessage(json);

        handleDisconnection(session, textMessage);
        handleGetAllGames(session);

        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }

}
