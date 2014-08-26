package markovchain.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.ribbon.AbstractRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;

import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 *
 * @author Admin
 */
public final class DesignTask extends AbstractTaskView implements ActionListener, ChangeListener, ItemListener {

    private IDesignController controller;
    private JRadioButton btnLine;
    private JRadioButton btnQuadCurve;
    private JRadioButton btnOrthogonal;
    private JRadioButton btnWedge;
    private JRadioButton btnCubicCurve;
    private JCheckBox chkArrowCentered;
    private JCheckBox chkEdgeLabel;
    private JCheckBox chkVetexLabel;
    private JCommandButton btnZoomIn;
    private JCommandButton btnZoomOut;
    private JComboBox cboLabelPositions;
    private JCheckBox chkBoldLabel;
    private JSlider jsVertexSize;
    private JSlider jsEdgeOffset;
    private JComboBox cboLayouts;
    private JCommandButton btnCircle;
//    private JCommandButton btnSimulator;

    public DesignTask(IDesignController controller) {
        this.controller = controller;
        initialize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnZoomIn) {
            controller.zoomIn();
        } else if (src == btnZoomOut) {
            controller.zoomOut();
        } else if (src == btnCircle) {
            controller.setCircleLayout();
        } else if (src instanceof AbstractButton) {
            AbstractButton source = (AbstractButton) e.getSource();
            if (source == btnLine) {
                if (source.isSelected()) {
                    controller.setEdgeShape(new EdgeShape.Line<Vertex, Edge>());
                }
            } else if (source == btnQuadCurve) {
                if (source.isSelected()) {
                    controller
                            .setEdgeShape(new EdgeShape.QuadCurve<Vertex, Edge>());
                }
            } else if (source == btnOrthogonal) {
                if (source.isSelected()) {
                    controller
                            .setEdgeShape(new EdgeShape.Orthogonal<Vertex, Edge>());
                }
            } else if (source == btnWedge) {
                if (source.isSelected()) {
                    controller.setEdgeShape(new EdgeShape.Wedge<Vertex, Edge>(
                            10));
                }
            } else if (source == btnCubicCurve) {
                if (source.isSelected()) {
                    controller.setEdgeShape(new EdgeShape.CubicCurve<Vertex, Edge>());
                }
            } else if (source == chkVetexLabel) {
                controller.setVertexVisible(source.isSelected());
            } else if (source == chkEdgeLabel) {
                controller.setEdgeVisible(source.isSelected());
            } else if (source == chkArrowCentered) {
                controller.setArrowCentered(source.isSelected());
            } else if (source == chkBoldLabel) {
                controller.setBoldlabel(source.isSelected());
            }
            // add more handlers...
        }
        controller.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == jsVertexSize) {
            JSlider s = (JSlider) e.getSource();
            controller.setVertexSize(s.getValue());
        } else if (source == jsEdgeOffset) {
            JSlider s = (JSlider) e.getSource();
            controller.setEdgeOffset(s.getValue());
        }
        // add more handlers...
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source == cboLabelPositions) {
            Renderer.VertexLabel.Position position = (Renderer.VertexLabel.Position) e.getItem();
            controller.setVertexPosition(position);
        }
    }

    private void initialize() {
        // Band Edges
        JRibbonBand bandEdgeShape = createRibbonBand("Edge Shape", "gear-gold.png");

        btnLine = new JRadioButton("Line");
        btnQuadCurve = new JRadioButton("Quad Curve");
        btnOrthogonal = new JRadioButton("Orthogonal");
        btnWedge = new JRadioButton("Wedge");
        btnCubicCurve = new JRadioButton("Cubic Curve");

        // group button
        addToGroup(btnLine, btnQuadCurve, btnOrthogonal, btnWedge, btnCubicCurve);

        bandEdgeShape.addRibbonComponent(createComponent(btnLine));
        bandEdgeShape.addRibbonComponent(createComponent(btnQuadCurve));
        bandEdgeShape.addRibbonComponent(createComponent(btnOrthogonal));
        bandEdgeShape.addRibbonComponent(createComponent(btnWedge));
        bandEdgeShape.addRibbonComponent(createComponent(btnCubicCurve));

        setResizePolicies(bandEdgeShape);

        // Band Edges
        JRibbonBand bandArrowLabel = createRibbonBand("Show/Hide", "list.png");
        chkArrowCentered = new JCheckBox("Centered Arrow");
        chkEdgeLabel = new JCheckBox("Edge Label");
        chkVetexLabel = new JCheckBox("Vertex Label");
        chkBoldLabel = new JCheckBox("Bold Label");


        bandArrowLabel.addRibbonComponent(createComponent(chkEdgeLabel));
        bandArrowLabel.addRibbonComponent(createComponent(chkVetexLabel));
        bandArrowLabel.addRibbonComponent(createComponent(chkArrowCentered));
        bandArrowLabel.addRibbonComponent(createComponent(chkBoldLabel));
        setResizePolicies(bandArrowLabel);

        // Band Zoom
        JRibbonBand bandZoom = createRibbonBand("Zoom", "zoom-in.png");

        btnZoomIn = createCommandButton("In", "zoom-in.png");
        btnZoomOut = createCommandButton("Out", "zoom-out.png");

        bandZoom.addCommandButton(btnZoomIn, RibbonElementPriority.TOP);
        bandZoom.addCommandButton(btnZoomOut, RibbonElementPriority.TOP);

        setResizePolicies(bandZoom);

        // Band Mode
        JRibbonBand bandMode = createRibbonBand("Mode", "mode.png");

        // mouse mode
//        String[] petStrings = {"Picking", "Editing", "Transforming"};
        bandMode.addRibbonComponent(createComponent("Mouse", controller.getModeComboBox()));

        // label position
        // ong them nhieu layout cua jung cho nay nhe
//        String[] layouts = {"Circle", "Other"};
//        cboLayouts = new JComboBox(layouts);
//        bandMode.addRibbonComponent(createComponent("Layout", cboLayouts));


        // label position
        cboLabelPositions = new JComboBox();
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.N);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.NE);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.E);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.SE);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.S);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.SW);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.W);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.NW);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.N);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.CNTR);
        cboLabelPositions.addItem(Renderer.VertexLabel.Position.AUTO);

        cboLabelPositions.setSelectedItem(Renderer.VertexLabel.Position.SE);
