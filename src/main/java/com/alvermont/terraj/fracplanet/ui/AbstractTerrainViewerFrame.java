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
 * AbstractTerrainViewerFrame.java
 *
 * Created on January 4, 2006, 10:58 AM
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.javascript.tools.shell.ShellGlobal;
import com.alvermont.javascript.tools.shell.ShellJSConsole;
import com.alvermont.javascript.tools.shell.ShellMain;
import com.alvermont.terraj.fracplanet.AllFracplanetParameters;
import com.alvermont.terraj.fracplanet.MeshStats;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrainFlat;
import com.alvermont.terraj.fracplanet.io.EnumDelegate;
import com.alvermont.terraj.fracplanet.io.FileUtils;
import com.alvermont.terraj.fracplanet.io.POVExporter;
import com.alvermont.terraj.fracplanet.render.TriangleMeshViewerDisplay;
import com.alvermont.terraj.fracplanet.rings.RenderTest;
import com.alvermont.terraj.fracplanet.util.DummyProgress;
import com.alvermont.terraj.stargen.util.MathUtils;
import com.alvermont.terraj.util.ui.LookAndFeelUtils;
import com.alvermont.terraj.util.ui.PNGFileFilter;
import com.alvermont.terraj.util.ui.XMLFileFilter;
import com.meghnasoft.async.AbstractAsynchronousAction;
import com.jogamp.opengl.util.awt.Screenshot;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * UI class for terrain display. This is an abstract base class. It contains
 * swing code but not code to initialize rendering and regenerate terrain etc. *
 *
 * @author martin
 * @version $Id: AbstractTerrainViewerFrame.java,v 1.26 2006/07/06 06:58:34 martin Exp $
 */
public abstract class AbstractTerrainViewerFrame extends javax.swing.JFrame
{
    /** Our logging object */
    private static Log log =
        LogFactory.getLog(AbstractTerrainViewerFrame.class);

    // RequireThis OFF: log

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /** The dialog containing all the terrain and rendering controls */
    private ControlsDialog controlsDialog;

    /** The dialog that contains the keyboard help text */
    private ControlsHelpDialog controlsHelpDialog;

    /** Dialog for camera position list */
    private CameraPosDialog cameraPosDialog;

    /** Dialog for the about box */
    private AboutBoxDialog aboutBoxDialog;

    /** File chooser for saving POV files */
    private JFileChooser povChooser = new JFileChooser();

    /** File chooser for saving PNG files */
    private JFileChooser pngChooser = new JFileChooser();

    /** File chooser for saving/loading XML files */
    private JFileChooser xmlChooser = new JFileChooser();

    /** File chooser for saving/loading script files */
    private JFileChooser jsChooser = new JFileChooser();

    /** Object containing all the parameters for controlling the program */
    private AllFracplanetParameters allParams = new AllFracplanetParameters();

    /** Object wrapping up control access for JS */
    private ControlObject controlJS = new ControlObject();

    /** The JavaScript console */
    private ShellJSConsole jsConsole;

    /** Indicates if the mode is in free camera (fly) */
    private boolean isFlyMode = false;

    /** Dummy mesh for when no clouds exist */
    private TriangleMesh dummyMesh;

    /** Action class to do a rings test render */
    protected class RingsTestAction extends AbstractAsynchronousAction
    {
        /**
         * Create a new instance of RingsTestAction
         *
         * @param name The name of the action
         */
        public RingsTestAction(String name)
        {
            super(name);
        }

        /**
         * Execute the action
         *
         * @param e The event associated with this action instigation
         * @return The result of the action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final RenderTest rt = new RenderTest();

            getParameters()
                .getRenderParameters()
                .setCameraPosition(display.getCameraPosition());

            return rt.renderToFrame(display.getMeshes().get(0), getAllParams());
        }

        /**
         * Called when the action has finished
         */
        public void finished()
        {
        }
    }

    /** FileFilter for POV files */
    protected class POVFilter extends FileFilter
    {
        /** Create a new instance of POVFilter */
        public POVFilter()
        {
        }

        /**
         * Test whether a file is to be accepted or not by this filter
         *
         * @param file The file that is to be tested by this filter
         * @return <pre>true</pre> if this file is to be accepted by this
         * filter otherwise <pre>false</pre>
         */
        public boolean accept(File file)
        {
            boolean isAccepted = false;

            if (file.isFile() && file.getName()
                    .toLowerCase()
                    .endsWith(".pov"))
            {
                isAccepted = true;
            }

            return isAccepted;
        }

