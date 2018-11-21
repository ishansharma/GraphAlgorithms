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

    HashSet<EdgeHandle> edgeSet;



    public EnumerateTopological(Graph g) {
        super(g, new EnumVertex());
        print = false;
        count = 0;
        sel = new Selector(g.getVertex(1));
        edgeSet = new HashSet<>();


        for(Vertex u: g) {

            for(Edge e: g.incident(u)){
                edgeSet.add(new EdgeHandle(e.fromVertex(),e.toVertex()));
            }
        }


    }



    static class EnumVertex implements Factory {
        EnumVertex() { }
        public EnumVertex make(Vertex u) { return new EnumVertex();	}
    }

//    Selector getSelector()
//    {
//        return new Selector();
//    }

    public  class Selector extends Enumerate.Approver<Vertex> {

        Vertex startVertex;

        Deque<Vertex> de_que;

//        public Selector(){
//
//        }

        public Selector(Vertex v){

            de_que  = new ArrayDeque<Vertex>(10);
            startVertex = v;
        }

        public Selector(int size,Vertex v){

            de_que  = new ArrayDeque<Vertex>(size);
            startVertex = v;
        }

        public void clear()
        {
            de_que.clear();
        }



        @Override
        public boolean select(Vertex u) {

            if(de_que.isEmpty()){

                if(u.equals(startVertex))
                {
                    de_que.push(u);
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                Vertex v = de_que.peek();
                EdgeHandle e = new EdgeHandle(v,u);

                if(edgeSet.contains(e)){
                    de_que.push(u);
                    return true;
                }
                else{
                    return false;
                }
            }

        }



        @Override
        public void unselect(Vertex u) {

            if(!de_que.isEmpty()){
                de_que.pop();
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


    // To do: LP4; return the number of topological orders of g
    public long enumerateTopological(boolean flag) {
        print = flag;

        Vertex[] input = new Vertex[g.size()];

//        input[0] = g.getVertex(1);
//        input[1] = g.getVertex(3);
//        input[2] = g.getVertex(2);
//        input[3] = g.getVertex(4);
//        input[4] = g.getVertex(5);
//        input[5] = g.getVertex(6);


        int index  = 0;



        for(Vertex v : g){
            input[index++] = v;
//            System.out.println(this.sel.select(v));
        }

        Enumerate<Vertex> enumVertex = new Enumerate<Vertex>(input,input.length - 2,this.sel);
//
        enumVertex.permute(input.length - 2);

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
