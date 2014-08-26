/**
 *
 */
package markovchain;

/**
 * Thrown when the input transition matrices have one or more columns having
 * sums over 1 or negative.
 *
 * @author Dang Khoa Vo
 */
public class MCTransitionValueViolationException extends Exception {

    private int state;

    public int getState() {
        return state;
    }

    public MCTransitionValueViolationException(int i) {
        super();
        this.state = i;
    }

    @Override
    public String getMessage() {
        return "Row " + state + " of the transitions is invalid.";
    }
}
