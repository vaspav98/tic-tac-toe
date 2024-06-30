package io.example;

import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private String name;
    private int numberOfPlayers;
    private transient List<WebSocketSession> connectionList = new ArrayList<>();

    public Game(String name, int numberOfPlayers) {
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

    public void removeConnection(WebSocketSession session) {
        connectionList.remove(session);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberOfPlayers == game.numberOfPlayers && Objects.equals(name, game.name) && Objects.equals(connectionList, game.connectionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, numberOfPlayers, connectionList);
    }
}
