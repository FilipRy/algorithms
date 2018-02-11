package graph;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class Node implements Comparable<Node> {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private int dist;

    @Getter
    @Setter
    private Node previousNode;

    /**
     * The list of edges starting at this Node
     */
    @Getter
    @Setter
    private List<Edge> edges = new ArrayList<>();

    public Node(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;

    }

    @Override
    public int compareTo(Node o) {
        return dist - o.dist;
    }
}
