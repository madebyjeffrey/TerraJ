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
 * SystemFrame.java
 *
 * Created on 20 April 2006, 11:03
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.fracplanet.io.FileUtils;
import com.alvermont.terraj.stargen.Constants;
import com.alvermont.terraj.stargen.Planet;
import com.alvermont.terraj.stargen.Primary;
import com.alvermont.terraj.util.io.ImagePrinter;
import com.alvermont.terraj.util.ui.PNGFileFilter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Displays the details of a solar system
 *
 * @author  martin
 * @version $Id: SystemFrame.java,v 1.16 2006/07/06 06:58:35 martin Exp $
 *
 * The JButton in JTable code came from www.devx.com. The original article
 * is at http://www.devx.com/getHelpOn/10MinuteSolution/20425. This didn't
 * work properly for me so I've disabled it for the moment.
 */
public class SystemFrame extends javax.swing.JFrame
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(SystemFrame.class);
    
    /** File chooser for saving PNG files */
    private static JFileChooser pngChooser = new JFileChooser();
    
    /** The key dialog */
    protected KeyDialog keyDialog;
    
    TableCellRenderer defaultRenderer;
    
    static
    {
        pngChooser.addChoosableFileFilter(new PNGFileFilter());
    }
    
    /**
     * Creates new form SystemFrame
     *
     * @param star The star at the centre of the solar system
     * @param planets The list of planets in the solar system
     */
    public SystemFrame(Primary star, List<Planet> planets)
    {
        initComponents();
        
        this.planets = planets;
        this.star = star;
        
        overviewTable.setModel(new JTableButtonModel());
        defaultRenderer = overviewTable.getDefaultRenderer(JButton.class);
        overviewTable.setDefaultRenderer(JButton.class,
                new JTableButtonRenderer(defaultRenderer));
        overviewTable.addMouseListener(new JTableButtonMouseListener(overviewTable));
    }
    
    class JTableButtonRenderer implements TableCellRenderer
    {
        private TableCellRenderer __defaultRenderer;
        
        public JTableButtonRenderer(TableCellRenderer renderer)
        {
            __defaultRenderer = renderer;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected,
                boolean hasFocus,
                int row, int column)
        {
            if(value instanceof Component)
                return (Component)value;
            return __defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
        }
    }
    
    class JTableButtonMouseListener implements MouseListener
    {
        private JTable __table;
        
        private void __forwardEventToButton(MouseEvent e)
        {
            TableColumnModel columnModel = __table.getColumnModel();
            int column = columnModel.getColumnIndexAtX(e.getX());
            int row    = e.getY() / __table.getRowHeight();
            Object value;
            JButton button;
            MouseEvent buttonEvent;
            
            if(row >= __table.getRowCount() || row < 0 ||
                    column >= __table.getColumnCount() || column < 0)
                return;
            
            value = __table.getValueAt(row, column);
            
            if(!(value instanceof JButton))
                return;
            
            button = (JButton)value;
            
            buttonEvent =
                    (MouseEvent)SwingUtilities.convertMouseEvent(__table, e, button);
            button.dispatchEvent(buttonEvent);
            // This is necessary so that when a button is pressed and released
            // it gets rendered properly.  Otherwise, the button may still appear
            // pressed down when it has been released.
            __table.repaint();
        }
        
        public JTableButtonMouseListener(JTable table)
        {
            __table = table;
        }
        
        public void mouseClicked(MouseEvent e)
        {
            __forwardEventToButton(e);
        }
        
        public void mouseEntered(MouseEvent e)
        {
            __forwardEventToButton(e);
        }
        
        public void mouseExited(MouseEvent e)
        {
            __forwardEventToButton(e);
        }
        
        public void mousePressed(MouseEvent e)
        {
            __forwardEventToButton(e);
        }
        
        public void mouseReleased(MouseEvent e)
        {
            __forwardEventToButton(e);
        }
    }
    
    class JTableButtonModel extends DefaultTableModel
    {
        private String[] __columns = { "#", "Type", "Distance", "Mass", "Radius"
                                        /*,"Action" */ };
        
        private Class[] classes = { java.lang.Integer.class, java.lang.String.class,
                                        java.lang.String.class, java.lang.String.class,
                                        java.lang.String.class, JButton.class };
                                        
        public String getColumnName(int column)
        {
            return __columns[column];
        }
                
        public int getColumnCount()
        {
            return __columns.length;
        }
        
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
        
        public Class getColumnClass(int column)
        {
            return classes[column];
        }
    }
    
    /**
     * Build a list of images to be used for a list of planets and assign them
     * to the panels.
     * 
     * @throws java.io.IOException If there is an error building the images
     */
    public void buildImages() throws IOException
    {
        List<JLabel> labels = UIUtils.buildImages(this.planets);
        
        JPanel panel = new JPanel();
        
        panel.add(UIUtils.getSunLabel());
        
        for (JLabel label : labels)
        {
            panel.add(label);
        }
        
        panel.setBackground(Color.BLACK);
        
        systemScrollpane.setViewportView(panel);
        
        systemScrollpane.validate();
        
        populateOverview();
    }
    
    /**
     * Fill in the details on the overview panel
     */
    public void populateOverview()
    {
        DefaultTableModel dtm = (DefaultTableModel) overviewTable.getModel();
        
        for (Planet p : this.planets)
        {
            Object[] row = new Object[6];
            
            row[0] = new Integer(p.getNumber());
            row[1] = p.getType().getPrintText(p.getType());
            row[2] = String.format("%.3f AU", p.getA());
            row[3] = String.format("%.3f EM", p.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES);
            row[4] = String.format("%.3f ER", p.getRadius() / Constants.KM_EARTH_RADIUS);
            row[5] = new JButton("Details ...");
            
            dtm.addRow(row);
        }
        
        // Disable auto resizing
        overviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set the first visible column to 100 pixels wide
        TableColumn col = overviewTable.getColumnModel().getColumn(0);
        int width = 50;
        col.setPreferredWidth(width);
        overviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        systemScrollpane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        overviewTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        closeMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        detailsMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        keyMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generated Solar System");
        systemScrollpane.setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Planetary Overview"));
        overviewTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "#", "Type", "Distance", "Mass", "Radius", "Action"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        overviewTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(overviewTable);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );

        jMenu1.setText("File");
        saveMenuItem.setText("Save Image ...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(saveMenuItem);

        printMenuItem.setText("Print Image ...");
        printMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                printMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(printMenuItem);

        jMenu1.add(jSeparator1);

        closeMenuItem.setText("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(closeMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("View");
        detailsMenuItem.setText("Solar System Details ...");
        detailsMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                detailsMenuItemActionPerformed(evt);
            }
        });

        jMenu2.add(detailsMenuItem);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");
        keyMenuItem.setText("Show Key ...");
        keyMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                keyMenuItemActionPerformed(evt);
            }
        });

        jMenu3.add(keyMenuItem);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, systemScrollpane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(systemScrollpane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-719)/2, (screenSize.height-499)/2, 719, 499);
    }// </editor-fold>//GEN-END:initComponents

    private void keyMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_keyMenuItemActionPerformed
    {//GEN-HEADEREND:event_keyMenuItemActionPerformed

        if (keyDialog == null)
            keyDialog = new KeyDialog(this, false);
        
        keyDialog.setVisible(true);
        
    }//GEN-LAST:event_keyMenuItemActionPerformed

    private void detailsMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_detailsMenuItemActionPerformed
    {//GEN-HEADEREND:event_detailsMenuItemActionPerformed

        try
        {
            Configuration cfg = new Configuration();
            // Specify the data source where the template files come from.
            cfg.setClassForTemplateLoading(TemplateTest.class,
                    "/com/alvermont/terraj/stargen/templates/");

            // Specify how templates will see the data model. This is an advanced topic...
            // but just use this:
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            DetailsFromTemplate dft = new DetailsFromTemplate();
            
            List<PlanetDetailsPanel> panels = new ArrayList<PlanetDetailsPanel>();
            
            // first the star
            
            Template temp = cfg.getTemplate("starmain_html.ftl");

            CharArrayWriter out = new CharArrayWriter();

            dft.processTemplate(temp, star, out);
            out.flush();

            BufferedImage image = UIUtils.getImage("Sun");

            image = UIUtils.scaleImage(image, 32, 64);

            PlanetDetailsPanel panel = new PlanetDetailsPanel(image, 
                                        "Star", 
                                        out.toString());

            panel.setName("Star");
                
            panels.add(panel);

            // now do the planets
            
            temp = cfg.getTemplate("planetmain_html.ftl");

            for (Planet planet : this.planets)
            {            
                out = new CharArrayWriter();
            
                dft.processTemplate(temp, planet, this.star, out);
                out.flush();
                
                image = UIUtils.getImage(planet.getType().getPrintText(planet.getType()));
                
                image = UIUtils.scaleImage(image, 64, 64);
                
                panel = new PlanetDetailsPanel(image, 
                                            planet.getType().getPrintText(planet.getType()), 
                                            out.toString());
                
                panel.setName("#" + planet.getNumber());
                
                panels.add(panel);
            }
            
            DetailsFrame details = new DetailsFrame(this.star, this.planets, panels);
            
            details.setVisible(true);
        }
        catch (Exception ex)
        {
            log.error("Error getting details from template", ex);
            
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage() +
                    "\nCheck log file for full details", "Template Error",
                    JOptionPane.ERROR_MESSAGE);
        }        
        
    }//GEN-LAST:event_detailsMenuItemActionPerformed
    
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveMenuItemActionPerformed
        
        final int choice = this.pngChooser.showSaveDialog(this);
        
        try
        {
            if (choice == JFileChooser.APPROVE_OPTION)
            {
                if (
                        !this.pngChooser.getSelectedFile()
                        .isFile() ||
                        (JOptionPane.showConfirmDialog(
                        this,
                        "This file already exists. Do you want to\n" +
                        "overwrite it?", "Replace File?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                {
                    List<BufferedImage> images = UIUtils.getPlanetImages(this.planets);
                    
                    BufferedImage collage = UIUtils.combineImagesHorizontal(images);
                    
                    final File target =
                            FileUtils.addExtension(
                            this.pngChooser.getSelectedFile(), ".png");
                    
                    ImageIO.write(collage, PNGFileFilter.getFormatName(target),
                            target);
                }
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
        
        
    }//GEN-LAST:event_saveMenuItemActionPerformed
    
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeMenuItemActionPerformed
    {//GEN-HEADEREND:event_closeMenuItemActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeMenuItemActionPerformed
    
    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_printMenuItemActionPerformed
    {//GEN-HEADEREND:event_printMenuItemActionPerformed
        
        try
        {
            List<BufferedImage> images = UIUtils.getPlanetImages(this.planets);
            
            BufferedImage collage = UIUtils.combineImagesHorizontal(images);
            
            final PrinterJob printJob = PrinterJob.getPrinterJob();
            final PageFormat pf = printJob.pageDialog(printJob.defaultPage());
            
            printJob.setPrintable(new ImagePrinter(collage, pf), pf);
            
            if (printJob.printDialog())
            {
                printJob.print();
            }
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
        
    }//GEN-LAST:event_printMenuItemActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenuItem detailsMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem keyMenuItem;
    private javax.swing.JTable overviewTable;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JScrollPane systemScrollpane;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Holds value of property planets.
     */
    private List<Planet> planets;
    
    /**
     * Getter for property planets.
     * @return Value of property planets.
     */
    public List<Planet> getPlanets()
    {
        return this.planets;
    }

    /**
     * Holds value of property star.
     */
    private Primary star;

    /**
     * Getter for property star.
     * @return Value of property star.
     */
    public Primary getStar()
    {
        return this.star;
    }
    
    
}
