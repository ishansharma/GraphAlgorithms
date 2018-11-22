/* Starter code for enumerating topological orders of a DAG
 * @author
 */

package ixs171130;
import rbk.Graph;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import java.util.*;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
    boolean print;  // Set to true to print array in visit
    long count;      // Number of permutations or combinations visited
    Selector sel;


    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex(null));
        print = false;
        count = 0;
        sel = new Selector();
        // Initialized the indegree of all the vertices of the Graph.
        initializeIndegree(g);

    }



    static class EnumVertex implements Factory {

        int inDegree  = 0;
        Vertex u;

        EnumVertex(Vertex v) {
            u = v;
        }
        public EnumVertex make(Vertex u) { return new EnumVertex(u);	}
    }


    public  class Selector extends Enumerate.Approver<Vertex> {

        public Selector(){

        }

        /**
         *
         * @param u Vertex  u of the graph to be Approved.
         * @return true if the indegree of u is zero. else return false.
         *
         * if approvedm update the indegree of the incident vertices.
         */

        @Override
        public boolean select(Vertex u) {

            if(get(u).inDegree == 0){

                for(Edge e: g.incident(u)){
                    get(e.otherEnd(u)).inDegree--;
                }

                return true;
            }

            return false;
        }


        /**
         *
         * @param u - Vertex  u of the graph to be Approved.
         * update the indegree of the incident vertices.
         */
        @Override
        public void unselect(Vertex u) {

            for(Edge e: g.incident(u)){
                get(e.otherEnd(u)).inDegree++;
            }

        }

        @Override
        public void visit(Vertex[] arr, int k) {
            count++;
            if(print) {
                for(Vertex u: arr) {
                    System.out.print(u + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     *
     * @param g
     * Initialize the indegree of all the vertices.
     */
    public void initializeIndegree(Graph g){

        for(Vertex u: g) {

            for(Edge e: g.incident(u)){
                get(e.otherEnd(u)).inDegree++;
            }
        }
    }


    // To do: LP4; return the number of topological orders of g
    public long enumerateTopological(boolean flag) {
        print = flag;

        Vertex[] input = new Vertex[g.size()]; // creating an array of all the vertices in the graph to be passed to permute.
        int i = 0;
        for(Vertex u : g)
        {
            input[i] = u;
            i++;
        }

        Enumerate<Vertex> enumVertex = new Enumerate<Vertex>(input,input.length ,this.sel); // passed approver for topological order.
        enumVertex.permute(input.length);

        return count;
    }

    //-------------------static methods----------------------

    public static long countTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(false);
    }

    public static long enumerateTopologicalOrders(Graph g) {
        EnumerateTopological et = new EnumerateTopological(g);
        return et.enumerateTopological(true);
    }

    public static void main(String[] args) {
        int VERBOSE = 0;
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
        Graph.Timer t = new Graph.Timer();
        long result;
        if(VERBOSE > 0) {
            result = enumerateTopologicalOrders(g);
        } else {
            result = countTopologicalOrders(g);
        }
        System.out.println("\n" + result + "\n" + t.end());
    }

}
