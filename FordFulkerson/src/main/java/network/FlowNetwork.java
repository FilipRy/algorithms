package network;

import lombok.Getter;
import lombok.Setter;

public class FlowNetwork {

    @Getter
    @Setter
    private FlowNode source;

    @Getter
    @Setter
    private FlowNode sink;

    @Getter
    @Setter
    private int vertexCount;

    public FlowNetwork() {
    }


}
