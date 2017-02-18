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
return true;
    }

    public boolean playGameA(Game g, iHeuristic heuristic){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);
        // Key-Value map for <Node,Cost(Node)>
        HashMap<Node,Integer> cost = new HashMap<>();
        PriorityQueue<Node> unvisited = new PriorityQueue<>(new NodeComparator());
        HashSet<Node> unvisited_set = new HashSet<>();
        HashSet<Node> visited = new HashSet<>();
        // Node for the starting position in our game.
        // It has a State of g.start, and a depth of 0.
        Node head = new Node(g.start,0,null);
        unvisited.add(head);
        cost.put(head,heuristic.evaluate(g.start,g.goal));


        while(!unvisited.isEmpty()){
            Node current = unvisited.remove();
            visited.add(current);

            if(current.data.equals(g.goal)){
                System.out.println("Goal Found");
                Stack<State> temp = new Stack<>();
                while(current.parent != null){
                    temp.add(current.data);
                    current = current.parent;
                }
                while(!temp.isEmpty()){
                    previous_moves.add(temp.pop());
                }
                return true;
            }
            ArrayList<Offset> valid_moves = getApplicableMoves(current.data);

            for(Offset o : valid_moves){
                ++considered_states;
                State successor = executeMove(o,current.data);
                Node tempNode = new Node(successor,current.depth+1,current);
                tempNode.cost = tempNode.depth + heuristic.evaluate(tempNode.data,g.goal);

                if(visited.contains(tempNode) && cost.get(tempNode) > tempNode.cost){
                    visited.remove(tempNode);
                }
                if(unvisited.contains(tempNode) && cost.get(tempNode) > tempNode.cost){
                    unvisited.remove(tempNode);
                }
                if(!visited.contains(tempNode) && !unvisited_set.contains(tempNode)){
                    ++generated_states;
                    cost.put(tempNode,tempNode.depth+heuristic.evaluate(tempNode.data,g.goal));
                    unvisited.add(tempNode);
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
