package io.example.JavaFX;

public class Game {
    private char[][] gameField = new char[3][3];
    private boolean isGame = true;
    private char nowSym = 'X';
    private char role;
    private boolean isMyTurn;

    public void clickBtn(int x, int y) {

        gameField[x][y] = nowSym;

        if (gameField[0][0] == gameField[1][0] && gameField[0][0] == gameField[2][0] &&
                (gameField[0][0] == 'X' || gameField[0][0] == '0')) {
            isGame = false;
        } else if (gameField[0][1] == gameField[1][1] && gameField[0][1] == gameField[2][1] &&
                (gameField[0][1] == 'X' || gameField[0][1] == '0')) {
            isGame = false;
        } else if (gameField[0][2] == gameField[1][2] && gameField[0][2] == gameField[2][2] &&
                (gameField[0][2] == 'X' || gameField[0][2] == '0')) {
            isGame = false;
        } else if (gameField[0][0] == gameField[0][1] && gameField[0][0] == gameField[0][2] &&
                (gameField[0][0] == 'X' || gameField[0][0] == '0')) {
            isGame = false;
        } else if (gameField[1][0] == gameField[1][1] && gameField[1][0] == gameField[1][2] &&
                (gameField[1][0] == 'X' || gameField[1][0] == '0')) {
            isGame = false;
        } else if (gameField[2][0] == gameField[2][1] && gameField[2][0] == gameField[2][2] &&
                (gameField[2][0] == 'X' || gameField[2][0] == '0')) {
            isGame = false;
        } else if (gameField[0][0] == gameField[1][1] && gameField[0][0] == gameField[2][2] &&
                (gameField[0][0] == 'X' || gameField[0][0] == '0')) {
            isGame = false;
        } else if (gameField[0][2] == gameField[1][1] && gameField[0][2] == gameField[2][0] &&
                (gameField[0][2] == 'X' || gameField[0][2] == '0')) {
            isGame = false;
        }

        nowSym = nowSym == 'X' ? '0' : 'X';
        isMyTurn = !isMyTurn;
    }

    public boolean isGame() {
        return isGame;
    }

    public char getNowSym() {
        return nowSym;
    }

    public void setRole(char role) {
        this.role = role;
        isMyTurn = role == 'X';
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }
}
