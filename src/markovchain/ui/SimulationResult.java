package markovchain.ui;

import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class SimulationResult {

    private String currentState;
    private String initialState;
    private int step;
    private ArrayList<StatisticalInfo> results;

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public int getCurrentStep() {
        return step;
    }

    public ArrayList<StatisticalInfo> getResults() {
        return results;
    }

    public SimulationResult(String initialState, String currentState, int step, ArrayList<StatisticalInfo> results) {
        this.initialState = initialState;
        this.currentState = currentState;
        this.step = step;
        this.results = results;
    }
}
