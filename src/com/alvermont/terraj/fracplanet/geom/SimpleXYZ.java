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
 * SimpleXYZ.java
 *
 * Created on December 28, 2005, 12:58 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a 3D coordinate
 *
 * @author martin
 * @version $Id: SimpleXYZ.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class SimpleXYZ implements XYZ
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(SimpleXYZ.class);

    /** Creates a new instance of SimpleXYZ */
    public SimpleXYZ()
    {
    }

    /**
     * Creates a new instance of SimpleXYZ as a copy of another one
     *
     * @param orig The object to create the copy from
     */
    public SimpleXYZ(XYZ orig)
    {
        this.x = orig.getX();
        this.y = orig.getY();
        this.z = orig.getZ();
    }

    /**
     * Creates a new instance of SimpleXYZ
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public SimpleXYZ(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Holds value of property x.
     */
    private float x;

    /**
     * Getter for property x.
     * @return Value of property x.
     */
    public float getX()
    {
        return this.x;
    }

    /**
     * Setter for property x.
     * @param x New value of property x.
     */
    public void setX(float x)
    {
        this.x = x;
    }

    /**
     * Holds value of property y.
     */
    private float y;

    /**
     * Getter for property y.
     * @return Value of property y.
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Setter for property y.
     * @param y New value of property y.
     */
    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * Holds value of property z.
     */
    private float z;

    /**
     * Getter for property z.
     * @return Value of property z.
     */
    public float getZ()
    {
        return this.z;
    }

    /**
     * Setter for property z.
     * @param z New value of property z.
     */
    public void setZ(float z)
    {
        this.z = z;
    }

    /**
     * Multiply by a scalar constant and assign the result to this object
     *
     * @param k The constant value to multiply by
     */
    public void opMultiplyAssign(float k)
    {
        this.x *= k;
        this.y *= k;
        this.z *= k;
    }

    /**
     * Divide by a scalar constant and assign the result to this object
     *
     * @param k The constant value to divide by
     */
    public void opDivideAssign(float k)
    {
        this.x /= k;
        this.y /= k;
        this.z /= k;
    }

    /**
     * Add a vector to this one and assign the result to this vector
     *
     * @param v The vector to be added to this one
     */
    public void opAddAssign(XYZ v)
    {
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
    }

    /**
     * Subtract a vector from this one and assign the result to this vector
     *
     * @param v The vector to be subtracted from this one
     */
    public void opSubtractAssign(XYZ v)
    {
        this.x -= v.getX();
        this.y -= v.getY();
        this.z -= v.getZ();
    }

    /**
     * Assign another vector to this one
     *
     * @param v The vector to be assigned to this one
     */
    public void opAssign(XYZ v)
    {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    /**
     * Return a new vector containing this one negated
     *
     * @return A new vector, the negation of this one
     */
    public XYZ opNegate()
    {
        return new SimpleXYZ(-this.x, -this.y, -this.z);
    }

    /**
     * Compute the square of the magnitude of this vector
     *
     * @return The square of the magnitude of this vector
     */
    public float magnitude2()
    {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    /**
     * Compute the magnitude of this vector
     *
     * @return The magnitude of this vector
     */
    public float magnitude()
    {
        return (float) Math.sqrt(magnitude2());
    }

    /**
     * Return a new vector containing the normalised value of this one
     *
     * @return A normalised version of this vector
     */
    public XYZ normalised()
    {
        final float m = magnitude();

        if (m == 0.0)
        {
            throw new AssertionError("Coordinate has zero magnitude");
        }

        return XYZMath.opDivide(this, m);
    }

    /**
     * Normalise this vector. The vector is updated to contain the result
     */
    public void normalise()
    {
        final XYZ n = normalised();

        opAssign(n);
    }

    /**
     * Test for equality between two vectors
     *
     * @param a The first object to be compared
     * @param b The second object to be compared
     * @return <pre>true</pre> if the objects are equal otherwise <pre>false</pre>
     */
    public boolean opEquals(XYZ a, XYZ b)
    {
        return (a.getX() == b.getX()) && (a.getY() == b.getY()) &&
            (a.getZ() == b.getZ());
    }

    /**
     * Test for inequality between two vectors
     *
     * @param a The first object to be compared
     * @param b The second object to be compared
     * @return <pre>true</pre> if the objects are not equal otherwise <pre>false</pre>
     */
    public boolean opNotEquals(XYZ a, XYZ b)
    {
        return (a.getX() != b.getX()) || (a.getY() != b.getY()) ||
            (a.getZ() != b.getZ());
    }

    /**
     * Implements the equals comparison for this object according to the
     * contract of this operation as detailed in the Java documentation.
     *
     * @param other The other object to be compared
     * @return <pre>true</pre> if the objects are equal
     */
    public boolean equals(Object other)
    {
        // basic checks first
        if (this == other)
        {
            return true;
        }

        if (other == null)
        {
            return false;
        }

        if (this.getClass() != other.getClass())
        {
            return false;
        }

        final XYZ o = (XYZ) other;

        return opEquals(this, o);
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return this.x + "," + this.y + "," + this.z;
    }

    /**
     *
     * Get a value by its element number 0=x, 1=y, 2=z
     *
     * @param element The element to be returned
     * @return The corresponding element value
     */
    public float getElement(int element)
    {
        switch (element)
        {
            case 0:
                return getX();

            case 1:
                return getY();

            case 2:
                return getZ();

            default:
                throw new IllegalArgumentException(
                    "Illegal request for " + "XYZ element: " + element);
        }
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p>
     * The general contract of <code>hashCode</code> is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the <tt>hashCode</tt> method
     *     must consistently return the same integer, provided no information
     *     used in <tt>equals</tt> comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the <tt>equals(Object)</tt>
     *     method, then calling the <code>hashCode</code> method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link java.lang.Object#equals(java.lang.Object)}
     *     method, then calling the <tt>hashCode</tt> method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hashtables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class <tt>Object</tt> does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.util.Hashtable
     */
    public int hashCode()
    {
        return (int) this.x + (int) this.y + (int) this.z;
    }
}
