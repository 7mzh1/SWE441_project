/* @(#)PertFactory.java
 * Copyright © 1996-2017 The authors and contributors of JHotDraw.
 * MIT License, CC-by License, or LGPL License.
 */

package org.jhotdraw.samples.net;

import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.draw.connector.LocatorConnector;
import org.jhotdraw.draw.connector.ChopRectangleConnector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.geom.*;
import org.jhotdraw.samples.net.figures.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.xml.*;
/**
 * NetFactory.
 * 
 * @author Werner Randelshofer
 * @version $Id$
 */
public class NetFactory extends DefaultDOMFactory {
    private static final Object[][] classTagArray = {
        { DefaultDrawing.class, "Net" },
        { NodeFigure.class, "node" },
        { LineConnectionFigure.class, "link" },
        { GroupFigure.class, "g" },
        { GroupFigure.class, "g" },
        { TextAreaFigure.class, "ta" },
        
        { LocatorConnector.class, "locConnect" },
        { ChopRectangleConnector.class, "rectConnect" },
        { ArrowTip.class, "arrowTip" },
        { Insets2D.Double.class, "insets" },
        { RelativeLocator.class, "relativeLoc" },
        };
    private static final Object[][] enumTagArray = {
        { AttributeKeys.StrokeType.class, "strokeType" },
    };
    
    /** Creates a new instance. */
    public NetFactory() {
        for (Object[] o : classTagArray) {
            addStorableClass((String) o[1], (Class) o[0]);
        }
        for (Object[] o : enumTagArray) {
            addEnumClass((String) o[1], (Class) o[0]);
        }
    }
}