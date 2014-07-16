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
 * ImmutableXYZ.java
 *
 * Created on January 2, 2006, 4:45 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for XYZ values that can't be modified after creation. Attempts to do
 * so will result in an <code>UnsupportedOperationException</code>
 *
 * @author martin
 * @version $Id: ImmutableXYZ.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public class ImmutableXYZ extends SimpleXYZ
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(ImmutableXYZ.class);

    /** Constant zero vector value */
    public static final XYZ XYZ_ZERO = new ImmutableXYZ(0.0f, 0.0f, 0.0f);

    /**
     * Creates an <code>ImmutableXYZ</code> object from an <code>XYZ</code>
     * object.
     *
     * @param orig The <code>XYZ</code> object to be used to initialize this one
     */
    public ImmutableXYZ(XYZ orig)
    {
        super(orig);
    }

    /**
     * Creates an <code>ImmutableXYZ</code> object from coordinate values
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    public ImmutableXYZ(final float x, final float y, final float z)
    {
        super(x, y, z);
    }

    /**
     * Setter for property z.
     *
     * @param z New value of property z.
     */
    public void setZ(final float z)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Setter for property y.
     *
     * @param y New value of property y.
     */
    public void setY(float y)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Setter for property x.
     *
     * @param x New value of property x.
     */
    public void setX(float x)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Subtract a vector from this one and assign the result to this vector
     *
     * @param v The vector to be subtracted from this one
     */
    public void opSubtractAssign(XYZ v)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Multiply by a scalar constant and assign the result to this object
     *
     * @param k The constant value to multiply by
     */
    public void opMultiplyAssign(float k)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Divide by a scalar constant and assign the result to this object
     *
     * @param k The constant value to divide by
     */
    public void opDivideAssign(float k)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Assign another vector to this one
     *
     * @param v The vector to be assigned to this one
     */
    public void opAssign(XYZ v)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Add a vector to this one and assign the result to this vector
     *
     * @param v The vector to be added to this one
     */
    public void opAddAssign(XYZ v)
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }

    /**
     * Normalise this vector. The vector is updated to contain the result
     */
    public void normalise()
    {
        throw new UnsupportedOperationException(
            "Cannot set values in this object");
    }
}
