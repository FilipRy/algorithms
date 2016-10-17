import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;

import java.util.ArrayList;
import java.util.List;

public class FordFulkerson {

    private FlowNetwork flowNetwork;

    private boolean visited[];

    public FordFulkerson(FlowNetwork flowNetwork) {
        this.flowNetwork = flowNetwork;
    }

    public List<FlowEdge> findAugmentingPath(FlowNode flowNode, List<FlowEdge> pathEdges) {
        visited[flowNode.getId()] = true;

        for (int i = 0; i < flowNode.getOutgoingEdges().size(); i++) {
            FlowEdge edge = flowNode.getOutgoingEdges().get(i);

            if (edge.getCapacity() - edge.getFlow() > 0) {

                if (edge.getTarget().equals(flowNetwork.getSink())) {
                    pathEdges.add(edge);
                    return pathEdges;
                }

                if (!visited[edge.getTarget().getId()]) {
                    pathEdges.add(edge);
                    return findAugmentingPath(edge.getTarget(), pathEdges);
                }
            }

        }
        return null;
    }

    private int findBottleneckValue(List<FlowEdge> path) {
        int bottleneck = Integer.MAX_VALUE;
        for (FlowEdge flowEdge : path) {
            int availableFlow = flowEdge.getCapacity() - flowEdge.getFlow();
            if (availableFlow < bottleneck) {
                bottleneck = availableFlow;
            }
        }
        return bottleneck;
    }

    private void decreaseFlowOfOriginalEdge(FlowEdge reverseEdge, FlowNode lastStart, int bottleneck) {
        FlowNode target = reverseEdge.getTarget();
        for (FlowEdge edge : target.getOutgoingEdges()) {
            if (edge.getTarget().equals(lastStart)) {
                edge.setFlow(edge.getFlow() - bottleneck);
            }
        }
    }

    private boolean updateReverseEdges(FlowEdge flowEdge, FlowNode lastStart, int updatedFlow) {
        FlowNode target = flowEdge.getTarget();

        boolean reverseEdgeExists = false;
        for (FlowEdge edge : target.getOutgoingEdges()) {
            if (edge.isReverse() && edge.getTarget().equals(lastStart)) {
                edge.setCapacity(updatedFlow);
                reverseEdgeExists = true;
            }
        }

        return reverseEdgeExists;
    }

    private void addReverseEdge(FlowEdge flowEdge, FlowNode lastStart, int updatedFlow) {
        FlowNode target = flowEdge.getTarget();

        FlowEdge residualEdge = new FlowEdge(updatedFlow, lastStart);
        residualEdge.setReverse(true);
        target.getOutgoingEdges().add(residualEdge);
    }

    public int run() {

        while (true) {

            visited = new boolean[flowNetwork.getEdgeCount()];

            List<FlowEdge> path = findAugmentingPath(flowNetwork.getSource(), new ArrayList<FlowEdge>());
            if (path != null) {//if a path exists

                int bottleneck = findBottleneckValue(path);

                FlowNode lastStart = flowNetwork.getSource();

                for (FlowEdge flowEdge : path) {
                    int updatedFlow = flowEdge.getFlow() + bottleneck;
                    flowEdge.setFlow(updatedFlow);//updating flow along the augmenting path

                    if (flowEdge.isReverse()) {
                        //if a reverse "undo" edge was used in the path, we must decrease the flow at original edge.
                        decreaseFlowOfOriginalEdge(flowEdge, lastStart, bottleneck);
                    } else {

                        boolean reverseEdgeExists = updateReverseEdges(flowEdge, lastStart, updatedFlow);

                        if (!reverseEdgeExists) {
                            addReverseEdge(flowEdge, lastStart, updatedFlow);
                        }
                    }

                    lastStart = flowEdge.getTarget();

                }

            } else {
                break;
            }
        }

        int maxFlow = 0;
        for(FlowEdge flowEdge: flowNetwork.getSource().getOutgoingEdges()) {
            maxFlow = maxFlow + flowEdge.getFlow();
        }

        return maxFlow;

    }

}