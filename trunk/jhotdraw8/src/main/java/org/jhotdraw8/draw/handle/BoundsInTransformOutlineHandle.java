/* @(#)BoundsInLocalOutlineHandle.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.draw.handle;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import org.jhotdraw8.draw.DrawingView;
import org.jhotdraw8.draw.figure.Figure;
import org.jhotdraw8.draw.figure.TransformableFigure;
import static org.jhotdraw8.draw.figure.TransformableFigure.TRANSLATE_X;
import static org.jhotdraw8.draw.figure.TransformableFigure.TRANSLATE_Y;
import org.jhotdraw8.geom.Geom;
import org.jhotdraw8.geom.Transforms;

/**
 * Draws the {@code boundsInLocal} of a {@code Figure}, but does not provide any
 * interactions.
 *
 * @author Werner Randelshofer
 * @version $Id: BoundsInLocalOutlineHandle.java 1120 2016-01-15 17:37:49Z
 * rawcoder $
 */
public class BoundsInTransformOutlineHandle extends AbstractHandle {

    private Polygon node;
    private double[] points;
    private String styleclass;

    public BoundsInTransformOutlineHandle(Figure figure) {
        this(figure, STYLECLASS_HANDLE_SELECT_OUTLINE);
    }

    public BoundsInTransformOutlineHandle(Figure figure, String styleclass) {
        super(figure);

        points = new double[8];
        node = new Polygon(points);
        this.styleclass = styleclass;
        initNode(node);
    }

    protected void initNode(Polygon r) {
        r.setFill(null);
        r.setStroke(Color.BLUE);
        r.getStyleClass().setAll(styleclass,STYLECLASS_HANDLE);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void updateNode(DrawingView view) {
        Figure f = getOwner();
        Transform t = Transforms.concat(view.getWorldToView(),f.getParentToWorld());
        if (f instanceof TransformableFigure) {
            TransformableFigure tf = (TransformableFigure) f;
            t = Transforms.concat(t,new Translate(tf.get(TRANSLATE_X), tf.get(TRANSLATE_Y)));
        }
        t = Transforms.concat(Transform.translate(0.5, 0.5),t);
        Bounds b = f.getBoundsInLocal();
        points[0] = b.getMinX();
        points[1] = b.getMinY();
        points[2] = b.getMaxX();
        points[3] = b.getMinY();
        points[4] = b.getMaxX();
        points[5] = b.getMaxY();
        points[6] = b.getMinX();
        points[7] = b.getMaxY();
        if (t!=null&&t.isType2D()) {
            t.transform2DPoints(points, 0, points, 0, 4);
        }

        ObservableList<Double> pp = node.getPoints();
        for (int i = 0; i < points.length; i++) {
            pp.set(i, points[i]);
        }

        node.getStrokeDashArray().setAll(2.0);
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }

    @Override
    public boolean contains(double x, double y, double tolerance) {
        return false;
    }


}
