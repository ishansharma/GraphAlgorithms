/* Driver code for PERT algorithm (LP4)
 * @author
 */

package ixs171130;

import rbk.Graph;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;

import java.io.File;
import java.util.*;

public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    public static class PERTVertex implements Factory {
        int ec, lc, slack, d;  // earliest, latest completion time, slack, duration of the task
        boolean critical; // is this vertex on a critical path
        Vertex v;

        /**
         * Constructor to create a PERTVertex from a normal graph vertex
         *
         * @param u Original Vertex
         */
        public PERTVertex(Vertex u) {
            v = u; // no need to duplicate everything, just store reference to the original vertex
            d = 0; // number of days required to make

            critical = false;
        }

        /**
         * Factory method to make a new vertex
         * @param u Vertex to create PERTVertex from
         * @return PERTVertex
         */
        public PERTVertex make(Vertex u) {
            return new PERTVertex(u);
        }
    }

    /**
     * Default constructor to construct PERT
     * @param g
     */
    public PERT(Graph g) {
        super(g, new PERTVertex(null));
    }

    /**
     * Set duration of a vertex u to d
     * @param u Graph vertex
     * @param d Duration for the task
     */
    public void setDuration(Vertex u, int d) {
        get(u).d = d;
    }

    /**
     * Perform pert on stored graph
     * @return True is algorithm can complete, False if it's not a DAG.
     */
    public boolean pert() {
        int maxTime;

        // in the graph, s and t are already there but edges for them are not.
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.size());
        int m = g.edgeSize();
        for(int i = 2; i < g.size(); i++) {
            g.addEdge(s, g.getVertex(i), 1, ++m);
            g.addEdge(g.getVertex(i), t, 1, ++m);
        }

        LinkedList<Vertex> topologicalOrder = DFS.topologicalOrder1(g);
        if(topologicalOrder == null) {
            return false;
        }

        // set Earliest Completion time to 0 for all vertices
        for(Vertex u: g) {
            get(u).ec = 0;
        }

        // loop 1: calculate earliest completion time
        for(Vertex u: topologicalOrder) {
            for(Edge e: g.outEdges(u)) {
                if(get(e.fromVertex()).ec + get(e.toVertex()).d > get(e.toVertex()).ec) {
                    get(e.toVertex()).ec = get(e.fromVertex()).ec + get(e.toVertex()).d;
                }
            }
        }

        maxTime = get(g.getVertex(g.size())).ec;

        for(Vertex u: g) {
            get(u).lc = maxTime;
        }

        // Iterator learned from: https://stackoverflow.com/posts/3227002/revisions
        for(Iterator it = topologicalOrder.descendingIterator(); it.hasNext();) {
            Vertex u = (Vertex) it.next();
            for(Edge e: g.outEdges(u)) {
                if(get(e.toVertex()).lc - get(e.toVertex()).d < get(e.fromVertex()).lc) {
                    get(e.fromVertex()).lc = get(e.toVertex()).lc - get(e.toVertex()).d;
                }
            }
            get(u).slack = get(u).lc - get(u).ec;
            get(u).critical = false;
            if(get(u).slack == 0) {
                get(u).critical = true;
            }
        }

        return true;
    }

    public int ec(Vertex u) {
        return get(u).ec;
    }

    public int lc(Vertex u) {
        return get(u).lc;
    }

    public int slack(Vertex u) {
        return get(u).slack;
    }

    public int criticalPath() {
        return 0;
    }

    public boolean critical(Vertex u) {
        return get(u).critical;
    }

    public int numCritical() {
        return 0;
    }

    /**
     * Create a instance of pert graph
     * @param g Graph
     * @param duration Duration array for vertices
     * @return
     */
    public static PERT pert(Graph g, int[] duration) {
        PERT p = new PERT(g);
        int i = 0;
        for(Vertex u: g) {
            p.setDuration(u, duration[i]);
            i++;
        }
        p.pert();
        return p;
    }

    public static void main(String[] args) throws Exception {
        String graph = "11 12   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        PERT p = new PERT(g);
        for (Vertex u : g) {
            p.setDuration(u, in.nextInt());
        }
        // Run PERT algorithm.  Returns null if g is not a DAG
        if (!p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for (Vertex u : g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
    }
}