/**
 * 
 */
package markovchain;

/**
 * Thrown when the input transition matrices are singular matrices. That means
 * there is no stationary distribution in this case.
 * 
 * @author Dang Khoa Vo
 */
public class MCSingularTransitionException extends Exception {

	public MCSingularTransitionException() {
		super();
	}

	@Override
	public String getMessage() {
		return "The transitions is the singular matrix.";
	}
}
