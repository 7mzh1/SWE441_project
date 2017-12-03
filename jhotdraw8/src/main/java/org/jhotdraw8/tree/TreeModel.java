/* @(#)TreeModel.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.tree;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javax.annotation.Nonnull;
import org.jhotdraw8.beans.ObservableMixin;
import org.jhotdraw8.event.Listener;

/**
 * TreeModel.
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @param <N> the node type
 */
public interface TreeModel<N> extends ObservableMixin {
    /**
     * Name of the root property.
     */
    String ROOT_PROPERTY = "root";

    /**
     * List of drawing model listeners.
     *
     * @return a list of drawing model listeners
     */
    CopyOnWriteArrayList<Listener<TreeModelEvent<N>>> getTreeModelListeners();

    /**
     * List of invalidation listeners.
     *
     * @return a list of drawing model listeners
     */
    CopyOnWriteArrayList<InvalidationListener> getInvalidationListeners();

    /**
     * The root of the drawing model.
     *
     * @return the root
     */
    ObjectProperty<N> rootProperty();

    /**
     * Adds a listener for {@code TreeModelEvent<E>}s.
     *
     * @param l the listener
     */
    default void addTreeModelListener(@Nonnull Listener<TreeModelEvent<N>> l) {
        getTreeModelListeners().add(l);
    }

    /**
     * Removes a listener for {@code TreeModelEvent<E>}s.
     *
     * @param l the listener
     */
    default void removeTreeModelListener(@Nonnull Listener<TreeModelEvent<N>> l) {
        getTreeModelListeners().remove(l);
    }

    /**
     * Gets the root of the tree.
     *
     * @return the drawing
     */@Nonnull 
    default N getRoot() {
        return rootProperty().get();
    }

    /**
     * Sets the root of the tree and fires appropriate
     * {@code TreeModelEvent<E>}s.
     *
     * @param root the new root
     */
    default void setRoot(@Nonnull N root) {
        rootProperty().set(root);
    }

    /**
     * Gets the children of the specified node.
     *
     * @param node the node.
     * @return the getChildren.
     */@Nonnull 
    List<N> getChildren(@Nonnull N node);

    /**
     * Gets the child count of the specified figure.
     *
     * @param node the parent.
     * @return the number of getChildren
     */
    int getChildCount(@Nonnull N node);

    /**
     * Gets the child at the given index from the parent.
     *
     * @param parent the parent.
     * @param index the index.
     * @return the child
     */@Nonnull 
    N getChildAt(@Nonnull N parent, int index);

    /**
     * Removes the specified child from its parent and fires appropriate
     * {@code TreeModelEvent<E>}s.
     *
     * @param child the child
     */
    void removeFromParent(@Nonnull N child);

    /**
     * Adds the specified child to a parent and fires appropriate
     * {@code TreeModelEvent<E>}s.
     *
     * @param child the new child
     * @param parent the parent.
     * @param index the index
     */
    void insertChildAt(@Nonnull N child, @Nonnull N parent, int index);

    /**
     * Adds the specified child to a parent and fires appropriate
     * {@code TreeModelEvent<E>}s.
     *
     * @param child the new child
     * @param parent the parent.
     */
    default void addChildTo(@Nonnull N child, @Nonnull N parent) {
        insertChildAt(child, parent, getChildCount(parent));
    }

    /**
     * Fires the specified event.
     *
     * @param event the event
     */
    default void fireTreeModelEvent(@Nonnull TreeModelEvent<N> event) {
       for (Listener<TreeModelEvent<N>> l : getTreeModelListeners()) {
           l.handle(event);
       }
    }

    // ---
    // convenience methods
    // ---
    /**
     * Fires "node invalidated" event for the specified node.
     *
     * @param f the figure
     */
    default void fireNodeInvalidated(@Nonnull N node) {
        fireTreeModelEvent(TreeModelEvent.nodeInvalidated(this, node));
    }
    
    default boolean isLeaf(@Nonnull N node) {
        return false;
    } 
}
