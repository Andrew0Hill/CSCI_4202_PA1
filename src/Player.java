import sun.misc.*;

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
    public boolean playGameIterative(Game g, int targetbound){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);
        // Bound starts at 1 and increments to 'targetbound'.
        int bound = 1;
        while(bound < targetbound && !playGame(previous_moves,g.goal,bound)){
            previous_moves.clear();
            previous_moves.add(g.start);
            ++bound;
        }
        if(bound == targetbound){
            return false;
        }else {
            return true;
        }
    }
    public boolean playGame(LinkedList<State> prev_moves, State target,int depthBound){
        // If we hit a depth bound, we're done. Return up the tree until we can make a different move.
        if(prev_moves.size() > depthBound){
            return false;
        }
        // We have found the correct path. End here.
        if(prev_moves.peekLast().equals(target)){
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

        Node head = new Node(g.start);
        head.depth = 0;
        // Queue for unexplored Nodes.
        Deque<Node> unvisited = new ArrayDeque<>();

        // The unvisited_set holds all members of the unvisited queue. When a member is added to the unvisited queue,
        // it is also added to this set. This set allows us to test for existence in the Queue in effectively O(1) time,
        // instead of the O(n) time provided by the .contains() function in the queue.
        HashSet<Node> unvisited_set = new HashSet<>();
        // Set to hold all visited nodes.
        HashSet<Node> visited = new HashSet<>();
        // Add the first node to the unvisited list.
        unvisited.add(head);
        while(!unvisited.isEmpty()){
            // Get current node.
            Node n = unvisited.remove();
            visited.add(n);
            // If this node is the goal we're done, so reconstruct the path using the Node parent references.
            // Here we use a stack reverse the order we put them on the previous_moves list in.
            if(n.data.equals(g.goal)){
                System.out.println("Goal Found");
                Stack<State> temp = new Stack<>();
                while(n.parent != null){
                    temp.add(n.data);
                    n = n.parent;
                }
                while(!temp.isEmpty()){
                    previous_moves.add(temp.pop());
                }
                return true;
            }else{
                // Generate the list of applicable moves for this state.
                ArrayList<Offset> moves = getApplicableMoves(n.data);
                // For each applicable move:
                for(Offset o : moves){
                    // We're considering using this state, so increment the count.
                    ++considered_states;
                    // Generate the resulting state if we make this move.
                    State newState = executeMove(o,n.data);
                    // Create a new Node to hold this state.
                    Node newNode = new Node(newState);
                    // Set the parent pointer and depth.
                    newNode.parent = n;
                    newNode.depth = n.depth+1;
                    // If this node isn't in the unvisited or visited sets:
                    if(!unvisited_set.contains(newNode) && ! visited.contains(newNode)){
                        // We're going to generate this state, so increment the count.
                        ++generated_states;
                        // Add to the unvisited Queue and the unvisited HashSet.
                        unvisited.add(newNode);
                        unvisited_set.add(newNode);
                    }

                }
            }
        }
        // If we get to the end and don't find anything, the game is unsolvable.
        return false;
    }

    public boolean playGameA(Game g, iHeuristic heuristic){
        considered_states = 0;
        generated_states = 0;
        previous_moves.clear();
        previous_moves.add(g.start);
        // Key-Value map for <Node,Cost(Node)> Holds the current "best" cost to a given Node.
        HashMap<Node,Integer> cost = new HashMap<>();
        // Priority Queue. Sorts Nodes according to their f(n)+g(n) cost.
        PriorityQueue<Node> unvisited = new PriorityQueue<>(new NodeComparator());
        // 'unvisited_set' lets us check for membership in the 'unvisited' PriorityQueue in O(1) time.
        // Decreases the runtime of the algorithm significantly for larger puzzles.
        HashSet<Node> unvisited_set = new HashSet<>();
        // Set for the Nodes we've visited before.
        HashSet<Node> visited = new HashSet<>();
        // Node for the starting position in our game.
        // It has a State of g.start, and a depth of 0.
        Node head = new Node(g.start,0,null);
        // Add the head node to the unvisited Queue.
        unvisited.add(head);
        // Evaluate the current best cost to this Node, and add it to the HashMap.
        cost.put(head,heuristic.evaluate(g.start,g.goal));

        // While the unvisited queue has nodes:
        while(!unvisited.isEmpty()){
            // Get the current node.
            Node current = unvisited.remove();
            visited.add(current);
            // If this is the goal node, we're all done. Create the path of moves used to get here.
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
            // Otherwise, get the set of valid moves for this state.
            ArrayList<Offset> valid_moves = getApplicableMoves(current.data);
            // For each of these moves:
            for(Offset o : valid_moves){
                // We're considering this move, so increment it.
                ++considered_states;
                // Get the successor state if we make this move.
                State successor = executeMove(o,current.data);
                // Create a node for this new State.
                Node tempNode = new Node(successor,current.depth+1,current);
                // Calculate the cost for this Node.
                tempNode.cost = tempNode.depth + heuristic.evaluate(tempNode.data,g.goal);
                // If we've been to this node before, but it has a lower cost this time, we want to replace it,
                // so remove from the visited set.
                if(visited.contains(tempNode) && cost.get(tempNode) > tempNode.cost){
                    visited.remove(tempNode);
                }
                // If this Node is one that we're going to explore soon, but this cost is lower than the other cost,
                // remove that node, because we'll add it again with a better cost.
                if(unvisited_set.contains(tempNode) && cost.get(tempNode) > tempNode.cost){
                    unvisited.remove(tempNode);
                }
                // If the node is brand new (or is replacing a higher cost node), we calculate the cost and add
                // it to the map.
                if(!visited.contains(tempNode) && !unvisited_set.contains(tempNode)){
                    ++generated_states;
                    cost.put(tempNode,tempNode.depth+heuristic.evaluate(tempNode.data,g.goal));
                    unvisited.add(tempNode);
                    unvisited_set.add(tempNode);
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
