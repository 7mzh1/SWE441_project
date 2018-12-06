/* @(#)ApplyAttributesAction.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.undo.*;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;
import static org.jhotdraw.draw.AttributeKeys.*;

/**
 * ApplyAttributesAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class ApplyAttributesAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    private Set<AttributeKey<?>> excludedAttributes = new HashSet<AttributeKey<?>>(
            Arrays.asList(new AttributeKey<?>[]{TRANSFORM, TEXT}));

    /** Creates a new instance. */
    public ApplyAttributesAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil labels = new ResourceBundleUtil(ResourceBundle.getBundle("org.jhotdraw.draw.Labels"));
        labels.configureAction(this, "edit.applyAttributes");
        updateEnabledState();
    }

    /**
     * Set of attributes that is excluded when applying default attributes.
     */
    public void setExcludedAttributes(Set<AttributeKey<?>> a) {
        this.excludedAttributes = new HashSet<AttributeKey<?>>(a);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        applyAttributes();
    }

    @SuppressWarnings("unchecked")
    public void applyAttributes() {
        DrawingEditor editor = getEditor();

        ResourceBundleUtil labels = new ResourceBundleUtil(ResourceBundle.getBundle("org.jhotdraw.draw.Labels"));
        CompositeEdit edit = new CompositeEdit(labels.getString("edit.applyAttributes.text"));
        DrawingView view = getView();
        view.getDrawing().fireUndoableEditHappened(edit);

        for (Figure figure : view.getSelectedFigures()) {
            figure.willChange();
            for (Map.Entry<AttributeKey<?>, Object> entry : editor.getDefaultAttributes().entrySet()) {
                if (!excludedAttributes.contains(entry.getKey())) {
                    figure.set((AttributeKey<Object>)entry.getKey(), entry.getValue());
                }
            }
            figure.changed();
        }
        view.getDrawing().fireUndoableEditHappened(edit);
    }

    public void selectionChanged(FigureSelectionEvent evt) {
        setEnabled(getView().getSelectionCount() == 1);
    }
}
