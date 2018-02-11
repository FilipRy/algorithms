package graph;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class Graph {


    @Getter
    @Setter
    private List<Node> nodes = new ArrayList<>();


    public Graph(List<Node> nodes) {
        this.nodes = nodes;
    }
}
