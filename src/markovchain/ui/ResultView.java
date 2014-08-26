package markovchain.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import markovchain.MCTransitionIdentityException;
import markovchain.MCTransitionIllegalSizeException;
import markovchain.MCTransitionSizeOneException;
import markovchain.MCTransitionValueViolationException;
import markovchain.MarkovChain;
import markovchain.Utilities;
import org.jdesktop.swingx.JXTable;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowManager;

/**
 *
 * @author Admin
 */
public class ResultView {

    private JTextArea txtSD;
    private JTextArea txtHistory;
    private JXTable table;
    private JButton btnCalculate;
    private JScrollPane sp;
    private JPanel pnlResult;
    private JPanel pnlHistory;
    private JPanel pnlSD;
    private boolean isBottom = false;
    private MainFrame model;

    public ResultView(MainFrame model) {
        this.model = model;
        initResultView(model.toolWindowManager);
        initHistoryView(model.toolWindowManager);
        initSDView(model.toolWindowManager);
    }

    public void updateResult(SimulationResult result) {
        updateTable(result.getResults());
        updateOutput(result);
    }

    public void setBottom(boolean value) {
        isBottom = value;
    }

    private void updateTable(ArrayList<StatisticalInfo> list) {
        if (isBottom) {
            table.setModel(new HozDataTableModel(list));

        } else {
            table.setModel(new DataTableModel(list));
        }
    }

    public void clearOutput() {
        txtHistory.setText("");
    }

    private void updateOutput(SimulationResult result) {
        if (result.getCurrentStep() % 1000 == 0) {
            txtHistory.setText(String.format("Simulation history\nInitial State: %s\nStep. Current state - Result [State, Hits, Prob.]", result.getInitialState()));
        }
        StringBuilder sb = new StringBuilder(txtHistory.getText()).append(StringUtils.NEW_LINE);
        sb.append(String.format("%d. ", result.getCurrentStep()));
        sb.append(String.format("%s - ", result.getCurrentState()));
        for (StatisticalInfo data : result.getResults()) {
            sb.append(data).append(" ");
        }

        txtHistory.setText(sb.toString());
    }

    private JPanel initResultView(ToolWindowManager toolWindowManager) {
        pnlResult = new JPanel(new BorderLayout());
        table = new JXTable();
        updateTable(new ArrayList<StatisticalInfo>());
        sp = new JScrollPane(table);
        pnlResult.add(sp, BorderLayout.CENTER);
        toolWindowManager.registerToolWindow("Result", // Id
                "", // Title
                null, // Icon
                pnlResult, // Component
                ToolWindowAnchor.LEFT);            // Anchor

        // Anchor
        ToolWindow output = toolWindowManager.getDockableById("Result");
        output.addPropertyChangeListener("anchor", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                boolean value = pce.getNewValue().toString().equalsIgnoreCase("BOTTOM");
                setBottom(value);
            }
        });
        return pnlHistory;
    }

    private void initHistoryView(ToolWindowManager toolWindowManager) {
        pnlHistory = new JPanel(new BorderLayout());
        txtHistory = new JTextArea(5, 20);
        txtHistory.setEditable(false);
        txtHistory.setLineWrap(true);

        toolWindowManager.registerToolWindow("History", // Id
                "", // Title
                null, // Icon
                pnlHistory, // Component
                ToolWindowAnchor.BOTTOM);
        pnlHistory.add(new JScrollPane(txtHistory), BorderLayout.CENTER);
    }

    private void initSDView(ToolWindowManager toolWindowManager) {
        pnlSD = new JPanel(new BorderLayout());
        txtSD = new JTextArea(5, 20);
        txtSD.setEditable(false);
        txtSD.setLineWrap(true);

        btnCalculate = new JButton("Show Stationary Distribution");
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                calculateSD();
            }
        });

        toolWindowManager.registerToolWindow("StationaryDistribution", // Id
                "", // Title
                null, // Icon
                pnlSD, // Component
                ToolWindowAnchor.BOTTOM);

        pnlSD.add(btnCalculate, BorderLayout.NORTH);
        pnlSD.add(new JScrollPane(txtSD), BorderLayout.CENTER);
    }

    private void calculateSD() {
        try {

            if (model.graphEditor.graph.getVertexCount() == 0) {
                model.showErrorDialog("Invalid graph");
                return;
            }
            MarkovChain mc;
            Map<Integer, Vertex> map;
            if (model.simController.chain != null) {
                mc = model.simController.chain.mc;
                map = model.simController.chain.map;
            } else {
                map = model.graphEditor.graph.createIndexVertexMap();
                double[][] mt = McGraph.createAdjMatrix(model.graphEditor.graph, map);
                mc = MarkovChain.create(mt);
            }

            StringBuilder sb = new StringBuilder("States: ");
            for (Vertex vertex : map.values()) {
                sb.append(String.format("[ %s ] ", vertex.getName()));
            }
            sb.append(StringUtils.NEW_LINE);
            sb.append(Utilities.getStationaryDistribution(mc));
            txtSD.setText(sb.toString());

        } catch (Exception e) {
            model.showErrorDialog("Invalid graph");
        }

    }
}
