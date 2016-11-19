// A Java program for Floyd Warshall All Pairs Shortest
// Path algorithm.
import java.util.*;
import java.lang.*;
import java.lang.reflect.Array;
import java.io.*;
 
 
class AllPairShortestPath
{
	private int dist[][];
	private int pred[][];
	private static int flow[][];
	private static int linked[][];
	private int minflow[][];
	private int maxflow[][];
	private int avgflow[][];
	private String pathMatrix[][];
    final static int INF = 99999;
    private int V = 1000;
    private ArrayList<Paths> tflow = new ArrayList<Paths>();
    private int sneakyStart,sneakyEnd; 
    void floydWarshall(int graph[][])
    {

        int i, j, k;
 
        /* Initialize the solution matrix same as input graph matrix.
           Or we can say the initial values of shortest distances
           are based on shortest paths considering no intermediate
           vertex. */
        for (i = 0; i < V; i++)
            for (j = 0; j < V; j++){
            	if (graph[i][j] != 0)
            		dist[i][j] = graph[i][j];
            	else if (i != j && linked[i][j] == 0)
            		dist[i][j] = INF;
            	else
            		dist[i][j] = 0;
                flow[i][j] = 0;
        		if (i != j)
        			pred[i][j] = i+1;
            }


 
        /* Add all vertices one by one to the set of intermediate
           vertices.
          ---> Before start of a iteration, we have shortest
               distances between all pairs of vertices such that
               the shortest distances consider only the vertices in
               set {0, 1, 2, .. k-1} as intermediate vertices.
          ----> After the end of a iteration, vertex no. k is added
                to the set of intermediate vertices and the set
                becomes {0, 1, 2, .. k} */
        for (k = 0; k < V; k++)
        {
            // Pick all vertices as source one by one
            for (i = 0; i < V; i++)
            {
                // Pick all vertices as destination for the
                // above picked source
                for (j = 0; j < V; j++)
                {
                    // If vertex k is on the shortest path from
                    // i to j, then update the value of dist[i][j]
                	// update predecessor matrix to reflect new path
                    if (dist[i][k] + dist[k][j] < dist[i][j]){
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pred[i][j] = pred[k][j];
                    }
                }
            }
        }
 
        // Print the shortest distance matrix
        printSolution(dist);
        printSolution(pred);
    }
 
    void printSolution(int matr[][])
    {
        System.out.println("Following matrix shows the shortest "+
                         "distances between every pair of vertices");
        for (int i=0; i<V; ++i)
        {
            for (int j=0; j<V; ++j)
            {
                if (matr[i][j]==INF)
                    System.out.print("INF ");
                else
                    System.out.print(matr[i][j]+"   ");
            }
            System.out.println();
        }
    }
    
    ArrayList giveMeThePath(int start, int end){
    	int current = end-1;
    	int begin = start -1;
    	ArrayList path = new ArrayList();
    	path.add(end);
    	while (pred[begin][current] != start){
    		current = pred[begin][current]-1;
    		path.add(current+1);
    	}
    	path.add(start);
		return path;
    }
    
    void makeAdjacencyMatrix(List<Integer> path, int edgeFlow){
    	int start =  path.get(0);
    	int current;
    	for (int i = 1; i < path.size(); i++){
    		current = path.get(i);
    		flow[current-1][start-1] += edgeFlow;
    		start = current;
    	}
    }
    
    void parseFlowList(){
    	List hereIsThePath;
    	for (Paths current : tflow){
    		hereIsThePath = giveMeThePath(current.begin+1, current.end+1);
    		makeAdjacencyMatrix(hereIsThePath, current.flow);
    	}
    	printSolution(flow);
    }
    
