/**
 * Created by Andrew on 2/5/2017.
 */
public class State {
    int[][] state;
    int dim;
    int zeroRow = 0;
    int zeroCol = 0;
    boolean isValid(int n){
        dim = n;
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
        getZeroPos();
        return true;
    }
    public void getZeroPos(){
        for(int i = 0; i < dim; ++i){
            for(int n = 0; n < dim; ++n){
                if(state[i][n] == 0){
                    zeroRow = i;
                    zeroCol = n;
                }
            }
        }
    }
}
