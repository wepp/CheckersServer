package com.checkers.domain.server;

import com.checkers.domain.vo.Check;
import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Position;
import com.checkers.domain.vo.Step;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Isaiev on 01.10.2015.
 */
public class GameThread implements Runnable {

    /**
     * MIN_SIZE is number of field before minimum
     * EXAMPLE: our min is 1 so MIN i s0
     */
    private static final int MIN_SIZE = 0;
    /**
     * The same as MIN so our max is 8
     * there is value 9
     */
    private static final int MAX_SIZE = 9;

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
        System.out.println("Game registred");
    }

    @Override
    public void run() {
        System.out.println("Game started");
        try {
            int i = 0; //for debug
            outObjWhite.writeObject("white");
            outObjectBlack.writeObject("black");
            outObjWhite.writeObject(new Field());
            System.out.println("First field was sent");
            while (true) {
                if (isWhitesTurn) {
                    Step whiteStep = (Step) inObjectWhite.readObject();
                    if (whiteStep != null) {
                        System.out.println("White's step");
                        //nextStep(whiteStep);
                        if (i >= 10) currentField = null; //for debug
                        if (currentField != null) {
                            i++; //for debug
                            outObjectBlack.writeObject(currentField);
                        } else {
                            break;
                        }
                    }
                } else {
                    Step blackStep = (Step) inObjectBlack.readObject();
                    if (blackStep != null) {
                        System.out.println("Black's step");
                        //nextStep(blackStep);
                        if (currentField != null) {
                            i++; //for debug
                            outObjWhite.writeObject(currentField);
                        } else {
                            break;
                        }
                    }
                }
            }
            closeAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            closeAll();
        }
    }

    private void nextStep(Step nextStep) {
        String player = isWhitesTurn ? "White" : "Black";
        if (isValidStep(nextStep)) {
            currentField = calculateNextField(nextStep);
            if (isUserWin(currentField)) {
                System.out.println(player + " player won!");
                currentField = null;
            } else isWhitesTurn = !isWhitesTurn;
        } else {
            System.err.println("Not valid step from " + player + "! Looser!");
            currentField = null;
        }
    }

    public Socket winner() {
        //TODO Eugen
        return white;
    }

    private boolean isValidStep(Step nextStep) {
        ArrayList<Position> possibleSteps = nextStep.getPositionAfterMove();
        /**
         * IF HAS NO MORE STEPS THAN
         * NO VALID STEPS
         */
        if (possibleSteps.size() == 0)
            return false;
        if (possibleSteps.size() == 1)
            return checkOptimalStep(nextStep);
        return true;
    }

    private boolean checkOptimalStep(Step nextStep) {
        Check currentCheck = nextStep.getCheck();
        Position currentPosition = currentCheck.getPosition();
        int curX = currentPosition.getX();
        int curY = currentPosition.getY();
        Position nextPosition = nextStep.getPositionAfterMove().get(0);
        int nextX = nextPosition.getX();
        int nextY = nextPosition.getY();

        for (Check check : currentField.getAllChecks())
            if (check.getPosition().getX() == nextX && check.getPosition().getY() == nextY)
                return false;

        /**
         * SIMPLE STEP
         */
        if (isSimpleXStepOnBoard(currentCheck, curX, nextX)) {
            /**
             * IF OTHER CKECK IS ON THIS FIELD
             */
            return ((nextY == curY - 1 && nextY > MIN_SIZE) || (nextY == curY + 1 && nextY < MAX_SIZE));
        } else if (isSimpleXAttackOnBoard(currentCheck, curX, nextX)) {
            if (currentCheck.isWhite()) {
                if (nextY == curY - 2) {
                    for (Check check : currentField.getAllChecks())
                        if (check.getPosition().getX() == curX - 1 && check.getPosition().getY() == curY - 1)
                            return true;
                } else if (nextY == curY + 2) {
                    for (Check check : currentField.getAllChecks())
                        if (check.getPosition().getX() == curX - 1 && check.getPosition().getY() == curY + 1)
                            return true;
                }
            } else if (!currentCheck.isWhite()) {
                if (nextY == curY - 2) {
                    for (Check check : currentField.getAllChecks())
                        if (check.getPosition().getX() == curX + 1 && check.getPosition().getY() == curY - 1)
                            return true;
                } else if (nextY == curY + 2) {
                    for (Check check : currentField.getAllChecks())
                        if (check.getPosition().getX() == curX + 1 && check.getPosition().getY() == curY + 1)
                            return true;
                }
            }
//                for (Check check : currentField.getAllChecks())
//                    if (check.getPosition().getX() == nextX && check.getPosition().getY() == nextY)
//                        return false;
//                return true;
        }
        return false;
    }

    private boolean isSimpleXStepOnBoard(Check currentCheck, int curX, int nextX) {
        return currentCheck.isWhite() && (curX - 1) == nextX && nextX > MIN_SIZE   // CHECK FOR WHITE CHECK STEP
                ||
                !currentCheck.isWhite() && (curX + 1) == nextX && nextX < MAX_SIZE;   // CHECK FOR BLACK CHECK STEP
    }

    private boolean isSimpleXAttackOnBoard(Check currentCheck, int curX, int nextX) {
        return currentCheck.isWhite() && (curX - 2) == nextX && nextX > MIN_SIZE   // CHECK FOR WHITE CHECK ATTACK
                ||
                !currentCheck.isWhite() && (curX + 2) == nextX && nextX < MAX_SIZE;   // CHECK FOR BLACK CHECK ATTACK
    }

    private Field calculateNextField(Step nextStep) {
        return new Field();
    }

    private boolean isUserWin(Field currentField) {
        return false;
    }

    private void fillField() {
        this.currentField = new Field();
        ArrayList<Check> checks = new ArrayList<Check>();
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                if(i%2==1){
                    if(j%2==0){
                        Check check = null;
                        Position pos = new Position(i,j);
                        if(i<=3) check = new Check(pos,true);
                        if(i>=6) check = new Check(pos,false);
                        if(check!=null) checks.add(check);

                    }
                } else {
                    if(j%2==1){
                        Check check = null;
                        Position pos = new Position(i,j);
                        if(i<=3) check = new Check(pos,true);
                        if(i>=6) check = new Check(pos,false);
                        if(check!=null) checks.add(check);
                    }
                }
            }
        }
        this.currentField.setAllChecks(checks);
    }

    private void closeAll() {
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
        StringBuilder sb = new StringBuilder("GameThread{");
        sb.append("black=").append(black);
        sb.append(", white=").append(white);
        sb.append('}');
        return sb.toString();
    }
}
