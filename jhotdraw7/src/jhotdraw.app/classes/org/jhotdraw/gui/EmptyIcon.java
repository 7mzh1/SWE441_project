/* @(#)EmptyIcon.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw.
 * MIT License, CC-by License, or LGPL License.
 */

package org.jhotdraw.gui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * EmptyIcon.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class EmptyIcon implements Icon {
    private int width;
    private int height;
    
    public EmptyIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

}