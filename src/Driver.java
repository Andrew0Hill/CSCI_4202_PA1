import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/5/2017.
 */
public class Driver {
    public static void main(String[] args){
        JSON_Parse parser = new JSON_Parse();
        Game game;
        try {
            game = parser.parseFromFile(args[0]);
            if(game != null && game.validate()){
                System.out.println("Valid game received.");
            }
            System.out.println("Beginning Backtracking Depth-First Search");
            Player p = new Player();
            //p.printGameState(game.start);
            p.playGame(game);
            System.out.println("Solution found in:" + p.previous_moves.size());
            p.previous_moves.forEach(State::print);
            p.playGameIterative(game);
            System.out.println("Solution found in:" + p.previous_moves.size());
            p.previous_moves.forEach(State::print);
        }catch(FileNotFoundException ex){
            System.out.println("File not found. Please try again.");
        }


    }

}
