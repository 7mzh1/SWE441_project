/*
 * @(#)LineFigure.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.css.CssRectangle2D;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.draw.handle.Handle;
import org.jhotdraw8.draw.handle.HandleType;
import org.jhotdraw8.draw.handle.LineOutlineHandle;
import org.jhotdraw8.draw.handle.MoveHandle;
import org.jhotdraw8.draw.handle.PointHandle;
import org.jhotdraw8.draw.key.CssPoint2DStyleableMapAccessor;
import org.jhotdraw8.draw.key.CssSizeStyleableKey;
import org.jhotdraw8.draw.locator.PointLocator;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.geom.FXTransforms;
import org.jhotdraw8.geom.Shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.List;

/**
 * A figure which draws a straight line from a start point to an end point.
 *
 * @author Werner Randelshofer
 */
public class LineFigure extends AbstractLeafFigure
        implements StrokableFigure, HideableFigure, StyleableFigure, LockableFigure,
        CompositableFigure, TransformableFigure, PathIterableFigure {

    /**
     * The CSS type selector for this object is {@value #TYPE_SELECTOR}.
     */
    public final static String TYPE_SELECTOR = "Line";

    @Nullable
    public final static CssSizeStyleableKey START_X = new CssSizeStyleableKey("startX", CssSize.ZERO);
    @Nullable
    public final static CssSizeStyleableKey START_Y = new CssSizeStyleableKey("startY", CssSize.ZERO);
    @Nullable
    public final static CssSizeStyleableKey END_X = new CssSizeStyleableKey("endX", CssSize.ZERO);
    @Nullable
    public final static CssSizeStyleableKey END_Y = new CssSizeStyleableKey("endY", CssSize.ZERO);
    @Nullable
    public final static CssPoint2DStyleableMapAccessor START = new CssPoint2DStyleableMapAccessor("start", START_X, START_Y);
    @Nullable
    public final static CssPoint2DStyleableMapAccessor END = new CssPoint2DStyleableMapAccessor("end", END_X, END_Y);

    public LineFigure() {
        this(0, 0, 1, 1);
    }

    public LineFigure(double startX, double startY, double endX, double endY) {
        set(START, new CssPoint2D(startX, startY));
        set(END, new CssPoint2D(endX, endY));
    }

    public LineFigure(@NonNull Point2D start, @NonNull Point2D end) {
        set(START, new CssPoint2D(start));
        set(END, new CssPoint2D(end));
    }

    @NonNull
    @Override
    public Bounds getLayoutBounds() {
        return getCssLayoutBounds().getConvertedBoundsValue();
    }

    @NonNull
    @Override
    public CssRectangle2D getCssLayoutBounds() {
        CssPoint2D start = getNonNull(START);
        CssPoint2D end = getNonNull(END);
        return new CssRectangle2D(start, end);
    }

    @Override
    public PathIterator getPathIterator(RenderContext ctx, AffineTransform tx) {
        return Shapes.awtShapeFromFX(new Line(getNonNull(START_X).getConvertedValue(),
                getNonNull(START_Y).getConvertedValue(),
                getNonNull(END_X).getConvertedValue(),
                getNonNull(END_Y).getConvertedValue())).getPathIterator(tx);
    }

    @Override
    public void reshapeInLocal(@NonNull Transform transform) {
        set(START, new CssPoint2D(transform.transform(getNonNull(START).getConvertedValue())));
        set(END, new CssPoint2D(transform.transform(getNonNull(END).getConvertedValue())));
    }

    @Override
    public void reshapeInLocal(@NonNull CssSize x, @NonNull CssSize y, @NonNull CssSize width, @NonNull CssSize height) {
        reshapeInLocal(x.getConvertedValue(), y.getConvertedValue(), width.getConvertedValue(), height.getConvertedValue());
    }

    @Override
    public void reshapeInLocal(double x, double y, double width, double height) {
        reshapeInLocal(FXTransforms.createReshapeTransform(getLayoutBounds(), x, y, width, height));
    }

    @NonNull
    @Override
    public Node createNode(RenderContext drawingView) {
        return new Line();
    }

    @Override
    public void updateNode(@NonNull RenderContext ctx, @NonNull Node node) {
        Line lineNode = (Line) node;
        applyHideableFigureProperties(ctx, node);
        applyStyleableFigureProperties(ctx, node);
        applyStrokableFigureProperties(ctx, lineNode);
        applyTransformableFigureProperties(ctx, node);
        applyCompositableFigureProperties(ctx, lineNode);
        Point2D start = getStyledNonNull(START).getConvertedValue();
        lineNode.setStartX(start.getX());
        lineNode.setStartY(start.getY());
        Point2D end = getStyledNonNull(END).getConvertedValue();
        lineNode.setEndX(end.getX());
        lineNode.setEndY(end.getY());
        lineNode.applyCss();
    }

    @Override
    public void createHandles(HandleType handleType, @NonNull List<Handle> list) {
        if (handleType == HandleType.SELECT) {
            list.add(new LineOutlineHandle(this));
        } else if (handleType == HandleType.MOVE) {
            list.add(new LineOutlineHandle(this));
            list.add(new MoveHandle(this, new PointLocator(START)));
            list.add(new MoveHandle(this, new PointLocator(END)));
        } else if (handleType == HandleType.RESIZE) {
            list.add(new LineOutlineHandle(this));
            list.add(new PointHandle(this, START));
            list.add(new PointHandle(this, END));
        } else if (handleType == HandleType.POINT) {
            list.add(new LineOutlineHandle(this));
            list.add(new PointHandle(this, START));
            list.add(new PointHandle(this, END));
        } else {
            super.createHandles(handleType, list);
        }
    }

    @NonNull
    @Override
    public String getTypeSelector() {
        return TYPE_SELECTOR;
    }

    @Override
    public void layout(@NonNull RenderContext ctx) {
        // empty
    }

    @Override
    public boolean isLayoutable() {
        return false;
    }

}
