import network.FlowEdge;
import network.FlowNetwork;
import network.FlowNode;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Filip on 10/22/2016.
 */
public class MatrixRoundingTest {


    @Test
    public void test_convertToFlowNetwork_shouldOK() {

        int rows = 3;
        int columns = 3;
        float matrix[][] = new float[rows][columns];

        matrix[0][0] = 3.14f;
        matrix[0][1] = 6.8f;
        matrix[0][2] = 7.3f;
        matrix[1][0] = 9.6f;
        matrix[1][1] = 2.4f;
        matrix[1][2] = 0.7f;
        matrix[2][0] = 3.6f;
        matrix[2][1] = 1.2f;
        matrix[2][2] = 6.5f;

        float rowSum[] = {17.24f, 12.7f, 11.3f};
        float columnSum[] = {16.34f, 10.4f, 14.5f};

        Matrix matrixObj = new Matrix(matrix);
        matrixObj.setRowSums(rowSum);
        matrixObj.setColumnSums(columnSum);

        MatrixRounding matrixRounding = new MatrixRounding(matrixObj);

        FlowNetwork flowNetwork = matrixRounding.convertToFlowNetwork();

        testConvertedFlowNetwork(matrixRounding, flowNetwork);

    }

    @Test
    public void test_roundMatrix1() {

        float matrix[][] = new float[2][3];

        matrix[0][0] = 3.3f;
        matrix[0][1] = 4.2f;
        matrix[0][2] = 5.2f;
        matrix[1][0] = 1.1f;
        matrix[1][1] = 0.9f;
        matrix[1][2] = 3.1f;

        float rowSum[] = {12.7f, 5.1f};
        float columnSum[] = {4.4f, 5.1f, 8.3f};

        Matrix matrixObj = new Matrix(matrix);
        matrixObj.setRowSums(rowSum);
        matrixObj.setColumnSums(columnSum);

        MatrixRounding matrixRounding = new MatrixRounding(matrixObj);

        FlowNetwork flowNetwork = matrixRounding.convertToFlowNetwork();

        testConvertedFlowNetwork(matrixRounding, flowNetwork);

        Matrix roundedMatrix = matrixRounding.roundMatrix();

        testMatrixIsRounded(roundedMatrix);

    }


    @Test
    public void test_roundMatrix2() {

        int rows = 3;
        int columns = 3;
        float matrix[][] = new float[rows][columns];

        matrix[0][0] = 3.14f;
        matrix[0][1] = 6.8f;
        matrix[0][2] = 7.3f;
        matrix[1][0] = 9.6f;
        matrix[1][1] = 2.4f;
        matrix[1][2] = 0.7f;
        matrix[2][0] = 3.6f;
        matrix[2][1] = 1.2f;
        matrix[2][2] = 6.5f;

        float rowSum[] = {17.24f, 12.7f, 11.3f};
        float columnSum[] = {16.34f, 10.4f, 14.5f};

        Matrix matrixObj = new Matrix(matrix);
        matrixObj.setRowSums(rowSum);
        matrixObj.setColumnSums(columnSum);

        MatrixRounding matrixRounding = new MatrixRounding(matrixObj);

        FlowNetwork flowNetwork = matrixRounding.convertToFlowNetwork();

        testConvertedFlowNetwork(matrixRounding, flowNetwork);

        Matrix roundedMatrix = matrixRounding.roundMatrix();

        testMatrixIsRounded(roundedMatrix);

    }


    private void testMatrixIsRounded(Matrix matrix) {

        for (int i = 0; i < matrix.getContent().length; i++) {
            float rowSum = 0;
            for (int j = 0; j < matrix.getContent()[i].length; j++) {
                rowSum = rowSum + matrix.getContent()[i][j];
            }
            assertTrue(rowSum == matrix.getRowSums()[i]);
        }

        for (int i = 0; i < matrix.getContent()[0].length; i++) {
            float columnSum = 0;
            for (int j = 0; j < matrix.getContent().length; j++) {
                columnSum = columnSum + matrix.getContent()[j][i];
            }
            assertTrue(columnSum == matrix.getColumnSums()[i]);
        }

    }

    private void testConvertedFlowNetwork(MatrixRounding matrixRounding, FlowNetwork flowNetwork) {

        Matrix matrixObj = matrixRounding.getMatrix();
        float matrix[][] = matrixObj.getContent();

        int rows = matrix.length;
        int columns = matrix[0].length;

        assertTrue(flowNetwork.getVertexCount() == rows + columns + 2);
        assertTrue(matrixRounding.getRowNodes().size() == rows);
        assertTrue(matrixRounding.getColumnNodes().size() == columns);

        //testing rows sums
        for (FlowEdge edge : matrixRounding.getSourceOfTheMatrix().getOutgoingEdges()) {
            int targetId = edge.getTarget().getId();

            float lowerBounds = edge.getLowerBounds();
            float upperBounds = edge.getUpperBounds();

            assertTrue(matrixObj.getRowSums()[targetId - 1] - lowerBounds <= 1f);
            assertTrue(upperBounds - matrixObj.getRowSums()[targetId - 1] <= 1f);

        }

        //testing the content of the matrix
        for (FlowNode rowNode : matrixRounding.getRowNodes()) {

            for (FlowEdge edge : rowNode.getOutgoingEdges()) {
                int rowNodeId = rowNode.getId();
                int targetId = edge.getTarget().getId();

                float lowerBounds = edge.getLowerBounds();
                float upperBounds = edge.getUpperBounds();

                assertTrue(matrix[rowNodeId - 1][targetId - rows - 1] - lowerBounds <= 1f);
                assertTrue(upperBounds - matrix[rowNodeId - 1][targetId - rows - 1] <= 1f);

            }

        }

        //testing columns sums
        for (FlowNode columnNode : matrixRounding.getColumnNodes()) {
            for (FlowEdge edge : columnNode.getOutgoingEdges()) {

                float lowerBounds = edge.getLowerBounds();
                float upperBounds = edge.getUpperBounds();

                assertTrue(matrixObj.getColumnSums()[columnNode.getId() - rows - 1] - lowerBounds <= 1f);
                assertTrue(upperBounds - matrixObj.getColumnSums()[columnNode.getId() - rows - 1] <= 1f);

            }

            assertTrue(matrixRounding.getSinkOfTheMatrix().getOutgoingEdges().size() == 1);
            assertTrue(matrixRounding.getSinkOfTheMatrix().getOutgoingEdges().get(0).getCapacity() == Integer.MAX_VALUE);

        }

    }

}
