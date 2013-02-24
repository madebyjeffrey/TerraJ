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
 * Geometry.java
 *
 * Created on December 28, 2005, 3:23 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;


/**
 * Interface for the geometry classes to implement
 *
 * @author martin
 * @version $Id: Geometry.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public interface Geometry
{
    /**
     * Get the height of a given point in this geometry.
     *
     * @param p The point representing the coordinates to be queried
     * @return The height of the specified point relative to assumed sea level at 0.0
     */
    public abstract float getHeight(XYZ p);

    /**
     * Set the height of a point in this geometry.
     *
     * @param p The point with the height that is to be set
     * @param v The new height to assign (relative to zero being sea level)
     */
    public abstract void setHeight(XYZ p, float v);

    /**
     * Get the midpoint between two points in this geometry.
     *
     * @param v0 The first point
     * @param v1 The second point
     * @return The midpoint between the two points
     */
    public abstract XYZ getMidpoint(XYZ v0, XYZ v1);

    /**
     * Get the normalised latitude for a point in this geometry. Normalised
     * latitude is 1.0 at the north pole, -1.0 at the south pole
     *
     * @param p The point to get the latitude for
     * @return The normalised latitude of the point
     */
    public abstract float getNormalisedLatitude(XYZ p);

    /**
     * Returns unit z vector for a particular point
     *
     * @param p The point to get the unit Z vector for
     * @return The unit Z vector for this point
     */
    public abstract XYZ up(XYZ p);

    /**
     * Returns unit y vector for a particular point
     *
     * @param p The point to get the unit Z vector for
     * @return The unit Y vector for this point
     */
    public abstract XYZ north(XYZ p);

    /**
     * Returns unit x vector for a particular point
     *
     * @param p The point to get the unit X vector for
     * @return The unit X vector for this point
     */
    public abstract XYZ east(XYZ p);

    /**
     * Adds a random peturbation to a point up to the specified amount
     *
     * @param v The point to be subjected to peturbation
     * @param variation The maximum amount of random variation to appy
     * @return A new peturbed point. The input value is not modified
     */
    public abstract XYZ perturb(XYZ v, XYZ variation);

    /**
     * Nasty hack to work around height setting possibly not being exact.
     * In some geometries (e.g spherical, but not flat) modifying a point to
     * be at a particular height does not guarantee that exact value will be
     * returned on a susequent height query.
     *
     * If this is the case, a non-zero epsilon value can be returned and used as
     * an error tolerence when comparing two heights for equivalence.
     *
     * @return The error tolerance for this geometry
     */
    public abstract float epsilon();
}
