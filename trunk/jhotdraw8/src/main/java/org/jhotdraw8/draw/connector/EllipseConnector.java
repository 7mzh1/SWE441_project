/* @(#)EllipseConnector.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */

package org.jhotdraw8.draw.connector;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import org.jhotdraw8.draw.figure.Figure;
import static org.jhotdraw8.draw.figure.StrokeableFigure.STROKE_COLOR;
import static org.jhotdraw8.draw.figure.StrokeableFigure.STROKE_TYPE;
import static org.jhotdraw8.draw.figure.StrokeableFigure.STROKE_WIDTH;
import org.jhotdraw8.draw.locator.Locator;
import org.jhotdraw8.draw.locator.RelativeLocator;
import org.jhotdraw8.geom.Geom;
import org.jhotdraw8.geom.Intersection;

/**
 * EllipseConnector.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class EllipseConnector extends LocatorConnector {

    public EllipseConnector() {
        super(new RelativeLocator(0.5,0.5));
    }
public EllipseConnector(Locator locator) {
        super(locator);
    }



    @Override
    public
Double intersect(Figure connection, Figure target, Point2D start, Point2D end) {
Point2D s=        target.worldToLocal(start);
Point2D e=        target.worldToLocal(end);
Bounds bounds=        target.getBoundsInLocal();

     // FIXME does not take line join into account
        if (target.getStyled(STROKE_COLOR) != null) {
            double grow;
            switch (target.getStyled(STROKE_TYPE)) {
                case CENTERED:
                default:
                    grow = target.getStyled(STROKE_WIDTH) / 2d;
                    break;
                case OUTSIDE:
                    grow = target.getStyled(STROKE_WIDTH);
                    break;
                case INSIDE:
                    grow = 0d;
                    break;
            }
           bounds = Geom.grow(bounds, grow, grow);
        }

Intersection i=Intersection.intersectLineEllipse(s, e, bounds);
double maxT=0;
for (double t:i.getTs()) if (t>maxT)maxT=t;
return i.isEmpty()?null:maxT;
    }
}
