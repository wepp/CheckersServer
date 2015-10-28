package com.checkers.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Isaiev on 24.09.2015.
 */
public class Step implements Serializable{
    
    public static final long serialVersionUID = 43L;

    private Check check;
    private ArrayList<Position> positionAfterMove;

    public Step(Check check, ArrayList<Position> positionAfterMove) {
        this.check = check;
        this.positionAfterMove = positionAfterMove;
    }
    
    public Step(){};

    public ArrayList<Position> getPositionAfterMove() {
        return positionAfterMove;
    }

    public Check getCheck() {
        return check;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Step{");
        sb.append("check=").append(check);
        sb.append(", positionAfterMove=").append(positionAfterMove);
        sb.append('}');
        return sb.toString();
    }
}
