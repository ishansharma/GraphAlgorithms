/*
 * Implementation of Data Structures SP8: Topological Sorting
 * Implementation of the topological sorting algorithm using depth-first search
 */


package ixs171130;

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.*;

// Class definition for DFS
// Extends GraphAlgorihm with vertex DFSVertex, defined inside the class
public class DFS extends GraphAlgorithm<DFS.DFSVertex> {
    LinkedList<Vertex> finishList = new LinkedList<>(); //list of the verticies in topological order
    int currentTopNumber; //class variable to hold the next topological order number to be assigned, used for cycle detection
    int cno;
    /**
     * DFS Vertex using Factory pattern
     */
    public static class DFSVertex implements Factory {
        int cno; //corresponds to the connected component that the vertex belongs to
        int topNum; //corresponds to the topological number of the vertex
        boolean seen; //if seen is true, then DFS has visited this node, otherwise it has not
        Vertex parent; //pointer to the parent of the node in the DFS traversal

        /**
         * Create a vertex specific to DFS class for our use
         * Constructor for DFS Vertex
         */
        public DFSVertex(Vertex u) {
            seen = false;
            parent = null;
            cno = -1;
            topNum = -1;
        }

        /**
         * Make a new DFS Vertex, make() is the use of Factory Pattern
         *
         * @param u Vertex from graph
         * @return DFSVertex with additional properties required for DFS
         */
        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    /**
     * Constructor for DFS
     * @param g Graph
     */
    public DFS(Graph g) {
        super(g, new DFSVertex(null)); //initailize a GraphAlgorithm object with a vertex of type DFSVertex
        currentTopNumber = g.size();
        cno = 0;
    }

    /**
     * Outer loops for DFS. First loop marks each vertex as not seen and parent as null and second one visits a
     * vertex if it's not visited.
     */
    private void dfs(Iterable<Vertex> vertices){
        //Initialize the finishList, currentTopNumber, and vertex attributse
        finishList = new LinkedList<>();
        int numberOfVerticies = 0;
        this.cno = 0;
        for (Vertex u: vertices) {
            get(u).seen = false;
            get(u).parent = null;
            get(u).topNum = -1;
            get(u).cno = 0;
            numberOfVerticies++;
        }

        currentTopNumber = numberOfVerticies;

        //For each vertex in the graph, if it has not been visited, visit it
        //If we discover there is a cycle, then return null
        //As we visit nodes, they will be added to the finishList in reverse order, corresponding to a topologial sorting

        for(Vertex u: vertices) {
            if(!get(u).seen) {
                cno++;
                get(u).cno = this.cno;
                dfsVisit(u);
            }
        }
    }

    public boolean dfs() {
        LinkedList<Vertex> vertexList = new LinkedList<>();
        for (Vertex aG : g) {
            vertexList.add(aG);
        }
        dfs(vertexList);
        return true;
    }


    /**
     * Helper function for dfs(). Marks the vertex as seen and explores all unvisited incident edges recursively
     * @param u Node to visit
     */
    public void dfsVisit(Vertex u) {
        //Mark the current node as seen
        get(u).seen = true;

        //For each vertex adjacent to u, if it has not been seen before, visit it and see if a cycle is found
        // Oherwise, check if the edge to v is a back-edge by seeing if its topological number has been set by the algorithm
        // If v's topological number has not been set, then v's class to dfsVisit has not finished, which indicates a cycle
        for(Edge e: g.incident(u)) {
            Vertex v = e.otherEnd(u);
            //v is a new node, go visit i
            if(!get(v).seen) {
                get(v).parent = u;
                get(v).cno = get(u).cno;
                dfsVisit(v);
            }
        }

        //all nodes adjacent to u have been procesed, so we set its topological number and add it to the finish list
        get(u).topNum = currentTopNumber--;
        finishList.add(0, u);
    }

    /**
     * Create a new instance of DFS and return the result
     * @param g Graph
     * @return DFS object
     */
    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        boolean status;
        status = d.dfs();
        if(status) {
            return d;
        } else {
            return null;
        }
    }


    /**
     * Find topological order of vertices using DFS
     *
     * @return List containing topological order, or null if cycle was found
     */
    public LinkedList<Vertex> topologicalOrder1() {
        DFS d = depthFirstSearch(g);

        if(d == null) { //indicate a cycle was found
            return null;
        }

        return d.finishList;
    }

    /**
     * Given a graph, find topological order of vertices
     * @param g Graph
     * @return List of vertices in topological order
     */
    public static LinkedList<Vertex> topologicalOrder1(Graph g) {
        DFS d = new DFS(g);

        if(d == null) {
            return null;
        }

        return d.topologicalOrder1();
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return cno;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    public static DFS stronglyConnectedComponents(Graph g) {
        DFS d = DFS.depthFirstSearch(g);
        d.dfs();
        List<Vertex> firstFinishList = d.finishList;
        g.reverseGraph();
        d.dfs(firstFinishList);
        g.reverseGraph();
        return d;
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String string = "11 16  1 11 1  2 3 1  2 7 1  3 10 1  4 9 1  4 1 1  5 7 1  5 8 1  6 3 1  7 8 1  8 2 1  9 11 1  10 6 1  11 4 1  11 3 1  11 6 1 0";

        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        DFS d = DFS.stronglyConnectedComponents(g);
        System.out.print("Number of components: " + d.connectedComponents() + "\n\n");

        System.out.println("Vertex\tComponent");
        for(Vertex v: g) {
            System.out.println(v.getName() + "\t\t" + d.cno(v));
        }
    }
}