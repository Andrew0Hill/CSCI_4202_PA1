/**
 * A set of rules used to play the game.
 * Accessed through the iRule interface.
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
    public static boolean down(State s){ return(s.zeroRow != (s.dim-1)); }
}
