package io.example.JavaFX;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private int numberOfPlayers;
    private List<WebSocketSession> connectionList = new ArrayList<>();

    public Room(String name, int numberOfPlayers) {
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WebSocketSession> getConnectionList() {
        return connectionList;
    }

    public void setConnection(WebSocketSession session) {
        connectionList.add(session);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}