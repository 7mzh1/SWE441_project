/*
 * @(#)AbstractConnectionHandle.java
 *
 * Copyright (c) 1996-2010 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and
 * contributors of the JHotDraw project ("the copyright holders").
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * the copyright holders. For details see accompanying license terms.
 */
package org.jhotdraw.draw.handle;

import org.jhotdraw.draw.handle.HandleAttributeKeys;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.AbstractHandle;
import org.jhotdraw.draw.liner.Liner;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.connector.ConnectorSubTracker;
import org.jhotdraw.draw.connector.RelativeConnector;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.draw.handle.BezierControlPointHandle;
import org.jhotdraw.draw.*;
import java.awt.event.InputEvent;
import org.jhotdraw.geom.*;
import org.jhotdraw.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * This abstract class can be extended to implement a {@link Handle}
 * the start or end point of a {@link ConnectionFigure}.
 *
 * @author Werner Randelshofer
 * @version $Id: AbstractConnectionHandle.java 527 2009-06-07 14:28:19Z rawcoder $
 */
public abstract class AbstractConnectionHandle extends AbstractHandle {

    private Connector savedTarget;
    private Connector connectableConnector;
    private Figure connectableFigure;
    /**
     * We temporarily remove the Liner from the connection figure, while the
     * handle is being moved.
     * We store the Liner here, and add it back when the user has finished
     * the interaction.
     */
    private Liner savedLiner;
    /**
     * All connectors of the connectable Figure.
     */
    protected Collection<Connector> connectors = Collections.emptyList();

    /**
     * Initializes the change connection handle.
     */
    protected AbstractConnectionHandle(ConnectionFigure owner) {
        super(owner);
    }

    public ConnectionFigure getOwner() {
        return (ConnectionFigure) super.getOwner();
    }

    public boolean isCombinableWith(Handle handle) {
        return false;
    }

    /**
     * Returns the connector of the change.
     */
    protected abstract Connector getTarget();

    /**
     * Disconnects the connection.
     */
    protected abstract void disconnect();

    /**
     * Connect the connection with the given figure.
     */
    protected abstract void connect(Connector c);

    /**
     * Sets the location of the connectableConnector point.
     */
    protected abstract void setLocation(Point2D.Double p);

    /**
     * Returns the start point of the connection.
     */
    protected abstract Point2D.Double getLocation();

    /**
     * Gets the side of the connection that is unaffected by
     * the change.
     */
    protected Connector getSource() {
        if (getTarget() == getOwner().getStartConnector()) {
            return getOwner().getEndConnector();
        }
        return getOwner().getStartConnector();
    }

    /* (non-Javadoc)
     * @see org.jhotdraw.draw.handle.AbstractHandle#attributeChanged(org.jhotdraw.draw.event.FigureEvent)
     */
    @Override
    public void attributeChanged(FigureEvent e) {
        AttributeKey<?> key = e.getAttribute();
        boolean strategyChange = (key.equals(AttributeKeys.START_CONNECTOR_STRATEGY) || key
                .equals(AttributeKeys.END_CONNECTOR_STRATEGY));
        if (strategyChange) {
            LineConnectionFigure connection = (LineConnectionFigure)e.getFigure();
            boolean isStart = (key.equals(AttributeKeys.START_CONNECTOR_STRATEGY));
            Connector connector = connection.getStartConnector();
            if (!isStart) {
                connector = connection.getEndConnector();
            }
            ConnectorSubTracker connectorSubTracker = view.getEditor().getConnectorSubTracker();
            connectorSubTracker.touchConnector(connector);
        }
    }


    /**
     * Initializes tracking
     */
    public void trackStart(Point anchor, int modifiersEx) {
        savedTarget = getTarget();
        savedLiner = getOwner().getLiner();
        getOwner().setLiner(null);
        ConnectionFigure connection = getOwner();
        boolean isStartConnector = (savedTarget == connection.getStartConnector()) ? true : false;

        ConnectorSubTracker connectorSubTracker = view.getEditor().getConnectorSubTracker();
        connectorSubTracker.dragConnector(savedTarget, ConnectorSubTracker.trackStart,
                isStartConnector,  null, modifiersEx);
        fireHandleRequestSecondaryHandles();
    }

