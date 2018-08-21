import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by carolchen on 21/08/2016.
 */




public class KMeans {
    //initiate a field to store the number of clusters
    private int K;
    //initiate a field to store the file path
    public String docPath;
    //intiate a list of the clusters
    public HashMap<Integer, Cluster> clusters = new HashMap<>();
    //initiate a list of the points (vector space)
    public ArrayList<Point> points = new ArrayList<>();


    //Constructor
    public KMeans(int clusNum, String docPath) {

        this.docPath = docPath;
        this.K= clusNum;

        /*Create the vector space and do length normalization on each vectors*/
        //Build the document term frequency map
        DocMaps docMaps = new DocMaps();
        HashMap<String, HashMap<String, Double>> dtMap = docMaps.addFiles(docPath, ".txt", true);
        //for each document vector
        for (HashMap.Entry<String, HashMap<String, Double>> entry: dtMap.entrySet()) {
            //intialize the l2norm
            Double l2norm= 0.0;
            /*calculate the l2norm*/
            //for each position in the document vector
            for(HashMap.Entry<String, Double> entryTF: entry.getValue().entrySet()) {
                //get the frequency
                Double fre = entryTF.getValue();
                //square the frequency and add it to the l2norm
                l2norm += fre*fre;
            }
            //get the square root of the sum
            l2norm = Math.sqrt(l2norm);
            /*do normalization*/
            //for each position in the document vector
            for(HashMap.Entry<String, Double> entryTF: entry.getValue().entrySet()) {
                //get the term
                String term = entryTF.getKey();
                //calculate the normalized frequency, divided by the l2norm
                Double nfre= entryTF.getValue()/l2norm;
                //change the frequency
                entry.getValue().put(term, nfre);
            }

            /*create a new vector point and put it in the point list */
            Point newpoint = new Point(entry.getKey(), entry.getValue());
            points.add(newpoint);
        }
    }


    /*a method to initiate the clustering process*/
    public void initiate() {
        //pick random k points from the space
        Random r = new Random();
        int rnum = r.nextInt(points.size() - K);
        //for each cluster
        for(int i=0; i<K; i++) {
            //Create a cluster and set the centroid
            Cluster c = new Cluster(i);
            //set the centroid
            c.setCentroid(points.get(rnum+i));
            //put the cluster in the cluster map
            clusters.put(c.id, c);
        }
    }


    /*this is a method to assign points to a new cluster, and return a new assignment map: HashMap<cluster_id, the new Cluster>*/
    public HashMap<Integer, Cluster> assignPoints() {
        //store the new clusters
        HashMap<Integer, Cluster> newclusters = new HashMap<>();
        //for each point
        for (Point point: this.points) {
            Integer best_cluster_id = null;
            Double best_cluster_value = Double.MAX_VALUE;
            //for each cluster
            for (Cluster cluster: this.clusters.values()) {
                //if the distance from the point to the cluster centroid is smaller than the so far best cluster
                if(cluster.disToCen(point.vector)<best_cluster_value) {
                    //mark this cluster as the so far best cluster
                    best_cluster_id = cluster.id;
                    //record the so far best distance value
                    best_cluster_value = cluster.disToCen(point.vector);
                }
            }
            //store the result in the new cluster
            //if the cluster is already in the map
            if (newclusters.containsKey(best_cluster_id)) {
                //update the point list of the cluster
                newclusters.get(best_cluster_id).points.add(point);
            } else {//if the cluster is not in the map
                //create a new cluster, with the point in the point list
                Cluster newc = new Cluster(best_cluster_id);
                newc.addPoint(point);
                //add the new cluster in the map
                newclusters.put(best_cluster_id, newc);
            }

        }

        return newclusters;

    }

    public Boolean notchanged(HashMap<Integer, Cluster> new_clusters) {
        Boolean notchanged = false;
        Boolean has_equal_point = true;
        //for each cluster pair
        for (int i =0; i<K; i++) {
            //get the cluster pair
            Cluster new_cluster = new_clusters.get(i);
            Cluster old_cluster = this.clusters.get(i);

            //for each point in the old cluster
            for (Point point: old_cluster.points) {
                //see if the point is in the new cluster
                if (new_cluster.containPoint(point)) {
                    has_equal_point = has_equal_point&&true;
                } else {
                    has_equal_point = false;
                }
                notchanged=has_equal_point;
            }

        }


        return notchanged;

    }


    public HashMap<Integer, Cluster> clustering() {
        //Initiate the process
        initiate();

        //assign all the points to a cluster
        HashMap<Integer, Cluster> new_clusters = assignPoints();

        //upper bound
        Integer upperbound = 1000;
        Integer count = 0;

        //while the assignment is changed
        while ((count<upperbound)&&(!notchanged(new_clusters))) {

            count++;
            //update the clusters
            this.clusters = new_clusters;
            //update the centroid of each clusters
            for (HashMap.Entry<Integer, Cluster> cluster: this.clusters.entrySet()) {
                cluster.getValue().updateCen();
            }
            //re-assign the points, update the new_clusters
            new_clusters = assignPoints();

            System.out.println("----------------one iteration------------------");
            for (HashMap.Entry<Integer, Cluster> entry: this.clusters.entrySet()) {
                System.out.println("Cluster number " +entry.getKey());
                System.out.println("has files: "+entry.getValue().filename());
            }
        }

        return this.clusters;

    }


    public void top5() {
        //for each cluster
        for (HashMap.Entry<Integer, Cluster> entry: this.clusters.entrySet()) {

            //create a Hashmap to store the distance and the filename: Hashmap<filename, distance>
            HashMap<String, Double> top5 = new HashMap<>();
            //if there are less than 5 points in the cluster
            if(entry.getValue().points.size()<=5) {
                //just print the filename of the 5 points out
                System.out.println("Cluster number: " +entry.getKey());
                System.out.println("Has top 5 files: "+entry.getValue().filename());
                System.out.println("**********************************");
            } else {//if there are more than 5 points in the cluster
                //store the first 5 point first in the top 5 map first
                for (int i=0; i<5; i++) {
                    //get the file name
                    String file_name = entry.getValue().points.get(i).id;
                    //calculate their distance to centroid
                    Double distance = entry.getValue().disToCen(entry.getValue().points.get(i).vector);
                    //put it in top 5
                    top5.put(file_name, distance);
                }
                //for each of the rest points
                for (int i=5; i<entry.getValue().points.size(); i++) {
                    //get the file name
                    String file_name = entry.getValue().points.get(i).id;
                    //calculate their distance to centroid
                    Double distance = entry.getValue().disToCen(entry.getValue().points.get(i).vector);


                    //compare with the current top 5
                    for (HashMap.Entry<String, Double> top5entry: top5.entrySet()) {
                        // if the distance is smaller that any entries of the top 5
                        if (distance<top5entry.getValue()) {

                            //remove key
                            String remove = top5entry.getKey();

                            //remove this entry
                            top5.remove(remove);
                            //put the new entry
                            top5.put(file_name, distance);

                            break;
                        }
                    }
                }

                System.out.println("Cluster number " +entry.getKey());
                System.out.println("Has top 5 files: ");
                for(String file: top5.keySet()) {
                    System.out.println(file);
                }
                System.out.println("***********************************");

            }
        }

    }




    public static void main(String[] args) {

        KMeans km = new KMeans(3, "/Users/carolchen/Desktop/KMeansTextClustering/blog_data_test");
        km.clustering();
        System.out.println("********************************************************************");
        System.out.println("*******************************top5*********************************");
        km.top5();




    }







}
