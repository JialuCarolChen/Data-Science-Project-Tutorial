import java.util.HashMap;

/**
 * Created by carolchen on 22/08/2016.
 */
public class Point {
    //initialize a field to store the name of file
    public String id;
    //initialize a field to store the vector point
    public HashMap<String, Double> vector;

    //Constructor
    public Point(String id, HashMap<String, Double> vector) {
        this.id = id;
        this.vector = vector;
    }



}
