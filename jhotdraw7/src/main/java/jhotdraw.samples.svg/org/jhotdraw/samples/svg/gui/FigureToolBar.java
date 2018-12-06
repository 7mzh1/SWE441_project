/* @(#)FigureToolBar.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.event.SelectionComponentRepainter;
import org.jhotdraw.draw.gui.JAttributeSlider;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.gui.JPopupButton;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;
import org.jhotdraw.samples.svg.SVGLabels;
import org.jhotdraw.text.JavaNumberFormatter;
import org.jhotdraw.util.Images;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.annotation.Nullable;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.SliderUI;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;

import static org.jhotdraw.samples.svg.SVGAttributeKeys.FILL_COLOR;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.OPACITY;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.STROKE_COLOR;

/**
 * FigureToolBar.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FigureToolBar extends AbstractToolBar {
    private static final long serialVersionUID = 1L;

    @Nullable private SelectionComponentDisplayer displayer;
    private ResourceBundleUtil labels;

    /** Creates new instance. */
    public FigureToolBar() {
        labels = SVGLabels.getLabels();
        setName(labels.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this);
        }
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel p = null;

        switch (state) {
            case 1: {
                p = new JPanel();
                p.setOpaque(false);
                p.setLayout(new GridBagLayout());
                p.setBorder(new EmptyBorder(5, 5, 5, 8));

                // Abort if no editor is set
                if (editor == null) {
                    break;
                }

                GridBagConstraints gbc;
                AbstractButton btn;

                // Opacity slider
                JPopupButton opacityPopupButton = new JPopupButton();
                JAttributeSlider opacitySlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.figureOpacity");
                opacityPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(opacityPopupButton));
                opacityPopupButton.setIcon(
                        new SelectionOpacityIcon(editor, OPACITY, FILL_COLOR, STROKE_COLOR, Images.createImage(getClass(), labels.getString("attribute.figureOpacity.largeIcon")),
                        new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(SOUTH_EAST);
                disposables.add(new SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 0, 0, 0);
                gbc.weighty = 1;
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI((SliderUI) PaletteSliderUI.createUI(opacitySlider));
                opacitySlider.setScaleFactor(100d);
                disposables.add(new FigureAttributeEditorHandler<Double>(OPACITY, opacitySlider, editor));
            }
            break;

            case 2: {
                p = new JPanel();
                p.setOpaque(false);
                p.setLayout(new GridBagLayout());
                p.setBorder(new EmptyBorder(5, 5, 5, 8));

                // Abort if no editor is set
                if (editor == null) {
                    break;
                }

                GridBagConstraints gbc;
                AbstractButton btn;

                // Opacity field with slider
                JAttributeTextField<Double> opacityField = new JAttributeTextField<Double>();
                opacityField.setColumns(4);
                opacityField.setToolTipText(labels.getString("attribute.figureOpacity.toolTipText"));
                opacityField.setHorizontalAlignment(JAttributeTextField.RIGHT);
                opacityField.putClientProperty("Palette.Component.segmentPosition", "first");
                opacityField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(opacityField));
                JavaNumberFormatter formatter = new JavaNumberFormatter(0d, 100d, 100d, false, "%");
                formatter.setUsesScientificNotation(false);
                formatter.setMaximumFractionDigits(1);
                opacityField.setFormatterFactory(new DefaultFormatterFactory(formatter));
                opacityField.setHorizontalAlignment(JTextField.LEADING);
                disposables.add(new FigureAttributeEditorHandler<Double>(OPACITY, opacityField, editor));
                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 0, 0, 0);
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.weightx = 1d;
                p.add(opacityField, gbc);
                JPopupButton opacityPopupButton = new JPopupButton();
                JAttributeSlider opacitySlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);
                opacityPopupButton.add(opacitySlider);
                labels.configureToolBarButton(opacityPopupButton, "attribute.figureOpacity");
                opacityPopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(opacityPopupButton));
                opacityPopupButton.setIcon(
                        new SelectionOpacityIcon(editor, OPACITY, FILL_COLOR, STROKE_COLOR, Images.createImage(getClass(), labels.getString("attribute.figureOpacity.largeIcon")),
                        new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)));
                opacityPopupButton.setPopupAnchor(SOUTH_EAST);
                disposables.add(new SelectionComponentRepainter(editor, opacityPopupButton));
                gbc = new GridBagConstraints();
                gbc.gridx = 2;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.weighty = 1;
                gbc.insets = new Insets(0, 0, 0, 0);
                p.add(opacityPopupButton, gbc);
                opacitySlider.setUI((SliderUI) PaletteSliderUI.createUI(opacitySlider));
                opacitySlider.setScaleFactor(100d);
                disposables.add(new FigureAttributeEditorHandler<Double>(OPACITY, opacitySlider, editor));
            }
            break;
        }
        return p;
    }

    @Override
    protected String getID() {
        return "figure";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
