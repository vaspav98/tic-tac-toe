package io.example.JavaFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedData {
    private String gameName;
    private String username;
    private char role;

    private List<Map<String, Object>> opponents = new ArrayList<>();

    private int numberOfPlayers;

    private int connections;

    private final static SharedData INSTANCE = new SharedData();

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public char getRole() {
        return role;
    }
    public void setRole(char role) {
        this.role = role;
    }

    public List<Map<String, Object>> getOpponents() {
        return opponents;
    }

    public void setOpponent(Map<String, Object> opponent) {
        this.opponents.add(opponent);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void clean() {
        username = null;
        role = '\u0000';
        opponents.clear();
        numberOfPlayers = 0;
        connections = 0;
        gameName = null;
    }

    public static SharedData getInstance() {
        return INSTANCE;
    }

}
