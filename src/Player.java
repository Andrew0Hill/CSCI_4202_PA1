import java.util.ArrayList;

/**
 * Created by Andrew on 2/6/2017.
 */
public class Player {
    public boolean checkRule(iRule r, State s){
        return r.isApplicable(s);
    }
    public ArrayList<int[]> getApplicableMoves(State s){
        ArrayList<int[]> moves = new ArrayList<>();
        if(checkRule(RuleSet::down,s)){
            moves.add(Move.DOWN);
        }
        if(checkRule(RuleSet::up,s)){
            moves.add(Move.UP);
        }
        if(checkRule(RuleSet::left,s)){
            moves.add(Move.LEFT);
        }
        if(checkRule(RuleSet::right,s)){
            moves.add(Move.RIGHT);
        }
        return moves;
    }
    public State executeMove(int[] move, State s){
        int tempVal = s.state[s.zeroRow+move[0]][s.zeroCol+move[1]];
        s.state[s.zeroRow+move[0]][s.zeroCol+move[1]] = 0;
        s.state[s.zeroRow][s.zeroCol] = tempVal;
        s.zeroRow = s.zeroRow+move[0];
        s.zeroCol = s.zeroCol+move[1];
        return s;
    }
}
