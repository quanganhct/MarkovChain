package markovchain.ui;

/**
 *
 * @author Admin
 */
public class AppMenuController implements IAppMenuController {

    // add stop simulation before raise any event below
    MainFrame model;
    AppMenu appMenu;

    public AppMenuController(MainFrame model) {
        this.model = model;
        appMenu = new AppMenu(this);
        appMenu.createView();
    }

    @Override
    public void exit() {
        if (confirm()) {
            model.exit();
        }
    }

    @Override
    public void newGraph() {
        if (confirm()) {
            model.newGraph();
        }
    }

    @Override
    public void load() {
        if (confirm()) {
            model.loadGraph();
        }
    }

    @Override
    public void save() {
        if (confirm()) {
            model.saveGraph();
        }
    }

    @Override
    public void saveAs() {
        if (confirm()) {
            model.saveGraphAs();
        }
    }

    private boolean confirm() {
        if (model.simController.isRunning) {
            return model.confirmStopSimulationMode();
        }
        return true;
    }
}
