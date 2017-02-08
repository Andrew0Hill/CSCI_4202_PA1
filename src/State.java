import java.util.Arrays;

/**
 * Created by Andrew on 2/5/2017.
 */
public class State {
    int[][] state;
    int dim;
    int zeroRow = 0;
    int zeroCol = 0;
    public State(){
    }
    public State(State old){
        this.dim = old.dim;
        this.zeroRow = old.zeroRow;
        this.zeroCol = old.zeroCol;
        this.state = new int[old.dim][old.dim];
        for(int i = 0; i < old.dim; ++i){
            for(int k = 0; k < old.dim; ++k){
                this.state[i][k] = old.state[i][k];
            }
        }
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof State)){
            return false;
        }
        if(o == this){
            return true;
        }
        State temp = (State)o;
        if(this.dim != temp.dim || this.zeroRow != temp.zeroRow || this.zeroCol != temp.zeroCol){
            return false;
        }
        for(int i = 0; i < this.dim; ++i){
            for(int k = 0; k < this.dim; ++k){
                if(this.state[i][k] != temp.state[i][k]){
                    return false;
                }
            }
        }
        return true;
    }
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

    public void print() {
        for(int[] row: this.state){
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
