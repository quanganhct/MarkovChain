package markovchain.ui;


import gallery.Gallery;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
   
    
    public static ResizableIcon getResizableIconFromGallery(String gallery) {
        return getResizableIconFromGallery(gallery, 32, 32);
    }
   
    public static ResizableIcon getResizableIconFromGallery(String gallery, int width, int height) {
        ResizableIcon result = null;
        try {  
            InputStream in = new FileInputStream(gallery);
            result = ImageWrapperResizableIcon.getIcon(in, new Dimension(width, height));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AbstractTaskView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
     // Image
     public static ResizableIcon getResizableIconFromResource(BufferedImage image, int width, int height) {
        return ImageWrapperResizableIcon.getIcon(image, new Dimension(width, height));
    }
    public static ResizableIcon getResizableIconFromGallery(BufferedImage image) {
        return getResizableIconFromResource(image, 32, 32);
    }
    public static ResizableIcon getResizableIconFromGallery(BufferedImage image, int width, int height) {
        return ImageWrapperResizableIcon.getIcon(image, new Dimension(width, height));
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
