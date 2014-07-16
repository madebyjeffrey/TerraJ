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
 * AbstractTriangleMesh.java
 *
 * Created on December 29, 2005, 9:15 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.Progress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains vertices and triangles of a triangles mesh.  Abstract base class
 * because specific classes must specify a geometry.
 * Not as general-purpose as it might be due to constraints imposed by OpenGL.
 * In particular, Triangle can have no attributes (e.g normal, colour) if a
 * single OpenGL call is to be made to draw all triangles,
 * so this information is entirely associated with Vertex.
 * Two colours can be associated with each vertices (required for fracplanet
 * application to obtain sharp coastlines), and it it a requirement for
 * subclasses to sort triangles so that all those before _triangle_switch_colour
 * use vertices colour index 0, and those afterwards vertices colour index 1.
 *  \todo The geometry() method is a mess.  It would surely be better to have
 * a Geometry* in the base class passed in via the constructor.
 *
 * @author martin
 * @version $Id: AbstractTriangleMesh.java,v 1.6 2006/07/06 06:58:35 martin Exp $
 */
public abstract class AbstractTriangleMesh
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(AbstractTriangleMesh.class);

    /** The vertices of the mesh */
    private VertexBufferArray vertices;

    /** The triangles of the mesh */
    private TriangleBufferArray triangles;

    /** The object for progress reports */
    private Progress progress;

    /** Colour switch index */
    private int switchColour = 0;

    /** Percentage value for 100 percent */
    private static final int STEPS_HUNDRED_PERCENT = 100;

    /** No of vertices in a triangle */
    private static final float VERTICES_IN_TRIANGLE = 3.0f;

    // RequireThis OFF: STEPS_HUNDRED_PERCENT
    // RequireThis OFF: VERTICES_IN_TRIANGLE

    /**
     * Holds value of property emissive.
     */
    private float emissive;

    /**
     * Holds value of property geometry.
     */
    private Geometry geometry;

    /**
     * Create a new instance of TriangleMesh
     *
     * @param progress The progress object to report on during mesh creation
     * @param geometry The geometry object to be used
     */
    public AbstractTriangleMesh(Progress progress, Geometry geometry)
    {
        this.progress = progress;
        this.geometry = geometry;

        this.emissive = 0.0f;

        this.setVertices(new VertexBufferArray());
        this.setTriangles(new TriangleBufferArray());
    }

    /**
     * Create a new instance of TriangleMesh
     *
     * @param progress The progress object to report on during mesh creation
     * @param geometry The geometry object to be used
     * @param vCapacity The initial vertices capacity to allocate
     * @param tCapacity The initial triangles capacity to allocate
     */
    public AbstractTriangleMesh(
        Progress progress, Geometry geometry, int vCapacity, int tCapacity)
    {
        this.progress = progress;
        this.geometry = geometry;

        this.emissive = 0.0f;

        this.setVertices(new VertexBufferArray(vCapacity));
        this.setTriangles(new TriangleBufferArray(tCapacity));
    }

    /**
     * Create a new instance of TriangleMesh as a copy of another one
     *
     * @param mesh The instance of TriangleMesh to be copied
     */
    public AbstractTriangleMesh(AbstractTriangleMesh mesh)
    {
        this.setVertices(new VertexBufferArray(mesh.getVertexCount()));
        this.setTriangles(new TriangleBufferArray(mesh.getTriangleCount()));

        this.getVertices()
            .addAll(mesh.getVertices());
        this.getTriangles()
            .addAll(mesh.getTriangles());

        this.emissive = mesh.emissive;
        this.switchColour = mesh.getSwitchColour();
    }

    /**
     * Add a vertex to this mesh
     *
     * @param v The new vertex to be added
     */
    public void addVertex(Vertex v)
    {
        this.getVertices()
            .add(v);
    }

    /**
     * Add a triangle to this mesh
     *
     * @param t The new triangle to be added
     */
    public void addTriangle(Triangle t)
    {
        this.getTriangles()
            .add(t);
    }

    /**
     * Get the height of a vertices
     *
     * @param i The index of the vertices to retrieve the height for
     * @return The height of the specified vertices
     */
    public float getVertexHeight(int i)
    {
        return this.geometry.getHeight(this.getVertices().get(i).getPosition());
    }

    /**
     * Set the height of a vertices
     *
     * @param i The index of the vertices to retrieve the height for
     * @param h The height that is to be set
     */
    public void setVertexHeight(int i, float h)
    {
        final XYZ p = new SimpleXYZ(this.getVertices().get(i).getPosition());

        this.geometry.setHeight(p, h);
        this.getVertices()
            .get(i)
            .setPosition(p);
    }

    /**
     * Return minimum height of a triangles's vertices.
     *
     * @param i The index of the triangles to get the information for
     * @return The height of the vertices with the lowest height in this triangles
     */
    public float getTriangleHeightMin(int i)
    {
        final Triangle t = this.getTriangles()
                .get(i);

        return Math.min(
            t.getVertex(0), Math.min(t.getVertex(1), t.getVertex(2)));
    }

    /**
     * Return maximum height of a triangles's vertices.
     *
     * @param i The index of the triangles to get the information for
     * @return The height of the vertices with the highest height in this triangles
     */
    public float getTriangleHeightMax(int i)
    {
        final Triangle t = this.getTriangles()
                .get(i);

        return Math.max(
            t.getVertex(0), Math.max(t.getVertex(1), t.getVertex(2)));
    }

    /**
     * Return average (mean) height of a triangles's vertices.
     *
     * @param i The index of the triangles to get the information for
     * @return The mean height for this triangles
     */
    public float getTriangleHeightAverage(int i)
    {
        final Triangle t = this.getTriangles()
                .get(i);

        return (getVertexHeight(0) + getVertexHeight(1) + getVertexHeight(2)) / VERTICES_IN_TRIANGLE;
    }

    /**
     * Return which vertices colour to use for a triangles.
     *
     * @param t The triangles to be tested
     * @return The correct colour index for the triangles (0 or 1)
     */
    public int whichColourForTriangle(int t)
    {
        int colour = 0;

        if (t >= this.getSwitchColour())
        {
            colour = 1;
        }

        return colour;
    }

    /**
     * Returns number of vertices in mesh
     *
     * @return The number of vertices in this mesh
     */
    public int getVertexCount()
    {
        return this.getVertices()
            .size();
    }

    /**
     * Returns number of triangles in mesh
     *
     * @return The number of triangles in this mesh
     */
    public int getTriangleCount()
    {
        return this.getTriangles()
            .size();
    }

    /**
     * Returns number of triangles in mesh indexing colour[0] of vertices.
     *
     * @return The number of triangles in this mesh using colour index 0
     */
    public int getTriangleColour0Count()
    {
        return this.getSwitchColour();
    }

    /**
     * Returns number of triangles in mesh indexing colour[1] of vertices.
     *
     * @return The number of triangles in this mesh using colour index 1
     */
    public int getTriangleColour1Count()
    {
        return this.getTriangles()
            .size() - this.getSwitchColour();
    }

    /**
     * Compute and return the normal to a triangles
     *
     * @param i The index of the triangles to get the normal for
     * @return The normal to the triangles
     */
    public XYZ getTriangleNormal(int i)
    {
        final Triangle t = this.getTriangles()
                .get(i);

        final XYZ v0 = this.getVertices()
                .get(t.getVertex(0))
                .getPosition();
        final XYZ v1 = this.getVertices()
                .get(t.getVertex(1))
                .getPosition();
        final XYZ v2 = this.getVertices()
                .get(t.getVertex(2))
                .getPosition();

        final XYZ t1 = XYZMath.opSubtract(v1, v0);
        final XYZ t2 = XYZMath.opSubtract(v2, v0);

        final XYZ t3 = XYZMath.opMultiply(t1, t2);

        return t3.normalised();
    }

    /**
     * (Re-)computes all vertices normals.
     */
    public void computeVertexNormals()
    {
        final int steps =
            this.getTriangles()
                .size() + this.getVertices()
                .size();
        int step = 0;

        final List<List<Integer>> verticesToTriangles =
            new ArrayList<List<Integer>>();

        for (int i = 0; i < this.getVertices()
                .size(); ++i)
            verticesToTriangles.add(new ArrayList<Integer>());

        this.getProgress()
            .progressStart(STEPS_HUNDRED_PERCENT, "Compute normals");

        for (int i = 0; i < this.getTriangles()
                .size(); ++i)
        {
            ++step;

            this.getProgress()
                .progressStep((STEPS_HUNDRED_PERCENT * step) / steps);

            final Triangle t = this.getTriangles()
                    .get(i);

            verticesToTriangles.get(t.getVertex(0))
                .add(i);
            verticesToTriangles.get(t.getVertex(1))
                .add(i);
            verticesToTriangles.get(t.getVertex(2))
                .add(i);
        }

        for (int i = 0; i < this.getVertices()
                .size(); ++i)
        {
            ++step;

            this.getProgress()
                .progressStep((STEPS_HUNDRED_PERCENT * step) / steps);

            final XYZ n = new SimpleXYZ(0.0f, 0.0f, 0.0f);

            for (Integer j : verticesToTriangles.get(i))
            {
                n.opAddAssign(getTriangleNormal(j));
            }

            n.opDivideAssign(verticesToTriangles.get(i).size());

            this.getVertices()
                .get(i)
                .setNormal(n);
        }

        this.getProgress()
            .progressComplete("Normals computed");
    }

    /**
     * Perform a single subdivision pass with perturbations up to the specified size
     * level parameter is just for progress information.
     *
     * @param variation The maximum amount of peturbation
     * @param level The current level number
     * @param levels The maximum level number
     */
    void subdivide(XYZ variation, int level, int levels)
    {
        final int steps =
            this.getVertices()
                .size() + this.getTriangles()
                .size();
        int step = 0;

        final String msg = "Subdivision level " + (1 + level) + " of " +
            levels;

        this.getProgress()
            .progressStart(STEPS_HUNDRED_PERCENT, msg);

        // temporarily clear the data and make a copy
        final TriangleBufferArray oldTriangle = this.getTriangles();

        this.setTriangles(new TriangleBufferArray(oldTriangle.capacity()));

        // perturb all the vertices
        for (int v = 0; v < this.getVertices()
                .size(); ++v)
        {
            ++step;
            this.getProgress()
                .progressStep((STEPS_HUNDRED_PERCENT * step) / steps);

            this.getVertices()
                .setPosition(
                    v,
                    geometry.perturb(
                        this.getVertices().get(v).getPosition(), variation));
        }

        // build map from edges to new midpoints
        final Map<TriangleEdge, Integer> edgeMap =
            new HashMap<TriangleEdge, Integer>();

        // create new vertices and triangles
        for (int t = 0; t < oldTriangle.size(); ++t)
        {
            ++step;
            this.getProgress()
                .progressStep((STEPS_HUNDRED_PERCENT * step) / steps);

            // These are the existing vertices
            final int i0 = oldTriangle.get(t)
                    .getVertex(0);
            final int i1 = oldTriangle.get(t)
                    .getVertex(1);
            final int i2 = oldTriangle.get(t)
                    .getVertex(2);

            // These are the edges
            final TriangleEdge e01 = new TriangleEdge(i0, i1);
            final TriangleEdge e12 = new TriangleEdge(i1, i2);
            final TriangleEdge e20 = new TriangleEdge(i2, i0);

            // Find edges in map, create midpoints for any that don't exist
            Integer e01v = edgeMap.get(e01);
            Integer e12v = edgeMap.get(e12);
            Integer e20v = edgeMap.get(e20);

            if (e01v == null)
            {
                e01v = this.getVertices()
                        .size();

                edgeMap.put(e01, e01v);

                this.getVertices()
                    .add(
                        new SimpleVertex(
                            this.geometry.perturb(
                                this.geometry.getMidpoint(
                                    this.getVertices().get(i0).getPosition(),
                                    this.getVertices().get(i1).getPosition()),
                                variation)));
            }

            if (e12v == null)
            {
                e12v = this.getVertices()
                        .size();

                edgeMap.put(e12, e12v);

                this.getVertices()
                    .add(
                        new SimpleVertex(
                            this.geometry.perturb(
                                this.geometry.getMidpoint(
                                    this.getVertices().get(i1).getPosition(),
                                    this.getVertices().get(i2).getPosition()),
                                variation)));
            }

            if (e20v == null)
            {
                e20v = getVertices()
                        .size();

                edgeMap.put(e20, e20v);

                this.getVertices()
                    .add(
                        new SimpleVertex(
                            this.geometry.perturb(
                                this.geometry.getMidpoint(
                                    this.getVertices().get(i2).getPosition(),
                                    this.getVertices().get(i0).getPosition()),
                                variation)));
            }

            // create the subdivided triangles
            this.getTriangles()
                .add(new SimpleTriangle(i0, e01v, e20v));
            this.getTriangles()
                .add(new SimpleTriangle(e01v, i1, e12v));
            this.getTriangles()
                .add(new SimpleTriangle(e20v, e12v, i2));
            this.getTriangles()
                .add(new SimpleTriangle(e01v, e12v, e20v));
        }

        this.getProgress()
            .progressComplete("Subdivision completed");
    }

    /**
     * Perform a number of subdivisions, possibly some unperturbed ("flat"),
     * and halving the perturbation variation each iteration.
     *
     * @param subdivisions The number of subdivisions to do in total
     * @param flatSubdivisions The number of subdivisions to do with no peturbation
     * @param variation The amount of initial variation to be applyed by peturbation
     */
    public void subdivide(
        int subdivisions, int flatSubdivisions, XYZ variation)
    {
        for (int s = 0; s < subdivisions; ++s)
        {
            if (s < flatSubdivisions)
            {
                subdivide(ImmutableXYZ.XYZ_ZERO, s, subdivisions);
            }
            else
            {
                subdivide(
                    XYZMath.opDivide(variation, (1 << s)), s, subdivisions);
            }
        }
    }

    /**
     * Getter for property emissive.
     * @return Value of property emissive.
     */
    public float getEmissive()
    {
        return this.emissive;
    }

    /**
     * Setter for property emissive.
     * @param emissive New value of property emissive.
     */
    public void setEmissive(float emissive)
    {
        this.emissive = emissive;
    }

    /**
     * Getter for property geometry.
     * @return Value of property geometry.
     */
    public Geometry getGeometry()
    {
        return this.geometry;
    }

    /**
     * Get the buffer containing the vertices data
     *
     * @return The buffer containing packed vertices data
     */
    public VertexBufferArray getVertices()
    {
        return this.vertices;
    }

    /**
     * Get the buffer containing the triangles data
     *
     * @return The buffer containing packed triangles data
     */
    public TriangleBufferArray getTriangles()
    {
        return this.triangles;
    }

    /**
     * Set the vertices for this mesh
     *
     * @param vertices The new vertices
     */
    public void setVertices(VertexBufferArray vertices)
    {
        this.vertices = vertices;
    }

    /**
     * Set the triangles for this array
     *
     * @param triangles The new triangles
     */
    public void setTriangles(TriangleBufferArray triangles)
    {
        this.triangles = triangles;
    }

    /**
     * Get the progress object being used
     *
     * @return The progress object used by this mesh
     */
    public Progress getProgress()
    {
        return progress;
    }

    /**
     * Get the index at which the triangles are drawn with colour 1 instead
     * of colour 0.
     *
     * @return The triangle colour switch index
     */
    public int getSwitchColour()
    {
        return switchColour;
    }

    /**
     * Setter for property switchColour.
     * @param switchColour New value of property switchColour.
     */
    public void setSwitchColour(int switchColour)
    {
        this.switchColour = switchColour;
    }
}
