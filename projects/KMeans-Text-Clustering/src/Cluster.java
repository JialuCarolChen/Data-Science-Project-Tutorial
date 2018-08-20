import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by carolchen on 22/08/2016.
 */
public class Cluster {

    public ArrayList<Point> points;
    public HashMap<String, Double> centroid;
    public int id;


    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList<Point>();
        this.centroid = new HashMap<>();
    }


    /*a method to add a point in to the cluster*/
    public void addPoint(Point newpoint) {
        points.add(newpoint);
    }


    /*a method to check if the cluster contain a point*/
    public boolean containPoint(Point point) {
        boolean haspoint = false;
        for (Point cpoint: points) {
            if(cpoint.id==point.id) {
                haspoint = true;
            }
        }
        return haspoint;
    }

    /*set a point as the centroid*/
    public void setCentroid(Point point) {
        this.centroid = point.vector;
    }


    /*a method to calculate and update the centroid*/
    public void updateCen() {
        //intialize a new point as centroid
        HashMap<String, Double> newCentroid = new HashMap<>();

        /*Sum up all the points*/
        //for each point in the point list
        for (Point point: points) {
            //for each entry set
            for (HashMap.Entry<String, Double> entry: point.vector.entrySet()) {
                //get the term and the frequency
                String term = entry.getKey();
                Double fre = entry.getValue();
                //if the term is already in the map of the centroid
                if(newCentroid.containsKey(term)) {
                    //get the frequency of the term in the centorid
                    Double cenfre = newCentroid.get(term);
                    //sum the frequency
                    newCentroid.put(term, fre+cenfre);
                } else {
                    //if the term is not in the map of the centroid, just put the frequency in
                    newCentroid.put(term, fre);
                }

            }
        }

        /*get the mean value*/
        //calculate the number of points
        Integer numpoint = points.size();
        //divide the sum frequency with the number of the point
        for (HashMap.Entry<String, Double> entry: newCentroid.entrySet()) {
            //get the term
            String term = entry.getKey();
            //caculate the mean
            Double mean = entry.getValue()/numpoint;
            //put the mean value in the map of the centroid
            newCentroid.put(term, mean);
        }

        /*set the centroid*/
        this.centroid = newCentroid;

    }


    /*a method to calculate the Cosine distance between the centroid and a point*/
    /*for normalized vector points, the distance equals to the dot product*/
    public Double disToCen(HashMap<String, Double> vecpoint) {
        //initialize the distance
        Double cosdis = 0.0;

        //for each position in the point vector
        for (HashMap.Entry<String, Double> entry: vecpoint.entrySet()) {
            //get the name of the position(term)
            String position = entry.getKey();
            //get the value of the position(frequency)
            Double val = entry.getValue();
            //if this position is also in the centroid vector
            if (this.centroid.containsKey(position)) {
                //get the value of the position
                Double cenval = this.centroid.get(position);
                //multiply the two value and add it to the distance
                cosdis += cenval*val;
            }

        }
        return cosdis;
    }

    /*get the file name of the points in the cluster*/
    public ArrayList<String> filename() {
        ArrayList<String> filename = new ArrayList<>();
        for (Point point: points) {
            filename.add(point.id);
        }
        return filename;
    }









}
