/**
 * 
 */
package markovchain;

/**
 * Thrown when the input transition matrices have violations in
 * size like not-square matrices.
 * 
 * @author Dang Khoa Vo
 */
public class MCTransitionIllegalSizeException extends Exception {
	
	public MCTransitionIllegalSizeException() {
		super();
	}

	@Override
	public String getMessage() {
		return "The transition has the illegal size.";
	}
}

