package markovchain.ui;

/**
 *
 * @author Admin
 */
public class StatisticalInfo {

    private String name;
    private int hits;
    private double prob;

    public String getName() {
        return name;
    }

    public int getHits() {
        return hits;
    }

    public double getProb() {
        return prob;
    }

    public StatisticalInfo(String name, int hits, double prob) {
        this.name = name;
        this.hits = hits;
        this.prob = prob;
    }

    @Override
    public String toString() {
        return String.format("[%s, %d, %.7f]", name, hits, prob);
    }
}
