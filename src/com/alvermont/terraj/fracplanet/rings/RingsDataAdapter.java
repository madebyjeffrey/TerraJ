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
 * RingsDataAdapter.java
 *
 * Created on 04 February 2006, 09:08
 */
package com.alvermont.terraj.fracplanet.rings;

import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.geom.Triangle;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import threeD.raytracer.primitives.Mesh;

/**
 * First attempt at an adaptor for the data I currently have in buffers to
 * Rings format, avoiding a copy of the entire mesh
 *
 * @author  martin
 * @version $Id: RingsDataAdapter.java,v 1.6 2006/07/06 06:59:43 martin Exp $
 */
public class RingsDataAdapter implements Mesh.VertexData
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(RingsDataAdapter.class);

    /**
     *
     * Creates a new instance of RingsDataAdapter
     *
     * @param mesh The mesh that this object will adapt to Rings
     */
    public RingsDataAdapter(TriangleMesh mesh)
    {
        this.originalMesh = mesh;
    }

    /**
     * Get the red component of the colour at a particular vertex
     *
     * @param i The index of the vertex to get the colour for
     * @return The corresponding red colour component
     */
    public double getRed(int i)
    {
        int colIndex = 0;

        if (i >= originalMesh.getVertices()
                .size())
        {
            colIndex = 1;
        }

        final int index = i % originalMesh.getVertices()
                .size();

        final FloatRGBA colour =
            new FloatRGBA(
                originalMesh.getVertices().get(index).getColour(colIndex));

        return (double) colour.getR();
    }

    /**
     * Get the green component of the colour at a particular vertex
     *
     * @param i The index of the vertex to get the colour for
     * @return The corresponding green colour component
     */
    public double getGreen(int i)
    {
        int colIndex = 0;

        if (i >= originalMesh.getVertices()
                .size())
        {
            colIndex = 1;
        }

        final int index = i % originalMesh.getVertices()
                .size();

        final FloatRGBA colour =
            new FloatRGBA(
                originalMesh.getVertices().get(index).getColour(colIndex));

        return (double) colour.getG();
    }

    /**
     * Get the blue component of the colour at a particular vertex
     *
     * @param i The index of the vertex to get the colour for
     * @return The corresponding blue colour component
     */
    public double getBlue(int i)
    {
        int colIndex = 0;

        if (i >= originalMesh.getVertices()
                .size())
        {
            colIndex = 1;
        }

        final int index = i % originalMesh.getVertices()
                .size();

        final FloatRGBA colour =
            new FloatRGBA(
                originalMesh.getVertices().get(index).getColour(colIndex));

        return (double) colour.getB();
    }

    /**
     * Get the X coordinate of the position vector of a vertex
     *
     * @param i The index of the vertex to be retrieved
     * @return The corresponding X coordinate
     */
    public double getX(int i)
    {
        final int index = i % originalMesh.getVertices()
                .size();

        return originalMesh.getVertices()
            .getPosition(index)
            .getX();
    }

    /**
     * Get the Y coordinate of the position vector of a vertex
     *
     * @param i The index of the vertex to be retrieved
     * @return The corresponding Y coordinate
     */
    public double getY(int i)
    {
        final int index = i % originalMesh.getVertices()
                .size();

        return originalMesh.getVertices()
            .getPosition(index)
            .getY();
    }

    /**
     * Get the Z coordinate of the position vector of a vertex
     *
     * @param i The index of the vertex to be retrieved
     * @return The corresponding Z coordinate
     */
    public double getZ(int i)
    {
        final int index = i % originalMesh.getVertices()
                .size();

        return originalMesh.getVertices()
            .getPosition(index)
            .getZ();
    }

    /**
     * Get the vertex indices for a specified triangle
     *
     * @param i The triangle to be retrieved
     * @return An array of 3 vertex indices for the triangle
     */
    public int[] getTriangle(int i)
    {
        final int[] verts = new int[3];

        int offset = 0;

        if (originalMesh.whichColourForTriangle(i) != 0)
        {
            offset = originalMesh.getVertices()
                    .size();
        }

        final Triangle t = originalMesh.getTriangles()
                .get(i);

        verts[0] = t.getVertex(0) + offset;
        verts[1] = t.getVertex(1) + offset;
        verts[2] = t.getVertex(2) + offset;

        return verts;
    }

    /**
     * Get the number of triangles that there are in this mesh
     *
     * @return The total number of triangles in this mesh
     */
    public int getTriangleCount()
    {
        return originalMesh.getTriangles()
            .size();
    }

    /**
     * Holds value of property originalMesh.
     */
    private TriangleMesh originalMesh;

    /**
     * Getter for property originalMesh.
     * @return Value of property originalMesh.
     */
    public TriangleMesh getOriginalMesh()
    {
        return this.originalMesh;
    }
}
