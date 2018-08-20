
***************************************************
Point: The Point class represent a point

-Fields:
*id: the id of a point is the name of the file, stored as a string
*vector: the vector represent the file terms and their frequencies, stored as a hash map

***************************************************
Cluster: The Cluster class represent a cluster.

—Fields: 
*id: the id of the cluster
*centroid: the centroid of the cluster, represented as a vector point, stored as a Hashmap<Term, Frequency>
*points: an Array list of all the points in the cluster

-Methods:
*addPoint(Point point): This method add a new point to the cluster
*containPoint(Point point): Given a point, this method check if this point is in the cluster, if the point is in the cluster, the method will return true, otherwise return false
*setCentroid(Point point): This method set a point as the centroid of the cluster
*updateCen(): This method update the centroid by calculating the mean of all the points containing in the cluster  
*disToCen(HashMap<String, Double>):This method calculate the Cosine distance between a given point and the centroid of the cluster
*filename(): This method return all the filenames of the points as an Arraylist

***************************************************
DocMaps: This class map the terms and frequencies of all the files

-Method:
*addFiles(String src, String ext, boolean recurse): This class reads-in all the files from the resources, tokenises the terms (stop words have been droped) and calculate their frequencies, and store the result in an HashMap<file_name, HashMap<Term, Frequency>>.
A StopWordChecker(from the ML lab) has been used to drop all the stop words.
A SimpleTokenizer(from the ML lab) has been used to tokenise the files

***************************************************
KMeans: This class do the k mean clustering procedure

-Fiels:
*K: a field to store the number of clusters
*docPath: a field to store the file path
*clusters: a field to store the clusters
*points: a field to store the list of points in the vector space 

-Constructor:
In the constructor, a DocMap is used to map the terms and the frequencies of the files, and a vector space is builded based on the mapping result. All the vector in the vector space has been normalized. 

-Methods: 
*initiate(): a method to initiate the clustering process (assign random points as the centroid of the clusters)
*notchanged(): a method to check that if the cluster has been changed
*clustering(): a method to do the clustering process
*top5(): a method to get the top 5 ranking document in the cluster


 