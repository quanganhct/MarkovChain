/**
 * 
 */
package markovchain;

/**
 * Used as an interface to compute a stationary distribution of a transition
 * matrix in which all states are in a strongly connected component. This
 * interface can also be used as a hook for other future implementation.
 * 
 * @author Dang Khoa Vo
 * 
 */

public interface MCStationaryDistributionMethod {

	/**
	 * Used to to compute compute stationary distribution.
	 * 
	 * @param transitions
	 *            transition matrix where its nodes are required to
	 *            be belong to a same strongly connected component.
	 * 
	 * @return a stationary distribution present by an array of probabilities in
	 *         which the order is consistent with the transition matrix's
	 * 
	 * @throws MCSingularTransitionException
	 * @throws MCTransitionIllegalSizeException
	 */
	public double[] solve(MCTransition transitions)
			throws MCSingularTransitionException,
			MCTransitionIllegalSizeException;

}
