package markovchain.ui;



import java.awt.Color;

/**
 *
 * @author Admin
 */
public class Vertex {

    private static final Color DEFAULT_COLOR = Color.RED;
    private static final Color HIT_COLOR = Color.GREEN;
    private static final Color TOGGLE_COLOR = Color.CYAN;
    Color color = DEFAULT_COLOR;
    String name;

    public Vertex(String name) {
        checkName(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void release() {
        color = DEFAULT_COLOR;
    }

    public void changeColor() {
        if (color == HIT_COLOR) {
            color = TOGGLE_COLOR;
        } else {
            color = HIT_COLOR;
        }
    }

    private void checkName(String name) {
        if (name == null || StringUtils.IsNullOrWhiteSpace(name)) {
            throw new IllegalArgumentException("Vertex name must not be empty");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
