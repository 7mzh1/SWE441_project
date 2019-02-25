/* @(#)LineConnectionWithMarkersFigure.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.draw.figure;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.jhotdraw8.annotation.Nonnull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssPoint2D;
import org.jhotdraw8.draw.connector.Connector;
import org.jhotdraw8.draw.render.RenderContext;
import org.jhotdraw8.geom.Shapes;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/**
 * AbstractStraightLineConnectionWithMarkersFigure draws a straight line from start to end.
 * <p>
 * A subclass can hardcode the markers, or can implement one or multiple "markerable" interfaces
 * that allow user-defineable markers: {@link MarkerStartableFigure}, {@link MarkerEndableFigure},
 * {@link MarkerSegmentableFigure}.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractStraightLineConnectionWithMarkersFigure extends AbstractLineConnectionFigure
        implements PathIterableFigure {

    public AbstractStraightLineConnectionWithMarkersFigure() {
        this(0, 0, 1, 1);
    }

    public AbstractStraightLineConnectionWithMarkersFigure(Point2D start, Point2D end) {
        this(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public AbstractStraightLineConnectionWithMarkersFigure(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    @Nonnull
    @Override
    public Node createNode(RenderContext drawingView) {
        javafx.scene.Group g = new javafx.scene.Group();
        final Line line = new Line();
        final Path startMarker=new Path();
        final Path endMarker=new Path();
        g.getChildren().addAll(line,startMarker,endMarker);
        return g;
    }

    /**
     * This method can be overridden by a subclass to apply styles to the line
     * node.
     *
     * @param ctx the context
     * @param node the node
     */
    protected void updateLineNode(RenderContext ctx, Line node) {

    }

    /**
     * This method can be overridden by a subclass to apply styles to the marker
     * node.
     *
     * @param ctx the context
     * @param node the node
     */
    protected void updateStartMarkerNode(RenderContext ctx, Path node) {
            // empty
    }

    /**
     * This method can be overridden by a subclass to apply styles to the marker
     * node.
     *
     * @param ctx the context
     * @param node the node
     */
    protected void updateEndMarkerNode(RenderContext ctx, Path node) {
            // empty
    }

    @Override
    public void updateNode(@Nonnull RenderContext ctx, @Nonnull Node node) {
        javafx.scene.Group g = (javafx.scene.Group) node;
        Line lineNode = (Line) g.getChildren().get(0);
        final Path startMarkerNode = (Path) g.getChildren().get(1);
        final Path endMarkerNode = (Path) g.getChildren().get(2);

        Point2D start = getNonnull(START).getConvertedValue();
        Point2D end = getNonnull(END).getConvertedValue();

        final double startInset = getStrokeCutStart(ctx);
        final double endInset = getStrokeCutEnd(ctx);
        final String startMarkerStr = getMarkerStartShape();
        updateMarkerNode(ctx, g, startMarkerNode, start, end, startMarkerStr, getMarkerStartScaleFactor());
        final String endMarkerStr = getMarkerEndShape();
        updateMarkerNode(ctx, g, endMarkerNode, end, start, endMarkerStr, getMarkerEndScaleFactor());

        Point2D dir = end.subtract(start).normalize();
        if (startInset != 0 ) {
            start = start.add(dir.multiply(startInset));
        }
        if (endInset != 0 ) {
            end = end.add(dir.multiply(-endInset));
        }
        lineNode.setStartX(start.getX());
        lineNode.setStartY(start.getY());
        lineNode.setEndX(end.getX());
        lineNode.setEndY(end.getY());

        updateLineNode(ctx, lineNode);
        updateStartMarkerNode(ctx, startMarkerNode);
        updateEndMarkerNode(ctx, endMarkerNode);
    }

    protected void updateMarkerNode(RenderContext ctx, javafx.scene.Group group,
                                    @Nonnull Path markerNode,
                                    @Nonnull Point2D start, @Nonnull Point2D end, @Nullable String svgString, double markerScaleFactor) {
        if (svgString != null) {
            markerNode.getElements().setAll(Shapes.fxPathElementsFromSvgString(svgString));
            double angle = Math.atan2(start.getY() - end.getY(), start.getX() - end.getX());
            markerNode.getTransforms().setAll(
                    new Rotate(angle * 180 / Math.PI, start.getX(), start.getY()),
                    new Scale(markerScaleFactor, markerScaleFactor, start.getX(), start.getY()),
                    new Translate(start.getX(), start.getY()));
            markerNode.setVisible(true);
        }else{
            markerNode.setVisible(false);
        }
    }

    @Override
    public PathIterator getPathIterator(AffineTransform tx) {
        // FIXME include markers in path
        return Shapes.awtShapeFromFX(new Line(
                getNonnull(START_X).getConvertedValue(),
                getNonnull(START_Y).getConvertedValue(),
                getNonnull(END_X).getConvertedValue(),
                getNonnull(END_Y).getConvertedValue())).getPathIterator(tx);
    }

    public abstract double getStrokeCutStart(RenderContext ctx);

    public abstract double getStrokeCutEnd(RenderContext ctx);

    @Nullable
    public abstract String getMarkerStartShape();

    public abstract double getMarkerStartScaleFactor();

    @Nullable
    public abstract String getMarkerEndShape();

    public abstract double getMarkerEndScaleFactor();

    @Nullable
    public abstract String getMarkerCenterShape();

    public abstract double getMarkerCenterScaleFactor();


    @Override
    public void layout(@Nonnull RenderContext ctx) {
        Point2D start = getNonnull(START).getConvertedValue();
        Point2D end = getNonnull(END).getConvertedValue();
        Connector startConnector = get(START_CONNECTOR);
        Connector endConnector = get(END_CONNECTOR);
        Figure startTarget = get(START_TARGET);
        Figure endTarget = get(END_TARGET);
        if (startConnector != null && startTarget != null) {
            start = startConnector.getPositionInWorld(this, startTarget);
        }
        if (endConnector != null && endTarget != null) {
            end = endConnector.getPositionInWorld(this, endTarget);
        }

        // We must switch off rotations for the following computations
        // because
        if (startConnector != null && startTarget != null) {
            final Point2D p = worldToParent(startConnector.chopStart(this, startTarget, start, end));
            set(START, new CssPoint2D(p));
        }
        if (endConnector != null && endTarget != null) {
            final Point2D p = worldToParent(endConnector.chopEnd(this, endTarget, start, end));
            set(END, new CssPoint2D(p));
        }
    }
}