/*
 * @(#)OpenAction.java
 *
 * Copyright (c) 1996-2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.app.action;

import org.jhotdraw.util.*;
import org.jhotdraw.gui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.gui.chooser.URIChooser;
import org.jhotdraw.net.URIUtil;

/**
 * Opens a file in a new view, or in the current view, if it is empty.
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class OpenAction extends AbstractApplicationAction {

    public final static String ID = "file.open";

    /** Creates a new instance. */
    public OpenAction(Application app) {
        super(app);
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
        labels.configureAction(this, ID);
    }

    protected URIChooser getChooser(View view) {
        return view.getOpenChooser();
    }

    public void actionPerformed(ActionEvent evt) {
        final Application app = getApplication();
        if (app.isEnabled()) {
            app.setEnabled(false);
            // Search for an empty view
            View emptyView = app.getActiveView();
            if (emptyView == null ||
                    emptyView.getURI() != null ||
                    emptyView.hasUnsavedChanges() ||
                    !emptyView.isEnabled()) {
                emptyView = null;
            }

            final View view;
            boolean disposeView;
            if (emptyView == null) {
                view = app.createView();
                app.add(view);
                disposeView = true;
            } else {
                view = emptyView;
                disposeView = false;
            }
            URIChooser fileChooser = getChooser(view);
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            if (showDialog(fileChooser, app.getComponent()) == JFileChooser.APPROVE_OPTION) {
                app.show(view);
                openViewFromURI(view, fileChooser.getSelectedURI());
            } else {
                if (disposeView) {
                    app.dispose(view);
                }
                app.setEnabled(true);
            }
        }
    }

    protected void openViewFromURI(final View view, final URI uri) {
        final Application app = getApplication();
        app.setEnabled(true);
        view.setEnabled(false);

        // If there is another view with we set the multiple open
        // id of our view to max(multiple open id) + 1.
        int multipleOpenId = 1;
        for (View aView : app.views()) {
            if (aView != view &&
                    aView.getURI() != null &&
                    aView.getURI().equals(uri)) {
                multipleOpenId = Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
            }
        }
        view.setMultipleOpenId(multipleOpenId);
        view.setEnabled(false);

        // Open the file
        view.execute(new Worker() {

            public Object construct() throws IOException {
                boolean exists = true;
                try {
                    exists = new File(uri).exists();
                } catch (IllegalArgumentException e) {

                }
                if (exists) {
                    view.read(uri);
                    return null;
                } else {
                    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    throw new IOException(labels.getFormatted("file.open.fileDoesNotExist.message", URIUtil.getName(uri)));
                }
            }

            @Override
            protected void done(Object value) {
                final Application app = getApplication();
                view.setURI(uri);
                view.setEnabled(true);
                Frame w = (Frame) SwingUtilities.getWindowAncestor(view.getComponent());
                if (w != null) {
                    w.setExtendedState(w.getExtendedState() & ~Frame.ICONIFIED);
                    w.toFront();
                }
                view.getComponent().requestFocus();
                app.addRecentURI(uri);
                app.setEnabled(true);
            }

            @Override
            protected void failed(Throwable value) {
                view.setEnabled(true);
                app.setEnabled(true);
                String message;
                if ((value instanceof Throwable) && ((Throwable) value).getMessage() != null) {
                    message = ((Throwable) value).getMessage();
                    ((Throwable) value).printStackTrace();
                } else if ((value instanceof Throwable)) {
                    message = value.toString();
                    ((Throwable) value).printStackTrace();
                } else {
                    message = value.toString();
                }
                ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                JSheet.showMessageSheet(view.getComponent(),
                        "<html>" + UIManager.getString("OptionPane.css") +
                        "<b>" + labels.getFormatted("file.open.couldntOpen.message", URIUtil.getName(uri)) + "</b><p>" +
                        ((message == null) ? "" : message),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /** We implement JFileChooser.showDialog by ourselves, so that we can center
     * dialogs properly on screen on Mac OS X.
     */
    public int showDialog(URIChooser chooser, Component parent) {
        final Component finalParent = parent;
        final int[] returnValue = new int[1];
        final JDialog dialog = createDialog(chooser, finalParent);
        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                returnValue[0] = JFileChooser.CANCEL_OPTION;
            }
        });
        chooser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("CancelSelection")) {
                    returnValue[0] = JFileChooser.CANCEL_OPTION;
                    dialog.setVisible(false);
                } else if (e.getActionCommand().equals("ApproveSelection")) {
                    returnValue[0] = JFileChooser.APPROVE_OPTION;
                    dialog.setVisible(false);
                }
            }
        });
        returnValue[0] = JFileChooser.ERROR_OPTION;
       chooser.rescanCurrentDirectory();

        dialog.setVisible(true);
        //chooser.firePropertyChange("JFileChooserDialogIsClosingProperty", dialog, null);
        dialog.removeAll();
        dialog.dispose();
        return returnValue[0];
    }

    /** We implement JFileChooser.showDialog by ourselves, so that we can center
     * dialogs properly on screen on Mac OS X.
     */
    protected JDialog createDialog(URIChooser chooser, Component parent) throws HeadlessException {
        String title = chooser.getDialogTitle();
       if (chooser instanceof JFileChooser) {
           ((JFileChooser) chooser).getAccessibleContext().setAccessibleDescription(title);
        }

        JDialog dialog;
        Window window = (parent instanceof Window) ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, title, true);
        } else {
            dialog = new JDialog((Dialog) window, title, true);
        }
        dialog.setComponentOrientation(chooser.getComponent().getComponentOrientation());

        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooser.getComponent(), BorderLayout.CENTER);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations =
                    UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
            }
        }
        dialog.pack();
        if (window.getBounds().isEmpty()) {
            Rectangle screenBounds = window.getGraphicsConfiguration().getBounds();
            dialog.setLocation(screenBounds.x + (screenBounds.width - dialog.getWidth()) / 2, //
                    screenBounds.y + (screenBounds.height - dialog.getHeight()) / 3);
        } else {
            dialog.setLocationRelativeTo(parent);
        }

        return dialog;
    }
}
