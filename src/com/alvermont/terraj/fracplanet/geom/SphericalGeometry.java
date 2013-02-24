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
 * SphericalGeometry.java
 *
 * Created on December 28, 2005, 3:58 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class implementing a spherical geometry. Concrete class providing a flat
 * geometry (a sphere with nominal radius 1, equator in the XY-plane,
 * Z axis through the poles).
 *
 * @author martin
 * @version $Id: SphericalGeometry.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public class SphericalGeometry extends AbstractGeometry implements Geometry
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(SphericalGeometry.class);

    /** The error tolerance for this geometry */
    public static final float SPHERICAL_EPSILON = 0.000001f;

    // RequireThis OFF: SPHERICAL_EPSILON

    /**
     * Creates a new instance of SphericalGeometry
     *
     * @param utils The math utility object to be used
     */
    public SphericalGeometry(MathUtils utils)
    {
        super(utils);
    }

    /**
     * Get the height of a given point in this geometry. Height is relative to
     * the surface of the unit radius sphere.
     *
     * @param p The point representing the coordinates to be queried
     * @return The height of the specified point relative to assumed sea level at 1.0
     */
    public float getHeight(XYZ p)
    {
        return p.magnitude() - 1.0f;
    }

    /**
     * Set the height of a point in this geometry. The height set is relative
     * to the surface of the unit radius sphere.
     *
     * @param p The point with the height that is to be set
     * @param v The new height to assign (relative to 1.0 being sea level)
     */
    public void setHeight(XYZ p, float v)
    {
        final float m = p.magnitude();

        p.opMultiplyAssign((1.0f + v) / m);
    }

    /**
     * Get the midpoint between two points in this geometry. Don't just take
     * the mid-point of the straight-line path through the sphere's surface:
     * must work relative to the sphere's surface.
     *
     * @param v0 The first point
     * @param v1 The second point
     * @return The midpoint between the two points
     */
    public XYZ getMidpoint(XYZ v0, XYZ v1)
    {
        final float h0 = v0.magnitude();
        final float h1 = v1.magnitude();

        final float hAv = 0.5f * (h0 + h1);

        final XYZ m =
            new SimpleXYZ(XYZMath.opMultiply(0.5f, XYZMath.opAdd(v0, v1)));

        return XYZMath.opMultiply(hAv / m.magnitude(), m);
    }

    /**
     * Get the normalised latitude for a point in this geometry. Normalised
     * latitude is 1.0 at the north pole, -1.0 at the south pole
     *
     * @param p The point to get the latitude for
     * @return The normalised latitude of the point
     */
    public float getNormalisedLatitude(XYZ p)
    {
        return p.getZ();
    }

    /**
     * Returns unit z vector for a particular point. Up is normal to the sphere.
     *
     * @param p The point to get the unit Z vector for
     * @return The unit Z vector for this point
     */
    public XYZ up(XYZ p)
    {
        return p.normalised();
    }

    /**
     * Returns unit y vector for a particular point. North is perpendicular
     * to "up" and "east"
     *
     * WARNING: Returns zero vector at the poles.
     *
     * @param p The point to get the unit Z vector for
     * @return The unit Y vector for this point
     */
    public XYZ north(XYZ p)
    {
        if ((p.getX() == 0) && (p.getY() == 0))
        {
            return ImmutableXYZ.XYZ_ZERO;
        }
        else
        {
            return XYZMath.opMultiply(up(p), east(p))
                .normalised();
        }
    }

    /**
     * Returns unit x vector for a particular point. East is perpendicular to
     * "up" and the polar vector.
     *
     * WARNING: Returns zero vector at the poles.
     *
     * @param p The point to get the unit X vector for
     * @return The unit X vector for this point
     */
    public XYZ east(XYZ p)
    {
        if ((p.getX() == 0) && (p.getY() == 0))
        {
            return ImmutableXYZ.XYZ_ZERO;
        }
        else
        {
            return XYZMath.opMultiply(new SimpleXYZ(0.0f, 0.0f, 1.0f), up(p))
                .normalised();
        }
    }

    /**
     * Adds a random peturbation to a point up to the specified amount
     *
     * @param p The point to be subjected to peturbation
     * @param variation The maximum amount of random variation to appy
     * @return A new peturbed point. The input value is not modified
     */
    public XYZ perturb(XYZ p, XYZ variation)
    {
        final RandomXYZInBox v = new RandomXYZInBox(getUtils(), variation);

        final XYZ t1 = XYZMath.opMultiply(v.getX(), east(p));
        final XYZ t2 = XYZMath.opMultiply(v.getY(), north(p));
        final XYZ t3 = XYZMath.opMultiply(v.getZ(), up(p));

        final XYZ t4 = XYZMath.opAdd(XYZMath.opAdd(t1, t2), t3);

        return XYZMath.opAdd(p, t4);
    }

    /**
     * Return the error tolerance for this geometry. This needs to return
     * something small for the lake flooding algorithm to work.
     *
     * @return The error tolerance for this geometry.
     */
    public float epsilon()
    {
        return SPHERICAL_EPSILON;
    }
}
