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
 * PrintHTMLDialog.java
 *
 * Created on 22 April 2006, 14:28
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.util.io.PrintUtilities;
import com.meghnasoft.async.AbstractAsynchronousAction;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.awt.event.ActionEvent;
import java.io.CharArrayWriter;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A print preview dialog box for the HTML document
 *
 * @author  martin
 * @version $Id: PrintHTMLDialog.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class PrintHTMLDialog extends javax.swing.JDialog
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(PrintHTMLDialog.class);
    
    /** The parent details form */
    protected DetailsFrame parent;
    
    /**
     * Creates new form PrintHTMLDialog
     *
     * @param parent The parent object for this dialog
     * @param modal Indicates whether or not this is a modal dialog
     */
    public PrintHTMLDialog(DetailsFrame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        this.parent = parent;
    }
    
    /**
     * Get the editor pane being displayed by this dialog
     * 
     * @return The editor pane for the dialog
     */
    public JEditorPane getEditorPane()
    {
        return htmlPane;
    }
    
    private class PrintAction extends AbstractAsynchronousAction
    {
        private DetailsFrame parent;
        
        public PrintAction(String name, DetailsFrame parent)
        {
            super(name);
            
            this.parent = parent;
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                PrintUtilities.printComponent(getEditorPane());
            }
            catch (Exception ex)
            {
                log.error("Error Printing", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Printing Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
        
        public void finished()
        {
        }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        htmlPane = new javax.swing.JEditorPane();
        printButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Print Preview");
        htmlPane.setEditable(false);
        htmlPane.setContentType("text/html");
        jScrollPane2.setViewportView(htmlPane);

        printButton.setAction(new PrintAction("Print ...", this.parent));
        printButton.setText("Print ...");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(closeButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 499, Short.MAX_VALUE)
                        .add(printButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(printButton)
                    .add(closeButton))
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-659)/2, (screenSize.height-391)/2, 659, 391);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed

        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_closeButtonActionPerformed
    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JEditorPane htmlPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton printButton;
    // End of variables declaration//GEN-END:variables
    
}
