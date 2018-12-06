/* @(#)BringToFrontAction.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw.draw.action;

import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * ToFrontAction.
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class BringToFrontAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final String ID = "edit.bringToFront";

    /** Creates a new instance. */
    public BringToFrontAction(DrawingEditor editor) {
        super(editor);
        ResourceBundleUtil labels =
                new ResourceBundleUtil(ResourceBundle.getBundle("org.jhotdraw.draw.Labels"));
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<Figure>(view.getSelectedFigures());
        bringToFront(view, figures);
        fireUndoableEditHappened(new AbstractUndoableEdit() {
    private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels =
                        new ResourceBundleUtil(ResourceBundle.getBundle("org.jhotdraw.draw.Labels"));
                return labels.getTextProperty(ID);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                BringToFrontAction.bringToFront(view, figures);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                SendToBackAction.sendToBack(view, figures);
            }
        });
    }

    public static void bringToFront(DrawingView view, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        for (Figure figure : drawing.sort(figures)) {
            drawing.bringToFront(figure);
        }
    }
}
