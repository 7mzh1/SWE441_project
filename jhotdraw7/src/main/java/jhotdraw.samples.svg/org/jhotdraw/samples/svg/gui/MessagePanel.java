/* @(#)MessagePanel.java
 * Copyright © The authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.samples.svg.SVGLabels;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionListener;

/**
 * MessagePanel.
 * <p>
 * The MessagePanel covers the whole content pane of the DrawingApplet. 
 * The DrawingApplet registers with the  DrawingComponent as an
 * ActionListener to receive "save" and "cancel" action 
 * commands.
 * 
 * @author Werner Randelshofer
 * @version $Id$
 */
public class MessagePanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    @Nullable private EventListenerList listeners;
    private ResourceBundleUtil labels;
    
    /** Creates new instance. */
    public MessagePanel() {
        this(null, null);
    }
    
    public MessagePanel(@Nullable Icon icon, @Nullable String message) {
        labels = SVGLabels.getLabels();
        initComponents();
        setIcon(icon);
        setMessage(message);
    }
    
    public void setMessage(@Nullable String message) {
        messageLabel.setText(message);
    }
    public void setIcon(@Nullable Icon icon) {
        iconLabel.setIcon(icon);
    }
    public void addActionListener(ActionListener listener) {
        if (listeners == null) {
            listeners = new EventListenerList();
            listeners.add(ActionListener.class, listener);
        }
        
    }
    public void removeActionListener(ActionListener listener) {
        if (listeners != null) {
            listeners.remove(ActionListener.class, listener);
            if (listeners.getListenerCount() == 0) {
                listeners = null;
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        iconLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new java.awt.GridBagLayout());
        add(iconLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(messageLabel, gridBagConstraints);

        closeButton.setText(labels.getString("messagePanel.close.text")); // NOI18N
        closeButton.setActionCommand("close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closePerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        add(closeButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void closePerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closePerformed
        for (ActionListener l : listeners.getListeners(ActionListener.class)) {
            l.actionPerformed(evt);
        }
    }//GEN-LAST:event_closePerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JLabel messageLabel;
    // End of variables declaration//GEN-END:variables
    
}
