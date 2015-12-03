package com.checkers.domain.server;

import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Player;
import com.checkers.domain.vo.Step;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class GameThread implements Runnable {

    private Player white;
    private Player black;
    private Player hisTurn;
    private long MAX_STEP_TIME = 5000;
    private boolean finished;
    private Queue<Field> gameStory;
    private List<Field> gameStoryStrings;
    private int chekersCount = -1;
    private int withoutHeat=0;

    public GameThread(Player white, Player black) {
        this.white = white;
        this.black = black;
        gameStory = new ConcurrentLinkedQueue();
        gameStoryStrings = new CopyOnWriteArrayList<Field>();
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
            saveStep(checkersRulesHolder.getField());
            while (!this.finished && withoutHeat<15) {
                if(chekersCount == checkersRulesHolder.getField().getAllChecks().size()) {
                    withoutHeat++;
                }else{
                    chekersCount = checkersRulesHolder.getField().getAllChecks().size();
                    withoutHeat=0;
                }
                hisTurn.writeObject(objectMapper.writeValueAsString(checkersRulesHolder.getField()));
                Step whiteStep = objectMapper.readValue((String)hisTurn.readObject(), Step.class);
                if(isValidTime(whiteStep.getUsedTime())
                   && checkersRulesHolder.setNextStep(whiteStep)){
                    if(hisTurn.equals(white)){
                        saveStep(checkersRulesHolder.getField());
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                    } else{
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                        saveStep(checkersRulesHolder.getField());
                    }
                    hisTurn = hisTurn.equals(white) ? black : white;
                }else {
                    if(hisTurn.equals(white)){
                        saveStep(checkersRulesHolder.getField());
                    } else{
                        checkersRulesHolder.revert(checkersRulesHolder.getField());
                        saveStep(checkersRulesHolder.getField());
                    }
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

    private void saveStep(Field field){
        Field fieldNew = new Field(field);
        gameStory.add(fieldNew);
        gameStoryStrings.add(fieldNew);
    }

    private boolean isValidTime(long usedTime) {
        return usedTime < MAX_STEP_TIME;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
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

    public Queue<Field> getGameStory() {
        return gameStory;
    }

    public List<Field> getGameStoryStrings() {
        return gameStoryStrings;
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
