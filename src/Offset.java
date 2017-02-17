/**
 * Created by Andrew on 2/8/2017.
 */
public class Offset {
    public static final Offset EMPTY = new Offset(-1,-1);
    int row;
    int col;
    Offset(int r, int c){
        row=r;
        col = c;
    }
    Offset(){
        row = -1;
        col = -1;
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Offset)){
            return false;
        }
        if(o == this){
            return true;
        }
        Offset t = (Offset)o;

        return ((t.col == this.col) && (t.row == this.row));
    }
}