    /**
     *  drags an existing connector if over the same figure
     *  updates the connection point but keeps the same connectors
     */
    public void trackStep(Point anchor, Point lead, int modifiersEx) {
        Point2D.Double p = view.viewToDrawing(lead);
        view.getConstrainer().constrainPoint(p);

        Figure targetFigure = findConnectableFigure(p, view.getDrawing());
        if (targetFigure == savedTarget.getOwner()) {
            ConnectionFigure connection = getOwner();
            boolean isStartConnector = (savedTarget == connection.getStartConnector()) ? true : false;
            ConnectorSubTracker connectorSubTracker = view.getEditor().getConnectorSubTracker();
            connectorSubTracker.dragConnector(savedTarget, ConnectorSubTracker.trackStep,
                    isStartConnector, p, modifiersEx);
            p = (isStartConnector) ? savedTarget.findStart(getOwner()) : savedTarget.findEnd(getOwner());
        }
        getOwner().willChange();
        setLocation(p);
        getOwner().changed();

        if (getOwner().findStartConnectorStrategyName() == null)
        repaintConnectors();
    }

    /**
     * Connects the figure to the new connectableConnector. If there is no
     * new connectableConnector the connection reverts to its original one.
     */
    public void trackEnd(Point anchor, Point lead, int modifiersEx) {
        ConnectionFigure f = getOwner();
        // Change node type
        if ((modifiersEx & (InputEvent.META_DOWN_MASK | InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)) != 0 &&
                (modifiersEx & InputEvent.BUTTON2_DOWN_MASK) == 0) {
            f.willChange();
            int index = getBezierNodeIndex();
            BezierPath.Node v = f.getNode(index);
            if (index > 0 && index < f.getNodeCount()) {
                v.mask = (v.mask + 3) % 4;
            } else if (index == 0) {
                v.mask = ((v.mask & BezierPath.C2_MASK) == 0) ? BezierPath.C2_MASK : 0;
            } else {
                v.mask = ((v.mask & BezierPath.C1_MASK) == 0) ? BezierPath.C1_MASK : 0;
            }
            f.setNode(index, v);
            f.changed();
            fireHandleRequestSecondaryHandles();
        }

        Point2D.Double p = view.viewToDrawing(lead);
        view.getConstrainer().constrainPoint(p);
        ConnectionFigure connection = getOwner();
        boolean isStartConnector = (savedTarget == connection.getStartConnector()) ? true : false;

        Connector target = findConnectionTarget(p, view.getDrawing());
        ConnectorSubTracker connectorSubTracker = view.getEditor().getConnectorSubTracker();
        if (target != savedTarget) {
            //dragging to a different figure ... must be a tracking connector
            p = (isStartConnector) ? target.findStart(getOwner()) : target.findEnd(getOwner());

            target = connectorSubTracker.dragConnector(target, ConnectorSubTracker.trackEnd,
                    isStartConnector, p, modifiersEx);
            // connector might have been vetoed
            if (target == null)
            target = savedTarget;
        }

        p = (isStartConnector) ? target.findStart(getOwner()) : target.findEnd(getOwner());
        setLocation(p);
        if (target != savedTarget) {
            disconnect();
            connect(target);
        }
        getOwner().setLiner(savedLiner);
        getOwner().updateConnection();
        connectorSubTracker.touchConnector(target);
        connectors = Collections.emptyList();
    }

    /**
     * @param p
     * @param drawing
     * @return connector
     */
    private Connector findConnectionTarget(Point2D.Double p, Drawing drawing) {
        //can't change a self-connection to another figure
        if (savedTarget.getOwner() == getSource().getOwner())
            return savedTarget;

        Figure targetFigure = findConnectableFigure(p, drawing);
        if (targetFigure != null && targetFigure != getSource().getOwner()) {
            Connector target = findConnector(p, targetFigure, getOwner());
            if ((targetFigure != null) && targetFigure.isConnectable()
                    && !targetFigure.includes(getOwner())
                    && (canConnect(getSource(), target))) {
                return target;
            }
        }
        return savedTarget;
    }

    protected abstract boolean canConnect(Connector existingEnd, Connector targetEnd);

    /**
     * @param p
     * @param f
     * @param prototype
     * @return connector
     */
    protected Connector findConnector(Point2D.Double p, Figure f, ConnectionFigure prototype) {
        ConnectorSubTracker connectorSubTracker = view.getEditor().getConnectorSubTracker();
        return connectorSubTracker.findConnector(p, f, prototype);
    }

    /**
     * Draws this handle.
     */
    @Override
    public void draw(Graphics2D g) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.transform(view.getDrawingToViewTransform());
        for (Connector c : connectors) {
            c.draw(gg);
        }

        gg.dispose();

