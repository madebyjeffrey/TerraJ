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
 * CameraPosition.java
 *
 * Created on January 12, 2006, 2:00 PM
 *
 */
package com.alvermont.terraj.fracplanet.render;

import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds the details (coordinates etc.) of a camera position.
 *
 * @author martin
 * @version $Id: CameraPosition.java,v 1.3 2006/07/06 06:58:36 martin Exp $
 */
public class CameraPosition implements Cloneable
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(CameraPosition.class);

    /** Creates a new instance of CameraPosition */
    public CameraPosition()
    {
        // default to a normally sensible position
        eye = new SimpleXYZ(0.0f, 2.0f, -15.0f);
        centre = new SimpleXYZ(0.0f, 0.0f, 0.0f);
        up = new SimpleXYZ(0.0f, 1.0f, 0.0f);

        eyeXRotation = 0.0f;
        eyeYRotation = 0.0f;

        name = "Untitled";
    }

    /**
     * Create a copy of a camera position object.
     *
     * Clone is also supported if the user prefers that style of operation.
     *
     * @param source The camera position to be copied.
     */
    public CameraPosition(CameraPosition source)
    {
        this.eye = new SimpleXYZ(source.getEye());
        this.centre = new SimpleXYZ(source.getCentre());
        this.up = new SimpleXYZ(source.getUp());

        this.eyeXRotation = source.eyeXRotation;
        this.eyeYRotation = source.eyeYRotation;

        this.name = source.name;
    }

    /**
     * Holds value of property eye.
     */
    private XYZ eye;

    /**
     * Getter for property eye.
     * @return Value of property eye.
     */
    public XYZ getEye()
    {
        return this.eye;
    }

    /**
     * Setter for property eye.
     * @param eye New value of property eye.
     */
    public void setEye(XYZ eye)
    {
        this.eye = eye;
    }

    /**
     * Holds value of property centre.
     */
    private XYZ centre;

    /**
     * Getter for property centre.
     * @return Value of property centre.
     */
    public XYZ getCentre()
    {
        return this.centre;
    }

    /**
     * Setter for property centre.
     * @param centre New value of property centre.
     */
    public void setCentre(XYZ centre)
    {
        this.centre = centre;
    }

    /**
     * Holds value of property up.
     */
    private XYZ up;

    /**
     * Getter for property up.
     * @return Value of property up.
     */
    public XYZ getUp()
    {
        return this.up;
    }

    /**
     * Setter for property up.
     * @param up New value of property up.
     */
    public void setUp(XYZ up)
    {
        this.up = up;
    }

    /**
     * Holds value of property eyeXRotation.
     */
    private float eyeXRotation;

    /**
     * Getter for property eyeXRotation.
     * @return Value of property eyeXRotation.
     */
    public float getEyeXRotation()
    {
        return this.eyeXRotation;
    }

    /**
     * Setter for property eyeXRotation.
     * @param eyeXRotation New value of property eyeXRotation.
     */
    public void setEyeXRotation(float eyeXRotation)
    {
        this.eyeXRotation = eyeXRotation;
    }

    /**
     * Holds value of property eyeYRotation.
     */
    private float eyeYRotation;

    /**
     * Getter for property eyeYRotation.
     * @return Value of property eyeYRotation.
     */
    public float getEyeYRotation()
    {
        return this.eyeYRotation;
    }

    /**
     * Setter for property eyeYRotation.
     * @param eyeYRotation New value of property eyeYRotation.
     */
    public void setEyeYRotation(float eyeYRotation)
    {
        this.eyeYRotation = eyeYRotation;
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
     * Creates a clone of this object. There is also a copy constructor
     * available which doesn't require the exception to be caught etc.
     *
     * @throws java.lang.CloneNotSupportedException If an ancestor of this object doesn't
     * support cloning. This is one of those "should never happen" things.
     * @return A new and independent copy of the object.
     */
    public Object clone() throws CloneNotSupportedException
    {
        // make a shallow copy
        final CameraPosition retValue = (CameraPosition) super.clone();

        // make this a deep copy
        retValue.eye = new SimpleXYZ(this.getEye());
        retValue.centre = new SimpleXYZ(this.getCentre());
        retValue.up = new SimpleXYZ(this.getUp());

        return retValue;
    }
}
