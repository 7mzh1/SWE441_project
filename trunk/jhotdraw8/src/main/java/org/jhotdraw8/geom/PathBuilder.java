/* @(#)PathBuilder.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */

package org.jhotdraw8.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.PathIterator;
import static org.jhotdraw8.geom.BezierPath.C2_MASK;

/**
 * PathBuilder.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public interface PathBuilder {
/** ArcTo.
        * Adds an elliptical arc, defined by two radii, an angle from the x-axis, a
     * flag to choose the large arc or not, a flag to indicate if we increase or
     * decrease the angles and the final point of the arc.
     * <p>
     * As specified in
     * http://www.w3.org/TR/SVG/paths.html#PathDataEllipticalArcCommands
     * <p>
     * The implementation of this method has been derived from Apache Batik
     * class org.apache.batik.ext.awt.geom.ExtendedGeneralPath#computArc
     *
     * @param x0 the start point of the ellipse
     * @param y0 the start point of the ellipse
     * @param rx the x radius of the ellipse
     * @param ry the y radius of the ellipse
     *
     * @param xAxisRotation the angle from the x-axis of the current coordinate
     * system to the x-axis of the ellipse in degrees.
     *
     * @param largeArcFlag the large arc flag. If true the arc spanning less
     * than or equal to 180 degrees is chosen, otherwise the arc spanning
     * greater than 180 degrees is chosen
     *
     * @param sweepFlag the sweep flag. If true the line joining center to arc
     * sweeps through decreasing angles otherwise it sweeps through increasing
     * angles
     *
     * @param x the absolute x coordinate of the final point of the arc.
     * @param y the absolute y coordinate of the final point of the arc.
     */
    default void arcTo(double x0, double y0, double rx, double ry,
            double xAxisRotation,
            double x, double y,
            boolean largeArcFlag, boolean sweepFlag
            ) {

        // Ensure radii are valid
        if (rx == 0 || ry == 0) {
            lineTo(x, y);
            return;
        }


        if (x0 == x && y0 == y) {
            // If the endpoints (x, y) and (x0, y0) are identical, then this
            // is equivalent to omitting the elliptical arc segment entirely.
            return;
        }

        // Compute the half distance between the current and the final point
        double dx2 = (x0 - x) / 2d;
        double dy2 = (y0 - y) / 2d;
        // Convert angle from degrees to radians
        double angle = Math.toRadians(xAxisRotation);
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        //
        // Step 1 : Compute (x1, y1)
        //
        double x1 = (cosAngle * dx2 + sinAngle * dy2);
        double y1 = (-sinAngle * dx2 + cosAngle * dy2);
        // Ensure radii are large enough
        rx = Math.abs(rx);
        ry = Math.abs(ry);
        double Prx = rx * rx;
        double Pry = ry * ry;
        double Px1 = x1 * x1;
        double Py1 = y1 * y1;
        // check that radii are large enough
        double radiiCheck = Px1 / Prx + Py1 / Pry;
        if (radiiCheck > 1) {
            rx = Math.sqrt(radiiCheck) * rx;
            ry = Math.sqrt(radiiCheck) * ry;
            Prx = rx * rx;
            Pry = ry * ry;
        }

        //
        // Step 2 : Compute (cx1, cy1)
        //
        double sign = (largeArcFlag == sweepFlag) ? -1 : 1;
        double sq = ((Prx * Pry) - (Prx * Py1) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1));
        sq = (sq < 0) ? 0 : sq;
        double coef = (sign * Math.sqrt(sq));
        double cx1 = coef * ((rx * y1) / ry);
        double cy1 = coef * -((ry * x1) / rx);

        //
        // Step 3 : Compute (cx, cy) from (cx1, cy1)
        //
        double sx2 = (x0 + x) / 2.0;
        double sy2 = (y0 + y) / 2.0;
        double cx = sx2 + (cosAngle * cx1 - sinAngle * cy1);
        double cy = sy2 + (sinAngle * cx1 + cosAngle * cy1);

        //
        // Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
        //
        double ux = (x1 - cx1) / rx;
        double uy = (y1 - cy1) / ry;
        double vx = (-x1 - cx1) / rx;
        double vy = (-y1 - cy1) / ry;
        double p, n;

        // Compute the angle start
        n = Math.sqrt((ux * ux) + (uy * uy));
        p = ux; // (1 * ux) + (0 * uy)
        sign = (uy < 0) ? -1d : 1d;
        double angleStart = Math.toDegrees(sign * Math.acos(p / n));

        // Compute the angle extent
        n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
        p = ux * vx + uy * vy;
        sign = (ux * vy - uy * vx < 0) ? -1d : 1d;
        double angleExtent = Math.toDegrees(sign * Math.acos(p / n));
        if (!sweepFlag && angleExtent > 0) {
            angleExtent -= 360f;
        } else if (sweepFlag && angleExtent < 0) {
            angleExtent += 360f;
        }
        angleExtent %= 360f;
        angleStart %= 360f;

        //
        // We can now build the resulting Arc2D in double precision
        //
        Arc2D.Double arc = new Arc2D.Double(
                cx - rx, cy - ry,
                rx * 2d, ry * 2d,
                -angleStart, -angleExtent,
                Arc2D.OPEN);

        // Create a path iterator of the rotated arc
        PathIterator i = arc.getPathIterator(
                AffineTransform.getRotateInstance(
                        angle, arc.getCenterX(), arc.getCenterY()));

        // Add the segments to the bezier path
        double[] coords = new double[6];
        i.next(); // skip first moveTo
        while (!i.isDone()) {
            int type = i.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    // ignore
                    break;
                case PathIterator.SEG_CUBICTO:
                    curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_LINETO:
                    lineTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_MOVETO:
                    // ignore
                    break;
                case PathIterator.SEG_QUADTO:
                    quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
            }
            i.next();
        }
    }

     void closePath();

    public void curveTo(double x, double y, double x0, double y0, double x1, double y1);

     void lineTo(double x, double y);
 void moveTo(double x,double y);

     void quadTo(double x, double y, double x0, double y0);


    default void smoothQuadTo(double x, double y, double x0, double y0){
        quadTo(x,y,x0,y0);
    }

    default void smoothCurveTo(double x, double y, double x0, double y0, double x1, double y1){
        curveTo(x,y,x0,y0,x1,y1);
    }

}