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
 * CameraJOGL.java
 *
 * Created on January 12, 2006, 2:26 PM
 *
 */
package com.alvermont.terraj.fracplanet.render;

import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.geom.XYZMath;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the Camera for the JOGL library
 *
 * @author martin
 * @version $Id: CameraJOGL.java,v 1.7 2006/07/06 06:58:36 martin Exp $
 */
public class CameraJOGL extends AbstractCamera implements Camera
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(CameraJOGL.class);

    /** The size of the rotation array */
    private static final int ROTATE_ARRAY_SIZE = 16;

    // RequireThis OFF: ROTATE_ARRAY_SIZE

    /** The GL object we're using for rendering */
    private GL2 gl;

    /** Our GLU object */
    private GLU glu = new GLU();

    /** Indicates whether the camera is on */
    private boolean cameraOn = false;

    /**
     * Creates a new instance of CameraJOGL
     *
     * @param gl The GL context to be used by the camera
     */
    public CameraJOGL(GL2 gl)
    {
        super();

        this.gl = gl;
    }

    /**
     * Creates a new instance of CameraJOGL
     *
     * @param gl The GL context to be used by the camera
     * @param position The initial camera position
     */
    public CameraJOGL(GL2 gl, CameraPosition position)
    {
        super(position);

        this.gl = gl;
    }

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param degrees The amount of desired rotation
     * @param x the X component of this rotation
     * @param y the Y component of this rotation
     * @param z the Z component of this rotation
     */
    public void rotateAxis(float degrees, float x, float y, float z)
    {
        // Rotate the camera about an arbitary axis (for positioning)
        this.gl.glRotatef(degrees, x, y, z);  // zzing
        
    }

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param x The X coordinate of the rotation point
     * @param y The Y coordinate of the rotation point
     * @param z The Z coordinate of the rotation point
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    public void rotatePoint(float x, float y, float z, float dx, float dy)
    {
        // Calculate the vector from the current view to the point
        getPosition()
            .setCentre(new SimpleXYZ(0, 0, 0));
        calculateNormal();

        final float[] rotate = new float[ROTATE_ARRAY_SIZE];
        float newUpX;
        float newUpY;
        float newUpZ;
        float newSceneX;
        float newSceneY;
        float newSceneZ;
        float newSceneX1;
        float newSceneY1;
        float newSceneZ1;

        // Allow limiting of rotation (see main)
        getPosition()
            .setEyeXRotation(getPosition().getEyeXRotation() + dx);
        getPosition()
            .setEyeYRotation(getPosition().getEyeYRotation() + dx);

        // Rotate about the Up/Down Plane
        this.gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        this.gl.glPushMatrix();

        this.gl.glLoadIdentity();
        this.gl.glRotatef(
            dy, getEyeNormal().getX(), getEyeNormal().getY(),
            getEyeNormal().getZ());
        this.gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, rotate, 0);

        // MagicNumber OFF

        // We now need to calculate a new up direction
        newUpX = ((rotate[0] * getPosition()
                .getUp()
                .getX()) + (rotate[1] * getPosition()
                .getUp()
                .getY()) + (rotate[2] * getPosition()
                .getUp()
                .getZ()) + rotate[3]);
        newUpY = ((rotate[4] * getPosition()
                .getUp()
                .getX()) + (rotate[5] * getPosition()
                .getUp()
                .getY()) + (rotate[6] * getPosition()
                .getUp()
                .getZ()) + rotate[7]);
        newUpZ = ((rotate[8] * getPosition()
                .getUp()
                .getX()) + (rotate[9] * getPosition()
                .getUp()
                .getY()) + (rotate[10] * getPosition()
                .getUp()
                .getZ()) + rotate[11]);

        getPosition()
            .setUp(new SimpleXYZ(newUpX, newUpY, newUpZ));

        newSceneX = ((rotate[0] * -getView()
                .getX()) + (rotate[1] * -getView()
                .getY()) + (rotate[2] * -getView()
                .getZ()) + rotate[3]);
        newSceneY = ((rotate[4] * -getView()
                .getX()) + (rotate[5] * -getView()
                .getY()) + (rotate[6] * -getView()
                .getZ()) + rotate[7]);
        newSceneZ = ((rotate[8] * -getView()
                .getX()) + (rotate[9] * -getView()
                .getY()) + (rotate[10] * -getView()
                .getZ()) + rotate[11]);

        this.gl.glLoadIdentity();
        this.gl.glRotatef(
            dx, getPosition().getUp().getX(), getPosition().getUp().getY(),
            getPosition().getUp().getZ());
        this.gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, rotate, 0);
        this.gl.glPopMatrix();

        newSceneX1 = ((rotate[0] * newSceneX) + (rotate[1] * newSceneY) +
            (rotate[2] * newSceneZ) + rotate[3]);
        newSceneY1 = ((rotate[4] * newSceneX) + (rotate[5] * newSceneY) +
            (rotate[6] * newSceneZ) + rotate[7]);
        newSceneZ1 = ((rotate[8] * newSceneX) + (rotate[9] * newSceneY) +
            (rotate[10] * newSceneZ) + rotate[11]);

        // MagicNumber ON
        getPosition()
            .setEye(new SimpleXYZ(newSceneX1, newSceneY1, newSceneZ1));

        // Make sure the vectors are up to date
        calculateNormal();
    }

    /**
     * Rotates about the eye - changes the scene center
     *
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    public void rotateEye(float dx, float dy)
    {
        final float[] rotate = new float[ROTATE_ARRAY_SIZE];
        float newUpX;
        float newUpY;
        float newUpZ;
        float newSceneX;
        float newSceneY;
        float newSceneZ;
        float newSceneX1;
        float newSceneY1;
        float newSceneZ1;

        // Allow limiting of rotation (see main)
        getPosition()
            .setEyeXRotation(getPosition().getEyeXRotation() + dx);
        getPosition()
            .setEyeYRotation(getPosition().getEyeYRotation() + dy);

        // Rotate about the Up/Down Plane
        this.gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        this.gl.glPushMatrix();

        this.gl.glLoadIdentity();
        this.gl.glRotatef(
            dy, getEyeNormal().getX(), getEyeNormal().getY(),
            getEyeNormal().getZ());
        this.gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, rotate, 0);

        // MagicNumber OFF

        // We now need to calculate a new up direction
        newUpX = ((rotate[0] * getPosition()
                .getUp()
                .getX()) + (rotate[1] * getPosition()
                .getUp()
                .getY()) + (rotate[2] * getPosition()
                .getUp()
                .getZ()) + rotate[3]);
        newUpY = ((rotate[4] * getPosition()
                .getUp()
                .getX()) + (rotate[5] * getPosition()
                .getUp()
                .getY()) + (rotate[6] * getPosition()
                .getUp()
                .getZ()) + rotate[7]);
        newUpZ = ((rotate[8] * getPosition()
                .getUp()
                .getX()) + (rotate[9] * getPosition()
                .getUp()
                .getY()) + (rotate[10] * getPosition()
                .getUp()
                .getZ()) + rotate[11]);

        getPosition()
            .setUp(new SimpleXYZ(newUpX, newUpY, newUpZ));

        newSceneX = ((rotate[0] * getView()
                .getX()) + (rotate[1] * getView()
                .getY()) + (rotate[2] * getView()
                .getZ()) + rotate[3]);
        newSceneY = ((rotate[4] * getView()
                .getX()) + (rotate[5] * getView()
                .getY()) + (rotate[6] * getView()
                .getZ()) + rotate[7]);
        newSceneZ = ((rotate[8] * getView()
                .getX()) + (rotate[9] * getView()
                .getY()) + (rotate[10] * getView()
                .getZ()) + rotate[11]);

        this.gl.glLoadIdentity();
        this.gl.glRotatef(
            dx, getPosition().getUp().getX(), getPosition().getUp().getY(),
            getPosition().getUp().getZ());
        this.gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, rotate, 0);
        this.gl.glPopMatrix();

        newSceneX1 = ((rotate[0] * newSceneX) + (rotate[1] * newSceneY) +
            (rotate[2] * newSceneZ) + rotate[3]);
        newSceneY1 = ((rotate[4] * newSceneX) + (rotate[5] * newSceneY) +
            (rotate[6] * newSceneZ) + rotate[7]);
        newSceneZ1 = ((rotate[8] * newSceneX) + (rotate[9] * newSceneY) +
            (rotate[10] * newSceneZ) + rotate[11]);

        final XYZ newscene = new SimpleXYZ(newSceneX1, newSceneY1, newSceneZ1);

        // MagicNumber ON
        getPosition()
            .getCentre()
            .opAssign(XYZMath.opAdd(getPosition().getEye(), newscene));

        // Make sure the vectors are up to date
        calculateNormal();
    }

    /**
     * Turn this camera on. Initializes the projection as required
     */
    public void on()
    {
        // Initialize projection
        if (!cameraOn)
        {
            if (isManagePerspective())
            {
                this.gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                this.gl.glLoadIdentity();
                glu.gluPerspective(45.0, 1.0, 1.0, 200.0);
            }

            cameraOn = true;
        }

        lookAtScene();
    }

    /**
     * Carry out the rendering operations to set up the camera ready to
     * draw a frame. Modifies the OpenGL camera projection etc.
     */
    public void lookAtScene()
    {
        final XYZ eye = getPosition()
                .getEye();
        final XYZ centre = getPosition()
                .getCentre();
        final XYZ up = getPosition()
                .getUp();

        this.gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        this.gl.glLoadIdentity();
        glu.gluLookAt(
            eye.getX(), eye.getY(), eye.getZ(), centre.getX(), centre.getY(),
            centre.getZ(), up.getX(), up.getY(), up.getZ());
    }

    /**
     * Turn this camera off. Resets any effects on OpenGL
     */
    public void off()
    {
        if (this.cameraOn)
        {
            if (isManagePerspective())
            {
                this.gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                this.gl.glLoadIdentity();
            }

            this.gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
            this.gl.glLoadIdentity();

            this.cameraOn = false;
        }
    }

    /**
     * Test whether this camera has been turned on
     *
     * @return <pre>true</pre> if this camera is on otherwise <pre>false</pre>
     */
    public boolean isOn()
    {
        return this.cameraOn;
    }
}
