/**
 * Created by Andrew on 2/5/2017.
 */
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PipedInputStream;

public class JSON_Parse {
    public Game parseFromFile(String filename) throws FileNotFoundException{
        Gson gson = new Gson();
        JsonReader input = new JsonReader(new FileReader(filename));
        return gson.fromJson(input,Game.class);
    }
}
