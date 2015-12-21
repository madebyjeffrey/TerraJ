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
 * TerrainViewerFrame.java
 *
 * Created on 18 January 2006, 13:12
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.terraj.fracplanet.CloudParameters;
import com.alvermont.terraj.fracplanet.MeshStats;
import com.alvermont.terraj.fracplanet.RenderParameters;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshCloudFlat;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshCloudPlanet;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrain;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrainFlat;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrainPlanet;
import com.alvermont.terraj.fracplanet.io.JarLibraryLoader;
import com.alvermont.terraj.fracplanet.render.TriangleMeshViewerDisplay;
import com.alvermont.terraj.stargen.util.MathUtils;
import com.meghnasoft.async.AbstractAsynchronousAction;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import javax.swing.ToolTipManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An implementation of the terrain viewer.
 *
 * @author martin
 * @version $Id: TerrainViewerFrame.java,v 1.17 2006/07/06 06:58:34 martin Exp $
 */
public class TerrainViewerFrame extends AbstractTerrainViewerFrame {

    /**
     * Our logger object
     */
    private static Log log = LogFactory.getLog(TerrainViewerFrame.class);
    private FPSAnimator anim;
    private TriangleMeshViewerDisplay tmvd;
    private ProgressDialog pd;
    private GLCanvas canvas;
    private MeshStats stats = new MeshStats();

    /**
     * Creates a new instance of TerrainViewerFrame
     */
    public TerrainViewerFrame() {
    }

    /**
     * An asynchronous action class to carry out terrain regeneration
     */
    private class RegenAction extends AbstractAsynchronousAction {

        /**
         * Creates a new instance of RegenAction
         *
         * @param name The name of the action
         */
        public RegenAction(String name) {
            super(name);
        }

        /**
         * Called to carry out the work of the action
         *
         * @param e The ActionEvent associated with the triggering of this
         * action
         * @return The object that is the result of this action
         */
        public Object asynchronousActionPerformed(ActionEvent e) {
            regenerate();

            return this;
        }

        /**
         * Called when the action has completed
         */
        public void finished() {
        }
    }

    /**
     * Called to initialize the display being managed by this object
     */
    public void initialize() {
        final MathUtils utils =
                new MathUtils(
                new Random(
                getAllParams().getTerrainParameters().getTerrainSeed()));

        this.pd = new ProgressDialog(this, false);

        // disable lightweight menus so they don't appear behind the GL canvas
        ToolTipManager.sharedInstance()
                .setLightWeightPopupEnabled(false);
/* // zzing 24 Feb 2013  We don't need this with JOGL 2
        try {
            JarLibraryLoader.setupPath();

            JarLibraryLoader.extractLibrary("jogl");
            JarLibraryLoader.extractLibrary("jogl_awt");
            JarLibraryLoader.extractLibrary("jogl_cg");
            JarLibraryLoader.extractLibrary("jogl_drihack");
        } catch (Exception e) {
            log.error("Failed to load native libraries from jar file", e);
        }
*/
        try {
            GLProfile glProfile = GLProfile.getDefault();
            System.out.println(glProfile); 
            GLCapabilities cap = new GLCapabilities(glProfile);
            System.out.println(cap);
            GLCanvas c = new GLCanvas( cap);
            
            
            this.canvas = c; 
            this.terrainPanel.add(this.canvas);
            
            
//            System.out.println(this.canvas.getGL());
            
            //new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
            /*GLProfile glprofile = GLProfile.getDefault();
            GLCapabilities glcapabilities = new GLCapabilities( glprofile );
            final GLCanvas glcanvas = new GLCanvas( glcapabilities );
            this.canvas = glcanvas; */
        } catch (Throwable e) {
            // this is fatal
            e.printStackTrace();

            System.exit(1);
        }

        this.canvas.setPreferredSize(new Dimension(0, 0));
        this.canvas.setMinimumSize(new Dimension(0, 0));

//        canvas.setGL(new DebugGL(canvas.getGL()));
        final RenderParameters rp = getAllParams()
                .getRenderParameters();

        this.anim = new FPSAnimator(this.canvas, (int) rp.getFpsTarget());

        // generate the terrain
        final RegenAction action = new RegenAction("Regenerate");

        action.actionPerformed(new ActionEvent(this, 1, "Regenerate"));
    }

    /**
     * Called when the terrain is to be regenerated
     */
    public void regenerate() {
        if (this.anim.isAnimating()) {
            this.anim.stop();
        }

        this.setVisible(false);

        final TerrainParameters tp = getAllParams()
                .getTerrainParameters();
        final CloudParameters cp = getAllParams()
                .getCloudParameters();

        final MathUtils utils = new MathUtils(new Random(tp.getTerrainSeed()));

        TriangleMeshTerrain terrain = null;

        // put up progress dialog
        this.pd.setVisible(true);

        // create the new terrain
        if (tp.getObjectType() == TerrainParameters.ObjectTypeEnum.PLANET) {
            terrain = new TriangleMeshTerrainPlanet(tp, this.pd, utils);
        } else {
            terrain = new TriangleMeshTerrainFlat(tp, this.pd, utils);
        }

        // update stats
        this.stats.setVertices(terrain.getVertexCount());
        this.stats.setTriangles(terrain.getTriangleCount());
        this.stats.setLandTriangles(terrain.getTriangleColour0Count());
        this.stats.setSeaTriangles(terrain.getTriangleColour1Count());

        this.setMeshStats(this.stats);

        final RenderParameters rp = getAllParams()
                .getRenderParameters();

        List<TriangleMesh> meshes = new ArrayList<TriangleMesh>();

        meshes.add(terrain);

        if (cp.isEnabled()) {
            TriangleMesh cloudMesh = null;

            switch (tp.getObjectType()) {
                case PLANET: {
                    cloudMesh = new TriangleMeshCloudPlanet(
                            pd, terrain.getGeometry(), tp, cp);

                    break;
                }

                default: {
                    cloudMesh = new TriangleMeshCloudFlat(
                            pd, terrain.getGeometry(), tp, cp);

                    break;
                }
            }

            meshes.add(cloudMesh);
        }

        // take the progress dialog down
        this.pd.setVisible(false);

        if (this.tmvd == null) {
            this.tmvd = new TriangleMeshViewerDisplay(rp, meshes, this.canvas);
            this.setDisplay(this.tmvd);
        } else {
            this.tmvd.setMeshes(meshes);
        }

        meshChanged();

        // some how this thing is registered before this
        if (this.anim == null) {
            this.anim = new FPSAnimator(
                    this.tmvd.getCanvas(), (int) rp.getFpsTarget());
        }

        this.setVisible(true);

        // start animation
        this.anim.start();
    }

    /**
     * Called when the display is being shut down
     */
    public void shutdown() {
        if ((this.anim != null) && this.anim.isAnimating()) {
            this.anim.stop();
        }
    }
}