        /**
         * Return a description of this filter
         *
         * @return A description of this filter as a string
         */
        public String getDescription()
        {
            return "POV files for povray";
        }
    }

    /** FileFilter for Javascript files */
    protected class JSFilter extends FileFilter
    {
        /** Create a new instance of POVFilter */
        public JSFilter()
        {
        }

        /**
         * Test whether a file is to be accepted or not by this filter
         *
         * @param file The file that is to be tested by this filter
         * @return <pre>true</pre> if this file is to be accepted by this
         * filter otherwise <pre>false</pre>
         */
        public boolean accept(File file)
        {
            boolean isAccepted = false;

            if (file.isFile() && file.getName()
                    .toLowerCase()
                    .endsWith(".js"))
            {
                isAccepted = true;
            }

            return isAccepted;
        }

        /**
         * Return a description of this filter
         *
         * @return A description of this filter as a string
         */
        public String getDescription()
        {
            return "Javascript files";
        }
    }

    /**
     * Fill in some objects that we expose to the JavaScript API
     */
    private void populateScope(Scriptable scope)
    {
        TriangleMesh cloudMesh = dummyMesh;

        if (display.getMeshes()
                .size() > 1)
        {
            cloudMesh = display.getMeshes()
                    .get(1);
        }

        final Object jsMesh =
            Context.javaToJS(display.getMeshes().get(0), scope);
        ScriptableObject.putProperty(scope, "mesh", jsMesh);

        final Object jsMesh2 = Context.javaToJS(cloudMesh, scope);
        ScriptableObject.putProperty(scope, "cloudMesh", jsMesh2);

        final Object jsParams = Context.javaToJS(allParams, scope);
        ScriptableObject.putProperty(scope, "params", jsParams);

        final Object jsCamera = Context.javaToJS(display.getCamera(), scope);
        ScriptableObject.putProperty(scope, "camera", jsCamera);

        final Object jsControl = Context.javaToJS(controlJS, scope);
        ScriptableObject.putProperty(scope, "control", jsControl);
    }

    private void setupJSGlobals()
    {
        final ShellGlobal scope = ShellMain.getGlobal();

        final Context cx = Context.enter();

        populateScope(scope);

        cx.exit();
    }

    /** Action class for the console */
    private class ScriptConsoleAction extends AbstractAsynchronousAction
    {
        public ScriptConsoleAction(String name)
        {
            super(name);
        }

        public Object asynchronousActionPerformed(ActionEvent e)
        {
            if (jsConsole == null)
            {
                final ShellGlobal scope = ShellMain.getGlobal();

                final Context cx = Context.enter();

                populateScope(scope);

                cx.exit();

                final String[] args = new String[0];

                jsConsole = new ShellJSConsole(args);

                jsConsole.startShell();
            }

            jsConsole.setVisible(true);

            return this;
        }

        public void finished()
        {
        }
    }

    /** Action class used to run Javascript */
    private class JavaScriptAction extends AbstractAsynchronousAction
    {
        private JFrame parent;

        public JavaScriptAction(String name, JFrame parent)
        {
            super(name);
            this.parent = parent;
        }

        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final int choice = jsChooser.showOpenDialog(parent);
            Object result = null;

            if (choice == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    final File target =
                        FileUtils.addExtension(
                            jsChooser.getSelectedFile(), ".js");

                    final Context cx = Context.enter();

                    Reader reader = null;

                    try
                    {
                        final Scriptable scope = cx.initStandardObjects();

                        populateScope(scope);

                        reader = new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(target)));

