package com.checkers.domain.vo;

/**
 * Created by Eugene on 08.11.2015.
 */
public class Game {
    private int gameId;
    private String whiteName;
    private String blackName;
    private String finished;
    private String winner;

    public Game() {
    }

    public Game(int gameId, String whiteName, String blackName, String finished, String winner) {
        this.gameId = gameId;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.finished = finished;
        this.winner = winner;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
