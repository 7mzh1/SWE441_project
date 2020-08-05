/*
 * @(#)AbstractDockRoot.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.gui.dock;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.jhotdraw8.annotation.NonNull;

import java.util.function.Predicate;

/**
 * Abstract base class for implementations of {@link DockRoot}.
 */
public abstract class AbstractDockRoot
        extends AbstractDockParent
        implements DockRoot {

    @NonNull
    private final ObjectProperty<Predicate<DraggableDockChild>> dockablePredicate = new SimpleObjectProperty<>(d -> true);

    @Override
    public @NonNull ObjectProperty<Predicate<DraggableDockChild>> dockablePredicateProperty() {
        return dockablePredicate;
    }
}
