// A Java program for Floyd Warshall All Pairs Shortest
// Path algorithm.
import java.util.*;
import java.lang.*;
import java.lang.reflect.Array;
import java.io.*;
 
 
class AllPairShortestPath
{
	private int dist[][];
	private static int pred[][];
	private static int flow[][];
	private static int linked[][];
	private static int minflow[][];
	private static int maxflow[][];
	private static double avgflow[][];
	private static String pathMatrix[][];
    final static int INF = 99999;
    private int V;
    private ArrayList<Paths> tflow = new ArrayList<Paths>();
    private static int sneakyStart;
	private static int sneakyEnd; 
	private static String fileExtension = new String("N75"); 
	private static Stack pathStack;
	
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
        		//if (i != j)
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
        //printSolution(dist);
        //printSolution(pred);
    }
 
    void printSolution(int matr[][], PrintWriter writer)
    {
        writer.println("Following matrix shows the shortest "+
                         "distances between every pair of vertices");
        for (int i=0; i<V; ++i)
        {
            for (int j=0; j<V; ++j)
            {
                if (matr[i][j]==INF)
                    writer.print("INF ");
                else
                    writer.print(matr[i][j]+"   ");
            }
            writer.println();
        }
    }
    
    Stack giveMeThePath(int start, int end){
    	int current = end-1;
    	int begin = start -1;
    	Stack pathStack = new Stack();
    	pathStack.push(end);
    	while (pred[begin][current] != start){
    		current = pred[begin][current]-1;
    		pathStack.push(current+1);
    	}
    	pathStack.push(start);	
		return pathStack;
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
    	//printSolution(flow);
    }
    
    int[][] parseInput(String filename){
    	V = 1000;
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
    	    		sneakyStart = Integer.parseInt(line[1].trim());
    	    		sneakyEnd = Integer.parseInt(line[2].trim());
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
    	//used to test different sized matrix's
    	//V = 2500;
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
        	    if (ed != null && ed.equals("E") && (start < V && start >=0) && (end < V && end >=0)){
        	    	graph[start][end] = weight;
        	    }
        	    else if (ed != null && ed.equals("F") && (start < V && start >=0) && (end < V && end >=0)){
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
    	avgflow = new double[V][V];
    	pathMatrix = new String [V][V];
    	return graph;
    }
    void printflow(PrintWriter writer){
    	printSolution(flow, writer);
    }
    
    void generateOutput(int[][] input, int[][] predessor){
    	double count;

    	String path = "";
    	String pathCorrect = "";
    	for (int i = 0; i < input.length; i++){
    		for (int j = 0; j < input.length; j++){
    	    	int max = 0, min = 0;
    			count = 1;
    			path = "";
    			if (i == j)
    				pathMatrix[i][j] = Integer.toString(input[i][j]);
    			else{
    				int currentval;
    		    	int current = j;
    		    	int begin = i ;
    		    	int next = j;
    		    	path += (j+1) + " ,";
    		    	while (pred[begin][current] != (i+1)){
    		    		current = pred[begin][current] - 1;
    		    		currentval = dist[current][next];
    		    		path += (current + 1) + " ,"; 
    		    		count++;
    		    		next = current;
    		    		if (count == 2 || currentval > max)
    		    			max = currentval;   		    	
    		    		if (count == 2 || currentval < min)
    		    			min = currentval;
    		    	}
    		    	path += (i+1);
		    		current = pred[begin][current] - 1;
		    		currentval = dist[current][next];
		    		if (count == 1 || currentval > max)
		    			max = currentval;   		    	
		    		if (count == 1 || currentval < min)
		    			min = currentval;
    			}
    			pathCorrect = new StringBuilder(path).reverse().toString();
    			pathMatrix[i][j] = pathCorrect;   			
    			avgflow[i][j] = dist[i][j] / count;
    			minflow[i][j] = min;
    			maxflow[i][j] = max;
    			
    		}
    	}
    } 
    
    void printPath(String matr[][], PrintWriter writer)
    {
			writer.println("Following matrix shows the shortest " + "path between every pair of vertices");
			for (int i = 0; i < V; ++i) {
				for (int j = 0; j < V; ++j) {
					if (i == j)
						writer.print(String.format("%20s", Integer.toString(i + 1)));
					else
						writer.print(String.format("%20s", matr[i][j]));
				}
				writer.println();
			}
    }
    
    void printavg(double matr[][], PrintWriter writer)
    {
        writer.println("Following matrix shows the average "+
                         "distances, per edge, between every pair of vertices");
        for (int i=0; i<V; ++i)
        {
            for (int j=0; j<V; ++j)
            {
            	if (i == j)
            		writer.print(String.format("%20s", Integer.toString((0))));
            	else
            		writer.print(String.format("%20s", Math.round(matr[i][j]*100.0)/100.0));
            }
            writer.println();
        }
    }
    
    void printStackToFile(PrintWriter writer, Stack pathStack)
    {
    	writer.print("[");
    		while(pathStack.size() > 0){
    			writer.print(pathStack.pop());
    			if (pathStack.size() != 0)
    				writer.print(", ");
    		}
    		writer.print("]");
			writer.flush();

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
		// start time
		long startTime = System.nanoTime();
		
    	try{
    		PrintWriter writer = new PrintWriter(fileExtension + "withfeatures"+".txt", "UTF-8");
    		Stack thePath;
			ArrayList testFlow = new ArrayList();
			AllPairShortestPath a = new AllPairShortestPath();
			int graph[][] = a.parseInput("sneakypathinput" + fileExtension + ".txt");
    		writer.println("Calculating sneaky path from " + sneakyStart + " to " + sneakyEnd + ".");
			// Print the solution
			a.floydWarshall(graph);
			//System.out.println("shortest path is as follows:  ");
			//thePath = a.giveMeThePath(6, 1);
			//System.out.println(Arrays.toString(thePath.toArray()));
			a.parseFlowList();
			a.printflow(writer);
			a.floydWarshall(flow);
			//thePath = a.giveMeThePath(2, 1);
			//System.out.println(Arrays.toString(thePath.toArray()));
			a.generateOutput(flow, pred);
			a.printPath(pathMatrix, writer);
			a.printavg(avgflow, writer);
			a.printSolution(maxflow, writer);
			a.printSolution(minflow, writer);
			// end time
			long endTime = System.nanoTime();
			//System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
			thePath = (a.giveMeThePath(sneakyStart, sneakyEnd));
			writer.println();
			writer.println("Here is that sneakypath.");
			a.printStackToFile(writer, thePath);
			writer.println();
			writer.println();
			writer.println("The edge with the lowest number of other cars in the sneaky path is.");
			writer.println(minflow[sneakyStart-1][sneakyEnd-1]);
			writer.println();
			writer.println("The edge with the highest number of other cars in the sneaky path is.");
			writer.println(maxflow[sneakyStart-1][sneakyEnd-1]);
			writer.println();
			writer.println("The average number of other cars on the sneaky path is.");
			writer.println(avgflow[sneakyStart-1][sneakyEnd-1]);
			writer.flush();			writer.close();
    	}
    	catch(Exception x){
    		x.printStackTrace();
    	}
	}
}