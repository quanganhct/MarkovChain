package markovchain.ui;

import javax.swing.JComboBox;

import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class DesignController implements IDesignController {

    MainFrame model;
    GraphEditor graphEditor;

    public DesignController(MainFrame model) {
        this.model = model;
        graphEditor = model.graphEditor;
    }

    @Override
    public JComboBox getModeComboBox() {
        return graphEditor.gm.getModeComboBox();
    }

    @Override
    public void zoomIn() {
        graphEditor.zoomIn();
    }

    @Override
    public void zoomOut() {
        graphEditor.zoomOut();
    }

    @Override
    public void setVertexSize(int value) {
        graphEditor.setVertexSize(value);
    }

    @Override
    public void setVertexPosition(Position position) {
        graphEditor.setVertexPosition(position);
    }

    @Override
    public void setEdgeShape(AbstractEdgeShapeTransformer<Vertex, Edge> shape) {
        graphEditor.setEdgeShape(shape);
    }

    @Override
    public void setVertexVisible(boolean isVisible) {
        graphEditor.setVertexVisible(isVisible);
    }

    @Override
    public void repaint() {
        graphEditor.vv.repaint();
    }

    @Override
    public void setEdgeVisible(boolean isVisible) {
        graphEditor.setEdgeVisible(isVisible);
    }

    @Override
    public void setArrowCentered(boolean isCenter) {
        graphEditor.setArrowCentered(isCenter);
    }

    @Override
    public void setBoldlabel(boolean isBold) {
        graphEditor.setBoldLabel(isBold);
    }

    @Override
    public void setEdgeOffset(int value) {
        graphEditor.setEdgeOffset(value);
    }

    @Override
    public void setCircleLayout() {
        graphEditor.setCircleLayout();
    }
}
