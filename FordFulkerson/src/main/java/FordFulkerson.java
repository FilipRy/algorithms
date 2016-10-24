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

    /**
     * Eliminates the lower and upper bound of the edges and "converts" them to demands of the nodes and capacity of the edge.
     * e.g.
     * edge (v,w) has lower bound = 2, upper bound = 9
     * this method increases the demand of v by 2 and decreases the demand of w by 2 and sets the capacity of edge (v, w) = 9 - 2 = 7
     *
     */
    private void eliminateBounds() {

        visited = new boolean[flowNetwork.getVertexCount()];

        Queue<FlowNode> flowNodeQueue = new LinkedList<>();
        flowNodeQueue.add(flowNetwork.getSource());
        visited[flowNetwork.getSource().getId()] = true;

        while (!flowNodeQueue.isEmpty()) {

            FlowNode flowNode = flowNodeQueue.poll();

            for (int i = 0; i < flowNode.getOutgoingEdges().size(); i++) {
                FlowEdge edge = flowNode.getOutgoingEdges().get(i);

                if(edge.getLowerBounds() > 0 || edge.getUpperBounds() > 0) {
                    int lowerBounds = edge.getLowerBounds();
                    int upperBounds = edge.getUpperBounds();

                    edge.setUpperBounds(0);
                    edge.setLowerBounds(0);

                    if(upperBounds < lowerBounds) {
                        throw new IllegalArgumentException("Upper bound of edge ("+ flowNode.getId() + ", " + edge.getTarget().getId() +") must be greater than lower bound");
                    }

                    edge.setCapacity(upperBounds - lowerBounds);

                    int sourceDemand = flowNode.getDemand();
                    int targetDemand = edge.getTarget().getDemand();

                    edge.getTarget().setDemand(targetDemand - lowerBounds);
                    flowNode.setDemand(sourceDemand + lowerBounds);

                }

                FlowNode target = edge.getTarget();

                if (!visited[target.getId()]) {
                    visited[target.getId()] = true;
                    flowNodeQueue.add(target);
                }

            }

        }
    }

    /**
     * Removes demands/supplies (if any) of the nodes and creates new source and sink.
     * Connects the nodes with supply to the new source and the nodes with demands to new sink.
     * @return
     */
    private int eliminateDemands() {

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
                int targetId = target.getId();

                if(targetId > visited.length) {//in case we want to visit newSource or newSink
                    continue;
                }

                if (!visited[targetId]) {
                    visited[targetId] = true;
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

        System.out.println("Demand of the network = " + positiveDemand);

        return positiveDemand;

    }


    public boolean hasCirculation() {

        int demand = eliminateDemands();

        int maxFlow = computeMaxFlow();

        return maxFlow == demand;

    }

    public boolean hasCirculationWithBounds() {
        eliminateBounds();

        int demand = eliminateDemands();

        int maxFlow = computeMaxFlow();

        return maxFlow == demand;
    }


    public int computeMaxFlow() {

        System.out.println("Computing max flow of the network");

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

        System.out.println("Max flow of the network = " + maxFlow);

        return maxFlow;

    }

}
