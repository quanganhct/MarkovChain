/**
 * 
 */
package markovchain;

/**
 * @author khoavo
 * 
 */

class InverseTransformMethod implements MCRandomTransformMethod {

	InverseTransformMethod() {
		super();
	}

	@Override
	public int nextState(double[] distribution) {
		double sum = 0.0;
		double random = Math.random();
		int state = 0;
		for (int i = 0; i < distribution.length; i++) {
			sum += distribution[i];
			if (Double.compare(random, sum) <= 0) {
				state = i;
				break;
			}
		}
		return state;
	}

}
