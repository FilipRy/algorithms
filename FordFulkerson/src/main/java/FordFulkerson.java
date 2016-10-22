import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FordFulkerson {

    private FlowNetwork flowNetwork;

    private boolean visited[];

    public FordFulkerson(FlowNetwork flowNetwork) {
        this.flowNetwork = flowNetwork;
        visited = new boolean[flowNetwork.getVertexCount()];
    }


    /**
     * The Augmenting path is found using DFS.
     * time complexity: O(m + n)
     * space complexity: O(m)
     *
     * @param flowNode
     * @param pathEdges
     * @return
     */
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
                    List<FlowEdge> foundPath = findAugmentingPath(edge.getTarget(), pathEdges);
                    if (foundPath != null) {
                        return foundPath;
                    } else {
                        pathEdges.remove(edge);
                    }
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

    private int prepareDemands() {

        visited = new boolean[flowNetwork.getVertexCount()];

        FlowNode newSource = new FlowNode(flowNetwork.getVertexCount());
        FlowNode newSink = new FlowNode(flowNetwork.getVertexCount() + 1);

        Queue<FlowNode> flowNodeQueue = new LinkedList<>();
        flowNodeQueue.add(flowNetwork.getSource());
        visited[flowNetwork.getSource().getId()] = true;

        int positiveDemand = 0;
        int negativeDemand = 0;

        while (!flowNodeQueue.isEmpty()) {

            FlowNode flowNode = flowNodeQueue.poll();

            if(flowNode.getDemand() > 0) {
                positiveDemand = positiveDemand + flowNode.getDemand();
                FlowEdge edgeToSink = new FlowEdge(flowNode.getDemand(), newSink);
                flowNode.getOutgoingEdges().add(edgeToSink);
            } else if(flowNode.getDemand() < 0) {
                negativeDemand = negativeDemand + flowNode.getDemand();
                FlowEdge edgeFromSource = new FlowEdge(-flowNode.getDemand(), flowNode);
                newSource.getOutgoingEdges().add(edgeFromSource);
            }

            for (int i = 0; i < flowNode.getOutgoingEdges().size(); i++) {
                FlowEdge edge = flowNode.getOutgoingEdges().get(i);

                FlowNode target = edge.getTarget();

                if(target.getId() > visited.length) {//in case we want to visit newSource or newSink
                    continue;
                }

                if (!visited[target.getId()]) {
                    visited[target.getId()] = true;
                    flowNodeQueue.add(target);
                }

            }

        }


        flowNetwork.setSource(newSource);
        flowNetwork.setSink(newSink);
        flowNetwork.setVertexCount(flowNetwork.getVertexCount() + 2);

        if (positiveDemand != -negativeDemand) {
            throw new IllegalArgumentException("sum of all demands must be the same as sum of all supplies");
        }

        return positiveDemand;

    }


    public boolean hasCirculation() {

        int demand = prepareDemands();

        int maxFlow = computeMaxFlow();

        return maxFlow == demand;

    }


    public int computeMaxFlow() {

        while (true) {

            visited = new boolean[flowNetwork.getVertexCount()];

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
                        //update capacity of the reverse "undo" edge e defined as c_r(e) = f(e)
                        boolean reverseEdgeExists = updateReverseEdges(flowEdge, lastStart, updatedFlow);

                        //create the reverse edge e if it not exists, set its capacity to f(e)
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
        for (FlowEdge flowEdge : flowNetwork.getSource().getOutgoingEdges()) {
            maxFlow = maxFlow + flowEdge.getFlow();
        }

        return maxFlow;

    }

}
