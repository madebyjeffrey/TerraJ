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
 * FloatRGBA.java
 *
 * Created on December 28, 2005, 1:32 PM
 *
 */
package com.alvermont.terraj.fracplanet.colour;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to represent rgb colours with floating point storage
 *
 * Note the various methods in this class of the form opXXXXX are translations
 * of the original C++ operator overloads.
 *
 *
 * @author martin
 * @version $Id: FloatRGBA.java,v 1.5 2006/07/06 06:59:43 martin Exp $
 */
public class FloatRGBA implements Serializable
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(FloatRGBA.class);

    /** The red value */
    private float r;

    /** The green value */
    private float g;

    /** The blue value */
    private float b;

    /** The alpha value */
    private float a;

    /**
     * Creates a new instance of FloatRGBA
     */
    public FloatRGBA()
    {
    }

    /**
     * Creates a new instance of FloatRGBA as a copy of an existing object
     *
     *
     * @param c The FloatRGBA object to initialize the new instance from
     */
    public FloatRGBA(FloatRGBA c)
    {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
    }

    /**
     * Creates a new instance of FloatRGBA with the specified floating point
     * colour values
     *
     *
     * @param r The red colour value
     * @param g The green colour value
     * @param b The blue colour value
     */
    public FloatRGBA(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0f;
    }

    /**
     * Creates a new instance of FloatRGBA with the specified floating point
     * colour values
     *
     *
     * @param a The alpha value
     * @param r The red colour value
     * @param g The green colour value
     * @param b The blue colour value
     */
    public FloatRGBA(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Creates a new instance of FloatRGBA from an existing ByteRGBA object
     *
     *
     *
     * @param orig The FloatRGBA object to initialize the new instance from
     */
    public FloatRGBA(ByteRGBA orig)
    {
        this.r = ((float) orig.getR()) / Byte.MAX_VALUE;
        this.g = ((float) orig.getG()) / Byte.MAX_VALUE;
        this.b = ((float) orig.getB()) / Byte.MAX_VALUE;
        this.a = ((float) orig.getA()) / Byte.MAX_VALUE;
    }

    /**
     * Add operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour to be added to this one.
     */
    public void opAddAssign(FloatRGBA v)
    {
        this.r += v.r;
        this.g += v.g;
        this.b += v.b;
        this.a += v.a;
    }

    /**
     * Subtract operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour to be subtracted from this one.
     */
    public void opSubtractAssign(FloatRGBA v)
    {
        this.r -= v.r;
        this.g -= v.g;
        this.b -= v.b;
        this.a -= v.a;
    }

    /**
     * Multiply operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour this one is to be multipled by
     */
    public void opMultiplyAssign(FloatRGBA v)
    {
        this.r *= v.r;
        this.g *= v.g;
        this.b *= v.b;
        this.a *= v.a;
    }

    /**
     * Multiply operation for a double value. The result is assigned to this object
     *
     * @param k The amount each component of this colour is to be multipled by
     */
    public void opMultiplyAssign(double k)
    {
        this.r *= k;
        this.g *= k;
        this.b *= k;
        this.a *= k;
    }

    /**
     * Test whether this object is equal to another FloatRGBA object. They are
     * equal if each component of the colour is the same.
     *
     *
     * @param f1 The first colour to compare
     * @param f2 The second colour to compare
     * @return <code>true</code> if the colour objects are equal otherwise
     * <code>false</code>
     */
    public boolean opEquals(FloatRGBA f1, FloatRGBA f2)
    {
        return (f1.r == f2.r) && (f1.g == f2.g) && (f1.b == f2.b) &&
            (f1.a == f2.a);
    }

    /**
     * Test whether this object is not equal to another FloatRGBA object. They are
     * equal if each component of the colour is the same.
     *
     *
     * @param f1 The first colour to compare
     * @param f2 The second colour to compare
     * @return <code>true</code> if the colour objects are not equal otherwise
     * <code>false</code>
     */
    public boolean opNotEquals(FloatRGBA f1, FloatRGBA f2)
    {
        return (f1.r != f2.r) || (f1.g != f2.g) || (f1.b != f2.b) ||
            (f1.a != f2.a);
    }

    /**
     * Test whether this object is equal to another one following the
     * standard rules for this comparison in java. e.g
     *
     * X == X is always true
     * X == null is always false
     *
     *
     * @param other The object to be compared
     * @return <code>true</code> if the other object is a FloatRBB and is
     * equal to this one
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

        final FloatRGBA o = (FloatRGBA) other;

        return opEquals(this, o);
    }

    /**
     * Subtract operation for FloatRGBA. A new object is returned, neither
     * input object is modified.
     *
     *
     * @param a The first object in the subtraction
     * @param b The second object in the subtraction
     * @return A new object containing the result of a-b
     */
    public static FloatRGBA opSubtract(FloatRGBA a, FloatRGBA b)
    {
        return new FloatRGBA(a.r - b.r, a.g - b.g, a.b - b.b, a.a - b.a);
    }

    /**
     * Negate unary operation for FloatRGBA. A new object is returned, the
     * input object is not modified.
     *
     *
     * @param c The FloatRGBA to be negated
     * @return A new FloatRGBA object containing the result of the negation
     */
    public static FloatRGBA opNegate(FloatRGBA c)
    {
        return new FloatRGBA(-c.r, -c.g, -c.b);
    }

    /**
     * Add operation for FloatRGBA. A new object is returned, neither
     * input object is modified.
     *
     *
     * @param a The first object in the addition
     * @param b The second object in the addition
     * @return A new object containing the result of a+b
     */
    public static FloatRGBA opAdd(FloatRGBA a, FloatRGBA b)
    {
        return new FloatRGBA(a.r + b.r, a.g + b.g, a.b + b.b, a.a + b.a);
    }

    /**
     * Multiply operation for FloatRGBA. A new object is returned, neither
     * input object is modified.
     *
     *
     * @param c The colour object to be multiplied
     * @param k A value to multiply the colour by
     * @return A new object containing the result of the multiplication
     */
    public static FloatRGBA opMultiply(float k, FloatRGBA c)
    {
        return new FloatRGBA(k * c.r, k * c.g, k * c.b, k * c.a);
    }

    /**
     * Multiply operation for FloatRGBA. A new object is returned, neither
     * input object is modified.
     *
     *
     * @param c The colour object to be multiplied
     * @param k A value to multiply the colour by
     * @return A new object containing the result of the multiplication
     */
    public static FloatRGBA opMultiply(FloatRGBA c, float k)
    {
        return opMultiply(k, c);
    }

    /**
     * Multiply operation for FloatRGBA. A new object is returned, neither
     * input object is modified.
     *
     *
     * @param f1 The first object in the multiplication
     * @param f2 The second object in the multiplication
     * @return A new object containing the result of f1*f2
     */
    public FloatRGBA opMultiply(FloatRGBA f1, FloatRGBA f2)
    {
        return new FloatRGBA(
            f1.r * f2.r, f1.g * f2.g, f1.b * f2.b, f1.a * f2.a);
    }

    /**
     * Getter for property r.
     * @return Value of property r.
     */
    public float getR()
    {
        return this.r;
    }

    /**
     * Getter for property g.
     * @return Value of property g.
     */
    public float getG()
    {
        return this.g;
    }

    /**
     * Getter for property b.
     * @return Value of property b.
     */
    public float getB()
    {
        return this.b;
    }

    /**
     * Setter for property b.
     * @param b New value of property b.
     */
    public void setB(float b)
    {
        this.b = b;
    }

    /**
     * Setter for property g.
     * @param g New value of property g.
     */
    public void setG(float g)
    {
        this.g = g;
    }

    /**
     * Setter for property r.
     * @param r New value of property r.
     */
    public void setR(float r)
    {
        this.r = r;
    }

    /**
     * Getter for property a.
     * @return Value of property a.
     */
    public float getA()
    {
        return this.a;
    }

    /**
     * Setter for property a.
     * @param a New value of property a.
     */
    public void setA(float a)
    {
        this.a = a;
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
        return (int) this.r + (int) this.g + (int) this.b + (int) this.a;
    }
}
