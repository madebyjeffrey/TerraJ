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
 * TriangleMeshIcosahedron.java
 *
 * Created on December 29, 2005, 11:18 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An icosahedron.
 *
 * @author martin
 * @version $Id: TriangleMeshIcosahedron.java,v 1.7 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleMeshIcosahedron extends TriangleMeshCloud
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleMeshIcosahedron.class);

    // MagicNumber OFF
    private static final float X = 0.525731112119133606f;
    private static final float Z = 0.850650808352039932f;

    // MagicNumber ON

    /** Number of vertices to add */
    private static final int VERTEX_COUNT = 12;

    /** Number of triangles to add */
    private static final int TRIANGLE_COUNT = 20;

    // RequireThis OFF: X
    // RequireThis OFF: Z
    // RequireThis OFF: VERTEX_COUNT
    // RequireThis OFF: TRIANGLE_COUNT

    /** Table of vertex data */
    private float[][] vdata =
        {
            { -X, 0.0f, Z },
            { X, 0.0f, Z },
            { -X, 0.0f, -Z },
            { X, 0.0f, -Z },
            { 0.0f, Z, X },
            { 0.0f, Z, -X },
            { 0.0f, -Z, X },
            { 0.0f, -Z, -X },
            { Z, X, 0.0f },
            { -Z, X, 0.0f },
            { Z, -X, 0.0f },
            { -Z, -X, 0.0f }
        };

    // RequireThis ON: X
    // RequireThis ON: Z

    // MagicNumber OFF

    /** Table of vertex index data */
    private int[][] tindices =
        {
            { 0, 4, 1 },
            { 0, 9, 4 },
            { 9, 5, 4 },
            { 4, 5, 8 },
            { 4, 8, 1 },
            { 8, 10, 1 },
            { 8, 3, 10 },
            { 5, 3, 8 },
            { 5, 2, 3 },
            { 2, 7, 3 },
            { 7, 10, 3 },
            { 7, 6, 10 },
            { 7, 11, 6 },
            { 11, 0, 6 },
            { 0, 1, 6 },
            { 6, 1, 10 },
            { 9, 0, 11 },
            { 9, 11, 2 },
            { 9, 2, 5 },
            { 7, 2, 11 }
        };

    // MagicNumber ON

    /**
     * Creates a new instance of TriangleMeshIcosahedron
     *
     * @param radius The radius of the object
     * @param utils The utils object to use
     * @param progress The object to use for reporting progress
     */
    public TriangleMeshIcosahedron(
        float radius, MathUtils utils, Progress progress)
    {
        super(progress, new SphericalGeometry(utils));

        log.debug("Creating TriangleMeshIcosahedron radius: " + radius);

        for (int v = 0; v < VERTEX_COUNT; ++v)
        {
            final XYZ base =
                new SimpleXYZ(
                    this.vdata[v][0], this.vdata[v][1], this.vdata[v][2]);

            addVertex(
                new SimpleVertex(XYZMath.opMultiply(radius, base.normalised())));
        }

        for (int t = 0; t < TRIANGLE_COUNT; ++t)
        {
            addTriangle(
                new SimpleTriangle(
                    this.tindices[t][2], this.tindices[t][1],
                    this.tindices[t][0]));
        }
    }
}
