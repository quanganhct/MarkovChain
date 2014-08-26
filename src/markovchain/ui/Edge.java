package markovchain.ui;

/**
 *
 * @author Admin
 */
public class Edge {

     double value;

    public Edge(double value) {
        if (!checkValue(value)) {
            throw new IllegalArgumentException("Cost must be between 0.0 and 1.0");
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (checkValue(value)) {
            this.value = value;
        }
    }

    public static boolean checkValue(double value) {
        return value >= 0 && value <= 1;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
