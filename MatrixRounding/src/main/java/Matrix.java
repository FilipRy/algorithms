import lombok.Getter;
import lombok.Setter;

/**
 * Represents the matrix
 */
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

    public Matrix(float[][] content) {
        this.content = content;

        rowSums = new float[content.length];
        columnSums = new float[content[0].length];

        for (int i = 0; i < content.length; i++) {
            for (int j = 0; j < content[i].length; j++) {
                rowSums[i] = rowSums[i] + content[i][j];
            }
        }

        for (int i = 0; i < content[0].length; i++) {
            for (int j = 0; j < content.length; j++) {
                columnSums[i] = columnSums[i] + content[j][i];
            }
        }


    }


    @Override
    public String toString() {
        String str = "";

        int i = 0;
        for (float[] rows : content) {
            for (float cell : rows) {
                str = str + "| " + cell + " ";
            }
            str = str + "| " + rowSums[i++] + '\n';
        }

        for (float columnSum : columnSums) {
            str = str + "| " + columnSum;
        }

        return str;
    }
}
