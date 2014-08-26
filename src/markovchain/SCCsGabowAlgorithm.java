/**
 * 
 */
package markovchain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author khoavo
 * 
 */
class SCCsGabowAlgorithm implements SCCsAlgorithm {

	private List<SCC> sccs;
	private MCTransition transition;
	private int[] sccVector;
	private int[] finishTime;
	private boolean[] visited;
	private boolean[] beingVisited;

	SCCsGabowAlgorithm() {
	}

	private SCCsGabowAlgorithm(MCTransition transition) {
		super();
		this.transition = transition;
		sccVector = new int[transition.size()];
		finishTime = new int[transition.size()];
		visited = new boolean[transition.size()];
		beingVisited = new boolean[transition.size()];
		for (int i = 0; i < transition.size(); i++) {
			visited[i] = false;
			beingVisited[i] = false;
			finishTime[i] = -1;
		}
	}

	@Override
	public List<SCC> SCCsDecompostion(MCTransition transition) {
		SCCsGabowAlgorithm algorithm = new SCCsGabowAlgorithm(transition);
		algorithm.sccs = new ArrayList<SCC>();
		int n = algorithm.excute();
		for (int i = 1; i <= n; i++) {
			int[] vertices = algorithm.getVerticesInSCC(i);
			if (vertices != null) {
				MCTransition t = algorithm.transition
						.VertexDecompostion(vertices);
				SCC scc = new SCC(t, vertices);
				scc.setDeadEnd(algorithm.checkDeadEnd(scc));
				algorithm.sccs.add(scc);
			}
		}
		return algorithm.sccs;
	}

	private int excute() {
		for (int i = 0; i < transition.size(); i++)
			dfs(transition, finishTime, i, -1);
		resetState();
		int index = indexOfMaxValue(finishTime);
		int scc = 1;
		MCTransition reverse = transition.reverse();
		while (index > -1) {
			dfs(reverse, finishTime.clone(), index, scc++);
			index = indexOfMaxValue(finishTime);
		}
		return scc;
	}

	private void dfs(final MCTransition transition, int[] finishTime, int id,
			int scc) {
		if (!beingVisited[id] && !visited[id]) {
			beingVisited[id] = true;
			int[] adjacencies = transition.getAdjacencies(id);
			if (adjacencies != null)
				for (int i = 0; i < adjacencies.length; i++)
					dfs(transition, finishTime, adjacencies[i], scc);
			sccVector[id] = scc;
			int time = maxValue(finishTime) + 1;
			finishTime[id] = time;
			visited[id] = true;
		}
	}

	private int indexOfMaxValue(int[] vector) {
		int max = -1;
		int index = -1;
		for (int i = 0; i < vector.length; i++)
			if (visited[i] == false && max < vector[i]) {
				max = vector[i];
				index = i;
			}
		return index;
	}

	private int maxValue(int[] vector) {
		int max = -1;
		for (int i = 0; i < vector.length; i++)
			if (max < vector[i])
				max = vector[i];
		return max;
	}

	private void resetState() {
		for (int i = 0; i < transition.size(); i++) {
			visited[i] = false;
			beingVisited[i] = false;
		}
	}

	private int[] getVerticesInSCC(int scc) {
		int size = 0;
		for (int i = 0; i < sccVector.length; i++)
			if (scc == sccVector[i])
				size++;
		if (size < 1)
			return null;
		int[] vertices = new int[size];
		int index = 0;
		for (int i = 0; i < sccVector.length; i++)
			if (scc == sccVector[i])
				vertices[index++] = i;
		return vertices;
	}

	private boolean checkDeadEnd(SCC scc) {
		int[] vertices = scc.indexes();
		for (int i = 0; i < vertices.length; i++) {
			int[] adjacencies = transition.getAdjacencies(vertices[i]);
			if (adjacencies == null)
				return true;
			for (int j = 0; j < adjacencies.length; j++)
				if (!contain(vertices, adjacencies[j]))
					return false;
		}
		return true;
	}

	private boolean contain(int[] a, int d) {
		for (int i = 0; i < a.length; i++)
			if (a[i] == d)
				return true;
		return false;
	}

}
