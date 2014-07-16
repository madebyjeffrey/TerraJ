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
 * FlatGeometry.java
 *
 * Created on December 28, 2005, 3:34 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concrete class providing a flat geometry (in the XY-plane, with Z up).
 *
 * @author martin
 * @version $Id: FlatGeometry.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public class FlatGeometry extends AbstractGeometry implements Geometry
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(FlatGeometry.class);

    /**
     * Creates a new instance of FlatGeometry
     * @param utils The utils object to use for random numbers
     */
    public FlatGeometry(MathUtils utils)
    {
        super(utils);
    }

    /**
     * Get the height of a point in this geometry. In flat geometry the height
     * is just the z co-ordinate of a point.
     *
     * @param p The point representing the coordinates to be queried
     * @return The height of the specified point relative to assumed sea level at 0.0
     */
    public float getHeight(XYZ p)
    {
        return p.getZ();
    }

    /**
     * Set the height of a point in this geometry. Setting a height is
     * simply assigning to the z-coordinate as this is a flat geometry.
     *
     * @param p The point with the height that is to be set
     * @param v The new height to assign (relative to zero being sea level)
     */
    public void setHeight(XYZ p, float v)
    {
        p.setZ(v);
    }

    /**
     * The mid-point between two points is simply their average.
     *
     * @param v0 The first point
     * @param v1 The second point
     * @return The midpoint between the two points
     */
    public XYZ getMidpoint(XYZ v0, XYZ v1)
    {
        return XYZMath.opMultiply(0.5f, XYZMath.opAdd(v0, v1));
    }

    /**
     * This doesn't really mean anything here, so return zero, which would
     * correspond to the equator of a spherical geometry.
     *
     * @param p The point to get the latitude for
     * @return The normalised latitude of the point
     */
    public float getNormalisedLatitude(XYZ p)
    {
        return 0.0f;
    }

    /**
     * Returns unit z vector.  (Up is the same everywhere in this geometry).
     *
     * @param p The point to get the unit Z vector for
     * @return The unit Z vector for this point
     */
    public XYZ up(XYZ p)
    {
        return new SimpleXYZ(0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns unit y vector.  (North is the same everywhere in this geometry).
     *
     * @param p The point to get the unit Y vector for
     * @return The unit Y vector for this point
     */
    public XYZ north(XYZ p)
    {
        return new SimpleXYZ(0.0f, 1.0f, 0.0f);
    }

    /**
     * Returns unit x vector.  (East is the same everywhere in this geometry).
     *
     * @param p The point to get the unit X vector for
     * @return The unit X vector for this point
     */
    public XYZ east(XYZ p)
    {
        return new SimpleXYZ(1.0f, 0.0f, 0.0f);
    }

    /**
     * Adds a random peturbation to a point up to the specified amount
     *
     * The correct thing to do would be to return
     * p+RandomXYZInEllipsoid(_r01,variation);
     * however, this uses a variable number of random number calls which
     * means small parameter changes can have big effects on generated terrain.
     * This, on the other hand, always uses the same number of random numbers,
     * but isn't statistically equivalent *
     *
     * @param p The point to be subjected to peturbation
     * @param variation The maximum amount of random variation to appy
     * @return A new peturbed point. The input value is not modified
     */
    public XYZ perturb(XYZ p, XYZ variation)
    {
        return XYZMath.opAdd(p, new RandomXYZInBox(getUtils(), variation));
    }

    /**
     * Returns zero.  Heights are stored exactly once assigned so no need
     * for non-zero epsilon
     *
     * @return The epsilon value for this geometry, always zero for the flat case
     */
    public float epsilon()
    {
        return 0.0f;
    }
}
