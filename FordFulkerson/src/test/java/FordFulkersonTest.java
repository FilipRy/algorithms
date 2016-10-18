import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Filip on 10/17/2016.
 */



public class FordFulkersonTest {


    @Test
    public void test_findAugmentingPath_shouldFindPath() {

        FlowNode source = new FlowNode(0);
        FlowNode node2 = new FlowNode(1);
        FlowNode node3 = new FlowNode(2);
        FlowNode sink = new FlowNode(3);


        /**
         * edges from source node
         */
        FlowEdge edge_s2 = new FlowEdge(10, node2);
        FlowEdge edge_s3 = new FlowEdge(10, node3);

        source.getOutgoingEdges().add(edge_s3);
        source.getOutgoingEdges().add(edge_s2);

        /**
         * edges from node 2
         */
        FlowEdge edge_2t = new FlowEdge(4, sink);
        node2.getOutgoingEdges().add(edge_2t);

        /**
         * edges from node 3
         */
        FlowEdge edge_3t = new FlowEdge(9, sink);
        edge_3t.setFlow(9);
        node3.getOutgoingEdges().add(edge_3t);

        FlowNetwork flowNetwork = new FlowNetwork();
        flowNetwork.setSink(sink);
        flowNetwork.setSource(source);
        flowNetwork.setVertexCount(4);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork);
        List<FlowEdge> foundPath = fordFulkerson.findAugmentingPath(flowNetwork.getSource(), new ArrayList<FlowEdge>());

        //the only augmenting path is s->2->t
        assertTrue(foundPath != null);
        assertTrue(foundPath.size() == 2);

        assertTrue(foundPath.get(0).equals(edge_s2));
        assertTrue(foundPath.get(1).equals(edge_2t));


    }

    @Test
    public void test_findAugmentingPath_shouldFindPath2() {

        FlowNode source = new FlowNode(0);
        FlowNode node2 = new FlowNode(1);
        FlowNode node3 = new FlowNode(2);
        FlowNode node4 = new FlowNode(3);
        FlowNode node5 = new FlowNode(4);
        FlowNode node6 = new FlowNode(5);
        FlowNode sink = new FlowNode(6);

        /**
         * edges from source node
         */
        FlowEdge edge_s2 = new FlowEdge(10, node2);
        FlowEdge edge_s3 = new FlowEdge(10, node3);

        source.getOutgoingEdges().add(edge_s3);
        source.getOutgoingEdges().add(edge_s2);


        /**
         * edges from node 2
         */
        FlowEdge edge_24 = new FlowEdge(4, node4);
        node2.getOutgoingEdges().add(edge_24);

        /**
         * edges from node 3
         */
        FlowEdge edge_35 = new FlowEdge(9, node5);
        FlowEdge edge_36 = new FlowEdge(9, node6);
        node3.getOutgoingEdges().add(edge_35);
        node3.getOutgoingEdges().add(edge_36);

        /**
         * edges from node 4
         */
        FlowEdge edge_4t = new FlowEdge(10, sink);
        node4.getOutgoingEdges().add(edge_4t);

        /**
         * edges from node 5
         */
        FlowEdge edge_5t = new FlowEdge(10, sink);
        edge_5t.setFlow(10);
        node5.getOutgoingEdges().add(edge_5t);

        /**
         * edges from node 6
         */
        FlowEdge edge_6t = new FlowEdge(10, sink);
        edge_6t.setFlow(10);
        node5.getOutgoingEdges().add(edge_6t);

        FlowNetwork flowNetwork = new FlowNetwork();
        flowNetwork.setSink(sink);
        flowNetwork.setSource(source);
        flowNetwork.setVertexCount(7);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork);

        List<FlowEdge> foundPath = fordFulkerson.findAugmentingPath(flowNetwork.getSource(), new ArrayList<>());

        assertTrue(foundPath != null);
        assertTrue(foundPath.size() == 3);

        assertTrue(foundPath.get(0).equals(edge_s2));
        assertTrue(foundPath.get(1).equals(edge_24));
        assertTrue(foundPath.get(2).equals(edge_4t));
    }

    @Test
    public void test_fordFulkerson_shouldOK() {

        FlowNode source = new FlowNode(0);
        FlowNode node2 = new FlowNode(1);
        FlowNode node3 = new FlowNode(2);
        FlowNode node4 = new FlowNode(3);
        FlowNode node5 = new FlowNode(4);
        FlowNode sink = new FlowNode(5);

        /**
         * edges from source node
         */
        FlowEdge edge_s2 = new FlowEdge(10, node2);
        FlowEdge edge_s3 = new FlowEdge(10, node3);

        source.getOutgoingEdges().add(edge_s2);
        source.getOutgoingEdges().add(edge_s3);

        /**
         * edges from node 2
         */
        FlowEdge edge_24 = new FlowEdge(4, node4);
        FlowEdge edge_23 = new FlowEdge(2, node3);
        FlowEdge edge_25 = new FlowEdge(8, node5);

        node2.getOutgoingEdges().add(edge_23);
        node2.getOutgoingEdges().add(edge_24);
        node2.getOutgoingEdges().add(edge_25);

        /**
         * edges from node 3
         */
        FlowEdge edge_35 = new FlowEdge(9, node5);
        node3.getOutgoingEdges().add(edge_35);

        /**
         * edges from node 4
         */
        FlowEdge edge_4t = new FlowEdge(10, sink);
        node4.getOutgoingEdges().add(edge_4t);

        /**
         * edges from node 5
         */
        FlowEdge edge_54 = new FlowEdge(6, node4);
        FlowEdge edge_5t = new FlowEdge(10, sink);
        node5.getOutgoingEdges().add(edge_54);
        node5.getOutgoingEdges().add(edge_5t);

        FlowNetwork flowNetwork = new FlowNetwork();
        flowNetwork.setSink(sink);
        flowNetwork.setSource(source);
        flowNetwork.setVertexCount(6);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork);
        int maxFlow = fordFulkerson.run();

        assertTrue(maxFlow == 19);

    }




}
