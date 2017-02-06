import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/5/2017.
 */
public class Driver {
    public static void main(String[] args){
        JSON_Parse parser = new JSON_Parse();
        Game game;
        State s = new State();
        try {
            game = parser.parseFromFile(args[0]);
            if(game != null && game.validate()){
                System.out.println("Valid game received.");
            }
        }catch(FileNotFoundException ex){
            System.out.println("File not found. Please try again.");
        }
        Player p = new Player();
        ArrayList<int[]> possibleMoves = p.getApplicableMoves(s);
    }

}
