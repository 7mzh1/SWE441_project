/*
 * @(#)FigurePropertyChangeEvent.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.collection.Key;
import org.jhotdraw8.event.Event;

/**
 * FigurePropertyChangeEvent.
 *
 * @author Werner Randelshofer
 */
public class FigurePropertyChangeEvent extends Event<Figure> {

    private final static long serialVersionUID = 1L;
    private final Key<?> key;
    private final Object oldValue;
    private final Object newValue;

    public <T> FigurePropertyChangeEvent(@NonNull Figure source, Key<T> key, T oldValue, T newValue) {
        super(source);
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Key<?> getKey() {
        return key;
    }

    @NonNull
    public <T> T getOldValue() {
        @SuppressWarnings("unchecked") T oldValue = (T) this.oldValue;
        return oldValue;
    }

    @NonNull
    public <T> T getNewValue() {
        @SuppressWarnings("unchecked") T newValue = (T) this.newValue;
        return newValue;
    }

}
