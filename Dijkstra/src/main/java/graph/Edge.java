package graph;


import lombok.Getter;
import lombok.Setter;

public class Edge {

    @Getter
    @Setter
    private int pathDist;

    @Getter
    @Setter
    private Node target;


    public Edge(int pathDist, Node target) {
        this.pathDist = pathDist;
        this.target = target;
    }
}
