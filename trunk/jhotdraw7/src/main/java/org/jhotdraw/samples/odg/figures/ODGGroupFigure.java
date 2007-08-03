/*
 * @(#)ODGGroupFigure.java  1.0  2007-07-28
 *
 * Copyright (c) 1996-2007 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.samples.odg.figures;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.samples.odg.*;
import org.jhotdraw.xml.*;
import static org.jhotdraw.samples.odg.ODGAttributeKeys.*;
/**
 * ODGGroupFigure.
 *
 * @author Werner Randelshofer
 * @version 1.0 2007-07-28 Created.
 */
public class ODGGroupFigure extends GroupFigure implements ODGFigure {
    private HashMap<AttributeKey, Object> attributes = new HashMap<AttributeKey,Object>();
    
    
    /** Creates a new instance. */
    public ODGGroupFigure() {
        ODGAttributeKeys.setDefaults(this);
    }
    
    @Override public void setAttribute(AttributeKey key, Object value) {
        if (key == OPACITY) {
            attributes.put(key, value);
        } else {
            super.setAttribute(key, value);
        }
        invalidate();
    }
    @Override public Object getAttribute(AttributeKey name) {
        return attributes.get(name);
    }
    @Override public Map<AttributeKey,Object> getAttributes() {
        return new HashMap<AttributeKey,Object>(attributes);
    }
    public void setAttributes(Map<AttributeKey, Object> map) {
        for (Map.Entry<AttributeKey, Object> entry : map.entrySet()) {
            setAttribute(entry.getKey(), entry.getValue());
        }
    }
    
    public void draw(Graphics2D g)  {
        double opacity = OPACITY.get(this);
        opacity = Math.min(Math.max(0d, opacity), 1d);
        if (opacity != 0d) {
            if (opacity != 1d) {
                Rectangle2D.Double drawingArea = getDrawingArea();
                
                Rectangle2D clipBounds = g.getClipBounds();
                if (clipBounds != null) {
                    Rectangle2D.intersect(drawingArea, clipBounds, drawingArea);
                }
                
                if (! drawingArea.isEmpty()) {
                    
                    BufferedImage buf = new BufferedImage(
                            (int) ((2 + drawingArea.width) * g.getTransform().getScaleX()),
                            (int) ((2 + drawingArea.height) * g.getTransform().getScaleY()),
                            BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gr = buf.createGraphics();
                    gr.scale(g.getTransform().getScaleX(), g.getTransform().getScaleY());
                    gr.translate((int) -drawingArea.x, (int) -drawingArea.y);
                    gr.setRenderingHints(g.getRenderingHints());
                    super.draw(gr);
                    gr.dispose();
                    Composite savedComposite = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
                    g.drawImage(buf, (int) drawingArea.x, (int) drawingArea.y,
                            2 + (int) drawingArea.width, 2 + (int) drawingArea.height, null);
                    g.setComposite(savedComposite);
                }
            } else {
                super.draw(g);
            }
        }
    }
    
    public Rectangle2D.Double getBounds() {
        if (cachedBounds == null) {
            if (getChildCount() == 0) {
                cachedBounds = new Rectangle2D.Double();
            } else {
                for (Figure f : children) {
                    Rectangle2D.Double bounds = f.getBounds();
                    if (TRANSFORM.get(f) != null) {
                        bounds.setRect(TRANSFORM.get(f).createTransformedShape(bounds).getBounds2D());
                    }
                    if (cachedBounds == null || cachedBounds.isEmpty()) {
                        cachedBounds = bounds;
                    } else {
                        cachedBounds.add(bounds);
                    }
                }
            }
        }
        return (Rectangle2D.Double) cachedBounds.clone();
    }
    
    @Override public LinkedList<Handle> createHandles(int detailLevel) {
        LinkedList<Handle> handles = new LinkedList<Handle>();
        if (detailLevel == 0) {
            TransformHandleKit.addTransformHandles(this, handles);
        }
        return handles;
    }
    
    @Override final public void write(DOMOutput out) throws IOException {
        throw new UnsupportedOperationException("Use ODGStorableOutput to write this Figure.");
    }
    @Override final public void read(DOMInput in) throws IOException {
        throw new UnsupportedOperationException("Use ODGStorableInput to read this Figure.");
    }
    public boolean isEmpty() {
        return getChildCount() == 0;
    }
    
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getClass().getName().substring(getClass().getName().lastIndexOf('.')+1));
        buf.append('@');
        buf.append(hashCode());
        if (getChildCount() > 0) {
            buf.append('(');
            for (Iterator<Figure> i = getChildren().iterator(); i.hasNext(); ) {
                Figure child = i.next();
                buf.append(child);
                if (i.hasNext()) {
                    buf.append(',');
                }
            }
            buf.append(')');
        }
        return buf.toString();
    }
    public ODGGroupFigure clone() {
        ODGGroupFigure that = (ODGGroupFigure) super.clone();
        that.attributes = new HashMap<AttributeKey,Object>(this.attributes);
        return that;
    }
}
