/* @(#)RemoveTransformationsAction.java
 * Copyright (c) 2016 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javafx.event.ActionEvent;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.collection.Key;
import org.jhotdraw8.draw.DrawingEditor;
import org.jhotdraw8.draw.DrawingView;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.draw.figure.TransformableFigure;
import org.jhotdraw8.draw.model.DrawingModel;
import org.jhotdraw8.util.Resources;
import org.jhotdraw8.app.Project;

/**
 * RemoveTransformationsAction.
 *
 * @author Werner Randelshofer
 */
public class RemoveTransformationsAction extends AbstractSelectedAction {

    public static final String ID = "edit.removeTransformations";

    /**
     * Creates a new instance.
     *
     * @param app the application
     * @param editor the drawing editor
     */
    public RemoveTransformationsAction(Application app, DrawingEditor editor) {
        super(app, editor);
        Resources labels
                = Resources.getResources("org.jhotdraw8.draw.Labels");
        labels.configureAction(this, ID);
    }

    @Override
    protected void onActionPerformed(ActionEvent e) {
        final DrawingView view = getView();
        if (view == null) {
            return;
        }
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        removeTransformations(view, figures);

    }

    public static void removeTransformations(DrawingView view, Collection<Figure> figures) {
        List<Key<?>> keys = TransformableFigure.getDeclaredKeys();

        DrawingModel model = view.getModel();
        for (Figure child : figures) {
            if (child instanceof TransformableFigure) {
                for (Key<?> k : keys) {
                    model.remove(child, k);
                }
            }
        }
    }
}
