package markovchain;

// inport the buit-in expeception
import java.io.IOException;
import markovchain.*;

public class Test {

	public static void main(String[] args) {
		int precise = 4;
		MarkovChain mc = null;
		// set the exponential precise
		MarkovChain.setPrecise(precise);
		try {
			// load markov chain from file data.txt
			mc = MarkovChain.load("D:\\Data\\Java\\mc11\\src\\data\\test.txt");
		} catch (NumberFormatException e) {
			// problem in create transitions from file
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			// problem in create transitions from file
			e.printStackTrace();
		} catch (MCTransitionIllegalSizeException e) {
			// the input transition matrices have violations in size like
			// not-square matrices
			e.printStackTrace();
		} catch (MCTransitionValueViolationException e) {
			// the input transition matrices have one or more columns having
			// sums over 1 or negative
			e.printStackTrace();
		} catch (MCTransitionSizeOneException e) {
			// the input transition matrices have size of one
			e.printStackTrace();
		} catch (MCTransitionIdentityException e) {
			// the input transition matrices are identity matrices
			e.printStackTrace();
		} catch (IOException e) {
			// problem in read or write file
			e.printStackTrace();
		}
		// show the simulation of markov chain mc, initial state = 0,
		// the number of step = 5000, exponential precise = 4
		Utilities.showSimulation(mc, 0, 10, true, precise);
		// show the stationary distributions of markov chain mc, exponential
		// precise = 4
		Utilities.showStationaryDistribution(mc, precise);
	}
}
