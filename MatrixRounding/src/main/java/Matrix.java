import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class Matrix {

    @Getter
    @Setter
    private float content[][];

    @Getter
    @Setter
    private float rowSums[];

    @Getter
    @Setter
    private float columnSums[];


    public Matrix(int rows, int column) {
        content = new float[rows][column];
        rowSums = new float[rows];
        columnSums = new float[column];
    }

    public Matrix(float[][] content) {
        this.content = content;
    }

}
