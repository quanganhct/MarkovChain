package markovchain.ui;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class McGraph extends DirectedSparseGraph<Vertex, Edge> {

    public McGraph() {
    }

    public Vertex getVertex(String name) {
        for (Vertex v : getVertices()) {
            if (v.getName().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }

//    public void changeVertexLabel(String oldName, String newName) {
//        Vertex v = getVertex(oldName);
//        if (v == null) {
//            throw new NullPointerException();
//        }
//        v.setName(newName);
//    }
    public boolean addEdge(String from, String to, double value) {
        Edge edge = new Edge(value);
        Vertex v1 = getOrAddVertex(from);
        Vertex v2 = getOrAddVertex(to);
        return addEdge(edge, v1, v2);
    }

    public boolean addEdge(int from, int to, double value) {
        return addEdge(Integer.toString(from), Integer.toString(to), value);
    }

    /**
     * If vertexId is not present, add it to vertexMap. In either case, return
     * the Vertex.
     */
    private Vertex getOrAddVertex(String name) {
        Vertex v = getVertex(name);
        if (v == null) {
            v = new Vertex(name);
            addVertex(v);
        }

        return v;
    }

    public void resetDefaultState() {
        for (Vertex v : getVertices()) {
            v.release();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("#V: %d%s", getVertexCount(), StringUtils.NEW_LINE));
        sb.append(String.format("#E: %d%s", getEdgeCount(), StringUtils.NEW_LINE));
        for (Vertex v : getVertices()) {
            sb.append(String.format("%s: ", v.name));
            for (Edge e : getOutEdges(v)) {
                Vertex v2 = getDest(e);
                sb.append(String.format("(%s, %.2f) ", v2.name, e.value, StringUtils.NEW_LINE));
            }
            sb.append(StringUtils.NEW_LINE);
        }
        return sb.toString();
    }

    public Map<Integer, Vertex> createIndexVertexMap() {
        Map<Integer, Vertex> map = new TreeMap<>();
        int index = 0;
        Vertex[] sortedVertices = sortByName(getVertices());
        for (Vertex v : sortedVertices) {
            map.put(index++, v);
            v.release();
        }
        return map;
    }

    public static boolean save(McGraph graph, String fileName) {
        Out out = null;
        try {
            StringBuilder sb = new StringBuilder();
            Map<Integer, Vertex> map = graph.createIndexVertexMap();
            int n = map.size();
            sb.append(String.format("%d", n)).append(StringUtils.NEW_LINE);
            for (int i = 0; i < n; i++) {
                sb.append(map.get(i).getName()).append(StringUtils.NEW_LINE);
            }
            double[][] mt = createAdjMatrix(graph);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mt[i][j] == 0) {
                        sb.append(String.format("%d ", 0));
                    } else {
                        sb.append(String.format("%f ", mt[i][j]));
                    }
                }
                sb.append(StringUtils.NEW_LINE);
            }
            out = new Out(fileName);
            out.print(sb.toString());
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public static McGraph load(String fileName) {
        In in = null;
        try {
            McGraph graph = new McGraph();
            Map<Integer, Vertex> map = new TreeMap<>();
            in = new In(fileName);
            String line = in.readLine().trim();
            int numStates = 0;
            int current = 0;
            String[] data = line.split("[\\s \\t]+");
            if (data.length == 1) {
                // read number of vertices
                numStates = Integer.parseInt(data[0]);
                for (int i = 0; i < numStates; i++) {
                    // read vertex name
                    String name = in.readLine().trim();
                    Vertex v = new Vertex(name);
                    graph.addVertex(v);
                    map.put(i, v);
                }
                data = in.readLine().split("[\\s \\t]+");
            } else {
                numStates = data.length;
                for (int i = 0; i < numStates; i++) {
                    Vertex v = new Vertex(Integer.toString(i + 1));
                    graph.addVertex(v);
                    map.put(i, v);
                }
            }
            while (true) {
                for (int i = 0; i < numStates; i++) {
                    double value = Double.parseDouble(data[i]);
                    if (value > 0) {
                        graph.addEdge(new Edge(value), map.get(current), map.get(i));
                    }
                }
                current++;
                if (current == numStates) {
                    break;
                }
                data = in.readLine().split("[\\s \\t]+");
            }
            if (in.hasNextLine()) {
                if (!StringUtils.IsNullOrWhiteSpace(in.readAll().trim())) {
                    return null;
                }
            }
            return graph;
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public static McGraph toGraph(double[][] transitions) {
        McGraph graph = new McGraph();
        int n = transitions.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double cost = transitions[i][j];
                if (cost > 0) {
                    graph.addEdge(i, j, cost);
                }
            }
        }
        return graph;
    }

    public static double[][] createAdjMatrix(McGraph graph) {
        return createAdjMatrix(graph, graph.createIndexVertexMap());
    }

    public static double[][] createAdjMatrix(McGraph graph, Map<Integer, Vertex> indexVertexMap) {
        int n = indexVertexMap.size();
        double[][] data = new double[n][n];
        for (int i = 0; i < n; i++) {
            Vertex v1 = indexVertexMap.get(i);
            for (int j = 0; j < n; j++) {
                Vertex v2 = indexVertexMap.get(j);
                Edge e = graph.findEdge(v1, v2);
                if (e != null) {
                    data[i][j] = e.value;
                } else {
                    data[i][j] = 0;
                }
            }
        }
        return data;
    }

    public static Vertex[] sortByName(Collection<Vertex> collection) {
        Vertex[] vertices = new Vertex[collection.size()];
        collection.toArray(vertices);
        Arrays.sort(vertices, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        return vertices;
    }
}
