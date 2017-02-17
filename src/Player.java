import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Andrew on 2/6/2017.
 */
public class Player {
    public LinkedList<State> previous_moves;
    int considered_states = 0;
    int generated_states = 0;
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
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);
        return playGame(previous_moves,g.goal,30);
    }
    public boolean playGameIterative(Game g){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);

        int bound = 1;
        while(bound < 30 && !playGame(previous_moves,g.goal,bound)){
            //System.out.println("Iterative Backtracking at Depth: " + bound);
            previous_moves.clear();
            previous_moves.add(g.start);
            ++bound;
        }
        if(bound == 30){
            return false;
        }else {
            return true;
        }
    }
    public boolean playGame(LinkedList<State> prev_moves, State target,int depthBound){

        if(prev_moves.size() > depthBound){
            return false;
        }
        // We have found the correct path. End here.
        if(prev_moves.peekLast().equals(target)){
            //printGameState(prev_moves.peekLast());
            //System.out.println("Solution found in: "+ prev_moves.size());
            return true;
        }else{
            // For each candidate move in the move set:
            State lastMove = prev_moves.getLast();
            // Get moves applicable to this state.
            ArrayList<Offset> moves = getApplicableMoves(lastMove);
            // Run for each Candidate move in the "moves" list.
            for(Offset candidate : moves){
                ++considered_states;
                // Execute the move on the lastMove state.
                State new_state = executeMove(candidate,lastMove);
                if(!prev_moves.contains(new_state)) {
                    ++generated_states;
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
    public boolean playGameGraphSearch(Game g){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);

        Node n = new Node(g.start);

        HashSet<Node> closed = new HashSet<>();
        Deque<Node> open = new ArrayDeque<>();

        open.add(n);

        while(!open.isEmpty()){

            Node current = open.remove();
            closed.add(current);
            State gamestate = current.data;
            if(current.data.equals(g.goal)){
                Stack<Node> reverse = new Stack<>();
                while(current!= n){
                    reverse.add(current);
                    current = current.parent;
                }
                while(!reverse.isEmpty()){
                    previous_moves.add(reverse.pop().data);
                }
                return true;
            }else{
                ArrayList<Offset> moves = getApplicableMoves(gamestate);

                for(Offset candidate : moves){
                    ++considered_states;
                    State temp = executeMove(candidate,gamestate);
                    Node tNode = new Node(temp);
                    if(!closed.contains(tNode) && !open.contains(tNode)){
                        ++generated_states;
                        tNode.parent = current;
                        //closed.add(tNode);
                        open.add(tNode);
                    }
                }
            }
        }
        return false;
    }

    public boolean playGameA(Game g, iHeuristic heuristic){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);

        Node n = new Node(g.start,0,heuristic.evaluate(g.start,g.goal));
        HashSet<Node> closed = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator());
        queue.add(n);
        while(!queue.isEmpty()){
            Node current = queue.remove();
            closed.add(current);
            State gamestate = current.data;
            if(current.data.equals(g.goal)){
                Stack<Node> reverse = new Stack<>();
                while(current!= n){
                    reverse.add(current);
                    current = current.parent;
                }
                while(!reverse.isEmpty()){
                    previous_moves.add(reverse.pop().data);
                }
                return true;
            }else{
                ArrayList<Offset> moves = getApplicableMoves(gamestate);

                for(Offset candidate : moves){
                    ++considered_states;
                    State temp = executeMove(candidate,gamestate);
                    Node tNode = new Node(temp);
                    if(!closed.contains(tNode) && !queue.contains(tNode)){
                        ++generated_states;
                        tNode.parent = current;
                        tNode.depth = current.depth+1;
                        tNode.cost = heuristic.evaluate(tNode.data,g.goal);
                        //closed.add(tNode);
                        queue.add(tNode);
                    }
                }
            }
        }
        return false;
    }
    public void printGameState(State s){
        for(int[] row: s.state){
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
