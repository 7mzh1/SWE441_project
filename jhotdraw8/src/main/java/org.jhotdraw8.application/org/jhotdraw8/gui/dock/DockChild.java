/*
 * @(#)DockChild.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.gui.dock;

import javafx.beans.property.BooleanProperty;
import org.jhotdraw8.annotation.NonNull;

/**
 * Represents a dock item that can be docked to a {@code DockParent}.
 */
public interface DockChild extends DockNode {
    /**
     * The name of the {@link #showingProperty()} ()}.
     */
    String SHOWING_PROPERTY = "showing";

    /**
     * Whether this dock child is showing.
     * <p>
     * A dock child that is not showing should not consume CPU resources.
     *
     * @return true if this dock child is showing.
     */
    @NonNull
    BooleanProperty showingProperty();


    default boolean isShowing() {
        return showingProperty().get();
    }

    default void setShowing(boolean newValue) {
        showingProperty().set(newValue);
    }
}
