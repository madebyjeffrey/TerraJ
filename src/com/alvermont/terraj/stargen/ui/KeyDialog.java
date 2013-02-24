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
 * KeyDialog.java
 *
 * Created on 22 April 2006, 09:57
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.stargen.PlanetType;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Displays the key to planetary data
 *
 * @author  martin
 * @version $Id: KeyDialog.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public class KeyDialog extends javax.swing.JDialog
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(KeyDialog.class);
    
    /**
     * Class for wrapping text in a JTable. This code came from here:
     * http://forum.java.sun.com/thread.jspa?threadID=636469&messageID=3999300
     */
    public class MyCellRenderer extends JTextArea implements TableCellRenderer
    {
        /**
         * Creates a new instance of MyCellRenderer 
         */
        public MyCellRenderer()
        {
            setLineWrap(true);
            setWrapStyleWord(true);
        }
                
        /**
         * Handles firing a property change event
         * 
         * @param propertyName The name of the property that has changed
         * @param oldValue The old value of the property
         * @param newValue The new value of the property
         */
        public void firePropertyChange(String propertyName,
                boolean oldValue,
                boolean newValue)
        {
            if ("lineWrap".equals(propertyName) || "wrapStyleWord".equals(propertyName))
                super.firePropertyChange(propertyName, oldValue, newValue);
        }

        /**
         * Get a renderer for a table cell component 
         *
         * @param table The table to get the renderer for
         * @param value The value to be rendered
         * @param isSelected Whether the cell is selected
         * @param hasFocus Whether the cell has focus
         * @param row The table row number
         * @param column The table column number
         * @return The renderer component for the table cell
         */
        public Component getTableCellRendererComponent(JTable table, Object
                value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            setText(value.toString());//or something in value, like value.getNote()...
            
            setSize(table.getColumnModel().getColumn(column).getWidth(),
                    getPreferredSize().height);
            
            if (table.getRowHeight(row) != getPreferredSize().height)
            {
                table.setRowHeight(row, getPreferredSize().height);
            }
            
            return this;
        }
    }
    
    private class MyTableModel extends DefaultTableModel
    {
        private Class[] classes = { Icon.class, String.class, String.class };
        private String[] names = { "Icon", "Name", "Description" };
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         * @param columnIndex  the column being queried
         * @return the Object.class
         */
        public Class<?> getColumnClass(int columnIndex)
        {
            return classes[columnIndex];
        }
        
        /**
         * Returns true regardless of parameter values.
         *
         * @param row             the row whose value is to be queried
         * @param column          the column whose value is to be queried
         * @return true
         * @see #setValueAt
         */
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
        
        /**
         * Returns the column name.
         *
         * @return a name for this column using the string value of the
         * appropriate member in <code>columnIdentifiers</code>.
         * If <code>columnIdentifiers</code> does not have an entry
         * for this index, returns the default
         * name provided by the superclass
         */
        public String getColumnName(int column)
        {
            return names[column];
        }
        
        /**
         * Returns the number of columns in this data table.
         *
         * @return the number of columns in the model
         */
        public int getColumnCount()
        {
            return classes.length;
        }
        
    }
    
    /**
     * Add an entry to the key 
     *
     * @param type The type of the planet
     * @param description The description of the planet
     */
    protected void addKeyEntry(PlanetType type, String description)
    {
        DefaultTableModel dtm = (DefaultTableModel) keyTable.getModel();
        
        Object[] row = new Object[3];
        
        try
        {
            BufferedImage image = UIUtils.getImage(type.getPrintText());
            
            image = UIUtils.scaleImage(image, 32, 32);
            
            Icon icon = new ImageIcon(image);
            
            row[0] = icon;
            row[1] = type.getPrintText();
            row[2] = description;
            
            dtm.addRow(row);
        }
        catch (IOException ioe)
        {
            log.error("IOException getting icons for key", ioe);
        }
    }
    
    /**
     * Create all the key entries
     */    
    protected void buildKey()
    {
        addKeyEntry(PlanetType.ROCK,
                "These are planets without atmospheres. They can be hot or cold. " +
                "The only requirement is a lack of atmosphere. Examples of \"rocks\" " +
                "in the solar system are Mercury and Pluto.");
        addKeyEntry(PlanetType.ASTEROIDS, "These are small airless bodies. Any " +
                "planet with a mass les than .1% that of Earth is designated an asteroid. " +
                "It is assumed that there is a whole belt of such objects.");
        addKeyEntry(PlanetType.VENUSIAN, "These are planets with a runaway greenhouse " +
                "effect. The defining characteristics are that they have an atmosphere, " +
                "water covering less than 5% of their surface and a surface temperature " + "" +
                "greater than the boiling point of water (and thus actually should " +
                "have no surface water at all).");
        addKeyEntry(PlanetType.TERRESTRIAL,
                "These are Earth-like planets with an atmosphere and a hydrosphere, " +
                "Water covers between 5% and 95% of their surface.");
        addKeyEntry(PlanetType.WATER,
                "These planets have an atmosphere and a hydrosphere. The water covers " +
                "more than 95% of the planet's surface.");
        addKeyEntry(PlanetType.MARTIAN,
                "These planets have a thin atmosphere and little or no surface water. " +
                "Less than 5% of the surface is covered with water and less than 95% with ice.");
        addKeyEntry(PlanetType.ICE,
                "These planets are covered in ice. It covers at least 95% of the surface. " +
                "These are generally planets that would be terrestrials if they were warmer. " +
                "Some are so far out they retain Hydrogen, but are so cold their gases freeze. " +
                "There is no example of this type of planet in the solar system.");
        addKeyEntry(PlanetType.GASGIANT,
                "These are the larger gas giants, represented in the solar system by " +
                "Jupiter and Saturn. They have masses at least 20 times that of the Earth.");
        addKeyEntry(PlanetType.SUBGASGIANT,
                "These are smaller gas giants such as Neptune and Uranus. They have masses less " +
                "than 20 times that of Earth. The cut-off is arbitrary, and based on the " +
                "fact that the blue/green Neptune and Uranus look quite different " +
                "from the striped Juptier and Saturn.");
        addKeyEntry(PlanetType.SUBSUBGASGIANT,
                "These are planets, like the gas giants retain Hydrogen, but are nonetheless mostly rock. " +
                "Their gas mass is less than 20% of their total mass. They tend to be very far from their sun.");
        addKeyEntry(PlanetType.UNKNOWN,
                "This designation is used for planets that are not classifiable as one of the above types. " +
                "This category is always temporary, until the program can be made to classify them.");
    }
    
    /**
     * Creates new form KeyDialog
     *
     * @param parent The parent object of this dialog 
     * @param modal Whether this is a modal dialog or not
     */
    public KeyDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        
        // shouldn't have to hardcode widths like this but jtable is very 
        // annoying and doing anything better is very painful.
        
        keyTable.getColumnModel().getColumn(0).setWidth(40);
        keyTable.getColumnModel().getColumn(0).setMaxWidth(80);
        keyTable.getColumnModel().getColumn(1).setWidth(80);
        keyTable.getColumnModel().getColumn(1).setMaxWidth(160);
        keyTable.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        
        buildKey();
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
        keyTable = new javax.swing.JTable();

        setTitle("Key");
        keyTable.setModel(new MyTableModel());
        keyTable.setAutoCreateColumnsFromModel(false);
        keyTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(keyTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-572)/2, (screenSize.height-506)/2, 572, 506);
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable keyTable;
    // End of variables declaration//GEN-END:variables
    
}
