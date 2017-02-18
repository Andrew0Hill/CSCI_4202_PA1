import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/5/2017.
 */
public class Driver {
    public static void main(String[] args) {
        JSON_Parse parser = new JSON_Parse();
        Game game;
        Player p = new Player();
        try {
            /*
            Parse the game from a file.
             */
            game = parser.parseFromFile(args[0]);
            if (game != null && game.validate()) {
                System.out.println("Valid game received.");
            }

            /*
            Backtracking Start
             */
            System.out.println("Beginning Backtracking Depth-First Search");

            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if (p.playGame(game)) {
                System.out.println("Solution found in: " + (p.previous_moves.size() - 1) + " moves.");
                System.out.println("Generated States: " + p.generated_states);
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            } else {
                System.out.println("Error: Solution not found.");
            }
            /*
            Backtracking End

            Iterative Backtracking Start
             */
            System.out.println("Beginning Iterative Depth-First Search");

            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if (p.playGameIterative(game)) {
                System.out.println("Optimal Solution found in: " + (p.previous_moves.size() - 1) + " moves.");
                System.out.println("Generated States: " + p.generated_states);
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            } else {
                System.out.println("Error: Solution not found.");
            }
            /*
            Iterative Backtracking End

            Graph Search Start
             */
/*            System.out.println("Beginning Graph Search:");

            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if (p.playGameGraphSearch(game)) {
                System.out.println("Solution found in: " + (p.previous_moves.size() - 1) + " moves.");
                System.out.println("Generated States: " + p.generated_states);
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            } else {
                System.out.println("Error: Solution not found.");
            }*/
            /*
            Graph Search End

            A* Search Begin
             */
            System.out.println("Beginning A* Search:");
            System.out.println("Start:");
            p.printGameState(game.start);
            System.out.println("Goal:");
            p.printGameState(game.goal);

            if (p.playGameA(game, Heuristics::h2)) {
                System.out.println("Solution found in: " + (p.previous_moves.size() - 1) + " moves.");
                System.out.println("Generated States: " + p.generated_states);
                System.out.println("Considered States: " + p.considered_states);
                for (State s : p.previous_moves) {
                    p.printGameState(s);
                }
            } else {
                System.out.println("Error: Solution not found.");
            }
            /*
            A* Search End
             */
            System.out.println(Heuristics.h2(game.start, game.goal));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Please try again.");
        }


    }

}
