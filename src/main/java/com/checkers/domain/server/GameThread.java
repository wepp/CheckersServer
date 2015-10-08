package com.checkers.domain.server;

import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Step;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class GameThread extends Thread {

    private Field currentField;
    private boolean isWhitesTurn;
    private Socket white;
    private Socket black;

    private ObjectOutputStream outObjWhite;
    private ObjectInputStream inObjectWhite;
    private ObjectOutputStream outObjectBlack;
    private ObjectInputStream inObjectBlack;

    public GameThread(Socket white, Socket black) {
        this.white = white;
        this.black = black;
        fillField();
        isWhitesTurn = true;
        try {
            this.outObjWhite = new ObjectOutputStream(white.getOutputStream());
            this.inObjectWhite = new ObjectInputStream(white.getInputStream());
            this.outObjectBlack = new ObjectOutputStream(black.getOutputStream());
            this.inObjectBlack = new ObjectInputStream(black.getInputStream());
        } catch (IOException e) {
            System.err.println("Server error! " + e);
        }
    }

    @Override
    public void run() {
        try{
            int i = 0; //for debug
            outObjWhite.writeObject(new Field());
            System.out.println("First field was sent");
            while (true) {
                if (isWhitesTurn) {
                    Step whiteStep = (Step) inObjectWhite.readObject();
                    if (whiteStep != null) {
                        System.out.println("White's step");
                        nextStep(whiteStep);
                        if(i>=10) currentField=null; //for debug
                        if(currentField!=null) {
                            i++; //for debug
                            outObjectBlack.writeObject(currentField);
                        }else{
                            break;
                        }
                    }
                }else {
                    Step blackStep = (Step) inObjectBlack.readObject();
                    if (blackStep != null) {
                        System.out.println("Black's step");
                        nextStep(blackStep);
                        if(currentField!=null) {
                            i++; //for debug
                            outObjWhite.writeObject(currentField);
                        }else{
                            break;
                        }
                    }
                }
            }
            closeAll();
        }
        catch(Exception e){
            throw new RuntimeException();
        } finally {
            closeAll();
        }
    }

    private void nextStep(Step nextStep) {
        String player = isWhitesTurn ? "White":"Black";
        if(isValidStep(nextStep)) {
            currentField = calculateNextField(nextStep);
            if(isUserWin(currentField)) {
                System.out.println(player + " player won!");
                currentField = null;
            }
            else isWhitesTurn = !isWhitesTurn;
        }else{
            System.err.println("Not valid step from "+ player+"! Looser!");
            currentField = null;
        }
    }

    public Socket winner(){
        return white;
    }

    private boolean isValidStep(Step nextStep){return true;}

    private Field calculateNextField(Step nextStep){return new Field();}

    private boolean isUserWin(Field currentField){return false;}

    private void fillField(){
        this.currentField = new Field();
    }

    private void closeAll(){
        try {
            inObjectWhite.close();
            outObjWhite.close();
            inObjectBlack.close();
            outObjectBlack.close();
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
        final StringBuilder sb = new StringBuilder("GameThread{");
        sb.append("black=").append(black);
        sb.append(", white=").append(white);
        sb.append('}');
        return sb.toString();
    }
}
