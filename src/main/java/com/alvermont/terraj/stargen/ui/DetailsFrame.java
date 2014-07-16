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
 * DetailsFrame.java
 *
 * Created on 20 April 2006, 12:45
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.fracplanet.io.FileUtils;
import com.meghnasoft.async.AbstractAsynchronousAction;
import java.awt.event.ActionEvent;
import java.io.CharArrayWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import com.alvermont.terraj.stargen.Planet;
import com.alvermont.terraj.stargen.Primary;
import com.alvermont.terraj.util.io.PrintUtilities;
import com.alvermont.terraj.util.ui.CelestiaFileFilter;
import com.alvermont.terraj.util.ui.FTLFileFilter;
import com.alvermont.terraj.util.ui.HTMLFileFilter;
import com.alvermont.terraj.util.ui.TextFileFilter;
import com.alvermont.terraj.util.ui.ZipFileFilter;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Displays a tabbed details view of a solar system, one planet per tab.
 *
 * @author  martin
 * @version $Id: DetailsFrame.java,v 1.17 2006/07/06 06:58:35 martin Exp $
 */
public class DetailsFrame extends javax.swing.JFrame
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(DetailsFrame.class);
    
    private static JFileChooser zipFileChooser = new JFileChooser();
    private static JFileChooser htmlFileChooser = new JFileChooser();
    private static JFileChooser textFileChooser = new JFileChooser();
    private static JFileChooser ftlFileChooser = new JFileChooser();
    private static JFileChooser sscFileChooser = new JFileChooser();
    
    private File templateFile;
    
    static
    {
        zipFileChooser.addChoosableFileFilter(new ZipFileFilter());
        htmlFileChooser.addChoosableFileFilter(new HTMLFileFilter());
        textFileChooser.addChoosableFileFilter(new TextFileFilter());
        ftlFileChooser.addChoosableFileFilter(new FTLFileFilter());
        sscFileChooser.addChoosableFileFilter(new CelestiaFileFilter());
    }
    
    /**
     * Creates new form DetailsFrame
     *
     * @param star The star that this object is for
     * @param planets The list of planets that this object is for
     * @param panels The details panels to be displayed by this object
     */
    public DetailsFrame(Primary star, List<Planet> planets, List<PlanetDetailsPanel> panels)
    {
        initComponents();
        
        this.planets = planets;
        this.star = star;
        
        if (star.getName() != null && star.getName().trim().length() > 0)
        {
            setTitle("Details of " + star.getName().trim());
        }
        
        for (PlanetDetailsPanel p : panels)
        {
            detailsPane.add(p);
        }
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
                final Configuration cfg = new Configuration();
                // Specify the data source where the template files come from.
                cfg.setClassForTemplateLoading(TemplateTest.class,
                        "/com/alvermont/terraj/stargen/templates/");
                
                // Specify how templates will see the data model. This is an advanced topic...
                // but just use this:
                cfg.setObjectWrapper(new DefaultObjectWrapper());
                
                DetailsFromTemplate dft = new DetailsFromTemplate();
                
                final Template temp = cfg.getTemplate("systemmain_noimg_html.ftl");
                
                final CharArrayWriter out = new CharArrayWriter(30000);
                
                dft.processTemplate(temp, parent.planets, parent.star, out);
                out.flush();
                out.close();

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run()
                    {
                        PrintHTMLDialog phd = new PrintHTMLDialog(parent, false);

                        phd.getEditorPane().setText(out.toString());

                        phd.setVisible(true);
                    }
                });
            }
            catch (Exception ex)
            {
                log.error("Error getting details from template", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Template Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
        
        public void finished()
        {
        }
    }
    
    private class SaveHTMLAction extends AbstractAsynchronousAction
    {
        protected DetailsFrame parent;
        
        public SaveHTMLAction(String name, DetailsFrame parent)
        {
            super(name);
            
            this.parent = parent;
        }
        
        protected void writeTemplate(String templateName, Writer out)
        throws TemplateException, IOException
        {
            Configuration cfg = new Configuration();
            // Specify the data source where the template files come from.
            cfg.setClassForTemplateLoading(TemplateTest.class,
                    "/com/alvermont/terraj/stargen/templates/");
            
            // Specify how templates will see the data model. This is an advanced topic...
            // but just use this:
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            
            DetailsFromTemplate dft = new DetailsFromTemplate();
            
            Template temp = cfg.getTemplate(templateName);
            
            dft.processTemplate(temp, parent.planets, parent.star, out);
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                int choice = htmlFileChooser.showSaveDialog(parent);
                
                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    if (
                            !parent.htmlFileChooser.getSelectedFile()
                            .isFile() ||
                            (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                    {
                        OutputStreamWriter out =
                                new OutputStreamWriter(new
                                FileOutputStream(htmlFileChooser.getSelectedFile()));
                        
                        try
                        {
                            writeTemplate("systemmain_noimg_html.ftl", out);
                            
                            out.flush();
                        }
                        finally
                        {
                            out.close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.error("Error saving HTML", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Error Saving HTML",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
        
        public void finished()
        {
        }        
    }
    
    private class SaveTextAction extends SaveHTMLAction
    {
        public SaveTextAction(String name, DetailsFrame parent)
        {
            super(name, parent);
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                int choice = textFileChooser.showSaveDialog(parent);
                
                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    if (
                            textFileChooser.getSelectedFile()
                            .isFile() ||
                            (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                    {
                        OutputStreamWriter out =
                                new OutputStreamWriter(new
                                FileOutputStream(textFileChooser.getSelectedFile()));
                        
                        try
                        {
                            writeTemplate("systemmain_text.ftl", out);
                            
                            out.flush();
                        }
                        finally
                        {
                            out.close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.error("Error saving Text", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Error Saving Text",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
    }
    
    private class SaveHTMLImagesAction extends SaveHTMLAction
    {
        public SaveHTMLImagesAction(String name, DetailsFrame parent)
        {
            super(name, parent);
        }
        
        protected void copyImagesToZip(ZipOutputStream zos) throws IOException
        {
            // get the image list
            
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(getClass().
                    getResourceAsStream("/com/alvermont/terraj/stargen/images/imagelist.txt")));
            
            try
            {
                String line = null;
                
                do
                {
                    line = br.readLine();
                    
                    if (line != null && line.trim().length() > 0 && !line.startsWith("#"))
                    {
                        line = line.trim();
                        
                        ZipEntry ze = new ZipEntry("images/" + line);
                        zos.putNextEntry(ze);
                        
                        InputStream in = getClass().
                                getResourceAsStream("/com/alvermont/terraj/stargen/images/" +
                                line);
                        
                        try
                        {
                            byte[] buffer = new byte[4096];

                            // copy data to the zip file
                            int len;
                            while ((len = in.read(buffer)) > 0)
                            {
                                zos.write(buffer, 0, len);
                            }                        
                        }
                        finally
                        {
                            in.close();
                            zos.closeEntry();
                        }
                    }
                } while (line != null);
            }
            finally
            {
                br.close();
            }
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                int choice = zipFileChooser.showSaveDialog(parent);
                
                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    if (
                            !zipFileChooser.getSelectedFile()
                            .isFile() ||
                            (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                    {
                        ZipOutputStream zos =
                                new ZipOutputStream(new FileOutputStream(zipFileChooser.getSelectedFile()));
                        
                        try
                        {
                            // first generate the main file
                            
                            ZipEntry ze = new ZipEntry("index.html");
                            
                            zos.putNextEntry(ze);
                            
                            OutputStreamWriter out =
                                    new OutputStreamWriter(zos);
                            
                            writeTemplate("systemmain_html.ftl", out);
                            
                            out.flush();
                            
                            zos.closeEntry();
                            
                            // now the key
                            
                            ze = new ZipEntry("key.html");
                            
                            zos.putNextEntry(ze);
                            
                            out = new OutputStreamWriter(zos);
                            
                            writeTemplate("key_html.ftl", out);
                            
                            out.flush();
                            
                            zos.closeEntry();
                            
                            // now add all the images
                            
                            copyImagesToZip(zos);
                        }
                        finally
                        {
                            zos.close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.error("Error saving HTML", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Error Saving HTML",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
    }
    
    private class CustomSaveAction extends AbstractAsynchronousAction
    {
        protected DetailsFrame parent;
        
        public CustomSaveAction(String name, DetailsFrame parent)
        {
            super(name);
            
            this.parent = parent;
        }
        
        protected void writeTemplate(String templateName, Writer out)
        throws TemplateException, IOException
        {
            Configuration cfg = new Configuration();
            // Specify the data source where the template files come from.
            cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
            
            // Specify how templates will see the data model. This is an advanced topic...
            // but just use this:
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            
            DetailsFromTemplate dft = new DetailsFromTemplate();
            
            Template temp = cfg.getTemplate(templateName);
            
            dft.processTemplate(temp, parent.planets, parent.star, out);
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                int choice = textFileChooser.showSaveDialog(parent);
                
                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    if (
                            textFileChooser.getSelectedFile()
                            .isFile() ||
                            (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                    {
                        OutputStreamWriter out =
                                new OutputStreamWriter(new
                                FileOutputStream(textFileChooser.getSelectedFile()));
                        
                        try
                        {
                            writeTemplate(templateFile.getName(), out);
                            
                            out.flush();
                        }
                        finally
                        {
                            out.close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.error("Error in custom save", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Error in Custom Save",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
        }
        
        public void finished()
        {
        }        
    }

    private class SaveCelestiaAction extends SaveHTMLAction
    {
        public SaveCelestiaAction(String name, DetailsFrame parent)
        {
            super(name, parent);
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            try
            {
                int choice = sscFileChooser.showSaveDialog(parent);
                
                if (choice == JFileChooser.APPROVE_OPTION)
                {
                    if (
                            sscFileChooser.getSelectedFile()
                            .isFile() ||
                            (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                    {
                        // first do the planets
                        
                        OutputStreamWriter out =
                                new OutputStreamWriter(new
                                FileOutputStream(FileUtils.changeExtension(sscFileChooser.
                                getSelectedFile(), ".ssc")));
                        
                        try
                        {
                            writeTemplate("celestia_planets.ftl", out);
                            
                            out.flush();
                        }
                        finally
                        {
                            out.close();
                        }
                        
                        // now the star
                        
                        out =   new OutputStreamWriter(new
                                FileOutputStream(FileUtils.changeExtension(sscFileChooser.
                                getSelectedFile(), ".stc")));
                        
                        try
                        {
                            writeTemplate("celestia_star.ftl", out);
                            
                            out.flush();
                        }
                        finally
                        {
                            out.close();
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                log.error("Error saving Text", ex);
                
                JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ex.getMessage() +
                        "\nCheck log file for full details", "Error Saving Text",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            return this;
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
        printEditor = new javax.swing.JEditorPane();
        detailsPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        exportMenu = new javax.swing.JMenu();
        saveHtmlMenuItem = new javax.swing.JMenuItem();
        saveHTMLImagesMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        saveTextMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        saveCelestiaMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenu2 = new javax.swing.JMenu();
        setTemplateMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        customExportMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        closeMenuItem = new javax.swing.JMenuItem();

        jScrollPane1.setPreferredSize(new java.awt.Dimension(50, 50));
        printEditor.setContentType("text/html");
        jScrollPane1.setViewportView(printEditor);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Solar System Details");

        jMenu1.setText("File");
        jMenuItem1.setAction(new PrintAction("Print ...", this));
        jMenuItem1.setText("Print Preview ...");
        jMenu1.add(jMenuItem1);

        jMenu1.add(jSeparator6);

        exportMenu.setText("Export");
        saveHtmlMenuItem.setAction(new SaveHTMLAction("SaveHTML", this));
        saveHtmlMenuItem.setText("As HTML ...");
        exportMenu.add(saveHtmlMenuItem);

        saveHTMLImagesMenuItem.setAction(new SaveHTMLImagesAction("SaveHTMLImages", this));
        saveHTMLImagesMenuItem.setText("As HTML with Images ...");
        exportMenu.add(saveHTMLImagesMenuItem);

        exportMenu.add(jSeparator3);

        saveTextMenuItem.setAction(new SaveTextAction("SaveText", this));
        saveTextMenuItem.setText("As Text ...");
        exportMenu.add(saveTextMenuItem);

        exportMenu.add(jSeparator1);

        saveCelestiaMenuItem.setAction(new SaveCelestiaAction("SaveCelestia", this));
        saveCelestiaMenuItem.setText("As Celestia Format ...");
        exportMenu.add(saveCelestiaMenuItem);

        exportMenu.add(jSeparator4);

        jMenu2.setText("Custom");
        setTemplateMenuItem.setText("Choose Template ...");
        setTemplateMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                setTemplateMenuItemActionPerformed(evt);
            }
        });

        jMenu2.add(setTemplateMenuItem);

        jMenu2.add(jSeparator2);

        customExportMenuItem.setAction(new CustomSaveAction("Custom Save", this));
        customExportMenuItem.setText("Export Using Template ...");
        customExportMenuItem.setEnabled(false);
        jMenu2.add(customExportMenuItem);

        exportMenu.add(jMenu2);

        jMenu1.add(exportMenu);

        jMenu1.add(jSeparator5);

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

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(detailsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(detailsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-565)/2, (screenSize.height-489)/2, 565, 489);
    }// </editor-fold>//GEN-END:initComponents

    private void setTemplateMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_setTemplateMenuItemActionPerformed
    {//GEN-HEADEREND:event_setTemplateMenuItemActionPerformed

        try
        {
            int choice = ftlFileChooser.showOpenDialog(this);

            if (choice == JFileChooser.APPROVE_OPTION && ftlFileChooser.getSelectedFile() != null)
            {
                customExportMenuItem.setEnabled(false);
                
                templateFile = ftlFileChooser.getSelectedFile();
                
                if (templateFile.exists() && templateFile.canRead())
                {
                    customExportMenuItem.setEnabled(true);
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Error selecting template", ex);

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage() +
                    "\nCheck log file for full details", "Error Selecting Template",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_setTemplateMenuItemActionPerformed
    
    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeMenuItemActionPerformed
    {//GEN-HEADEREND:event_closeMenuItemActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeMenuItemActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenuItem customExportMenuItem;
    private javax.swing.JTabbedPane detailsPane;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JEditorPane printEditor;
    private javax.swing.JMenuItem saveCelestiaMenuItem;
    private javax.swing.JMenuItem saveHTMLImagesMenuItem;
    private javax.swing.JMenuItem saveHtmlMenuItem;
    private javax.swing.JMenuItem saveTextMenuItem;
    private javax.swing.JMenuItem setTemplateMenuItem;
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
