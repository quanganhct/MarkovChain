package markovchain.ui;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import org.apache.commons.collections15.Factory;

public class CustomMousePlugin<V, E> extends EditingGraphMousePlugin<V, E> {

    private PickingGraphMousePlugin<V, E> pickingPlugin;
    protected V vertex;
    protected E edge;
    protected double offsetx;
    /**
     * the y distance from the picked vertex center to the mouse point
     */
    protected double offsety;

    public CustomMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
        super(vertexFactory, edgeFactory);
        // TODO Auto-generated constructor stub
        pickingPlugin = new PickingGraphMousePlugin<>();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.isControlDown()) {
            down = e.getPoint();
            VisualizationViewer<V, E> vv = (VisualizationViewer) e.getSource();
            GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
            PickedState<V> pickedVertexState = vv.getPickedVertexState();
//            PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
            if (pickSupport != null && pickedVertexState != null) {
                Layout<V, E> layout = vv.getGraphLayout();
                // p is the screen point for the mouse event
                Point2D ip = e.getPoint();

                vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());
                if (vertex != null) {
                    if (pickedVertexState.isPicked(vertex) == false) {
                        pickedVertexState.clear();
                        pickedVertexState.pick(vertex, true);
                    }
                    // layout.getLocation applies the layout transformer so
                    // q is transformed by the layout transformer only
                    Point2D q = layout.transform(vertex);
                    // transform the mouse point to graph coordinate system
                    Point2D gp = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.LAYOUT, ip);

                    offsetx = (float) (gp.getX() - q.getX());
                    offsety = (float) (gp.getY() - q.getY());
                }
            }
            if (vertex != null) {
                e.consume();
            }
        } else {
            super.mousePressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.isControlDown()) {
            VisualizationViewer<V, E> vv = (VisualizationViewer) e.getSource();
            if (vertex != null) {
                Point p = e.getPoint();
                Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p);
                Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(down);
                Layout<V, E> layout = vv.getGraphLayout();
                double dx = graphPoint.getX() - graphDown.getX();
                double dy = graphPoint.getY() - graphDown.getY();
                PickedState<V> ps = vv.getPickedVertexState();

                for (V v : ps.getPicked()) {
                    Point2D vp = layout.transform(v);
                    vp.setLocation(vp.getX() + dx, vp.getY() + dy);
                    layout.setLocation(v, vp);
                }
                down = p;

            }

            if (vertex != null) {
                e.consume();
            }
            vv.repaint();
        } else {
            super.mouseDragged(e);
        }
    }
}
