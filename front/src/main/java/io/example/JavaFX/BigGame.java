package io.example.JavaFX;

import java.util.ArrayList;
import java.util.List;

public class BigGame {

    private char[][] gameField = new char[20][20];
    private boolean isGame = true;
    private char nowSym = 'X';
    private char role;
    private boolean isMyTurn;
    private int numberOfPlayers;
    List<Character> roleList = new ArrayList<>();

    public BigGame(char role, int numberOfPlayers) {
        this.role = role;
        this.numberOfPlayers = numberOfPlayers;
        fillList(numberOfPlayers);
        isMyTurn = role == 'X';
    }

    private void fillList(int num) {
        roleList.add('X');
        roleList.add('0');
        if (num >= 3) {
            roleList.add('#');
        }
        if (num >= 4) {
            roleList.add('$');
        }
        if (num == 5) {
            roleList.add('@');
        }
    }

    public void clickBtn(int x, int y) {
        gameField[x][y] = nowSym;

        int winStreak = 0;
        for (int i = 0; i < 20; i++) {
            char cage = gameField[x][i];
            if (cage == nowSym) {
                winStreak += 1;
            } else {
                winStreak = 0;
            }
            if (winStreak >= 5) {
                isGame = false;
                return;
            }
        }
        winStreak = 0;

        for (int i = 0; i < 20; i++) {
            char cage = gameField[i][y];
            if (cage == nowSym) {
                winStreak += 1;
            } else {
                winStreak = 0;
            }
            if (winStreak >= 5) {
                isGame = false;
                return;
            }
        }
        winStreak = 0;

        for (int i = 0; i < 40; i++) {
            try {
                char cage = gameField[x - 19 + i][y - 19 + i];
                if (cage == nowSym) {
                    winStreak += 1;
                } else {
                    winStreak = 0;
                }
                if (winStreak >= 5) {
                    isGame = false;
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        winStreak = 0;

        for (int i = 0; i < 40; i++) {
            try {
                char cage = gameField[x - 19 + i][y + 19 - i];
                if (cage == nowSym) {
                    winStreak += 1;
                } else {
                    winStreak = 0;
                }
                if (winStreak >= 5) {
                    isGame = false;
                    return;
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        switchPlayerTurn();
    }

    public void switchPlayerTurn() {
        int currentSymbolIndex = roleList.indexOf(nowSym);
        if (currentSymbolIndex == roleList.size() - 1) {
            nowSym = roleList.get(0);
        } else {
            nowSym = roleList.get(currentSymbolIndex + 1);
        }

        isMyTurn = role == nowSym;
    }

    public boolean isGame() {
        return isGame;
    }

    public char getNowSym() {
        return nowSym;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }
}
