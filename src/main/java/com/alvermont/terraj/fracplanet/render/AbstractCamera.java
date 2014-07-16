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
 * Camera.java
 *
 * Created on January 12, 2006, 1:57 PM
 *
 */
package com.alvermont.terraj.fracplanet.render;

import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.geom.XYZMath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>This is a Java / JOGL port of the OpenGL Camera class originally written in
 * C++ by Stephen Manley - smanley at nyx dot net. You can get the original code from
 * his website at: http://www.nyx.net/~smanley/graphics/opengl.html
 * along with many other interesting OpenGL things.</p>
 *
 * <p>I didn't implement the save and load camera settings. You can just get the
 * camera position from it as a bean and serialize that to XML or whatever
 * persistence mechanism is required.</p>
 *
 * <p>This class is abstract as the maths doesn't depend on the rendering.
 * There is a subclass CameraJOGL for use with JOGL as the GL renderer.</p>
 *
 * @author martin
 * @version $Id: AbstractCamera.java,v 1.6 2006/07/06 06:58:36 martin Exp $
 */
public abstract class AbstractCamera
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(Camera.class);

    // these don't seem to be used currently

    //protected float nearl, farl, left, right, top, bottom;
    //protected float aspect;
    //protected float fov;

    /** The current normal to the eye position */
    private XYZ eyeNormal = new SimpleXYZ(0.0f, 0.0f, 0.0f);

    /** The current vector for the view to scene centre */
    private XYZ view = new SimpleXYZ(0.0f, 0.0f, 0.0f);

    /** Creates a new instance of Camera */
    public AbstractCamera()
    {
        this.cameraNumber = 0;
        this.setPosition(new CameraPosition());

        calculateNormal();
    }

    /**
     * Creates a new instance of Camera
     *
     * @param position The initial camera position. Note that this position
     * will be cloned and subsequent changes to the camera position will not
     * affect the original object.
     */
    public AbstractCamera(CameraPosition position)
    {
        this.cameraNumber = 0;
        this.setPosition(new CameraPosition(position));

        calculateNormal();
    }

    /**
     * Calculate the normal for the camera
     */
    protected void calculateNormal()
    {
        // Determine viewing vector
        this.getView()
            .opAssign(
                XYZMath.opSubtract(
                    getPosition().getCentre(), getPosition().getEye()));

        // Calculate the cross product of the view and up vectors
        this.setEyeNormal(XYZMath.opMultiply(getView(), getPosition().getUp()));

        this.getEyeNormal()
            .normalise();
    }

    /**
     * Move the camera left by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveLeft(float dist)
    {
        // Left and right are along the normal vector to the camera - normals
        // only change under a rotation.
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(getEyeNormal(), -dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(this.getEyeNormal(), -dist));
    }

    /**
     * Move the camera right by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveRight(float dist)
    {
        // Left and right are along the normal vector to the camera - normals
        // only change under a rotation.
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(getEyeNormal(), dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(this.getEyeNormal(), dist));
    }

    /**
     * Move the camera up by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveUp(float dist)
    {
        // Up and down are along the camera's up and down vector
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(getPosition().getUp(), dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(getPosition().getUp(), dist));
    }

    /**
     * Move the camera down by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveDown(float dist)
    {
        // Up and down are along the camera's up and down vector
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(getPosition().getUp(), -dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(getPosition().getUp(), -dist));
    }

    /**
     * Move the camera forward by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveForward(float dist)
    {
        // Forward and back are along the viewing vector
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(this.getView(), -dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(this.getView(), -dist));
    }

    /**
     * Move the camera back by the specified distance
     *
     * @param dist The distance to move the camera
     */
    public void moveBackward(float dist)
    {
        // Forward and back are along the viewing vector
        getPosition()
            .getEye()
            .opAddAssign(XYZMath.opMultiply(this.getView(), dist));

        getPosition()
            .getCentre()
            .opAddAssign(XYZMath.opMultiply(this.getView(), dist));
    }

    /**
     * Move the eye position by a specified amount
     *
     * @param dx The distance to move the eye point in the X axis
     * @param dy The distance to move the eye point in the Y axis
     * @param dz The distance to move the eye point in the Z axis
     */
    public void moveEye(float dx, float dy, float dz)
    {
        getPosition()
            .getEye()
            .setX(getPosition().getEye().getX() + dx);
        getPosition()
            .getEye()
            .setY(getPosition().getEye().getY() + dy);
        getPosition()
            .getEye()
            .setZ(getPosition().getEye().getZ() + dz);
    }

    /**
     * Move the eye position by a specified vector
     *
     * @param delta The distance to move the eye point in XYZ
     */
    public void moveEye(XYZ delta)
    {
        getPosition()
            .getEye()
            .opAddAssign(delta);
    }

    /**
     * Move the centre position by a specified amount
     *
     * @param dx The distance to move the centre point in the X axis
     * @param dy The distance to move the centre point in the Y axis
     * @param dz The distance to move the centre point in the Z axis
     */
    public void moveCentre(float dx, float dy, float dz)
    {
        getPosition()
            .getCentre()
            .setX(getPosition().getCentre().getX() + dx);
        getPosition()
            .getCentre()
            .setY(getPosition().getCentre().getY() + dy);
        getPosition()
            .getCentre()
            .setZ(getPosition().getCentre().getZ() + dz);
    }

    /**
     * Move the centre position by a specified vector
     *
     * @param delta The distance to move the centre position in XYZ
     */
    public void moveCentre(XYZ delta)
    {
        getPosition()
            .getCentre()
            .opAddAssign(delta);
    }

    // abstract methods that need to be implemented

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param degrees The amount of desired rotation
     * @param x the X component of this rotation
     * @param y the Y component of this rotation
     * @param z the Z component of this rotation
     */
    public abstract void rotateAxis(float degrees, float x, float y, float z);

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param x The X coordinate of the rotation point
     * @param y The Y coordinate of the rotation point
     * @param z The Z coordinate of the rotation point
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    public abstract void rotatePoint(
        float x, float y, float z, float dx, float dy);

    /**
     * Rotates about the eye - changes the scene center
     *
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    public abstract void rotateEye(float dx, float dy);

    /**
     * Turn this camera on. Initializes the projection as required
     */
    public abstract void on();

    /**
     * Turn this camera off.
     */
    public abstract void off();

    /**
     * Test whether this camera has been turned on
     *
     * @return <pre>true</pre> if this camera is on otherwise <pre>false</pre>
     */
    public abstract boolean isOn();

    /**
     * Carry out the rendering operations to set up the camera ready to
     * draw a frame. Modifies the OpenGL projection etc.
     */
    public abstract void lookAtScene();

    /**
     * Holds value of property cameraNumber.
     */
    private int cameraNumber;

    /**
     * Getter for property cameraNumber.
     * @return Value of property cameraNumber.
     */
    public int getCameraNumber()
    {
        return this.cameraNumber;
    }

    /**
     * Holds value of property position.
     */
    private CameraPosition position;

    /**
     * Getter for property position.
     * @return Value of property position.
     */
    public CameraPosition getPosition()
    {
        return this.position;
    }

    /**
     * Setter for property position.
     * @param position New value of property position.
     */
    public void setPosition(CameraPosition position)
    {
        this.position = new CameraPosition(position);

        calculateNormal();
    }

    /**
     * Setter for property cameraNumber.
     * @param cameraNumber New value of property cameraNumber.
     */
    public void setCameraNumber(int cameraNumber)
    {
        this.cameraNumber = cameraNumber;
    }

    /**
     * Holds value of property managePerspective.
     */
    private boolean managePerspective = true;

    /**
     * Getter for property managePerspective.
     * @return Value of property managePerspective.
     */
    public boolean isManagePerspective()
    {
        return this.managePerspective;
    }

    /**
     * Setter for property managePerspective.
     * @param managePerspective New value of property managePerspective.
     */
    public void setManagePerspective(boolean managePerspective)
    {
        this.managePerspective = managePerspective;
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the eye normal vector for this camera
     *
     * @return The current eye normal vector for the camera
     */
    public XYZ getEyeNormal()
    {
        return this.eyeNormal;
    }

    /**
     * Set the eye normal vector for this camera
     *
     * @param eyeNormal The new eye normal vector for the camera
     */
    public void setEyeNormal(XYZ eyeNormal)
    {
        this.eyeNormal = eyeNormal;
    }

    /**
     * Get the view vector for this camera
     *
     * @return The current view vector for the camera
     */
    public XYZ getView()
    {
        return this.view;
    }

    /**
     * Set the view vector for this camera
     *
     * @param view The new view vector for the camera
     */
    public void setView(XYZ view)
    {
        this.view = view;
    }
}
