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
 * Matrix34.java
 *
 * Created on 18 April 2006, 14:10
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to hold 3x4 matrices
 *
 * @author  martin
 * @version $Id: Matrix34.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public class Matrix34
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(Matrix34.class);

    /**
     * Rotation below actually means any sort of origin preserving transform;
     * it's just easier to visualize rotations and most of the time that's what they are.
     */
    protected Matrix33 rotate;

    /** The translation vector */
    protected XYZ translate;

    /** Creates a new instance of Matrix34 */
    public Matrix34()
    {
        rotate = new Matrix33();
        translate = new SimpleXYZ();
    }

    /**
     *  Construct a new instance from a rotation and translation
     *
     * @param r The rotation matrix to be used
     * @param t The translation to be used
     */
    public Matrix34(Matrix33 r, final XYZ t)
    {
        this();

        this.rotate.assign(r);
        this.translate.opAssign(t);
    }

    /**
     * Construct a new instance from column vectors
     *
     * @param rx The first column vector
     * @param ry The second column vector
     * @param rz The third column vector
     * @param tr The translation vector
     */
    public Matrix34(final XYZ rx, final XYZ ry, final XYZ rz, final XYZ tr)
    {
        this.rotate = new Matrix33(rx, ry, rz);
        this.translate = tr;
    }

    /**
     * Assign another matrix to this one
     *
     * @param m The matrix to be assigned to this one
     * @return This matrix, for method chaining purposes
     */
    public Matrix34 assign(final Matrix34 m)
    {
        this.rotate.assign(m.rotate);
        this.translate.opAddAssign(m.translate);

        return this;
    }

    /**
     * Carry out a multiplication on a matrix by a vector
     *
     * @param m The matrix to be multiplied
     * @param v The vector to be multiplied by
     * @return The new matrix
     */
    public XYZ operatorMult(final Matrix34 m, final XYZ v)
    {
        return XYZMath.opAdd(m.rotate.operatorApply(m.rotate, v), m.translate);
    }

    /**
     * Carry out a multiplication of 2 matrices
     *
     * @param a The first matrix to be multiplied
     * @param b The second matrix to be multiplied
     * @return The result of the multiplication
     */
    public Matrix34 operatorMult(final Matrix34 a, final Matrix34 b)
    {
        return new Matrix34(
            a.rotate.operatorMult(a.rotate, b.rotate),
            XYZMath.opAdd(
                a.rotate.operatorApply(a.rotate, b.translate), a.translate));
    }

    /**
     * Get an identity matrix for a 3x4
     *
     * @return An object representing the identity for 3x4 transformations
     */
    public static Matrix34 getIdentity()
    {
        Matrix34 mat = new Matrix34();

        mat.rotate.assign(Matrix33.getIdentity());
        mat.translate = ImmutableXYZ.XYZ_ZERO;

        return mat;
    }

    /**
     * Get a translation matrix
     *
     * @param t The amount of desired translation
     * @return The corresponding translation matrix
     */
    public static Matrix34 getTranslate(XYZ t)
    {
        Matrix34 mat = new Matrix34();

        mat.rotate.assign(Matrix33.getIdentity());
        mat.translate = t;

        return mat;
    }

    /**
     * Get a rotation matrix about an axis through a point
     *
     * @param axis The axis vector
     * @param angle The angle of rotation (in radians)
     * @param pt The point at which the rotation will be made
     * @return The corresponding rotation matrix
     */
    public static Matrix34 getRotateAboutAxisThrough(
        final XYZ axis, float angle, final XYZ pt)
    {
        Matrix34 mt = getTranslate(pt);
        Matrix34 mm =
            getTranslate(new SimpleXYZ(-pt.getX(), -pt.getY(), -pt.getZ()));

        Matrix34 m2 =
            new Matrix34(
                Matrix33.getRotateAboutAxis(axis, angle), ImmutableXYZ.XYZ_ZERO);

        Matrix34 mz = mt.operatorMult(mt, m2);

        Matrix34 m = new Matrix34();

        return m.assign(mz.operatorMult(mz, mm));
    }

    /**
     * Get a rotation matrix about an axis through a point
     *
     * @param axis The axis
     * @param pt The point at which the rotation will be made
     * @return The corresponding rotation matrix
     */
    public static Matrix34 getRotateAboutAxisThrough(
        final XYZ axis, final XYZ pt)
    {
        final float axis_magnitude = axis.magnitude();

        Matrix34 m = getIdentity();

        if (axis_magnitude != 0.0f)
        {
            m.assign(
                getRotateAboutAxisThrough(
                    XYZMath.opDivide(axis, axis_magnitude), axis_magnitude, pt));
        }

        return m;
    }
}
