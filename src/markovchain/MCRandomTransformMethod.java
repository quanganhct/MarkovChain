/**
 * 
 */
package markovchain;

/**
 * Used as an interface to create random events for transitions in a Markov
 * process. This interface can also be used as a hook for other future implementation
 * for randomly transformation.
 * 
 * @author Dang Khoa Vo
 * 
 */

public interface MCRandomTransformMethod {

	/**
	 * Used as a trigger to cause a move from a state to another state based on
	 * a random event.
	 * 
	 * @param distribution
	 *            the array of probabilities from a state to others
	 * 
	 * @return the identifier of the next state (node).
	 */
	public int nextState(double[] distribution);
}
