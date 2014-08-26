package markovchain.ui;


import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import org.pushingpixels.flamingo.api.common.HorizontalAlignment;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.AbstractRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import resources.Resources;

/**
 *
 * @author Admin
 */
public abstract class AbstractTaskView {

    protected RibbonTask task;

    public RibbonTask getTask() {
        return task;
    }

    public static ResizableIcon getResizableIconFromResource(String resource) {
        return getResizableIconFromResource(resource, 32, 32);
    }

    public static ResizableIcon getResizableIconFromResource(String resource, int width, int height) {
        return ImageWrapperResizableIcon.getIcon(Resources.class.getResourceAsStream(resource), new Dimension(width, height));
    }

    public JRibbonComponent createComponent(JComponent button) {
        return createComponent("", button);
    }

    public JRibbonComponent createComponent(String caption, JComponent button) {
        JRibbonComponent com = new JRibbonComponent(null, caption, button);
        // left alignment
        com.setHorizontalAlignment(HorizontalAlignment.FILL);
        return com;
    }

    public void addToGroup(JRadioButton... buttons) {
        ButtonGroup group = new ButtonGroup();
        for (JRadioButton jRadioButton : buttons) {
            group.add(jRadioButton);
        }
    }

    public JRibbonBand createRibbonBand(String title, String icon) {
        return new JRibbonBand(title, getResizableIconFromResource(icon));
    }

    public void setResizePolicies(JRibbonBand band) {
        band.setResizePolicies((List) Arrays.asList(
                new CoreRibbonResizePolicies.None(band.getControlPanel()),
                new IconRibbonBandResizePolicy(band.getControlPanel())));
    }

    public JCommandButton createCommandButton(String title, String icon) {
        return new JCommandButton(title, getResizableIconFromResource(icon));
    }

    public AbstractRibbonBand[] createBands(AbstractRibbonBand<?>... bands) {
        int numBands = bands.length;
        AbstractRibbonBand[] arr = new AbstractRibbonBand[numBands];
        System.arraycopy(bands, 0, arr, 0, numBands);
        return arr;

        //        ArrayList<AbstractRibbonBand<?>> list = new ArrayList<>();
        //        list.add(bandMode);
        //        list.add(bandEdgeShape);
        //        list.add(bandArrowLabel);
        //        list.add(bandZoom);
        //        list.add(bandSize);
        //        
        //        AbstractRibbonBand[] bands = new AbstractRibbonBand[list.size()];
        //        list.toArray(bands);
    }
}
