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
 * NewInterface.java
 *
 * Created on 13 January 2006, 10:19
 */
package com.alvermont.terraj.fracplanet.render;

import com.alvermont.terraj.fracplanet.geom.XYZ;

/**
 * This is the interface to be implemented by objects that are Cameras
 *
 * @author  martin
 * @version $Id: Camera.java,v 1.2 2006/07/06 06:58:36 martin Exp $
 */
public interface Camera
{
    /**
     * Getter for property cameraNumber.
     *
     * @return Value of property cameraNumber.
     */
    int getCameraNumber();

    /**
     * Getter for property position.
     *
     * @return Value of property position.
     */
    CameraPosition getPosition();

    /**
     * Getter for property managePerspective.
     *
     * @return Value of property managePerspective.
     */
    boolean isManagePerspective();

    /**
     * Test whether this camera has been turned on
     *
     * @return <pre>true</pre> if this camera is on otherwise <pre>false</pre>
     */
    boolean isOn();

    /**
     * Carry out the rendering operations to set up the camera ready to
     * draw a frame. Modifies the OpenGL projection etc.
     */
    void lookAtScene();

    /**
     * Move the camera back by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveBackward(float dist);

    /**
     * Move the centre position by a specified vector
     *
     * @param delta The distance to move the centre position in XYZ
     */
    void moveCentre(XYZ delta);

    /**
     * Move the centre position by a specified amount
     *
     * @param dx The distance to move the centre point in the X axis
     * @param dy The distance to move the centre point in the Y axis
     * @param dz The distance to move the centre point in the Z axis
     */
    void moveCentre(float dx, float dy, float dz);

    /**
     * Move the camera down by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveDown(float dist);

    /**
     * Move the eye position by a specified vector
     *
     * @param delta The distance to move the eye point in XYZ
     */
    void moveEye(XYZ delta);

    /**
     * Move the eye position by a specified amount
     *
     * @param dx The distance to move the eye point in the X axis
     * @param dy The distance to move the eye point in the Y axis
     * @param dz The distance to move the eye point in the Z axis
     */
    void moveEye(float dx, float dy, float dz);

    /**
     * Move the camera forward by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveForward(float dist);

    /**
     * Move the camera left by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveLeft(float dist);

    /**
     * Move the camera right by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveRight(float dist);

    /**
     * Move the camera up by the specified distance
     *
     * @param dist The distance to move the camera
     */
    void moveUp(float dist);

    /**
     * Turn this camera off.
     */
    void off();

    /**
     * Turn this camera on. Initializes the projection as required
     */
    void on();

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param degrees The amount of desired rotation
     * @param x the X component of this rotation
     * @param y the Y component of this rotation
     * @param z the Z component of this rotation
     */
    void rotateAxis(float degrees, float x, float y, float z);

    /**
     * Rotates about the eye - changes the scene center
     *
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    void rotateEye(float dx, float dy);

    /**
     * Rotate the camera about an arbitary axis (for positioning)
     *
     * @param x The X coordinate of the rotation point
     * @param y The Y coordinate of the rotation point
     * @param z The Z coordinate of the rotation point
     * @param dx The desired X rotation
     * @param dy The desired Y rotation
     */
    void rotatePoint(float x, float y, float z, float dx, float dy);

    /**
     * Setter for property cameraNumber.
     *
     * @param cameraNumber New value of property cameraNumber.
     */
    void setCameraNumber(int cameraNumber);

    /**
     * Setter for property managePerspective.
     *
     * @param managePerspective New value of property managePerspective.
     */
    void setManagePerspective(boolean managePerspective);

    /**
     * Setter for property position.
     *
     * @param position New value of property position.
     */
    void setPosition(CameraPosition position);

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName();

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name);
}
