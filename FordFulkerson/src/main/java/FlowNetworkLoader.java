import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 10/18/2016.
 */
public class FlowNetworkLoader {

    /**
     *
     * @param filename:
     *                Structure of the input file is as follows:
     *                N ... # number of nodes of the network (including source and sink)
     *                next at most N lines are structured as follows:
     *                1st number v in the row is the vertex id, 2nd number is the demand (supply) of the vertex, 3rd to i-th number represents the edges starting at v with their capacities
     *                Precondition vertex with id 0 is source, N-1 is sink
     *                e.g. 0 0 1 17 2 12 3 11 - vertex 0 (source) has demand 0 and has is connected to vertices 1, 2, 3 through edges with capacities: 17, 12, 11 (respectively)
     *
     * @return
     * @throws IOException
     */
    public static FlowNetwork loadNetworkFromFile(String filename) throws IOException {

        FlowNetwork flowNetwork = new FlowNetwork();

        int lineNr = 0;

        List<FlowNode> flowNodes = new ArrayList<>();

        for (String line : Files.readAllLines(Paths.get(filename))) {
            if (lineNr == 0) {
                int vertexCount = Integer.parseInt(line);
                for (int i = 0; i < vertexCount; i++) {
                    FlowNode flowNode = new FlowNode(i);
                    flowNodes.add(flowNode);
                }
            } else {
                String[] nodes = line.split(" ");
                int startNodeId = Integer.parseInt(nodes[0]);
                FlowNode start = flowNodes.get(startNodeId);

                int demand = Integer.parseInt(nodes[1]);

                start.setDemand(demand);

                for (int i = 2; i < nodes.length - 1; i += 2) {
                    int targetNodeId = Integer.parseInt(nodes[i]);
                    int edgeCapacity = Integer.parseInt(nodes[i + 1]);
                    FlowNode targetNode = flowNodes.get(targetNodeId);
                    FlowEdge flowEdge = new FlowEdge(edgeCapacity, targetNode);
                    start.getOutgoingEdges().add(flowEdge);
                }
            }

            lineNr++;

        }

        flowNetwork.setVertexCount(flowNodes.size());
        flowNetwork.setSource(flowNodes.get(0));
        flowNetwork.setSink(flowNodes.get(flowNodes.size() - 1));

        return flowNetwork;

    }


}
