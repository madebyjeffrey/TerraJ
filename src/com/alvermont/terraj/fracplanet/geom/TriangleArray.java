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
 * TriangleArray.java
 *
 * Created on December 31, 2005, 1:18 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;


/**
 * Interface for objects that handle a triangle array
 *
 * @author martin
 * @version $Id: TriangleArray.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public interface TriangleArray
{
    /**
     * Set a value in this triangle array
     *
     * @param index The index of the element to be set
     * @param t The triangle object to set this element to
     */
    public void set(int index, Triangle t);

    /**
     * Set a value in this triangle array
     *
     * @param index The index of the element to be set
     * @param vertex The vertex number to be set (0,1 or 2)
     * @param value The value to set this vertex to
     */
    public void set(int index, int vertex, int value);

    /**
     * Get a value from this triangle array
     *
     * @param index The index of the element to be retrieved
     * @return The <code>Triangle</code> object at this index
     */
    public Triangle get(int index);

    /**
     * Get a vertex value from this triangle array
     *
     * @param index The index of the element to be retrieved
     * @param vertex The vertex number to be retrieved (0,1 or 2)
     * @return The value of the specified vertex at this index
     */
    public int get(int index, int vertex);

    /**
     * Add a triangle to this array
     *
     * @param t The new triangle to be added
     */
    public void add(Triangle t);

    /**
     * Add a triangle composed of discrete vertices to this array
     *
     * @param vertex0 The first vertex of the triangle
     * @param vertex1 The second vertex of the triangle
     * @param vertex2 The third vertex of the triangle
     */
    public void add(int vertex0, int vertex1, int vertex2);

    /**
     * Add all triangles from another triangle array to this one
     *
     * @param source The triangle array to be added to this one. The original
     * array is not modified by this operation.
     */
    public void addAll(TriangleBufferArray source);

    /**
     * Return the size of this array
     *
     * @return The number of triangles contained in this array
     */
    public int size();

    /**
     * Remove all triangles from this array, clearing it out completely
     */
    public void clear();
}
