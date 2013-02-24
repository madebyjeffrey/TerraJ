/*
 * Java Terrain and Stellar System Ports
 *
 * Copyright (C) 2006 Martin H. Smith based on work by original
 * authors.
 *
 * Released under the terms of the GNU General Public License
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * Linking TerraJ statically or dynamically with other modules is making a
 * combined work based on TerraJ. Thus, the terms and conditions of the
 * GNU General Public License cover the whole combination.
 *
 * In addition, as a special exception, the copyright holders of TerraJ
 * give you permission to combine this program with free software programs
 * or libraries that are released under the GNU LGPL and with code included
 * in the standard release of JOGL, Java Getopt and FreeMarker under the BSD
 * license (or modified versions of such code, with unchanged license) and with
 * Apache Commons and Log4J libraries under the Apache license (or modified versions
 * of such code. You may copy and distribute such a system following the terms
 * of the GNU GPL for TerraJ and the licenses of the other code concerned,
 * provided that you include the source code of that other code when and as the
 * GNU GPL requires distribution of source code.
 *
 * Note that people who make modified versions of TerraJ are not obligated to grant
 * this special exception for their modified versions; it is their choice whether
 * to do so. The GNU General Public License gives permission to release a modified
 * version without this exception; this exception also makes it possible to release
 * a modified version which carries forward this exception.
 */

/*
 * ImportXMLSettingsDialog.java
 *
 * Created on 21 January 2006, 20:13
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.terraj.util.ui.XMLFileFilter;
import java.io.File;

/**
 * Dialog for importing parts of an XML settings file
 *
 * @author  martin
 * @version $Id: ImportXMLSettingsDialog.java,v 1.6 2006/07/06 06:58:34 martin Exp $
 */
public class ImportXMLSettingsDialog extends javax.swing.JDialog
{
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cameraCheckbox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JFileChooser chooser;
    private javax.swing.JCheckBox exportCheckbox;
    private javax.swing.JPanel filePanel;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JCheckBox renderCheckbox;
    private javax.swing.JCheckBox terrainCheckbox;

    // End of variables declaration//GEN-END:variables

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /**
     * Holds value of property confirmed.
     */
    private boolean confirmed = false;

