/* @(#)Disposable.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw.
 * MIT License, CC-by License, or LGPL License.
 */

package org.jhotdraw.app;

import javax.annotation.Nonnull;

/**
 * Interface for objects which explicitly must be disposed to free resources.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface Disposable {
    /** Disposes of all resources held by this object so that they can be
     * garbage collected.
     */
    public void dispose();
}