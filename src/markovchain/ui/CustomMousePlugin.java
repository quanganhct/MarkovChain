package markovchain.ui;

import java.awt.Cursor;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;

import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import markovchain.ui.GraphEditor.EdgeFactory;

import org.apache.commons.collections15.Factory;

public class CustomMousePlugin<V, E> extends EditingGraphMousePlugin<V, E> {

	private PickingGraphMousePlugin<V, E> pickingPlugin;
	protected V vertex;
	protected E edge;
	protected double offsetx;
	private DesignTask design = new DesignTask();
	protected double offsety;
	private PickingGraphMousePlugin<V, E> pickingMouse;

	public CustomMousePlugin(Factory<V> vertexFactory, EdgeFactory edgeFactory) {
		super(vertexFactory, (Factory<E>) edgeFactory);
		// TODO Auto-generated constructor stub
		pickingMouse = new PickingGraphMousePlugin<V, E>();
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
				.getSource();
		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		final Graph<V, E> graph = vv.getGraphLayout().getGraph();
		Point2D p = e.getPoint();
		// Delete Vertex and edge with button delete
		if (design.geDel()) {
			if (pickSupport != null) {
				final V vertex = pickSupport.getVertex(vv.getGraphLayout(),
						p.getX(), p.getY());
				final E edge = pickSupport.getEdge(vv.getGraphLayout(),
						p.getX(), p.getY());
				if (vertex != null) {
					for (E edges : graph.getInEdges(vertex)) {
						if (!graph.getSource(edges).equals(vertex)) {
							V source = graph.getSource(edges);
							E loop = graph.findEdge(source, source);
							if (loop == null) {
								graph.addEdge(
										((E) new Edge(((Edge) edges).getValue())),
										source, source);
							} else {
								graph.removeEdge(loop);
								graph.addEdge(
										((E) new Edge(((Edge) loop).getValue()
												+ ((Edge) edges).getValue())),
										source, source);
							}
						}
					}
					graph.removeVertex(vertex);
					vv.repaint();
				} else if (edge != null) {
					E loop = graph.findEdge(graph.getSource(edge),
							graph.getSource(edge));
					if (loop == null) {
						graph.addEdge((E) (new Edge(((Edge) edge).getValue())),
								graph.getSource(edge), graph.getSource(edge));
					} else {
						graph.removeEdge(loop);
						graph.addEdge((E) (new Edge(((Edge) loop).getValue()
								+ ((Edge) edge).getValue())),
								graph.getSource(edge), graph.getSource(edge));
					}
					graph.removeEdge(edge);
					vv.repaint();
				}
			}
		} else if (e.isControlDown()) {
			if (checkModifiers(e)) {
				if (pickSupport != null) {
					if ((graph instanceof DirectedGraph)) {
						this.edgeIsDirected = EdgeType.DIRECTED;
					} else {
						this.edgeIsDirected = EdgeType.UNDIRECTED;
					}
					V vertex = pickSupport.getVertex(vv.getModel()
							.getGraphLayout(), p.getX(), p.getY());
					if (vertex != null) {
						this.startVertex = vertex;
						this.down = e.getPoint();
						transformEdgeShape(this.down, this.down);
						vv.addPostRenderPaintable(this.edgePaintable);
						if (((e.getModifiers() & 0x1) != 0)
								&& (!(vv.getModel().getGraphLayout().getGraph() instanceof UndirectedGraph))) {
							this.edgeIsDirected = EdgeType.DIRECTED;
						}
						if (this.edgeIsDirected == EdgeType.DIRECTED) {
							transformArrowShape(this.down, e.getPoint());
							vv.addPostRenderPaintable(this.arrowPaintable);
						}
					} else {
						V newVertex = this.vertexFactory.create();
						Layout<V, E> layout = vv.getModel().getGraphLayout();
						graph.addVertex(newVertex);
						layout.setLocation(newVertex,
								vv.getRenderContext()
										.getMultiLayerTransformer()
										.inverseTransform(e.getPoint()));
						graph.addEdge((E) ((EdgeFactory) this.edgeFactory)
								.create(1.0), newVertex, newVertex,
								this.edgeIsDirected);
					}
				}
				vv.repaint();
			}
		} else {
			pickingMouse.mousePressed(e);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!design.geDel()) {
			if (e.isControlDown()) {
				if (checkModifiers(e)) {
					if (this.startVertex != null) {
						transformEdgeShape(this.down, e.getPoint());
						if (this.edgeIsDirected == EdgeType.DIRECTED) {
							transformArrowShape(this.down, e.getPoint());
						}
					}
					VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
							.getSource();

					vv.repaint();
				}
			} else {
				if (!design.geDel()) {
					pickingMouse.mouseDragged(e);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isControlDown()) {
			if (checkModifiers(e)) {
				VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
						.getSource();

				Point2D p = e.getPoint();
				Layout<V, E> layout = vv.getModel().getGraphLayout();
				GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
				if (pickSupport != null) {
					V vertex = pickSupport
							.getVertex(layout, p.getX(), p.getY());
					if ((vertex != null) && (this.startVertex != null)) {
						Graph<V, E> graph = vv.getGraphLayout().getGraph();
						E newEdge = this.edgeFactory.create();

						E loop = graph.findEdge(this.startVertex,
								this.startVertex);
						if (loop != null) {
							double balance = ((Edge) loop).getValue()
									- ((Edge) newEdge).getValue();
							if (balance != ((Edge) loop).getValue()) {
								if (balance > 0) {
									graph.removeEdge(loop);
									graph.addEdge(((E) new Edge(balance)),
											this.startVertex, this.startVertex,
											this.edgeIsDirected);
									graph.addEdge(newEdge, this.startVertex,
											vertex, this.edgeIsDirected);
								} else if (balance == 0) {
									graph.removeEdge(loop);
									graph.addEdge(newEdge, this.startVertex,
											vertex, this.edgeIsDirected);
								} else {
									JOptionPane
											.showMessageDialog(null,
													"Total output of the vertex cannot be greater than 1");
								}
							}
						} else {
							JOptionPane
									.showMessageDialog(null,
											"This vertex cannot have edge anymore (Total output = 1)");
						}
						vv.repaint();
					}
				}
				this.startVertex = null;
				this.down = null;
				this.edgeIsDirected = EdgeType.UNDIRECTED;
				vv.removePostRenderPaintable(this.edgePaintable);
				vv.removePostRenderPaintable(this.arrowPaintable);

			}
		} else {
			pickingMouse.mouseReleased(e);
		}
	}

	private void transformEdgeShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		float dist = (float) Math.sqrt(dx * dx + dy * dy);
		xform.scale(dist / this.rawEdge.getBounds().getWidth(), 1.0D);
		this.edgeShape = xform.createTransformedShape(this.rawEdge);
	}

	private void transformArrowShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x2, y2);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		this.arrowShape = xform.createTransformedShape(this.rawArrowShape);
	}
}