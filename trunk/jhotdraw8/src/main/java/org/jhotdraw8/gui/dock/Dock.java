/* @(#)Dock.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.gui.dock;

import org.jhotdraw8.gui.dock.DockItem;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * A {@code Dock} contains one or more {@link DockItem}s.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public interface Dock {

    ObservableList<DockItem> getItems();

    default Node getNode() {
        return (Node) this;
    }
    
    /** Returns true if the user may add and remove items. */
    boolean isEditable();
}