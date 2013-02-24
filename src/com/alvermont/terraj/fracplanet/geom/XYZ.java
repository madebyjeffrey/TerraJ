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
 * XYZ.java
 *
 * Created on January 1, 2006, 9:26 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;


/**
 * Interface for XYZ objects
 *
 * @author martin
 * @version $Id: XYZ.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public interface XYZ
{
    /**
     * Getter for property x.
     *
     * @return Value of property x.
     */
    float getX();

    /**
     * Getter for property y.
     *
     * @return Value of property y.
     */
    float getY();

    /**
     * Getter for property z.
     *
     * @return Value of property z.
     */
    float getZ();

    /**
     * Compute the magnitude of this vector
     *
     * @return The magnitude of this vector
     */
    float magnitude();

    /**
     * Compute the square of the magnitude of this vector
     *
     * @return The square of the magnitude of this vector
     */
    float magnitude2();

    /**
     * Normalise this vector. The vector is updated to contain the result
     */
    void normalise();

    /**
     * Return a new vector containing the normalised value of this one
     *
     * @return A normalised version of this vector
     */
    XYZ normalised();

    /**
     * Add a vector to this one and assign the result to this vector
     *
     * @param v The vector to be added to this one
     */
    void opAddAssign(XYZ v);

    /**
     * Assign another vector to this one
     *
     * @param v The vector to be assigned to this one
     */
    void opAssign(XYZ v);

    /**
     * Divide by a scalar constant and assign the result to this object
     *
     * @param k The constant value to divide by
     */
    void opDivideAssign(float k);

    /**
     * Test for equality between two vectors
     *
     * @param a The first object to be compared
     * @param b The second object to be compared
     * @return <pre>true</pre> if the objects are equal otherwise <pre>false</pre>
     */
    boolean opEquals(XYZ a, XYZ b);

    /**
     * Multiply by a scalar constant and assign the result to this object
     *
     * @param k The constant value to multiply by
     */
    void opMultiplyAssign(float k);

    /**
     * Return a new vector containing this one negated
     *
     * @return A new vector, the negation of this one
     */
    XYZ opNegate();

    /**
     * Test for inequality between two vectors
     *
     * @param a The first object to be compared
     * @param b The second object to be compared
     * @return <pre>true</pre> if the objects are not equal otherwise <pre>false</pre>
     */
    boolean opNotEquals(XYZ a, XYZ b);

    /**
     * Subtract a vector from this one and assign the result to this vector
     *
     * @param v The vector to be subtracted from this one
     */
    void opSubtractAssign(XYZ v);

    /**
     * Setter for property x.
     *
     * @param x New value of property x.
     */
    void setX(float x);

    /**
     * Setter for property y.
     *
     * @param y New value of property y.
     */
    void setY(float y);

    /**
     * Setter for property z.
     *
     * @param z New value of property z.
     */
    void setZ(float z);

    /**
     * Access value by element number 0=x, 1=y, 2=z
     *
     * @param element The desired element index
     * @return the corresponding element
     */
    float getElement(int element);
}
