/*
 * @(#)CopyAction.java
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
import org.jhotdraw8.util.Resources;
import org.jhotdraw8.app.Project;

/**
 * Copies the selected region and place its contents into the system clipboard.
 * <p>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class CopyAction extends AbstractSelectionAction {

    private static final long serialVersionUID = 1L;

    public static final String ID = "edit.copy";

    /**
     * Creates a new instance which acts on the currently focused component.
     *
     * @param app the application
     */
    public CopyAction(Application app) {
        this(app, null);
    }

    /**
     * Creates a new instance which acts on the specified component.
     *
     * @param app the application
     * @param target The target of the action. Specify empty for the currently
     * focused component.
     */
    public CopyAction(Application app, Node target) {
        super(app, target);
        Resources.getResources("org.jhotdraw8.app.Labels").configureAction(this, ID);
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, EditableComponent c) {
        c.copy();
    }
}