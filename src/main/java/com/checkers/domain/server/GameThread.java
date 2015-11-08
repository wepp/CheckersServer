package com.checkers.domain.server;

import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Player;
import com.checkers.domain.vo.Step;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class GameThread implements Runnable {

    private Player white;
    private Player black;
    private Player hisTurn;
    private long MAX_STEP_TIME = 5000;
    private boolean finished;
    private List<Field> gameStory;

    public GameThread(Player white, Player black) {
        this.white = white;
        this.black = black;
        gameStory = Lists.newArrayList();
        System.out.println("Game registred");
    }

    @Override
    public void run() {
        System.out.println("Game started");
        try {
            int i = 0; //for debug
            CheckersRulesHolder checkersRulesHolder = new CheckersRulesHolder();
            this.finished = false;
            hisTurn = white;
            ObjectMapper objectMapper = new ObjectMapper();
            while (!this.finished) {
                hisTurn.writeObject(objectMapper.writeValueAsString(checkersRulesHolder.getField()));
                Step whiteStep = objectMapper.readValue((String)hisTurn.readObject(), Step.class);
                if(isValidTime(whiteStep.getUsedTime())
                   && checkersRulesHolder.setNextStep(whiteStep)){
                    if(hisTurn.equals(white)){
                        gameStory.add(checkersRulesHolder.getField());
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                    } else{
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                        gameStory.add(checkersRulesHolder.getField());
                    }
                    hisTurn = hisTurn.equals(white) ? black : white;
                }else {
                    if(hisTurn.equals(white)){
                        gameStory.add(checkersRulesHolder.getField());
                    } else{
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                        gameStory.add(checkersRulesHolder.getField());
                    }
                    hisTurn = hisTurn.equals(white) ? black : white;
                    this.finished = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
            hisTurn = hisTurn.equals(white) ? black : white;
            this.finished = true;
        }
    }

    private boolean isValidTime(long usedTime) {
        return usedTime < MAX_STEP_TIME;
    }

    public boolean gameFinished(){
        return finished;
    }

    public Player getWinner(){
        if(finished){
            return hisTurn;
        }else {
            return null;
        }
    }

    private void closeAll() {
        try {
            white.close();
            black.close();
        } catch (IOException e) {
            System.err.println("Server error! " + e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameThread that = (GameThread) o;

        if (!black.equals(that.black)) return false;
        if (!white.equals(that.white)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = white.hashCode();
        result = 31 * result + black.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("GameThread{");
        sb.append("black=").append(black);
        sb.append(", white=").append(white);
        sb.append('}');
        return sb.toString();
    }
}
