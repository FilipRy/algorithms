package network;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private List<FlowEdge> outgoingEdges = new ArrayList<FlowEdge>();

    @Getter
    @Setter
    private int demand;

    public FlowNode() {
    }

    public FlowNode(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowNode flowNode = (FlowNode) o;

        return id == flowNode.id;

    }

}
