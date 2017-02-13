import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/5/2017.
 */
public class Driver {
    public static void main(String[] args){
        JSON_Parse parser = new JSON_Parse();
        Game game;
        Player p = new Player();
        try {
            game = parser.parseFromFile(args[0]);
            if(game != null && game.validate()){
                System.out.println("Valid game received.");
            }
            System.out.println("Beginning Backtracking Depth-First Search");

            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if(p.playGame(game)) {
                System.out.println("Solution found in:" + p.previous_moves.size() + " moves.");
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            }else{
                System.out.println("Error: Solution not found.");
            }
            System.out.println("Beginning Iterative Depth-First Search");

            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if(p.playGameIterative(game)) {
                System.out.println("Optimal Solution found in:" + p.previous_moves.size() + " moves.");
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            }else{
                System.out.println("Error: Solution not found.");
            }
        }catch(FileNotFoundException ex){
            System.out.println("File not found. Please try again.");
        }


    }

}
