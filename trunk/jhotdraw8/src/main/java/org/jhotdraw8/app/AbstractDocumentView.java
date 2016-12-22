/* @(#)AbstractDocumentView.java
 * Copyright (c) 2016 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.app;

import java.net.URI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * AbstractDocumentView.
 *
 * @author Werner Randelshofer
 * @version $$Id: AbstractDocumentView.java 1169 2016-12-11 12:51:19Z rawcoder
 * $$
 */
public abstract class AbstractDocumentView extends AbstractProjectView<DocumentView> implements DocumentView {

    protected final BooleanProperty modified = new SimpleBooleanProperty();
    protected final ObjectProperty<URI> uri = new SimpleObjectProperty<>();

    @Override
    public BooleanProperty modifiedProperty() {
        return modified;
    }

    @Override
    public void clearModified() {
        modified.set(false);
    }

    protected void markAsModified() {
        modified.set(true);
    }

    @Override
    public ObjectProperty<URI> uriProperty() {
        return uri;
    }

}