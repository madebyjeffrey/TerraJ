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
 * Matrix33.java
 *
 * Created on 18 April 2006, 13:16
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to hold 3x3 matrices
 *
 * @author  martin
 * @version $Id: Matrix33.java,v 1.6 2006/07/06 06:58:35 martin Exp $
 */
public class Matrix33
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(Matrix33.class);

    /** The contents of the matrix */
    protected XYZ[] basis;

    /** Creates a new instance of Matrix33 */
    public Matrix33()
    {
        basis = new SimpleXYZ[3];
    }

    /**
     * Creates a new instance of Matrix33
     *
     * @param rx The first column
     * @param ry The second column
     * @param rz The third column
     */
    public Matrix33(final XYZ rx, final XYZ ry, final XYZ rz)
    {
        basis[0].opAssign(rx);
        basis[1].opAssign(ry);
        basis[2].opAssign(rz);
    }

    /**
     * Assign another matrix to this one
     *
     * @param m The matrix to be assigned
     * @return The matrix after assignment, for chaining purposes
     */
    public Matrix33 assign(final Matrix33 m)
    {
        basis[0].opAssign(m.basis[0]);
        basis[1].opAssign(m.basis[1]);
        basis[2].opAssign(m.basis[2]);

        return m;
    }

    /**
     * Access a given element
     *
     * @param row The row of the element to get
     * @param col The column of the element to get
     * @return the corresponding element value
     */
    public float element(final int row, final int col)
    {
        return basis[col].getElement(row);
    }

    /**
     * Set a given element
     *
     * @param row The row of the element to set
     * @param col The column of the element to set
     * @param value The new value to set the element to
     */
    public void setElement(final int row, final int col, final float value)
    {
        final XYZ c = basis[col];

        switch (row)
        {
            case 0:
                c.setX(value);

                break;

            case 1:
                c.setX(value);

                break;

            case 2:
                c.setX(value);

                break;

            default:
                throw new IllegalArgumentException(
                    "Illegal access to element: " + row);
        }
    }

    /**
     * Get a copy of the first row of the matrix
     *
     * @return The matrix row
     */
    public XYZ row0()
    {
        return new SimpleXYZ(basis[0].getX(), basis[1].getX(), basis[2].getX());
    }

    /**
     * Get a copy of the second row of the matrix
     *
     * @return The matrix row
     */
    public XYZ row1()
    {
        return new SimpleXYZ(basis[0].getY(), basis[1].getY(), basis[2].getY());
    }

    /**
     * Get a copy of the third row of the matrix
     *
     * @return The matrix row
     */
    public XYZ row2()
    {
        return new SimpleXYZ(basis[0].getZ(), basis[1].getZ(), basis[2].getZ());
    }

    /**
     * Multiply a matrix by a scalar
     *
     * @param k The scalar to multiply by
     * @param m The matrix to be multiplied
     * @return The result of the multiplication
     */
    public Matrix33 operatorMult(final float k, final Matrix33 m)
    {
        return new Matrix33(
            XYZMath.opMultiply(k, m.basis[0]), XYZMath.opMultiply(
                k, m.basis[1]), XYZMath.opMultiply(k, m.basis[2]));
    }

    /**
     * Multiply a matrix by a scalar
     *
     * @param k The scalar to multiply by
     * @param m The matrix to be multiplied
     * @return The result of the multiplication
     */
    public Matrix33 operatorMult(final Matrix33 m, final float k)
    {
        return new Matrix33(
            XYZMath.opMultiply(k, m.basis[0]), XYZMath.opMultiply(
                k, m.basis[1]), XYZMath.opMultiply(k, m.basis[2]));
    }

    /**
     * Divide a matrix by a scalar
     *
     *
     * @param k The scalar to divide by
     * @param m The matrix to be divided
     * @return The result of the division
     */
    public Matrix33 operatorDivide(final Matrix33 m, final float k)
    {
        return new Matrix33(
            XYZMath.opDivide(m.basis[0], k), XYZMath.opDivide(m.basis[1], k),
            XYZMath.opDivide(m.basis[2], k));
    }

    /**
     * Apply matrix to vector (was operator* in C++)
     *
     * @param m The matrix
     * @param v The vector
     * @return The result of the operation
     */
    public XYZ operatorApply(final Matrix33 m, final XYZ v)
    {
        return new SimpleXYZ(
            XYZMath.opDotProduct(m.row0(), v), XYZMath.opDotProduct(
                m.row1(), v), XYZMath.opDotProduct(m.row2(), v));
    }

    /**
     * Matrix multiplication
     *
     * @param a The first matrix to be multipled
     * @param b The second matrix to be multiplied
     * @return The result of the matrix multiplication
     */
    public Matrix33 operatorMult(final Matrix33 a, final Matrix33 b)
    {
        return new Matrix33(
            a.operatorApply(a, b.basis[0]), a.operatorApply(a, b.basis[1]),
            a.operatorApply(a, b.basis[2]));
    }

    /**
     * Calculate the cofactor of a row and column
     *
     * @param row The row of interest
     * @param col The column of interest
     * @return The cofactor
     */
    public float cofactor(final int row, final int col)
    {
        final int row0 = (row == 0 ? 1 : 0);
        final int col0 = (col == 0 ? 1 : 0);

        final int row1 = (row == 2 ? 1 : 2);
        final int col1 = (col == 2 ? 1 : 2);

        return element(row0, col0) * element(row1, col1) -
        element(row0, col1) * element(row1, col0);
    }

    /**
     * Calculate the determinant of this matrix
     *
     * @return The determinant of the supplied matrix
     */
    public float determinant()
    {
        return element(0, 0) * cofactor(0, 0) - element(0, 1) * cofactor(0, 1) +
        element(0, 2) * cofactor(0, 2);
    }

    /**
     * Calculate the inverse of this matrix
     *
     * @return The inverse of this matrix
     */
    public Matrix33 inverted()
    {
        Matrix33 ret = new Matrix33();

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 3; col++)
            {
                final float cf = cofactor(row, col);

                // NB Transpose is deliberate
                ret.setElement(col, row, (((row + col) & 1) != 0) ? -cf : cf);
            }
        }

        return operatorDivide(ret, determinant());
    }

    /**
     * Get the identity 3x3 matrix
     *
     * @return A matrix that represents the identity for a 3x3 matrix
     */
    public static Matrix33 getIdentity()
    {
        final Matrix33 mat = new Matrix33();

        mat.basis[0] = new SimpleXYZ(1.0f, 0.0f, 0.0f);
        mat.basis[1] = new SimpleXYZ(0.0f, 1.0f, 0.0f);
        mat.basis[2] = new SimpleXYZ(0.0f, 0.0f, 1.0f);

        return mat;
    }

    /**
     * Get a matrix that represents a rotation around the X axis
     *
     * @param angle The angle of rotation desired (in radians)
     * @return The corresponding rotation matrix
     */
    public static Matrix33 getRotateAboutX(float angle)
    {
        final Matrix33 mat = new Matrix33();

        final float ca = (float) Math.cos(angle);
        final float sa = (float) Math.sin(angle);

        mat.basis[0] = new SimpleXYZ(1.0f, 0.0f, 0.0f);
        mat.basis[1] = new SimpleXYZ(0.0f, ca, sa);
        mat.basis[2] = new SimpleXYZ(0.0f, -sa, ca);

        return mat;
    }

    /**
     * Get a matrix that represents a rotation around the Y axis
     *
     * @param angle The angle of rotation desired (in radians)
     * @return The corresponding rotation matrix
     */
    public static Matrix33 getRotateAboutY(float angle)
    {
        final Matrix33 mat = new Matrix33();

        final float ca = (float) Math.cos(angle);
        final float sa = (float) Math.sin(angle);

        mat.basis[0] = new SimpleXYZ(ca, 0.0f, -sa);
        mat.basis[1] = new SimpleXYZ(0.0f, 1.0f, 0.0f);
        mat.basis[2] = new SimpleXYZ(sa, 0.0f, ca);

        return mat;
    }

    /**
     * Get a matrix that represents a rotation around the Z axis
     *
     * @param angle The angle of rotation desired (in radians)
     * @return The corresponding rotation matrix
     */
    public static Matrix33 getRotateAboutZ(float angle)
    {
        final Matrix33 mat = new Matrix33();

        final float ca = (float) Math.cos(angle);
        final float sa = (float) Math.sin(angle);

        mat.basis[0] = new SimpleXYZ(ca, sa, 0.0f);
        mat.basis[1] = new SimpleXYZ(-sa, ca, 0.0f);
        mat.basis[2] = new SimpleXYZ(0.0f, 0.0f, 1.0f);

        return mat;
    }

    /**
     * Get a matrix that represents a rotation around an arbitrary axis
     *
     * @param axis The axis to be used
     * @param angle The amount of desired rotation (in radians)
     * @return The corresponding rotation matrix
     */
    public static Matrix33 getRotateAboutAxis(final XYZ axis, float angle)
    {
        // Want 2 vectors perpendicular to axis TODO: Check for degenerate cases
        final XYZ axis_ortho0 =
            new SimpleXYZ(
                XYZMath.opMultiply(axis, new SimpleXYZ(1.0f, 0.0f, 0.0f)).normalised());
        final XYZ axis_ortho1 =
            new SimpleXYZ(XYZMath.opMultiply(axis, axis_ortho0));

        // The matrix which rotates identity basis to axis&orthos.  z axis goes to passed in axis
        final Matrix33 xyz_to_axis =
            new Matrix33(axis_ortho0, axis_ortho1, axis);

        final Matrix33 axis_to_xyz =
            new Matrix33().assign(xyz_to_axis.inverted());

        Matrix33 m = new Matrix33();

        return m.assign(
            xyz_to_axis.operatorMult(
                xyz_to_axis,
                axis_to_xyz.operatorMult(getRotateAboutZ(angle), axis_to_xyz)));
    }
}
