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
 * CameraPosDialog.java
 *
 * Created on 21 January 2006, 13:02
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.terraj.fracplanet.render.CameraPosition;
import com.alvermont.terraj.fracplanet.render.TriangleMeshViewerDisplay;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A dialog that manages a list of saved camera positions
 *
 * @author  martin
 * @version $Id: CameraPosDialog.java,v 1.10 2006/07/06 06:58:34 martin Exp $
 */
public class CameraPosDialog extends javax.swing.JDialog
{
    private List<CameraPositionAdapter> positions =
        new ArrayList<CameraPositionAdapter>();
    private TriangleMeshViewerDisplay display;

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /** A listener class to update the dialog state */
    private class MySelectionListener implements ListSelectionListener
    {
        /** Create a new instance of MySelectionListener */
        public MySelectionListener()
        {
        }

        public void valueChanged(ListSelectionEvent e)
        {
            final DefaultListSelectionModel dlsm =
                (DefaultListSelectionModel) cameraTable.getSelectionModel();
            final CameraTableModel model =
                (CameraTableModel) cameraTable.getModel();

            // can always delete or goto if something is selected
            deleteButton.setEnabled(!dlsm.isSelectionEmpty());
            gotoButton.setEnabled(!dlsm.isSelectionEmpty());

            // can only move up / down if not already at top or bottom
            if (dlsm.isSelectionEmpty())
            {
                upButton.setEnabled(false);
                downButton.setEnabled(false);
            }
            else
            {
                upButton.setEnabled(dlsm.getMinSelectionIndex() > 0);
                downButton.setEnabled(
                    dlsm.getMinSelectionIndex() < (model.getRowCount() - 1));
            }
        }
    }

    private class MyMouseListener extends MouseAdapter
    {
        public MyMouseListener()
        {
        }

        public void mouseClicked(MouseEvent e)
        {
            super.mouseClicked(e);

            // we look for a double click of button 1
            if (
                (e.getClickCount() == 2) &&
                    (e.getButton() == MouseEvent.BUTTON1))
            {
                final int row =
                    cameraTable.getSelectionModel()
                        .getMinSelectionIndex();
                final CameraPositionAdapter adapt = positions.get(row);

                display.setCameraPosition(adapt.getPos());
            }
        }
    }

    private class CameraPositionAdapter
    {
        private CameraPosition pos;

        public CameraPositionAdapter(CameraPosition pos)
        {
            this.pos = pos;
        }

        public CameraPositionAdapter(CameraPosition pos, int number)
        {
            this.pos = pos;
            this.number = number;
        }

        public Object[] toObjectArray()
        {
            final Object[] data = new Object[7];

            int i = 0;

            data[i] = new Integer(number);
            data[++i] = this.pos.getName();
            data[++i] = this.pos.getEye();
            data[++i] = this.pos.getCentre();
            data[++i] = this.pos.getUp();
            data[++i] = this.pos.getEyeXRotation();
            data[++i] = this.pos.getEyeYRotation();

            return data;
        }

        /**
         * Holds value of property number.
         */
        private int number;

        /**
         * Getter for property number.
         * @return Value of property number.
         */
        public int getNumber()
        {
            return this.number;
        }

        /**
         * Setter for property number.
         * @param number New value of property number.
         */
        public void setNumber(int number)
        {
            this.number = number;
        }

        /**
         * Getter for property pos.
         * @return Value of property pos.
         */
        public CameraPosition getPos()
        {
            return this.pos;
        }

        /**
         * Setter for property pos.
         * @param pos New value of property pos.
         */
        public void setPos(CameraPosition pos)
        {
            this.pos = pos;
        }
    }

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /**
     * Creates new form CameraPosDialog
     *
     * @param parent The parent object for this form
     * @param modal Indicates whether this is a modal dialog
     */
    public CameraPosDialog(AbstractTerrainViewerFrame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        final DefaultListSelectionModel dlsm =
            (DefaultListSelectionModel) cameraTable.getSelectionModel();

        dlsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dlsm.addListSelectionListener(new MySelectionListener());
        cameraTable.addMouseListener(new MyMouseListener());

        ((DefaultCellEditor) cameraTable.getDefaultEditor(String.class)).setClickCountToStart(
            1);
    }

    /**
     * Get the list of camera positions from this object
     *
     * @return The list of current camera positions that the user has created
     * or edited
     */
    public List<CameraPosition> getPositions()
    {
        final List<CameraPosition> poslist = new ArrayList<CameraPosition>();
        final CameraTableModel model = new CameraTableModel();

        for (CameraPositionAdapter cpa : this.positions)
        {
            final CameraPosition pos = cpa.getPos();

            // TODO: can't edit name because this is wrong. Fix it.
            //pos.setName((String) model.getValueAt(poslist.size(), 1));
            poslist.add(pos);
        }

        return poslist;
    }

    /**
     * Set the list of camera positions
     *
     * @param poslist A list of camera positions that is to be set as the
     * current list in this object
     */
    void setPositions(List<CameraPosition> poslist)
    {
        final CameraTableModel model = new CameraTableModel();

        for (CameraPosition pos : poslist)
        {
            final CameraPositionAdapter cpa = new CameraPositionAdapter(pos);

            this.positions.add(cpa);
            model.addRow(cpa.toObjectArray());
        }

        cameraTable.setModel(model);
    }

