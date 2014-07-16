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
 * MeshTranslator.java
 *
 * Created on 27 January 2006, 09:16
 */
package com.alvermont.terraj.fracplanet.rings;

import com.alvermont.terraj.fracplanet.geom.TriangleBufferArray;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.VertexBufferArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import threeD.raytracer.graphics.RGB;
import threeD.raytracer.primitives.Mesh;
import threeD.raytracer.util.Vector;

/**
 * First work with Rings. Attempts to translate one of my mesh structures
 * into one that rings can use.
 *
 * @author  martin
 * @version $Id: MeshTranslator.java,v 1.11 2006/07/06 06:59:43 martin Exp $
 */
public class MeshTranslator
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(MeshTranslator.class);

    // RequireThis OFF: log

    /** Creates a new instance of MeshTranslator */
    protected MeshTranslator()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Convert one of our floating point colour object to a Rings RGB
     * object.
     *
     * @param col The object to be converted
     * @return The corresponding Rings colour object
     */
    public static RGB toRGB(
        com.alvermont.terraj.fracplanet.colour.FloatRGBA col)
    {
        return new RGB(col.getR(), col.getG(), col.getB());
    }

    /**
     * Convert one of our integer colour object to a Rings RGB
     * object.
     *
     * @param col The object to be converted
     * @return The corresponding Rings colour object
     */
    public static RGB toRGB(
        com.alvermont.terraj.fracplanet.colour.ByteRGBA col)
    {
        return toRGB(new com.alvermont.terraj.fracplanet.colour.FloatRGBA(col));
    }

    /**
     * Convert one of our vector object to a Rings object
     *
     * @param xyz The object to be converted
     * @return The corresponding Rings vector object
     */
    public static Vector toVector(com.alvermont.terraj.fracplanet.geom.XYZ xyz)
    {
        return new Vector(xyz.getX(), xyz.getY(), xyz.getZ());
    }

    /**
     * Convert a mesh vertex to a Rings object.
     *
     * @param orig Our vertex object
     * @param colIndex The colour index to use
     * @return A Mesh.Vertex object for Rings
     */
    public static Mesh.Vertex toMeshVertex(
        com.alvermont.terraj.fracplanet.geom.Vertex orig, int colIndex)
    {
        final com.alvermont.terraj.fracplanet.geom.XYZ pos = orig.getPosition();
        final com.alvermont.terraj.fracplanet.geom.XYZ norm = orig.getNormal();
        final com.alvermont.terraj.fracplanet.colour.ByteRGBA colour =
            orig.getColour(colIndex);

        final Mesh.Vertex v = new Mesh.Vertex(toVector(pos));

        v.setNormal(toVector(norm));
        v.setColor(toRGB(colour));

        return v;
    }

    /**
     * Translate one of our meshes to Rings by creating a copy of it using
     * the Rings set of objects
     *
     * @param original The mesh to be converted
     * @return A mesh that is suitable for rendering by Rings
     */
    public static Mesh translateToRings(TriangleMesh original)
    {
        final TriangleBufferArray origTri = original.getTriangles();
        final VertexBufferArray origVert = original.getVertices();

        // allocate new arrays
        //final int[][] newTri = new int[origTri.size()][3];
        final Mesh.Vertex[] newVert = new Mesh.Vertex[origVert.size() * 2];

        log.debug(origVert.size() + " vertices ...");

        // process each vertex
        for (int v = 0; v < origVert.size(); ++v)
        {
            newVert[v] = toMeshVertex(origVert.get(v), 0);
            newVert[v + origVert.size()] = toMeshVertex(origVert.get(v), 1);
        }

        final Mesh mesh = new Mesh();

        mesh.setVectors(newVert);

        log.debug(origTri.size() + " triangles ...");

        // process each triangle
        for (int t = 0; t < origTri.size(); ++t)
        {
            final com.alvermont.terraj.fracplanet.geom.Triangle oldT =
                origTri.get(t);

            //newTri[t][0] = oldT.getVertex(0);
            //newTri[t][1] = oldT.getVertex(1);
            //newTri[t][2] = oldT.getVertex(2);

            //newT.setInterpolateVertexColor(true);
            //newT.setUseTransform(false);

            //newTri[t] = newT;
            int vertexOffset = 0;

            if (t >= original.getTriangleColour0Count())
            {
                vertexOffset = origVert.size();
            }

            mesh.addTriangle(
                oldT.getVertex(0) + vertexOffset,
                oldT.getVertex(1) + vertexOffset,
                oldT.getVertex(2) + vertexOffset);
        }

        //mesh.setTriangleData(newTri);
        mesh.setColor(new RGB(1.0, 1.0, 1.0));
        mesh.setInterpolateColor(true);
        mesh.setRemoveBackFaces(true);
        mesh.setSmooth(true);
        //mesh.clearIntersectionCache();
        mesh.setShadeFront(true);
        mesh.setShadeBack(true);

        return mesh;
    }
}
