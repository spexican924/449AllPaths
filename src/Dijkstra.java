// Arup Guha
// Written on 10/6/07 - edited version of an exam question solution from Fall 06.

import java.io.*;
import java.util.*;

// Stores information necessary to run Dijkstra's for each vertex in the graph.
class point {
	
	public Integer distance;
	public boolean chosen;
	public int last;
	
	public point(int d, int source) {
		distance = new Integer(d);
		last = source;
		chosen = false;
	}
}

public class Dijkstra {
	
	// No path will be longer than this.
	final static int  MAXINT = 1000000000;
	
	public static void main(String[] args) throws Exception {
		
		Scanner fin = new Scanner (new File("dijkstra.in"));
		int[][] adj;

		int n = fin.nextInt();
		int set = 1;

		while (n != 0) {
		
			// Read in this adjacency matrix.
			adj = new int[n][n];
			for (int i = 0; i<n*n; i++)
				adj[i/n][i%n] = fin.nextInt();
		
			// Run Dijkstra's and store the return array of shortest distances.
			point[] answers = dijkstra(adj, 0);

			System.out.println("Data Set #"+set+":");
			
			// Print out a line for each vertex.
			for (int i=1; i<n; i++) {

				// First part of the info we want.
				System.out.print("Distance to vertex "+i+" is "+answers[i].distance+". ");
		
				// Set up variables to reconstruct the desired path.
				String path = "";
				int end = i;
				int source = 0;
				boolean firstTime = true;
				
				// We build the path up from the end, so when the end is the
				// source, we can stop.
				while (end != source) {
		
					// here we prepend the proper vertex to our path, working
					// backwards. The first time through, we don't add a -.
					if (firstTime) 
						path = end + path;
					else
						path = (end + "-") + path;
						
					// Now that we've added end to our path, our new end is the
					// vertex that we would go to BEFORE our old end.
					end = answers[end].last;
				
					// If we get here, we're done with our first iteration.
					firstTime = false;	
				
				} // end while for building path.

				// Now just add an edge from the source to what's already been
				// built.
				path = source + "-" + path;
		
				// Now we can print out the path.
				System.out.println("Path is "+path+".");

			} // end for loop through vertices

			System.out.println();
			set++;
			n = fin.nextInt();
		}

		fin.close();

	} // end-main
	
	public static point[] dijkstra(int[][] adj, int source) {
		
		point[] estimates = new point[adj.length];
		
		// Set up our initial estimates.
		for (int i=0; i<estimates.length; i++)
			estimates[i] = new point(MAXINT, source);
			
		// This estimate is 0, now.
		estimates[source].distance = 0;
		
		// Really, we can run this n-1 times, where n is the number of
		// vertices. The last iteration does NOT produce any different paths.
		for (int i=0; i<estimates.length-1; i++) {
			
			// Pick the minimal vertex to add into, S, our set of vertices
			// for which we have shortest distances.
			int vertex = 0;
			int bestseen = MAXINT;
			
			// In order to be chosen here, you can not have been previously
			// chosen. Also, you have to be smaller than all other candidates.
			for (int j=0; j<estimates.length; j++) {
				if (estimates[j].chosen == false && 
				    estimates[j].distance < bestseen) {
				
					bestseen = estimates[j].distance;
					vertex = j;
				}
			}
			
			// Choose this vertex!
			estimates[vertex].chosen = true;
			
			// Update our estimates based on edges that leave from this vertex.
			for (int j = 0; j<estimates.length; j++) {
				
				// Do we get a shorter distance by traveling to vertex, and then
				// taking the edge from vertex to j? If so, make the update here.
				if (estimates[vertex].distance+adj[vertex][j] < 
				    estimates[j].distance) {
				    
				    // Our new estimate to get to j, going through vertex.
				    estimates[j].distance = estimates[vertex].distance + adj[vertex][j];	
				    
				    // This also means that vertex is the last vertex on the new
				    // shortest path to j, so we need to store this also.
				    estimates[j].last = vertex;
				}
			}
			
		}
		
		// We return these whole estimates array.
		return estimates;
	}
}