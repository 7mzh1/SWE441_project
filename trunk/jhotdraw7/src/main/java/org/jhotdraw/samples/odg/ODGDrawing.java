/*
 * @(#)ODGDrawing.java  2007-06-24
 *
 * Copyright (c) 2007 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.samples.odg;

import java.io.IOException;
import org.jhotdraw.draw.*;
import org.jhotdraw.samples.svg.figures.*;
import org.jhotdraw.xml.*;
/**
 * ODGDrawing.
 * 
 * @author Werner Randelshofer
 * @version 1.0 2007-06-24 Created.
 */
public class ODGDrawing extends QuadTreeDrawing {
    private String title;
    private String description;
    
    /** Creates a new instance. */
    public ODGDrawing() {
    }
    
    public void setTitle(String newValue) {
        String oldValue = title;
        title = newValue;
        firePropertyChange("title", oldValue, newValue);
    }
    public String getTitle() {
        return title;
    }
    public void setDescription(String newValue) {
        String oldValue = description;
        description = newValue;
        firePropertyChange("description", oldValue, newValue);
    }
    public String getDescription() {
        return description;
    }
    
    
    public void read(DOMInput in) throws IOException {
        for (int i=0, n = in.getElementCount(); i < n; i++) {
            in.openElement(i);
            String name = in.getTagName();
            if (name.equals("title")) {
                title = in.getText();
                in.closeElement();
            } else if (name.equals("desc")) {
                description = in.getText();
                in.closeElement();
            } else if (name.equals("defs")) {
                // We ignore "defs" elements for now.
                in.closeElement();
            } else if (name.equals("use")) {
                // We ignore "use" elements for now.
                in.closeElement();
            } else if (name.equals("script")) {
                // We ignore "script" elements for now.
                in.closeElement();
            } else if (name.equals("style")) {
                // We ignore "style" elements for now.
                in.closeElement();
            } else if (name.equals("radialGradient")) {
                // We ignore "radialGradient" elements for now.
                in.closeElement();
            } else {
                in.closeElement();
                Object f = (Object) in.readObject(i);
                if (f instanceof ODGDrawing) {
                    SVGGroupFigure g = new SVGGroupFigure();
                    g.willChange();
                    for (Figure child : ((ODGDrawing) f).getChildren()) {
                        g.basicAdd(child);
                    }
                    g.changed();
                    if (! g.isEmpty()) {
                        add(g);
                    }
                } else if (f instanceof SVGFigure) {
                    if (!((SVGFigure) f).isEmpty()) {
                        add((Figure) f);
                    }
                } else {
                    throw new IOException("Unexpected child "+f);
                }
            }
        }
        readAttributes(in);
    }
    
    protected void readAttributes(DOMInput in) throws IOException {
        // SVGUtil.readAttributes(this, in);
    }
    
   @Override public void write(DOMOutput out) throws IOException {
        out.addAttribute("xmlns","http://www.w3.org/2000/svg");
        out.addAttribute("version","1.2");
        out.addAttribute("baseProfile","tiny");

       for (Figure f : getChildren()) {
            out.writeObject(f);
        }
    }
}
