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

                for (int i = 1; i < nodes.length - 1; i += 2) {
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
