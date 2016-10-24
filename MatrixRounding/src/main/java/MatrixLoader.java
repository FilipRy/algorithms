import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Loads a matrix from a file
 */
public class MatrixLoader {


    /**
     *
     * @param filename
     * @return
     */
    public static Matrix loadMatrixFromFile(String filename) throws IOException {

        int lineNr = 0;

        int rows = 0;
        int columns = 0;

        float matrixContent[][] = new float[0][0];

        for (String line : Files.readAllLines(Paths.get(filename))) {

            if(lineNr == 0) {
                String[] sizes = line.split(" ");
                rows = Integer.parseInt(sizes[0]);
                columns = Integer.parseInt(sizes[1]);
                matrixContent = new float[rows][columns];
            } else {
                String[] cells = line.split(" ");

                for (int i = 0; i < cells.length; i++) {
                    matrixContent[lineNr - 1][i] = Float.parseFloat(cells[i]);
                }

            }

            lineNr++;

        }

        Matrix matrix = new Matrix(matrixContent);

        return matrix;

    }



}
