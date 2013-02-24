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
 * MeshStats.java
 *
 * Created on January 11, 2006, 3:47 PM
 *
 */
package com.alvermont.terraj.fracplanet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some stats on a mesh for display in the program.
 *
 * @author martin
 * @version $Id: MeshStats.java,v 1.2 2006/07/06 06:58:34 martin Exp $
 */
public class MeshStats
{
    /**
     * Our logging object
     */
    private static Log log = LogFactory.getLog(MeshStats.class);

    /**
     * Holds value of property vertices.
     */
    private int vertices;

    /**
     * Holds value of property triangles.
     */
    private int triangles;

    /**
     * Holds value of property landTriangles.
     */
    private int landTriangles;

    /**
     * Holds value of property seaTriangles.
     */
    private int seaTriangles;

    /**
     * Creates a new instance of MeshStats
     */
    public MeshStats()
    {
    }

    /**
     * Getter for property vertices.
     *
     * @return Value of property vertices.
     */
    public int getVertices()
    {
        return this.vertices;
    }

    /**
     * Setter for property vertices.
     *
     * @param vertices New value of property vertices.
     */
    public void setVertices(final int vertices)
    {
        this.vertices = vertices;
    }

    /**
     * Getter for property triangles.
     *
     * @return Value of property triangles.
     */
    public int getTriangles()
    {
        return this.triangles;
    }

    /**
     * Setter for property triangles.
     *
     * @param triangles New value of property triangles.
     */
    public void setTriangles(final int triangles)
    {
        this.triangles = triangles;
    }

    /**
     * Getter for property landTriangles.
     *
     * @return Value of property landTriangles.
     */
    public int getLandTriangles()
    {
        return this.landTriangles;
    }

    /**
     * Setter for property landTriangles.
     *
     * @param landTriangles New value of property landTriangles.
     */
    public void setLandTriangles(final int landTriangles)
    {
        this.landTriangles = landTriangles;
    }

    /**
     * Getter for property seaTriangles.
     *
     * @return Value of property seaTriangles.
     */
    public int getSeaTriangles()
    {
        return this.seaTriangles;
    }

    /**
     * Setter for property seaTriangles.
     *
     * @param seaTriangles New value of property seaTriangles.
     */
    public void setSeaTriangles(final int seaTriangles)
    {
        this.seaTriangles = seaTriangles;
    }
}