    /**
     * Set the display object being used by this form
     *
     * @param display The mesh display object we are associated with
     */
    void setDisplay(TriangleMeshViewerDisplay display)
    {
        this.display = display;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        cameraTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        gotoButton = new javax.swing.JButton();

        setTitle("Camera Positions");
        cameraTable.setModel(new CameraTableModel());
        cameraTable.setToolTipText("Table of camera position data");
        cameraTable.setAutoResizeMode(
            javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(cameraTable);

        addButton.setText("Add");
        addButton.setToolTipText("Add the current camera position to the list");
        addButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    addButtonActionPerformed(evt);
                }
            });

        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Delete the selected camera position");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    deleteButtonActionPerformed(evt);
                }
            });

        upButton.setText("Up");
        upButton.setToolTipText("Move the selected position up in the list");
        upButton.setEnabled(false);
        upButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    upButtonActionPerformed(evt);
                }
            });

        downButton.setText("Down");
        downButton.setToolTipText(
            "Move the selected position down in the list");
        downButton.setEnabled(false);
        downButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    downButtonActionPerformed(evt);
                }
            });

        jButton5.setText("More >>");
        jButton5.setToolTipText("Display or hide the detailed information");
        jButton5.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton5ActionPerformed(evt);
                }
            });

        gotoButton.setText("Goto");
        gotoButton.setToolTipText("Move the camera to the selected position");
        gotoButton.setEnabled(false);
        gotoButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    gotoButtonActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout layout =
            new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane()
            .setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                layout.createSequentialGroup().addContainerGap().add(
                    layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        jScrollPane1,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 366,
                        Short.MAX_VALUE).add(
                        layout.createSequentialGroup().add(addButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED).add(
                            deleteButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 128,
                            Short.MAX_VALUE).add(upButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED).add(
                            downButton)).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        layout.createSequentialGroup().add(gotoButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 232,
                            Short.MAX_VALUE).add(jButton5))).addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().addContainerGap().add(
                    jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    363, Short.MAX_VALUE).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        addButton).add(deleteButton).add(downButton).add(
                        upButton)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jButton5).add(
                        gotoButton)).addContainerGap()));

        java.awt.Dimension screenSize =
            java.awt.Toolkit.getDefaultToolkit()
                .getScreenSize();
        setBounds(
            (screenSize.width - 394) / 2, (screenSize.height - 470) / 2, 394,
            470);
    } // </editor-fold>//GEN-END:initComponents

    private void gotoButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gotoButtonActionPerformed
    {//GEN-HEADEREND:event_gotoButtonActionPerformed

        final int row = cameraTable.getSelectionModel()
                .getMinSelectionIndex();
        final CameraPositionAdapter adapt = positions.get(row);

        display.setCameraPosition(adapt.getPos());
    }//GEN-LAST:event_gotoButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_downButtonActionPerformed
    {//GEN-HEADEREND:event_downButtonActionPerformed

        final CameraTableModel model =
            (CameraTableModel) cameraTable.getModel();
        final int row = cameraTable.getSelectionModel()
                .getMinSelectionIndex();

        final CameraPositionAdapter adapt = positions.remove(row);
        positions.add(row + 1, adapt);

        model.moveRow(row, row + 1);
    }//GEN-LAST:event_downButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_upButtonActionPerformed
    {//GEN-HEADEREND:event_upButtonActionPerformed

        final CameraTableModel model =
            (CameraTableModel) cameraTable.getModel();
        final int row = cameraTable.getSelectionModel()
                .getMinSelectionIndex();

        final CameraPositionAdapter adapt = positions.remove(row);
        positions.add(row - 1, adapt);

        model.moveRow(row, row - 1);
    }//GEN-LAST:event_upButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
    {//GEN-HEADEREND:event_deleteButtonActionPerformed

        final CameraTableModel model =
            (CameraTableModel) cameraTable.getModel();
        final int row = cameraTable.getSelectionModel()
                .getMinSelectionIndex();

        model.removeRow(row);
        positions.remove(row);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed

        final CameraTableModel model =
            (CameraTableModel) cameraTable.getModel();
        final JButton button = (JButton) evt.getSource();

        model.changeVisibility(!model.getVisibility());

        if (model.getVisibility())
        {
            button.setText("<< Less");
        }
        else
        {
            button.setText("More >>");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
    {//GEN-HEADEREND:event_addButtonActionPerformed

        final Object[] newRow = new Object[5];

        final CameraTableModel model =
            (CameraTableModel) cameraTable.getModel();
        final CameraPosition pos =
            new CameraPosition(display.getCameraPosition());

        final CameraPositionAdapter adapt = new CameraPositionAdapter(pos);
        positions.add(adapt);

        model.addRow(adapt.toObjectArray());
    }//GEN-LAST:event_addButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable cameraTable;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton gotoButton;
    private javax.swing.JButton jButton5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton upButton;

    // End of variables declaration//GEN-END:variables
}
