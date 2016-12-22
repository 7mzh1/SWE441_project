/* @(#)ProjectView.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw8.app;

import org.jhotdraw8.collection.HierarchicalMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.jhotdraw8.app.action.Action;
import org.jhotdraw8.beans.PropertyBean;

/**
 * A {@code ProjectView} provides a user interface for a project which is
 * identified by an URI.
 * <p>
 * The life-cycle of project view objects is managed by an application. See the
 * class comment of {@link Application} on how to launch an application.
 * <p>
 * The lifecycle of a project view consists of the following steps:
 * <ol>
 * <li><b>Creation</b><br>
 * The application instantiates the project view object by calling
 * {@code newInstance()} on the class of the view.
 * </li>
 * <li><b>Initialisation</b><br>
 * The application calls the following methods:
 * {@code getActionMap().setParent();setApplication(); init()}. Then it either
 * calls {@code clear()} or {@code read()}.
 * </li>
 * <li><b>Start</b><br>
 * The application adds the component of the project view to a container (for
 * example a JFrame) and then calls {@code start()}.
 * </li>
 * <li><b>Activation</b><br>
 * When a view becomes the active project view of the application, application
 * calls {@code activate()}.
 * </li>
 * <li><b>Deactivation</b><br>
 * When a view is not anymore the active project view of the application,
 * application calls {@code deactivate()}. At a later time, the project view may
 * become activated again.
 * </li>
 * <li><b>Stop</b><br>
 * The application calls {@code stop()} on the project view and then removes the
 * component from its container. At a later time, the project view may be
 * started again.
 * </li>
 * <li><b>Dispose</b><br>
 * When the view is no longer needed, application calls {@code dispose()} on the
 * view, followed by
 * {@code setApplication(null);}, {@code getActionMap().setParent(null)} and
 * then removes all references to it, so that it can be garbage collected.
 * </li>
 * </ol>
 *
 * @param <V> the project view type
 * @design.pattern Application Framework, KeyAbstraction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface ProjectView<V extends ProjectView<V>> extends Disableable, PropertyBean {

    /**
     * Initializes the view. This method must be called before the view can be
     * used.
     */
    public void init();

    /**
     * Starts the view.
     */
    public void start();

    /**
     * Activates the view.
     */
    public void activate();

    /**
     * Deactivates the view.
     */
    public void deactivate();

    /**
     * Stops the view.
     */
    public void stop();

    /**
     * Disposes of the view.
     */
    public void dispose();

    /**
     * Returns the scene node which renders the view.
     *
     * @return The node.
     */
    public Node getNode();

    /**
     * Provides a title for the view
     *
     * @return The title property.
     */
    public StringProperty titleProperty();

    // convenience method
    default public String getTitle() {
        return titleProperty().get();
    }

    default public void setTitle(String newValue) {
        titleProperty().set(newValue);
    }

    /**
     * The application property is maintained by the application.
     *
     * @return the property
     */
    public ObjectProperty<Application<V>> applicationProperty();

    default public Application<V> getApplication() {
        return applicationProperty().get();
    }

    default public void setApplication(Application<V> newValue) {
        applicationProperty().set(newValue);
    }

    /**
     * The action map of the view.
     *
     * @return the action map
     */
    public HierarchicalMap<String, Action> getActionMap();

    public IntegerProperty disambiguationProperty();

    default public int getDisambiguation() {
        return disambiguationProperty().get();
    }

    default public void setDisambiguation(int newValue) {
        disambiguationProperty().set(newValue);
    }

}