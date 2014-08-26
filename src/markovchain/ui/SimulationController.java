package markovchain.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SimulationController implements ISimulationController {

    MainFrame model;
    GraphEditor graphEditor;
    McSimulator chain;
    Timer timer;
    SimulationTask simulationTask;
    int delay = 800;
    final int DEFAULT_DELAY = 800;
    final int MAX_DELAY = 1600;
    final int MIN_DELAY = 50;
    final int MIN_STEP = 2_000_000;    // minimum step to use progress
    boolean isRunning = false;

    public SimulationController(MainFrame model) {
        this.model = model;
        graphEditor = model.graphEditor;
        simulationTask = new SimulationTask(model, this);
        simulationTask.createView();

    }

    @Override
    public void next() {
        if (chain.hasNext()) {
            chain.nextState();
            graphEditor.vv.repaint();
            simulationTask.setCurrentStep(chain.getCurrentStep());
            model.resultView.updateResult(chain.getSimulationResult());

        } else {
            simulationTask.stopSimulation();
            isRunning = false;
            timer.stop();
        }
    }

    @Override
    public void pause() {
        if (timer != null) {
            timer.stop();
            simulationTask.pauseSimulation();
        }
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.stop();
            simulationTask.stopSimulation();
            graphEditor.graph.resetDefaultState();
            graphEditor.vv.repaint();
            isRunning = false;
            chain = null;
            timer = null;
        }
    }

    @Override
    public void increaseSpeed() {
        if (timer != null) {
            if (delay > MIN_DELAY) {
                delay = delay / 2;
                simulationTask.activeIncreaseButton(true);
            } else {
                simulationTask.activeIncreaseButton(false);
            }
            simulationTask.activeDecreaseButton(true);
            timer.setDelay(delay);
        }
    }

    @Override
    public void decreaseSpeed() {
        if (timer != null) {
            if (delay < MAX_DELAY) {
                delay = delay * 2;
                simulationTask.activeDecreaseButton(true);
            } else {
                simulationTask.activeDecreaseButton(false);
            }
            simulationTask.activeIncreaseButton(true);
            timer.setDelay(delay);
        }
    }

    @Override
    public void start(Vertex vertex, int numSteps) {

        if (!isRunning) {
            try {
                chain = new McSimulator(model.graphEditor.graph, vertex, numSteps);
            } catch (Exception ex) {
                model.showErrorDialog(ex.getMessage());
                return;
            }
            delay = DEFAULT_DELAY;
            model.resultView.clearOutput();
            simulationTask.startSimulation();
            simulationTask.setCurrentStep(0);

            try {
                model.resultView.updateResult(chain.getSimulationResult());
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            graphEditor.vv.repaint();
            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    next();
                }
            });
            timer.setDelay(delay);
            timer.start();
            isRunning = true;
        } else {
            timer.start();
            simulationTask.startSimulation();
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void move() {
        int currentStep = chain.getCurrentStep();
        int numSteps = chain.getNumberOfSteps();
        boolean isValid = false;
        do {
            if (currentStep >= numSteps) {
                model.showDialog("No more steps to move");
                simulationTask.activeMoveButton(false);
                return;
            }
            String value = model.showInputDialog(String.format("Enter the value between %d and %d", currentStep + 1, numSteps));
            if (value == null) {
                break;
            }
            try {
                int step = Integer.parseInt(value);
                if (step > currentStep && step <= numSteps) {
                    isValid = true;
                    new Thread(createRunnable(step)).start();
                }
            } catch (Exception e) {
            }
        } while (!isValid);
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
    public void setCircleLayout() {
        graphEditor.setCircleLayout();
    }

    private Runnable createRunnable(final int steps) {

        Runnable heavyRunnable = new Runnable() {
            @Override
            public void run() {
                int total = steps - chain.getCurrentStep();
                String format = "Moving %d of %d steps";
                ProgressMonitor monitor = null;
                try {
                    if (total < MIN_STEP) {
                        for (int i = 1; i <= total; i++) {
                            chain.nextState();
                        }
                    } else {
                        int tick = total / 100;
                        monitor = ProgressUtil.createModalProgressMonitor(model, total, false, 0);
                        monitor.start(String.format(format, 0, total));
                        for (int i = 1; i <= total; i++) {
                            chain.nextState();
                            if (i % tick == 0) {
                                monitor.setCurrent(String.format(format, i, total), i);
                            }
                        }
                    }

                } finally {
                    if (monitor != null && monitor.getCurrent() != monitor.getTotal()) {
                        monitor.setCurrent(String.format(format, total, total), total);
                    }
                    simulationTask.setCurrentStep(chain.getCurrentStep());
                    graphEditor.vv.repaint();
                    model.resultView.updateResult(chain.getSimulationResult());

                    if (!chain.hasNext()) {
                        simulationTask.activeMoveButton(false);
                    }
                }
            }
        };
        return heavyRunnable;
    }
}
