/*
 * @(#)VertexPath.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.graph;

import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.collection.ImmutableList;
import org.jhotdraw8.collection.ImmutableLists;
import org.jhotdraw8.collection.ReadOnlyCollection;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a vertex path through a graph.
 * <p>
 * Path elements are vertices.
 *
 * @author Werner Randelshofer
 */
public class VertexPath<V> {

    @Nonnull
    private final ImmutableList<V> vertices;

    public VertexPath(@Nonnull Collection<V> elements) {
        this.vertices = ImmutableLists.ofCollection(elements);
    }

    public VertexPath(@Nonnull ReadOnlyCollection<V> elements) {
        this.vertices = ImmutableLists.ofCollection(elements);
    }

    @SafeVarargs
    public VertexPath(@Nonnull V... elements) {
        this.vertices = ImmutableLists.of(elements);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VertexPath<?> other = (VertexPath<?>) obj;
        if (!Objects.equals(this.vertices, other.vertices)) {
            return false;
        }
        return true;
    }

    public V getSecondToLastVertex() {
        return vertices.get(vertices.size() - 2);
    }

    @Nonnull
    public ImmutableList<V> getVertices() {
        return vertices;
    }

    public V getFirstVertex() {
        return vertices.get(0);
    }

    public V getSecondVertex() {
        return vertices.get(1);
    }

    public V getLastVertex() {
        return vertices.get(vertices.size() - 1);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.vertices);
        return hash;
    }

    public int indexOf(V v) {
        return vertices.indexOf(v);
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public int numOfVertices() {
        return vertices.size();
    }

    @Nonnull
    @Override
    public String toString() {
        return "VertexPath{" + vertices + '}';
    }

    /**
     * Creates a new VertexPath with the specified vertices.
     *
     * @param <VV>     the vertex type
     * @param vertices the vertices
     * @return the vertex path
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <VV> VertexPath<VV> of(VV... vertices) {
        return new VertexPath<>(vertices);
    }

    public VertexPath<V> joinedPath(VertexPath<V> nextPath) {
        if (isEmpty()) {
            return nextPath;
        }
        return new VertexPath<V>(ImmutableLists.addAll(this.vertices.subList(0, numOfVertices() - 1), nextPath.vertices));
    }

}
