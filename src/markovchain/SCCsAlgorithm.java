/**
 * 
 */
package markovchain;

import java.util.List;

/**
 * Used as an interface to compute strongly connected components of a graph.
 * This interface can also be used as a hook for other future implementation for
 * computing strongly connected components
 * 
 * @author Dang Khoa Vo
 * 
 */
public interface SCCsAlgorithm {

	/**
	 * Used to decompose a graphs presented by {@link MCTransition} into many
	 * {@link SCC}.
	 * 
	 * @param transition
	 *            the transition matrix that needs to compute strongly connected
	 *            components.
	 * 
	 * @return list of {@link SCC}
	 */
	public List<SCC> SCCsDecompostion(MCTransition transition);

}
