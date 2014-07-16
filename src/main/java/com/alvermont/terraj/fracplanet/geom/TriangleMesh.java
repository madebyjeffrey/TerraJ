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
 * TriangleMesh.java
 *
 * Created on 28 January 2006, 11:29
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.TerrainParameters;

/**
 * The interface that meshes must implement
 *
 * @author  martin
 * @version $Id: TriangleMesh.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public interface TriangleMesh
{
    /**
     * Add a triangle to this mesh
     *
     *
     * @param t The new triangle to be added
     */
    void addTriangle(Triangle t);

    /**
     * Add a vertex to this mesh
     *
     *
     * @param v The new vertex to be added
     */
    void addVertex(Vertex v);

    /**
     * (Re-)computes all vertices normals.
     */
    void computeVertexNormals();

    /**
     * Getter for property emissive.
     *
     * @return Value of property emissive.
     */
    float getEmissive();

    /**
     * Getter for property geometry.
     *
     * @return Value of property geometry.
     */
    Geometry getGeometry();

    /**
     * Returns number of triangles in mesh indexing colour[0] of vertices.
     *
     *
     * @return The number of triangles in this mesh using colour index 0
     */
    int getTriangleColour0Count();

    /**
     * Returns number of triangles in mesh indexing colour[1] of vertices.
     *
     *
     * @return The number of triangles in this mesh using colour index 1
     */
    int getTriangleColour1Count();

    /**
     * Returns number of triangles in mesh
     *
     *
     * @return The number of triangles in this mesh
     */
    int getTriangleCount();

    /**
     * Return average (mean) height of a triangles's vertices.
     *
     *
     * @param i The index of the triangles to get the information for
     * @return The mean height for this triangles
     */
    float getTriangleHeightAverage(int i);

    /**
     * Return maximum height of a triangles's vertices.
     *
     *
     * @param i The index of the triangles to get the information for
     * @return The height of the vertices with the highest height in this triangles
     */
    float getTriangleHeightMax(int i);

    /**
     * Return minimum height of a triangles's vertices.
     *
     *
     * @param i The index of the triangles to get the information for
     * @return The height of the vertices with the lowest height in this triangles
     */
    float getTriangleHeightMin(int i);

    /**
     * Compute and return the normal to a triangles
     *
     *
     * @param i The index of the triangles to get the normal for
     * @return The normal to the triangles
     */
    XYZ getTriangleNormal(int i);

    /**
     * Get the buffer containing the triangles data
     *
     *
     * @return The buffer containing packed triangles data
     */
    TriangleBufferArray getTriangles();

    /**
     * Returns number of vertices in mesh
     *
     *
     * @return The number of vertices in this mesh
     */
    int getVertexCount();

    /**
     * Get the height of a vertices
     *
     *
     * @param i The index of the vertices to retrieve the height for
     * @return The height of the specified vertices
     */
    float getVertexHeight(int i);

    /**
     * Get the buffer containing the vertices data
     *
     *
     * @return The buffer containing packed vertices data
     */
    VertexBufferArray getVertices();

    /**
     * Setter for property emissive.
     *
     * @param emissive New value of property emissive.
     */
    void setEmissive(float emissive);

    /**
     * Set the height of a vertices
     *
     *
     * @param i The index of the vertices to retrieve the height for
     * @param h The height that is to be set
     */
    void setVertexHeight(int i, float h);

    /**
     * Perform a number of subdivisions, possibly some unperturbed ("flat"),
     * and halving the perturbation variation each iteration.
     *
     *
     * @param subdivisions The number of subdivisions to do in total
     * @param flatSubdivisions The number of subdivisions to do with no peturbation
     * @param variation The amount of initial variation to be applyed by peturbation
     */
    void subdivide(int subdivisions, int flatSubdivisions, XYZ variation);

    /**
     * Return which vertices colour to use for a triangles.
     *
     *
     * @param t The triangles to be tested
     * @return The correct colour index for the triangles (0 or 1)
     */
    int whichColourForTriangle(int t);

    /**
     * Recolour the mesh based on altitude
     *
     * @param parameters The terrain parameters to be used
     */
    void doColours(TerrainParameters parameters);
}
