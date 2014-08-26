/**
 *
 */
package markovchain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class is used for Markov chain simulation. It provides methods
 * implementing simulations of Markov processes and the computation of
 * stationary distributions, if any
 *
 * @author Dang Khoa Vo
 */
public class MarkovChain {

    private static int precise = 3;
    private final MCTransition transitions;
    private final int size;
    private MCRandomTransformMethod rtfMethod;
    private MCStationaryDistributionMethod sdMethod;
    private SCCsAlgorithm sccAlgorithm;
    private List<SCC> sccs = new ArrayList<>();
    private int[] states = null;

    private MarkovChain(MCTransition transitions)
            throws MCTransitionSizeOneException {
        this.transitions = transitions;
        if (transitions.size() == 1) {
            throw new MCTransitionSizeOneException();
        }
        this.size = transitions.size();
        setDefaultOptions();
    }

    /**
     * Used to create a {@link MarkovChain} object. It can be seen as the
     * constructor for {@link MarkovChain} class.
     *
     * @param data a 2-dimensional array that presents for a transition matrix
     * used for simulating Markov processes.
     *
     * @return {@link MarkovChain}
     *
     * @throws MCTransitionIllegalSizeException
     * @throws MCTransitionValueViolationException
     * @throws MCTransitionSizeOneException
     * @throws MCTransitionIdentityException
     */
    public static MarkovChain create(double[][] data)
            throws MCTransitionIllegalSizeException,
            MCTransitionValueViolationException, MCTransitionSizeOneException,
            MCTransitionIdentityException {
        return new MarkovChain(new MCTransition(data));
    }

    /**
     * Used to save the transition matrix of a {@link MarkovChain} object to a
     * text file.
     *
     * @param file the path and the name of the file.
     *
     * @throws IOException
     */
    public void save(String file) throws IOException {
        save(transitions.data(), file, precise);
    }

    /**
     * Used to load a transition matrix from a text file to create a new
     * {@link MarkovChain} object.
     *
     * @param file the path and the name of the file.
     *
     * @return {@link MarkovChain}
     *
     * @throws MCTransitionIllegalSizeException
     * @throws MCTransitionValueViolationException
     * @throws MCTransitionSizeOneException
     * @throws MCTransitionIdentityException
     * @throws IOException occur when there are problems in reading file.
     * @throws ArrayIndexOutOfBoundsException occur when the input file
     * vilolates the defined file format.
     * @throws NumberFormatException occur when values in the input file cannot
     * be convert in to double value.
     *
     */
    public static MarkovChain load(String file)
            throws MCTransitionIllegalSizeException,
            MCTransitionValueViolationException, MCTransitionSizeOneException,
            MCTransitionIdentityException, NumberFormatException,
            ArrayIndexOutOfBoundsException, IOException {
        double[][] data = read(file);
        if (data.length == 1) {
            throw new MCTransitionSizeOneException();
        }
        return new MarkovChain(new MCTransition(data));
    }

    /**
     * Used to create a {@link MarkovChain} object that has a random transition
     * matrix with a certain size.
     *
     * @param size the size of the transition matrix.
     *
     * @return {@link MarkovChain}.
     *
     * @throws MCTransitionSizeOneException
     * @throws MCTransitionIllegalSizeException
     */
    public static MarkovChain random(int size)
            throws MCTransitionSizeOneException,
            MCTransitionIllegalSizeException {
        if (size == 1) {
            throw new MCTransitionSizeOneException();
        } else if (size < 1) {
            throw new MCTransitionIllegalSizeException();
        }
        return new MarkovChain(MCTransition.random(size, precise));
    }

    /**
     * Used to create a random transition matrix with a certain size and store
     * it in a text file.
     *
     * @param file the path and the name of the file.
     * @param size the size of the random transition matrix.
     * @throws IOException
     */
    public static void random(String file, int size) throws IOException {
        save(MCTransition.random(size, precise).data(), file, precise);
    }

    /**
     * Setter for the decimal dot precise of numbers used in the transition
     * matrix when they are generated randomly, the default is 3
     *
     * @param precise the decimal dot precise
     */
    public static void setPrecise(int precise) {
        MarkovChain.precise = precise;
    }

    private void setDefaultOptions() {
        setSdMethod(new LinearEquationMethod());
        setRtfMethod(new InverseTransformMethod());
        setSccAlgorithm(new SCCsGabowAlgorithm());
    }

    /**
     * Setter for a new implementation to compute stationary distributions.
     *
     * @param sdMethod an object of classes that implement
     * {@link MCStationaryDistributionMethod}.
     */
    public void setSdMethod(MCStationaryDistributionMethod sdMethod) {
        this.sdMethod = sdMethod;
    }

    /**
     * Setter for a new implementation to create a random move for Markov
     * process simulation.
     *
     * @param rtfMethod an object of classes that implement
     * {@link MCRandomTransformMethod}.
     */
    public void setRtfMethod(MCRandomTransformMethod rtfMethod) {
        this.rtfMethod = rtfMethod;
    }

    /**
     * Setter for a new implementation decompose transition matrices into
     * strongly connected components.
     *
     * @param sccAlgorithm an object of classes that implement
     * {@link SCCsAlgorithm}.
     */
    public void setSccAlgorithm(SCCsAlgorithm sccAlgorithm) {
        this.sccAlgorithm = sccAlgorithm;
    }

    /**
     * Getter for an array of steps that was simulated in the previous
     * simulation.
     *
     * @return an array of state identifiers, if there are a simulation carried
     * out beforehand, return an null array.
     */
    public int[] getSimulatingSteps() {
        return states;
    }

