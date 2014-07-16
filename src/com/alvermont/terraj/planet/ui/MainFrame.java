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
 * MainFrame.java
 *
 * Created on 23 January 2006, 08:09
 */
package com.alvermont.terraj.planet.ui;

import com.alvermont.terraj.fracplanet.ui.ProgressDialog;
import com.alvermont.terraj.planet.project.HeightfieldGenerator;
import com.alvermont.terraj.util.ui.XMLFileFilter;
import com.alvermont.terraj.planet.AllPlanetParameters;
import com.alvermont.terraj.planet.io.ImageBuilder;
import com.alvermont.terraj.planet.project.ProjectionManager;
import com.alvermont.terraj.planet.project.Projector;
import com.alvermont.terraj.util.ui.JNLPFileChooser;
import com.alvermont.terraj.util.ui.FormattedTextFieldVerifier;
import com.alvermont.terraj.util.ui.LookAndFeelUtils;
import com.meghnasoft.async.AbstractAsynchronousAction;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.AccessControlException;
import java.text.NumberFormat;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The main user interface for the planet generator
 *
 * @author  martin
 * @version $Id: MainFrame.java,v 1.18 2006/07/06 06:58:35 martin Exp $
 */
public class MainFrame extends javax.swing.JFrame
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(MainFrame.class);

    /** The projection manager object we use */
    ProjectionManager projManager = new ProjectionManager();

    /** The about box dialog */
    private AboutBoxDialog aboutBox;

    /** The parameters we're using to create terrains */
    AllPlanetParameters params;

    /** File chooser for saving/loading XML files */
    protected JNLPFileChooser xmlChooser;

    /** File chooser for saving/loading text files */
    protected JNLPFileChooser textChooser;

    /** File chooser for saving/loading rgb files */
    protected JNLPFileChooser rgbChooser;

    /** Object for random numbers */
    protected Random random = new Random();

    /** Object to access preferences */
    Preferences prefs;

    /** The look and feel changer */
    private LookAndFeelUtils lafUtils = LookAndFeelUtils.getInstance();

    /** Preference name for use of native look and feel */
    public static final String PREF_NAME_NATIVE_LAF = "nativeLAF";

    // NETBEANS SWING CODE USE RELAXED CHECKSTYLE SETTINGS

    /** Creates new form MainFrame */
    public MainFrame()
    {
        initComponents();

        boolean nativeLAF = true;

        try
        {
            prefs = Preferences.userNodeForPackage(MainFrame.class);

            nativeLAF = prefs.getBoolean(PREF_NAME_NATIVE_LAF, true);
        }
        catch (AccessControlException ace)
        {
            log.error("No access to preferences? JNLP?", ace);
        }

        if (!lafUtils.setSystemLookAndFeel(nativeLAF, this))
        {
            JOptionPane.showMessageDialog(
                this, "Couldn't set look and feel", "Error",
                JOptionPane.ERROR_MESSAGE);
        }

        nativeLAFCheckbox.setSelected(nativeLAF);
        
        this.xmlChooser = new JNLPFileChooser(".xml");
        this.textChooser = new JNLPFileChooser(".txt");
        this.rgbChooser = new JNLPFileChooser(".rgb");

        this.params = new AllPlanetParameters();

        setListModel();

        latSpinner.setEditor(new JSpinner.NumberEditor(latSpinner, "00.000"));
        lonSpinner.setEditor(new JSpinner.NumberEditor(lonSpinner, "000.000"));
        vgridSpinner.setEditor(
            new JSpinner.NumberEditor(vgridSpinner, "00.000"));
        hgridSpinner.setEditor(
            new JSpinner.NumberEditor(hgridSpinner, "000.000"));

        // TODO: verify range of valid values for scale
        scaleSpinner.setEditor(new JSpinner.NumberEditor(scaleSpinner, "0.00"));

        seedField.setValue(0.0);
        seedField.setInputVerifier(new FormattedTextFieldVerifier());

        setComboNumericEditor(widthComboBox);
        setComboNumericEditor(heightComboBox);

        updateAllFromParameters();

        xmlChooser.addChoosableFileFilter(new XMLFileFilter());
    }

    /** An action class to generate a terrain */
    private class GenerateAction extends AbstractAsynchronousAction
    {
        private JFrame parent;

        public GenerateAction(JFrame parent, String name)
        {
            super(name);
            this.parent = parent;
        }

        /**
         * Generate an image using a projection
         *
         * @param pd The progress object to be used
         */
        protected void generateProjection(ProgressDialog pd)
        {
            final Projector proj = (Projector) projComboBox.getSelectedItem();

            proj.setParameters(new AllPlanetParameters(params));
            proj.setProgress(pd);

            proj.project();

            pd.setVisible(false);
            
            final ImageBuilder ib = new ImageBuilder();

            final BufferedImage image = ib.getImage(proj);

            final TerrainFrame td = new TerrainFrame(image, lafUtils);

            td.setVisible(true);
            
        }
        
        /**
         * Generate a heightfield output as a text file
         *
         * @param pd The progress object to be used
         */
        protected void generateHeightfield(ProgressDialog pd)
        {
            final HeightfieldGenerator hg = new HeightfieldGenerator();
            
            // create the heightfield data
            
            hg.setParameters(new AllPlanetParameters(params));
            hg.setProgress(pd);

            hg.generate();
            
            pd.setVisible(false);
            
            // now write it out
            
            final int choice = textChooser.showSaveDialog(parent);

            if (choice == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            textChooser.getFileContents().
                            getOutputStream(true))));
                    
                    try
                    {
                        for (int y = 0; y < params.getProjectionParameters().getHeight(); ++y)
                        {
                            for (int x = 0; x < params.getProjectionParameters().getWidth(); ++x)
                            {
                                pw.print(hg.getHeightAt(x, y));
                                pw.print(' ');
                            }
                            
                            pw.println();
                        }
                    }
                    finally
                    {
                        pw.close();
                    }
                }
                catch (IOException ioe)
                {
                    log.error("Error writing file", ioe);

                    JOptionPane.showMessageDialog(
                        parent,
                        "Error: " + ioe.getMessage() +
                        "\nCheck log file for full details", "Error Saving",
                        JOptionPane.ERROR_MESSAGE);
                }
            }            
        }
        
        public Object asynchronousActionPerformed(ActionEvent e)
        {
            final ProgressDialog pd = new ProgressDialog(parent, false);

            pd.setVisible(true);

            // slightly different if we're outputting a heightfield
            
            if (heightfieldCheckbox.isSelected())
            {
                generateHeightfield(pd);
            }
            else
            {
                generateProjection(pd);
            }
            
            pd.setVisible(false);
            pd.dispose();

            return this;
        }

        public void finished()
        {
        }
    }

    /**
     * Pick a random colour
     *
     * @return A randomly chosen colour
     */
    protected Color randomColour()
    {
        final int r = random.nextInt(256);
        final int g = random.nextInt(256);
        final int b = random.nextInt(256);

        return new Color(r, g, b);
    }

    /**
     * Update the parameters on the projection page from our object
     */
    protected void updateProjectParameters()
    {
        Projector proj =
            projManager.findByName(
                params.getProjectionParameters().getProjectionName());

        if (proj == null)
        {
            proj = projManager.getDefaultProjection();
        }

        projComboBox.setSelectedItem(proj);

        latSpinner.setValue(params.getProjectionParameters().getLat());
        lonSpinner.setValue(params.getProjectionParameters().getLon());

        seedField.setValue(params.getPlanetParameters().getSeed());

        if (params.getProjectionParameters()
                .isHeightfield())
        {
            heightfieldCheckbox.setSelected(true);
        }
    }

    /**
     * Update the parameters on the grid page from our object
     */
    protected void updateGridParameters()
    {
        vgridSpinner.setValue(params.getProjectionParameters().getVgrid());
        hgridSpinner.setValue(params.getProjectionParameters().getHgrid());
    }

    /**
     * Update the parameters on the output page from our object
     */
    protected void updateOutputParameters()
    {
        widthComboBox.setSelectedItem(
            params.getProjectionParameters().getWidth());
        heightComboBox.setSelectedItem(
            params.getProjectionParameters().getHeight());
        reverseCheckbox.setSelected(
            params.getProjectionParameters().isReverseBackground());

        scaleSpinner.setValue(params.getProjectionParameters().getScale());
    }

    /**
     * Update the parameters on the colour page from our object
     */
    protected void updateColourParameters()
    {
        final int[][] colours = params.getProjectionParameters()
                .getColors();

        this.oceanColourButton.setBackground(
            new Color(colours[0][0], colours[0][1], colours[0][2]));
        this.shoreColourButton.setBackground(
            new Color(colours[1][0], colours[1][1], colours[1][2]));
        this.lowColourButton.setBackground(
            new Color(colours[2][0], colours[2][1], colours[2][2]));
        this.highColourButton.setBackground(
            new Color(colours[3][0], colours[3][1], colours[3][2]));
        this.mountainColourButton.setBackground(
            new Color(colours[4][0], colours[4][1], colours[4][2]));
        this.rockyColourButton.setBackground(
            new Color(colours[5][0], colours[5][1], colours[5][2]));
        this.peakColourButton.setBackground(
            new Color(colours[6][0], colours[6][1], colours[6][2]));
        this.spaceColourButton.setBackground(
            new Color(colours[7][0], colours[7][1], colours[7][2]));
        this.lineColourButton.setBackground(
            new Color(colours[8][0], colours[8][1], colours[8][2]));
    }

    /**
     * Update the parameters on the options page from our object
     */
    protected void updateOptionsParameters()
    {
        altColourCheckbox.setSelected(
            params.getProjectionParameters().isAltColors());
        laticCheckbox.setSelected(params.getProjectionParameters().isLatic());
        shadeCheckbox.setSelected(params.getProjectionParameters().isDoShade());

        shadeAngleSpinner.setValue(
            params.getProjectionParameters().getShadeAngle());

        final int lighter =
            params.getProjectionParameters()
                .getLighterColours();

        lighterSpinner.setValue(lighter);

        outlineCheckbox.setSelected(
            params.getProjectionParameters().isOutline());
        edgesCheckbox.setSelected(params.getProjectionParameters().isEdges());
    }

    /**
     * Set up our state from that contained in the parameters object
     */
    protected void updateAllFromParameters()
    {
        updateProjectParameters();
        updateGridParameters();
        updateOutputParameters();
        updateColourParameters();
        updateOptionsParameters();
    }

    /**
     * Bring up a colour selector for the specified colour and set the
     * corresponding colour parameter if the user picks a new one
     *
     * @param source The button that was pressed to produce this action
     * @param label The string to be displayed by the colour dialog
     * @param index The index of the colour parameter to be set
     * @return The new colour that was chosen or <code>null</code> if the
     * user cancelled.
     */
    protected Color pickColour(JButton source, String label, int index)
    {
        final Color newColour =
            JColorChooser.showDialog(this, label, source.getBackground());

        if (newColour != null)
        {
            /// then a new colour was chosen
            source.setBackground(newColour);

            final int[][] colours =
                params.getProjectionParameters()
                    .getColors();

            colours[index][0] = newColour.getRed();
            colours[index][1] = newColour.getGreen();
            colours[index][2] = newColour.getBlue();
        }

        return newColour;
    }

    /**
     * Set a colour to a randomly chosen one
     *
     * @param source The button that was pressed to produce this action
     * @param index The index of the colour parameter to be set
     */
    protected void randomizeColour(JButton source, int index)
    {
        final Color newColour = randomColour();

        source.setBackground(newColour);

        final int[][] colours = params.getProjectionParameters()
                .getColors();

        colours[index][0] = newColour.getRed();
        colours[index][1] = newColour.getGreen();
        colours[index][2] = newColour.getBlue();
    }

    private final class CustomDocument extends PlainDocument
    {
        /* Create a new instance of CustomDocument */
        public CustomDocument()
        {
        }

        public void insertString(
            int offset, String typedOrPastedText, AttributeSet attributeSet)
            throws BadLocationException
        {
            if (typedOrPastedText == null)
            {
                return;
            }

            final StringBuffer sb = new StringBuffer();

            for (int c = 0; c < typedOrPastedText.length(); ++c)
            {
                if (Character.isDigit(typedOrPastedText.charAt(c)))
                {
                    sb.append(typedOrPastedText.charAt(c));
                }
            }

            super.insertString(offset, sb.toString(), attributeSet);
        }
    }

    /**
     * Set a combo box to have a numeric editor with validation
     *
     * @param box The <code>JComboBox</code> to be set up
     */
    protected void setComboNumericEditor(JComboBox box)
    {
        final ComboBoxEditor editor = box.getEditor();
        final Component editorComponent = editor.getEditorComponent();

        if (editorComponent instanceof JTextField)
        {
            final JTextField textField = (JTextField) editorComponent;
            textField.setDocument(new CustomDocument());
        }
    }

    /**
     * Set up our list model from the projection list
     */
    protected void setListModel()
    {
        final DefaultComboBoxModel<Projector> dcm = new DefaultComboBoxModel<>();

        for (Projector p : projManager.getProjections())
        {
            dcm.addElement(p);
        }

        projComboBox.setModel(dcm);

        // make the first one selected
        dcm.setSelectedItem(dcm.getElementAt(0));

        // TODO: do this properly
        projComboBoxActionPerformed(new ActionEvent(this, 0, "Hack"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        projPanel = new javax.swing.JPanel();
        projComboBox = new javax.swing.JComboBox<Projector>();
        jLabel1 = new javax.swing.JLabel();
        latSpinner = new javax.swing.JSpinner();
        lonSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        previewLabel = new javax.swing.JLabel();
        heightfieldCheckbox = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(12);
        format.setMaximumFractionDigits(12);
        seedField = new JFormattedTextField(format);
        randomSeedButton = new javax.swing.JButton();
        randomAllButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        vgridSpinner = new javax.swing.JSpinner();
        hgridSpinner = new javax.swing.JSpinner();
        outputJPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        widthComboBox = new javax.swing.JComboBox();
        heightComboBox = new javax.swing.JComboBox();
        reverseCheckbox = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        scaleSpinner = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        oceanColourButton = new javax.swing.JButton();
        shoreColourButton = new javax.swing.JButton();
        lowColourButton = new javax.swing.JButton();
        highColourButton = new javax.swing.JButton();
        mountainColourButton = new javax.swing.JButton();
        rockyColourButton = new javax.swing.JButton();
        peakColourButton = new javax.swing.JButton();
        spaceColourButton = new javax.swing.JButton();
        lineColourButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        randomAllColourButton = new javax.swing.JButton();
        randomOceanColourButton = new javax.swing.JButton();
        randomShoreColourButton = new javax.swing.JButton();
        randomLowColourButton = new javax.swing.JButton();
        randomHighColourButton = new javax.swing.JButton();
        randomMountainColourButton = new javax.swing.JButton();
        randomRockyColourButton = new javax.swing.JButton();
        randomPeakColourButton = new javax.swing.JButton();
        randomSpaceColourButton = new javax.swing.JButton();
        randomLineColourButton = new javax.swing.JButton();
        optionsPanel = new javax.swing.JPanel();
        altColourCheckbox = new javax.swing.JCheckBox();
        laticCheckbox = new javax.swing.JCheckBox();
        shadeCheckbox = new javax.swing.JCheckBox();
        shadeAngleSpinner = new javax.swing.JSpinner();
        lighterSpinner = new javax.swing.JSpinner();
        jLabel19 = new javax.swing.JLabel();
        outlineCheckbox = new javax.swing.JCheckBox();
        edgesCheckbox = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loadParamsItem = new javax.swing.JMenuItem();
        saveParamsItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        nativeLAFCheckbox = new javax.swing.JCheckBoxMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Terrain Generator");
        jButton1.setAction(new GenerateAction(this, "Generate"));
        jButton1.setText("Generate ...");
        jButton1.setToolTipText("Generate the terrain using the parameters you have set up");
        jButton1.setPreferredSize(new java.awt.Dimension(120, 23));

        jTabbedPane1.setToolTipText("");
        projPanel.setRequestFocusEnabled(false);
        projComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        projComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                projComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Projection Type");
        jLabel1.setToolTipText("Specify the map projection to be used");

        latSpinner.setModel(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.001));
        latSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                latSpinnerStateChanged(evt);
            }
        });

        lonSpinner.setModel(new SpinnerNumberModel(0.0, -180.0, 180.0, 0.001));
        lonSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                lonSpinnerStateChanged(evt);
            }
        });

        jLabel2.setText("Latitude");
        jLabel2.setToolTipText("Set the latitude for the projection");

        jLabel3.setText("Longitude");
        jLabel3.setToolTipText("Set the longitude for the projection");

        previewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        previewLabel.setText("Not Initialized!");
        previewLabel.setToolTipText("A preview of the map projection");
        previewLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        heightfieldCheckbox.setText("Don't use a projection, generate heightfield output");
        heightfieldCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        heightfieldCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        heightfieldCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                heightfieldCheckboxActionPerformed(evt);
            }
        });

        jLabel17.setText("Seed value for random terrain generation");
        jLabel17.setToolTipText("Set the seed value. A  particular seed will produce the same terrain");

        seedField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        seedField.setText("0.0");
        seedField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                seedFieldActionPerformed(evt);
            }
        });

        randomSeedButton.setText("Random Seed");
        randomSeedButton.setToolTipText("Pick a random seed value");
        randomSeedButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomSeedButtonActionPerformed(evt);
            }
        });

        randomAllButton.setText("Randomize All");
        randomAllButton.setToolTipText("Randomize all the items on this page");
        randomAllButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomAllButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout projPanelLayout = new org.jdesktop.layout.GroupLayout(projPanel);
        projPanel.setLayout(projPanelLayout);
        projPanelLayout.setHorizontalGroup(
            projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, projPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, previewLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, heightfieldCheckbox)
                    .add(projPanelLayout.createSequentialGroup()
                        .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel17))
                        .add(145, 145, 145)
                        .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lonSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, latSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, projComboBox, 0, 318, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, seedField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, projPanelLayout.createSequentialGroup()
                                .add(randomSeedButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 116, Short.MAX_VALUE)
                                .add(randomAllButton)))))
                .addContainerGap())
        );
        projPanelLayout.setVerticalGroup(
            projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(projPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(projComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(latSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lonSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel17)
                    .add(seedField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(projPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(randomAllButton)
                    .add(randomSeedButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(previewLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(heightfieldCheckbox)
                .addContainerGap())
        );
        jTabbedPane1.addTab("Project", null, projPanel, "Set the projection parameters");

        jLabel4.setText("Add a grid at this vertical spacing in degrees (0 = none)");

        jLabel5.setText("Add a grid at this horizontal spacing in degrees (0 = none)");

        vgridSpinner.setModel(new SpinnerNumberModel(0.0, -90.0, 90.0, 0.001));
        vgridSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                vgridSpinnerStateChanged(evt);
            }
        });

        hgridSpinner.setModel(new SpinnerNumberModel(0.0, -180.0, 180.0, 0.001));
        hgridSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                hgridSpinnerStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 290, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(hgridSpinner)
                    .add(vgridSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(vgridSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(hgridSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(334, Short.MAX_VALUE))
        );
        jTabbedPane1.addTab("Grid", null, jPanel1, "Set the grid parameters");

        jLabel6.setText("Output width in pixels");

        jLabel7.setText("Output height in pixels");

        widthComboBox.setEditable(true);
        widthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "320", "640", "800", "1024", "1280", "1600" }));
        widthComboBox.setSelectedIndex(2);
        widthComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                widthComboBoxActionPerformed(evt);
            }
        });

        heightComboBox.setEditable(true);
        heightComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "200", "480", "600", "1024", "1200" }));
        heightComboBox.setSelectedIndex(2);
        heightComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                heightComboBoxActionPerformed(evt);
            }
        });

        reverseCheckbox.setText("Reverse the background on the output");
        reverseCheckbox.setToolTipText("If selected will invert the background colour");
        reverseCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reverseCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel18.setText("Scale (1.0 = normal)");

        scaleSpinner.setModel(new SpinnerNumberModel(1.0, 0.01, 10.0, 0.01));
        scaleSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                scaleSpinnerStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout outputJPanelLayout = new org.jdesktop.layout.GroupLayout(outputJPanel);
        outputJPanel.setLayout(outputJPanelLayout);
        outputJPanelLayout.setHorizontalGroup(
            outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputJPanelLayout.createSequentialGroup()
                        .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel6)
                            .add(jLabel7))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 458, Short.MAX_VALUE)
                        .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(heightComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(widthComboBox, 0, 85, Short.MAX_VALUE)))
                    .add(outputJPanelLayout.createSequentialGroup()
                        .add(jLabel18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 401, Short.MAX_VALUE)
                        .add(scaleSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(reverseCheckbox))
                .addContainerGap())
        );
        outputJPanelLayout.setVerticalGroup(
            outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(widthComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(heightComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(scaleSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reverseCheckbox)
                .addContainerGap(283, Short.MAX_VALUE))
        );
        jTabbedPane1.addTab("Output", null, outputJPanel, "Set the output parameters");

        oceanColourButton.setText("Select ...");
        oceanColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                oceanColourButtonActionPerformed(evt);
            }
        });

        shoreColourButton.setText("Select ...");
        shoreColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shoreColourButtonActionPerformed(evt);
            }
        });

        lowColourButton.setText("Select ...");
        lowColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lowColourButtonActionPerformed(evt);
            }
        });

        highColourButton.setText("Select ...");
        highColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                highColourButtonActionPerformed(evt);
            }
        });

        mountainColourButton.setText("Select ...");
        mountainColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mountainColourButtonActionPerformed(evt);
            }
        });

        rockyColourButton.setText("Select ...");
        rockyColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rockyColourButtonActionPerformed(evt);
            }
        });

        peakColourButton.setText("Select ...");
        peakColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                peakColourButtonActionPerformed(evt);
            }
        });

        spaceColourButton.setText("Select ...");
        spaceColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                spaceColourButtonActionPerformed(evt);
            }
        });

        lineColourButton.setText("Select ...");
        lineColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                lineColourButtonActionPerformed(evt);
            }
        });

        jLabel8.setText("Colour to use for ocean depths");

        jLabel9.setText("Colour to use for shores");

        jLabel10.setText("Colour to use for lowlands");

        jLabel11.setText("Colour to use for highlands");

        jLabel12.setText("Colour to use for mountains");

        jLabel13.setText("Colour to use for high rocky peaks");

        jLabel14.setText("Colour to use for peaks");

        jLabel15.setText("Colour to use for space");

        jLabel16.setText("Colour to use for lines");

        randomAllColourButton.setText("Randomize All Colours");
        randomAllColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomAllColourButtonActionPerformed(evt);
            }
        });

        randomOceanColourButton.setText("Random");
        randomOceanColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomOceanColourButtonActionPerformed(evt);
            }
        });

        randomShoreColourButton.setText("Random");
        randomShoreColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomShoreColourButtonActionPerformed(evt);
            }
        });

        randomLowColourButton.setText("Random");
        randomLowColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomLowColourButtonActionPerformed(evt);
            }
        });

        randomHighColourButton.setText("Random");
        randomHighColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomHighColourButtonActionPerformed(evt);
            }
        });

        randomMountainColourButton.setText("Random");
        randomMountainColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomMountainColourButtonActionPerformed(evt);
            }
        });

        randomRockyColourButton.setText("Random");
        randomRockyColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomRockyColourButtonActionPerformed(evt);
            }
        });

        randomPeakColourButton.setText("Random");
        randomPeakColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomPeakColourButtonActionPerformed(evt);
            }
        });

        randomSpaceColourButton.setText("Random");
        randomSpaceColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomSpaceColourButtonActionPerformed(evt);
            }
        });

        randomLineColourButton.setText("Random");
        randomLineColourButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomLineColourButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel9)
                            .add(jLabel10)
                            .add(jLabel11)
                            .add(jLabel12)
                            .add(jLabel13)
                            .add(jLabel14)
                            .add(jLabel15)
                            .add(jLabel16))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 222, Short.MAX_VALUE)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(randomOceanColourButton)
                            .add(randomShoreColourButton)
                            .add(randomLowColourButton)
                            .add(randomMountainColourButton)
                            .add(randomRockyColourButton)
                            .add(randomPeakColourButton)
                            .add(randomSpaceColourButton)
                            .add(randomLineColourButton)
                            .add(randomHighColourButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, oceanColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, shoreColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lowColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, highColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, mountainColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, rockyColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, peakColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, spaceColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lineColourButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, randomAllColourButton))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(oceanColourButton)
                    .add(jLabel8)
                    .add(randomOceanColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(shoreColourButton)
                    .add(jLabel9)
                    .add(randomShoreColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lowColourButton)
                    .add(jLabel10)
                    .add(randomLowColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(highColourButton)
                    .add(jLabel11)
                    .add(randomHighColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mountainColourButton)
                    .add(jLabel12)
                    .add(randomMountainColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rockyColourButton)
                    .add(jLabel13)
                    .add(randomRockyColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(peakColourButton)
                    .add(jLabel14)
                    .add(randomPeakColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(spaceColourButton)
                    .add(jLabel15)
                    .add(randomSpaceColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lineColourButton)
                    .add(jLabel16)
                    .add(randomLineColourButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 91, Short.MAX_VALUE)
                .add(randomAllColourButton)
                .addContainerGap())
        );
        jTabbedPane1.addTab("Colours", null, jPanel3, "Set the colour parameters");

        altColourCheckbox.setText("Use an alternative colouring scheme");
        altColourCheckbox.setToolTipText("An alternate colour scheme more like an atlas");
        altColourCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        altColourCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        altColourCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                altColourCheckboxActionPerformed(evt);
            }
        });

        laticCheckbox.setText("Use latitude based colouring");
        laticCheckbox.setToolTipText("Colour terrain based on latitude");
        laticCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        laticCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        laticCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                laticCheckboxActionPerformed(evt);
            }
        });

        shadeCheckbox.setText("Do shading with light angle (degrees)");
        shadeCheckbox.setToolTipText("Use bumpmap shading on the terrain");
        shadeCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        shadeCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        shadeCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                shadeCheckboxActionPerformed(evt);
            }
        });

        shadeAngleSpinner.setModel(new SpinnerNumberModel(150.0, 0.0, 360.0, 0.1));
        shadeAngleSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                shadeAngleSpinnerStateChanged(evt);
            }
        });

        lighterSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        lighterSpinner.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                lighterSpinnerStateChanged(evt);
            }
        });

        jLabel19.setText("Lighten colours by this amount (doesn't work with alt colours)");
        jLabel19.setToolTipText("Use lighter colouring");

        outlineCheckbox.setText("Draw in outline mode only");
        outlineCheckbox.setToolTipText("Draw a black and white coastline output only");
        outlineCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outlineCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        outlineCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                outlineCheckboxActionPerformed(evt);
            }
        });

        edgesCheckbox.setText("Draw the edges of coastlines in black");
        edgesCheckbox.setToolTipText("Outline the coasts in black");
        edgesCheckbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        edgesCheckbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        edgesCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                edgesCheckboxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout optionsPanelLayout = new org.jdesktop.layout.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(altColourCheckbox)
                    .add(laticCheckbox)
                    .add(optionsPanelLayout.createSequentialGroup()
                        .add(shadeCheckbox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 304, Short.MAX_VALUE)
                        .add(shadeAngleSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, optionsPanelLayout.createSequentialGroup()
                        .add(17, 17, 17)
                        .add(jLabel19)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 189, Short.MAX_VALUE)
                        .add(lighterSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(optionsPanelLayout.createSequentialGroup()
                        .add(17, 17, 17)
                        .add(outlineCheckbox))
                    .add(edgesCheckbox))
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(altColourCheckbox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(laticCheckbox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(shadeCheckbox)
                    .add(shadeAngleSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lighterSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel19))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(edgesCheckbox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outlineCheckbox)
                .addContainerGap(250, Short.MAX_VALUE))
        );

        optionsPanelLayout.linkSize(new java.awt.Component[] {altColourCheckbox, laticCheckbox, shadeCheckbox}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jTabbedPane1.addTab("Options", null, optionsPanel, "Set the rest of the options");

        fileMenu.setText("File");
        loadParamsItem.setText("Load Settings ...");
        loadParamsItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadParamsItemActionPerformed(evt);
            }
        });

        fileMenu.add(loadParamsItem);

        saveParamsItem.setText("Save Settings ...");
        saveParamsItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveParamsItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveParamsItem);

        fileMenu.add(jSeparator1);

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

        optionsMenu.setText("Options");
        nativeLAFCheckbox.setText("Use system look and feel");
        nativeLAFCheckbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nativeLAFCheckboxActionPerformed(evt);
            }
        });

        optionsMenu.add(nativeLAFCheckbox);

        jMenuBar1.add(optionsMenu);

        helpMenu.setText("Help");
        aboutItem.setText("About ...");
        aboutItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aboutItemActionPerformed(evt);
            }
        });

        helpMenu.add(aboutItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-715)/2, (screenSize.height-515)/2, 715, 515);
    }// </editor-fold>//GEN-END:initComponents
    
    private void edgesCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_edgesCheckboxActionPerformed
    {//GEN-HEADEREND:event_edgesCheckboxActionPerformed
        params.getProjectionParameters()
            .setEdges(edgesCheckbox.isSelected());
    }//GEN-LAST:event_edgesCheckboxActionPerformed

    private void outlineCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_outlineCheckboxActionPerformed
    {//GEN-HEADEREND:event_outlineCheckboxActionPerformed
        params.getProjectionParameters()
            .setOutline(outlineCheckbox.isSelected());
    }//GEN-LAST:event_outlineCheckboxActionPerformed

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutItemActionPerformed
    {//GEN-HEADEREND:event_aboutItemActionPerformed

        if (aboutBox == null)
        {
            aboutBox = new AboutBoxDialog(this, true);
        }

        aboutBox.setVisible(true);
    }//GEN-LAST:event_aboutItemActionPerformed

    private void nativeLAFCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_nativeLAFCheckboxActionPerformed
    {//GEN-HEADEREND:event_nativeLAFCheckboxActionPerformed

        final boolean nativeLAF = nativeLAFCheckbox.isSelected();

        if (prefs != null)
        {
            prefs.putBoolean(PREF_NAME_NATIVE_LAF, nativeLAF);
        }

        if (!lafUtils.setSystemLookAndFeel(nativeLAF, this))
        {
            JOptionPane.showMessageDialog(
                this, "Couldn't set look and feel", "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_nativeLAFCheckboxActionPerformed

    private void lighterSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_lighterSpinnerStateChanged
    {//GEN-HEADEREND:event_lighterSpinnerStateChanged

        final Integer val = (Integer) lighterSpinner.getValue();

        params.getProjectionParameters()
            .setLighterColours(val);
    }//GEN-LAST:event_lighterSpinnerStateChanged

    private void exitItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitItemActionPerformed
    {//GEN-HEADEREND:event_exitItemActionPerformed
        this.setVisible(false);
        System.exit(1);
    }//GEN-LAST:event_exitItemActionPerformed

    private void randomAllColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomAllColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomAllColourButtonActionPerformed
        randomizeColour(lineColourButton, 8);
        randomizeColour(spaceColourButton, 7);
        randomizeColour(peakColourButton, 6);
        randomizeColour(rockyColourButton, 5);
        randomizeColour(mountainColourButton, 4);
        randomizeColour(highColourButton, 3);
        randomizeColour(lowColourButton, 2);
        randomizeColour(shoreColourButton, 1);
        randomizeColour(oceanColourButton, 0);
    }//GEN-LAST:event_randomAllColourButtonActionPerformed

    private void randomLineColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomLineColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomLineColourButtonActionPerformed
        randomizeColour(lineColourButton, 8);
    }//GEN-LAST:event_randomLineColourButtonActionPerformed

    private void randomSpaceColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomSpaceColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomSpaceColourButtonActionPerformed
        randomizeColour(spaceColourButton, 7);
    }//GEN-LAST:event_randomSpaceColourButtonActionPerformed

    private void randomPeakColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomPeakColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomPeakColourButtonActionPerformed
        randomizeColour(peakColourButton, 6);
    }//GEN-LAST:event_randomPeakColourButtonActionPerformed

    private void randomRockyColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomRockyColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomRockyColourButtonActionPerformed
        randomizeColour(rockyColourButton, 5);
    }//GEN-LAST:event_randomRockyColourButtonActionPerformed

    private void randomMountainColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomMountainColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomMountainColourButtonActionPerformed
        randomizeColour(mountainColourButton, 4);
    }//GEN-LAST:event_randomMountainColourButtonActionPerformed

    private void randomHighColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomHighColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomHighColourButtonActionPerformed
        randomizeColour(highColourButton, 3);
    }//GEN-LAST:event_randomHighColourButtonActionPerformed

    private void randomLowColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomLowColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomLowColourButtonActionPerformed
        randomizeColour(lowColourButton, 2);
    }//GEN-LAST:event_randomLowColourButtonActionPerformed

    private void randomShoreColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomShoreColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomShoreColourButtonActionPerformed
        randomizeColour(shoreColourButton, 1);
    }//GEN-LAST:event_randomShoreColourButtonActionPerformed

    private void randomOceanColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomOceanColourButtonActionPerformed
    {//GEN-HEADEREND:event_randomOceanColourButtonActionPerformed
        randomizeColour(oceanColourButton, 0);
    }//GEN-LAST:event_randomOceanColourButtonActionPerformed

    private void randomAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomAllButtonActionPerformed
    {//GEN-HEADEREND:event_randomAllButtonActionPerformed

        final int which = random.nextInt(projComboBox.getModel().getSize());

        projComboBox.setSelectedIndex(which);

        final double seed = this.random.nextDouble();

        this.params.getPlanetParameters()
            .setSeed(seed);

        final double lat = (random.nextDouble() * 180) - 90;
        final double lon = (random.nextDouble() * 360) - 180;

        this.params.getProjectionParameters()
            .setLat(lat);
        this.params.getProjectionParameters()
            .setLon(lon);

        updateProjectParameters();
    }//GEN-LAST:event_randomAllButtonActionPerformed

    private void scaleSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_scaleSpinnerStateChanged
    {//GEN-HEADEREND:event_scaleSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        params.getProjectionParameters()
            .setScale((Double) source.getValue());
    }//GEN-LAST:event_scaleSpinnerStateChanged

    private void loadParamsItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_loadParamsItemActionPerformed
    {//GEN-HEADEREND:event_loadParamsItemActionPerformed

        final int choice = xmlChooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                final InputStream target =
                    xmlChooser.getFileContents()
                        .getInputStream();

                final XMLDecoder decoder = new XMLDecoder(target);

                final AllPlanetParameters newParams =
                    (AllPlanetParameters) decoder.readObject();

                this.params = newParams;

                updateAllFromParameters();
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

        final int choice = xmlChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                if (
                    !xmlChooser.getFileContents()
                        .canRead() ||
                        (JOptionPane.showConfirmDialog(
                            this,
                            "This file already exists. Do you want to\n" +
                            "overwrite it?", "Replace File?",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
                {
                    final OutputStream target =
                        xmlChooser.getFileContents()
                            .getOutputStream(true);

                    final XMLEncoder enc = new XMLEncoder(target);

                    enc.writeObject(params);

                    enc.close();
                    target.close();
                }
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
    }//GEN-LAST:event_saveParamsItemActionPerformed

    private void shadeCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_shadeCheckboxActionPerformed
    {//GEN-HEADEREND:event_shadeCheckboxActionPerformed
        params.getProjectionParameters()
            .setDoShade(shadeCheckbox.isSelected());
    }//GEN-LAST:event_shadeCheckboxActionPerformed

    private void laticCheckboxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_laticCheckboxActionPerformed
    {//GEN-HEADEREND:event_laticCheckboxActionPerformed
        params.getProjectionParameters()
            .setLatic(laticCheckbox.isSelected());
    }//GEN-LAST:event_laticCheckboxActionPerformed

    private void altColourCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_altColourCheckboxActionPerformed
    {//GEN-HEADEREND:event_altColourCheckboxActionPerformed
        params.getProjectionParameters()
            .setAltColors(altColourCheckbox.isSelected());

        lighterSpinner.setEnabled(!altColourCheckbox.isSelected());
    }//GEN-LAST:event_altColourCheckboxActionPerformed

    private void shadeAngleSpinnerStateChanged(
        javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_shadeAngleSpinnerStateChanged
    {//GEN-HEADEREND:event_shadeAngleSpinnerStateChanged

        final Double value = (Double) shadeAngleSpinner.getValue();
        params.getProjectionParameters()
            .setShadeAngle(value);
    }//GEN-LAST:event_shadeAngleSpinnerStateChanged

    private void lineColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_lineColourButtonActionPerformed
    {//GEN-HEADEREND:event_lineColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Line Colour", 8);
    }//GEN-LAST:event_lineColourButtonActionPerformed

    private void spaceColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_spaceColourButtonActionPerformed
    {//GEN-HEADEREND:event_spaceColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Space Colour", 7);
    }//GEN-LAST:event_spaceColourButtonActionPerformed

    private void peakColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_peakColourButtonActionPerformed
    {//GEN-HEADEREND:event_peakColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Peak Colour", 6);
    }//GEN-LAST:event_peakColourButtonActionPerformed

    private void rockyColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_rockyColourButtonActionPerformed
    {//GEN-HEADEREND:event_rockyColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Rocky Colour", 5);
    }//GEN-LAST:event_rockyColourButtonActionPerformed

    private void mountainColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_mountainColourButtonActionPerformed
    {//GEN-HEADEREND:event_mountainColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Mountain Colour", 4);
    }//GEN-LAST:event_mountainColourButtonActionPerformed

    private void highColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_highColourButtonActionPerformed
    {//GEN-HEADEREND:event_highColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Highland Colour", 3);
    }//GEN-LAST:event_highColourButtonActionPerformed

    private void lowColourButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_lowColourButtonActionPerformed
    {//GEN-HEADEREND:event_lowColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Lowland Colour", 2);
    }//GEN-LAST:event_lowColourButtonActionPerformed

    private void oceanColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_oceanColourButtonActionPerformed
    {//GEN-HEADEREND:event_oceanColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Ocean Colour", 0);
    }//GEN-LAST:event_oceanColourButtonActionPerformed

    private void shoreColourButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_shoreColourButtonActionPerformed
    {//GEN-HEADEREND:event_shoreColourButtonActionPerformed
        pickColour((JButton) evt.getSource(), "Choose Shore Colour", 1);
    }//GEN-LAST:event_shoreColourButtonActionPerformed

    private void heightComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_heightComboBoxActionPerformed
    {//GEN-HEADEREND:event_heightComboBoxActionPerformed

        Object val = heightComboBox.getSelectedItem();

        Integer value;

        if (val instanceof Integer)
        {
            value = (Integer) val;
        }
        else
        {
            value = Integer.parseInt((String) val);
        }

        params.getProjectionParameters()
            .setHeight(value);
    }//GEN-LAST:event_heightComboBoxActionPerformed

    private void widthComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_widthComboBoxActionPerformed
    {//GEN-HEADEREND:event_widthComboBoxActionPerformed

        final Object val = widthComboBox.getSelectedItem();

        Integer value;

        if (val instanceof Integer)
        {
            value = (Integer) val;
        }
        else
        {
            value = Integer.parseInt((String) val);
        }

        params.getProjectionParameters()
            .setWidth(value);
    }//GEN-LAST:event_widthComboBoxActionPerformed

    private void hgridSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_hgridSpinnerStateChanged
    {//GEN-HEADEREND:event_hgridSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        params.getProjectionParameters()
            .setHgrid((Double) source.getValue());
    }//GEN-LAST:event_hgridSpinnerStateChanged

    private void vgridSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_vgridSpinnerStateChanged
    {//GEN-HEADEREND:event_vgridSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        params.getProjectionParameters()
            .setVgrid((Double) source.getValue());
    }//GEN-LAST:event_vgridSpinnerStateChanged

    private void seedFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_seedFieldActionPerformed
    {//GEN-HEADEREND:event_seedFieldActionPerformed

        final Double value = (Double) seedField.getValue();
        params.getPlanetParameters()
            .setSeed(value);
    }//GEN-LAST:event_seedFieldActionPerformed

    private void lonSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_lonSpinnerStateChanged
    {//GEN-HEADEREND:event_lonSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        params.getProjectionParameters()
            .setLat((Double) source.getValue());
    }//GEN-LAST:event_lonSpinnerStateChanged

    private void latSpinnerStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_latSpinnerStateChanged
    {//GEN-HEADEREND:event_latSpinnerStateChanged

        final JSpinner source = (JSpinner) evt.getSource();
        params.getProjectionParameters()
            .setLat((Double) source.getValue());
    }//GEN-LAST:event_latSpinnerStateChanged

    private void randomSeedButtonActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomSeedButtonActionPerformed
    {//GEN-HEADEREND:event_randomSeedButtonActionPerformed

        final double seed = Math.random();

        params.getPlanetParameters()
            .setSeed(seed);
        seedField.setValue(seed);
    }//GEN-LAST:event_randomSeedButtonActionPerformed

    private void heightfieldCheckboxActionPerformed(
        java.awt.event.ActionEvent evt)//GEN-FIRST:event_heightfieldCheckboxActionPerformed
    {//GEN-HEADEREND:event_heightfieldCheckboxActionPerformed

        final boolean state = heightfieldCheckbox.isSelected();

        projComboBox.setEnabled(!state);
        previewLabel.setEnabled(!state);

        params.getProjectionParameters()
            .setHeightfield(state);
    }//GEN-LAST:event_heightfieldCheckboxActionPerformed

    private void projComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_projComboBoxActionPerformed
    {//GEN-HEADEREND:event_projComboBoxActionPerformed

        final DefaultComboBoxModel dcm =
            (DefaultComboBoxModel) projComboBox.getModel();

        final Projector p = (Projector) dcm.getSelectedItem();

        params.getProjectionParameters()
            .setProjectionName(p.toString());

        final String path =
            projManager.formatThumbnailName(p.getThumbnailName());

        final URL imgURL = projManager.getClass()
                .getResource(path);

        if (imgURL != null)
        {
            final ImageIcon i =
                new ImageIcon(imgURL, "Projection Preview Image");

            previewLabel.setText("");
            previewLabel.setIcon(i);
        }
        else
        {
            previewLabel.setText("Preview Icon Not Found!\n" + path);
            previewLabel.setIcon(null);
        }
    }//GEN-LAST:event_projComboBoxActionPerformed

    /**
     * The entrypoint
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    new MainFrame().setVisible(true);
                }
            });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JCheckBox altColourCheckbox;
    private javax.swing.JCheckBox edgesCheckbox;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox heightComboBox;
    private javax.swing.JCheckBox heightfieldCheckbox;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSpinner hgridSpinner;
    private javax.swing.JButton highColourButton;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner latSpinner;
    private javax.swing.JCheckBox laticCheckbox;
    private javax.swing.JSpinner lighterSpinner;
    private javax.swing.JButton lineColourButton;
    private javax.swing.JMenuItem loadParamsItem;
    private javax.swing.JSpinner lonSpinner;
    private javax.swing.JButton lowColourButton;
    private javax.swing.JButton mountainColourButton;
    private javax.swing.JCheckBoxMenuItem nativeLAFCheckbox;
    private javax.swing.JButton oceanColourButton;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JCheckBox outlineCheckbox;
    private javax.swing.JPanel outputJPanel;
    private javax.swing.JButton peakColourButton;
    private javax.swing.JLabel previewLabel;
    private javax.swing.JComboBox<Projector> projComboBox;
    private javax.swing.JPanel projPanel;
    private javax.swing.JButton randomAllButton;
    private javax.swing.JButton randomAllColourButton;
    private javax.swing.JButton randomHighColourButton;
    private javax.swing.JButton randomLineColourButton;
    private javax.swing.JButton randomLowColourButton;
    private javax.swing.JButton randomMountainColourButton;
    private javax.swing.JButton randomOceanColourButton;
    private javax.swing.JButton randomPeakColourButton;
    private javax.swing.JButton randomRockyColourButton;
    private javax.swing.JButton randomSeedButton;
    private javax.swing.JButton randomShoreColourButton;
    private javax.swing.JButton randomSpaceColourButton;
    private javax.swing.JCheckBox reverseCheckbox;
    private javax.swing.JButton rockyColourButton;
    private javax.swing.JMenuItem saveParamsItem;
    private javax.swing.JSpinner scaleSpinner;
    private javax.swing.JFormattedTextField seedField;
    private javax.swing.JSpinner shadeAngleSpinner;
    private javax.swing.JCheckBox shadeCheckbox;
    private javax.swing.JButton shoreColourButton;
    private javax.swing.JButton spaceColourButton;
    private javax.swing.JSpinner vgridSpinner;
    private javax.swing.JComboBox widthComboBox;
    // End of variables declaration//GEN-END:variables
}