        if (getTarget() == null) {
            drawCircle(g,
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.DISCONNECTED_CONNECTION_HANDLE_FILL_COLOR),
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.DISCONNECTED_CONNECTION_HANDLE_STROKE_COLOR));
        } else {
            drawCircle(g,
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.CONNECTED_CONNECTION_HANDLE_FILL_COLOR),
                    (Color) getEditor().getHandleAttribute(HandleAttributeKeys.CONNECTED_CONNECTION_HANDLE_STROKE_COLOR));
        }
    }

    private Figure findConnectableFigure(Point2D.Double p, Drawing drawing) {
        for (Figure f : drawing.getFiguresFrontToBack()) {
            if (!f.includes(getOwner()) && f.isConnectable() && f.contains(p)) {
                return f;
            }
        }
        return null;
    }

    protected void setPotentialTarget(Connector newTarget) {
        this.connectableConnector = newTarget;
    }

    protected Rectangle basicGetBounds() {
        //if (connection.getPointCount() == 0) return new Rectangle(0, 0, getHandlesize(), getHandlesize());
        Point center = view.drawingToView(getLocation());
        return new Rectangle(center.x - getHandlesize() / 2, center.y - getHandlesize() / 2, getHandlesize(), getHandlesize());
    }

    protected BezierFigure getBezierFigure() {
        return (BezierFigure) getOwner();
    }

    abstract protected int getBezierNodeIndex();

    @Override
    final public Collection<Handle> createSecondaryHandles() {
        LinkedList<Handle> list = new LinkedList<Handle>();

        if (((ConnectionFigure) getOwner()).findStartConnectorStrategyName() != null)
            return list;


        if (((ConnectionFigure) getOwner()).getLiner() == null && savedLiner == null) {
            int index = getBezierNodeIndex();
            BezierFigure f = getBezierFigure();
            BezierPath.Node v = f.getNode(index);
            if ((v.mask & BezierPath.C1_MASK) != 0 &&
                    (index != 0 || f.isClosed())) {
                list.add(new BezierControlPointHandle(f, index, 1));
            }
            if ((v.mask & BezierPath.C2_MASK) != 0 &&
                    (index < f.getNodeCount() - 1 ||
                    f.isClosed())) {
                list.add(new BezierControlPointHandle(f, index, 2));
            }
            if (index > 0 || f.isClosed()) {
                int i = (index == 0) ? f.getNodeCount() - 1 : index - 1;
                v = f.getNode(i);
                if ((v.mask & BezierPath.C2_MASK) != 0) {
                    list.add(new BezierControlPointHandle(f, i, 2));
                }
            }
            if (index < f.getNodeCount() - 2 || f.isClosed()) {
                int i = (index == f.getNodeCount() - 1) ? 0 : index + 1;
                v = f.getNode(i);
                if ((v.mask & BezierPath.C1_MASK) != 0) {
                    list.add(new BezierControlPointHandle(f, i, 1));
                }
            }
        }
        return list;
    }

    protected BezierPath.Node getBezierNode() {
        int index = getBezierNodeIndex();
        return getBezierFigure().getNodeCount() > index ? getBezierFigure().getNode(index) : null;
    }

    public String getToolTipText(Point p) {
        ConnectionFigure f = (ConnectionFigure) getOwner();
        if (f.getLiner() == null && savedLiner == null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            BezierPath.Node node = getBezierNode();
            return (node == null) ? null : labels.getFormatted("handle.bezierNode.toolTipText",
                    labels.getFormatted(
                    (node.getMask() == 0) ? "bezierNode.linearNode" : ((node.getMask() == BezierPath.C1C2_MASK) ? "bezierNode.cubicNode" : "bezierNode.quadraticNode")));
        } else {
            return null;
        }
    }

    /**
     * Updates the list of connectors that we draw when the user
     * moves or drags the mouse over a figure to which can connect.
     */
    public void repaintConnectors() {
        Rectangle2D.Double invalidArea = null;
        for (Connector c : connectors) {
            if (invalidArea == null) {
                invalidArea = c.getDrawingArea();
            } else {
                invalidArea.add(c.getDrawingArea());
            }
        }
        connectors = (connectableFigure == null) ? new java.util.LinkedList<Connector>() : connectableFigure.getConnectors(getOwner());
        for (Connector c : connectors) {
            if (invalidArea == null) {
                invalidArea = c.getDrawingArea();
            } else {
                invalidArea.add(c.getDrawingArea());
            }
        }
        if (invalidArea != null) {
            view.getComponent().repaint(
                    view.drawingToView(invalidArea));
        }
    }
}