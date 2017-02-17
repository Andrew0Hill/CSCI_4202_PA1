import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Andrew on 2/17/2017.
 */
public class Heuristics {
    public static int h1(State s1, State s2){
        int misplaced_tiles = 0;
        for(int i = 0; i < s1.dim; ++i){
            for(int j = 0; j < s1.dim; ++j){
                if(s1.state[i][j] != s2.state[i][j]){
                    ++misplaced_tiles;
                }
            }
        }
        return misplaced_tiles;
    }
    public static int h2(State s1, State s2){
        int sum_distance = 0;
        int[] m_dist = new int[s1.dim*s1.dim];
        for(int i = 0; i < s1.dim; ++i){
            for(int j = 0; j < s1.dim; ++j){
                if(s1.state[i][j] != s2.state[i][j]){
                    for(int x = 0; x < s1.dim; ++x){
                        for(int y = 0; y < s1.dim; ++y){
                            if(s2.state[x][y] == s1.state[i][j]){
                                int curr = s2.state[x][y];
                                m_dist[curr] = (Math.abs(i-x)+Math.abs(j-y));
                            }
                        }
                    }
                }
            }
        }
        for(int d : m_dist){
            sum_distance +=d;
        }
        return sum_distance;
    }
}
