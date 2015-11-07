package com.checkers.domain.server;

import com.checkers.domain.vo.Check;
import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Position;
import com.checkers.domain.vo.Step;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Eugene on 08.11.2015.
 */
public class CheckersRulesHolder {

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

    public CheckersRulesHolder() {
        currentField = new Field();
        fillField(currentField);
    }

    private void fillField(Field currentField) {
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
        currentField.setAllChecks(checks);
    }

    public void revert() {

    }

    public boolean setNextStep(Step nextStep) {
        return calculateNextField(currentField, nextStep);
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

    private boolean calculateNextField(Field currentField, Step nextStep) {
        boolean result = true;
        Check ourCheck = nextStep.getCheck();
        for(Position position : nextStep.getPositionAfterMove()){
            result &= calculateNextField(currentField, ourCheck, position);
        }
        return result;
    }

    private boolean calculateNextField(Field currentField, Check check, Position position) {
        boolean result = true;
        result &= currentField.getAllChecks().contains(check);
        result &= isPositionValid(position);
        result &= isPositionValid(currentField, position);
        return result;
    }

    private boolean isPositionValid(Field currentField, final Position position) {
        return Iterables.any(currentField.getAllChecks(), new Predicate<Check>() {
            @Override
            public boolean apply(Check input) {
                return input.equals(position);
            }
        });
    }

    private boolean isPositionValid(Position position) {
        return position.getX() < MAX_SIZE && position.getX() > MIN_SIZE
            && position.getY() < MAX_SIZE && position.getY() > MIN_SIZE;
    }

    public Field getField() {
        return currentField;
    }
}
