/*
 * @(#)UMLEdgeFigure.java
 * Copyright © 2021 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.samples.modeler.figure;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssSize;
import org.jhotdraw8.draw.figure.AbstractElbowLineConnectionWithMarkersFigure;
import org.jhotdraw8.draw.figure.CompositableFigure;
import org.jhotdraw8.draw.figure.ElbowableLineFigure;
import org.jhotdraw8.draw.figure.HideableFigure;
import org.jhotdraw8.draw.figure.LockableFigure;
import org.jhotdraw8.draw.figure.MarkerEndableFigure;
import org.jhotdraw8.draw.figure.MarkerFillableFigure;
import org.jhotdraw8.draw.figure.MarkerSegmentableFigure;
import org.jhotdraw8.draw.figure.MarkerStartableFigure;
import org.jhotdraw8.draw.figure.MarkerStrokableFigure;
import org.jhotdraw8.draw.figure.StrokableFigure;
import org.jhotdraw8.draw.figure.StrokeCuttableFigure;
import org.jhotdraw8.draw.figure.StyleableFigure;
import org.jhotdraw8.draw.key.BooleanStyleableKey;
import org.jhotdraw8.draw.key.NullableBooleanStyleableKey;
import org.jhotdraw8.draw.key.NullableStringStyleableKey;
import org.jhotdraw8.draw.render.RenderContext;

/**
 * Renders a "UMLEdge" element.
 * <p>
 * A UMLEdge is drawn as a line that connects two "UMLShape" figures, such
 * as two "UMLClassifierShape" figures.<br>
 * An UMLEdge can have start-, mid- and end-markers which help to visualize the
 * metaclass and direction of the edge.
 *
 * @author Werner Randelshofer
 */
public class UMLEdgeFigure extends AbstractElbowLineConnectionWithMarkersFigure
        implements HideableFigure, StyleableFigure,
        LockableFigure, CompositableFigure, MarkerFillableFigure, MarkerStrokableFigure, StrokableFigure,
        MarkerStartableFigure, MarkerEndableFigure, MarkerSegmentableFigure, StrokeCuttableFigure,
        ElbowableLineFigure {

    /**
     * The CSS type selector for this object is {@value #TYPE_SELECTOR}.
     */
    public static final String TYPE_SELECTOR = "MLEdge";
    public static final @Nullable NullableStringStyleableKey KEYWORD = MLConstants.KEYWORD;
    public static final @Nullable NullableBooleanStyleableKey SOURCE_OWNED = MLConstants.SOURCE_OWNED;
    public static final @Nullable NullableBooleanStyleableKey TARGET_OWNED = MLConstants.TARGET_OWNED;
    public static final BooleanStyleableKey KEYWORD_VISIBLE = MLConstants.KEYWORD_LABEL_VISIBLE;

    public UMLEdgeFigure() {
        this(0, 0, 1, 1);
    }

    public UMLEdgeFigure(@NonNull Point2D start, @NonNull Point2D end) {
        this(start.getX(), start.getY(), end.getX(), end.getY());
    }

    public UMLEdgeFigure(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    @Override
    public @NonNull String getTypeSelector() {
        return TYPE_SELECTOR;
    }

    @Override
    protected void updateEndMarkerNode(RenderContext ctx, @NonNull Path node) {
        super.updateEndMarkerNode(ctx, node);
        applyMarkerFillableFigureProperties(ctx, node);
    }

    @Override
    protected void updateLineNode(RenderContext ctx, @NonNull Polyline node) {
        applyStrokableFigureProperties(ctx, node);
    }

    @Override
    public void updateNode(@NonNull RenderContext ctx, @NonNull Node node) {
        super.updateNode(ctx, node);

        applyHideableFigureProperties(ctx, node);
        applyCompositableFigureProperties(ctx, node);
        applyStyleableFigureProperties(ctx, node);
    }

    @Override
    protected void updateStartMarkerNode(RenderContext ctx, @NonNull Path node) {
        super.updateStartMarkerNode(ctx, node);
        applyMarkerFillableFigureProperties(ctx, node);
    }

    @Override
    public double getMarkerEndScaleFactor() {
        return MarkerEndableFigure.super.getMarkerEndScaleFactor();
    }

    @Override
    public String getMarkerEndShape() {
        return MarkerEndableFigure.super.getMarkerEndShape();
    }

    @Override
    public double getMarkerStartScaleFactor() {
        return MarkerStartableFigure.super.getMarkerStartScaleFactor();
    }

    @Override
    public String getMarkerStartShape() {
        return MarkerStartableFigure.super.getMarkerStartShape();
    }

    @Override
    public double getStrokeCutEnd(RenderContext ctx) {
        return StrokeCuttableFigure.super.getStrokeCutEnd();
    }

    @Override
    public double getStrokeCutStart(RenderContext ctx) {
        return StrokeCuttableFigure.super.getStrokeCutStart();
    }

    @Override
    public @Nullable String getMarkerSegmentShape() {
        return MarkerSegmentableFigure.super.getMarkerSegmentShape();
    }

    @Override
    public double getMarkerSegmentScaleFactor() {
        return MarkerSegmentableFigure.super.getMarkerSegmentScaleFactor();
    }

    @Override
    public @Nullable CssSize getElbowOffset() {
        return ElbowableLineFigure.super.getElbowOffset();
    }
}
