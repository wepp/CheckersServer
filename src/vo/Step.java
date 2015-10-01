package vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Isaiev on 24.09.2015.
 */
public class Step implements Serializable{

    private Check check;
    private List<Position> positionAfteMove;

    public Step(Check check, List<Position> positionAfteMove) {
        this.check = check;
        this.positionAfteMove = positionAfteMove;
    }
    
    public Step(){};

    public Check getCheck() {
        return check;
    }

    public List<Position> getPositionAfteMove() {
        return positionAfteMove;
    }

    @Override
    public String toString() {
        return "Step{" +
                "check=" + check +
                ", positionAfteMove=" + positionAfteMove +
                '}';
    }
}
