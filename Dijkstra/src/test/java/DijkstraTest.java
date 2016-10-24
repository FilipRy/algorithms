import graph.Edge;
import graph.Graph;
import graph.Node;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Filip on 10/24/2016.
 */
public class DijkstraTest {


    @Test
    public void test_findShortestPath1() {

        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);


        Edge edge12 = new Edge(1, node2);
        Edge edge13 = new Edge(2, node3);
        Edge edge14 = new Edge(5, node4);

        Edge edge21 = new Edge(1, node1);
        Edge edge24 = new Edge(3, node4);

        Edge edge31 = new Edge(2, node1);
        Edge edge34 = new Edge(1, node4);

        Edge edge41 = new Edge(5, node1);
        Edge edge42 = new Edge(3, node2);
        Edge edge43 = new Edge(1, node3);

        node1.getEdges().add(edge12);
        node1.getEdges().add(edge13);
        node1.getEdges().add(edge14);

        node2.getEdges().add(edge21);
        node2.getEdges().add(edge24);

        node3.getEdges().add(edge31);
        node3.getEdges().add(edge34);

        node4.getEdges().add(edge41);
        node4.getEdges().add(edge42);
        node4.getEdges().add(edge43);


        List<Node> nodes = new ArrayList<>();
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);

        Graph graph = new Graph(nodes);

        Dijkstra dijkstra = new Dijkstra(graph);

        List<Node> shortestPath = dijkstra.findShortestPath(node1, node4);

        assertTrue(shortestPath != null);
        assertTrue(shortestPath.size() == 2);
        assertTrue(node4.getDist() == 3);

        assertTrue(shortestPath.get(0).equals(node3));
        assertTrue(shortestPath.get(1).equals(node4));

    }

}
