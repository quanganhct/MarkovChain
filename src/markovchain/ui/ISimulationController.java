package markovchain.ui;


/**
 *
 * @author Admin
 */
public interface ISimulationController {

    public void next();

    public void pause();

    public void stop();

    public void increaseSpeed();

    public void decreaseSpeed();

    public void start(Vertex vertex, int numSteps);

    boolean isRunning();

    public void move();

    void zoomIn();

    void zoomOut();

    void setCircleLayout();
}
