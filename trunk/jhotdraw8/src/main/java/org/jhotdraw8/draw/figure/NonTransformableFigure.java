/* @(#)NonTransformableFigure.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.figure;

import javafx.scene.transform.Transform;
import org.jhotdraw8.geom.Transforms;

/**
 * Provides default implementations for figures which can not be transformed.
 *
 * @design.pattern Figure Mixin, Traits.
 *
 * @author Werner Randelshofer
 * @version $Id: NonTransformableFigure.java 1206 2016-12-17 12:42:21Z rawcoder
 * $
 */
public interface NonTransformableFigure extends TransformCacheableFigure {

    @Override
    default void transformInParent(Transform transform) {
        // empty because non-transformable figures can not be transformed
    }

    @Override
    default void transformInLocal(Transform transform) {
        // empty because non-transformable figures can not be transformed
    }

    @Override
    default void reshapeInParent(Transform transform) {
        reshapeInLocal(Transforms.concat(getParentToLocal(),transform));
    }

    @Override
    default Transform getLocalToParent() {
        return FigureImplementationDetails.IDENTITY_TRANSFORM;
    }

    @Override
    default Transform getParentToLocal() {
        return FigureImplementationDetails.IDENTITY_TRANSFORM;
    }
}