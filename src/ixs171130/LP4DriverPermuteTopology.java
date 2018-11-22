
// Change to your netid
package ixs171130;

import rbk.Graph;

import java.util.Scanner;

public class LP4DriverPermuteTopology {
    public static void main(String[] args) throws Exception {
	boolean details = false;
	String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
	Scanner in;
	// If there is a command line argument, use it as file from which
	// input is read, otherwise use input from string.
	in = args.length > 0 ? new Scanner(new java.io.File(args[0])) : new Scanner(graph);
	if(args.length > 1) { details = true; }
		int VERBOSE = 0;
	rbk.Graph g = rbk.Graph.readDirectedGraph(in);
		Graph.Timer t = new Graph.Timer();
		long result;
		if(VERBOSE > 0) {
			result = EnumerateTopological.enumerateTopologicalOrders(g);
		} else {
			result = EnumerateTopological.countTopologicalOrders(g);
		}
		System.out.println("\n" + result + "\n" + t.end());



    }
}
