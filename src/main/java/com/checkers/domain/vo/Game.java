package com.checkers.domain.vo;

import java.util.Set;

/**
 * Created by Eugene on 08.11.2015.
 */
public class Game {
    private int gameId;
    private String whiteName;
    private String blackName;
    private boolean finished;
    private String winner;
    private Set<Check> field;

    public Game() {
    }

    public Game(int gameId, String whiteName, String blackName, boolean finished, String winner) {
        this.gameId = gameId;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.finished = finished;
        this.winner = winner;
    }

    public Game(int gameId, String whiteName, String blackName, boolean finished, String winner, Set<Check> field) {
        this.gameId = gameId;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.finished = finished;
        this.winner = winner;
        this.field = field;
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

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Set<Check> getField() {
        return field;
    }

    public void setField(Set<Check> field) {
        this.field = field;
    }
}
