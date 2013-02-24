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
 * TriangleBufferArray.java
 *
 * Created on December 31, 2005, 1:14 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements an array of triangles using a buffer that can be passed
 * directly to rendering.
 *
 * @author martin
 * @version $Id: TriangleBufferArray.java,v 1.7 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleBufferArray implements TriangleArray, BufferedElement
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleBufferArray.class);

    /** The default number of elements this buffer will initially be capable of holding */
    public static final int DEFAULT_CAPACITY = 30000;

    /** The percentage to increase the capacity by when a resize is required */
    public static final int CAPACITY_PCT_INCREASE = 70;

    /** Number of int values that make up each entry in this buffer */
    private static final int INTS_PER_ENTRY = 3;

    /** Size of an int value in bytes */
    private static final int SIZEOF_INT = 4;

    /** Value for 100 percent */
    private static final int PERCENT_100 = 100;

    // RequireThis OFF: DEFAULT_CAPACITY
    // RequireThis OFF: CAPACITY_PCT_INCREASE
    // RequireThis OFF: INTS_PER_ENTRY
    // RequireThis OFF: PERCENT_100
    // RequireThis OFF: SIZEOF_INT

    /** The IntBuffer used to store the data */
    private IntBuffer buffer;

    /** Creates a new instance of TriangleBufferArray */
    public TriangleBufferArray()
    {
        this.buffer = ByteBuffer.allocateDirect(
                DEFAULT_CAPACITY * INTS_PER_ENTRY * SIZEOF_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        this.buffer.limit(0);
    }

    /**
     * Create a TriangleBufferArray with a specified capacity
     *
     * @param capacity The number of elements this buffer should be capable of holding
     */
    public TriangleBufferArray(int capacity)
    {
        this.buffer = ByteBuffer.allocateDirect(
                capacity * INTS_PER_ENTRY * SIZEOF_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        this.buffer.limit(0);
    }

    /**
     * Resize this buffer to accomodate a larger number of elements
     */
    protected void resizeBuffer()
    {
        // we can't resize it so we have to allocate a new one and copy the data across
        final int slots = this.buffer.capacity() / INTS_PER_ENTRY;
        final int newCapacity =
            this.buffer.capacity() +
            (((slots * CAPACITY_PCT_INCREASE) / PERCENT_100) * INTS_PER_ENTRY);

        final IntBuffer newBuffer =
            ByteBuffer.allocateDirect(newCapacity * SIZEOF_INT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();

        newBuffer.limit(this.buffer.limit());

        if (log.isDebugEnabled())
        {
            log.debug(
                "Resizing triangle buffer capacity to: " + newCapacity + " " +
                newBuffer.capacity());
        }

        this.buffer.rewind();
        newBuffer.put(this.buffer);

        this.buffer = newBuffer;
    }

    /**
     * Set a value in this triangle array
     *
     * @param index The index of the element to be set
     * @param t The triangle object to set this element to
     */
    public void set(int index, Triangle t)
    {
        // first make sure there's room
        while (buffer.capacity() < (index * INTS_PER_ENTRY))
        {
            resizeBuffer();
        }

        if (index >= (this.buffer.limit() / INTS_PER_ENTRY))
        {
            this.buffer.limit((index + 1) * INTS_PER_ENTRY);
        }

        this.buffer.put(index * INTS_PER_ENTRY, t.getVertex(0));
        this.buffer.put((index * INTS_PER_ENTRY) + 1, t.getVertex(1));
        this.buffer.put((index * INTS_PER_ENTRY) + 2, t.getVertex(2));
    }

    /**
     * Set a value in this triangle array
     *
     * @param index The index of the element to be set
     * @param vertex The vertex number to be set (0,1 or 2)
     * @param value The value to set this vertex to
     */
    public void set(int index, int vertex, int value)
    {
        // first make sure there's room
        while (this.buffer.capacity() < (index * INTS_PER_ENTRY))
        {
            resizeBuffer();
        }

        if ((index + 1) >= (this.buffer.limit() / INTS_PER_ENTRY))
        {
            this.buffer.limit((index + 1) * INTS_PER_ENTRY);
        }

        if (vertex > 2)
        {
            throw new ArrayIndexOutOfBoundsException(
                "Illegal access to triangle vertex > 2: " + index);
        }

        this.buffer.put((index * INTS_PER_ENTRY) + vertex, value);
    }

    /**
     * Get a value from this triangle array
     *
     * @param index The index of the element to be retrieved
     * @return The <code>Triangle</code> object at this index
     */
    public Triangle get(int index)
    {
        if (index >= size())
        {
            throw new ArrayIndexOutOfBoundsException(
                "Request for invalid triangle index: " + index + " max is: " +
                size());
        }

        return new BufferedTriangle(index * INTS_PER_ENTRY);
    }

    /**
     * Get a vertex value from this triangle array
     *
     * @param index The index of the element to be retrieved
     * @param vertex The vertex number to be retrieved (0,1 or 2)
     * @return The value of the specified vertex at this index
     */
    public int get(int index, int vertex)
    {
        if (index >= size())
        {
            throw new ArrayIndexOutOfBoundsException(
                "Request for invalid triangle index: " + index + " max is: " +
                size());
        }

        if (vertex > 2)
        {
            throw new ArrayIndexOutOfBoundsException(
                "Illegal access to triangle vertex > 2: " + index);
        }

        return this.buffer.get((index * INTS_PER_ENTRY) + vertex);
    }

    /**
     * Add a triangle to this array
     *
     * @param t The new triangle to be added
     */
    public void add(Triangle t)
    {
        // first make sure there's room
        final int slot = this.buffer.limit() / INTS_PER_ENTRY;

        final int slots = this.buffer.capacity() / INTS_PER_ENTRY;

        //System.out.println("slot=" + slot + " capacity = " + slots);
        if (slot >= slots)
        {
            resizeBuffer();
        }

        this.buffer.limit(this.buffer.limit() + INTS_PER_ENTRY);
        set(slot, t);
    }

    /**
     * Add a triangle composed of discrete vertices to this array
     *
     * @param vertex0 The first vertex of the triangle
     * @param vertex1 The second vertex of the triangle
     * @param vertex2 The third vertex of the triangle
     */
    public void add(int vertex0, int vertex1, int vertex2)
    {
        // first make sure there's room
        if (this.buffer.limit() == this.buffer.capacity())
        {
            resizeBuffer();
        }

        set(this.buffer.limit(), 0, vertex0);
        set(this.buffer.limit(), 1, vertex1);
        set(this.buffer.limit(), 2, vertex2);

        this.buffer.limit(this.buffer.limit() + INTS_PER_ENTRY);
    }

    /**
     * Remove all triangles from this array, clearing it out completely
     */
    public void clear()
    {
        this.buffer.clear();
        this.buffer.limit(0);
    }

    /**
     * Add all triangles from another triangle array to this one
     *
     * @param source The triangle array to be added to this one. The original
     * array is not modified by this operation.
     */
    public void addAll(TriangleBufferArray source)
    {
        // first make sure there's room
        while (
            this.buffer.capacity() < (source.buffer.limit() + buffer.limit()))
        {
            resizeBuffer();
        }

        final int oldLimit = buffer.limit();

        source.buffer.rewind();

        this.buffer.limit(buffer.limit() + source.buffer.limit());
        this.buffer.position(oldLimit);

        this.buffer.put(source.buffer);
        this.buffer.position(0);
    }

    /**
     * Return the size of this array
     *
     * @return The number of triangles contained in this array
     */
    public int size()
    {
        return (this.buffer.limit()) / INTS_PER_ENTRY;
    }

    /**
     * Return the capacity of this array
     *
     * @return the number of elements this array can hold
     */
    public int capacity()
    {
        final int slots = this.buffer.capacity() / INTS_PER_ENTRY;

        return slots;
    }

    /**
     * Get the buffer from this object for passing to routines that need
     * direct access to it such as OpenGL rendering. The buffer will be
     * returned as an unmodifiable copy for read only access.
     *
     * @return An unmodifiable version of the data buffer
     */
    public IntBuffer getBuffer()
    {
        return this.buffer.asReadOnlyBuffer();
    }

    /**
     * Get the size of an element in this array
     *
     * @return The size of an array element in bytes
     */
    public int getElementSize()
    {
        return ByteBufferUtils.sizeofInt();
    }

    /**
     * Class that implements the triangle interface for elements stored in
     * this buffer and acts as a proxy to the data
     */
    protected class BufferedTriangle implements Triangle
    {
        /** The element offset in the buffer that this object is wrapping */
        private int offset;

        /**
         * Create a new instance of BufferedTriangle
         *
         * @param offset The offset in the buffer to be wrapped
         */
        protected BufferedTriangle(int offset)
        {
            this.offset = offset;
        }

        /**
         * Get the vertex at a particular index
         *
         * @param index The index of the vertex to retrieve (0, 1 or 2)
         * @return The value of the vertex specified for this triangle
         */
        public int getVertex(int index)
        {
            if (index > 2)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Illegal access to triangle vertex > 2: " + index);
            }

            return buffer.get(offset + index);
        }
    }
}
