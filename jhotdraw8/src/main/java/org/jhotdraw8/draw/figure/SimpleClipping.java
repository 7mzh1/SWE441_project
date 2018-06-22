/* @(#)SimpleClipping.java
 * Copyright © 2017 by the authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.draw.handle.Handle;
import org.jhotdraw8.draw.handle.HandleType;

/**
 * SimpleClipping.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SimpleClipping extends AbstractCompositeFigure
        implements Clipping, StyleableFigure, LockedFigure, NonTransformableFigure {

    public SimpleClipping() {
    }

    public SimpleClipping(@NonNull Collection<Figure> children) {
        getChildren().addAll(children);
    }

    @Override
    public void reshapeInLocal(Transform transform) {
        for (Figure child : getChildren()) {
            child.reshapeInLocal(transform);
        }
    }

    @Override
    public void updateNode(@NonNull RenderContext ctx, @NonNull Node n) {
        applyStyleableFigureProperties(ctx, n);

        List<Node> nodes = new ArrayList<Node>(getChildren().size());
        for (Figure child : getChildren()) {
            nodes.add(ctx.getNode(child));
        }
        ObservableList<Node> group = ((Group) n).getChildren();
        if (!group.equals(nodes)) {
            group.setAll(nodes);
        }
    }

    @NonNull
    @Override
    public Node createNode(RenderContext ctx) {
        Group n = new Group();
        return n;
    }

    /**
     * Layer figures always return false for isSelectable.
     *
     * @return false
     */
    @Override
    public boolean isSelectable() {
        return false;
    }

    /**
     * This method whether the provided figure is a suitable parent for this
     * figure.
     * <p>
     * This implementation returns true if {@code newParent} is a
     * {@link Drawing}.
     *
     * @param newParent The new parent figure.
     * @return true if {@code newParent} is an acceptable parent
     */
    @Override
    public boolean isSuitableParent(@Nullable Figure newParent) {
        return newParent == null;
    }

    /**
     * Layers never create handles.
     */
    @Override
    public void createHandles(HandleType handleType, List<Handle> list) {
        // empty
    }

}
