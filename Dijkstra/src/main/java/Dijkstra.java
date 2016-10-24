import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class Dijkstra {

    private Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }


    /**
     * The total runtime is O(|V| log|V| + |E|)
     * @param source
     * @param destination
     * @return
     */
    public List<Node> findShortestPath(Node source, Node destination) {

        for(Node node: graph.getNodes()) {
            node.setDist(Integer.MAX_VALUE);
            node.setPreviousNode(null);
        }
        source.setDist(0);

        Queue<Node> nodeQueue = new PriorityQueue<>();

        nodeQueue.add(source);

        while (!nodeQueue.isEmpty()) {// O(|V|)

            Node node = nodeQueue.poll();// O(log|V|)

            if(node.equals(destination)) {
                break;
            }

            for(Edge e: node.getEdges()) {
                if(e.getTarget().getDist() > node.getDist() + e.getPathDist()) {
                    Node target = e.getTarget();
                    target.setDist(node.getDist() + e.getPathDist());
                    target.setPreviousNode(node);
                    nodeQueue.add(target);// O(log|V|)
                }
            }

        }

        List<Node> shortestPath = new ArrayList<>();

        Node currentNode = destination;

        while (!currentNode.equals(source)) {
            shortestPath.add(currentNode);
            currentNode = currentNode.getPreviousNode();
        }
        Collections.reverse(shortestPath);

        return shortestPath;

    }


}
