/* @(#)CustomSkin.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.gui;

import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;

/**
 * A custom skin without behavior.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class CustomSkin<C extends Control> extends SkinBase<C> {

    public CustomSkin(C control) {
        super(control);
    }

}
