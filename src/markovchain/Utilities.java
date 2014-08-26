/**
 *
 */
package markovchain;

/**
 * The utility class for the usage of {@link MarkovChain} class.
 *
 * @author Dang Khoa Vo
 *
 */
public class Utilities {

    /**
     * Used as an utility to show a Markov process simulation.
     *
     * @param mc the {@link MarkovChain} object wanted to simulate.
     * @param initState the initial state of a simulation.
     * @param numberOfSteps the number of steps is carried out.
     * @param showSteps the flag for the show of each simulating steps, true for
     * show, false for otherwis.
     * @param precise the exponential precise of the result printed out.
     */
    public static void showSimulation(MarkovChain mc, int initState,
            int numberOfSteps, boolean showSteps, int precise) {
        double begin = System.currentTimeMillis();
        double[] a = null;
        try {
            a = mc.simulatingDistribution(initState, numberOfSteps);
        } catch (MCStateIllegalIndexException e) {
            System.out.printf("The state is out of the Markov chain bound.\n");
        }
        System.out.printf("The average frequencies:\n");
        System.out.print(" [");
        for (int i = 0; i < a.length; i++) {
            if (i == a.length - 1) {
                System.out.printf("%1." + precise + "f]\n", a[i]);
            } else {
                System.out.printf("%1." + precise + "f, ", a[i]);
            }
        }
        if (showSteps) {
            showSteps(mc);
        }
        System.out.printf("The simulation takes %.0f miliseconds.\n",
                System.currentTimeMillis() - begin);
    }

    private static void showSteps(MarkovChain mc) {
        int[] states = mc.getSimulatingSteps();
        if (states == null) {
            System.out.printf("There are no simulating steps to show.\n");
        } else {
            System.out.printf("The simulating steps:\n");
            for (int i = 0; i < states.length; i++) {
                if (i == states.length - 1) {
                    System.out.printf("state %d\n", states[i]);
                } else {
                    System.out.printf("state %d -> ", states[i]);
                }
            }
        }
    }

    /**
     * Used as an utility to show stationary distribution.
     *
     * @param mc the {@link MarkovChain} object wanted to compute stationary
     * distributions.
     *
     * @param precise the exponential precise of the result printed out.
     */
    public static void showStationaryDistribution(MarkovChain mc, int precise) {
        double[][] data = null;
        try {
            data = mc.stationaryDistribution();
        } catch (MCSingularTransitionException e) {
            System.out.printf("There are no stationary distribution.\n");
        } catch (MCTransitionIdentityException e) {
            System.out
                    .printf("The stationary distribution is any distribution.\n");
        }
        double begin = System.currentTimeMillis();
        System.out.printf("There are %d stationary distributions.\n",
                data.length);
        for (int i = 0; i < data.length; i++) {
            System.out.printf("+ Stationary distribution %d:\n", i + 1);
            System.out.print(" [");
            for (int j = 0; j < data[0].length; j++) {
                if (j == data[0].length - 1) {
                    System.out.printf("%1." + precise + "f]", data[i][j]);
                } else {
                    System.out.printf("%1." + precise + "f, ", data[i][j]);
                }
            }
            System.out.println();
        }
        System.out.printf("The computation takes %.0f miliseconds.\n",
                System.currentTimeMillis() - begin);

    }

    /**
     * Used as an utility to show the transition matrix.
     *
     * @param mc the {@link MarkovChain} object wanted to print the transition
     * matrix.
     *
     * @param precise the exponential precise of the result printed out.
     */
    public void showTransitions(MarkovChain mc, int precise) {
        for (int i = 0; i < mc.getTransitions().length; i++) {
            for (int j = 0; j < mc.getTransitions().length; j++) {
                System.out.printf("%1." + precise + "f ",
                        mc.getTransitions()[i][j]);
            }
            System.out.println();
        }
    }
    /*
     * public static void print(double[] a, int precise) {
     * System.out.print(" ["); for (int i = 0; i < a.length; i++) if (i ==
     * a.length - 1) System.out.printf("%1." + precise + "f]", a[i]); else
     * System.out.printf("%1." + precise + "f, ", a[i]); System.out.println(); }
     */

    // viet ham tinh cho nay nhe
    public static String getStationaryDistribution(MarkovChain mc) {
        double[][] data = null;
        try {
            data = mc.stationaryDistribution();
            StringBuilder sb;
            sb = new StringBuilder(String.format("There are %d stationary distributions.\n", data.length));
            for (int i = 0; i < data.length; i++) {
                sb.append(String.format("+ Stationary distribution %d:\n", i + 1));
                for (int j = 0; j < data[0].length; j++) {
                    sb.append(" [");
                    sb.append(String.format("%f5]", data[i][j]));
                }
                sb.append("\n");
            }
            return sb.toString();
        } catch (MCSingularTransitionException e) {
            return "There are no stationary distribution.\n";
        } catch (MCTransitionIdentityException e) {
            return "The stationary distribution is any distribution.\n";
        }
    }
}
