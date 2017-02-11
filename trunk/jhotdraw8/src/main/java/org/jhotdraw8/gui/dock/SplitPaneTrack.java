/* @(#)SplitPaneTrack.java
 * Copyright (c) 2017 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.gui.dock;

import javafx.geometry.Orientation;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

/**
 * SplitPaneTrack.
 *
 * @author Werner Randelshofer
 * @version $$Id$$
 */
public class SplitPaneTrack extends SplitPane implements Track {

    public SplitPaneTrack() {
        this(VERTICAL);
    }

    public SplitPaneTrack(Orientation o) {
        setOrientation(o);
                getStyleClass().add("track");
        setStyle("-fx-background-color:transparent;-fx-border-width:0,0;-fx-padding:0;");

    }

    public SplitPaneTrack(Node... items) {
        super(items);
    }

    public static SplitPaneTrack createVerticalTrack() {
        return new SplitPaneTrack(VERTICAL);
    }

    public static SplitPaneTrack createHorizontalTrack() {
        return new SplitPaneTrack(Orientation.HORIZONTAL);
    }
}