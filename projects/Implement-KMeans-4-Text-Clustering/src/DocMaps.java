import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Created by carolchen on 21/08/2016.
 */
public class DocMaps {


    //read each document in the directory, and create
    public HashMap<String, HashMap<String, Double>> addFiles(String src, String ext, boolean recurse) {

        //The DocumentMap store a term frequency map for each document(HashMap<File_name, HashMap(String, Interger)>)
        HashMap<String, HashMap<String, Double>> DocumentMap = new HashMap<>();

        //create a file finder
        FileFinder finder = new FileFinder();
        //get all the files and store it as an ArrayList
        ArrayList<File> files = finder.GetAllFiles(src, ext, recurse);

        //for each file
        for(File file: files) {
            //create a HashMap to store the term frequency
            HashMap<String, Double> termFre = new HashMap<>();

            //read the file and count the frequency of each word
            BufferedReader br = null;
            try {
                //read the file and store the content
                br = new BufferedReader(new FileReader(file));
                String content = new String();
                String line = null;
                while ((line = br.readLine()) != null) {
                    content += line;
                }
                //tokenize the content
                SimpleTokenizer st = new SimpleTokenizer();
                ArrayList<String> tokens = st.extractTokens(content, true);
                //create a stopwordchecker to filter the stopwords
                StopWordChecker swc = new StopWordChecker();
                //for each token
                for (String token : tokens) {
                    //check to make sure the token is not a stop-word
                    if (!swc.isStopWord(token)) {
                        //count the term frequency while adding it to the termFre map:
                        //if the token is already in the term frequency map
                        if (termFre.containsKey(token)) {
                            //update the term frequency +1
                            termFre.put(token, termFre.get(token)+1.0);
                        }else {//otherwise just put the token in the map, with term frequency 1
                            termFre.put(token,1.0);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not add file '" + file + "': " + e);
                e.printStackTrace(System.err);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't close reader for file '" + file + "': " + e);
                }
            }

            /*For Testing*/
            //System.out.println(file.getName());
            //System.out.println(termFre);

            //store the term frequency map in the DocumentMap with the file name
            DocumentMap.put(file.getName(), termFre);
        }

        return DocumentMap;

    }

    public static void main(String[] args) {
        String path = "/Users/carolchen/Desktop/KMeansTextClustering/blog_data";
        DocMaps dm = new DocMaps();
        System.out.println(dm.addFiles(path, ".txt", true));
    }
    




}
