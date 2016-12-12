/* @(#)LockedFigure.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.figure;

import org.jhotdraw8.collection.BooleanKey;

/**
 * Interface for figures which are always locked.
 *
 * @design.pattern Figure Mixin, Traits.
 * 
 * @author Werner Randelshofer
 */
public interface LockedFigure extends Figure {

 
    /**
     * Whether this figure is not locked and all its parents are editable.
     *
     * @return false
     */
    @Override
    default boolean isEditable() {
            return false;
    }

    /**
     * Whether the figure is not locked and all its parents are editable.
     *
     * @return false
     */
    @Override
    default boolean isDeletable() {
        return false;
    }
    /**
     * Whether the figure is not locked and all its parents are editable.
     *
     * @return false
     */
    @Override
    default boolean isSelectable() {
        return false;
    }
}
