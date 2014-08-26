/**
 * 
 */
package markovchain;

/**
 * The class impelemts a strongly connected component.
 * 
 * @author Dang Khoa Vo
 * 
 */
public class SCC {

	private final MCTransition transitions;
	private final int[] vertices;
	private boolean isDeadEnd;

	/**
	 * The constructor of {@link SCC} class.
	 * 
	 * @param transition
	 *            a {@link MCTransition} only contains nodes in a same strongly
	 *            connected component.
	 * 
	 * @param vertices
	 *            a 1-dimensional array. It presents for nodes in a same
	 *            strongly connected component with elements being the
	 *            identifiers of nodes
	 *            <p>
	 *            Node: The order of indexes in the above 2 parameters are
	 *            strictly required consistent. That means if the vertices is
	 *            [2, 4, 6], the indexes of of 0, 1 and 2 for rows and columns
	 *            in the transition are 2, 4 and 6, respectively.
	 *            <p>
	 *            For example: if the transition t is {{1, 1, 0}, {1, 0, 1}, {0,
	 *            1, 1}}. Then t[0][0] = 1 means there is an edge from 2 to 2,
	 *            t[0][1] = 1 means there is an edge from 2 to 4, t[0][2] = 0
	 *            means there is no edge from 2 to 6, and so on.
	 */
	public SCC(MCTransition transition, int[] vertices) {
		super();
		this.transitions = transition;
		this.vertices = vertices;
		this.isDeadEnd = false;
	}

	/**
	 * The getter for the transition matrix of the current strongly connected
	 * component.
	 * 
	 * @return {@link MCTransition}
	 */
	public MCTransition transitions() {
		return transitions;
	}

	/**
	 * Getter for the the identifiers of nodes in the current strongly connected
	 * component.
	 * 
	 * @return a 1-dimensional array of node identifiers
	 */
	public int[] indexes() {
		return vertices;
	}

	/**
	 * Getter for the dead end flag. A strongly connected component being dead
	 * end means there is no edge from it to any other strongly connected
	 * components.
	 * 
	 * @param isDeadEnd
	 *            true if strongly connected component is dead end, false
	 *            otherwise.
	 */
	public void setDeadEnd(boolean isDeadEnd) {
		this.isDeadEnd = isDeadEnd;
	}

	/**
	 * Getter for the dead end flag.
	 * 
	 * @return true for a dead end strongly connected component, false for
	 *         otherwise.
	 */
	public boolean isDeadEnd() {
		return isDeadEnd;
	}

}