    /**
     * Getter for a transition matrix.
     *
     * @return a 2-dimensional array of the transition matrix.
     */
    public double[][] getTransitions() {
        return transitions.data();
    }

    /**
     * Used to simulate Markov process.
     *
     * @param initState the initial state of a simulation.
     * @param numberOfSteps the number of steps is carried out.
     * @return an 1-dimensional array of average frequencies of visited states.
     * The order of state identifiers in this array is consistent with the
     * transition matrices.
     * @throws MCStateIllegalIndexException
     */
    public double[] simulatingDistribution(int initState, int numberOfSteps)
            throws MCStateIllegalIndexException {
        // modify: add code
        if (numberOfSteps < 0) {
            throw new IllegalArgumentException("Number of steps must be a non-negative number");
        }
        // modify: extract method
        double[] initDistribution = createInitDistribution(initState, size);
        return simulatingDistribution(initDistribution, numberOfSteps);
    }

    private double[] simulatingDistribution(double[] initDistribution,
            int numberOfSteps) {
        states = new int[numberOfSteps];
        int[] frequencies = new int[size];
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = 0;
        }
        int step = 0;
        int currentState = getNextState(initDistribution);
        while (step < numberOfSteps) {
            states[step++] = currentState;
            frequencies[currentState]++;
            // modify: extract method
            currentState = getNextState(transitions.data()[currentState]);
        }
        // modify: extract method
//        return calculateDistribution(frequencies, numberOfSteps);
        return calculateDistribution(frequencies, numberOfSteps);
    }

    /**
     * Used to compute stationary distributions.
     *
     * @return an 2-dimension matrix that presents for stationary distributions
     * in which each row in the matrix presents for a stationary distribution.
     *
     * @throws MCSingularTransitionException
     * @throws MCTransitionIdentityException
     */
    public double[][] stationaryDistribution()
            throws MCSingularTransitionException, MCTransitionIdentityException {
        if (transitions.isIdentity()) {
            throw new MCTransitionIdentityException();
        }
        sccs = sccAlgorithm.SCCsDecompostion(transitions);
        int sccNum = 0;
        for (SCC scc : sccs) {
            if (scc.isDeadEnd()) {
                sccNum++;
            }
        }
        double[][] result = new double[sccNum][size];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = 0.0;
            }
        }
        int i = 0;
        for (SCC scc : sccs) {
            if (scc.isDeadEnd()) {
                double[] sd = null;
                try {
                    sd = sdMethod.solve(scc.transitions());
                } catch (MCTransitionIllegalSizeException e) {
                }
                int[] indexes = scc.indexes();
                for (int j = 0; j < indexes.length; j++) {
                    result[i][indexes[j]] = sd[j];
                }
                i++;
            }
        }
        return result;
    }

    private static double[][] read(String fileName)
            throws NumberFormatException, IOException,
            ArrayIndexOutOfBoundsException {
        double[][] result = null;
        FileInputStream fstream = new FileInputStream(fileName);
        try (DataInputStream in = new DataInputStream(fstream)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            strLine = br.readLine();
            if (strLine != null) {
                String[] data = strLine.trim().split("[\\s \\t]+");
                result = new double[data.length][data.length];
                for (int i = 0; i < data.length; i++) {
                    result[0][i] = Double.valueOf(data[i]).doubleValue();
                }
            }
            int index = 1;
            while ((strLine = br.readLine()) != null) {
                String[] data = strLine.trim().split("\\s+");
                for (int i = 0; i < data.length; i++) {
                    result[index][i] = Double.valueOf(data[i]).doubleValue();
                }
                index++;
            }
        }
        return result;
    }

    private static boolean save(double[][] data, String fileName, int precise) {
        File file = new File(fileName);
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    out.format("%1." + precise + "f", data[i][j]);
                    out.write(" ");
                }
                out.write("\n");
                out.flush();
            }
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    // modify
    public int numStates() {
        return size;
    }
    // modify: extract methods

//    public static double[] calculateDistribution(int[] frequencies, int numberOfSteps) {
//        double[] distribution = new double[frequencies.length];
//        for (int i = 0; i < distribution.length; i++) {
//            distribution[i] = (frequencies[i] * 1.0) / numberOfSteps;
//        }
//        return distribution;
//    }
//    public static double[] calculateDistribution(int[] frequencies) {
//        int sum = 0;
//        int n = frequencies.length;
//        for (int i = 0; i < n; i++) {
//            sum += frequencies[i];
//        }
//        double[] distribution = new double[n];
//        for (int i = 0; i < distribution.length; i++) {
//            distribution[i] = (frequencies[i] * 1.0) / sum;
//        }
//        return distribution;
//    }
    public static double[] calculateDistribution(int[] frequencies, int steps) {
        int n = frequencies.length;
        double[] distribution = new double[n];
        if (steps <= 0) {
            steps = 1;
        }
        for (int i = 0; i < distribution.length; i++) {
            distribution[i] = (frequencies[i] * 1.0) / steps;
        }
        return distribution;
    }

    public static double[] createInitDistribution(int initState, int numStates) throws IllegalArgumentException, MCStateIllegalIndexException {
        if (initState >= numStates || initState < 0) {
            throw new MCStateIllegalIndexException(initState);
        }
        double[] initDistribution = new double[numStates];
        for (int i = 0; i < numStates; i++) {
            if (i == initState) {
                initDistribution[i] = 1.0;
            } else {
                initDistribution[i] = 0.0;
            }
        }
        return initDistribution;
    }

    public int getNextState(double[] states) {
        return rtfMethod.nextState(states);
    }
}
