/* @(#)AbstractTreeModel.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.tree;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jhotdraw8.tree.TreeModel;
import org.jhotdraw8.tree.TreeModelEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.beans.InvalidationListener;
import org.jhotdraw8.event.Listener;

/**
 * AbstractTreeModel.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractTreeModel<E> implements TreeModel<E> {

    private final CopyOnWriteArrayList<Listener<TreeModelEvent<E>>> treeModelListeners = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<InvalidationListener> invalidationListeners = new CopyOnWriteArrayList<>();

    @NonNull
    @Override
    final public CopyOnWriteArrayList<Listener<TreeModelEvent<E>>> getTreeModelListeners() {
        return treeModelListeners;
    }

    @NonNull
    @Override
    final public CopyOnWriteArrayList<InvalidationListener> getInvalidationListeners() {
        return invalidationListeners;
    }
}
