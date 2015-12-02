/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.draw;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.HandleType;

/**
 * SimpleLayer.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SimpleLayer extends AbstractCompositeFigure implements Layer, StyleableFigure, LockableFigure {

    @Override
    public void reshape(Transform transform) {
        for (Figure child : childrenProperty()) {
            child.reshape(transform);
        }
    }

    @Override
    public void updateNode(RenderContext v, Node n) {
        applyHideableFigureProperties(n);
        ObservableList<Node> group = ((Group) n).getChildren();
        group.clear();
        for (Figure child : childrenProperty()) {
            group.add(v.getNode(child));
        }
    }

    @Override
    public Node createNode(RenderContext drawingView) {
        return new Group();
    }

    /** This method throws an illegal argument exception if the new
     * parent is not an instance of Drawing.
     *
     * @param newValue the desired parent */
    protected void checkNewParent(Figure newValue) {
        if (newValue != null && !(newValue instanceof Drawing)) {
            throw new IllegalArgumentException("A Layer can only be added as a child to a Drawing. Illegal parent: "
                    + newValue);
        }
    }

    /** Layer figures always return false for isSelectable.
     *
     * @return false */
    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public boolean isLayoutable() {
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
    public boolean isSuitableParent(Figure newParent) {
        return (newParent instanceof Drawing);
    }

    /** Layers never create handles. */
    @Override
    public void createHandles(HandleType handleType, DrawingView dv, List<Handle> list) {
        // empty
    }
    
    
}