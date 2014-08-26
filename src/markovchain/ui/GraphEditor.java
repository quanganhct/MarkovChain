package markovchain.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.CenterEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.Paint;
import javax.swing.JApplet;

public class GraphEditor extends JApplet {

    McGraph graph;
    AbstractLayout<Vertex, Edge> layout;
    VisualizationViewer<Vertex, Edge> vv;
    CustomModalGraphMouse<Vertex, Edge> gm;
    VertexShapeSizeAspect<Vertex, Edge> vssa;
    VertexFontTransformer<Vertex> vff;
    EdgeFontTransformer<Edge> eff;
    final protected ScalingControl scaler = new CrossoverScalingControl();

    public GraphEditor() {
        graph = new McGraph();

        layout = new CircleLayout(graph);
        layout.setSize(new Dimension(350, 350));

        vv = new VisualizationViewer<>(layout);

        vv.setBackground(Color.white);
        vv.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());
        vv.getRenderContext().setVertexLabelTransformer(
                MapTransformer.<Vertex, String>getInstance(LazyMap
                .<Vertex, String>decorate(
                new HashMap<Vertex, String>(),
                new ToStringLabeller<Vertex>())));

        vv.getRenderContext().setEdgeLabelTransformer(
                MapTransformer.<Edge, String>getInstance(LazyMap
                .<Edge, String>decorate(new HashMap<Edge, String>(),
                new ToStringLabeller<Edge>())));

        vv.setVertexToolTipTransformer(vv.getRenderContext()
                .getVertexLabelTransformer());

        vff = new VertexFontTransformer<>();
        eff = new EdgeFontTransformer<>();

        vv.getRenderContext().setVertexFontTransformer(vff);
        vv.getRenderContext().setEdgeFontTransformer(eff);

        gm = new CustomModalGraphMouse<>(vv.getRenderContext(), new VertexFactory(), new EdgeFactory());
        vv.setGraphMouse(gm);
        gm.setMode(Mode.EDITING);

