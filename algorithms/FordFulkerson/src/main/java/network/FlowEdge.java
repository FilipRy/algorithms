package network;

import lombok.Getter;
import lombok.Setter;

public class FlowEdge {

    @Getter
    @Setter
    private int capacity;

    @Getter
    @Setter
    private int lowerBounds;

    @Getter
    @Setter
    private int upperBounds;

    @Getter
    @Setter
    private int flow;

    @Getter
    @Setter
    private FlowNode target;

    @Getter
    @Setter
    private boolean isReverse;


    public FlowEdge() {
    }

    public FlowEdge(int capacity, FlowNode target) {
        this.capacity = capacity;
        this.target = target;
    }

    public FlowEdge(int lowerBounds, int upperBounds, FlowNode target) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.target = target;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowEdge edge = (FlowEdge) o;

        if (capacity != edge.capacity) return false;
        if (flow != edge.flow) return false;
        return target != null ? target.equals(edge.target) : edge.target == null;

    }

}
