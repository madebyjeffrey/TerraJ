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
 * ControlsDialog2.java
 *
 * Created on January 5, 2006, 1:12 PM
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.terraj.fracplanet.CloudParameters;
import com.alvermont.terraj.fracplanet.ExportParameters;
import com.alvermont.terraj.fracplanet.MeshStats;
import com.alvermont.terraj.fracplanet.RenderParameters;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.io.ColourFile;
import com.alvermont.terraj.fracplanet.io.FileUtils;
import com.meghnasoft.async.AbstractAsynchronousAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.filechooser.FileFilter;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * UI Class for the display controls
 *
 * @author  martin
 * @version $Id: ControlsDialog.java,v 1.14 2006/07/06 06:58:34 martin Exp $
 */
public class ControlsDialog extends javax.swing.JDialog
{
    /** Our logging object */
    private static Log log =
        LogFactory.getLog(AbstractTerrainViewerFrame.class);

    // RequireThis OFF: log

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /** The file chooser */
    private JFileChooser colChooser = new JFileChooser();

    /** The parent viewer frame of this dialog */
    protected AbstractTerrainViewerFrame parent;

    /** A FileFilter class for RGB files */
    protected class RGBFilter extends FileFilter
    {
        /** Create a new instance of RGBFilter */
        public RGBFilter()
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
            boolean accept = false;

            if (file.isFile() && file.getName()
                    .toLowerCase()
                    .endsWith(".rgb"))
            {
                accept = true;
            }

            return accept;
        }

