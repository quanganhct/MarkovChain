/**
 * 
 */
package markovchain;

/**
 * @author khoavo
 * 
 */
class LinearEquationMethod implements MCStationaryDistributionMethod {

	LinearEquationMethod() {
		super();
	}

	@Override
	public double[] solve(MCTransition transitions)
			throws MCSingularTransitionException,
			MCTransitionIllegalSizeException {
		int r = transitions.size();
		double[] u = new double[r];
		for (int i = 0; i < r; i++)
			u[i] = 1.0 / r;
		MCTransition U = new MCTransition(r);
		for (int i = 0; i < r; i++)
			for (int j = 0; j < r; j++)
				U.data()[i][j] = 1.0 / r;
		MCTransition I = MCTransition.identity(r);
		MCTransition tmp = ((I.minus(transitions)).plus(U)).transpose();
		return new LUPDecomposition().LUPSolve(tmp, u);
	}

}
