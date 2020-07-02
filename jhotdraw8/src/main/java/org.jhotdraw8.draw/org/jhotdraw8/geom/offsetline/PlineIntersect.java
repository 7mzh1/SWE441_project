package org.jhotdraw8.geom.offsetline;

import javafx.geometry.Point2D;

/**
 * Represents a non-coincident polyline intersect.
 */
public class PlineIntersect {
    /**
     * Index of the start vertex of the first segment.
     */
    int sIndex1;
    /** Index of the start vertex of the second segment. */
    int sIndex2;
    /** Point of intersection. */
    Point2D pos;

    PlineIntersect() {
    }

    PlineIntersect(int si1, int si2, Point2D p) {
        this.sIndex1 = si1;
        this.sIndex2 = si2;
        this.pos = p;
    }

    @Override
    public String toString() {
        return "PlineIntersect{" +
                "sIndex1=" + sIndex1 +
                ", sIndex2=" + sIndex2 +
                ", pos=" + pos +
                '}';
    }
}
