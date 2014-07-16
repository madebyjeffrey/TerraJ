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
 * ByteRGBA.java
 *
 * Created on December 28, 2005, 1:27 PM
 *
 */
package com.alvermont.terraj.fracplanet.colour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to represent red-green-blue colours stored with 8-bit resolution. Note
 * the various methods in this class of the form opXXXXX are translations of
 * the original C++ operator overloads.
 *
 *
 * @author martin
 * @version $Id: ByteRGBA.java,v 1.5 2006/07/06 06:59:43 martin Exp $
 */
public class ByteRGBA
{
    /**
     * Our logging object
     */
    private static Log log = LogFactory.getLog(ByteRGBA.class);

    /**
     * The red colour value
     */
    private byte r;

    /**
     * The green colour value
     */
    private byte g;

    /**
     * The green colour value
     */
    private byte b;

    /**
     * The alpha value
     */
    private byte a;

    /**
     * Constant colour value for black
     */
    public static final ByteRGBA BLACK = new ByteRGBA(0, 0, 0);

    /**
     * Constant colour value for red
     */
    public static final ByteRGBA RED = new ByteRGBA(Byte.MAX_VALUE, 0, 0);

    /**
     * Constant colour value for green
     */
    public static final ByteRGBA GREEN = new ByteRGBA(0, Byte.MAX_VALUE, 0);

    /**
     * Constant colour value for blue
     */
    public static final ByteRGBA BLUE = new ByteRGBA(0, 0, Byte.MAX_VALUE);

    /**
     * Creates a new instance of ByteRGBA
     */
    public ByteRGBA()
    {
    }

    /**
     * Creates a new instance of ByteRGBA as a copy of another one
     *
     * @param c The object that will be used to initilize this one
     */
    public ByteRGBA(ByteRGBA c)
    {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
    }

    /**
     * Creates a new instance of ByteRGBA from byte colour values
     *
     * @param r The colour value for red
     * @param g The colour value for green
     * @param b The colour value for blue
     */
    public ByteRGBA(byte r, byte g, byte b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = Byte.MAX_VALUE;
    }

    /**
     * Creates a new instance of ByteRGBA from byte colour values
     *
     * @param a The alpha value
     * @param r The colour value for red
     * @param g The colour value for green
     * @param b The colour value for blue
     */
    public ByteRGBA(byte r, byte g, byte b, byte a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Creates a new instance of ByteRGBA from int colour values
     *
     * @param r The colour value for red
     * @param g The colour value for green
     * @param b The colour value for blue
     */
    public ByteRGBA(int r, int g, int b)
    {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = Byte.MAX_VALUE;
    }

    /**
     * Creates a new instance of ByteRGBA from int colour values
     *
     * @param r The colour value for red
     * @param g The colour value for green
     * @param b The colour value for blue
     * @param a The alpha value
     */
    public ByteRGBA(int r, int g, int b, int a)
    {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
    }

    /**
     * Creates a new instance of ByteRGBA using the values stored in a FloatRGBA
     *
     * @param c The FloatRGBA object that will be used to initialize this one
     */
    public ByteRGBA(FloatRGBA c)
    {
        this(
            (byte) (Byte.MAX_VALUE * c.getR()),
            (byte) (Byte.MAX_VALUE * c.getG()),
            (byte) (Byte.MAX_VALUE * c.getB()),
            (byte) (Byte.MAX_VALUE * c.getA()));
    }

    /**
     * Add another ByteRGBA to this one and assign the result to this object
     *
     * @param v The value to be added to this one
     */
    public void opAddAssign(ByteRGBA v)
    {
        this.r += v.r;
        this.g += v.g;
        this.b += v.b;
        this.a += v.a;
    }

    /**
     * Subtract another ByteRGBA from this one and assign the result to this
     * object
     *
     *
     * @param v The value to be subtracted from this one
     */
    public void opSubtractAssign(ByteRGBA v)
    {
        this.r -= v.r;
        this.g -= v.g;
        this.b -= v.b;
        this.a -= v.a;
    }

    /**
     * Get the red colour value
     *
     * @return The red colour value as a byte
     */
    public byte getR()
    {
        return this.r;
    }

    /**
     * Get the green colour value
     *
     * @return The green colour value as a byte
     */
    public byte getG()
    {
        return this.g;
    }

    /**
     * Get the blue colour value
     *
     * @return The blue colour value as a byte
     */
    public byte getB()
    {
        return this.b;
    }

    /**
     * Get the alpha value
     *
     * @return The blue colour value as a byte
     */
    public byte getA()
    {
        return this.a;
    }
}