    int[][] parseInput(String filename){

    	File file = new File(filename);
    	BufferedReader reader = null;
    	int start = 1;
    	int end = 2;
    	int weight = 0;
    	boolean edge;
    	String ed = null;
    	int graph[][];
    	try {
    	    reader = new BufferedReader(new FileReader(file));
    	    String text = null;
    	    int linecount = 0;
    	    while ((text = reader.readLine()) != null){
    	    	String[] line = text.split(",");
    	    	if (line.length == 3){
    	    		V = Integer.parseInt(line[0].trim());
    	    		sneakyStart = Integer.parseInt(line[1].trim())-1;
    	    		sneakyEnd = Integer.parseInt(line[2].trim())-1;
    	    		// if the parameters were not at the top of the file, close and reopen to return to the beginning
    	    		if (linecount != 0){
    	    			reader.close();
    	    			reader = new BufferedReader(new FileReader(file));
    	    	}
    	    	break;
    	    	}
    	    	linecount++;
    	    }
    	}
    	    catch(Exception e){
    	    	e.printStackTrace();
    	    }
    	    graph = new int [V][V];
    	    try{
        	    String text = null;
    	    while ((text = reader.readLine()) != null) {
    	    	if (!text.equals("")){
    	    	String[] line = text.split(",");
    	    	if (line.length == 4){
    	    		ed = line[0].trim();
    	    		start = Integer.parseInt(line[1].trim())-1;
    	    		end = Integer.parseInt(line[2].trim())-1;
    	    		weight = Integer.parseInt(line[3].trim());
    	    	}
        	    if (ed != null && ed.equals("E")){
        	    	graph[start][end] = weight;
        	    }
        	    else if (ed != null && ed.equals("F")){
        	    	Paths temp = new Paths(start, end, weight);
        	    	tflow.add(temp);
        	    }
    	    }
    	    }
    	    

    	} catch (FileNotFoundException e) {
    	    e.printStackTrace();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    	    try {
    	        if (reader != null) {
    	            reader.close();
    	        }
    	    } catch (IOException e) {
    	    }
    	}
    	linked = new int [graph.length][graph.length];
    	for (int i = 0; i < graph.length; i++){
    		for (int j = 0; j < graph.length; j++){
    			if (graph[i][j] != 0)
    				linked[i][j] = 1;
    		}
    	}
        dist = new int[V][V];
        pred = new int[V][V];
        flow = new int[V][V];
        minflow = new int[V][V];
    	maxflow = new int[V][V];
    	avgflow = new int[V][V];
    	pathMatrix = new String [V][V];
    	return graph;
    }
    void printflow(){
    	printSolution(flow);
    }
    
    void generateOutput(int[][] input, int[][] predessor){
    	int max, min, count;
    	String path = "";
    	String pathCorrect;
    	for (int i = 0; i < input.length; i++){
    		for (int j = 0; j < input.length; j++){
    			if (i == j)
    				pathMatrix[i][j] = Integer.toString(input[i][j]);
    			else{
    		    	int current = j-1;
    		    	int begin = i -1;
    		    	path += j + ", ";
    		    	while (pred[begin][current] != i){
    		    		current = pred[begin][current]-1;
    		    		path += current + ", "; 
    		    	}
    		    	path += i;
    			}
    			pathCorrect = new StringBuilder(path).reverse().toString();
    		}
    	}
    } 

	public static void main(String[] args) {
		/*
        int graph[][] = { {0,   INF,  3, INF, INF},
                          {4, 0,   INF, 1, 2},
                          {3, 8, 0, 2, 6},
                          {INF, 1, INF, 0, 4},
                          {INF, 1, 6, 4, 0}
                        };
        */
        ArrayList testFlow = new ArrayList();
        AllPairShortestPath a = new AllPairShortestPath();
        int graph[][] = a.parseInput("sneakypathinput.txt");
        // Print the solution
        a.floydWarshall(graph);
        System.out.println("shortest path is as follows:  ");
        ArrayList thePath = a.giveMeThePath(6, 1);
        System.out.println(Arrays.toString(thePath.toArray()));
        a.parseFlowList();
        a.printflow();
        a.floydWarshall(flow);
        thePath = a.giveMeThePath(2, 1);
        System.out.println(Arrays.toString(thePath.toArray()));

	}
}