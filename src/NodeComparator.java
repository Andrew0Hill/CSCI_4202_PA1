import java.util.Comparator;

/**
 * Created by Andrew on 2/17/2017.
 */
public class NodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2){
        return ((n1.depth+n1.cost) - (n2.depth+n2.cost));
    }
}
