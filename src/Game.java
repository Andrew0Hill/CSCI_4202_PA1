/**
 * Created by Andrew on 2/5/2017.
 */
public class Game {
    int n;
    State start;
    State goal;

    public boolean validate(){
        // Check that each field exists and that N is a non-negative integer.
        if(this.n <= 0 || this.start == null || this.goal == null){
            return false;
        }else{
            // Validate the start and goal states.
            if(!start.isValid(n) || !goal.isValid(n)){
                return false;
            }
        }
        return true;
    }
}