        getContentPane().add(new GraphZoomScrollPane(vv));
    }

    private Transformer<Vertex, Paint> createVertexFillPaintTransformer() {
        return new Transformer<Vertex, Paint>() {
            @Override
            public Paint transform(Vertex v) {
                PickedState<Vertex> pickedState = vv.getPickedVertexState();
                if (pickedState.isPicked(v)) {
                    return Color.YELLOW;
                }
                return v.color;
            }
        };
    }

    public VisualizationViewer getVisualizationViewer() {
        return vv;
    }

    public CustomModalGraphMouse<Vertex, Edge> getMyModalGraphMouse() {
        return gm;
    }

    class VertexFactory implements Factory<Vertex> {

        int i = 0;

        @Override
        public Vertex create() {
            return new Vertex(Integer.toString(i++));
        }
    }

    class EdgeFactory implements Factory<Edge> {

        @Override
        public Edge create() {
            double probability = showInputProbability(1);
            if (probability != -1) {
                return new Edge(probability);
            }
            return null;
        }
    }

    public static Double showInputProbability(double value) {
        boolean inputAccepted = false;
        value = (value >= 0 && value <= 1) ? value : -1;
        while (!inputAccepted) {
            try {
                String s = JOptionPane.showInputDialog("Enter the value between 0.0 and 1.0");
                if (s != null) {
                    value = Double.parseDouble(s);
                    if (Edge.checkValue(value)) {
                        return value;
                    }
                } else {
                    inputAccepted = true;
                }
            } catch (NumberFormatException ex) {
            }
        }
        return value;
    }

    private final static class VertexShapeSizeAspect<V, E>
            extends AbstractVertexShapeTransformer<V>
            implements Transformer<V, Shape> {

        protected boolean stretch = false;
        protected boolean funny_shapes = false;
        protected Graph<V, E> graph;
//        protected AffineTransform scaleTransform = new AffineTransform();

        public VertexShapeSizeAspect(Graph<V, E> graphIn, final int i) {
            this.graph = graphIn;
            setSizeTransformer(new Transformer<V, Integer>() {
                @Override
                public Integer transform(V v) {
                    return i;
                }
            });
            setAspectRatioTransformer(new Transformer<V, Float>() {
                @Override
                public Float transform(V v) {
                    if (stretch) {
                        return (float) (graph.inDegree(v) + 1)
                                / (graph.outDegree(v) + 1);
                    } else {
                        return 1.0f;
                    }
                }
            });
        }

        public void setStretching(boolean stretch) {
            this.stretch = stretch;
        }

        public void setScaling(boolean scale) {
        }

        public void useFunnyShapes(boolean use) {
            this.funny_shapes = use;
        }

        @Override
        public Shape transform(V v) {
            if (funny_shapes) {
                if (graph.degree(v) < 5) {
                    int sides = Math.max(graph.degree(v), 3);
                    return factory.getRegularPolygon(v, sides);
                } else {
                    return factory.getRegularStar(v, graph.degree(v));
                }
            } else {
                return factory.getEllipse(v);
            }
        }
    }

    private final static class VertexFontTransformer<V>
            implements Transformer<V, Font> {

        protected boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        @Override
        public Font transform(V v) {
            if (bold) {
                return b;
            } else {
                return f;
            }
        }
    }

    private final static class EdgeFontTransformer<E> implements
            Transformer<E, Font> {

        protected boolean bold = false;
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font b = new Font("Helvetica", Font.BOLD, 12);

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        @Override
        public Font transform(E e) {
            if (bold) {
                return b;
            } else {
                return f;
            }
        }
    }

    public void setVertexSize(int value) {
        // TODO Auto-generated method stub
        vssa = new VertexShapeSizeAspect<>(graph, value);
        vv.getRenderContext().setVertexShapeTransformer(vssa);
        vv.repaint();
    }

    public void setVertexPosition(Position position) {
        // TODO Auto-generated method stub
        vv.getRenderer().getVertexLabelRenderer().setPosition(position);
        vv.repaint();
    }

    public void setEdgeShape(AbstractEdgeShapeTransformer<Vertex, Edge> shape) {
        // TODO Auto-generated method stub
        vv.getRenderContext().setEdgeShapeTransformer(shape);
    }

    public void setVertexVisible(boolean isVisible) {
        if (isVisible) {
            vv.getRenderContext().setVertexLabelTransformer(
                    MapTransformer.<Vertex, String>getInstance(LazyMap
                    .<Vertex, String>decorate(
                    new HashMap<Vertex, String>(),
                    new ToStringLabeller<Vertex>())));
        } else {
            vv.getRenderContext().setVertexLabelTransformer(new ConstantTransformer(null));
        }
    }

    public void setEdgeVisible(boolean isVisible) {
        if (isVisible) {
            vv.getRenderContext().setEdgeLabelTransformer(
                    MapTransformer.<Edge, String>getInstance(LazyMap
                    .<Edge, String>decorate(
                    new HashMap<Edge, String>(),
                    new ToStringLabeller<Edge>())));
        } else {
            vv.getRenderContext().setEdgeLabelTransformer(new ConstantTransformer(null));
        }
    }

    public void setArrowCentered(boolean isCenter) {
        // TODO Auto-generated method stub
        if (isCenter) {
            vv.getRenderer()
                    .getEdgeRenderer()
                    .setEdgeArrowRenderingSupport(
                    new CenterEdgeArrowRenderingSupport());
        } else {
            vv.getRenderer()
                    .getEdgeRenderer()
                    .setEdgeArrowRenderingSupport(
                    new BasicEdgeArrowRenderingSupport());
        }
    }

    public void setBoldLabel(boolean isBold) {
        // TODO Auto-generated method stub
        vff.setBold(isBold);
        eff.setBold(isBold);
    }

    public void zoomIn() {
        // TODO Auto-generated method stub
        scaler.scale(vv, 1.1f, vv.getCenter());
    }

    public void zoomOut() {
        // TODO Auto-generated method stub
        scaler.scale(vv, 1 / 1.1f, vv.getCenter());
    }

    /**
     *
     * @param mode: {Mode.EDITING, Mode.TRANSFORMING, Mode.PICKING}
     */
    public void setMode(Mode mode) {
        gm.setMode(mode);
    }

    public void setEdgeOffset(int value) {
        // TODO Auto-generated method stub
        AbstractEdgeShapeTransformer<Vertex, Edge> aesf = (AbstractEdgeShapeTransformer<Vertex, Edge>) vv
                .getRenderContext().getEdgeShapeTransformer();
        aesf.setControlOffsetIncrement(value);
        vv.repaint();
    }

    public void setCircleLayout() {
        Layout<Vertex, Edge> l = new CircleLayout<>(graph);
        l.setInitializer(vv.getGraphLayout());
        l.setSize(vv.getSize());

        LayoutTransition<Vertex, Edge> lt =
                new LayoutTransition<>(vv, vv.getGraphLayout(), l);
        Animator animator = new Animator(lt);
        animator.start();
        vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
        vv.repaint();
    }
}
