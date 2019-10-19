/*
 * @(#)RevertFileAction.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.app.action.file;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DataFormat;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.app.Application;
import org.jhotdraw8.app.ApplicationLabels;
import org.jhotdraw8.app.DocumentBasedActivity;
import org.jhotdraw8.app.action.AbstractActivityAction;
import org.jhotdraw8.concurrent.SimpleWorkState;
import org.jhotdraw8.concurrent.WorkState;

import java.net.URI;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Lets the user write unsaved changes of the active view, then presents an
 * {@code URIChooser} and loads the selected URI into the active view.
 * <p>
 *
 * @author Werner Randelshofer
 */
public class RevertFileAction extends AbstractActivityAction<DocumentBasedActivity> {

    private static final long serialVersionUID = 1L;

    public static final String ID = "file.revert";

    /**
     * Creates a new instance.
     *
     * @param app  the application
     * @param view the view
     */
    public RevertFileAction(Application app, DocumentBasedActivity view) {
        super(app, view, DocumentBasedActivity.class);
        ApplicationLabels.getResources().configureAction(this, ID);
    }

    @Override
    protected void handleActionPerformed(ActionEvent event, @Nonnull DocumentBasedActivity activity) {
        if (isDisabled()) {
            return;
        }
        final URI uri = activity.getURI();
        final DataFormat dataFormat = activity.getDataFormat();
        if (activity.isModified()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    ApplicationLabels.getResources().getString("file.revert.doYouWantToRevert.message"), ButtonType.YES, ButtonType.CANCEL);
            alert.getDialogPane().setMaxWidth(640.0);
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                doIt(activity, uri, dataFormat);
            }
        } else {
            doIt(activity, uri, dataFormat);
        }
    }

    private void doIt(DocumentBasedActivity view, @Nullable URI uri, DataFormat dataFormat) {
        WorkState workState = new SimpleWorkState(getLabel());
        view.addDisabler(workState);

        final BiFunction<DataFormat, Throwable, Void> handler = (actualDataFormat, throwable) -> {
            if (throwable != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, createErrorMessage(throwable));
                alert.getDialogPane().setMaxWidth(640.0);
                alert.showAndWait();
                throwable.printStackTrace();
            }
            view.clearModified();
            view.removeDisabler(workState);
            return null;
        };

        if (uri == null) {
            view.clear().handle((ignored, throwable) -> handler.apply(null, throwable));
        } else {
            view.read(uri, dataFormat, null, false, workState).handle(handler);
        }
    }

}
