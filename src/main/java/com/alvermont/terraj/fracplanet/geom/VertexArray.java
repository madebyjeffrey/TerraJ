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
 * VertexArray.java
 *
 * Created on January 1, 2006, 10:18 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;


/**
 * Interface for vertex array objects
 *
 * @author martin
 * @version $Id: VertexArray.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public interface VertexArray
{
    // Object access

    /**
     * Set a vertex at a particular index
     *
     * @param index The index of the vertex to be set
     * @param v The <code>Vertex</code> value to be stored at that index
     */
    public void set(int index, Vertex v);

    /**
     * Retrieve a vertex at a particular index
     *
     * @param index The index to retrieve the vertex for
     * @return The <code>Vertex</code> value at the specified index
     */
    public Vertex get(int index);

    // Direct access (subset of properties)

    /**
     * Set the position of the vertex at the specified index
     *
     * @param index The index of the vertex to be set
     * @param position The position this vertex is to be set to
     */
    public void setPosition(int index, XYZ position);

    /**
     * Retrieve the vertex position at the specified index
     *
     * @param index The index to retrieve the vertex position for
     * @return The vertex position object for the specified index
     */
    public XYZ getPosition(int index);

    /**
     * Set the position of the vertex at the specified index
     *
     * @param index The index of the vertex to be set
     * @param position The normal this vertex is to be set to
     */
    public void setNormal(int index, XYZ position);

    /**
     * Retrieve the vertex normal at the specified index
     *
     * @param index The index to retrieve the vertex normal for
     * @return The vertex normal object for the specified index
     */
    public XYZ getNormal(int index);

    // add elements

    /**
     * Add a vertex to this array
     *
     * @param v The vertex that is to be added to the array
     */
    public void add(Vertex v);

    /**
     * Add all the vertices in the source array to this array
     *
     * @param source An existing array, the contents of which will be added
     * to this one. The contents of the source array will remain unchanged.
     */
    public void addAll(VertexBufferArray source);

    // other operations

    /**
     * Return the number of vertices currently stored
     *
     * @return The number of vertices that are currently stored in the array
     */
    public int size();

    /**
     * Clear the array. After this call the array will contain no vertices
     */
    public void clear();
}
