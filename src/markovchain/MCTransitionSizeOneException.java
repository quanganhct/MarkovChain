package markovchain;

/**
 * Thrown when the input transition matrices have size of one.
 * 
 * @author Dang Khoa Vo
 */
public class MCTransitionSizeOneException extends Exception {
	public MCTransitionSizeOneException() {
		super();
	}

	@Override
	public String getMessage() {
		return "The transitions has size of 1x1.";
	}
}
