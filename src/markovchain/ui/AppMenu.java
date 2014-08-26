package markovchain.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu;
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary;

/**
 *
 * @author Admin
 */
public class AppMenu extends RibbonApplicationMenu implements ActionListener {

    private static final String NEW_MENU = "New";
    private static final String OPEN_MENU = "Open";
    private static final String SAVE_MENU = "Save";
    private static final String SAVEAS_MENU = "Save As...";
    private static final String EXITMENU = "Exit";
    private RibbonApplicationMenuEntryPrimary newMenu;
    private RibbonApplicationMenuEntryPrimary openMenu;
    private RibbonApplicationMenuEntryPrimary saveMenu;
    private RibbonApplicationMenuEntryPrimary saveAsMenu;
    private RibbonApplicationMenuEntryPrimary exitMenu;
    IAppMenuController controller;

    public AppMenu(IAppMenuController controller) {
        this.controller = controller;
    }

    private RibbonApplicationMenuEntryPrimary createMenu(String icon, String title) {
        return new RibbonApplicationMenuEntryPrimary(
                AbstractTaskView.getResizableIconFromResource(icon), title, this,
                JCommandButton.CommandButtonKind.ACTION_ONLY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCommandButton) {
            JCommandMenuButton src = (JCommandMenuButton) e.getSource();
            String text = src.getText();
            switch (text) {
                case SAVE_MENU:
                    controller.save();
                    break;
                case SAVEAS_MENU:
                    controller.saveAs();
                    break;
                case NEW_MENU:
                    controller.newGraph();
                    break;
                case OPEN_MENU:
                    controller.load();
                    break;
                case EXITMENU:
                    controller.exit();
                    break;
            }
        }
    }

    public void createView() {
        newMenu = createMenu("new.png", NEW_MENU);
        openMenu = createMenu("open.png", OPEN_MENU);
        saveMenu = createMenu("save.png", SAVE_MENU);
        saveAsMenu = createMenu("saveas.png", SAVEAS_MENU);
        exitMenu = createMenu("close.png", EXITMENU);

        addMenuEntry(newMenu);
        addMenuEntry(openMenu);
        addMenuSeparator();
        addMenuEntry(saveMenu);
        addMenuEntry(saveAsMenu);
        addMenuSeparator();
        addMenuEntry(exitMenu);
    }
}
