import lombok.Getter;
import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MatrixRounding {

    @Getter
    private Matrix matrix;

    /**
     * This node represents the source of flow network created from the matrix
     */
    @Getter
    private FlowNode sourceOfTheMatrix;

    /**
     * This node represents the sink of flow network created from the matrix
     */
    @Getter
    private FlowNode sinkOfTheMatrix;


    /**
     * The nodes representing the rows in the flow network
     */
    @Getter
    private List<FlowNode> rowNodes = new ArrayList<>();

    /**
     * The nodes representing the columns in the flow network
     */
    @Getter
    private List<FlowNode> columnNodes = new ArrayList<>();

    private FordFulkerson fordFulkerson;

    public MatrixRounding(Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix roundMatrix() {

        System.out.println("Rounding the matrix...");

        FlowNetwork flowNetwork = convertToFlowNetwork();

        fordFulkerson = new FordFulkerson(flowNetwork);

        //this must return true
        //After running this method, flow network is modified to the form, which represents the rounded matrix
        //node with id = 0 is the old source, node with id = rows + columns is the old sink
        fordFulkerson.hasCirculationWithBounds();

        int rows = matrix.getContent().length;
        int columns = matrix.getContent()[0].length;

        float roundedRowsSums[] = roundRowsSums(rows);
        float roundedColumnsSums[] = roundColumnsSums(columns);
        float roundedMatrixContent[][] = roundMatrixContent(rows, columns);

        Matrix roundedMatrix = new Matrix(roundedMatrixContent);
        roundedMatrix.setColumnSums(roundedColumnsSums);
        roundedMatrix.setRowSums(roundedRowsSums);

        System.out.println("Matrix is rounded:");
        System.out.println(roundedMatrix);

        return roundedMatrix;
    }


    /**
     * rounding the sums of rows
     */
    private float[] roundRowsSums(int rows) {

        float roundedRowsSums[] = new float[rows];

        for (int i = 0; i < sourceOfTheMatrix.getOutgoingEdges().size(); i++) {
            FlowEdge edgeFromSource = sourceOfTheMatrix.getOutgoingEdges().get(i);
            int edgeFromsourceId = edgeFromSource.getTarget().getId();
            if (edgeFromsourceId > 0 && edgeFromsourceId <= rows) {//this edges connects us to node representing the row
                int roundedSum = 0;
                if (edgeFromSource.getFlow() == 0) {
                    roundedSum = (int) matrix.getRowSums()[i];
                } else {
                    roundedSum = (int) matrix.getRowSums()[i] + 1;
                }
                roundedRowsSums[i] = roundedSum;
            }

        }

        return roundedRowsSums;
    }

    /**
     * rounding the sums of columns
     */
    private float[] roundColumnsSums(int columns) {
        float roundedColumnsSums[] = new float[columns];

        for (int i = 0; i < columnNodes.size(); i++) {
            FlowNode columnNode = columnNodes.get(i);
            for (int j = 0; j < columnNode.getOutgoingEdges().size(); j++) {

                FlowEdge edgeToOldSink = columnNode.getOutgoingEdges().get(j);
                FlowNode target = edgeToOldSink.getTarget();

                if (target.getId() == sinkOfTheMatrix.getId()) {//the target is the old sink

                    int roundedSum = 0;
                    if (edgeToOldSink.getFlow() == 0) {
                        roundedSum = (int) matrix.getColumnSums()[i];
                    } else {
                        roundedSum = (int) matrix.getColumnSums()[i] + 1;
                    }
                    roundedColumnsSums[i] = roundedSum;
                }
            }
        }
        return roundedColumnsSums;
    }

    /**
     * rounding the content of the matrix
     */
    private float[][] roundMatrixContent(int rows, int columns) {

        float roundedMatrixContent[][] = new float[rows][columns];

        for (int i = 0; i < rowNodes.size(); i++) {

            FlowNode rowNode = rowNodes.get(i);


            for (int j = 0; j < rowNode.getOutgoingEdges().size(); j++) {

                FlowEdge edge = rowNode.getOutgoingEdges().get(j);

                int targetId = edge.getTarget().getId();

                if (targetId > rows && targetId <= rows + columns) {//is the target a node representing column ? It may happen that no, because edges may be added.

                    int roundedVal = 0;
                    if (edge.getFlow() == 0) {
                        roundedVal = (int) matrix.getContent()[i][targetId - rows - 1];
                    } else {
                        roundedVal = (int) matrix.getContent()[i][targetId - rows - 1] + 1;
                    }

                    roundedMatrixContent[i][j] = roundedVal;
                }

            }


        }

        return roundedMatrixContent;

    }

    /**
     * converts the @matrix to corresponding flow network
     *
     * @return
     */
    public FlowNetwork convertToFlowNetwork() {

        this.rowNodes = new ArrayList<>();
        this.columnNodes = new ArrayList<>();

        System.out.println("Converting the matrix to flow network...");

        FlowNetwork flowNetwork = new FlowNetwork();

        FlowNode source = new FlowNode(0);
        this.sourceOfTheMatrix = source;

        List<FlowNode> nodes = new ArrayList<>();

        int rows = matrix.getContent().length;
        int columns = matrix.getContent()[0].length;

        FlowNode sink = new FlowNode(rows + columns + 1);
        this.sinkOfTheMatrix = sink;

        //we have rows + columns rows
        for (int i = 0; i < rows; i++) {
            FlowNode flowNode = new FlowNode(i + 1);
            nodes.add(flowNode);
            this.rowNodes.add(flowNode);
        }

        for (int i = rows; i < rows + columns; i++) {
            FlowNode flowNode = new FlowNode(i + 1);
            nodes.add(flowNode);
            this.columnNodes.add(flowNode);
        }

        //creating edges between source to nodes representing the rows
        for (int i = 0; i < rows; i++) {
            int lowerBound = (int) matrix.getRowSums()[i];
            int upperBound = lowerBound + 1;
            FlowEdge flowEdge = new FlowEdge(lowerBound, upperBound, nodes.get(i));
            source.getOutgoingEdges().add(flowEdge);
        }

        // creating edges between each node representing the row and each node representing the column
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                float value = matrix.getContent()[i][j];
                int lowerBound = (int) value;
                int upperBound = lowerBound + 1;
                FlowEdge flowEdge = new FlowEdge(lowerBound, upperBound, nodes.get(rows + j));

                FlowNode startNode = nodes.get(i);
                startNode.getOutgoingEdges().add(flowEdge);
            }
        }

        //creating edges between nodes representing the columns and sink node
        for (int i = 0; i < columns; i++) {
            FlowNode startNode = nodes.get(rows + i);

            int lowerBound = (int) matrix.getColumnSums()[i];
            int upperBound = lowerBound + 1;

            FlowEdge flowEdge = new FlowEdge(lowerBound, upperBound, sink);
            startNode.getOutgoingEdges().add(flowEdge);
        }

        //creating edge between sink and source
        FlowEdge infinityEdge = new FlowEdge(Integer.MAX_VALUE, source);
        sink.getOutgoingEdges().add(infinityEdge);

        flowNetwork.setSource(source);
        flowNetwork.setSink(sink);
        flowNetwork.setVertexCount(rows + columns + 2);

        System.out.println("Converting operation finished");

        return flowNetwork;
    }

}
