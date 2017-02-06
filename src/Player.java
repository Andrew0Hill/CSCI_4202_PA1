import java.util.ArrayList;

/**
 * Created by Andrew on 2/6/2017.
 */
public class Player {
    public boolean checkRule(iRule r, State s){
        return r.isApplicable(s);
    }
    public ArrayList<Move> getApplicableMoves(State s){
        ArrayList<Move> moves = new ArrayList<>();
        if(checkRule(RuleSet::down,s)){
            moves.add(new downMove());
        }
        if(checkRule(RuleSet::up,s)){
            moves.add(new upMove());
        }
        if(checkRule(RuleSet::left,s)){
            moves.add(new leftMove());
        }
        if(checkRule(RuleSet::right,s)){
            moves.add(new rightMove());
        }
        return moves;
    }
}
