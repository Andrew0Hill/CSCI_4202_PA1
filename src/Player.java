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

    // Gets the moves applicable to State s.
    // Returns an ArrayList<> of int[].
    // Each int[] is size 2 and contains the offset for a move.
    public ArrayList<Offset> getApplicableMoves(State s){
        ArrayList<Offset> moves = new ArrayList<>();
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
    public State executeMove(Offset diff, State s){
        State newState = new State(s);
        int tempVal = newState.state[newState.zeroRow+diff.row][newState.zeroCol+diff.col];
        newState.state[newState.zeroRow+diff.row][newState.zeroCol+diff.col] = 0;
        newState.state[newState.zeroRow][newState.zeroCol] = tempVal;
        newState.zeroRow = newState.zeroRow+diff.row;
        newState.zeroCol = newState.zeroCol+diff.col;
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
            // Get the most recent move.
            State lastMove = prev_moves.getLast();
            // Get moves applicable to this state.
            ArrayList<Offset> moves = getApplicableMoves(lastMove);
            // Run for each Candidate move in the "moves" list.
            for(Offset candidate : moves){
                // Execute the move on the lastMove state.
                State new_state = executeMove(candidate,lastMove);
                // Check that the new state hasn't already been visited
                if(!prev_moves.contains(new_state)) {
                    // Add the new move to the moves list.
                    prev_moves.add(new_state);
                    // Call the function recursively.
                    if (playGame(prev_moves, target,depthBound)) {
                        return true;
                    }else{
                        // If the function returns false, remove the last move, and go to the next one.
                        prev_moves.removeLast();
                    }
                }
            }
            // Return false if all moves at this level are tried without a solution.
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
