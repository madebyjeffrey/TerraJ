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
 * SimpleTriangle.java
 *
 * Created on December 28, 2005, 7:31 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to hold triangles. This is a simple java bean implementation of
 * the <code>Triangle</code> interface.
 *
 * @author martin
 * @version $Id: SimpleTriangle.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public class SimpleTriangle implements Triangle
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(SimpleTriangle.class);

    /** Number of vertices in a triangle */
    private static final int VERTICES_IN_TRIANGLE = 3;

    // RequireThis OFF: VERTICES_IN_TRIANGLE

    /**
     * Holds value of property vertices.
     */
    private int[] vertices = new int[VERTICES_IN_TRIANGLE];

    /** Creates a new instance of Triangle */
    public SimpleTriangle()
    {
    }

    /**
     * Creates a new instance of Triangle
     *
     * @param v0 The vertices index of the first corner of the triangle
     * @param v1 The vertices index of the second corner of the triangle
     * @param v2 The vertices index of the third corner of the triangle
     */
    public SimpleTriangle(int v0, int v1, int v2)
    {
        this.vertices[0] = v0;
        this.vertices[1] = v1;
        this.vertices[2] = v2;
    }

    /**
     * Creates a new instance of Triangle as a copy of an existing one
     *
     * @param t The triangle to use to initialize the new object instance
     */
    public SimpleTriangle(SimpleTriangle t)
    {
        System.arraycopy(t.vertices, 0, this.vertices, 0, this.vertices.length);
    }

    /**
     * Indexed getter for property vertices.
     *
     * @param index Index of the property.
     * @return Value of the property at <CODE>index</CODE>.
     */
    public int getVertex(int index)
    {
        return this.vertices[index];
    }
}
