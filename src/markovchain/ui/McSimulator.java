package markovchain.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import markovchain.MCStateIllegalIndexException;
import markovchain.MCTransitionIdentityException;
import markovchain.MCTransitionIllegalSizeException;
import markovchain.MCTransitionSizeOneException;
import markovchain.MCTransitionValueViolationException;
import markovchain.MarkovChain;

/**
 *
 * @author Admin
 */
public final class McSimulator {

    Map<Integer, Vertex> map = new TreeMap<>();
    MarkovChain mc;
    private int numberOfSteps;
    private int currentStep;
    private int currentState;
    //private int[] states;
    private int[] frequencies;
    private int initState;

    public McSimulator(McGraph graph, Vertex initVertex, int numberOfSteps) throws IllegalArgumentException {
        try {
            this.map = graph.createIndexVertexMap();
            double[][] mt = McGraph.createAdjMatrix(graph, map);
            this.mc = MarkovChain.create(mt);
            Map.Entry<Integer, Vertex> entry = findEntry(initVertex);
            if (entry == null) {
                throw new IllegalArgumentException("Invalid initial state");
            }
            this.numberOfSteps = numberOfSteps;
            initState = entry.getKey();
            currentStep = 0;
            currentState = mc.getNextState(MarkovChain.createInitDistribution(initState, mc.numStates()));
            //states = new int[numberOfSteps];
            frequencies = new int[mc.numStates()];
            for (int i = 0; i < frequencies.length; i++) {
                frequencies[i] = 0;
            }
//            frequencies[currentState] = 1;
            getVertex(currentState).changeColor();

        } catch (MCStateIllegalIndexException ex) {
            throw new IllegalArgumentException("The state " + map.get(ex.getState()).getName() + " for simulation is illegal.");
        } catch (MCTransitionValueViolationException ex) {
            throw new IllegalArgumentException("The state " + map.get(ex.getState()).getName() + " has invalid transitions");
        } catch (MCTransitionIllegalSizeException | MCTransitionSizeOneException | MCTransitionIdentityException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid state");
        }
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getCurrentState() {
        return currentState;
    }

//    public int[] getStates() {
//        return states;
//    }

    public int[] getFrequencies() {
        return frequencies;
    }

    public boolean hasNext() {
        return currentStep < numberOfSteps;
    }

    public int nextState() {
        if (!hasNext()) {
            throw new IndexOutOfBoundsException();
        }

        int previousState = currentState;
        currentState = mc.getNextState(getCurrentDistribution());

        //tates[currentStep++] = currentState;
        currentStep++;
        frequencies[currentState]++;

        changeVertexColor(previousState, currentState);

        return currentState;
    }

    public void move(int step) {
        if (step < 0 || step > numberOfSteps) {
            throw new IllegalArgumentException(String.format("The step must be between %d and %d", currentStep + 1, numberOfSteps));
        }
        int i = currentStep;
        while (i < step) {
            nextState();
            i++;
        }
    }

    public double[] getDistributionForIteratedSteps() {
  //      int numSteps = hasNext() ? currentStep : numberOfSteps;
        return MarkovChain.calculateDistribution(frequencies, currentStep);
    }

    private double[] getCurrentDistribution() {
        return mc.getTransitions()[currentState];
    }

    public Vertex getVertex(int index) {
        if (!map.containsKey(index)) {
            throw new IllegalArgumentException("The key does not exist");
        }
        return map.get(index);
    }

    private void changeVertexColor(int previousState, int nextState) {
        if (previousState != nextState) {
            getVertex(previousState).release();
        }
        getVertex(nextState).changeColor();
    }

    private Map.Entry<Integer, Vertex> findEntry(Vertex v) {
        for (Map.Entry<Integer, Vertex> entry : map.entrySet()) {
            if (v == entry.getValue()) {
                return entry;
            }
        }
        return null;
    }

    private ArrayList<StatisticalInfo> getResults() {
        int n = map.size();
        ArrayList<StatisticalInfo> list = new ArrayList<>();
        double[] probs = MarkovChain.calculateDistribution(frequencies, currentStep);
        for (int i = 0; i < n; i++) {
            StatisticalInfo data = new StatisticalInfo(map.get(i).getName(), frequencies[i], probs[i]);
            list.add(data);
        }
        return list;
    }

    public SimulationResult getSimulationResult() {
        ArrayList<StatisticalInfo> results = getResults();
        return new SimulationResult(getVertex(initState).getName(), getVertex(currentState).getName(), currentStep, results);
    }
}
