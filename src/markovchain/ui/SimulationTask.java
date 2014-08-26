package markovchain.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.AbstractRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

/**
 *
 * @author Admin
 */
public class SimulationTask extends AbstractTaskView implements ActionListener {

    private MainFrame model;
    private ISimulationController controller;
    private JCommandButton btnStart;
    private JCommandButton btnNext;
    private JCommandButton btnPause;
    private JCommandButton btnStop;
    private JCommandButton btnDecrease;
    private JCommandButton btnIncrease;
    private JComboBox cboVertices;
    private JTextField txtSteps;
    private JLabel lblCurrentStep;
    private JButton btnRefresh;
    private JCommandButton btnMove;
    private JCommandButton btnZoomIn;
    private JCommandButton btnZoomOut;
    private JCommandButton btnCircle;
    private final int MAX_STEP = 2_000_000_000;

    public SimulationTask(MainFrame model, ISimulationController controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnStart) {
            int steps = 0;
            try {
                steps = Integer.parseInt(txtSteps.getText());
                if (steps <= 0 || steps > MAX_STEP) {
                    throw new IllegalArgumentException(
                            String.format("The number of steps is must be between 1 and %d",
                            MAX_STEP));
                }
            } catch (Exception ex) {
                model.showErrorDialog(String.format("The number of steps is must be between 1 and %d",
                        MAX_STEP));
                return;
            }
            try {
                controller.start((Vertex) cboVertices.getSelectedItem(), steps);
            } catch (Exception ex) {
                model.showErrorDialog(ex.getMessage());
            }
        } else if (source == btnPause) {
            controller.pause();
        } else if (source == btnNext) {
            controller.next();
        } else if (source == btnStop) {
            controller.stop();
        } else if (source == btnMove) {
            controller.move();
        } else if (source == btnIncrease) {
            controller.increaseSpeed();
        } else if (source == btnDecrease) {
            controller.decreaseSpeed();
        } else if (source == btnZoomIn) {
            controller.zoomIn();
        } else if (source == btnZoomOut) {
            controller.zoomOut();
        } else if (source == btnCircle) {
            controller.setCircleLayout();
        }


    }

    public void refreshInitialVertices() {
        Vertex[] vertices = McGraph.sortByName(model.graphEditor.graph.getVertices());
        cboVertices.removeAllItems();
        for (Vertex vertex : vertices) {
            cboVertices.addItem(vertex);
        }
    }

    public void createView() {
        // Band Config
        JRibbonBand bandConfig = createRibbonBand("Input", "config.png");
        txtSteps = new JTextField("100");
        txtSteps.setPreferredSize(new Dimension(50, 25));
        txtSteps.setHorizontalAlignment(JTextField.RIGHT);

        cboVertices = new JComboBox();
        refreshInitialVertices();
        lblCurrentStep = new JLabel("0");
        lblCurrentStep.setPreferredSize(new Dimension(75, 25));
        lblCurrentStep.setHorizontalAlignment(JTextField.RIGHT);

        btnRefresh = new JButton(getResizableIconFromResource("refresh.png", 16, 16));
        btnRefresh.setPreferredSize(new Dimension(28, 24));
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshInitialVertices();
            }
        });


        bandConfig.addRibbonComponent(createComponent("Initial State: ", cboVertices));
        bandConfig.addRibbonComponent(createComponent("Steps ", txtSteps));
        bandConfig.addRibbonComponent(createComponent("Cur. Step", lblCurrentStep));
        bandConfig.addRibbonComponent(createComponent(btnRefresh));


        setResizePolicies(bandConfig);


        // Band Edges
        JRibbonBand bandControls = createRibbonBand("Controls", "start.png");
        btnStart = createCommandButton("Start", "start.png");
        btnPause = createCommandButton("Pause", "pause.png");
        btnStop = createCommandButton("Stop", "stop.png");
        btnNext = createCommandButton("Next", "next.png");
        btnMove = createCommandButton("Move", "move.png");

        bandControls.addCommandButton(btnStart, RibbonElementPriority.TOP);
        bandControls.addCommandButton(btnPause, RibbonElementPriority.TOP);
        bandControls.addCommandButton(btnStop, RibbonElementPriority.TOP);
        bandControls.addCommandButton(btnNext, RibbonElementPriority.TOP);
        bandControls.addCommandButton(btnMove, RibbonElementPriority.TOP);
        setResizePolicies(bandControls);

        JRibbonBand bandSpeed = createRibbonBand("Speed", "speed.png");
        btnDecrease = createCommandButton("Decrease", "decrease.png");
        btnIncrease = createCommandButton("Increase", "increase.png");

        bandSpeed.addCommandButton(btnDecrease, RibbonElementPriority.TOP);
        bandSpeed.addCommandButton(btnIncrease, RibbonElementPriority.TOP);
        setResizePolicies(bandSpeed);

        // Band Zoom
        JRibbonBand bandZoom = createRibbonBand("Zoom", "zoom-in.png");

        btnZoomIn = createCommandButton("In", "zoom-in.png");
        btnZoomOut = createCommandButton("Out", "zoom-out.png");

        bandZoom.addCommandButton(btnZoomIn, RibbonElementPriority.TOP);
        bandZoom.addCommandButton(btnZoomOut, RibbonElementPriority.TOP);

        setResizePolicies(bandZoom);

        // Band Zoom
        JRibbonBand bandLayout = createRibbonBand("Arrange", "circle.png");

        btnCircle = createCommandButton("Circle layout", "circle.png");
        bandLayout.addCommandButton(btnCircle, RibbonElementPriority.TOP);

        setResizePolicies(bandLayout);

        // create task
        AbstractRibbonBand[] bands = createBands(bandConfig, bandControls, bandSpeed, bandZoom, bandLayout);
        task = new RibbonTask("Simulation", bands);

        // default states
        activeStartButton(true);
        activeNextButton(false);
        activePauseButton(false);
        activeMoveButton(false);
        activeIncreaseButton(false);
        activeDecreaseButton(false);


        // events
        btnStart.addActionListener(this);
        btnNext.addActionListener(this);
        btnPause.addActionListener(this);
        btnStop.addActionListener(this);
        btnMove.addActionListener(this);

        btnDecrease.addActionListener(this);
        btnIncrease.addActionListener(this);
        btnZoomOut.addActionListener(this);
        btnZoomIn.addActionListener(this);
        btnCircle.addActionListener(this);

    }

    public void activeRefreshButton(boolean value) {
        btnRefresh.setEnabled(value);
    }

    public void activeStartButton(boolean value) {
        btnStart.setEnabled(value);
    }

    public void activePauseButton(boolean value) {
        btnPause.setEnabled(value);
    }

    public void activeIncreaseButton(boolean value) {
        btnIncrease.setEnabled(value);
    }

    public void activeDecreaseButton(boolean value) {
        btnDecrease.setEnabled(value);
    }

    public void activeNextButton(boolean value) {
        btnNext.setEnabled(value);
    }

    public void activeMoveButton(boolean value) {
        btnMove.setEnabled(value);
    }

    public void setCurrentStep(int step) {
        lblCurrentStep.setText(Integer.toString(step));
    }

    public void startSimulation() {
        activeRefreshButton(false);
        activeStartButton(false);
        activeNextButton(false);
        activeMoveButton(false);
        activeInputArea(false);
        activePauseButton(true);
        activeIncreaseButton(true);
        activeDecreaseButton(true);
    }

    public void stopSimulation() {
        activeRefreshButton(true);
        activeStartButton(true);
        activeNextButton(false);
        activeMoveButton(false);
        activePauseButton(false);
        activeInputArea(true);
        activeIncreaseButton(false);
        activeDecreaseButton(false);
    }

    public void pauseSimulation() {
        activeStartButton(true);

        activeNextButton(true);
        activeMoveButton(true);

        activeIncreaseButton(false);
        activeDecreaseButton(false);
    }

    public void activeInputArea(boolean value) {
        txtSteps.setEditable(value);
        cboVertices.setEnabled(value);
    }
}
