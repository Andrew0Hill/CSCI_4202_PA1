/**
 * Created by Andrew on 2/5/2017.
 */
public class State {
    int[][] state;
    boolean isValid(int n){
        //Check length of both dimensions of array.
        if(state.length != n || state[0].length != n){
            return false;
        }
        // Create boolean array of size n*n. We use the indices of the array to count the appearances of numbers.
        boolean used[] = new boolean[n*n];
        for(int[] a : state){
            for(int b : a){
                if(b > n*n || b < 0){return false;}
                used[b] = true;
            }
        }
        for(boolean b : used){
            if(!b){
                return false;
            }
        }
        return true;
    }
}
