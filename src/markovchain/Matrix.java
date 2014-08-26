/**
 *
 */
package markovchain;

/**
 * @author khoavo
 *
 */
class Matrix {

    private final int rows;
    private final int cols;
    private double[][] data;

    Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    Matrix(double[][] data) {
        rows = data.length;
        cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    Matrix(Matrix A) {
        this(A.data);
    }

    int rows() {
        return rows;
    }

    int cols() {
        return cols;
    }

    double[][] data() {
        return data;
    }

    static Matrix random(int rows, int cols, int epsilon) {
        Matrix a = new Matrix(rows, cols);
        double precise = Math.pow(10, epsilon);
        double[] tmp = new double[cols];
        for (int i = 0; i < rows; i++) {
            double sum = 0.0;
            for (int j = 0; j < cols; j++) {
                tmp[j] = Math.random();
                sum += tmp[j];
            }
            for (int j = 0; j < cols - 1; j++) {
                a.data[i][j] = Math.round((tmp[j] / sum) * precise) / precise;
            }
            //a.data[i][j] = tmp[j] / sum;
            double sum2 = 0.0;
            for (int j = 0; j < cols - 1; j++) {
                sum2 += a.data[i][j];
            }
            double dif = 1.0 - sum2;
            if (Double.compare(sum2, 1.0) != 0) {
                a.data[i][cols - 1] += dif;
            }
        }
        return a;
    }

    boolean isIdentity() {
        return this.equals(Matrix.identity(this.cols));
    }

    static Matrix identity(int size) {
        Matrix a = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            a.data[i][i] = 1;
        }
        return a;
    }

    boolean isSquare() {
        if (this.rows == this.cols) {
            return true;
        } else {
            return false;
        }
    }

    int isSumEachRowEqualOne() {
        for (int i = 0; i < rows; i++) {
            double sum = 0.0;
            for (int j = 0; j < cols; j++) {
                sum += data[i][j];
            }
            if (Math.abs(1.0 - sum) > 10e-10) {
                return i + 1;
            }
        }
        return 0;
    }

    Matrix transpose() {
        Matrix a = new Matrix(this.rows, this.cols);
        for (int i = 0; i < a.cols; i++) {
            for (int j = 0; j < a.rows; j++) {
                a.data[i][j] = this.data[j][i];
            }
        }
        return a;
    }

    Matrix plus(Matrix a) throws MatrixIllegalSizeException {
        Matrix b = new Matrix(this);
        if (b.cols != a.cols || b.rows != a.rows) {
            throw new MatrixIllegalSizeException();
        }
        for (int i = 0; i < b.rows; i++) {
            for (int j = 0; j < b.cols; j++) {
                b.data[i][j] = b.data[i][j] + a.data[i][j];
            }
        }
        return b;
    }

    Matrix minus(Matrix a) throws MatrixIllegalSizeException {
        return this.plus(a.scalar(-1.0));
    }

    Matrix scalar(double d) {
        Matrix a = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                a.data[i][j] = d * this.data[i][j];
            }
        }
        return a;
    }

    boolean equals(Matrix a) {
        if (this.rows != a.rows || this.cols != a.cols) {
            return false;
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.data[i][j] != a.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    Matrix mul(Matrix a) throws MatrixIllegalSizeException {
        if (this.cols != a.rows) {
            throw new MatrixIllegalSizeException();
        }
        Matrix b = new Matrix(this.rows, a.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                for (int k = 0; k < this.cols; k++) {
                    b.data[i][j] += this.data[i][k] * a.data[k][j];
                }
            }
        }
        return b;
    }

    Matrix power(int n) {
        if (n == 1) {
            return new Matrix(this);
        }
        Matrix tmp = this.power(n / 2);
        if (n % 2 == 0) {
            try {
                return tmp.mul(tmp);
            } catch (MatrixIllegalSizeException e) {
                // nothing to do here
                System.out.print(e.getMessage());
                return null;
            }
        }
        try {
            return this.mul(tmp.mul(tmp));
        } catch (MatrixIllegalSizeException e) {
            // nothing to do here
            System.out.print(e.getMessage());
            return null;
        }
    }

    double[] convertToArray() {
        double[] r = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int z = (i + 1) * j;
                r[z] = this.data[i][j];
            }
        }
        return r;
    }

    static Matrix convertArrayToMatrix(double[] a) {
        Matrix m = new Matrix(1, a.length);
        System.arraycopy(a, 0, m.data[0], 0, a.length);
        return m;
    }
}
