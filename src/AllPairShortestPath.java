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
    final static int INF = 99999;
    private int V;
 
    void floydWarshall(int graph[][])
    {
    	V = graph.length;
        dist = new int[V][V];
        pred = new int[V][V];
        flow = new int[V][V];
        int i, j, k;
 
        /* Initialize the solution matrix same as input graph matrix.
           Or we can say the initial values of shortest distances
           are based on shortest paths considering no intermediate
           vertex. */
        for (i = 0; i < V; i++)
            for (j = 0; j < V; j++){
                dist[i][j] = graph[i][j];
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
    
    void parseFlowList(List<Paths> otherCars){
    	List hereIsThePath;
    	for (Paths current : otherCars){
    		hereIsThePath = giveMeThePath(current.begin, current.end);
    		makeAdjacencyMatrix(hereIsThePath, current.flow);
    	}
    	printSolution(flow);
    }
 

	public static void main(String[] args) {
		
        int graph[][] = { {0,   INF,  3, INF, INF},
                          {4, 0,   INF, 1, 2},
                          {3, 8, 0, 2, 6},
                          {INF, 1, INF, 0, 4},
                          {INF, 1, 6, 4, 0}
                        };
        
        ArrayList testFlow = new ArrayList();
        Paths path1 = new Paths(4,3,90);
        testFlow.add(path1);
        AllPairShortestPath a = new AllPairShortestPath();
 
        // Print the solution
        a.floydWarshall(graph);
        System.out.println("shortest path is as follows:  ");
        ArrayList thePath = a.giveMeThePath(4, 3);
        System.out.println(Arrays.toString(thePath.toArray()));
        a.parseFlowList(testFlow);
        
        
        
	}
}