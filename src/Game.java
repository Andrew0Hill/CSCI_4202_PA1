/**
 * Created by Andrew on 2/5/2017.
 */
public class Game {
    int n;
    int[][] start;
    int[][] goal;

    public boolean validate(){
        // Check that each field exists and that N is a non-negative integer.
        if(this.n <= 0 || this.start == null || this.goal == null){
            return false;
        }else{
            //Check length of both dimensions of array.
            if(start.length != n || start[0].length != n){
                return false;
            }
            // Create boolean array of size n*n. We use the indices of the array to count the appearances of numbers.
            boolean used[] = new boolean[n*n];
            for(int[] a : start){
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
            used = new boolean[n*n];
            for(int[] a : goal){
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
        }
        return true;
    }
}
