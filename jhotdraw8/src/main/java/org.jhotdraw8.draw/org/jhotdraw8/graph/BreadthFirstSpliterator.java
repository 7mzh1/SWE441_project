/*
 * @(#)BreadthFirstSpliterator.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.AbstractEnumeratorSpliterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Function;

/**
 * BreadthFirstSpliterator.
 *
 * @param <V> the vertex type
 * @author Werner Randelshofer
 */
public class BreadthFirstSpliterator<V> extends AbstractEnumeratorSpliterator<V> {

    @NonNull
    private final Function<V, Iterable<V>> nextFunction;
    @NonNull
    private final Deque<V> deque;


    /**
     * Creates a new instance.
     *
     * @param nextFunction the nextFunction
     * @param root         the root vertex
     */
    public BreadthFirstSpliterator(@NonNull Function<V, Iterable<V>> nextFunction, @NonNull V root) {
        super(Long.MAX_VALUE, ORDERED | DISTINCT | NONNULL);
        Objects.requireNonNull(nextFunction, "nextFunction is null");
        Objects.requireNonNull(root, "root is null");
        this.nextFunction = nextFunction;
        deque = new ArrayDeque<>(16);
        deque.add(root);
    }

    @Override
    public boolean moveNext() {
        current = deque.pollLast();
        if (current == null) {
            return false;
        }
        for (V next : nextFunction.apply(current)) {
                deque.addLast(next);
        }
        return true;
    }
}
