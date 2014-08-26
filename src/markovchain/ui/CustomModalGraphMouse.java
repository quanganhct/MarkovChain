package markovchain.ui;


/**
 *
 */
import java.awt.ItemSelectable;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.AnimatedPickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.RotatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ShearingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;

/**
 * @author vudn
 *
 */
public class CustomModalGraphMouse<V, E> extends AbstractModalGraphMouse implements ModalGraphMouse, ItemSelectable {

    protected Factory<V> vertexFactory;
    protected Factory<E> edgeFactory;
    protected CustomMousePlugin<V, E> editingPlugin;
    protected CustomEditingPopupMousePlugin<V, E> popupPlugin;
    protected MultiLayerTransformer basicTransformer;
    protected RenderContext<V, E> rc;

    protected CustomModalGraphMouse(RenderContext<V, E> rc, Factory<V> vertexFactory, Factory<E> edgeFactory) {
        super(1.1f, 1 / 1.1f);
        // TODO Auto-generated constructor stub
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
        this.rc = rc;
        this.basicTransformer = rc.getMultiLayerTransformer();
        loadPlugins();
    }

    /* (non-Javadoc)
     * @see edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse#loadPlugins()
     */
    @Override
    protected void loadPlugins() {
        // TODO Auto-generated method stub
        editingPlugin = new CustomMousePlugin<>(vertexFactory, edgeFactory);
        translatingPlugin = new TranslatingGraphMousePlugin(InputEvent.BUTTON1_MASK);
        scalingPlugin = new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, in, out);
        rotatingPlugin = new RotatingGraphMousePlugin();
        shearingPlugin = new ShearingGraphMousePlugin();
        pickingPlugin = new PickingGraphMousePlugin<V,E>();
		animatedPickingPlugin = new AnimatedPickingGraphMousePlugin<V,E>();
		popupPlugin = new CustomEditingPopupMousePlugin<>(vertexFactory, edgeFactory);
        setMode(Mode.EDITING);
    }

    @Override
    public void setMode(Mode mode) {
        if (this.mode != mode) {
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                    this.mode, ItemEvent.DESELECTED));
            this.mode = mode;
            if (mode == Mode.TRANSFORMING) {
                setTransformingMode();
            } else if (mode == Mode.EDITING) {
                setEditingMode();
            } else if (mode == Mode.PICKING) {
            	setPickingMode();
            }
            
            if (modeBox != null) {
                modeBox.setSelectedItem(mode);
            }
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, mode, ItemEvent.SELECTED));
        }
    }

    protected void setEditingMode() {
        // TODO Auto-generated method stub
        remove(translatingPlugin);
        remove(scalingPlugin);
        remove(rotatingPlugin);
        remove(shearingPlugin);
        remove(pickingPlugin);
        remove(animatedPickingPlugin);
        add(editingPlugin);
        add(popupPlugin);
        rc.getPickedVertexState().clear();
        rc.getPickedEdgeState().clear();
    }

    @Override
    protected void setTransformingMode() {
        // TODO Auto-generated method stub
        super.setTransformingMode();
        remove(editingPlugin);
        remove(popupPlugin);
        rc.getPickedVertexState().clear();
        rc.getPickedEdgeState().clear();
    }
    
    @Override
    protected void setPickingMode() {
    	// TODO Auto-generated method stub
    	super.setPickingMode();
    	remove(editingPlugin);
    }

    @Override
    public JComboBox getModeComboBox() {
        if (modeBox == null) {
            modeBox = new JComboBox(new Mode[]{Mode.EDITING, Mode.TRANSFORMING, Mode.PICKING});
            modeBox.addItemListener(getModeListener());
        }
        modeBox.setSelectedItem(mode);
        return modeBox;
    }
}
