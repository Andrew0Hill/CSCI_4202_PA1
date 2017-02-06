/**
 * Created by Andrew on 2/6/2017.
 */
public class RuleSet {
    public static boolean left(State s){
        return (s.zeroCol != 0);
    }
    public static boolean right(State s){
        return (s.zeroCol != (s.dim-1));
    }
    public static boolean up(State s){
        return (s.zeroRow != 0);
    }
    public static boolean down(State s){
        return(s.zeroRow != (s.dim-1));
    }
}
