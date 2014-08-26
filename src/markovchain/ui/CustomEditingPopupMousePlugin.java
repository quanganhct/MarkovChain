package markovchain.ui;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.MapTransformer;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

public class CustomEditingPopupMousePlugin<V, E> extends AbstractPopupGraphMousePlugin {

    protected Factory<V> vertexFactory;
    protected Factory<E> edgeFactory;

    public CustomEditingPopupMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
    }

    @Override
    protected void handlePopup(MouseEvent e) {
        // TODO Auto-generated method stub
        final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
        final Graph<V, E> graph = vv.getGraphLayout().getGraph();
        Point2D p = e.getPoint();// vv.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());

        GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
        if (pickSupport != null) {
            System.out.println();
            final V vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
            Point2D ip = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(Layer.VIEW, p);

            if (vertex != null) {
                JPopupMenu popup = new JPopupMenu();
                popup.add(new AbstractAction("Delete Vertex") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        graph.removeVertex(vertex);
                        vv.repaint();
                    }
                });

                popup.add(new AbstractAction("Edit Vertex") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Transformer<V, String> vs = vv
                                .getRenderContext()
                                .getVertexLabelTransformer();
                        if (vs instanceof MapTransformer) {
                            Map<V, String> map = ((MapTransformer) vs)
                                    .getMap();
                            // Layout<V, E> layout = vv.getGraphLayout();
                            // p is the screen point for the mouse event
                            // Point2D p = e.getPoint();

                            String newLabel = JOptionPane
                                    .showInputDialog("Enter the label for vertex", vertex);
                            if (newLabel != null) {
                                Vertex v = (Vertex) vertex;
                                map.put(vertex, newLabel);
                                v.setName(newLabel);
                                vv.repaint();
                            }
                        }

                    }
                });

                popup.show(vv, e.getX(), e.getY());
            } else if (edge != null) {
                JPopupMenu popup = new JPopupMenu();
                popup.add(new AbstractAction("Delete edge") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        graph.removeEdge(edge);
                        vv.repaint();
                    }
                });

                popup.add(new AbstractAction("Edit edge") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Transformer<E, String> es = vv
                                .getRenderContext()
                                .getEdgeLabelTransformer();
                        if (es instanceof MapTransformer) {
                            Map<E, String> map = ((MapTransformer) es)
                                    .getMap();

                            double value = GraphEditor.showInputProbability(((Edge) edge).getValue());
                            if (value != -1) {
                                map.put(edge, Double.toString(value));
                                if (edge instanceof Edge) {
                                    ((Edge) edge).setValue(value);
                                }
                                vv.repaint();
                            }
                        }
                    }
                });
                popup.show(vv, e.getX(), e.getY());
            }
        }
    }
}
