package com.checkers.domain.server;

import com.checkers.domain.vo.Check;
import com.checkers.domain.vo.Field;
import com.checkers.domain.vo.Position;
import com.checkers.domain.vo.Step;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
    private static final int MAX_SIZE = 7;

    private Field currentField;

    public CheckersRulesHolder() {
        currentField = new Field();
        fillField(currentField);
    }

    private void fillField(Field currentField) {
        Set<Check> checks = Sets.newHashSet();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if ((i % 2 + j % 2) % 2 == 1) {
                    Position pos = new Position(i, j);
                    Check check = new Check(pos, j < 3 ? 0 : 1);
                    if (j < 3 || j > 4)
                        checks.add(check);
                }
            }
        }
        currentField.setAllChecks(checks);
    }

    public void revert(Field currentField) {
        Set newS = Sets.newHashSet();
        for (Check check : currentField.getAllChecks()) {
            newS.add(revert(check));
        }
        currentField.setAllChecks(newS);
    }

    private Check revert(Check check) {
        Check result = new Check();
        Position position = revert(check.getPosition());
        result.setQueen(check.isQueen());
        result.setPosition(position);
        result.setColor(check.getColor() == 0 ? 1 : 0);
        return result;
    }

    private Position revert(Position position) {
        return new Position(MAX_SIZE - position.getX(), MAX_SIZE - position.getY());
    }

    public boolean setNextStep(Step nextStep) {
        return calculateNextField(currentField, nextStep);
    }

    private boolean calculateNextField(Field currentField, Step nextStep) {
        boolean result = true;
        Check ourCheck = nextStep.getCheck();
        if(nextStep.getPositionAfterMove().isEmpty()
                || checkNeedToHeat(currentField, ourCheck, nextStep.getPositionAfterMove().get(0)))
            return false;
        if(nextStep.getPositionAfterMove().size() > 1){
            for (Position position : nextStep.getPositionAfterMove()) {
                result &= isHeatStep(ourCheck.getPosition(), position);
                result &= calculateNextField(currentField, ourCheck, position);
            }
        }else {
            result &= calculateNextField(currentField, ourCheck, nextStep.getPositionAfterMove().get(0));
        }
        return result;
    }

    private boolean checkNeedToHeat(Field currentField, Check ourCheck, Position stepTo) {
        if(isSimpleStep(ourCheck.getPosition(), stepTo)){
          for (Check check : currentField.getAllChecks()){
              if(check.getColor() != ourCheck.getColor())
                  continue;
              for(Position position : possibleHeatPositions(check.getPosition())){
                  if(canBeat(currentField, check, position))
                      return true;
              }
          }
        }
        return false;
    }

    private List<Position> possibleHeatPositions(Position position) {
        List<Position> positions = Lists.newArrayList();
        positions.add(new Position(position.getX() + 2, position.getY() + 2));
        positions.add(new Position(position.getX() + 2, position.getY() - 2));
        positions.add(new Position(position.getX() - 2, position.getY() + 2));
        positions.add(new Position(position.getX() - 2, position.getY() - 2));
        return positions;
    }

    private boolean calculateNextField(Field currentField, Check check, Position position) {
        boolean result = true;
        result &= currentField.getAllChecks().contains(check);
        result &= isPositionValid(position);//position is in borders
        result &= isPositionValid(currentField, position);// there is no other checks on position
        result &= getCheckByPosition(currentField, check.getPosition()).getColor() == 0;
        result &= canStep(currentField, check, position);//checks if it is heat if so is enemies check heated
        if (result) {
            step(currentField, check, position);
        }
        return result;
    }

    private boolean isPositionValid(Field currentField, final Position position) {
        return !Iterables.any(currentField.getAllChecks(), new Predicate<Check>() {
            @Override
            public boolean apply(Check input) {
                return input.getPosition().equals(position);
            }
        });
    }

    private boolean isPositionValid(Position position) {
        return position.getX() <= MAX_SIZE && position.getX() >= MIN_SIZE
                && position.getY() <= MAX_SIZE && position.getY() >= MIN_SIZE;
    }

    private void step(Field currentField, Check check, Position position) {
        if (isSimpleStep(check.getPosition(), position)) {
            makeSimpleStep(currentField, check, position);
        } else {
            makeHeatStep(currentField, check, position);
        }
    }

    private void makeHeatStep(Field currentField, Check check, Position position) {
        Position enemyPosition = countEnemyPosition(check.getPosition(), position);
        Check enemy = getCheckByPosition(currentField, enemyPosition);
        currentField.getAllChecks().remove(enemy);
        makeSimpleStep(currentField, check, position);
    }

    private void makeSimpleStep(Field currentField, Check check, Position position) {
        currentField.getAllChecks().remove(check);
        check.setPosition(position);
        if (positionForQueen(position)) {
            check.setQueen(true);
        }
        currentField.getAllChecks().add(check);
    }

    private boolean positionForQueen(Position position) {
        return position.getY() == MAX_SIZE;
    }

    private boolean canStep(Field currentField, Check check, Position position) {
        return isSimpleStep(check, position)
                || canBeat(currentField, check, position);
    }

    private boolean canBeat(Field currentField, Check check, Position position) {
        boolean result = true;
        result &= isPositionValid(currentField, position);// there is no other checks on positio
        result &= isPositionValid(position);//position is in borders
        result &= isHeatStep(check.getPosition(), position);
        result &= (isQueenStep(check.getPosition(), position) && check.isQueen())
                || isSimpleCheckStep(check.getPosition(), position);

        Position enemyPosition = countEnemyPosition(check.getPosition(), position);
        Check enemy = getCheckByPosition(currentField, enemyPosition);

        result &= enemy != null ? areEnemies(check, enemy) : false;
        return result;
    }

    private boolean isSimpleStep(Check check, Position positionNew) {
        Position position = check.getPosition();
        return Math.abs(position.getY() - positionNew.getY()) == Math.abs(position.getX() - positionNew.getX())
                && Math.abs(position.getY() - positionNew.getY()) == 1
                && ((isQueenStep(check.getPosition(), position) && check.isQueen())
                || isSimpleCheckStep(check.getPosition(), position));
    }

    private boolean isHeatStep(Position position, Position positionNew) {
        return Math.abs(position.getY() - positionNew.getY()) == Math.abs(position.getX() - positionNew.getX())
                && Math.abs(position.getY() - positionNew.getY()) == 2;
    }

    private boolean isQueenStep(Position position, Position positionNew) {
        return position.getY() - positionNew.getY() > 0;
    }

    private boolean isSimpleCheckStep(Position position, Position positionNew) {
        return position.getY() - positionNew.getY() < 0;
    }

    private Position countEnemyPosition(Position position, Position positionNew) {
        int newX = position.getX() + (position.getX() > positionNew.getX() ? -1 : 1);
        int newY = position.getY() + (position.getY() > positionNew.getY() ? -1 : 1);
        return new Position(newX, newY);
    }

    private boolean areEnemies(Check check, Check enemy) {
        return check.getColor() != enemy.getColor();
    }

    private Check getCheckByPosition(Field currentField, final Position enemyPosition) {
        Check enemy = null;
        try {
            enemy = Iterables.find(currentField.getAllChecks(), new Predicate<Check>() {
                @Override
                public boolean apply(Check input) {
                    return enemyPosition.equals(input.getPosition());
                }
            });
        } catch (NoSuchElementException e) {
        }
        return enemy;
    }

    public Field getField() {
        return currentField;
    }
}
