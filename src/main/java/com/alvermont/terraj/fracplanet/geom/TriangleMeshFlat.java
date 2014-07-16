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
 * TriangleMeshFlat.java
 *
 * Created on 19 April 2006, 08:35
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A mesh representing a flat terrain. Note flat refers to the geometry in
 * use. Obviously the terrain itself isn't entirely flat!
 *
 * @author  martin
 * @version $Id: TriangleMeshFlat.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleMeshFlat extends TriangleMeshCloud
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(TriangleMeshFlat.class);

    /**
     * An enum that represents the different types of terrain this class
     * can create
     */
    public enum FlatTerrainType
    {
        /** Enum constant for a triangular 'flat' terrain */
        TRIANGLE,
        /** Enum constant for a square 'flat' terrain */
        SQUARE, 
        /** Enum constant for a hexagonal 'flat' terrain */
        HEXAGON;
    }

    /**
     * Creates a new instance of TriangleMeshFlat
     *
     * @param ttype The type of terrain to be generated (triangle, square, hexagon)
     *
     * @param z The Z distance of the terrain from the origin
     * @param utils The math object with the seeded random number generator
     * @param progress The progress object to be used
     * @param geometry The geometry object to be used
     */
    public TriangleMeshFlat(
        FlatTerrainType ttype, float z, MathUtils utils, Progress progress,
        Geometry geometry)
    {
        super(progress, geometry);

        this.ttype = ttype;
        this.z = z;

        buildMesh();
    }

    /**
     * Create the mesh object
     */
    protected void buildMesh()
    {
        switch (this.ttype)
        {
            case TRIANGLE:

                for (int i = 0; i < 3; ++i)
                {
                    addVertex(
                        new SimpleVertex(
                            new SimpleXYZ(
                                (float) Math.cos(i * 2.0 * Math.PI / 3.0),
                                (float) Math.sin(i * 2.0 * Math.PI / 3.0), z)));
                }

                addTriangle(new SimpleTriangle(0, 1, 2));

                break;

            case SQUARE:
                addVertex(new SimpleVertex(new SimpleXYZ(0.0f, 0.0f, z)));

                for (int i = 0; i < 4; ++i)
                {
                    addVertex(
                        new SimpleVertex(
                            new SimpleXYZ(
                                (float) Math.cos(i * Math.PI / 2.0),
                                (float) Math.sin(i * Math.PI / 2.0), z)));
                }

                for (int i = 0; i < 4; ++i)
                {
                    addTriangle(new SimpleTriangle(0, 1 + i, 1 + (i + 1) % 4));
                }

                break;

            case HEXAGON:default:
                addVertex(new SimpleVertex(new SimpleXYZ(0.0f, 0.0f, z)));

                for (int i = 0; i < 6; ++i)
                {
                    addVertex(
                        new SimpleVertex(
                            new SimpleXYZ(
                                (float) Math.cos(i * Math.PI / 3.0),
                                (float) Math.sin(i * Math.PI / 3.0), z)));
                }

                for (int i = 0; i < 6; ++i)
                {
                    addTriangle(new SimpleTriangle(0, 1 + i, 1 + (i + 1) % 6));
                }

                break;
        }
    }

    /**
     * Holds value of property ttype.
     */
    private FlatTerrainType ttype;

    /**
     * Getter for property ttype.
     * @return Value of property ttype.
     */
    public FlatTerrainType getTtype()
    {
        return this.ttype;
    }

    /**
     * Holds value of property z.
     */
    private float z;

    /**
     * Getter for property z.
     * @return Value of property z.
     */
    public float getZ()
    {
        return this.z;
    }

    /**
     * Setter for property z.
     * @param z New value of property z.
     */
    public void setZ(float z)
    {
        this.z = z;
    }
}
