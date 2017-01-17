/*
 * @(#)DeleteAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the 
 * accompanying license terms.
 */
package org.jhotdraw8.app.action.edit;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.EditableComponent;
import org.jhotdraw8.app.action.AbstractFocusOwnerAction;
import org.jhotdraw8.util.Resources;
import org.jhotdraw8.app.Project;

/**
 * Deletes the region at (or after) the caret position.
 * <p>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DeleteAction extends AbstractSelectionAction {

    private static final long serialVersionUID = 1L;
    /**
     * The ID for this action.
     */
    public static final String ID = "edit.delete";

    /**
     * Creates a new instance which acts on the currently focused component.
     *
     * @param app the app
     */
    public DeleteAction(Application app) {
        super(app);
        Resources.getResources("org.jhotdraw8.app.Labels").configureAction(this, ID);
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, EditableComponent c) {
        c.deleteSelection();
    }

}