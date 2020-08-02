/*
 * @(#)TreeNode.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.tree;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.SpliteratorIterable;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a node of a tree structure.
 * <p>
 * A node has zero or one parents, and zero or more children.
 * <p>
 * All nodes in the same tree structure are of the same type {@literal <T>}.
 * <p>
 * A node may support only a restricted set of parent types
 * {@literal <P extends T>}.
 * <p>
 * A node may only support a restricted set of child types
 * {@literal <C extends T>}.
 * <p>
 * The type {@literal <T>} is checked at compile time using a Java type
 * parameter. The types {@literal <P>} and {@literal <C>} are checked at
 * runtime.
 *
 * @param <T> the type of the tree node
 * @author Werner Randelshofer
 */
public interface TreeNode<T extends TreeNode<T>> {

    /**
     * Returns an iterable which can iterate through this figure and all its
     * ancesters up to the root.
     *
     * @return the iterable
     */
    @NonNull
    default Iterable<T> ancestorIterable() {
        @SuppressWarnings("unchecked")
        Iterable<T> i = () -> new TreeNode.AncestorIterator<>((T) this);
        return i;
    }

    /**
     * Returns an iterable which can iterate through this figure and all its
     * descendants in breadth first sequence.
     *
     * @return the iterable
     */
    @NonNull
    default Iterable<T> breadthFirstIterable() {
        return new SpliteratorIterable<>(
                () -> {
                    @SuppressWarnings("unchecked")
                    T t = (T) this;
                    return new TreeBreadthFirstSpliterator<>(TreeNode<T>::getChildren, t, n -> true);
                });
    }

    /**
     * Dumps the figure and its descendants to system.out.
     */
    default void dumpTree() {
        try {
            dumpTree(System.out, 0);
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Dumps the figure and its descendants.
     *
     * @param out   an output stream
     * @param depth the indentation depth
     * @throws java.io.IOException from appendable
     */
    default void dumpTree(@NonNull Appendable out, int depth) throws IOException {
        for (int i = 0; i < depth; i++) {
            out.append('.');
        }
        out.append(toString());
        out.append('\n');
        for (T child : getChildren()) {
            child.dumpTree(out, depth + 1);
        }
    }

    /**
     * Returns the nearest ancestor of the specified type.
     *
     * @param <TT>         The ancestor type
     * @param ancestorType The ancestor type
     * @return Nearest ancestor of type {@literal <T>} or null if no ancestor of
     * this type is present. Returns {@code this} if this object is of type
     * {@literal <T>}.
     */
    @NonNull
    @Nullable
    default <TT> TT getAncestor(@NonNull Class<TT> ancestorType) {
        @SuppressWarnings("unchecked")
        T ancestor = (T) this;
        while (ancestor != null && !ancestorType.isInstance(ancestor)) {
            ancestor = ancestor.getParent();
        }
        @SuppressWarnings("unchecked")
        TT temp = (TT) ancestor;
        return temp;
    }

    /**
     * Gets the child with the specified index from the node.
     *
     * @param index the index
     * @return the child
     */
    default T getChild(int index) {
        return getChildren().get(index);
    }

    /**
     * Sets the child with the specified index from the node.
     *
     * @param index    the index
     * @param newChild the new child
     * @return the old child
     */
    default T setChild(int index, T newChild) {
        return getChildren().set(index, newChild);
    }

    /**
     * Returns the children of the tree node.
     * <p>
     * In order to keep the tree structure consistent, the following rules must
     * be followed:
     * <ul>
     * <li>If a child is added to this list, then it must be removed from its
     * former parent, and this this tree node must be set as the parent of the
     * child.</li>
     * <li>
     * If a child is removed from this tree node, then the parent of the child
     * must be set to null.</li>
     * </ul>
     *
     * @return the children
     */
    @NonNull List<T> getChildren();

    /**
     * Gets the first child.
     *
     * @return The first child. Returns null if the figure has no getChildren.
     */
    @Nullable
    default T getFirstChild() {
        return getChildren().isEmpty() //
                ? null//
                : getChildren().get(getChildren().size() - 1);
    }

    /**
     * Gets the last child.
     *
     * @return The last child. Returns null if the figure has no getChildren.
     */
    @Nullable
    default T getLastChild() {
        return getChildren().isEmpty() ? null : getChildren().get(0);
    }

    /**
     * Returns the parent of the tree node.
     * <p>
     * Note that - by convention - the parent property is changed only by a
     * parent tree node.
     *
     * @return the parent. Returns null if the tree node has no parent.
     */
    @Nullable
    T getParent();

    /**
     * Returns the path to this node.
     *
     * @return path including this node
     */
    @NonNull
    @SuppressWarnings("unchecked")
    default List<T> getPath() {
        LinkedList<T> path = new LinkedList<>();
        for (T node = (T) this; node != null; node = node.getParent()) {
            path.addFirst(node);
        }
        return path;
    }

    /**
     * Returns an iterable which can iterate through this figure and all its
     * descendants in postorder sequence.
     *
     * @return the iterable
     */
    @NonNull
    default Iterable<T> postorderIterable() {
        return new SpliteratorIterable<>(
                () -> {
                    @SuppressWarnings("unchecked") T t = (T) this;
                    return new PostorderSpliterator<>(TreeNode<T>::getChildren, t);
                }
        );
    }

    /**
     * Returns an iterable which can iterate through this figure and all its
     * descendants in depth first sequence.
     *
     * @return the iterable
     */
    @NonNull
    default Iterable<T> depthFirstIterable() {
        return new SpliteratorIterable<>(
                () -> {
                    @SuppressWarnings("unchecked")
                    T t = (T) this;
                    return new TreeDepthFirstSpliterator<>(TreeNode<T>::getChildren, t, n -> true);
                });
    }

    /**
     * Returns an iterable which can iterate through this figure and all its
     * descendants in preorder sequence.
     *
     * @return the iterable
     */
    @NonNull
    default Iterable<T> preorderIterable() {
        return new SpliteratorIterable<>(
                () -> {
                    @SuppressWarnings("unchecked") T t = (T) this;
                    return new PreorderSpliterator<>(TreeNode<T>::getChildren, t);
                }
        );
    }

    /**
     * Returns a spliterator which can iterate through this figure and all its
     * descendants in preorder sequence.
     *
     * @return the iterable
     */
    @NonNull
    default PreorderSpliterator<T> preorderSpliterator() {
        @SuppressWarnings("unchecked") T t = (T) this;
        return new PreorderSpliterator<>(TreeNode<T>::getChildren, t);
    }

    /**
     * @param <T> the type of the tree nodes
     */
    class AncestorIterator<T extends TreeNode<T>> implements Iterator<T> {

        @Nullable
        private T node;

        private AncestorIterator(@Nullable T node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Nullable
        @Override
        public T next() {
            if (node == null) {
                throw new NoSuchElementException();
            }
            T next = node;
            node = node.getParent();
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

}