        /**
         * Return a description of this filter
         *
         * @return A description of this filter as a string
         */
        public String getDescription()
        {
            return "RGB files containing colour information";
        }
    }

    /**
     * Asynchronous action class to regenerate terrain
     */
    private class RegenAction extends AbstractAsynchronousAction
    {
        /**
         * Creates a new instance of RegenAction
         *
         * @param name The name of the action
         */
        public RegenAction(String name)
        {
            super(name);
        }

        /**
         * Called to carry out the work of the action
         *
         * @param e The ActionEvent associated with the triggering of this action
         * @return The object that is the result of this action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            parent.regenerate();

            return this;
        }

        /**
         * Called when the action has completed
         */
        public void finished()
        {
        }
    }

    /**
     * Asynchronous action class to regenerate terrain with a new terrain seed
     */
    private class RegenTerrainAction extends AbstractAsynchronousAction
    {
        /**
         * Creates a new instance of RegenTerrainAction
         *
         * @param name The name of the action
         */
        public RegenTerrainAction(String name)
        {
            super(name);
        }

        /**
         * Called to carry out the work of the action
         *
         * @param e The ActionEvent associated with the triggering of this action
         * @return The object that is the result of this action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final TerrainParameters params =
                parent.getParameters()
                    .getTerrainParameters();

            params.setTerrainSeed(params.getTerrainSeed() + 1);
            terrainSeedSpinner.setValue(params.getTerrainSeed());

            parent.regenerate();

            return this;
        }

        /**
         * Called when the action has completed
         */
        public void finished()
        {
        }
    }

    /**
     * Asynchronous action class to regenerate terrain with a new river seed
     */
    private class RegenRiversAction extends AbstractAsynchronousAction
    {
        /**
         * Creates a new instance of RegenRiversAction
         *
         * @param name The name of the action
         */
        public RegenRiversAction(String name)
        {
            super(name);
        }

        /**
         * Called to carry out the work of the action
         *
         * @param e The ActionEvent associated with the triggering of this action
         * @return The object that is the result of this action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final TerrainParameters params =
                parent.getParameters()
                    .getTerrainParameters();
            params.setRiversSeed(params.getRiversSeed() + 1);
            riverSpinner.setValue(params.getRiversSeed());

            parent.regenerate();

            return this;
        }

        /**
         * Called when the action has completed
         */
        public void finished()
        {
        }
    }

    /**
     * Asynchronous action class to regenerate terrain with a new cloud seed
     */
    private class RegenCloudsAction extends AbstractAsynchronousAction
    {
        /**
         * Creates a new instance of RegenCloudsAction
         *
         * @param name The name of the action
         */
        public RegenCloudsAction(String name)
        {
            super(name);
        }

        /**
         * Called to carry out the work of the action
         *
         * @param e The ActionEvent associated with the triggering of this action
         * @return The object that is the result of this action
         */
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final CloudParameters params =
                parent.getParameters()
                    .getCloudParameters();

            params.setSeed(params.getSeed() + 1);
            cloudsSeedSpinner.setValue(params.getSeed());

            parent.regenerate();

            return this;
        }

        /**
         * Called when the action has completed
         */
        public void finished()
        {
        }
    }

    /**
     * Creates new form ControlsDialog
     *
     * @param parent The parent object for this dialog
     * @param modal Whether this a modal dialog (<pre>true</pre>) or non-modal
     * (<pre>false</pre>)
     */
    public ControlsDialog(AbstractTerrainViewerFrame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        this.parent = parent;

        ambientSpinner.setEditor(
            new JSpinner.NumberEditor(ambientSpinner, "0.000"));

        this.colChooser.addChoosableFileFilter(new RGBFilter());

        updateFromTerrainParams();
        updateFromExportParams();
        updateFromRenderParams();
        updateFromCloudParams();
    }

    /**
     * Update all the controls from the terrain parameters we have been given
     */
    protected void updateFromTerrainParams()
    {
        // terrain basics 
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        if (params.getObjectType() == TerrainParameters.ObjectTypeEnum.PLANET)
        {
            this.terrainTypeCombo.setSelectedItem("Planet");
        }
        else if (
            params.getObjectType() == TerrainParameters.ObjectTypeEnum.TERRAIN_HEXAGON)
        {
            this.terrainTypeCombo.setSelectedItem("Hexagonal Terrain");
        }
        else if (
            params.getObjectType() == TerrainParameters.ObjectTypeEnum.TERRAIN_SQUARE)
        {
            this.terrainTypeCombo.setSelectedItem("Square Terrain");
        }
        else if (
            params.getObjectType() == TerrainParameters.ObjectTypeEnum.TERRAIN_TRIANGLE)
        {
            this.terrainTypeCombo.setSelectedItem("Triangular Terrain");
        }
        else
        {
            this.terrainTypeCombo.setSelectedItem("Terrain");
        }

        this.baseHeightSpinner.setValue(
            (int) (100.0f * params.getBaseHeight()));
        this.powerLawSpinner.setValue((int) (100.0f * params.getPowerLaw()));
        this.terrainSeedSpinner.setValue(params.getTerrainSeed());

        // terrain subdivisions
        this.subdivisionSpinner.setValue(params.getSubdivisions());
        this.unperturbedSpinner.setValue(params.getSubdivisionsUnperturbed());
        this.horizPeturbSpinner.setValue(
            (int) (params.getVariation()
                .getX() * 100.0f));
        this.vertPeturbSpinner.setValue(
            (int) (params.getVariation()
                .getZ() * 100.0f));

        // terrain noise
        this.noiseTermSpinner.setValue(params.getNoiseTerms());
        this.noiseFrequencySpinner.setValue(
            (int) (params.getNoiseFrequency() * 100.0f));
        this.noiseAmplitudeSpinner.setValue(
            (int) (params.getNoiseAmplitude() * 100.0f));
        this.noiseAmplitudeDecaySpinner.setValue(
            (int) (params.getNoiseAmplitudeDecay() * 100.0f));

        // snow
        this.snowEquatorSpinner.setValue(
            (int) (params.getSnowlineEquator() * 100.0f));
        this.snowPoleSpinner.setValue(
            (int) (params.getSnowlinePole() * 100.0f));
        this.snowPowerLawSpinner.setValue(
            (int) (params.getSnowlinePowerLaw() * 100.0f));
        this.snowSlopeSpinner.setValue(
            (int) (params.getSnowlineSlopeEffect() * 100.0f));
        this.snowGlacierSpinner.setValue(
            (int) (params.getSnowlineGlacierEffect() * 100.0f));

        // rivers
        this.riverSpinner.setValue(params.getRivers());
        this.riverSeedSpinner.setValue(params.getRiversSeed());
        this.riverLakeSeaSpinner.setValue(
            (int) (params.getLakeBecomesSea() * 100.0f));

        // colours
        this.lowTerrainButton.setBackground(
            new Color(
                params.getColourLow().getR(), params.getColourLow().getG(),
                params.getColourLow().getB()));

        this.highTerrainButton.setBackground(
            new Color(
                params.getColourHigh().getR(), params.getColourHigh().getG(),
                params.getColourHigh().getB()));

        this.oceanButton.setBackground(
            new Color(
                params.getColourOcean().getR(), params.getColourOcean().getG(),
                params.getColourOcean().getB()));

        this.riverButton.setBackground(
            new Color(
                params.getColourRiver().getR(), params.getColourRiver().getG(),
                params.getColourRiver().getB()));

        this.shorelineButton.setBackground(
            new Color(
                params.getColourShoreline().getR(),
                params.getColourShoreline().getG(),
                params.getColourShoreline().getB()));

        this.snowButton.setBackground(
            new Color(
                params.getColourSnow().getR(), params.getColourSnow().getG(),
                params.getColourSnow().getB()));

        this.oceansEmissiveSpinner.setValue(
            (int) (100.0f * params.getOceansAndRiversEmissive()));
    }

    /**
     * Update all the controls from the export parameters we have been given
     */
    protected void updateFromExportParams()
    {
        final ExportParameters ep =
            this.parent.getParameters()
                .getExportParameters();

        this.exportSeaCheckbox.setSelected(ep.isSeaObject());
        this.exportAtmosphereCheckbox.setSelected(ep.isAtmosphere());
        this.excludeAlternateCheckbox.setSelected(
            ep.isExcludeAlternateColour());
    }

    /**
     * Update all the controls from the cloud parameters we have been given
     */
    protected void updateFromCloudParams()
    {
        final CloudParameters cp =
            this.parent.getParameters()
                .getCloudParameters();

        this.enableCloudsCheckbox.setSelected(cp.isEnabled());
        this.cloudsSubdivisionsCheckbox.setSelected(cp.isUseOwnSubdivisions());
        this.cloudsSubdivisionSpinner.setValue(
            new Integer(cp.getSubdivisions()));
        this.cloudsHeightSpinner.setValue(new Integer(cp.getHeight()));
        this.cloudsSeedSpinner.setValue(cp.getSeed());

        this.cloudButton.setBackground(
            new Color(
                cp.getColour().getR(), cp.getColour().getG(),
                cp.getColour().getB()));
    }

    /**
     * Update all the controls from the render parameters we have been given
     */
    public void updateFromRenderParams()
    {
        final RenderParameters rp =
            this.parent.getParameters()
                .getRenderParameters();

        // render options
        this.wireframeCheckbox.setSelected(rp.isWireframe());
        this.nvidiaHackCheckbox.setSelected(rp.isDisableGLDeleteList());
        this.displayListCheckbox.setSelected(rp.isDisplayList());

        this.framesPerSecondSpinner.setValue((int) rp.getFpsTarget());

        // lighting options
        this.ambientSpinner.setValue(new Double(rp.getAmbient()));

        this.sunlightButton.setBackground(
            new Color(
                rp.getSunColour().getR(), rp.getSunColour().getG(),
                rp.getSunColour().getB()));

        this.enableFogCheckbox.setSelected(rp.isEnableFog());
        this.fogDistanceSpinner.setValue((int) (rp.getFogDistance() * 100.0f));
        this.fogButton.setBackground(
            new Color(
                rp.getFogColour().getR(), rp.getFogColour().getG(),
                rp.getFogColour().getB()));

        this.sunXSlider.setValue((int) rp.getSunPosition().getX());
        this.sunYSlider.setValue((int) rp.getSunPosition().getY());
        this.sunZSlider.setValue((int) rp.getSunPosition().getZ());
    }

    /**
     * Update the mesh stats from the ones that we have been given
     */
    protected void updateFromMeshStats()
    {
        verticesLabel.setText("Total vertices: " + meshStats.getVertices());
        trianglesLabel.setText("Total triangles: " + meshStats.getTriangles());
        landTriangleLabel.setText(
            "Land triangles: " + meshStats.getLandTriangles());
        seaTriangleLabel.setText(
            "Sea triangles: " + meshStats.getSeaTriangles());
    }

    /**
     * Update all the controls and statistics from the parameters we have been given
     */
    public void updateAll()
    {
        updateFromTerrainParams();
        updateFromRenderParams();
        updateFromExportParams();
        updateFromCloudParams();

        updateFromMeshStats();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        terrainButtonGroup = new javax.swing.ButtonGroup();
        regenTerrainButton = new javax.swing.JButton();
        regenRiverButton = new javax.swing.JButton();
        regenButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        basicPanel = new javax.swing.JPanel();
        terrainTypeCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        baseHeightSpinner = new javax.swing.JSpinner();
        terrainSeedSpinner = new javax.swing.JSpinner();
        powerLawSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        subdivPanel = new javax.swing.JPanel();
        subdivisionSpinner = new javax.swing.JSpinner();
        unperturbedSpinner = new javax.swing.JSpinner();
        vertPeturbSpinner = new javax.swing.JSpinner();
        horizPeturbSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        noisePanel = new javax.swing.JPanel();
        noiseTermSpinner = new javax.swing.JSpinner();
        noiseFrequencySpinner = new javax.swing.JSpinner();
        noiseAmplitudeSpinner = new javax.swing.JSpinner();
        noiseAmplitudeDecaySpinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        snowPanel = new javax.swing.JPanel();
        snowEquatorSpinner = new javax.swing.JSpinner();
        snowPoleSpinner = new javax.swing.JSpinner();
        snowPowerLawSpinner = new javax.swing.JSpinner();
        snowSlopeSpinner = new javax.swing.JSpinner();
        snowGlacierSpinner = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        riverPanel = new javax.swing.JPanel();
        riverSpinner = new javax.swing.JSpinner();
        riverSeedSpinner = new javax.swing.JSpinner();
        riverLakeSeaSpinner = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        colourResetButton = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lowTerrainButton = new javax.swing.JButton();
        highTerrainButton = new javax.swing.JButton();
        oceanButton = new javax.swing.JButton();
        riverButton = new javax.swing.JButton();
        shorelineButton = new javax.swing.JButton();
        snowButton = new javax.swing.JButton();
        saveColoursButton = new javax.swing.JButton();
        loadColoursButton = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        oceansEmissiveSpinner = new javax.swing.JSpinner();
        jPanel8 = new javax.swing.JPanel();
        enableCloudsCheckbox = new javax.swing.JCheckBox();
        cloudsSubdivisionsCheckbox = new javax.swing.JCheckBox();
        cloudsSubdivisionSpinner = new javax.swing.JSpinner();
        jLabel35 = new javax.swing.JLabel();
        cloudsSeedSpinner = new javax.swing.JSpinner();
        jLabel36 = new javax.swing.JLabel();
        cloudButton = new javax.swing.JButton();
        resetCloudsButton = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        cloudsHeightSpinner = new javax.swing.JSpinner();
        renderPanel = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        sunlightButton = new javax.swing.JButton();
        sunXSlider = new javax.swing.JSlider();
        sunYSlider = new javax.swing.JSlider();
        sunZSlider = new javax.swing.JSlider();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        resetSunlightButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        fogButton = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        ambientSpinner = new javax.swing.JSpinner();
        resetAmbientButton = new javax.swing.JButton();
        enableFogCheckbox = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        fogDistanceSpinner = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        resetRenderButton = new javax.swing.JButton();
        wireframeCheckbox = new javax.swing.JCheckBox();
        displayListCheckbox = new javax.swing.JCheckBox();
        nvidiaHackCheckbox = new javax.swing.JCheckBox();
        framesPerSecondSpinner = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        exportSeaCheckbox = new javax.swing.JCheckBox();
        exportAtmosphereCheckbox = new javax.swing.JCheckBox();
        excludeAlternateCheckbox = new javax.swing.JCheckBox();
        resetExportButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        verticesLabel = new javax.swing.JLabel();
        trianglesLabel = new javax.swing.JLabel();
        landTriangleLabel = new javax.swing.JLabel();
        seaTriangleLabel = new javax.swing.JLabel();
        regenCloudsButton = new javax.swing.JButton();

        setTitle("Controls");
        regenTerrainButton.setAction(new RegenTerrainAction("RegenTerrain"));
        regenTerrainButton.setText("... with new terrain seed");

        regenRiverButton.setAction(new RegenRiversAction("RegenRivers"));
        regenRiverButton.setText("... with new rivers seed");

        regenButton.setAction(new RegenAction("Regenerate"));
        regenButton.setText("Regenerate");

        jTabbedPane1.setTabLayoutPolicy(
            javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane2.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    jTabbedPane2StateChanged(evt);
                }
            });

        terrainTypeCombo.setModel(
            new javax.swing.DefaultComboBoxModel(
                new String[]
                {
                    "Planet", "Hexagonal Terrain", "Square Terrain",
                    "Triangular Terrain"
                }));
        terrainTypeCombo.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    terrainTypeComboActionPerformed(evt);
                }
            });

        jLabel1.setText("Terrain type");

        baseHeightSpinner.setModel(new SpinnerNumberModel(0, -100, 100, 10));
        baseHeightSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    baseHeightSpinnerStateChanged(evt);
                }
            });

        terrainSeedSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    terrainSeedSpinnerStateChanged(evt);
                }
            });

        powerLawSpinner.setModel(new SpinnerNumberModel(1, 1, 10000, 10));
        powerLawSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    powerLawSpinnerStateChanged(evt);
                }
            });

        jLabel2.setText("Base land height (%)");

        jLabel3.setText("Terrain random seed");

        jLabel4.setText("Power law");

        jButton1.setText("Reset");
        jButton1.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton1ActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout basicPanelLayout =
            new org.jdesktop.layout.GroupLayout(basicPanel);
        basicPanel.setLayout(basicPanelLayout);
        basicPanelLayout.setHorizontalGroup(
            basicPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                basicPanelLayout.createSequentialGroup().addContainerGap().add(
                    basicPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        basicPanelLayout.createSequentialGroup().add(
                            basicPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel1).add(jLabel2).add(jLabel3).add(jLabel4)).add(
                            76, 76, 76).add(
                            basicPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                powerLawSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                167, Short.MAX_VALUE).add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                terrainSeedSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                167, Short.MAX_VALUE).add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                baseHeightSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                167, Short.MAX_VALUE).add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                terrainTypeCombo,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                167,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, jButton1)).addContainerGap()));
        basicPanelLayout.setVerticalGroup(
            basicPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                basicPanelLayout.createSequentialGroup().addContainerGap().add(
                    basicPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(
                        terrainTypeCombo,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    basicPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(
                        baseHeightSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    basicPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(
                        terrainSeedSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    basicPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(
                        powerLawSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 146,
                    Short.MAX_VALUE).add(jButton1).addContainerGap()));
        jTabbedPane2.addTab("Basic", basicPanel);

        subdivisionSpinner.setModel(new SpinnerNumberModel(1, 0, 16, 1));
        subdivisionSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    subdivisionSpinnerStateChanged(evt);
                }
            });

        unperturbedSpinner.setModel(new SpinnerNumberModel(1, 0, 16, 1));
        unperturbedSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    unperturbedSpinnerStateChanged(evt);
                }
            });

        vertPeturbSpinner.setModel(new SpinnerNumberModel(0, 0, 50, 1));
        vertPeturbSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    vertPeturbSpinnerStateChanged(evt);
                }
            });

        horizPeturbSpinner.setModel(new SpinnerNumberModel(0, 0, 25, 1));
        horizPeturbSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    horizPeturbSpinnerStateChanged(evt);
                }
            });

        jLabel5.setText("Total subdivisions");

        jLabel6.setText("Unperturbed subdivisions");

        jLabel7.setText("Vertical peturbation");

        jLabel8.setText("Horizontal peturbation");

        jButton2.setText("Reset");
        jButton2.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton2ActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout subdivPanelLayout =
            new org.jdesktop.layout.GroupLayout(subdivPanel);
        subdivPanel.setLayout(subdivPanelLayout);
        subdivPanelLayout.setHorizontalGroup(
            subdivPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                subdivPanelLayout.createSequentialGroup().addContainerGap().add(
                    subdivPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        subdivPanelLayout.createSequentialGroup().add(
                            subdivPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel5).add(jLabel6).add(jLabel7).add(jLabel8)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 118,
                            Short.MAX_VALUE).add(
                            subdivPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING, false).add(
                                horizPeturbSpinner).add(vertPeturbSpinner).add(
                                unperturbedSpinner).add(
                                subdivisionSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                105, Short.MAX_VALUE))).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, jButton2)).addContainerGap()));
        subdivPanelLayout.setVerticalGroup(
            subdivPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                subdivPanelLayout.createSequentialGroup().addContainerGap().add(
                    subdivPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(
                        subdivisionSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    subdivPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(
                        unperturbedSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    subdivPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(
                        vertPeturbSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    subdivPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8).add(
                        horizPeturbSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 148,
                    Short.MAX_VALUE).add(jButton2).addContainerGap()));
        jTabbedPane2.addTab("Subdivision", subdivPanel);

        noiseTermSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
        noiseTermSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    noiseTermSpinnerStateChanged(evt);
                }
            });

        noiseFrequencySpinner.setModel(new SpinnerNumberModel(0, 0, 10000, 10));
        noiseFrequencySpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    noiseFrequencySpinnerStateChanged(evt);
                }
            });

        noiseAmplitudeSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        noiseAmplitudeSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    noiseAmplitudeSpinnerStateChanged(evt);
                }
            });

        noiseAmplitudeDecaySpinner.setModel(
            new SpinnerNumberModel(0, 0, 100, 10));
        noiseAmplitudeDecaySpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    noiseAmplitudeDecaySpinnerStateChanged(evt);
                }
            });

        jLabel9.setText("Noise terms");

        jLabel10.setText("Noise frequency");

        jLabel11.setText("Noise amplitude");

        jLabel12.setText("Noise amplitude decay");

        jButton3.setText("Reset");
        jButton3.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton3ActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout noisePanelLayout =
            new org.jdesktop.layout.GroupLayout(noisePanel);
        noisePanel.setLayout(noisePanelLayout);
        noisePanelLayout.setHorizontalGroup(
            noisePanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                noisePanelLayout.createSequentialGroup().addContainerGap().add(
                    noisePanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        noisePanelLayout.createSequentialGroup().add(
                            noisePanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel9).add(jLabel10).add(jLabel11).add(
                                jLabel12)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 125,
                            Short.MAX_VALUE).add(
                            noisePanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING, false).add(
                                noiseAmplitudeDecaySpinner).add(
                                noiseAmplitudeSpinner).add(
                                noiseFrequencySpinner).add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                noiseTermSpinner,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                112,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, jButton3)).addContainerGap()));
        noisePanelLayout.setVerticalGroup(
            noisePanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                noisePanelLayout.createSequentialGroup().addContainerGap().add(
                    noisePanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel9).add(
                        noiseTermSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    noisePanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel10).add(
                        noiseFrequencySpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    noisePanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel11).add(
                        noiseAmplitudeSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    noisePanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel12).add(
                        noiseAmplitudeDecaySpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 148,
                    Short.MAX_VALUE).add(jButton3).addContainerGap()));
        jTabbedPane2.addTab("Noise", noisePanel);

        org.jdesktop.layout.GroupLayout jPanel1Layout =
            new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369,
                Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jTabbedPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 316,
                Short.MAX_VALUE));
        jTabbedPane1.addTab("Terrain", jPanel1);

        snowEquatorSpinner.setModel(new SpinnerNumberModel(0, -100, 200, 10));
        snowEquatorSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    snowEquatorSpinnerStateChanged(evt);
                }
            });

        snowPoleSpinner.setModel(new SpinnerNumberModel(0, -100, 200, 10));
        snowPoleSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    snowPoleSpinnerStateChanged(evt);
                }
            });

        snowPowerLawSpinner.setModel(new SpinnerNumberModel(1, 1, 1000, 10));
        snowPowerLawSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    snowPowerLawSpinnerStateChanged(evt);
                }
            });

        snowSlopeSpinner.setModel(new SpinnerNumberModel(0, 0, 10000, 5));
        snowSlopeSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    snowSlopeSpinnerStateChanged(evt);
                }
            });

        snowGlacierSpinner.setModel(new SpinnerNumberModel(0, -1000, 1000, 5));
        snowGlacierSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    snowGlacierSpinnerStateChanged(evt);
                }
            });

        jLabel13.setText("Snowline at equator");

        jLabel14.setText("Snowline at pole");

        jLabel15.setText("Snowline power law");

        jLabel16.setText("Snowline slope suppression");

        jLabel17.setText("Snowline glacier effect");

        jButton4.setText("Reset");
        jButton4.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton4ActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout snowPanelLayout =
            new org.jdesktop.layout.GroupLayout(snowPanel);
        snowPanel.setLayout(snowPanelLayout);
        snowPanelLayout.setHorizontalGroup(
            snowPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                snowPanelLayout.createSequentialGroup().addContainerGap().add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        snowPanelLayout.createSequentialGroup().add(
                            snowPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel13).add(jLabel14).add(jLabel15).add(
                                jLabel16).add(jLabel17)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 115,
                            Short.MAX_VALUE).add(
                            snowPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING, false).add(
                                snowGlacierSpinner).add(snowSlopeSpinner).add(
                                snowPowerLawSpinner).add(snowPoleSpinner).add(
                                snowEquatorSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                104, Short.MAX_VALUE))).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, jButton4)).addContainerGap()));
        snowPanelLayout.setVerticalGroup(
            snowPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                snowPanelLayout.createSequentialGroup().addContainerGap().add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel13).add(
                        snowEquatorSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel14).add(
                        snowPoleSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel15).add(
                        snowPowerLawSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel16).add(
                        snowSlopeSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    snowPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel17).add(
                        snowGlacierSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 147,
                    Short.MAX_VALUE).add(jButton4).addContainerGap()));
        jTabbedPane1.addTab("Snow", snowPanel);

        riverSpinner.setModel(new SpinnerNumberModel(0, 0, 1000000, 100));
        riverSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    riverSpinnerStateChanged(evt);
                }
            });

        riverSeedSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    riverSeedSpinnerStateChanged(evt);
                }
            });

        riverLakeSeaSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        riverLakeSeaSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    riverLakeSeaSpinnerStateChanged(evt);
                }
            });

        jLabel18.setText("Number of rivers");

        jLabel19.setText("River random seed");

        jLabel20.setText("Lake becomes sea");

        jButton5.setText("Reset");
        jButton5.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    jButton5ActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout riverPanelLayout =
            new org.jdesktop.layout.GroupLayout(riverPanel);
        riverPanel.setLayout(riverPanelLayout);
        riverPanelLayout.setHorizontalGroup(
            riverPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                riverPanelLayout.createSequentialGroup().addContainerGap().add(
                    riverPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        riverPanelLayout.createSequentialGroup().add(
                            riverPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel18).add(jLabel19).add(jLabel20)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 155,
                            Short.MAX_VALUE).add(
                            riverPanelLayout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING, false).add(
                                riverLakeSeaSpinner).add(riverSeedSpinner).add(
                                riverSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                104, Short.MAX_VALUE))).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, jButton5)).addContainerGap()));
        riverPanelLayout.setVerticalGroup(
            riverPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                riverPanelLayout.createSequentialGroup().addContainerGap().add(
                    riverPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel18).add(
                        riverSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    riverPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel19).add(
                        riverSeedSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    riverPanelLayout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel20).add(
                        riverLakeSeaSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 199,
                    Short.MAX_VALUE).add(jButton5).addContainerGap()));
        jTabbedPane1.addTab("Rivers", riverPanel);

        colourResetButton.setText("Reset");
        colourResetButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    colourResetButtonActionPerformed(evt);
                }
            });

        jLabel21.setText("Colour of low terrain");

        jLabel22.setText("Colour of high terrain");

        jLabel23.setText("Colour of ocean");

        jLabel24.setText("Colour of rivers");

        jLabel25.setText("Colour of shoreline");

        jLabel26.setText("Colour of snow");

        lowTerrainButton.setText("Select ...");
        lowTerrainButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    lowTerrainButtonActionPerformed(evt);
                }
            });

        highTerrainButton.setText("Select ...");
        highTerrainButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    highTerrainButtonActionPerformed(evt);
                }
            });

        oceanButton.setText("Select ...");
        oceanButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    oceanButtonActionPerformed(evt);
                }
            });

        riverButton.setText("Select ...");
        riverButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    riverButtonActionPerformed(evt);
                }
            });

        shorelineButton.setText("Select ...");
        shorelineButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    shorelineButtonActionPerformed(evt);
                }
            });

        snowButton.setText("Select ...");
        snowButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    snowButtonActionPerformed(evt);
                }
            });

        saveColoursButton.setText("Save ...");
        saveColoursButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    saveColoursButtonActionPerformed(evt);
                }
            });

        loadColoursButton.setText("Load ...");
        loadColoursButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    loadColoursButtonActionPerformed(evt);
                }
            });

        jLabel27.setText("Oceans and rivers emissive");

        oceansEmissiveSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 5));
        oceansEmissiveSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    oceansEmissiveSpinnerStateChanged(evt);
                }
            });

        org.jdesktop.layout.GroupLayout jPanel2Layout =
            new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel2Layout.createSequentialGroup().addContainerGap().add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(jLabel26).add(
                        jLabel24).add(jLabel22).add(jLabel21).add(jLabel25).add(
                        jLabel23).add(
                        jPanel2Layout.createSequentialGroup().add(
                            saveColoursButton).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED).add(
                            loadColoursButton)).add(jLabel27)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 34, Short.MAX_VALUE).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING, false).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, snowButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, riverButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.TRAILING, oceanButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        highTerrainButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        lowTerrainButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 163,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        shorelineButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        oceansEmissiveSpinner).add(
                        colourResetButton,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel2Layout.createSequentialGroup().addContainerGap().add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        lowTerrainButton).add(jLabel21)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        highTerrainButton).add(jLabel22)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        oceanButton).add(jLabel23)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        riverButton).add(jLabel24)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        jLabel25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
                        shorelineButton)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        snowButton).add(jLabel26)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(jLabel27).add(
                        oceansEmissiveSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 77, Short.MAX_VALUE).add(
                    jPanel2Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.TRAILING).add(
                        colourResetButton).add(
                        jPanel2Layout.createParallelGroup(
                            org.jdesktop.layout.GroupLayout.BASELINE).add(
                            saveColoursButton).add(loadColoursButton))).addContainerGap()));
        jTabbedPane1.addTab("Colours", jPanel2);

        enableCloudsCheckbox.setText("Enable clouds");
        enableCloudsCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        enableCloudsCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableCloudsCheckbox.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    enableCloudsCheckboxStateChanged(evt);
                }
            });

        cloudsSubdivisionsCheckbox.setText("Use own subdivisions");
        cloudsSubdivisionsCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cloudsSubdivisionsCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        cloudsSubdivisionsCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    cloudsSubdivisionsCheckboxActionPerformed(evt);
                }
            });
        cloudsSubdivisionsCheckbox.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    cloudsSubdivisionsCheckboxStateChanged(evt);
                }
            });

        cloudsSubdivisionSpinner.setModel(new SpinnerNumberModel(1, 0, 16, 1));
        cloudsSubdivisionSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    cloudsSubdivisionSpinnerStateChanged(evt);
                }
            });

        jLabel35.setText("Clouds random seed");

        cloudsSeedSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    cloudsSeedSpinnerStateChanged(evt);
                }
            });

        jLabel36.setText("Cloud colour");

        cloudButton.setText("Select ...");
        cloudButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    cloudButtonActionPerformed(evt);
                }
            });

        resetCloudsButton.setText("Reset");
        resetCloudsButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    resetCloudsButtonActionPerformed(evt);
                }
            });

        jLabel37.setText("Cloud height");

        cloudsHeightSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        cloudsHeightSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    cloudsHeightSpinnerStateChanged(evt);
                }
            });

        org.jdesktop.layout.GroupLayout jPanel8Layout =
            new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel8Layout.createSequentialGroup().addContainerGap().add(
                    jPanel8Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        enableCloudsCheckbox).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        resetCloudsButton).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        jPanel8Layout.createSequentialGroup().add(jLabel36).add(
                            123, 123, 123).add(
                            cloudButton,
                            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167,
                            Short.MAX_VALUE)).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        jPanel8Layout.createSequentialGroup().add(
                            jPanel8Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel35).add(cloudsSubdivisionsCheckbox).add(
                                jLabel37)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 63,
                            Short.MAX_VALUE).add(
                            jPanel8Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING, false).add(
                                cloudsHeightSpinner).add(
                                cloudsSubdivisionSpinner).add(
                                cloudsSeedSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                167, Short.MAX_VALUE)))).addContainerGap()));
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel8Layout.createSequentialGroup().addContainerGap().add(
                    enableCloudsCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel8Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        cloudsSubdivisionsCheckbox).add(
                        cloudsSubdivisionSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel8Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        cloudsSeedSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
                        jLabel35)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel8Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        cloudsHeightSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(
                        jLabel37)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel8Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        cloudButton).add(
                        jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 149,
                    Short.MAX_VALUE).add(resetCloudsButton).addContainerGap()));
        jTabbedPane1.addTab("Clouds", jPanel8);

        jLabel30.setText("Sunlight colour setting");

        sunlightButton.setText("Select ...");
        sunlightButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    sunlightButtonActionPerformed(evt);
                }
            });

        sunXSlider.setMajorTickSpacing(50);
        sunXSlider.setMinimum(-100);
        sunXSlider.setMinorTickSpacing(25);
        sunXSlider.setPaintLabels(true);
        sunXSlider.setPaintTicks(true);
        sunXSlider.setValue(0);
        sunXSlider.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    sunXSliderStateChanged(evt);
                }
            });

        sunYSlider.setMajorTickSpacing(50);
        sunYSlider.setMinimum(-100);
        sunYSlider.setMinorTickSpacing(25);
        sunYSlider.setPaintLabels(true);
        sunYSlider.setPaintTicks(true);
        sunYSlider.setValue(0);
        sunYSlider.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    sunYSliderStateChanged(evt);
                }
            });

        sunZSlider.setMajorTickSpacing(50);
        sunZSlider.setMinimum(-100);
        sunZSlider.setMinorTickSpacing(25);
        sunZSlider.setPaintLabels(true);
        sunZSlider.setPaintTicks(true);
        sunZSlider.setValue(0);
        sunZSlider.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    sunZSliderStateChanged(evt);
                }
            });

        jLabel31.setLabelFor(sunXSlider);
        jLabel31.setText("Sun X Location");

        jLabel32.setText("Sun Y Location");

        jLabel33.setText("Sun Z Location");

        resetSunlightButton.setText("Reset");
        resetSunlightButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    resetSunlightButtonActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout jPanel6Layout =
            new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel6Layout.createSequentialGroup().addContainerGap().add(
                    jPanel6Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        resetSunlightButton).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        jPanel6Layout.createSequentialGroup().add(jLabel30).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED, 75,
                            Short.MAX_VALUE).add(
                            sunlightButton,
                            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163,
                            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(
                        jPanel6Layout.createSequentialGroup().add(
                            jPanel6Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                jLabel32).add(jLabel33).add(jLabel31)).addPreferredGap(
                            org.jdesktop.layout.LayoutStyle.RELATED).add(
                            jPanel6Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                sunXSlider,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                270, Short.MAX_VALUE).add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                sunZSlider,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                270, Short.MAX_VALUE).add(
                                sunYSlider,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                270, Short.MAX_VALUE)))).addContainerGap()));
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel6Layout.createSequentialGroup().addContainerGap().add(
                    jPanel6Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        sunlightButton).add(jLabel30)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel6Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(jLabel31).add(
                        sunXSlider,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel6Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(jLabel32).add(
                        sunYSlider,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel6Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(jLabel33).add(
                        sunZSlider,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 67, Short.MAX_VALUE).add(
                    resetSunlightButton).addContainerGap()));
        jTabbedPane3.addTab("Sun", jPanel6);

        fogButton.setText("Select ...");
        fogButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    fogButtonActionPerformed(evt);
                }
            });

        jLabel34.setText("Ambient light intensity");

        ambientSpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.001));
        ambientSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    ambientSpinnerStateChanged(evt);
                }
            });

        resetAmbientButton.setText("Reset");
        resetAmbientButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    resetAmbientButtonActionPerformed(evt);
                }
            });

        enableFogCheckbox.setText("Enable fog");
        enableFogCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        enableFogCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        enableFogCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    enableFogCheckboxActionPerformed(evt);
                }
            });

        jLabel29.setText("Fog distance");

        fogDistanceSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        fogDistanceSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    fogDistanceSpinnerStateChanged(evt);
                }
            });

        org.jdesktop.layout.GroupLayout jPanel7Layout =
            new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel7Layout.createSequentialGroup().addContainerGap().add(
                    jPanel7Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        org.jdesktop.layout.GroupLayout.TRAILING,
                        resetAmbientButton).add(
                        jPanel7Layout.createSequentialGroup().add(
                            jPanel7Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                enableFogCheckbox).add(jLabel34).add(jLabel29)).add(
                            25, 25, 25).add(
                            jPanel7Layout.createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING).add(
                                fogDistanceSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                213, Short.MAX_VALUE).add(
                                ambientSpinner,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                213, Short.MAX_VALUE).add(
                                fogButton,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                213, Short.MAX_VALUE)))).addContainerGap()));
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel7Layout.createSequentialGroup().addContainerGap().add(
                    jPanel7Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel34).add(
                        ambientSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel7Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(
                        fogButton).add(enableFogCheckbox)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel7Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel29).add(
                        fogDistanceSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 171,
                    Short.MAX_VALUE).add(resetAmbientButton).addContainerGap()));
        jTabbedPane3.addTab("Ambient", jPanel7);

        resetRenderButton.setText("Reset");
        resetRenderButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    resetRenderButtonActionPerformed(evt);
                }
            });

        wireframeCheckbox.setText("Wireframe rendering only");
        wireframeCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wireframeCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        wireframeCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    wireframeCheckboxActionPerformed(evt);
                }
            });

        displayListCheckbox.setText("Use an OpenGL display list for rendering");
        displayListCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        displayListCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayListCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    displayListCheckboxActionPerformed(evt);
                }
            });

        nvidiaHackCheckbox.setText(
            "Disable glDeleteList() for Linux / NVIDIA issue");
        nvidiaHackCheckbox.setToolTipText(
            "Turning this off Linux with nvidia GL may crash the program");
        nvidiaHackCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        nvidiaHackCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nvidiaHackCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    nvidiaHackCheckboxActionPerformed(evt);
                }
            });

        framesPerSecondSpinner.setModel(new SpinnerNumberModel(50, 1, 100, 1));
        framesPerSecondSpinner.addChangeListener(
            new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent evt)
                {
                    framesPerSecondSpinnerStateChanged(evt);
                }
            });

        jLabel28.setText("Target frames per second");

        org.jdesktop.layout.GroupLayout jPanel4Layout =
            new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel4Layout.createSequentialGroup().addContainerGap().add(
                    wireframeCheckbox).addContainerGap(215, Short.MAX_VALUE)).add(
                jPanel4Layout.createSequentialGroup().addContainerGap().add(
                    displayListCheckbox).addContainerGap(145, Short.MAX_VALUE)).add(
                jPanel4Layout.createSequentialGroup().addContainerGap().add(
                    nvidiaHackCheckbox).addContainerGap(119, Short.MAX_VALUE)).add(
                jPanel4Layout.createSequentialGroup().addContainerGap().add(
                    jLabel28).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    framesPerSecondSpinner,
                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82,
                    org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(
                    144, Short.MAX_VALUE)).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel4Layout.createSequentialGroup().addContainerGap(
                    291, Short.MAX_VALUE).add(resetRenderButton).addContainerGap()));
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel4Layout.createSequentialGroup().addContainerGap().add(
                    wireframeCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    displayListCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    nvidiaHackCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    jPanel4Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel28).add(
                        framesPerSecondSpinner,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(
                    135, 135, 135).add(resetRenderButton).addContainerGap()));
        jTabbedPane3.addTab("Options", jPanel4);

        org.jdesktop.layout.GroupLayout renderPanelLayout =
            new org.jdesktop.layout.GroupLayout(renderPanel);
        renderPanel.setLayout(renderPanelLayout);
        renderPanelLayout.setHorizontalGroup(
            renderPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369,
                Short.MAX_VALUE));
        renderPanelLayout.setVerticalGroup(
            renderPanelLayout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jTabbedPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 316,
                Short.MAX_VALUE));
        jTabbedPane1.addTab(
            "Render", null, renderPanel, "Options affecting rendering");

        exportSeaCheckbox.setText("Export sea as object rather than triangles");
        exportSeaCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        exportSeaCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        exportSeaCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    exportSeaCheckboxActionPerformed(evt);
                }
            });

        exportAtmosphereCheckbox.setText("Output an atmosphere object");
        exportAtmosphereCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        exportAtmosphereCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        exportAtmosphereCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    exportAtmosphereCheckboxActionPerformed(evt);
                }
            });

        excludeAlternateCheckbox.setText("Exclude alternate colour triangles");
        excludeAlternateCheckbox.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        excludeAlternateCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        excludeAlternateCheckbox.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    excludeAlternateCheckboxActionPerformed(evt);
                }
            });

        resetExportButton.setText("Reset");
        resetExportButton.addActionListener(
            new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    resetExportButtonActionPerformed(evt);
                }
            });

        org.jdesktop.layout.GroupLayout jPanel3Layout =
            new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel3Layout.createSequentialGroup().addContainerGap().add(
                    jPanel3Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        exportSeaCheckbox).add(exportAtmosphereCheckbox).add(
                        excludeAlternateCheckbox)).addContainerGap(
                    140, Short.MAX_VALUE)).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel3Layout.createSequentialGroup().addContainerGap(
                    296, Short.MAX_VALUE).add(resetExportButton).addContainerGap()));
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel3Layout.createSequentialGroup().addContainerGap().add(
                    exportSeaCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    exportAtmosphereCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    excludeAlternateCheckbox).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED, 214,
                    Short.MAX_VALUE).add(resetExportButton).addContainerGap()));
        jTabbedPane1.addTab("Export", jPanel3);

        verticesLabel.setText("jLabel29");

        trianglesLabel.setText("jLabel30");

        landTriangleLabel.setText("jLabel29");

        seaTriangleLabel.setText("jLabel30");

        org.jdesktop.layout.GroupLayout jPanel5Layout =
            new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel5Layout.createSequentialGroup().addContainerGap().add(
                    jPanel5Layout.createParallelGroup(
                        org.jdesktop.layout.GroupLayout.LEADING).add(
                        verticesLabel).add(trianglesLabel).add(
                        landTriangleLabel).add(seaTriangleLabel)).addContainerGap(
                    319, Short.MAX_VALUE)));
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel5Layout.createSequentialGroup().addContainerGap().add(
                    verticesLabel).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    trianglesLabel).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    landTriangleLabel).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    seaTriangleLabel).addContainerGap(231, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Stats", jPanel5);

        regenCloudsButton.setAction(new RegenCloudsAction("RegenClouds"));
        regenCloudsButton.setText("... with new clouds seed");

        org.jdesktop.layout.GroupLayout layout =
            new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane()
            .setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                regenRiverButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                374, Short.MAX_VALUE).add(
                regenButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374,
                Short.MAX_VALUE).add(
                jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374,
                Short.MAX_VALUE).add(
                regenTerrainButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                374, Short.MAX_VALUE).add(
                regenCloudsButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                374, Short.MAX_VALUE));
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                layout.createSequentialGroup().add(
                    jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    341, Short.MAX_VALUE).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(regenButton).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    regenRiverButton).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    regenTerrainButton).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(
                    regenCloudsButton)));
        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void cloudsSeedSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cloudsSeedSpinnerStateChanged
    {//GEN-HEADEREND:event_cloudsSeedSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final CloudParameters params =
            this.parent.getParameters()
                .getCloudParameters();

        Integer intval = (Integer) source.getValue();

        params.setSeed(intval);
    }//GEN-LAST:event_cloudsSeedSpinnerStateChanged

    private void enableCloudsCheckboxStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_enableCloudsCheckboxStateChanged
    {//GEN-HEADEREND:event_enableCloudsCheckboxStateChanged

        final CloudParameters cp = parent.getParameters()
                .getCloudParameters();

        cp.setEnabled(enableCloudsCheckbox.isSelected());
    }//GEN-LAST:event_enableCloudsCheckboxStateChanged

    private void terrainSeedSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_terrainSeedSpinnerStateChanged
    {//GEN-HEADEREND:event_terrainSeedSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setTerrainSeed((Integer) source.getValue());
    }//GEN-LAST:event_terrainSeedSpinnerStateChanged

    private void cloudsSubdivisionsCheckboxStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cloudsSubdivisionsCheckboxStateChanged
    {//GEN-HEADEREND:event_cloudsSubdivisionsCheckboxStateChanged

        final CloudParameters cp = parent.getParameters()
                .getCloudParameters();

        cp.setUseOwnSubdivisions(cloudsSubdivisionsCheckbox.isSelected());

        cloudsSubdivisionSpinner.setEnabled(
            cloudsSubdivisionsCheckbox.isEnabled());
    }//GEN-LAST:event_cloudsSubdivisionsCheckboxStateChanged

    private void cloudsSubdivisionsCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_cloudsSubdivisionsCheckboxActionPerformed
    {
//GEN-HEADEREND:event_cloudsSubdivisionsCheckboxActionPerformed
    }//GEN-LAST:event_cloudsSubdivisionsCheckboxActionPerformed

    private void cloudsHeightSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cloudsHeightSpinnerStateChanged
    {//GEN-HEADEREND:event_cloudsHeightSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final CloudParameters params =
            this.parent.getParameters()
                .getCloudParameters();

        params.setHeight((Integer) source.getValue());
    }//GEN-LAST:event_cloudsHeightSpinnerStateChanged

    private void resetCloudsButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetCloudsButtonActionPerformed
    {//GEN-HEADEREND:event_resetCloudsButtonActionPerformed
        parent.getParameters()
            .getCloudParameters()
            .reset();
        updateFromCloudParams();
    }//GEN-LAST:event_resetCloudsButtonActionPerformed

    private void cloudButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cloudButtonActionPerformed
    {//GEN-HEADEREND:event_cloudButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Cloud Colour", cloudButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final CloudParameters params =
                this.parent.getParameters()
                    .getCloudParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setColour(newRGB);
            cloudButton.setBackground(newColour);
        }
    }//GEN-LAST:event_cloudButtonActionPerformed

    private void cloudsSubdivisionSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cloudsSubdivisionSpinnerStateChanged
    {//GEN-HEADEREND:event_cloudsSubdivisionSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final CloudParameters params =
            this.parent.getParameters()
                .getCloudParameters();

        params.setSubdivisions((Integer) source.getValue());
    }//GEN-LAST:event_cloudsSubdivisionSpinnerStateChanged

    private void enableFogCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_enableFogCheckboxActionPerformed
    {//GEN-HEADEREND:event_enableFogCheckboxActionPerformed

        JCheckBox source = (JCheckBox) evt.getSource();

        this.parent.getParameters()
            .getRenderParameters()
            .setEnableFog(source.isSelected());
    }//GEN-LAST:event_enableFogCheckboxActionPerformed

    private void fogDistanceSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_fogDistanceSpinnerStateChanged
    {//GEN-HEADEREND:event_fogDistanceSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();

        float value = ((Integer) fogDistanceSpinner.getValue()) / 100.0f;

        this.parent.getParameters()
            .getRenderParameters()
            .setFogDistance(value);
    }//GEN-LAST:event_fogDistanceSpinnerStateChanged

    private void fogButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fogButtonActionPerformed
    {//GEN-HEADEREND:event_fogButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Fog Colour", fogButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final RenderParameters params =
                this.parent.getParameters()
                    .getRenderParameters();

            FloatRGBA newRGB = new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setFogColour(newRGB);
            fogButton.setBackground(newColour);
        }
    }//GEN-LAST:event_fogButtonActionPerformed

    private void ambientSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_ambientSpinnerStateChanged
    {//GEN-HEADEREND:event_ambientSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();

        Double value = (Double) ambientSpinner.getValue();

        this.parent.getParameters()
            .getRenderParameters()
            .setAmbient(value.floatValue());
    }//GEN-LAST:event_ambientSpinnerStateChanged

    private void resetAmbientButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetAmbientButtonActionPerformed
    {//GEN-HEADEREND:event_resetAmbientButtonActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .resetAmbient();
        updateFromRenderParams();
    }//GEN-LAST:event_resetAmbientButtonActionPerformed

    private void sunZSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sunZSliderStateChanged
    {//GEN-HEADEREND:event_sunZSliderStateChanged

        final JSlider source = (JSlider) evt.getSource();

        if (!source.getValueIsAdjusting())
        {
            int value = (int) source.getValue();

            this.parent.getParameters()
                .getRenderParameters()
                .getSunPosition()
                .setZ(value);
        }
    }//GEN-LAST:event_sunZSliderStateChanged

    private void sunYSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sunYSliderStateChanged
    {//GEN-HEADEREND:event_sunYSliderStateChanged

        final JSlider source = (JSlider) evt.getSource();

        if (!source.getValueIsAdjusting())
        {
            int value = (int) source.getValue();

            this.parent.getParameters()
                .getRenderParameters()
                .getSunPosition()
                .setY(value);
        }
    }//GEN-LAST:event_sunYSliderStateChanged

    private void sunXSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_sunXSliderStateChanged
    {//GEN-HEADEREND:event_sunXSliderStateChanged

        final JSlider source = (JSlider) evt.getSource();

        if (!source.getValueIsAdjusting())
        {
            int value = (int) source.getValue();

            this.parent.getParameters()
                .getRenderParameters()
                .getSunPosition()
                .setX(value);
        }
    }//GEN-LAST:event_sunXSliderStateChanged

    private void resetSunlightButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetSunlightButtonActionPerformed
    {//GEN-HEADEREND:event_resetSunlightButtonActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .resetSunlight();
        updateFromRenderParams();
    }//GEN-LAST:event_resetSunlightButtonActionPerformed

    private void sunlightButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sunlightButtonActionPerformed
    {//GEN-HEADEREND:event_sunlightButtonActionPerformed

        Color newColour =
            JColorChooser.showDialog(
                this, "Choose Ambient Light Colour",
                sunlightButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            float[] comps = newColour.getRGBColorComponents(null);

            final RenderParameters params =
                this.parent.getParameters()
                    .getRenderParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setSunColour(newRGB);
            sunlightButton.setBackground(newColour);
        }
    }//GEN-LAST:event_sunlightButtonActionPerformed

    private void framesPerSecondSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_framesPerSecondSpinnerStateChanged
    {//GEN-HEADEREND:event_framesPerSecondSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();

        this.parent.getParameters()
            .getRenderParameters()
            .setFpsTarget((Integer) framesPerSecondSpinner.getValue());
    }//GEN-LAST:event_framesPerSecondSpinnerStateChanged

    private void excludeAlternateCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_excludeAlternateCheckboxActionPerformed
    {//GEN-HEADEREND:event_excludeAlternateCheckboxActionPerformed
        this.parent.getParameters()
            .getExportParameters()
            .setExcludeAlternateColour(excludeAlternateCheckbox.isSelected());
    }//GEN-LAST:event_excludeAlternateCheckboxActionPerformed

    private void exportAtmosphereCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportAtmosphereCheckboxActionPerformed
    {//GEN-HEADEREND:event_exportAtmosphereCheckboxActionPerformed
        this.parent.getParameters()
            .getExportParameters()
            .setAtmosphere(exportAtmosphereCheckbox.isSelected());
    }//GEN-LAST:event_exportAtmosphereCheckboxActionPerformed

    private void exportSeaCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportSeaCheckboxActionPerformed
    {//GEN-HEADEREND:event_exportSeaCheckboxActionPerformed
        this.parent.getParameters()
            .getExportParameters()
            .setSeaObject(exportSeaCheckbox.isSelected());
    }//GEN-LAST:event_exportSeaCheckboxActionPerformed

    private void nvidiaHackCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_nvidiaHackCheckboxActionPerformed
    {//GEN-HEADEREND:event_nvidiaHackCheckboxActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .setDisableGLDeleteList(nvidiaHackCheckbox.isSelected());
    }//GEN-LAST:event_nvidiaHackCheckboxActionPerformed

    private void displayListCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayListCheckboxActionPerformed
    {//GEN-HEADEREND:event_displayListCheckboxActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .setDisplayList(displayListCheckbox.isSelected());
    }//GEN-LAST:event_displayListCheckboxActionPerformed

    private void wireframeCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_wireframeCheckboxActionPerformed
    {//GEN-HEADEREND:event_wireframeCheckboxActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .setWireframe(wireframeCheckbox.isSelected());
    }//GEN-LAST:event_wireframeCheckboxActionPerformed

    private void resetRenderButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetRenderButtonActionPerformed
    {//GEN-HEADEREND:event_resetRenderButtonActionPerformed
        this.parent.getParameters()
            .getRenderParameters()
            .resetOptions();
        updateFromRenderParams();
    }//GEN-LAST:event_resetRenderButtonActionPerformed

    private void resetExportButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetExportButtonActionPerformed
    {//GEN-HEADEREND:event_resetExportButtonActionPerformed
        this.parent.getParameters()
            .getExportParameters()
            .reset();
        updateFromExportParams();
    }//GEN-LAST:event_resetExportButtonActionPerformed

    private void oceansEmissiveSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_oceansEmissiveSpinnerStateChanged
    {//GEN-HEADEREND:event_oceansEmissiveSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setOceansAndRiversEmissive(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_oceansEmissiveSpinnerStateChanged

    private void loadColoursButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadColoursButtonActionPerformed
    {//GEN-HEADEREND:event_loadColoursButtonActionPerformed

        final int choice = this.colChooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                final TerrainParameters params =
                    this.parent.getParameters()
                        .getTerrainParameters();

                final List<FloatRGBA> cols =
                    ColourFile.readFile(this.colChooser.getSelectedFile());

                if (cols.size() != 6)
                {
                    throw new IOException("Expecting 6 colours in input file");
                }

                params.setColourLow(cols.get(0));
                params.setColourHigh(cols.get(1));
                params.setColourOcean(cols.get(2));
                params.setColourRiver(cols.get(3));
                params.setColourShoreline(cols.get(4));
                params.setColourSnow(cols.get(5));

                updateFromTerrainParams();
            }
            catch (IOException ioe)
            {
                log.error("Error writing colour file", ioe);

                JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ioe.getMessage() +
                    "\nCheck log file for full details", "Error Loading",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadColoursButtonActionPerformed

    private void saveColoursButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveColoursButtonActionPerformed
    {//GEN-HEADEREND:event_saveColoursButtonActionPerformed

        final int choice = colChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            if (
                !colChooser.getSelectedFile()
                    .isFile() ||
                    (JOptionPane.showConfirmDialog(
                        this,
                        "This file already exists. Do you want to\n" +
                        "overwrite it?", "Replace File?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
            {
                try
                {
                    final TerrainParameters params =
                        this.parent.getParameters()
                            .getTerrainParameters();

                    final List<FloatRGBA> cols = new ArrayList<FloatRGBA>();

                    cols.add(params.getColourLow());
                    cols.add(params.getColourHigh());
                    cols.add(params.getColourOcean());
                    cols.add(params.getColourRiver());
                    cols.add(params.getColourShoreline());
                    cols.add(params.getColourSnow());

                    ColourFile.writeFile(
                        cols,
                        FileUtils.addExtension(
                            colChooser.getSelectedFile(), ".rgb"));
                }
                catch (IOException ioe)
                {
                    log.error("Error writing colour file", ioe);

                    JOptionPane.showMessageDialog(
                        this,
                        "Error: " + ioe.getMessage() +
                        "\nCheck log file for full details", "Error Saving",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_saveColoursButtonActionPerformed

    private void snowButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_snowButtonActionPerformed
    {//GEN-HEADEREND:event_snowButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Snow Colour", snowButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setColourSnow(newRGB);
            snowButton.setBackground(newColour);
        }
    }//GEN-LAST:event_snowButtonActionPerformed

    private void shorelineButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shorelineButtonActionPerformed
    {//GEN-HEADEREND:event_shorelineButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Shoreline Colour", shorelineButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setColourShoreline(newRGB);
            shorelineButton.setBackground(newColour);
        }
    }//GEN-LAST:event_shorelineButtonActionPerformed

    private void riverButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_riverButtonActionPerformed
    {//GEN-HEADEREND:event_riverButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose River Colour", riverButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);

            params.setColourRiver(newRGB);
            riverButton.setBackground(newColour);
        }
    }//GEN-LAST:event_riverButtonActionPerformed

    private void oceanButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_oceanButtonActionPerformed
    {//GEN-HEADEREND:event_oceanButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Ocean Colour", oceanButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);

            params.setColourOcean(newRGB);
            oceanButton.setBackground(newColour);
        }
    }//GEN-LAST:event_oceanButtonActionPerformed

    private void highTerrainButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_highTerrainButtonActionPerformed
    {//GEN-HEADEREND:event_highTerrainButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose High Terrain Colour",
                highTerrainButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setColourHigh(newRGB);
            highTerrainButton.setBackground(newColour);
        }
    }//GEN-LAST:event_highTerrainButtonActionPerformed

    private void colourResetButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_colourResetButtonActionPerformed
    {//GEN-HEADEREND:event_colourResetButtonActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetColourParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_colourResetButtonActionPerformed

    private void lowTerrainButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_lowTerrainButtonActionPerformed
    {//GEN-HEADEREND:event_lowTerrainButtonActionPerformed

        final Color newColour =
            JColorChooser.showDialog(
                this, "Choose Low Terrain Colour",
                lowTerrainButton.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            final float[] comps = newColour.getRGBColorComponents(null);

            final TerrainParameters params =
                this.parent.getParameters()
                    .getTerrainParameters();

            final FloatRGBA newRGB =
                new FloatRGBA(comps[0], comps[1], comps[2]);
            params.setColourLow(newRGB);
            lowTerrainButton.setBackground(newColour);
        }
    }//GEN-LAST:event_lowTerrainButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetRiverParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetSnowParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetTerrainNoiseParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetTerrainSubdivParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.resetTerrainBasicParameters();
        updateFromTerrainParams();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void snowGlacierSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_snowGlacierSpinnerStateChanged
    {//GEN-HEADEREND:event_snowGlacierSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();

        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSnowlineGlacierEffect(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_snowGlacierSpinnerStateChanged

    private void snowSlopeSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_snowSlopeSpinnerStateChanged
    {//GEN-HEADEREND:event_snowSlopeSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSnowlineSlopeEffect(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_snowSlopeSpinnerStateChanged

    private void snowPowerLawSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_snowPowerLawSpinnerStateChanged
    {//GEN-HEADEREND:event_snowPowerLawSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSnowlinePowerLaw(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_snowPowerLawSpinnerStateChanged

    private void snowPoleSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_snowPoleSpinnerStateChanged
    {//GEN-HEADEREND:event_snowPoleSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSnowlinePole(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_snowPoleSpinnerStateChanged

    private void snowEquatorSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_snowEquatorSpinnerStateChanged
    {//GEN-HEADEREND:event_snowEquatorSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSnowlineEquator(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_snowEquatorSpinnerStateChanged

    private void riverLakeSeaSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_riverLakeSeaSpinnerStateChanged
    {//GEN-HEADEREND:event_riverLakeSeaSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setLakeBecomesSea(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);
    }//GEN-LAST:event_riverLakeSeaSpinnerStateChanged

    private void riverSeedSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_riverSeedSpinnerStateChanged
    {//GEN-HEADEREND:event_riverSeedSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setRiversSeed((Integer) source.getValue());
    }//GEN-LAST:event_riverSeedSpinnerStateChanged

    private void riverSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_riverSpinnerStateChanged
    {//GEN-HEADEREND:event_riverSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setRivers((Integer) source.getValue());
    }//GEN-LAST:event_riverSpinnerStateChanged

    private void noiseAmplitudeDecaySpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_noiseAmplitudeDecaySpinnerStateChanged
    {//GEN-HEADEREND:event_noiseAmplitudeDecaySpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setNoiseAmplitudeDecay(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);

        if (log.isDebugEnabled())
        {
            log.debug(
                "Noise amplitude decay set to: " +
                params.getNoiseAmplitudeDecay());
        }
    }//GEN-LAST:event_noiseAmplitudeDecaySpinnerStateChanged

    private void noiseAmplitudeSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_noiseAmplitudeSpinnerStateChanged
    {//GEN-HEADEREND:event_noiseAmplitudeSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setNoiseAmplitude(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);

        if (log.isDebugEnabled())
        {
            log.debug("Noise amplitude set to: " + params.getNoiseAmplitude());
        }
    }//GEN-LAST:event_noiseAmplitudeSpinnerStateChanged

    private void noiseFrequencySpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_noiseFrequencySpinnerStateChanged
    {//GEN-HEADEREND:event_noiseFrequencySpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setNoiseFrequency(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);

        if (log.isDebugEnabled())
        {
            log.debug("Noise frequency set to: " + params.getNoiseFrequency());
        }
    }//GEN-LAST:event_noiseFrequencySpinnerStateChanged

    private void noiseTermSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_noiseTermSpinnerStateChanged
    {//GEN-HEADEREND:event_noiseTermSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setNoiseTerms((Integer) source.getValue());
    }//GEN-LAST:event_noiseTermSpinnerStateChanged

    private void horizPeturbSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_horizPeturbSpinnerStateChanged
    {//GEN-HEADEREND:event_horizPeturbSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        final float value =
            (float) (((Integer) source.getValue()).intValue()) / 100.0f;

        params.getVariation()
            .setX(value);

        if (log.isDebugEnabled())
        {
            log.debug(
                "Horizontal peturbation set to: " +
                params.getVariation().getX());
        }
    }//GEN-LAST:event_horizPeturbSpinnerStateChanged

    private void vertPeturbSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_vertPeturbSpinnerStateChanged
    {//GEN-HEADEREND:event_vertPeturbSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        final float value =
            (float) (((Integer) source.getValue()).intValue()) / 100.0f;

        params.getVariation()
            .setZ(value);

        if (log.isDebugEnabled())
        {
            log.debug(
                "Vertical peturbation set to: " + params.getVariation().getZ());
        }
    }//GEN-LAST:event_vertPeturbSpinnerStateChanged

    private void unperturbedSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_unperturbedSpinnerStateChanged
    {//GEN-HEADEREND:event_unperturbedSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSubdivisionsUnperturbed((Integer) source.getValue());

        if (log.isDebugEnabled())
        {
            log.debug("Subdivisions set to: " + params.getSubdivisions());
        }
    }//GEN-LAST:event_unperturbedSpinnerStateChanged

    private void terrainTypeComboActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_terrainTypeComboActionPerformed
    {//GEN-HEADEREND:event_terrainTypeComboActionPerformed

        final JComboBox box = (JComboBox) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        final String selected = (String) box.getSelectedItem();

        if (selected.equals("Planet"))
        {
            params.setObjectType(TerrainParameters.ObjectTypeEnum.PLANET);
        }
        else if (selected.equals("Triangular Terrain"))
        {
            params.setObjectType(
                TerrainParameters.ObjectTypeEnum.TERRAIN_TRIANGLE);
        }
        else if (selected.equals("Square Terrain"))
        {
            params.setObjectType(
                TerrainParameters.ObjectTypeEnum.TERRAIN_SQUARE);
        }
        else
        {
            params.setObjectType(
                TerrainParameters.ObjectTypeEnum.TERRAIN_HEXAGON);
        }
    }//GEN-LAST:event_terrainTypeComboActionPerformed

    private void powerLawSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_powerLawSpinnerStateChanged
    {//GEN-HEADEREND:event_powerLawSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setPowerLaw(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);

        if (log.isDebugEnabled())
        {
            log.debug("Power law set to: " + params.getPowerLaw());
        }
    }//GEN-LAST:event_powerLawSpinnerStateChanged

    private void baseHeightSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_baseHeightSpinnerStateChanged
    {//GEN-HEADEREND:event_baseHeightSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setBaseHeight(
            ((float) ((Integer) source.getValue()).intValue()) / 100.0f);

        if (log.isDebugEnabled())
        {
            log.debug("Base height set to: " + params.getBaseHeight());
        }
    }//GEN-LAST:event_baseHeightSpinnerStateChanged

    private void subdivisionSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_subdivisionSpinnerStateChanged
    {//GEN-HEADEREND:event_subdivisionSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        final TerrainParameters params =
            this.parent.getParameters()
                .getTerrainParameters();

        params.setSubdivisions((Integer) source.getValue());

        if (log.isDebugEnabled())
        {
            log.debug("Subdivisions set to: " + params.getSubdivisions());
        }
    }//GEN-LAST:event_subdivisionSpinnerStateChanged

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jTabbedPane2StateChanged
    {
//GEN-HEADEREND:event_jTabbedPane2StateChanged
    }//GEN-LAST:event_jTabbedPane2StateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner ambientSpinner;
    private javax.swing.JSpinner baseHeightSpinner;
    private javax.swing.JPanel basicPanel;
    private javax.swing.JButton cloudButton;
    private javax.swing.JSpinner cloudsHeightSpinner;
    private javax.swing.JSpinner cloudsSeedSpinner;
    private javax.swing.JSpinner cloudsSubdivisionSpinner;
    private javax.swing.JCheckBox cloudsSubdivisionsCheckbox;
    private javax.swing.JButton colourResetButton;
    private javax.swing.JCheckBox displayListCheckbox;
    private javax.swing.JCheckBox enableCloudsCheckbox;
    private javax.swing.JCheckBox enableFogCheckbox;
    private javax.swing.JCheckBox excludeAlternateCheckbox;
    private javax.swing.JCheckBox exportAtmosphereCheckbox;
    private javax.swing.JCheckBox exportSeaCheckbox;
    private javax.swing.JButton fogButton;
    private javax.swing.JSpinner fogDistanceSpinner;
    private javax.swing.JSpinner framesPerSecondSpinner;
    private javax.swing.JButton highTerrainButton;
    private javax.swing.JSpinner horizPeturbSpinner;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel landTriangleLabel;
    private javax.swing.JButton loadColoursButton;
    private javax.swing.JButton lowTerrainButton;
    private javax.swing.JSpinner noiseAmplitudeDecaySpinner;
    private javax.swing.JSpinner noiseAmplitudeSpinner;
    private javax.swing.JSpinner noiseFrequencySpinner;
    private javax.swing.JPanel noisePanel;
    private javax.swing.JSpinner noiseTermSpinner;
    private javax.swing.JCheckBox nvidiaHackCheckbox;
    private javax.swing.JButton oceanButton;
    private javax.swing.JSpinner oceansEmissiveSpinner;
    private javax.swing.JSpinner powerLawSpinner;
    private javax.swing.JButton regenButton;
    private javax.swing.JButton regenCloudsButton;
    private javax.swing.JButton regenRiverButton;
    private javax.swing.JButton regenTerrainButton;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JButton resetAmbientButton;
    private javax.swing.JButton resetCloudsButton;
    private javax.swing.JButton resetExportButton;
    private javax.swing.JButton resetRenderButton;
    private javax.swing.JButton resetSunlightButton;
    private javax.swing.JButton riverButton;
    private javax.swing.JSpinner riverLakeSeaSpinner;
    private javax.swing.JPanel riverPanel;
    private javax.swing.JSpinner riverSeedSpinner;
    private javax.swing.JSpinner riverSpinner;
    private javax.swing.JButton saveColoursButton;
    private javax.swing.JLabel seaTriangleLabel;
    private javax.swing.JButton shorelineButton;
    private javax.swing.JButton snowButton;
    private javax.swing.JSpinner snowEquatorSpinner;
    private javax.swing.JSpinner snowGlacierSpinner;
    private javax.swing.JPanel snowPanel;
    private javax.swing.JSpinner snowPoleSpinner;
    private javax.swing.JSpinner snowPowerLawSpinner;
    private javax.swing.JSpinner snowSlopeSpinner;
    private javax.swing.JPanel subdivPanel;
    private javax.swing.JSpinner subdivisionSpinner;
    private javax.swing.JSlider sunXSlider;
    private javax.swing.JSlider sunYSlider;
    private javax.swing.JSlider sunZSlider;
    private javax.swing.JButton sunlightButton;
    private javax.swing.ButtonGroup terrainButtonGroup;
    private javax.swing.JSpinner terrainSeedSpinner;
    private javax.swing.JComboBox terrainTypeCombo;
    private javax.swing.JLabel trianglesLabel;
    private javax.swing.JSpinner unperturbedSpinner;
    private javax.swing.JSpinner vertPeturbSpinner;
    private javax.swing.JLabel verticesLabel;
    private javax.swing.JCheckBox wireframeCheckbox;

    // End of variables declaration//GEN-END:variables

    // END OF NETBEANS SWING CODE

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
        updateFromMeshStats();
    }
}
