/* @(#)FXPathBuilder.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.geom;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;

/**
 * FXPathBuilder.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class FXPathBuilder extends AbstractPathBuilder {

    public FXPathBuilder() {
        this(new ArrayList<PathElement>());
    }

    public FXPathBuilder(List<PathElement> elements) {
        this.elements = elements;
    }

    @Override
    protected void doArcTo(double rx, double ry, double xAxisRotation, double x, double y, boolean largeArcFlag, boolean sweepFlag) {
        elements.add(new ArcTo(rx, ry, xAxisRotation, x, y, largeArcFlag, sweepFlag));
    }

    private List<PathElement> elements;

    @Override
    protected void doClosePath() {
        elements.add(new ClosePath());
    }

    @Override
    protected void doCurveTo(double x, double y, double x0, double y0, double x1, double y1) {
        elements.add(new CubicCurveTo(x, y, x0, y0, x1, y1));
    }

    @Override
    protected void doLineTo(double x, double y) {
        elements.add(new LineTo(x, y));
    }

    @Override
    protected void doMoveTo(double x, double y) {
        elements.add(new MoveTo(x, y));
    }

    @Override
    protected void doQuadTo(double x, double y, double x0, double y0) {
        elements.add(new QuadCurveTo(x, y, x0, y0));
    }

    public Path get() {
        finish();
        return new Path(elements);
    }

    public List<PathElement> getElements() {
        finish();
        return elements;
    }
}
