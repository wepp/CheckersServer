package vo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isaiev on 24.09.2015.
 */
public class Field implements Serializable {
    
    ANY-ACCESS-MODIFIER static final long serialVersionUID = 42L;

    private ArrayList<Check> allChecks;

    public Field() {
        allChecks=new ArrayList<Check>();
    }

    public ArrayList<Check> getAllChecks() {
        return allChecks;
    }

    protected void setAllChecks(ArrayList<Check> allChecks) {
        this.allChecks = allChecks;
    }
}
