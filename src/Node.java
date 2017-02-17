import java.util.ArrayList;

/**
 * Created by Andrew on 2/17/2017.
 */
public class Node {
    State data;
    Node parent;
    Node(State s){
        data = new State(s);
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
