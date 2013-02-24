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
 * TriangleMeshViewerDisplay.java
 *
 * Created on December 31, 2005, 10:13 AM
 *
 */
package com.alvermont.terraj.fracplanet.render;

import com.alvermont.terraj.fracplanet.RenderParameters;
import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.Vertex;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.util.ByteBufferUtils;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.fixedfunc.*;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that displays a mesh using JOGL rendering.
 *
 * @author martin
 * @version $Id: TriangleMeshViewerDisplay.java,v 1.16 2006/07/06 15:17:24 martin Exp $
 */
public class TriangleMeshViewerDisplay extends JComponent
    implements GLEventListener
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleMeshViewerDisplay.class);
    private List<TriangleMesh> meshes;
    private RenderParameters parameters;
    private int displayListIndex = 0;
    private int frame;
    private int width;
    private int height;

    // TODO: frame timings

    // camera position
    private XYZ cameraPosition;
    private XYZ cameraLookAt;
    private XYZ cameraUp;
    private XYZ defaultCameraPosition;
    private XYZ defaultCameraLookAt;
    private XYZ defaultCameraUp;
    private XYZ cameraMotion;
    private XYZ cameraRotation;
    private float camRotZ = 0.0f;

    // object parameters
    private float objectTilt;
    private float objectRotation;

    /** Indicates whether we are in flying mode (free camera) */
    private boolean flying = false;
    private GLCanvas canvas;
    private Camera camera;
    private CameraPosition savedCameraPosition;
    private int sizeofFloat = ByteBufferUtils.sizeofFloat();

    /**
     * Creates a new instance of TriangleMeshViewerDisplay
     *
     * @param meshes The list of meshes to be displayed
     * @param param The set of rendering parameters to be used
     * @param canvas The GL canvas that is to be used for rendering
     */
    public TriangleMeshViewerDisplay(
        RenderParameters param, List<TriangleMesh> meshes, GLCanvas canvas)
    {
        super();

        this.canvas = canvas;
        this.meshes = meshes;
        this.parameters = param;
        this.frame = 0;
        this.width = 0;
        this.height = 0;

        // MagicNumber OFF
        this.defaultCameraPosition = new SimpleXYZ(-3.0f, 0.0f, 0.0f);
        this.defaultCameraLookAt = new SimpleXYZ(0.0f, 0.0f, 0.0f);
        this.defaultCameraUp = new SimpleXYZ(0.0f, 0.0f, 1.0f);

        this.cameraPosition = new SimpleXYZ(-3.0f, 0.0f, 0.0f);
        this.cameraLookAt = new SimpleXYZ(0.0f, 0.0f, 0.0f);
        this.cameraUp = new SimpleXYZ(0.0f, 0.0f, 1.0f);

        this.cameraMotion = new SimpleXYZ(0.0f, 0.0f, 0.0f);
        this.cameraRotation = new SimpleXYZ(0.0f, 0.0f, 0.0f);

        this.objectTilt = (float) ((-30f * Math.PI) / 180.0f);
        this.objectRotation = 0.0f;

        // MagicNumber ON
        this.displayListIndex = 0;

        final CameraPosition campos = new CameraPosition();

        campos.setEye(this.defaultCameraPosition);
        campos.setCentre(this.defaultCameraLookAt);
        campos.setUp(this.defaultCameraUp);

        this.savedCameraPosition = new CameraPosition(campos);

        System.out.println("Canvas GetGL: " + canvas.getGL());
        // zzing casted to get rid of error
        this.camera = new CameraJOGL((GL2)canvas.getGL(), campos);

        canvas.addGLEventListener(this);
    }

    /**
     * KeyAdapter class used to process keyboard events
     */
    protected class MyKeyAdapter extends KeyAdapter
    {
        /** Create an instance of MyKeyAdapter */
        public MyKeyAdapter()
        {
        }

        /** Amount of motion per keypress in normal mode */
        public static final float CAMERA_MOTION_INCREMENT = 0.01f;

        /** Amount of rotation per keypress in norma mode */
        public static final float CAMERA_ROTATION_INCREMENT = 0.1f;

        /** Increase in motion/rotation per keypress with shift key held */
        public static final float CAMERA_SHIFT_MULTIPLIER = 5.0f;
        private float cameraMultiplier = 1.0f;

        // RequireThis OFF: CAMERA_MOTION_INCREMENT
        // RequireThis OFF: CAMERA_ROTATION_INCREMENT
        // RequireThis OFF: CAMERA_SHIFT_MULTIPLIER

        /**
         * Called when a key has been pressed with the corresponding event
         *
         * @param e An event object describing the key press
         */
        public void keyPressed(java.awt.event.KeyEvent e)
        {
            if (isFlying())
            {
                switch (e.getKeyCode())
                {
                    // shift?
                    case KeyEvent.VK_SHIFT:
                        this.cameraMultiplier = CAMERA_SHIFT_MULTIPLIER;

                        break;

                    // camera reset
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_DELETE:
                    case KeyEvent.VK_BACK_SPACE:
                        cameraMotion.setX(0);
                        cameraMotion.setY(0);
                        cameraMotion.setZ(0);

                        cameraRotation.setX(0);
                        cameraRotation.setY(0);
                        cameraRotation.setZ(0);

                        camRotZ = 0.0f;

                        getCamera()
                            .setPosition(savedCameraPosition);

                        break;

                    // camera motion
                    case KeyEvent.VK_W:
                        cameraMotion.setZ(
                            CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_S:
                        cameraMotion.setZ(
                            -CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_A:
                        cameraMotion.setX(
                            CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_D:
                        cameraMotion.setX(
                            -CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_PAGE_UP:
                        cameraMotion.setY(
                            CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_PAGE_DOWN:
                        cameraMotion.setY(
                            -CAMERA_MOTION_INCREMENT * cameraMultiplier);

                        break;

                    // camera rotation
                    case KeyEvent.VK_NUMPAD4:
                        cameraRotation.setX(
                            -CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_NUMPAD6:
                        cameraRotation.setX(
                            CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_NUMPAD8:
                        cameraRotation.setY(
                            -CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_NUMPAD2:
                        cameraRotation.setY(
                            CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_NUMPAD7:
                        cameraRotation.setZ(
                            CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    case KeyEvent.VK_NUMPAD9:
                        cameraRotation.setZ(
                            -CAMERA_ROTATION_INCREMENT * cameraMultiplier);

                        break;

                    default:

                        // do nothing
                        break;
                }
            }

            super.keyPressed(e);
        }

        /**
         * Called when a key has been released with the corresponding event
         *
         * @param e An event object describing the key release
         */
        public void keyReleased(java.awt.event.KeyEvent e)
        {
            if (isFlying())
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_SHIFT:
                        cameraMultiplier = 1.0f;

                        break;

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_S:
                        cameraMotion.setZ(0);

                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_D:
                        cameraMotion.setX(0);

                        break;

                    case KeyEvent.VK_PAGE_UP:
                    case KeyEvent.VK_PAGE_DOWN:
                        cameraMotion.setY(0);

                        break;

                    case KeyEvent.VK_NUMPAD4:
                    case KeyEvent.VK_NUMPAD6:
                        cameraRotation.setX(0);

                        break;

                    case KeyEvent.VK_NUMPAD2:
                    case KeyEvent.VK_NUMPAD8:
                        cameraRotation.setY(0);

                        break;

                    case KeyEvent.VK_NUMPAD7:
                    case KeyEvent.VK_NUMPAD9:
                        cameraRotation.setZ(0);

                        break;

                    default:

                        // do nothing
                        break;
                }
            }

            super.keyReleased(e);
        }
    }

    /**
     * Enter fly mode, giving the user a free camera
     */
    public void beginFly()
    {
        // to avoid confusion with coordinate systems etc.
        objectRotation = 0.0f;
        objectRotationSpeed = 0.0f;
        objectTilt = 0.0f;

        flying = true;
    }

    /**
     * Exit fly mode fixing the camera
     */
    public void endFly()
    {
        flying = false;
    }

    /**
     * If there is any cached data e.g. display lists then remove it and
     * force a redraw
     */
    public void forceRedraw()
    {
        if (displayListIndex != 0)
        {
            final GL2 gl = (GL2)canvas.getGL();

            // NVIDIA driver seems to crash when a list is deleted, don't like
            // doing this but it looks like the only way for now given the
            // high speed that the driver can run at with display lists.
            if (!parameters.isDisableGLDeleteList())
            {
                gl.glDeleteLists(displayListIndex, 1);
            }

            displayListIndex = 0;
        }
    }

    /**
     * Set the mesh that is being rendered by this viewer.
     *
     * @param meshes The list of meshes to be displayed
     */
    public void setMeshes(List<TriangleMesh> meshes)
    {
        this.meshes = meshes;

        forceRedraw();
    }

    /**
     * Draw a frame with the specified camera and object parameters.
     *
     * Note this isn't currently being used as I'm using a slightly different
     * rendering model with JOGL than the original C++ code did. It's left
     * here for completeness - MS.
     *
     * @param p The camera position
     * @param l The camera lookat point (scene centre)
     * @param u The camera up vector
     * @param r The object rotation
     * @param t The object tilt
     */
    public void drawFrame(XYZ p, XYZ l, XYZ u, float r, float t)
    {
        ++frame;

        cameraPosition = p;
        cameraLookAt = l;
        cameraUp = u;
        objectRotation = r;
        objectTilt = t;

        //updateGL();
        // ?????
        canvas.repaint();
    }

    /**
     * Initialize our use of JOGL. We set up the client state ready to begin
     * rendering.
     *
     * @param gLAutoDrawable The drawable that will be used for output
     */
    public void init(GLAutoDrawable gLAutoDrawable)
    {
        final GL2 gl = (GL2)gLAutoDrawable.getGL();
        final GLU glu = new GLU();

        log.debug("Init GL using: " + gl.getClass().getName());

        // zzing do not add to here! as per http://stackoverflow.com/questions/8465401/why-glautodrawable-doesnt-have-the-method-addmouselistener
        // need to move it else where
        //gLAutoDrawable.addKeyListener(new MyKeyAdapter());
        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Switch depth-buffering on
        gl.glEnable(GL.GL_DEPTH_TEST);

        // Basic lighting stuff (set ambient globally rather than in light)
        final float[] blackLight = { 0.0f, 0.0f, 0.0f, 1.0f };

        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, blackLight, 0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPECULAR, blackLight, 0);
        gl.glEnable(GLLightingFunc.GL_LIGHT0);
        gl.glEnable(GLLightingFunc.GL_LIGHTING);

        // Do smooth shading 'cos colours are specified at vertices
        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);

        // Don't waste time on back-facers
        gl.glFrontFace(GL.GL_CCW);
        gl.glCullFace(GL.GL_BACK);
        gl.glEnable(GL.GL_CULL_FACE);

        // Use arrays of data
        gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
        gl.glEnableClientState(GLPointerFunc.GL_NORMAL_ARRAY);

        log.debug("Init GL complete.");
    }

    // mods
    private int frames = 0;

    // end of mods

    /**
     * Called to display a frame. This carries out the rendering of the scene
     * using the drawable object.
     *
     * @param gLAutoDrawable The GL drawable object that is to be used for
     * rendering
     */
    public void display(GLAutoDrawable gLAutoDrawable)
    {
        final GL2 gl = (GL2)gLAutoDrawable.getGL();
        final GLU glu = new GLU();

        if (parameters.isEnableFog())
        {
            // clear to fog background colour
            final FloatRGBA fogCol = parameters.getFogColour();

            final float[] colBuffer = new float[3];

            colBuffer[0] = fogCol.getR();
            colBuffer[1] = fogCol.getG();
            colBuffer[2] = fogCol.getB();

            gl.glClearColor(colBuffer[0], colBuffer[1], colBuffer[2], 1.0f);

            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

            gl.glEnable(GL2.GL_FOG);

            gl.glFogi(GL2.GL_FOG_MODE, GL.GL_LINEAR);

            gl.glFogfv(GL2.GL_FOG_COLOR, colBuffer, 0);
            gl.glFogf(GL2.GL_FOG_DENSITY, 0.35f);
            gl.glFogf(GL2.GL_FOG_START, 0.0f);
            gl.glFogf(GL2.GL_FOG_END, parameters.getFogDistance());
        }
        else
        {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            gl.glDisable(GL2.GL_FOG);

            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        }

        // set up ambient light
        final float a = parameters.getAmbient();

        final float[] globalAmbient = { a, a, a, 1.0f };
        gl.glLightModelfv(GL2ES1.GL_LIGHT_MODEL_AMBIENT, globalAmbient, 0);

        final float[] lightDiffuse = { 1.0f - a, 1.0f - a, 1.0f - a, 1.0f };
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, lightDiffuse, 0);

        // position and orient the camera
        getCamera()
            .moveLeft(cameraMotion.getX());
        getCamera()
            .moveUp(cameraMotion.getY());
        getCamera()
            .moveBackward(cameraMotion.getZ());

        camRotZ += cameraRotation.getZ();

        getCamera()
            .rotateEye(cameraRotation.getX(), cameraRotation.getY());
        getCamera()
            .rotateAxis(camRotZ, 0.0f, 0.0f, 1.0f);

        //log.debug(camRotZ);
        getCamera()
            .lookAtScene();

        //        float lightPosition[] = {-2.0f, -3.0f, 1.0f, 0.0f };
        //        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0);

        // set up sunlight
        final XYZ sunpos = parameters.getSunPosition();

        final float[] lightPosition = new float[4];

        lightPosition[0] = sunpos.getX();
        lightPosition[1] = sunpos.getY();
        lightPosition[2] = sunpos.getZ();

        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPosition, 0);

        final FloatRGBA lightColour = parameters.getSunColour();

        lightPosition[0] = lightColour.getR();
        lightPosition[1] = lightColour.getG();
        lightPosition[2] = lightColour.getB();
        lightPosition[0] = 1.0f;

        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, lightPosition, 0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_SPECULAR, lightPosition, 0);

        objectRotation += objectRotationSpeed;

        objectRotation %= 360.0f;

        gl.glRotatef(objectTilt, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(objectRotation, 0.0f, 0.0f, 1.0f);

        int drawMode = GL2GL3.GL_FILL;

        if (parameters.isWireframe())
        {
            drawMode = GL2GL3.GL_LINE;
        }

        // end of mods
        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, drawMode);

        gl.glEnable(gl.GL_CULL_FACE);
        gl.glFrontFace(gl.GL_CCW);

        if (parameters.isDisplayList() && (displayListIndex != 0))
        {
            gl.glCallList(displayListIndex);
        }
        else
        {
            final boolean buildingList =
                parameters.isDisplayList() && (displayListIndex == 0);

            if (buildingList)
            {
                displayListIndex = gl.glGenLists(1);
                gl.glNewList(displayListIndex, GL2.GL_COMPILE);

                if (log.isDebugEnabled())
                {
                    log.debug("Building display list");
                }
            }

            final float[] defaultMaterialWhite = { 1.0f, 1.0f, 1.0f };
            final float[] defaultMaterialBlack = { 0.0f, 0.0f, 0.0f };

            gl.glMaterialfv(
                GL.GL_FRONT, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, defaultMaterialWhite, 0);
            gl.glMaterialfv(
                GL.GL_FRONT, GLLightingFunc.GL_EMISSION, defaultMaterialBlack, 0);

            boolean first = true;

            for (TriangleMesh mesh : meshes)
            {
                // Meshes after the first are rendered twice: first the backfacing polys then the front facing.
                // This solves the problem of either clouds disappearing when we're under them (with backface culling)
                // or weird stuff around the periphery when culling is on.
                // It's quite an expensive solution!
                final int passes = (first ? 1 : 2);

                for (int pass = 0; pass < passes; pass++)
                {
                    if (passes == 2 && pass == 0)
                    {
                        gl.glCullFace(gl.GL_FRONT);
                    }
                    else
                    {
                        gl.glCullFace(gl.GL_BACK);
                    }

                    if (mesh.getEmissive() == 0.0)
                    {
                        // Switch blending on for non-emissive meshes after the first
                        if (!first)
                        {
                            gl.glEnable(gl.GL_BLEND);
                            gl.glBlendFunc(
                                gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
                        }
                        else
                        {
                            gl.glDisable(gl.GL_BLEND);
                        }

                        // Use "Color Material" mode 'cos everything is the same
                        // material.... just change the colour
                        gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);
                        gl.glColorMaterial(
                            GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE);

                        // Point GL at arrays of data
                        final FloatBuffer vertBuffer =
                            mesh.getVertices()
                                .getPositionBuffer();
                        vertBuffer.position(0);

                        gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertBuffer);

                        final FloatBuffer normBuffer =
                            mesh.getVertices()
                                .getNormalBuffer();
                        normBuffer.position(0);

                        gl.glNormalPointer(GL.GL_FLOAT, 0, normBuffer);

                        final Buffer colBuffer =
                            mesh.getVertices()
                                .getColourBuffer();
                        colBuffer.position(0);

                        if (first)
                        {
                            gl.glColorPointer(3, GL.GL_BYTE, 8, colBuffer);
                        }
                        else
                        {
                            gl.glColorPointer(4, GL.GL_BYTE, 8, colBuffer);
                        }

                        // Draw the colour-zero triangles
                        final Buffer triBuffer =
                            mesh.getTriangles()
                                .getBuffer();

                        triBuffer.position(0);

                        gl.glDrawElements(
                            GL.GL_TRIANGLES, 3 * mesh.getTriangleColour0Count(),
                            GL.GL_UNSIGNED_INT, triBuffer);

                        // Switch to alternate colour and draw the colour-one triangles
                        colBuffer.position(4);

                        if (first)
                        {
                            gl.glColorPointer(3, GL.GL_BYTE, 8, colBuffer);
                        }
                        else
                        {
                            gl.glColorPointer(4, GL.GL_BYTE, 8, colBuffer);
                        }

                        triBuffer.position(mesh.getTriangleColour0Count() * 3);

                        gl.glDrawElements(
                            GL.GL_TRIANGLES, 3 * mesh.getTriangleColour1Count(),
                            GL.GL_UNSIGNED_INT, triBuffer);

                        gl.glDisable(GLLightingFunc.GL_COLOR_MATERIAL);
                    }
                    else
                    {
                        // We abuse alpha for emission, so no blending
                        gl.glDisable(gl.GL_BLEND);

                        // If there could be emissive vertices, we need to do things the hard way.
                        final float k = 1.0f / 255.0f;
                        final float em = k * (mesh.getEmissive());
                        final float ad = k * (1.0f - mesh.getEmissive());

                        gl.glBegin(GL.GL_TRIANGLES);

                        final float[] cAmbDiff = new float[3];
                        final float[] cEmissive = new float[3];

                        for (int t = 0; t < mesh.getTriangles()
                                .size(); ++t)
                        {
                            int c = 0;

                            if (t >= mesh.getTriangleColour0Count())
                            {
                                c = 1;
                            }

                            for (int i = 0; i < 3; ++i)
                            {
                                final int vn =
                                    mesh.getTriangles()
                                        .get(t)
                                        .getVertex(i);
                                final Vertex v = mesh.getVertices()
                                        .get(vn);

                                final ByteRGBA col = v.getColour(c);

                                if (v.getEmissive(c))
                                {
                                    cAmbDiff[0] = col.getR() * ad;
                                    cAmbDiff[1] = col.getG() * ad;
                                    cAmbDiff[2] = col.getB() * ad;
                                    cEmissive[0] = col.getR() * em;
                                    cEmissive[1] = col.getG() * em;
                                    cEmissive[2] = col.getB() * em;
                                }
                                else
                                {
                                    cAmbDiff[0] = col.getR() * k;
                                    cAmbDiff[1] = col.getG() * k;
                                    cAmbDiff[2] = col.getB() * k;
                                    cEmissive[0] = 0.0f;
                                    cEmissive[1] = 0.0f;
                                    cEmissive[2] = 0.0f;
                                }

                                gl.glNormal3f(
                                    v.getNormal().getX(), v.getNormal().getY(),
                                    v.getNormal().getZ());
                                gl.glMaterialfv(
                                    GL.GL_FRONT_AND_BACK,
                                    GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, cAmbDiff, 0);
                                gl.glMaterialfv(
                                    GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_EMISSION,
                                    cEmissive, 0);
                                gl.glVertex3f(
                                    v.getPosition().getX(),
                                    v.getPosition().getY(),
                                    v.getPosition().getZ());
                            }
                        }

                        gl.glEnd();
                    }
                }

                first = false;
            }

            if (buildingList)
            {
                gl.glEndList();

                if (log.isDebugEnabled())
                {
                    log.debug("... built display list");
                }
            }
        }

        gl.glFlush();
    }

    /**
     * Called when the GL drawable has been resized
     *
     * @param gLAutoDrawable The drawable being used for rendering
     * @param x The new x coordinate of the drawable
     * @param y The new y coordinate of the drawable
     * @param w The new width of the drawable
     * @param h The new height of the drawable
     */
    public void reshape(
        GLAutoDrawable gLAutoDrawable, int x, int y, int w, int h)
    {
        final GL2 gl = (GL2)gLAutoDrawable.getGL();
        final GLU glu = new GLU();

        log.debug("Viewer GL reshape to: " + w + ", " + h);

        log.debug("OS NAME: " + System.getProperty("os.name"));
        log.debug("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        log.debug("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        log.debug("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        // allow for bug???? / unimplemented????? glDeleteList() on linux
        // nvidia driver.
        if (
            System.getProperty("os.name")
                .equals("Linux") &&
                gl.glGetString(GL.GL_VENDOR)
                .equals("NVIDIA Corporation"))
        {
            parameters.setDisableGLDeleteList(true);

            log.warn("NVIDIA/Linux: Display lists will not be freed if used.");
        }

        width = w;
        height = h;

        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();

        double vAngle = 45.0;

        if (width <= height)
        {
            vAngle = (45.0 * height) / width;
        }

        // View angle is specified in vertical direction, but we need
        // to exaggerate it if image is taller than wide.
        final float viewAngleDegrees = (float) Math.min(90.0, vAngle);

        // Was 0.1 (too far); 0.001 gives artefacts
        glu.gluPerspective(
            viewAngleDegrees, (float) width / (float) height, 0.01, 10.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

        getCamera()
            .setManagePerspective(false);
        getCamera()
            .on();

        // updateGL();
        // ?????
        canvas.repaint();

        log.debug("GL reshape complete");
    }

    /**
     * Called when the display mode or device associated with our drawable has
     * changed.
     *
     * @param gLAutoDrawable The drawable object we're using
     * @param b <pre>true</pre> if there has been a display mode change else
     * <pre>false</pre>
     * @param b0 <pre>true</pre> If there has been a device change otherwise
     * <pre>false</pre>
     */
    public void displayChanged(
        GLAutoDrawable gLAutoDrawable, boolean b, boolean b0)
    {
        // not currently implemented by JOGL
        display(gLAutoDrawable);
    }

    /**
     * Getter for property canvas.
     * @return Value of property canvas.
     */
    public GLCanvas getCanvas()
    {
        return this.canvas;
    }

    /**
     * Sets the tilt angle that the object is drawn at
     *
     * @param tilt A new tilt angle in degrees
     */
    public void setObjectTilt(float tilt)
    {
        this.objectTilt = tilt;
    }

    /**
     * Sets the rotation angle that the object is drawn at
     *
     * @param rotation The new rotation angle in degrees
     */
    public void setObjectRotation(float rotation)
    {
        this.objectRotation = rotation;
    }

    /**
     * Holds value of property objectRotationSpeed.
     */
    private float objectRotationSpeed = 0.0f;

    /**
     * Getter for property objectRotationSpeed.
     * @return Value of property objectRotationSpeed.
     */
    public float getObjectRotationSpeed()
    {
        return this.objectRotationSpeed;
    }

    /**
     * Setter for property objectRotationSpeed.
     * @param objectRotationSpeed New value of property objectRotationSpeed.
     */
    public void setObjectRotationSpeed(float objectRotationSpeed)
    {
        this.objectRotationSpeed = objectRotationSpeed;
    }

    /**
     * Sets the rendering parameters in use by this viewer
     *
     * @param param The new set of rendering parameters to be used
     */
    public void setRenderParameters(RenderParameters param)
    {
        this.parameters = param;
    }

    /**
     * Getter for property meshes.
     * @return Value of property meshes.
     */
    public List<TriangleMesh> getMeshes()
    {
        return meshes;
    }

    /**
     * Retrieve the current camera position of the viewer. This is a convenience
     * method and could be removed as access to the camera object is already
     * provided
     *
     * @return The current camera position object
     */
    public CameraPosition getCameraPosition()
    {
        return getCamera()
            .getPosition();
    }

    /**
     * Set the camera position in the viewer to the specified one
     *
     * @param cameraPosition An object describing the new position that the
     * camera is to be given
     */
    public void setCameraPosition(CameraPosition cameraPosition)
    {
        getCamera()
            .setPosition(cameraPosition);
    }

    /**
     * Get the camera for this display
     *
     * @return The camera object used by the display.
     */
    public Camera getCamera()
    {
        return camera;
    }

    /**
     * Test whether the fly mode is on or off
     *
     * @return <pre>true</pre> if the fly mode is on otherwise <pre>false</pre>
     */
    public boolean isFlying()
    {
        return flying;
    }
    
    public void dispose(GLAutoDrawable drawable) 
    {} // zzing  release all opengl resources
}
