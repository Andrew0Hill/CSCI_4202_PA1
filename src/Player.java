import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Andrew on 2/6/2017.
 */
public class Player {
    public LinkedList<State> previous_moves;
    Player(){
        previous_moves = new LinkedList<>();
    }
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
        State newState = new State(s);
        int tempVal = newState.state[newState.zeroRow+move[0]][newState.zeroCol+move[1]];
        newState.state[newState.zeroRow+move[0]][newState.zeroCol+move[1]] = 0;
        newState.state[newState.zeroRow][newState.zeroCol] = tempVal;
        newState.zeroRow = newState.zeroRow+move[0];
        newState.zeroCol = newState.zeroCol+move[1];
        return newState;
    }
    public boolean playGame(Game g){
        previous_moves.clear();
        previous_moves.add(g.start);
        return playGame(previous_moves,g.goal,30);
    }
    public boolean playGameIterative(Game g){
        previous_moves.clear();
        previous_moves.add(g.start);
        int bound = 1;
        while(bound < 30 && !playGame(previous_moves,g.goal,bound)){
            System.out.println("Iterative Backtracking at Depth: " + bound);
            previous_moves.clear();
            previous_moves.add(g.start);
            ++bound;
        }
        return true;
    }
    public boolean playGame(LinkedList<State> prev_moves, State target,int depthBound){

        if(prev_moves.size() > depthBound){
            return false;
        }
        // We have found the correct path. End here.
        if(prev_moves.peekLast().equals(target)){
            //printGameState(prev_moves.peekLast());
            System.out.println("Solution found in: "+ prev_moves.size());
            return true;
        }else{
            // For each candidate move in the move set:
            State lastMove = prev_moves.getLast();
            ArrayList<int[]> moves = getApplicableMoves(lastMove);
            for(int[] candidate : moves){
                State new_state = executeMove(candidate,lastMove);
                if(!prev_moves.contains(new_state)) {
                    prev_moves.add(new_state);
                    //printGameState(new_state);
                    if (playGame(prev_moves, target,depthBound)) {
                        return true;
                    }else{
                        prev_moves.removeLast();
                    }
                }
            }
            return false;
        }
    }
    public void printGameState(State s){
        for(int[] row: s.state){
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
