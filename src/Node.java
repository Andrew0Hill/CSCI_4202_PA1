import java.util.ArrayList;

/**
 * Created by Andrew on 2/17/2017.
 */
public class Node {
    State data;
    Node parent;
    int depth;
    int cost;
    Node(State s){
        data = new State(s);
    }
    Node(State s, int dp, int cst){
        data = new State(s);
        depth = dp;
        cost = cst;
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)){
            return false;
        }
        if(o == this){
            return true;
        }
        Node n = (Node)o;

        // Return true if the State held in this node is equal to the state held in another node.
        return (n.data.equals(this.data));
    }
}
