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
 * XYZMath.java
 *
 * Created on January 1, 2006, 9:56 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factored the operations on XYZ out to here so classes that implement
 * XYZ don't have to duplicate them all.
 *
 * @author martin
 * @version $Id: XYZMath.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class XYZMath
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(XYZMath.class);

    /** Creates a new instance of XYZMath */
    protected XYZMath()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Multiply two vectors and return a new one
     *
     * @param a The first vector to multiply
     * @param b The second vector to multiply
     * @return The result of vector multiplication of the two vectors
     */
    public static XYZ opMultiply(XYZ a, XYZ b)
    {
        return new SimpleXYZ(
            (a.getY() * b.getZ()) - (a.getZ() * b.getY()),
            (a.getZ() * b.getX()) - (a.getX() * b.getZ()),
            (a.getX() * b.getY()) - (a.getY() * b.getX()));
    }

    /**
     * Compute the dot product of two vectors
     *
     * @param a The first vector to multiply
     * @param b The second vector to multiply
     * @return The resulting dot product of the two vectors
     */
    public static float opDotProduct(XYZ a, XYZ b)
    {
        return (a.getX() * b.getX()) + (a.getY() * b.getY()) +
        (a.getZ() * b.getZ());
    }

    /**
     * Add two vectors and return a new one
     *
     * @param a The first vector
     * @param b The second vector
     * @return The result of the vector addition
     */
    public static XYZ opAdd(XYZ a, XYZ b)
    {
        return new SimpleXYZ(
            a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }

    /**
     * Subtract two vectors and return a new one
     *
     * @param a The first vector
     * @param b The second vector
     * @return The result of the vector subtraction
     */
    public static XYZ opSubtract(XYZ a, XYZ b)
    {
        return new SimpleXYZ(
            a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    /**
     * Multiply a vector by a constant and return the result
     *
     * @param k The constant to multiply by
     * @param v The vector to be multiplied
     * @return A new vector containing the result of the multiplication
     */
    public static XYZ opMultiply(float k, XYZ v)
    {
        return new SimpleXYZ(k * v.getX(), k * v.getY(), k * v.getZ());
    }

    /**
     * Multiply a vector by a constant and return the result
     *
     * @param k The constant to multiply by
     * @param v The vector to be multiplied
     * @return A new vector containing the result of the multiplication
     */
    public static XYZ opMultiply(XYZ v, float k)
    {
        return opMultiply(k, v);
    }

    /**
     * Divide a vector by a constant and return the result
     *
     * @param k The constant to multiply by
     * @param v The vector to be multiplied
     * @return A new vector containing the result of the division
     */
    public static XYZ opDivide(XYZ v, float k)
    {
        return opMultiply(1.0f / k, v);
    }
}
