package markovchain;

/**
 * Thrown when the input states is out of transition matrices' bound or
 * negative.
 *
 * @author Dang Khoa Vo
 */
public class MCStateIllegalIndexException extends Exception {

    private int state;

    public int getState() {
        return state;
    }

    public MCStateIllegalIndexException(int state) {
        super();
        this.state = state;
    }

    @Override
    public String getMessage() {
        return "The input state " + state + " for simulation is illegal.";
    }
}
