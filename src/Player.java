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
        // Key-Value map for <Node,Cost(Node)>
        HashMap<Node,Integer> visited = new HashMap<>();
        ArrayDeque<Node> unvisited = new ArrayDeque<>();

        // Node for the starting position in our game.
        // It has a State of g.start, and a depth of 0.
        Node head = new Node(g.start,0,null);
        unvisited.add(head);
        while(!unvisited.isEmpty()){
            // Get the first Node in the Queue
            Node current = unvisited.removeFirst();
            if(current.data.equals(g.goal)){
                System.out.println("Solution found.");
                return true;
            }
            ArrayList<Offset> moves = getApplicableMoves(current.data);

            for(Offset candidate : moves){
                // Try a new move from the set of valid moves.
                State newState = executeMove(candidate,current.data);
                // New Node, with the corresponding state added, a depth of current+1, and a parent of "current"
                Node newNode = new Node(newState,current.depth+1,current);

                // Node is brand new, it isn't in either the known visited or unvisited node sets.
                if(!visited.containsKey(newNode) && !unvisited.contains(newNode)){
                    unvisited.add(newNode);
                }
                // This node is visited before. We should check if it can offer a cheaper cost this time.
                else if(visited.containsKey(newNode)){
                    if(visited.get(newNode) > newNode.depth) {
                        // Replace the node with our newNode.
                        /*
                        Note: The Hash function for Node is designed so that only the "board state"
                        is a member of the hash. This means that even we're using newNode as a key in our HashMap,
                        the values of all other members of the HashMap's matching Node can differ from ours. By
                        replacing the Node in the HashMap with out NewNode, we "update" the values of all the Node's
                        members (in this case, the "predecessor" reference.
                         */
                        visited.put(newNode, newNode.depth);
                    }
                }
                // This node is currently on our unvisited stack. We don't need to visit twice, so we'll check if it
                // offers a lower cost this time and update it if so.
                else if(unvisited.contains(newNode)){
                    for(Node n : unvisited){
                        if(n.equals(newNode) && n.depth > ){
                        }
                    }
                }
            }




        }
    }

    public boolean playGameA(Game g, iHeuristic heuristic){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);


    }
    public void printGameState(State s){
        for(int[] row: s.state){
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
