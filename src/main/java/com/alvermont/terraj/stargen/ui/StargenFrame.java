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
 * StargenFrame.java
 *
 * Created on 20 April 2006, 07:44
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.fracplanet.AllFracplanetParameters;
import com.alvermont.terraj.fracplanet.io.FileUtils;
import com.alvermont.terraj.stargen.Display;
import com.alvermont.terraj.stargen.GenStar;
import com.alvermont.terraj.stargen.Generator;
import com.alvermont.terraj.stargen.Planet;
import com.alvermont.terraj.stargen.Primary;
import com.alvermont.terraj.stargen.StargenParameters;
import com.alvermont.terraj.stargen.util.MathUtils;
import com.alvermont.terraj.util.ui.LookAndFeelUtils;
import com.alvermont.terraj.util.ui.XMLFileFilter;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The main frame for the Stargen user interface
 *
 * @author  martin
 * @version $Id: StargenFrame.java,v 1.16 2006/07/08 12:56:21 martin Exp $
 */
public class StargenFrame extends javax.swing.JFrame
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(StargenFrame.class);
        
    /** The parameters we're using */
    private StargenParameters parameters = new StargenParameters();
    
    /** The file chooser for saving and loading parameters */
    private JFileChooser xmlChooser;
    
    /** The 'about' box */
    private AboutBoxDialog aboutBox;
    
    /** Creates new form StargenFrame */
    public StargenFrame()
    {
        initComponents();
        
        LookAndFeelUtils.getInstance().setSystemLookAndFeel(true, this);
        
        xmlChooser = new JFileChooser();
        xmlChooser.addChoosableFileFilter(new XMLFileFilter());
        
        updateFromParameters(this.parameters);
        validate();
    }
    
    /**
     * Update a linked checkbox and combo box. The combo box will be enabled
     * if the checkbox is selected
     * 
     * @param component1 The checkbox that controls the combobox
     * @param component2 The combobox
     * @param state The new state to set the checkbox to
     * @param value The value to store in the combo box
     */
    protected void updateCombo(JCheckBox component1, JComboBox component2, 
                boolean state, String value)
    {
        component1.setSelected(state);
        component2.setEnabled(state);
        component2.getModel().setSelectedItem(value);
    }

    /**
     * Update a linked checkbox and spinner. The spinner will be enabled
     * if the checkbox is selected
     * 
     * @param component1 The checkbox that controls the spinner
     * @param component2 The spinner
     * @param state The new state to set the checkbox to
     * @param value The value to store in the spinner
     */
    protected void updateSpinner(JCheckBox component1, JSpinner component2, 
                boolean state, int value)
    {
        component1.setSelected(state);
        component2.setEnabled(state);
        component2.setValue(value);
    }
    
    /**
     * Update a linked checkbox and spinner. The spinner will be enabled
     * if the checkbox is selected
     * 
     * @param component1 The checkbox that controls the spinner
     * @param component2 The spinner
     * @param state The new state to set the checkbox to
     * @param value The value to store in the spinner
     */
    protected void updateSpinner(JCheckBox component1, JSpinner component2, 
                boolean state, double value)
    {
        component1.setSelected(state);
        component2.setEnabled(state);
        component2.setValue(value);
    }

    /**
     * Update all the UI components from a parameters object so they match
     * the parameters state
     * 
     * @param parameters The parameters object to update all the components from
     */
    protected void updateFromParameters(StargenParameters parameters)
    {
        // the basic parameters 
        
        nameCheckbox.setSelected(parameters.isNameEnabled());
        nameField.setEnabled(parameters.isNameEnabled());
        nameField.setText(parameters.getName());
        
        updateSpinner(hipCheckbox, hipSpinner, parameters.isHipparcusNumberEnabled(),
                        parameters.getHipparcusNumber());
        
        updateSpinner(massCheckbox, massSpinner, parameters.isMassEnabled(), 
                        parameters.getMass());
        
        updateSpinner(lumCheckbox, lumSpinner, parameters.isLuminosityEnabled(),
                        parameters.getLuminosity());
        
        updateSpinner(seedCheckbox, seedSpinner, parameters.isSeedEnabled(),
                        parameters.getSeed());
        
        // the spectral parameters
        
        updateCombo(classCheckbox, classCombo, parameters.isSpectralClassEnabled(),
                        parameters.getSpectralClass());
        
        updateCombo(subclassCheckbox, subclassCombo, parameters.isSpectralSubclassEnabled(),
                        parameters.getSpectralSubclass() + "");

        updateCombo(lumClassCheckbox, lumClassCombo, parameters.isLuminosityClassEnabled(),
                        parameters.getLuminosityClass());
        
        // the position parameters

        updateSpinner(distanceCheckbox, distanceSpinner, parameters.isDistanceEnabled(),
                        parameters.getDistance());

        updateSpinner(ascensionCheckbox, ascensionSpinner, parameters.isRightAscensionEnabled(),
                        parameters.getRightAscension());

        updateSpinner(declinationCheckbox, declinationSpinner, parameters.isDeclinationEnabled(),
                    parameters.getDeclination());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        generateButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        nameCheckbox = new javax.swing.JCheckBox();
        massCheckbox = new javax.swing.JCheckBox();
        hipCheckbox = new javax.swing.JCheckBox();
        lumCheckbox = new javax.swing.JCheckBox();
        nameField = new javax.swing.JTextField();
        hipSpinner = new javax.swing.JSpinner();
        massSpinner = new javax.swing.JSpinner();
        lumSpinner = new javax.swing.JSpinner();
        seedCheckbox = new javax.swing.JCheckBox();
        seedSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        classCheckbox = new javax.swing.JCheckBox();
        subclassCheckbox = new javax.swing.JCheckBox();
        lumClassCheckbox = new javax.swing.JCheckBox();
        classCombo = new javax.swing.JComboBox();
        subclassCombo = new javax.swing.JComboBox();
        lumClassCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        distanceCheckbox = new javax.swing.JCheckBox();
        ascensionCheckbox = new javax.swing.JCheckBox();
        declinationCheckbox = new javax.swing.JCheckBox();
        distanceSpinner = new javax.swing.JSpinner();
        ascensionSpinner = new javax.swing.JSpinner();
        declinationSpinner = new javax.swing.JSpinner();
        resetButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stargen Solar System Generator");
        generateButton.setText("Generate ...");
        generateButton.setToolTipText("Generate a solar system using the parameters in the dialog");
        generateButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                generateButtonActionPerformed(evt);
            }
        });

        nameCheckbox.setText("Name of Star");
        nameCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        nameCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        nameCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                nameCheckboxStateChanged(evt);
            }
        });

        massCheckbox.setText("Stellar Mass (Sol = 1.0)");
        massCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        massCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        massCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                massCheckboxStateChanged(evt);
            }
        });

        hipCheckbox.setText("Hipparcus Catalogue Number");
        hipCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        hipCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        hipCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                hipCheckboxStateChanged(evt);
            }
        });

        lumCheckbox.setText("Stellar Luminosity (Sol = 1.0)");
        lumCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lumCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lumCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                lumCheckboxStateChanged(evt);
            }
        });

        nameField.setText("Stargen");
        nameField.setToolTipText("The name of the star and the system for use in output");
        nameField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nameFieldActionPerformed(evt);
            }
        });
        nameField.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                nameFieldFocusLost(evt);
            }
        });

        hipSpinner.setModel(new SpinnerNumberModel(500000, 0, 10000000, 1));
        hipSpinner.setToolTipText("Stellar catalogue number used for Celestia output");
        hipSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                hipSpinnerStateChanged(evt);
            }
        });

        massSpinner.setModel(new SpinnerNumberModel(1.0, 0.1, 10000.0, 0.01));
        massSpinner.setToolTipText("The stellar mass compared to our Sun");
        massSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                massSpinnerStateChanged(evt);
            }
        });

        lumSpinner.setModel(new SpinnerNumberModel(1.0, 0.1, 10000.0, 0.01));
        lumSpinner.setToolTipText("The stellar luminosity compared to our Sun");
        lumSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                lumSpinnerStateChanged(evt);
            }
        });

        seedCheckbox.setText("Random number seed");
        seedCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        seedCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        seedCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                seedCheckboxStateChanged(evt);
            }
        });

        seedSpinner.setModel(new SpinnerNumberModel((Number) new Long(0L), (Comparable) new Long(0L), (Comparable) Long.MAX_VALUE, (Number) new Long(1)));
        seedSpinner.setToolTipText("Select a specific seed value. The same seed always generates the same result");
        seedSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                seedSpinnerStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(massCheckbox)
                            .add(hipCheckbox)
                            .add(lumCheckbox)
                            .add(seedCheckbox))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(massSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                    .add(hipSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                                .add(lumSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(seedSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(nameCheckbox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(nameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(nameCheckbox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(hipCheckbox)
                    .add(hipSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(9, 9, 9)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(massCheckbox)
                    .add(massSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lumCheckbox)
                    .add(lumSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(seedCheckbox)
                    .add(seedSpinner))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jTabbedPane1.addTab("Basic", jPanel1);

        classCheckbox.setText("Spectral Class");
        classCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        classCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        classCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                classCheckboxStateChanged(evt);
            }
        });

        subclassCheckbox.setText("Spectral Subclass");
        subclassCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        subclassCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        subclassCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                subclassCheckboxStateChanged(evt);
            }
        });

        lumClassCheckbox.setText("Luminosity Class");
        lumClassCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lumClassCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        lumClassCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                lumClassCheckboxStateChanged(evt);
            }
        });

        classCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "O", "B", "A", "F", "G", "K", "M" }));
        classCombo.setToolTipText("Select the spectral class of the star. O is the hottest.");
        classCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                classComboActionPerformed(evt);
            }
        });

        subclassCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        subclassCombo.setToolTipText("Select the spectral subclass of the star");
        subclassCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                subclassComboActionPerformed(evt);
            }
        });

        lumClassCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ia", "Ib", "II", "III", "IV", "V", "VI", "VII" }));
        lumClassCombo.setToolTipText("Select the luminosity class of the star");
        lumClassCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lumClassComboActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lumClassCheckbox)
                    .add(classCheckbox)
                    .add(subclassCheckbox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 279, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(classCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(subclassCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lumClassCombo, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(16, 16, 16)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(classCheckbox)
                    .add(classCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(subclassCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(subclassCheckbox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lumClassCheckbox)
                    .add(lumClassCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jTabbedPane1.addTab("Spectral", jPanel2);

        distanceCheckbox.setText("Distance (light years)");
        distanceCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        distanceCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        distanceCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                distanceCheckboxStateChanged(evt);
            }
        });

        ascensionCheckbox.setText("Right Ascension (degrees)");
        ascensionCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ascensionCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ascensionCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                ascensionCheckboxStateChanged(evt);
            }
        });

        declinationCheckbox.setText("Declination (degrees)");
        declinationCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        declinationCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        declinationCheckbox.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                declinationCheckboxStateChanged(evt);
            }
        });

        distanceSpinner.setModel(new SpinnerNumberModel(100.0, 0.0, 10000.0, 0.01));
        distanceSpinner.setToolTipText("Set the distance of the star from Earth in light years");
        distanceSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                distanceSpinnerStateChanged(evt);
            }
        });

        ascensionSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 360.0, 0.01));
        ascensionSpinner.setToolTipText("Set the right ascension of the star in degrees");
        ascensionSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                ascensionSpinnerStateChanged(evt);
            }
        });

        declinationSpinner.setModel(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.01));
        declinationSpinner.setToolTipText("Set the declination of the star in degrees");
        declinationSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                declinationSpinnerStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(distanceCheckbox)
                    .add(ascensionCheckbox)
                    .add(declinationCheckbox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 201, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(declinationSpinner)
                    .add(ascensionSpinner)
                    .add(distanceSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(distanceCheckbox)
                    .add(distanceSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ascensionCheckbox)
                    .add(ascensionSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(declinationCheckbox)
                    .add(declinationSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jTabbedPane1.addTab("Position", jPanel3);

        resetButton.setText("Reset All");
        resetButton.setToolTipText("Reset all the parameters to their defaults");
        resetButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resetButtonActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        loadMenuItem.setText("Load Settings ...");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(loadMenuItem);

        saveMenuItem.setText("Save Settings ...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(saveMenuItem);

        jMenu1.add(jSeparator1);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(exitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");
        aboutItem.setText("About ...");
        aboutItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aboutItemActionPerformed(evt);
            }
        });

        jMenu2.add(aboutItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(resetButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 273, Short.MAX_VALUE)
                        .add(generateButton))
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 445, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 194, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(resetButton)
                    .add(generateButton))
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-473)/2, (screenSize.height-300)/2, 473, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutItemActionPerformed
    {//GEN-HEADEREND:event_aboutItemActionPerformed
        
        if (aboutBox == null)
        {
            aboutBox = new AboutBoxDialog(this, false);
        }
        
        aboutBox.setVisible(true);
            
    }//GEN-LAST:event_aboutItemActionPerformed

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadMenuItemActionPerformed
    {//GEN-HEADEREND:event_loadMenuItemActionPerformed
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

                final StargenParameters params =
                    (StargenParameters) decoder.readObject();

                this.parameters = params;
                updateFromParameters(params);
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
    }//GEN-LAST:event_loadMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveMenuItemActionPerformed

        final int choice = this.xmlChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            if (
                !this.xmlChooser.getSelectedFile()
                    .exists() ||
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

                    enc.writeObject(this.parameters);

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
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void declinationSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_declinationSpinnerStateChanged
    {//GEN-HEADEREND:event_declinationSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setDeclination((Double) src.getValue());

    }//GEN-LAST:event_declinationSpinnerStateChanged

    private void ascensionSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_ascensionSpinnerStateChanged
    {//GEN-HEADEREND:event_ascensionSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setRightAscension((Double) src.getValue());

    }//GEN-LAST:event_ascensionSpinnerStateChanged

    private void distanceSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_distanceSpinnerStateChanged
    {//GEN-HEADEREND:event_distanceSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setDistance((Double) src.getValue());        
        
    }//GEN-LAST:event_distanceSpinnerStateChanged

    private void lumClassComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_lumClassComboActionPerformed
    {//GEN-HEADEREND:event_lumClassComboActionPerformed

        JComboBox src = (JComboBox) evt.getSource();
        
        this.parameters.setLuminosityClass((String) (src.getModel().getSelectedItem()));

    }//GEN-LAST:event_lumClassComboActionPerformed

    private void subclassComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_subclassComboActionPerformed
    {//GEN-HEADEREND:event_subclassComboActionPerformed

        JComboBox src = (JComboBox) evt.getSource();
        
        this.parameters.setSpectralSubclass(Integer.parseInt((String) 
                                        (src.getModel().getSelectedItem())));
        
    }//GEN-LAST:event_subclassComboActionPerformed

    private void classComboActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_classComboActionPerformed
    {//GEN-HEADEREND:event_classComboActionPerformed

        JComboBox src = (JComboBox) evt.getSource();
        
        this.parameters.setSpectralClass((String) (src.getModel().getSelectedItem()));
        
    }//GEN-LAST:event_classComboActionPerformed

    private void seedSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_seedSpinnerStateChanged
    {//GEN-HEADEREND:event_seedSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setSeed(((Number) src.getValue()).longValue());

    }//GEN-LAST:event_seedSpinnerStateChanged

    private void lumSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_lumSpinnerStateChanged
    {//GEN-HEADEREND:event_lumSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setMass((Double) src.getValue());        
        
    }//GEN-LAST:event_lumSpinnerStateChanged

    private void massSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_massSpinnerStateChanged
    {//GEN-HEADEREND:event_massSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setMass((Double) src.getValue());

    }//GEN-LAST:event_massSpinnerStateChanged

    private void hipSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_hipSpinnerStateChanged
    {//GEN-HEADEREND:event_hipSpinnerStateChanged

        JSpinner src = (JSpinner) evt.getSource();
        
        this.parameters.setHipparcusNumber((Integer) src.getValue());
        
    }//GEN-LAST:event_hipSpinnerStateChanged

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_nameFieldFocusLost
    {//GEN-HEADEREND:event_nameFieldFocusLost

        this.parameters.setName(nameField.getText());
        
    }//GEN-LAST:event_nameFieldFocusLost

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nameFieldActionPerformed
    {//GEN-HEADEREND:event_nameFieldActionPerformed

        this.parameters.setName(nameField.getText());
        
    }//GEN-LAST:event_nameFieldActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resetButtonActionPerformed
    {//GEN-HEADEREND:event_resetButtonActionPerformed

        this.parameters.reset();
        updateFromParameters(this.parameters);
        
    }//GEN-LAST:event_resetButtonActionPerformed

    private void seedCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_seedCheckboxStateChanged
    {//GEN-HEADEREND:event_seedCheckboxStateChanged

        seedSpinner.setEnabled(seedCheckbox.isSelected());
        this.parameters.setSeedEnabled(seedCheckbox.isSelected());
        
    }//GEN-LAST:event_seedCheckboxStateChanged

    private void declinationCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_declinationCheckboxStateChanged
    {//GEN-HEADEREND:event_declinationCheckboxStateChanged

        declinationSpinner.setEnabled(declinationCheckbox.isSelected());
        this.parameters.setDeclinationEnabled(declinationCheckbox.isSelected());
        
    }//GEN-LAST:event_declinationCheckboxStateChanged

    private void ascensionCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_ascensionCheckboxStateChanged
    {//GEN-HEADEREND:event_ascensionCheckboxStateChanged

        ascensionSpinner.setEnabled(ascensionCheckbox.isSelected());
        this.parameters.setRightAscensionEnabled(ascensionCheckbox.isSelected());
        
    }//GEN-LAST:event_ascensionCheckboxStateChanged

    private void distanceCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_distanceCheckboxStateChanged
    {//GEN-HEADEREND:event_distanceCheckboxStateChanged

        distanceSpinner.setEnabled(distanceCheckbox.isSelected());
        this.parameters.setDistanceEnabled(distanceCheckbox.isSelected());
        
    }//GEN-LAST:event_distanceCheckboxStateChanged

    private void lumClassCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_lumClassCheckboxStateChanged
    {//GEN-HEADEREND:event_lumClassCheckboxStateChanged

        lumClassCombo.setEnabled(lumClassCheckbox.isSelected());
        this.parameters.setLuminosityClassEnabled(lumClassCheckbox.isSelected());
        
    }//GEN-LAST:event_lumClassCheckboxStateChanged

    private void subclassCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_subclassCheckboxStateChanged
    {//GEN-HEADEREND:event_subclassCheckboxStateChanged

        subclassCombo.setEnabled(subclassCheckbox.isSelected());
        this.parameters.setSpectralSubclassEnabled(subclassCheckbox.isSelected());
        
    }//GEN-LAST:event_subclassCheckboxStateChanged

    private void classCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_classCheckboxStateChanged
    {//GEN-HEADEREND:event_classCheckboxStateChanged

        classCombo.setEnabled(classCheckbox.isSelected());
        this.parameters.setSpectralClassEnabled(classCheckbox.isSelected());
        
    }//GEN-LAST:event_classCheckboxStateChanged

    private void lumCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_lumCheckboxStateChanged
    {//GEN-HEADEREND:event_lumCheckboxStateChanged

        lumSpinner.setEnabled(lumCheckbox.isSelected());
        this.parameters.setLuminosityEnabled(lumCheckbox.isSelected());
        
    }//GEN-LAST:event_lumCheckboxStateChanged

    private void massCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_massCheckboxStateChanged
    {//GEN-HEADEREND:event_massCheckboxStateChanged

        massSpinner.setEnabled(massCheckbox.isSelected());
        this.parameters.setMassEnabled(massCheckbox.isSelected());
        
    }//GEN-LAST:event_massCheckboxStateChanged

    private void hipCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_hipCheckboxStateChanged
    {//GEN-HEADEREND:event_hipCheckboxStateChanged

        hipSpinner.setEnabled(hipCheckbox.isSelected());
        this.parameters.setHipparcusNumberEnabled(hipCheckbox.isSelected());
        
    }//GEN-LAST:event_hipCheckboxStateChanged

    private void nameCheckboxStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_nameCheckboxStateChanged
    {//GEN-HEADEREND:event_nameCheckboxStateChanged

        nameField.setEnabled(nameCheckbox.isSelected());
        this.parameters.setNameEnabled(nameCheckbox.isEnabled());
        
    }//GEN-LAST:event_nameCheckboxStateChanged

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitMenuItemActionPerformed
    {//GEN-HEADEREND:event_exitMenuItemActionPerformed

        System.exit(1);
        
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private Primary generateStar(GenStar generator, StargenParameters params)
    {
        Primary sun = null;
        
        if (!params.isSpectralClassEnabled())
        {
            sun = generator.generateStar();
        }
        else
        {
            sun = generator.generateStar(params.getSpectralClass());
        }
        
        sun.setName(params.getName());

        if (params.isHipparcusNumberEnabled())
        {
            sun.setHipparcusNumber(params.getHipparcusNumber());
        }
        else
        {
            sun.setHipparcusNumber(generator.getUtils().nextInt(100000) + 500000);
        }
        
        if (params.isMassEnabled())
        {
            sun.setMass(params.getMass());
        }
        
        if (params.isLuminosityEnabled())
        {
            sun.setLuminosity(params.getLuminosity());
        }
        
        if (params.isSpectralSubclassEnabled())
        {
            sun.setSpectralSubclass(params.getSpectralSubclass());
        }
        
        if (params.isLuminosityClassEnabled())
        {
            sun.setLuminosityClass(params.getLuminosityClass());
        }
        
        if (params.isDistanceEnabled())
        {
            sun.setDistance(params.getDistance());
        }
        else
        {
            sun.setDistance((generator.getUtils().nextDouble() * 1000.0) + 10.0);
        }
        
        if (params.isRightAscensionEnabled())
        {
            sun.setRightAscension(params.getRightAscension());

            sun.setRightAscension(generator.getUtils().nextDouble() * 360.0);
        }
        
        if (params.isDeclinationEnabled())
        {
            sun.setDeclination((generator.getUtils().nextDouble() * 180.0) - 90.0);
        }
        
        return sun;
    }
    
    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_generateButtonActionPerformed
    {//GEN-HEADEREND:event_generateButtonActionPerformed

        MathUtils utils;
        Generator gen;
        final Display display = new Display();

        GenStar generator = null;
        
        if (!this.parameters.isSeedEnabled())
        {
            long seed = System.currentTimeMillis();
            utils = new MathUtils(new Random(seed));
            
            generator = new GenStar(utils);
            gen = new Generator(utils);
            
            this.seedSpinner.setValue(seed);
        }
        else
        {
            utils = new MathUtils(new Random(this.parameters.getSeed()));
            
            generator = new GenStar(utils);
            gen = new Generator(utils);
        }
        
        Primary sun = generateStar(generator, this.parameters);

        gen.setGenerateMoons(true);
        gen.setMoonMinMassLimit(.001);

        gen.generate(sun, sun.getHipparcusNumber(), sun.getName());

        final List<Planet> planets = sun.getPlanets();
        
        SystemFrame system = new SystemFrame(sun, planets);
        
        try
        {
            system.buildImages();
        }
        catch (IOException ioe)
        {
            log.error("Failed to build images: ", ioe);
        }
        
        system.setVisible(true);
        
    }//GEN-LAST:event_generateButtonActionPerformed
    
    /**
     * The main entrypoint for the program
     *
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new StargenFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JCheckBox ascensionCheckbox;
    private javax.swing.JSpinner ascensionSpinner;
    private javax.swing.JCheckBox classCheckbox;
    private javax.swing.JComboBox classCombo;
    private javax.swing.JCheckBox declinationCheckbox;
    private javax.swing.JSpinner declinationSpinner;
    private javax.swing.JCheckBox distanceCheckbox;
    private javax.swing.JSpinner distanceSpinner;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JButton generateButton;
    private javax.swing.JCheckBox hipCheckbox;
    private javax.swing.JSpinner hipSpinner;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JCheckBox lumCheckbox;
    private javax.swing.JCheckBox lumClassCheckbox;
    private javax.swing.JComboBox lumClassCombo;
    private javax.swing.JSpinner lumSpinner;
    private javax.swing.JCheckBox massCheckbox;
    private javax.swing.JSpinner massSpinner;
    private javax.swing.JCheckBox nameCheckbox;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton resetButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JCheckBox seedCheckbox;
    private javax.swing.JSpinner seedSpinner;
    private javax.swing.JCheckBox subclassCheckbox;
    private javax.swing.JComboBox subclassCombo;
    // End of variables declaration//GEN-END:variables
    
}
