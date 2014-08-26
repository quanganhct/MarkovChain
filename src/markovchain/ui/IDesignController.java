package markovchain.ui;

import javax.swing.JComboBox;

import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 *
 * @author Admin
 */
public interface IDesignController {

    public JComboBox getModeComboBox();

    public void zoomIn();

    public void zoomOut();

    public void setVertexSize(int value);

    public void setVertexPosition(Position position);

    public void setEdgeShape(AbstractEdgeShapeTransformer<Vertex, Edge> shape);

    public void setVertexVisible(boolean isVisible);

    public void setEdgeVisible(boolean isVisible);

    public void repaint();

    public void setArrowCentered(boolean isCenter);

    public void setBoldlabel(boolean isBold);

    public void setEdgeOffset(int value);

    public void setCircleLayout();
}
