package markovchain.ui;

import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowManager;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

public class MainFrame extends JRibbonFrame {

    GraphEditor graphEditor;
    RibbonTask designRibbonTask;
    RibbonTask simRibonTask;
    JRibbon ribbon;
    SimulationController simController;
    DesignController designController;
    AppMenuController appMenuController;
    ResultView resultView;
    ToolWindowManager toolWindowManager;
    JDialog dlgWaiting;
    private String currentFilePath = null;

    public MainFrame() throws HeadlessException {
        setApplicationIcon(AbstractTaskView.getResizableIconFromResource("app2.png"));
        setTitle("Markov Chain Simulation");
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initialize();


    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Notice", JOptionPane.INFORMATION_MESSAGE);
    }

    public String showInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message, "Input", JOptionPane.WARNING_MESSAGE);
    }

    public boolean confirmStopSimulationMode() {
        int rs = JOptionPane.showConfirmDialog(this, "The simulation process is running. Are you sure you want to stop this process?",
                "Notice",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (rs == JOptionPane.YES_OPTION) {
            simController.stop();
            return true;
        }
        return false;
    }

    private void safeExitSimulationMode() {
        int rs = JOptionPane.showConfirmDialog(this, "The simulation process is running. Are you sure you want to stop this process?",
                "Notice",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (rs == JOptionPane.YES_OPTION) {
            simController.stop();
            ribbon.setSelectedTask(designRibbonTask);
        }
    }

    public void newGraph() {
        if (hasUnSavedGraph()) {
            int rs = JOptionPane.showConfirmDialog(this, "Do you want to save changes you made to this graph before creating new one?",
                    "Notice",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (rs == JOptionPane.NO_OPTION) {
                createNewInstance();
                this.dispose();
            } else if (rs == JOptionPane.YES_NO_OPTION) {
                if (saveGraph()) {
                    createNewInstance();
                    this.dispose();
                }
            }
        } else {
            createNewInstance();
            this.dispose();
        }
    }

    public boolean saveGraph() {
        if (StringUtils.IsNullOrWhiteSpace(currentFilePath)) {
            return saveGraphAs();
        } else {
            try {
                boolean rs = McGraph.save(graphEditor.graph, currentFilePath);
                if (rs) {
                    showDialog("Save successfully");
                } else {
                    showErrorDialog("Save fail");
                }
                return rs;
            } catch (Exception e) {
                showErrorDialog(e.getMessage());
                return false;
            }
        }
    }

    public boolean saveGraphAs() {
        try {
            String path = showSaveFileDialog();
            if (path != null) {
                if (McGraph.save(graphEditor.graph, path)) {
                    showDialog("Save successfully");
                    return true;
                } else {
                    showErrorDialog("Save fail");
                }
            }

        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
        return false;
    }

    public void loadGraph() {
        try {
            String path = showOpenFileDialog();
            if (path != null) {
                McGraph graph = McGraph.load(path);
                if (graph != null) {
                    currentFilePath = path;
                    graphEditor.graph = graph;
                    designController.setCircleLayout();
                } else {
                    showErrorDialog("Invalid file format. ");
                }
            }
        } catch (Exception e) {
            showErrorDialog("Invalid file format");
        }
    }

    public void exit() {
        if (hasUnSavedGraph()) {
            int rs = JOptionPane.showConfirmDialog(this, "Do you want to save changes?",
                    "Notice",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (rs == JOptionPane.NO_OPTION) {
                System.exit(0);
            } else if (rs == JOptionPane.YES_NO_OPTION) {
                if (saveGraph()) {
                    System.exit(0);
                }
            }
        } else {
            System.exit(0);
        }
    }

    public String showSaveFileDialog() {
        JFileChooser chooser = createFileChooser();
        int code = chooser.showSaveDialog(this);
        if (code == JFileChooser.APPROVE_OPTION) {
            return getPath(chooser);
        }
        return null;
    }

    public String showOpenFileDialog() {
        JFileChooser chooser = createFileChooser();
        int code = chooser.showOpenDialog(this);
        if (code == JFileChooser.APPROVE_OPTION) {
            return getPath(chooser);
        }
        return null;
    }

    private boolean hasUnSavedGraph() {
        if (!StringUtils.IsNullOrWhiteSpace(currentFilePath)
                || graphEditor.graph.getVertexCount() > 0) {
            return true;
        }
        return false;
    }

    private String getPath(JFileChooser chooser) {
        File selectedFile = chooser.getSelectedFile();
        String path = selectedFile.getPath();
        if (!StringUtils.endsWithIgnoreCase(path, ".txt")) {
            path += ".txt";
        }
        return path;
    }

    private JFileChooser createFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("data"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files (*.txt)", "txt");
        chooser.setFileFilter(filter);
        return chooser;
    }

    private void initialize() {

        graphEditor = new GraphEditor();
        ribbon = getRibbon();
        designController = new DesignController(this);
        DesignTask designTask = new DesignTask(designController);
        designRibbonTask = designTask.getTask();
        ribbon.addTask(designRibbonTask);

        simController = new SimulationController(this);
        simRibonTask = simController.simulationTask.getTask();
        ribbon.addTask(simRibonTask);


        appMenuController = new AppMenuController(this);
        ribbon.setApplicationMenu(appMenuController.appMenu);


        ribbon.addPropertyChangeListener("selectedTask", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                RibbonTask oldTask = (RibbonTask) pce.getOldValue();
                RibbonTask newTask = (RibbonTask) pce.getNewValue();
                //resultView.setVisible(newTask == simRibonTask);
                activeResultView(newTask == simRibonTask);
                if (oldTask == simRibonTask && simController.isRunning) {
                    ribbon.setSelectedTask(simRibonTask);
                    safeExitSimulationMode();
                } else if (newTask == simRibonTask) {
                    graphEditor.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                    simController.simulationTask.refreshInitialVertices();
                }
            }
        });

        addTaskbar("new.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                appMenuController.newGraph();
            }
        });
        addTaskbar("open.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                appMenuController.load();
            }
        });
        addTaskbar("save32.png", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                appMenuController.save();
            }
        });
        MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();
        toolWindowManager = windowManager;
        ContentManager contentManager = toolWindowManager.getContentManager();
        contentManager.addContent("GraphEditor",
                "GraphEditor",
                null, // An icon
                graphEditor);
        getContentPane().add(windowManager);


        resultView = new ResultView(this);

        pack();
    }

    private void activeResultView(boolean active) {
        for (ToolWindow window : toolWindowManager.getToolWindows()) {
            window.setAvailable(active);
        }
    }

    private void addTaskbar(String icon, ActionListener action) {
        JCommandButton c = new JCommandButton(AbstractTaskView.getResizableIconFromResource(icon));
        c.addActionListener(action);
        ribbon.addTaskbarComponent(c);
    }

    public static void createNewInstance() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
