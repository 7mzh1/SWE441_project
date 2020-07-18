package org.jhotdraw8.geom.bezier2biarc;

import javafx.geometry.Point2D;

/**
 * Defines a line in point-slope form: y - y1 = m * (x - x1).
 * <p>
 * Vertical line: m = NaN
 * <p>
 * Horizontal line: m = 0
 */
public class Line {
    /**
     * Slope.
     */
    public final double m;
    /**
     * Point.
     */
    public final Point2D p;

    /**
     * Define a line by two points.
     *
     * @param p1 first point
     * @param p2 second point
     */
    public Line(Point2D p1, Point2D p2) {
        this(p1, slope(p1, p2));
    }


    /**
     * Define a line by a point and slope.
     *
     * @param p point
     * @param m slope
     */
    public Line(Point2D p, double m) {
        this.p = p;
        this.m = m;
    }

    /**
     * Calculate the intersection point of this line and another one.
     *
     * @param l another line
     * @return the intersection point
     */
    public Point2D Intersection(Line l) {
        if (Double.isNaN(this.m)) {
            return verticalIntersection(this, l);
        } else if (Double.isNaN(l.m)) {
            return verticalIntersection(l, this);
        } else {
            var x = (this.m * this.p.getX() - l.m * l.p.getX() - this.p.getY() + l.p.getY()) / (this.m - l.m);
            var y = m * x - m * p.getX() + p.getY();
            return new Point2D(x, y);
        }
    }


    /**
     * Special case, the first one is vertical (we suppose that the other one is not,
     * otherwise they do not cross).
     *
     * @param vl
     * @param l
     * @return
     */
    private static Point2D verticalIntersection(Line vl, Line l) {
        var x = vl.p.getX();
        var y = l.m * (x - l.p.getX()) + l.p.getY();
        return new Point2D(x, y);
    }

    /**
     * Creates a a line which is perpendicular to the line defined by P and P1 and goes through P
     *
     * @param P
     * @param P1
     * @return
     */
    public static Line createPerpendicularAt(Point2D P, Point2D P1) {
        var m = slope(P, P1);

        if (m == 0) {
            return new Line(P, Double.NaN);
        } else if (Double.isNaN(m)) {
            return new Line(P, 0);
        } else {
            return new Line(P, -1f / m);
        }
    }

    private static double slope(Point2D P1, Point2D P2) {
        if (P2.getX() == P1.getX()) {
            return Double.NaN;
        } else {
            return (P2.getY() - P1.getY()) / (P2.getX() - P1.getX());
        }
    }

}