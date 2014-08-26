/**
 *
 */
package markovchain;

/**
 * The class implements transition matrices used in {@link MarkovChain}.
 *
 * @author Dang Khoa Vo
 *
 */
public class MCTransition {

    final Matrix matrix;
    final int size;

    /**
     * The constructor of {@link MCTransition} class.
     *
     * @param data a 2-dimensional array that presents for a transition matrix
     * used for simulating Markov processes.
     *
     * @throws MCTransitionIllegalSizeException
     * @throws MCTransitionValueViolationException
     * @throws MCTransitionIdentityException
     */
    public MCTransition(double[][] data)
            throws MCTransitionIllegalSizeException,
            MCTransitionValueViolationException, MCTransitionIdentityException {
        this.matrix = new Matrix(data);
        if (matrix.isIdentity()) {
            throw new MCTransitionIdentityException();
        }
        if (matrix.cols() < 1 || !matrix.isSquare()) {
            throw new MCTransitionIllegalSizeException();
        }
        int i = matrix.isSumEachRowEqualOne();
        if (i > 0) {
            throw new MCTransitionValueViolationException(i - 1);
        }
        size = matrix.cols();
    }

    /**
     * The constructor of {@link MCTransition} class. It clone the parameter to
     * create another matrix transition.
     *
     * @param transition the new cloned {@link MCTransition} object.
     */
    public MCTransition(MCTransition transition) {
        super();
        this.matrix = new Matrix(transition.matrix.data().clone());
        this.size = transition.size();
    }

    private MCTransition(Matrix matrix) {
        super();
        this.matrix = matrix;
        size = matrix.cols();
    }

    /**
     * The constructor of {@link MCTransition} class, it create an data-empty
     * {@link MCTransition} object.
     *
     * @param size the the size of the transition matrix.
     */
    MCTransition(int size) {
        this.size = size;
        matrix = new Matrix(size, size);
    }

    /**
     * Getter for the data of the transition matrix.
     *
     * @return 2-dimensional array of data
     */
    public double[][] data() {
        return matrix.data();
    }

    /**
     * Getter for the size of the transition matrix.
     *
     * @return the size in integer
     */
    public int size() {
        return size;
    }

    /**
     * An utility to create a random transition matrix with a certain size.
     *
     * @param size the size of the transition matrix.
     * @param precise the exponential precise of data genrated
     *
     * @return {@link MCTransition}
     */
    public static MCTransition random(int size, int precise) {
        return new MCTransition(Matrix.random(size, size, precise));
    }

    /**
     * An utility to create a identify matrix with a certain size.
     *
     * @param size the size of a identify matrix
     * @return {@link MCTransition}
     */
    public static MCTransition identity(int size) {
        return new MCTransition(Matrix.identity(size));
    }

    /**
     * Used to transpose a transition matrix and return a new one after
     * transposition.
     *
     * @return {@link MCTransition}
     */
    public MCTransition transpose() {
        return new MCTransition(matrix.transpose());
    }

    /**
     * Used to plus the current transition matrix with an other transition
     * matrix and return a new one.
     *
     * @param tran a transition matrix used as the operator.
     * @return {@link MCTransition}
     * @throws MCTransitionIllegalSizeException
     */
    public MCTransition plus(MCTransition tran)
            throws MCTransitionIllegalSizeException {
        try {
            return new MCTransition(matrix.plus(tran.matrix));
        } catch (MatrixIllegalSizeException e) {
            throw new MCTransitionIllegalSizeException();
        }
    }

    /**
     * Used to minus the current transition matrix with a transition matrix and
     * return a new one.
     *
     * @param tran a transition matrix used as the operator.
     * @return {@link MCTransition}
     * @throws MCTransitionIllegalSizeException
     */
    public MCTransition minus(MCTransition tran)
            throws MCTransitionIllegalSizeException {
        try {
            return new MCTransition(matrix.minus(tran.matrix));
        } catch (MatrixIllegalSizeException e) {
            throw new MCTransitionIllegalSizeException();
        }
    }

    /**
     * Used to scalar the current transition matrix with amount of d and return
     * a new one.
     *
     * @param d amount of value to scalar.
     * @return {@link MCTransition}
     */
    public MCTransition scalar(double d) {
        return new MCTransition(matrix.scalar(d));
    }

    /**
     * Used to check whether a transition is square or not.
     *
     * @return true for square, false for otherwise.
     */
    public boolean isIdentity() {
        return this.matrix.isIdentity();
    }

    /**
     * Used to decompose a {@link MCTransition} into a smaller
     * {@link MCTransition}. The result {@link MCTransition} consists of only
     * nodes having identifier in the vertices array.
     *
     * @param vertices array of node identifiers need to be decomposed.
     * @return {@link MCTransition}
     */
    public MCTransition VertexDecompostion(int[] vertices) {
        int n = vertices.length;
        MCTransition decompostion = new MCTransition(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                decompostion.data()[i][j] = this.data()[vertices[i]][vertices[j]];
            }
        }
        return decompostion;

    }

    /**
     * Used to get adjacent nodes of a certain node.
     *
     * @param id identifier of node that need to get adjacencies.
     * @return list of adjacent node identifier.
     */
    public int[] getAdjacencies(int id) {
        int n = 0;
        for (int i = 0; i < this.size; i++) {
            if (Double.compare(matrix.data()[id][i], 0.0) != 0) {
                n++;
            }
        }
        if (n == 0) {
            return null;
        }
        int[] result = new int[n];
        int index = 0;
        for (int i = 0; i < this.size; i++) {
            if (Double.compare(matrix.data()[id][i], 0.0) != 0) {
                result[index++] = i;
            }
        }
        return result;
    }

    /**
     * Used to reverse all edge of a {@link MCTransition} object, then return a
     * new reversed {@link MCTransition} object.
     *
     * @return {@link MCTransition}
     */
    public MCTransition reverse() {
        MCTransition reverse = new MCTransition(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                reverse.data()[j][i] = this.data()[i][j];
            }
        }
        return reverse;
    }
}
