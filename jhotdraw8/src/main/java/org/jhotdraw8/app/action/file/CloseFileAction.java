/* @(#)CloseFileAction.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.app.action.file;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.Labels;
import org.jhotdraw8.app.action.AbstractSaveUnsavedChangesAction;
import org.jhotdraw8.app.DocumentOrientedActivityViewController;

/**
 * Closes the active view after letting the user save unsaved changes.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class CloseFileAction extends AbstractSaveUnsavedChangesAction {

    private static final long serialVersionUID = 1L;

    public static final String ID = "file.close";

    /**
     * Creates a new instance.
     *
     * @param app the application
     * @param view the view
     */
    public CloseFileAction(Application app, DocumentOrientedActivityViewController view) {
        super(app, view);
        Labels.getLabels().configureAction(this, ID);
    }

    public CloseFileAction(Application app) {
        this(app, null);
    }

    @Nonnull
    @Override
    protected CompletionStage<Void> doIt(@Nullable DocumentOrientedActivityViewController view) {
        if (view != null) {
            app.remove(view);
        }
        return CompletableFuture.completedFuture(null);
    }
}
