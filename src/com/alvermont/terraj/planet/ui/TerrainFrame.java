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
 * TerrainFrame.java
 *
 * Created on 24 January 2006, 17:39
 */
package com.alvermont.terraj.planet.ui;

import com.alvermont.terraj.util.ui.PNGFileFilter;
import com.alvermont.terraj.util.io.ImagePrinter;
import com.alvermont.terraj.util.ui.JNLPFileChooser;
import com.alvermont.terraj.util.ui.LookAndFeelUtils;
import com.alvermont.terraj.util.ui.LookAndFeelWatcher;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Frame that displays the results of terrain generation. Some of this is
 * based on the tutorial at:
 * http://java.sun.com/products/java-media/2D/forDevelopers/sdk12print.html
 * but has been modified for images.
 *
 * @author  martin
 * @version $Id: TerrainFrame.java,v 1.8 2006/07/06 06:58:35 martin Exp $
 */
public class TerrainFrame extends javax.swing.JFrame
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TerrainFrame.class);

    // RequireThis OFF: log

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS
    private static final String[] extArray = { ".png", ".jpg", ".jpeg" };
    private static final List<String> extensions = Arrays.asList(extArray);

    /** The file chooser for PNG files */
    private static JNLPFileChooser pngChooser = new JNLPFileChooser(extensions);

    /** The utilities object for look and feelin changing */
    private LookAndFeelUtils lafUtils;

    /** Our look and feel change listener */
    protected LookAndFeelWatcher lafListener;

    static
    {
        pngChooser.addChoosableFileFilter(new PNGFileFilter());
    }

    /** The image we are displaying */
    BufferedImage image;

    /**
     * Creates new form TerrainFrame
     *
     * @param image The image to be displayed
     * @param lafUtils The look and feel object so we can deregister on close
     */
    public TerrainFrame(BufferedImage image, LookAndFeelUtils lafUtils)
    {
        super();

        this.image = image;
        this.lafUtils = lafUtils;

        this.lafListener = new LookAndFeelWatcher(this);

        lafUtils.addLookAndFeelChangedEventListener(this.lafListener);

        initComponents();

        ImageIcon icon = new ImageIcon(image);

        imageLabel.setText("");
        imageLabel.setIcon(icon);
        imageLabel.setPreferredSize(
            new Dimension(image.getWidth(), image.getHeight()));
        imageScrollpane.setPreferredSize(
            new Dimension(image.getWidth(), image.getHeight()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        imageScrollpane = new javax.swing.JScrollPane();
        imageLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveImageItem = new javax.swing.JMenuItem();
        printItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        closeItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Terrain");
        addWindowListener(
            new java.awt.event.WindowAdapter()
            {
                public void windowClosed(java.awt.event.WindowEvent evt)
                {
                    formWindowClosed(evt);
                }
            });

        imageScrollpane.setPreferredSize(
            new Dimension(image.getWidth(), image.getHeight()));
        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLabel.setText("Unset");
        imageLabel.setPreferredSize(
            new Dimension(image.getWidth(), image.getHeight()));
        imageScrollpane.setViewportView(imageLabel);

        fileMenu.setText("File");
        saveImageItem.setText("Save Image ...");
        saveImageItem.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    saveImageItemActionPerformed(evt);
                }
            });

        fileMenu.add(saveImageItem);

        printItem.setText("Print ...");
        printItem.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    printItemActionPerformed(evt);
                }
            });

        fileMenu.add(printItem);

        fileMenu.add(jSeparator1);

        closeItem.setText("Close");
        closeItem.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    closeItemActionPerformed(evt);
                }
            });

        fileMenu.add(closeItem);

        jMenuBar1.add(fileMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout =
            new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane()
            .setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                layout.createSequentialGroup().addContainerGap().add(
                    imageScrollpane,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 849,
                    Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                layout.createSequentialGroup().addContainerGap().add(
                    imageScrollpane,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 649,
                    Short.MAX_VALUE).addContainerGap()));

        java.awt.Dimension screenSize =
            java.awt.Toolkit.getDefaultToolkit()
                .getScreenSize();
        setBounds(
            (screenSize.width - 877) / 2, (screenSize.height - 719) / 2, 877,
            719);
    } // </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosed
    {//GEN-HEADEREND:event_formWindowClosed
        lafUtils.removeLookAndFeelChangedEventListener(lafListener);
    }//GEN-LAST:event_formWindowClosed

    private void saveImageItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveImageItemActionPerformed
    {//GEN-HEADEREND:event_saveImageItemActionPerformed

        int choice = pngChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                if (
                    !pngChooser.getFileContents()
                        .canRead() ||
                        (JOptionPane.showConfirmDialog(
                            this,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                {
                    final OutputStream target =
                        pngChooser.getFileContents()
                            .getOutputStream(true);

                    // More JNLP silliness. How am I supposed to know what size
                    // an image file will compress to and there is no guidance
                    // in the documentation as to what value can be reasonably
                    // requested here. I've just picked something and gone with
                    // it, which seems to be in line with the philosophy of
                    // the people who designed Java WebStart
                    pngChooser.getFileContents()
                        .setMaxLength(500000);

                    ImageIO.write(
                        image,
                        PNGFileFilter.getFormatName(
                            new File(pngChooser.getFileContents().getName())),
                        target);

                    target.close();
                }
            }
            catch (IOException ioe)
            {
                log.error("Error writing image file", ioe);

                JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ioe.getMessage() +
                    "\nCheck log file for full details", "Error Saving",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveImageItemActionPerformed

    private void printItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_printItemActionPerformed
    {//GEN-HEADEREND:event_printItemActionPerformed

        final PrinterJob printJob = PrinterJob.getPrinterJob();
        final PageFormat pf = printJob.pageDialog(printJob.defaultPage());

        printJob.setPrintable(new ImagePrinter(image, pf), pf);

        if (printJob.printDialog())
        {
            try
            {
                printJob.print();
            }
            catch (Exception e)
            {
                log.error("Error printing", e);

                JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage() +
                    "\nCheck log file for full details", "Error Printing",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_printItemActionPerformed

    private void closeItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeItemActionPerformed
    {//GEN-HEADEREND:event_closeItemActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JScrollPane imageScrollpane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem printItem;
    private javax.swing.JMenuItem saveImageItem;

    // End of variables declaration//GEN-END:variables
}