//        String[] positions = {"SE", "S", "SW", "W", "NW", "CNTR", "AUTO"};
//        cboLabelPositions = new JComboBox(positions);
        bandMode.addRibbonComponent(createComponent("Label Position", cboLabelPositions));



        // Band Zoom
        JRibbonBand bandLayout = createRibbonBand("Arrange", "circle.png");

        btnCircle = createCommandButton("Circle layout", "circle.png");
        bandLayout.addCommandButton(btnCircle, RibbonElementPriority.TOP);

        setResizePolicies(bandLayout);



        // Band Size
        JRibbonBand bandSize = createRibbonBand("Size / Position", "size-position.png");
        jsVertexSize = new JSlider(10, 50, 30);
        bandSize.addRibbonComponent(createComponent("Vertex", jsVertexSize));

        jsEdgeOffset = new JSlider(50, 150, 50);
        bandSize.addRibbonComponent(createComponent("Edge", jsEdgeOffset));


//        jsVertexSize.setMajorTickSpacing(10);
//        jsVertexSize.setMinorTickSpacing(5);
//        jsVertexSize.setPaintTicks(true);
//
//        jsEdgeOffset.setMajorTickSpacing(10);
//        jsEdgeOffset.setMinorTickSpacing(5);
//        jsEdgeOffset.setPaintTicks(true);
        setResizePolicies(bandSize);

        // Band simulation
//        JRibbonBand bandSimulation = createRibbonBand("Simulation", "simulator.png");
//        btnSimulator = createCommandButton("  Simulator  ", "simulator.png");
//        bandSimulation.addCommandButton(btnSimulator, RibbonElementPriority.TOP);
//        setResizePolicies(bandSimulation);

        // create task
        AbstractRibbonBand[] bands = createBands(bandMode, bandLayout, bandEdgeShape,
                bandArrowLabel, bandZoom, bandSize);
        task = new RibbonTask("Design", bands);

        // default states
        btnQuadCurve.setSelected(true);
        chkVetexLabel.setSelected(true);
        chkEdgeLabel.setSelected(true);

        // bind events
        btnZoomIn.addActionListener(this);
        btnZoomOut.addActionListener(this);
        btnCircle.addActionListener(this);

        jsVertexSize.addChangeListener(this);
        jsEdgeOffset.addChangeListener(this);
        cboLabelPositions.addItemListener(this);

        btnLine.addActionListener(this);
        btnQuadCurve.addActionListener(this);
        btnOrthogonal.addActionListener(this);
        btnWedge.addActionListener(this);
        btnCubicCurve.addActionListener(this);

        chkVetexLabel.addActionListener(this);
        chkEdgeLabel.addActionListener(this);
        chkArrowCentered.addActionListener(this);
        chkBoldLabel.addActionListener(this);

    }
}
