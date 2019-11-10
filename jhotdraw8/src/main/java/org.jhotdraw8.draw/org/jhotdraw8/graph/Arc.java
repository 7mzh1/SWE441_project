/*
 * @(#)Arc.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

/**
 * Data record for an arrow with associated data in a directed graph
 * ("arrow record" is abbreviated to "arc").
 * <p>
 * Provides the start and end vertex of the arrow, and the data associated
 * to the arrow.
 * <p>
 * If a vertex has no predecessor, then start is null, and
 * method {@link #isRoot} returns true.
 *
 * @param <V> the vertex type
 * @param <A> the arrow data type
 */
public class Arc<V, A> {
    private final V start;
    private final V end;
    private final A data;

    public Arc(V start, V end, A data) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    public A getData() {
        return data;
    }

    public V getEnd() {
        return end;
    }

    public V getStart() {
        return start;
    }

    /**
     * Returns true if the vertex has no predecessor.
     *
     * @return true if start==null.
     */
    public boolean isRoot() {
        return start == null;
    }
}
