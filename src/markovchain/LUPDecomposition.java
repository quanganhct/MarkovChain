/**
 * 
 */
package markovchain;

/**
 * @author khoavo
 * 
 */
class LUPDecomposition {

	private MCTransition LU;
	private int n;
	private int[] pi;

	private LUPDecomposition(MCTransition matrix)
			throws MCTransitionSizeOneException {
		n = matrix.size();
		if (n == 1)
			throw new MCTransitionSizeOneException();
		LU = new MCTransition(matrix);
		pi = new int[n];
	}

	LUPDecomposition() {
		super();
	}

	private void partition() throws MCSingularTransitionException {
		for (int i = 0; i < n; i++)
			pi[i] = i;
		for (int k = 0; k < n; k++) {
			double p = 0.0;
			int k1 = 0;
			for (int i = k; i < n; i++)
				if (Double.compare(Math.abs(LU.data()[i][k]), p) > 0) {
					p = Math.abs(LU.data()[i][k]);
					k1 = i;
				}
			if (Double.compare(p, 0.0) == 0)
				throw new MCSingularTransitionException();
			swap(pi, k, k1);
			for (int i = 0; i < n; i++)
				swap(LU, k, i, k1, i);
			for (int i = k + 1; i < n; i++) {
				LU.data()[i][k] = LU.data()[i][k] / LU.data()[k][k];
				for (int j = k + 1; j < n; j++)
					LU.data()[i][j] = LU.data()[i][j] - LU.data()[i][k]
							* LU.data()[k][j];
			}
		}
	}

	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private static void swap(MCTransition A, int i1, int j1, int i2, int j2) {
		double tmp = A.data()[i1][j1];
		A.data()[i1][j1] = A.data()[i2][j2];
		A.data()[i2][j2] = tmp;
	}

	private int[] getP() {
		return pi;
	}

	private Matrix getU() {
		Matrix U = new Matrix(n, n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (i <= j)
					U.data()[i][j] = LU.data()[i][j];
				else
					U.data()[i][j] = 0.0;
		return U;
	}

	private Matrix getL() {
		Matrix L = new Matrix(n, n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (i > j)
					L.data()[i][j] = LU.data()[i][j];
				else if (i == j)
					L.data()[i][j] = 1.0;
				else
					L.data()[i][j] = 0.0;
		return L;
	}

	double[] LUPSolve(MCTransition matrix, double[] b)
			throws MCSingularTransitionException, MCTransitionIllegalSizeException {
		int n = b.length;
		if (n != matrix.size())
			throw new MCTransitionIllegalSizeException();
		LUPDecomposition decomposition = null;
		try {
			decomposition = new LUPDecomposition(matrix);
		} catch (MCTransitionSizeOneException e){
			return new double[]{1.0};
		} 
		
		double[] x = new double[n];
		double[] y = new double[n];
		decomposition.partition();
		Matrix L = decomposition.getL();
		Matrix U = decomposition.getU();
		int[] pi = decomposition.getP();
		for (int i = 0; i < n; i++) {
			double sum = 0.0;
			for (int j = 0; j < i; j++)
				sum += L.data()[i][j] * y[j];
			y[i] = b[pi[i]] - sum;
		}
		for (int i = n - 1; i >= 0; i--) {
			double sum = 0.0;
			for (int j = i + 1; j < n; j++)
				sum += U.data()[i][j] * x[j];
			x[i] = (y[i] - sum) / U.data()[i][i];
		}
		return x;
	}

}