    /**
     * Creates new form ImportXMLSettingsDialog
     *
     * @param parent The parent object of this form
     * @param modal Indicates whether this is a modal dialog
     */
    public ImportXMLSettingsDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.chooser.addChoosableFileFilter(new XMLFileFilter());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        cancelButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        filePanel = new javax.swing.JPanel();
        chooser = new javax.swing.JFileChooser();
        optionsPanel = new javax.swing.JPanel();
        terrainCheckbox = new javax.swing.JCheckBox();
        renderCheckbox = new javax.swing.JCheckBox();
        exportCheckbox = new javax.swing.JCheckBox();
        cameraCheckbox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setTitle("Import Settings From XML File");
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    cancelButtonActionPerformed(evt);
                }
            });

        importButton.setText("Import");
        importButton.setEnabled(false);
        importButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    importButtonActionPerformed(evt);
                }
            });

        chooser.setControlButtonsAreShown(false);

        org.jdesktop.layout.GroupLayout filePanelLayout =
            new org.jdesktop.layout.GroupLayout(filePanel);
        filePanel.setLayout(filePanelLayout);
        filePanelLayout.setHorizontalGroup(
            filePanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                filePanelLayout.createSequentialGroup().addContainerGap().add(
                    chooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(
                    37, Short.MAX_VALUE)));
        filePanelLayout.setVerticalGroup(
            filePanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                filePanelLayout.createSequentialGroup().addContainerGap(
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE).add(
                    chooser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jTabbedPane1.addTab("File", filePanel);

        terrainCheckbox.setText(
            "Terrain Settings (such as colours, heights and snow)");
        terrainCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        terrainCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        terrainCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    terrainCheckboxActionPerformed(evt);
                }
            });

        renderCheckbox.setText(
            "Render Settings (such as sun position, fog and ambient light)");
        renderCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        renderCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        renderCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    renderCheckboxActionPerformed(evt);
                }
            });

        exportCheckbox.setText(
            "Export settings (such as whether to export atmosphere)");
        exportCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        exportCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        exportCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    exportCheckboxActionPerformed(evt);
                }
            });

        cameraCheckbox.setText(
            "Camera position list (all the saved camera positions)");
        cameraCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cameraCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cameraCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    cameraCheckboxActionPerformed(evt);
                }
            });

        jLabel1.setText("Import the following items from the file");

        org.jdesktop.layout.GroupLayout optionsPanelLayout =
            new org.jdesktop.layout.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                optionsPanelLayout.createSequentialGroup().addContainerGap().add(
                    optionsPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING, false).add(
                        jLabel1).add(
                        terrainCheckbox,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323,
                        Short.MAX_VALUE).add(
                        renderCheckbox,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        exportCheckbox,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        cameraCheckbox,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)).addContainerGap(273, Short.MAX_VALUE)));
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                optionsPanelLayout.createSequentialGroup().addContainerGap().add(
                    jLabel1).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    terrainCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    renderCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    exportCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    cameraCheckbox).addContainerGap(290, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Options", optionsPanel);

        org.jdesktop.layout.GroupLayout layout =
            new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane()
            .setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().addContainerGap().add(
                    layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.TRAILING).add(
                        org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane1,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 611,
                        Short.MAX_VALUE).add(
                        layout.createSequentialGroup().add(importButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED).add(
                            cancelButton))).addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().addContainerGap().add(
                    jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    424, Short.MAX_VALUE).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        cancelButton).add(importButton)).addContainerGap()));
        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void cameraCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cameraCheckboxActionPerformed
    {//GEN-HEADEREND:event_cameraCheckboxActionPerformed
        resetEnabled();
    }//GEN-LAST:event_cameraCheckboxActionPerformed

    private void exportCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportCheckboxActionPerformed
    {//GEN-HEADEREND:event_exportCheckboxActionPerformed
        resetEnabled();
    }//GEN-LAST:event_exportCheckboxActionPerformed

    /**
     * Get the file selected for importing
     *
     * @return The file selected by the user for importing
     */
    public File getSelectedFile()
    {
        return chooser.getSelectedFile();
    }

    /**
     * Indicates whether the terrain parameters are to be imported
     *
     * @return <pre>true</pre> if terrain parameters are to be imported
     * otherwise <pre>false</pre>
     */
    public boolean isImportTerrain()
    {
        return terrainCheckbox.isSelected();
    }

    /**
     * Indicates whether the render parameters are to be imported
     *
     * @return <pre>true</pre> if render parameters are to be imported
     * otherwise <pre>false</pre>
     */
    public boolean isImportRender()
    {
        return renderCheckbox.isSelected();
    }

    /**
     * Indicates whether the camera parameters are to be imported
     *
     * @return <pre>true</pre> if camera parameters are to be imported
     * otherwise <pre>false</pre>
     */
    public boolean isImportCamera()
    {
        return cameraCheckbox.isSelected();
    }

    /**
     * Indicates whether the export parameters are to be imported
     *
     * @return <pre>true</pre> if export parameters are to be imported
     * otherwise <pre>false</pre>
     */
    public boolean isImportExport()
    {
        return exportCheckbox.isSelected();
    }

    /**
     * Reset the state of the import button
     */
    protected void resetEnabled()
    {
        boolean state =
            this.cameraCheckbox.isSelected() ||
                this.exportCheckbox.isSelected() ||
                this.renderCheckbox.isSelected() ||
                this.terrainCheckbox.isSelected();

        if (chooser.getSelectedFile() == null)
        {
            state = false; // nothing to open
        }

        importButton.setEnabled(state);
    }

    private void renderCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renderCheckboxActionPerformed
    {//GEN-HEADEREND:event_renderCheckboxActionPerformed
        resetEnabled();
    }//GEN-LAST:event_renderCheckboxActionPerformed

    private void terrainCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_terrainCheckboxActionPerformed
    {//GEN-HEADEREND:event_terrainCheckboxActionPerformed
        resetEnabled();
    }//GEN-LAST:event_terrainCheckboxActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_importButtonActionPerformed
    {//GEN-HEADEREND:event_importButtonActionPerformed
        confirmed = true;
        setVisible(false);
    }//GEN-LAST:event_importButtonActionPerformed

    /**
     * Getter for property confirmed.
     * @return Value of property confirmed.
     */
    public boolean isConfirmed()
    {
        return this.confirmed;
    }
}
