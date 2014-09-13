package markovchain.ui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.EventObject;
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

public class CustomEditingPopupMousePlugin<V, E> extends
		AbstractPopupGraphMousePlugin {

	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;

	public CustomEditingPopupMousePlugin(Factory<V> vertexFactory,
			Factory<E> edgeFactory) {
		this.vertexFactory = vertexFactory;
		this.edgeFactory = edgeFactory;
	}

	private void deleteEdge(Graph<V, E> graph, E edge) {
		E loop = graph.findEdge(graph.getSource(edge), graph.getSource(edge));
		if (loop == null) {
			graph.addEdge((E) (new Edge(((Edge) edge).getValue())),
					graph.getSource(edge), graph.getSource(edge));
		} else {
			graph.removeEdge(loop);
			graph.addEdge((E) (new Edge(((Edge) loop).getValue()
					+ ((Edge) edge).getValue())), graph.getSource(edge),
					graph.getSource(edge));
		}
		graph.removeEdge(edge);
	}

	private void editEdge(Graph<V, E> graph, E edge, Map<E, String> map,
			double value) {
		double balance = ((Edge) edge).getValue() - value;
		E loop = graph.findEdge(graph.getSource(edge), graph.getSource(edge));
		if (loop == null) {
			if (balance > 0) {
				graph.addEdge((E) (new Edge(balance)), graph.getSource(edge),
						graph.getSource(edge));
				map.put(edge, Double.toString(value));
				if (edge instanceof Edge) {
					((Edge) edge).setValue(value);
				}

			} else if (balance < 0) {
				JOptionPane.showMessageDialog(null,
						"Total output of the vertex cannot be greater than 1");
			}
		} else {
			balance = balance + ((Edge) loop).getValue();
			if (balance > 0) {
				graph.removeEdge(loop);
				graph.addEdge((E) (new Edge(balance)), graph.getSource(edge),
						graph.getSource(edge));
				map.put(edge, Double.toString(value));
				if (edge instanceof Edge) {
					((Edge) edge).setValue(value);
				}
			} else if (balance == 0) {
				graph.removeEdge(loop);
				map.put(edge, Double.toString(value));
				if (edge instanceof Edge) {
					((Edge) edge).setValue(value);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Total output of the vertex cannot be greater than 1");
			}
			System.out.println("Value:" + value + " ; Balance:" + balance);
		}
	}

	@Override
	protected void handlePopup(MouseEvent e) {
		// TODO Auto-generated method stub
		final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
				.getSource();
		final Graph<V, E> graph = vv.getGraphLayout().getGraph();
		Point2D p = e.getPoint();// vv.getRenderContext().getBasicTransformer().inverseViewTransform(e.getPoint());

		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		if (pickSupport != null) {
			System.out.println();
			final V vertex = pickSupport.getVertex(vv.getGraphLayout(),
					p.getX(), p.getY());
			final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(),
					p.getY());
			Point2D ip = vv.getRenderContext().getMultiLayerTransformer()
					.inverseTransform(Layer.VIEW, p);

			if (vertex != null) {
				JPopupMenu popup = new JPopupMenu();
				popup.add(new AbstractAction("Delete Vertex") {
					@Override
					public void actionPerformed(ActionEvent e) {
						for (E edge : graph.getInEdges(vertex)) {
							if (!graph.getSource(edge).equals(vertex)) {
								V source = graph.getSource(edge);
								E loop = graph.findEdge(source, source);
								if (loop == null) {
									graph.addEdge(
											((E) new Edge(((Edge) edge)
													.getValue())), source,
											source);
								} else {
									graph.removeEdge(loop);
									graph.addEdge(
											((E) new Edge(((Edge) loop)
													.getValue()
													+ ((Edge) edge).getValue())),
											source, source);
								}
							}
						}
						graph.removeVertex(vertex);
						vv.repaint();
					}
				});

				popup.add(new AbstractAction("Edit Vertex") {
					@Override
					public void actionPerformed(ActionEvent e) {
						Transformer<V, String> vs = vv.getRenderContext()
								.getVertexLabelTransformer();
						if (vs instanceof MapTransformer) {
							Map<V, String> map = ((MapTransformer) vs).getMap();
							// Layout<V, E> layout = vv.getGraphLayout();
							// p is the screen point for the mouse event
							// Point2D p = e.getPoint();

							String newLabel = JOptionPane.showInputDialog(
									"Enter the label for vertex", vertex);
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
						if (!(graph.getSource(edge).equals(graph.getDest(edge)))) {
							deleteEdge(graph, edge);
							vv.repaint();
						} else {
							JOptionPane.showMessageDialog(null,
									"This edge can not be deleted");
						}
					}
				});

				popup.add(new AbstractAction("Edit edge") {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!(graph.getSource(edge).equals(graph.getDest(edge)))) {
							Transformer<E, String> es = vv.getRenderContext()
									.getEdgeLabelTransformer();
							if (es instanceof MapTransformer) {
								Map<E, String> map = ((MapTransformer) es)
										.getMap();
								double value = GraphEditor
										.showInputProbability(((Edge) edge)
												.getValue());
								if (value != -1) {
									editEdge(graph, edge, map, value);

									vv.repaint();
								}
							}

						} else {
							JOptionPane.showMessageDialog(null,
									"This edge can not be edited");
						}
					}
				});
				popup.show(vv, e.getX(), e.getY());
			}
		}
	}
}
