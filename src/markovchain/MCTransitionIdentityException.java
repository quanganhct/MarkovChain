/**
 * 
 */
package markovchain;

/**
 * Thrown when the input transition matrices are identity
 * matrices. That means, in this case, any arbitrary distribution is the
 * stationary distribution
 * 
 * @author Dang Khoa Vo
 */
public class MCTransitionIdentityException extends Exception {

	public MCTransitionIdentityException() {
		super();
	}

	@Override
	public String getMessage() {
		return "The transitions is an identity matrix.";
	}

}