                        result = cx.evaluateReader(
                                scope, reader, target.getName(), 1, null);
                    }
                    finally
                    {
                        if (reader != null)
                        {
                            reader.close();
                        }

                        Context.exit();
                    }
                }
                catch (IOException ioe)
                {
                    log.error("Error reading file", ioe);

                    JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ioe.getMessage() +
                        "\nCheck log file for full details", "Error Loading",
                        JOptionPane.ERROR_MESSAGE);
                }
                catch (RhinoException re)
                {
                    log.error("RhinoException", re);

                    JOptionPane.showMessageDialog(
                        parent, "Error: " + re.getMessage(), "Script Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }

            return result;
        }

        public void finished()
        {
        }
    }

    /**
     * Action class used to export POV data. It runs in a new thread by
     * means of AbstractAsynchronousAction
     */
    protected class ExportPOVAction extends AbstractAsynchronousAction
    {
        /** The parent frame of this action */
        private JFrame parent;

        /**
         * Creates a new instance of ExportPOVAction
         *
         * @param parent The parent frame object
         * @param name The name of this action
         */
        public ExportPOVAction(JFrame parent, String name)
        {
            super(name);

            this.parent = parent;
        }

        /**
         * This is the method that carries out the actionPerformed() that
         * a normal action would do when it is executed
         *
         * @param e The ActionEvent associated with triggering this action
         * @return An object representing the result of the action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final int choice = povChooser.showSaveDialog(parent);

            if (choice == JFileChooser.APPROVE_OPTION)
            {
                if (
                    !povChooser.getSelectedFile()
                        .isFile() ||
                        (JOptionPane.showConfirmDialog(
                            parent,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                {
                    try
                    {
                        final POVExporter exporter =
                            new POVExporter(display.getMeshes());

                        final File target =
                            FileUtils.changeExtension(
                                povChooser.getSelectedFile(), ".pov");

                        getParameters()
                            .getRenderParameters()
                            .setCameraPosition(display.getCameraPosition());

                        final ProgressDialog progress =
                            new ProgressDialog(parent, false);

                        progress.setVisible(true);

                        exporter.export(target, getParameters(), progress);

                        progress.setVisible(false);
                        progress.dispose();
                    }
                    catch (IOException ioe)
                    {
                        log.error("Error writing pov file", ioe);

                        JOptionPane.showMessageDialog(
                            parent,
                            "Error: " + ioe.getMessage() +
                            "\nCheck log file for full details", "Error Saving",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            return this;
        }

        /**
         * Called when this action has completed
         */
        public void finished()
        {
        }
    }

    private class MyWindowAdapter extends WindowAdapter
    {
        private JCheckBoxMenuItem item;

        public MyWindowAdapter(JCheckBoxMenuItem item)
        {
            this.item = item;
        }

        public void windowClosing(WindowEvent e)
        {
            super.windowClosing(e);

            item.setSelected(false);
        }
    }

    /**
     * Class that provides access to additional methods for JS that we
     * allow to be used.
     */
    private class ControlObject
    {
        public ControlObject()
        {
        }

        public void recolour()
        {
            display.getMeshes()
                .get(0)
                .doColours(allParams.getTerrainParameters());
        }

        public void forceRedraw()
        {
            display.forceRedraw();
        }

        public void rebuild()
        {
            regenerate();
        }
    }

    /** Method that will be called to initialize the viewer */
    public abstract void initialize();

    /** Method that will be called when the terrain needs to be regenerated */
    public abstract void regenerate();

    /** Method that will be called when the viewer is being shut down */
    public abstract void shutdown();

    /** Method that will be called when the mesh has changed */
    public void meshChanged()
    {
        log.debug("Terrain mesh or global parameters have been changed");

        // update the console global objects
        setupJSGlobals();
    }

    /**
     * Creates new form AbstractTerrainViewerFrame
     */
    public AbstractTerrainViewerFrame()
    {
        // Globally use heavyweight components for all popup menus. Required
        // because GLCanvas is heavyweight
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        initComponents();
        
        // randomize the seeds
        
        Random initRandom = new Random();
        
        this.allParams.getTerrainParameters().setTerrainSeed(initRandom.nextInt());
        this.allParams.getTerrainParameters().setRiversSeed(initRandom.nextInt());
        this.allParams.getCloudParameters().setSeed(initRandom.nextInt());

        // set look and feel as early as is practicable
        
        LookAndFeelUtils.getInstance().setSystemLookAndFeel(true, this);
        
        controlsDialog = new ControlsDialog(this, false);
        cameraPosDialog = new CameraPosDialog(this, false);

        cameraPosDialog.addWindowListener(
            new MyWindowAdapter(showCameraDialogCheckbox));

        povChooser.addChoosableFileFilter(new POVFilter());
        pngChooser.addChoosableFileFilter(new PNGFileFilter());
        xmlChooser.addChoosableFileFilter(new XMLFileFilter());
        jsChooser.addChoosableFileFilter(new JSFilter());

        // TODO: consider writing a dummy mesh class instead of this. The dummy
        // mesh is used by Javascript when no cloud mesh exists
        
        TerrainParameters dummyTP = new TerrainParameters();
        dummyTP.setSubdivisions(1);

        dummyMesh = new TriangleMeshTerrainFlat(
                dummyTP, new DummyProgress(), new MathUtils());

        dummyMesh.getTriangles()
            .clear();
        dummyMesh.getVertices()
            .clear();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        spinSlider = new javax.swing.JSlider();
        tiltSlider = new javax.swing.JSlider();
        terrainPanel = new javax.swing.JPanel();
        controlsButton = new javax.swing.JButton();
        flyButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadParamsItem = new javax.swing.JMenuItem();
        importParamsItem = new javax.swing.JMenuItem();
        saveParamsItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javaScriptItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        exitItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        exportPOVItem = new javax.swing.JMenuItem();
        exportRingsItem = new javax.swing.JMenuItem();
        saveImageItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        showControlsHelpCheckbox = new javax.swing.JCheckBoxMenuItem();
        showCameraDialogCheckbox = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jsConsoleItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Terrain Viewer");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                windowClosingEvent(evt);
            }
        });

        spinSlider.setMajorTickSpacing(200);
        spinSlider.setMaximum(1000);
        spinSlider.setMinimum(-1000);
        spinSlider.setMinorTickSpacing(100);
        spinSlider.setPaintTicks(true);
        spinSlider.setSnapToTicks(true);
        spinSlider.setValue(0);
        spinSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                spinSliderStateChanged(evt);
            }
        });

        tiltSlider.setMajorTickSpacing(10);
        tiltSlider.setMaximum(90);
        tiltSlider.setMinimum(-90);
        tiltSlider.setMinorTickSpacing(5);
        tiltSlider.setOrientation(javax.swing.JSlider.VERTICAL);
        tiltSlider.setPaintLabels(true);
        tiltSlider.setPaintTicks(true);
        tiltSlider.setValue(0);
        tiltSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tiltSliderStateChanged(evt);
            }
        });

        terrainPanel.setPreferredSize(new java.awt.Dimension(0, 0));

        org.jdesktop.layout.GroupLayout terrainPanelLayout = new org.jdesktop.layout.GroupLayout(terrainPanel);
        terrainPanel.setLayout(terrainPanelLayout);
        terrainPanelLayout.setHorizontalGroup(
            terrainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 549, Short.MAX_VALUE)
        );
        terrainPanelLayout.setVerticalGroup(
            terrainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 503, Short.MAX_VALUE)
        );

        controlsButton.setText("Controls ...");
        controlsButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                controlsButtonActionPerformed(evt);
            }
        });

        flyButton.setText("Fly Mode");
        flyButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                flyButtonActionPerformed(evt);
            }
        });

        fileMenu.setText("File");
        fileMenu.setFocusCycleRoot(true);

        loadParamsItem.setText("Load all settings ...");
        loadParamsItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadParamsItemActionPerformed(evt);
            }
        });
        fileMenu.add(loadParamsItem);

        importParamsItem.setText("Import settings ...");
        importParamsItem.setToolTipText("Merge some settings from a file into this one");
        importParamsItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                importParamsItemActionPerformed(evt);
            }
        });
        fileMenu.add(importParamsItem);

        saveParamsItem.setText("Save all settings ...");
        saveParamsItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveParamsItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveParamsItem);
        fileMenu.add(jSeparator1);

        javaScriptItem.setAction(new JavaScriptAction("JavaScript", this));
        javaScriptItem.setText("Run Javascript ...");
        fileMenu.add(javaScriptItem);
        fileMenu.add(jSeparator2);

        exitItem.setText("Exit ...");
        exitItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        jMenuBar1.add(fileMenu);

        exportMenu.setText("Export");

        exportPOVItem.setAction(new ExportPOVAction(this, "ExportPOV"));
        exportPOVItem.setText("Export to POV Format ...");
        exportMenu.add(exportPOVItem);

        exportRingsItem.setAction(new RingsTestAction("Test"));
        exportRingsItem.setText("Export to Rings raytracer ...");
        exportMenu.add(exportRingsItem);

        saveImageItem.setText("Save Screenshot ...");
        saveImageItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveImageItemActionPerformed(evt);
            }
        });
        exportMenu.add(saveImageItem);

        jMenuBar1.add(exportMenu);

        viewMenu.setText("View");

        showControlsHelpCheckbox.setText("Keys Help ...");
        showControlsHelpCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showControlsHelpCheckboxActionPerformed(evt);
            }
        });
        viewMenu.add(showControlsHelpCheckbox);

        showCameraDialogCheckbox.setText("Camera Positions ...");
        showCameraDialogCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showCameraDialogCheckboxActionPerformed(evt);
            }
        });
        viewMenu.add(showCameraDialogCheckbox);
        viewMenu.add(jSeparator3);

        jsConsoleItem.setAction(new ScriptConsoleAction("Console"));
        jsConsoleItem.setText("Javascript Console ...");
        viewMenu.add(jsConsoleItem);

        jMenuBar1.add(viewMenu);

        helpMenu.setText("Help");

        helpMenuItem.setText("About ...");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, spinSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                            .add(terrainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tiltSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(flyButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 427, Short.MAX_VALUE)
                        .add(controlsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(terrainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tiltSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spinSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(controlsButton)
                    .add(flyButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpMenuItemActionPerformed
    {//GEN-HEADEREND:event_helpMenuItemActionPerformed

        if (this.aboutBoxDialog == null)
        {
            this.aboutBoxDialog = new AboutBoxDialog(this, true);
        }

        aboutBoxDialog.setVisible(true);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void importParamsItemActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_importParamsItemActionPerformed
    {//GEN-HEADEREND:event_importParamsItemActionPerformed

        final ImportXMLSettingsDialog dialog =
            new ImportXMLSettingsDialog(this, true);

        try
        {
            dialog.setVisible(true);

            if (dialog.isConfirmed())
            {
                log.debug("Import confirmed");

                try
                {
                    final File target =
                        FileUtils.addExtension(
                            dialog.getSelectedFile(), ".xml");

                    final XMLDecoder decoder =
                        new XMLDecoder(new FileInputStream(target));

                    final AllFracplanetParameters newParams =
                        (AllFracplanetParameters) decoder.readObject();

                    final AllFracplanetParameters params = getParameters();

                    // set the requested items in the parameters
                    if (dialog.isImportTerrain())
                    {
                        params.setTerrainParameters(
                            newParams.getTerrainParameters());
                    }

                    if (dialog.isImportRender())
                    {
                        params.setRenderParameters(
                            newParams.getRenderParameters());
                    }

                    if (dialog.isImportExport())
                    {
                        params.setExportParameters(
                            newParams.getExportParameters());
                    }

                    if (dialog.isImportCamera())
                    {
                        params.setCameraPositionParameters(
                            newParams.getCameraPositionParameters());
                    }

                    // ok, update with any changes to the parameters
                    setParameters(params);

                    display.setRenderParameters(params.getRenderParameters());
                    display.setCameraPosition(
                        params.getRenderParameters().getCameraPosition());
                    cameraPosDialog.setPositions(
                        params.getCameraPositionParameters().getPositions());

                    regenerate();
                }
                catch (IOException ioe)
                {
                    log.error("Error reading file", ioe);

                    JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ioe.getMessage() +
                        "\nCheck log file for full details", "Error Loading",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        finally
        {
            dialog.setVisible(false);
            dialog.dispose();
        }
    }//GEN-LAST:event_importParamsItemActionPerformed

    private void showCameraDialogCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_showCameraDialogCheckboxActionPerformed
    {//GEN-HEADEREND:event_showCameraDialogCheckboxActionPerformed

        final JCheckBoxMenuItem box = (JCheckBoxMenuItem) evt.getSource();

        this.cameraPosDialog.setVisible(box.isSelected());
    }//GEN-LAST:event_showCameraDialogCheckboxActionPerformed

    private void windowClosingEvent(java.awt.event.WindowEvent evt)//GEN-FIRST:event_windowClosingEvent
    {//GEN-HEADEREND:event_windowClosingEvent
        shutdown();
    }//GEN-LAST:event_windowClosingEvent

    private void showControlsHelpCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_showControlsHelpCheckboxActionPerformed
    {//GEN-HEADEREND:event_showControlsHelpCheckboxActionPerformed

        final JCheckBoxMenuItem box = (JCheckBoxMenuItem) evt.getSource();

        if (this.controlsHelpDialog == null)
        {
            this.controlsHelpDialog = new ControlsHelpDialog(this, false);
            this.controlsHelpDialog.addWindowListener(
                new MyWindowAdapter(showControlsHelpCheckbox));
        }

        this.controlsHelpDialog.setVisible(box.isSelected());
    }//GEN-LAST:event_showControlsHelpCheckboxActionPerformed

    private void loadParamsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadParamsItemActionPerformed
    {//GEN-HEADEREND:event_loadParamsItemActionPerformed

        final int choice = this.xmlChooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                final File target =
                    FileUtils.addExtension(
                        this.xmlChooser.getSelectedFile(), ".xml");

                final XMLDecoder decoder =
                    new XMLDecoder(new FileInputStream(target));

                final AllFracplanetParameters params =
                    (AllFracplanetParameters) decoder.readObject();

                setParameters(params);

                display.setRenderParameters(params.getRenderParameters());
                display.setCameraPosition(
                    params.getRenderParameters().getCameraPosition());
                this.cameraPosDialog.setPositions(
                    params.getCameraPositionParameters().getPositions());

                regenerate();
            }
            catch (IOException ioe)
            {
                log.error("Error reading file", ioe);

                JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ioe.getMessage() +
                    "\nCheck log file for full details", "Error Loading",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadParamsItemActionPerformed

    private void saveParamsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveParamsItemActionPerformed
    {//GEN-HEADEREND:event_saveParamsItemActionPerformed

        final int choice = this.xmlChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            if (
                !this.xmlChooser.getSelectedFile()
                    .isFile() ||
                    (JOptionPane.showConfirmDialog(
                        this,
                        "This file already exists. Do you want to\n" +
                        "overwrite it?", "Replace File?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
            {
                try
                {
                    final File target =
                        FileUtils.addExtension(
                            this.xmlChooser.getSelectedFile(), ".xml");

                    final XMLEncoder enc =
                        new XMLEncoder(new FileOutputStream(target));

                    getParameters()
                        .getRenderParameters()
                        .setCameraPosition(display.getCameraPosition());
                    getParameters()
                        .getCameraPositionParameters()
                        .setPositions(this.cameraPosDialog.getPositions());

                    enc.setPersistenceDelegate(
                        TerrainParameters.ObjectTypeEnum.class,
                        new EnumDelegate<TerrainParameters.ObjectTypeEnum>(
                            TerrainParameters.ObjectTypeEnum.class));

                    enc.writeObject(getParameters());

                    enc.close();
                }
                catch (IOException ioe)
                {
                    log.error("Error writing xml file", ioe);

                    JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ioe.getMessage() +
                        "\nCheck log file for full details", "Error Saving",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_saveParamsItemActionPerformed

    private void flyButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_flyButtonActionPerformed
    {//GEN-HEADEREND:event_flyButtonActionPerformed
        this.isFlyMode = !this.isFlyMode;

        if (this.isFlyMode)
        {
            tiltSlider.setValue(0);
            spinSlider.setValue(0);

            flyButton.setText("Exit Fly Mode");
        }
        else
        {
            flyButton.setText("Fly Mode");
        }

        tiltSlider.setEnabled(!this.isFlyMode);
        spinSlider.setEnabled(!this.isFlyMode);

        controlsButton.setEnabled(!this.isFlyMode);

        controlsDialog.setVisible(false);

        if (this.isFlyMode)
        {
            this.display.getCanvas()
                .requestFocus();

            display.beginFly();
        }
        else
        {
            display.endFly();
        }
    }//GEN-LAST:event_flyButtonActionPerformed

    private void saveImageItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveImageItemActionPerformed
    {//GEN-HEADEREND:event_saveImageItemActionPerformed

        final int choice = this.pngChooser.showSaveDialog(this);

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
                try
                {
                    display.getCanvas()
                        .getContext()
                        .makeCurrent();

                    try
                    {
                        final File target =
                            FileUtils.addExtension(
                                this.pngChooser.getSelectedFile(), ".png");

                        Screenshot.writeToFile(
                            target, display.getCanvas().getWidth(),
                            display.getCanvas().getHeight());
                    }
                    finally
                    {
                        display.getCanvas()
                            .getContext()
                            .release();
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
        }
    }//GEN-LAST:event_saveImageItemActionPerformed

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitItemActionPerformed
    {//GEN-HEADEREND:event_exitItemActionPerformed

        if (
            JOptionPane.showConfirmDialog(
                    this, "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            this.setVisible(false);

            System.exit(1);
        }
    }//GEN-LAST:event_exitItemActionPerformed

    private void controlsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_controlsButtonActionPerformed
    {//GEN-HEADEREND:event_controlsButtonActionPerformed
        this.controlsDialog.updateFromRenderParams();
        this.controlsDialog.setVisible(!this.controlsDialog.isVisible());
    }//GEN-LAST:event_controlsButtonActionPerformed

    private void spinSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_spinSliderStateChanged
    {//GEN-HEADEREND:event_spinSliderStateChanged

        final JSlider source = (JSlider) evt.getSource();

        if (!source.getValueIsAdjusting())
        {
            final float value = source.getValue() / 500.0f;

            log.debug("Spin slider set to: " + value);

            display.setObjectRotationSpeed(value);
        }
    }//GEN-LAST:event_spinSliderStateChanged

    private void tiltSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tiltSliderStateChanged
    {//GEN-HEADEREND:event_tiltSliderStateChanged

        final JSlider source = (JSlider) evt.getSource();

        if (!source.getValueIsAdjusting())
        {
            final float value = source.getValue();

            log.debug("Tilt slider set to: " + value);

            display.setObjectTilt(-value);
        }
    }//GEN-LAST:event_tiltSliderStateChanged

    private TriangleMeshViewerDisplay display;

    /**
     * Set the display object being used for rendering by this frame
     *
     * @param display The new object to use for rendering
     */
    public void setDisplay(TriangleMeshViewerDisplay display)
    {
        boolean needsAdd = false;

        if (this.display == null)
        {
            needsAdd = true;
        }

        if (
            (this.display != null) &&
                (this.display.getCanvas() != display.getCanvas()))
        {
            terrainPanel.remove(this.display.getCanvas());
            needsAdd = true;
        }

        this.display = display;

        if (needsAdd)
        {
            // have to force the layout to get round interactions between
            // GL and Swing
            terrainPanel.setLayout(new BorderLayout());
            terrainPanel.add(display.getCanvas(), BorderLayout.CENTER);

            terrainPanel.validate();
        }

        this.cameraPosDialog.setDisplay(display);
    }

    /**
     * Holds value of property meshStats.
     */
    private MeshStats meshStats;

    /**
     * Setter for property meshStats.
     * @param meshStats New value of property meshStats.
     */
    public void setMeshStats(MeshStats meshStats)
    {
        this.meshStats = meshStats;
        this.controlsDialog.setMeshStats(meshStats);
    }

    /**
     * Return the parameters that we are using for our display
     *
     * @return The parameters object that is being used by this object
     */
    public AllFracplanetParameters getParameters()
    {
        return this.getAllParams();
    }

    /**
     * Set the parameters that we are using for our display
     *
     * @param params The new set of parameters to be used
     */
    public void setParameters(AllFracplanetParameters params)
    {
        this.allParams = params;

        this.controlsDialog.updateAll();
        this.cameraPosDialog.setPositions(
            params.getCameraPositionParameters().getPositions());

        // objects need updating
        meshChanged();
    }

    /**
     * Get the parameters object in use by this frame
     *
     * @return The parameters object
     */
    public AllFracplanetParameters getAllParams()
    {
        return allParams;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton controlsButton;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenuItem exportPOVItem;
    private javax.swing.JMenuItem exportRingsItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton flyButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JMenuItem importParamsItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem javaScriptItem;
    private javax.swing.JMenuItem jsConsoleItem;
    private javax.swing.JMenuItem loadParamsItem;
    private javax.swing.JMenuItem saveImageItem;
    private javax.swing.JMenuItem saveParamsItem;
    private javax.swing.JCheckBoxMenuItem showCameraDialogCheckbox;
    private javax.swing.JCheckBoxMenuItem showControlsHelpCheckbox;
    private javax.swing.JSlider spinSlider;
    public javax.swing.JPanel terrainPanel;
    private javax.swing.JSlider tiltSlider;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}
