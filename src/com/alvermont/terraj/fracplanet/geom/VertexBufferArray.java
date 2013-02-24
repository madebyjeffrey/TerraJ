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
 * VertexBufferArray.java
 *
 * Created on January 1, 2006, 10:17 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.util.ByteBufferUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Vertex data stored in an byte buffers. The element layout for each item is:
 *
 * Vertex buffer:
 *
 * Item     Data Type       Stored Type     Size        Total Size
 * ---------------------------------------------------------------
 * position XYZ             float           4           12
 * normal   XYZ             float           4           12
 *
 * Colour Buffer:
 *
 * Item     Data Type       Stored Type     Size        Total Size
 * ---------------------------------------------------------------
 * colour   ByteRGBA[2]     byte[4]         4           8
 *
 * Emissive Buffer:
 *
 * Item     Data Type       Stored Type     Size        Total Size
 * ---------------------------------------------------------------
 * emissive boolean[2]      byte[4]         4           8
 *
 * Total element size is therefore: 12 + 12 + 8 + 8 = 40 bytes.
 *
 * This is for performance so we can pass the buffers directly to OpenGL
 * rather than make individual calls with each object.
 *
 *
 * @author martin
 * @version $Id: VertexBufferArray.java,v 1.16 2006/07/06 06:58:34 martin Exp $
 */
public class VertexBufferArray implements VertexArray, BufferedElement
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(VertexBufferArray.class);

    // RequireThis OFF: DEFAULT_CAPACITY
    // RequireThis OFF: CAPACITY_PCT_INCREASE
    // RequireThis OFF: FLOATSIZE
    // RequireThis OFF: ELEMENTSIZE
    // RequireThis OFF: HUNDRED_PERCENT
    // RequireThis OFF: MASK_LSB
    // RequireThis OFF: FLOATS_PER_ENTRY
    // RequireThis OFF: SIZEOF_EMISSIVE_ENTRY
    // RequireThis OFF: SIZEOF_COLOUR_ENTRY

    /** The default number of elements to allocate in a buffer */
    public static final int DEFAULT_CAPACITY = 50000;

    /** The percentage size increase to apply when a buffer reaches its capacity */
    public static final int CAPACITY_PCT_INCREASE = 70;

    /** The size of a floating point value in bytes */
    protected static final int FLOATSIZE = ByteBufferUtils.sizeofFloat();

    /** The number of floats that make up an entry */
    private static final int FLOATS_PER_ENTRY = 6;

    /** The size in bytes of a colour entry */
    private static final int SIZEOF_COLOUR_ENTRY = 8;

    /** The size in bytes of an emissive entry */
    private static final int SIZEOF_EMISSIVE_ENTRY = 8;

    /** The element size of a single element in this buffer in bytes */
    private static final int ELEMENTSIZE =
        (FLOATSIZE * FLOATS_PER_ENTRY) + SIZEOF_COLOUR_ENTRY +
        SIZEOF_EMISSIVE_ENTRY;

    /** Constant value for a flag that is on */
    private static final int MASK_LSB = 0xff;

    /** Constant value for 100 percent */
    private static final int HUNDRED_PERCENT = 100;

    /** The main buffer holding the underlying data storage */
    private ByteBuffer buffer;

    /** A slice of the main buffer holding the position data */
    private FloatBuffer positionBuffer;

    /** A slice of the main buffer holding the normal data */
    private FloatBuffer normalBuffer;

    /** A slice of the main buffer holding the colour data */
    private ByteBuffer colourBuffer;

    /** A slice of the main buffer holding the emissive data */
    private ByteBuffer emissiveBuffer;

    /** Creates a new instance of VertexBufferArray */
    public VertexBufferArray()
    {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Chop up the main buffer into smaller buffers that are used to hold
     * each piece of data.
     *
     * @param capacity The capacity (number of elements it can store) of the buffer
     */
    protected void sliceAndDice(int capacity)
    {
        final int sizeofPositions = capacity * FLOATSIZE * 3;
        final int sizeofNormals = capacity * FLOATSIZE * 3;
        final int sizeofColours = capacity * SIZEOF_COLOUR_ENTRY;
        final int sizeofEmissive = capacity * SIZEOF_EMISSIVE_ENTRY;

        buffer.position(0);
        buffer.limit(sizeofPositions);

        positionBuffer = buffer.slice()
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        buffer.limit(sizeofPositions + sizeofNormals);
        buffer.position(sizeofPositions);

        normalBuffer = buffer.slice()
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        buffer.limit(sizeofPositions + sizeofNormals + sizeofColours);
        buffer.position(sizeofPositions + sizeofNormals);

        colourBuffer = buffer.slice()
                .order(ByteOrder.nativeOrder());

        buffer.limit(buffer.capacity());
        buffer.position(sizeofPositions + sizeofNormals + sizeofColours);

        emissiveBuffer = buffer.slice()
                .order(ByteOrder.nativeOrder());

        positionBuffer.limit(0);
        normalBuffer.limit(0);
        colourBuffer.limit(0);
        emissiveBuffer.limit(0);

        buffer.limit(buffer.capacity());
        buffer.rewind();
    }

    /**
     * Creates a new instance of VertexBufferArray
     *
     * @param capacity The initial capacity of this buffer
     */
    public VertexBufferArray(int capacity)
    {
        this.buffer = ByteBuffer.allocateDirect(capacity * ELEMENTSIZE)
                .order(ByteOrder.nativeOrder());

        sliceAndDice(capacity);
    }

    /**
     * Class that implements the Vertex interface and acts as a proxy to the
     * data stored in the buffer. This allows the caller to avoid knowledge
     * of the buffer storage format.
     */
    protected class BufferedVertex implements Vertex
    {
        /** The index into the buffer that this object is wrapping */
        private int index;

        /**
         * Creates a new instance of BufferedVertex
         *
         * @param index The index into the buffer that this object is wrapping
         */
        protected BufferedVertex(int index)
        {
            this.index = index;
        }

        /**
         * Indexed getter for property colour.
         *
         * @param c Index of the property.
         * @return Value of the property at <CODE>index c</CODE>.
         */
        public ByteRGBA getColour(int c)
        {
            if (c > 1)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid index for colour of vertex: " + index);
            }

            final int boffset = (index * SIZEOF_COLOUR_ENTRY) + (c * 4);

            colourBuffer.position(boffset);

            final byte[] dst = new byte[4];

            colourBuffer.get(dst);

            return new ByteRGBA(dst[0], dst[1], dst[2], dst[3]);
        }

        /**
         * Indexed getter for property emissive.
         *
         * @param ei Index of the property.
         * @return Value of the property at <CODE>index ei</CODE>.
         */
        public boolean getEmissive(int ei)
        {
            if (ei > 1)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid index for emissive value of vertex: " + index);
            }

            final int boffset = (index << 3) + (ei << 2);

            emissiveBuffer.position(boffset);

            boolean emissive = false;

            if (emissiveBuffer.get() != 0)
            {
                emissive = true;
            }

            return emissive;
        }

        /**
         * Getter for property normal.
         *
         * @return Value of property normal.
         */
        public XYZ getNormal()
        {
            return new BufferedXYZ(normalBuffer, index);
        }

        /**
         * Getter for property position.
         *
         * @return Value of property position.
         */
        public XYZ getPosition()
        {
            return new BufferedXYZ(positionBuffer, index);
        }

        /**
         * Indexed setter for property colour.
         *
         * @param ci Index of the property.
         * @param colour New value of the property at <CODE>index ci</CODE>.
         */
        public void setColour(int ci, ByteRGBA colour)
        {
            if (ci > 1)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid index for colour of vertex: " + index);
            }

            final int boffset = (index * SIZEOF_COLOUR_ENTRY) + (ci * 4);

            colourBuffer.position(boffset);

            final byte[] src = new byte[4];

            src[0] = colour.getR();
            src[1] = colour.getG();
            src[2] = colour.getB();
            src[3] = colour.getA();

            colourBuffer.put(src);
        }

        /**
         * Indexed setter for property colour.
         *
         * @param colIndex Index of the property.
         * @param colour New value of the property at <CODE>colIndex</CODE>.
         */
        public void setColour(int colIndex, FloatRGBA colour)
        {
            final ByteRGBA col = new ByteRGBA(colour);

            setColour(colIndex, col);
        }

        /**
         * Indexed setter for property emissive.
         *
         * @param ei Index of the property.
         * @param emissive New value of the property at <CODE>index ei</CODE>.
         */
        public void setEmissive(int ei, boolean emissive)
        {
            if (ei > 1)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid index for emissive value of vertex: " + index);
            }

            final int boffset = (index << 3) + (ei << 2);

            if (emissive)
            {
                emissiveBuffer.put(boffset, (byte) MASK_LSB);
            }
            else
            {
                emissiveBuffer.put(boffset, (byte) 0x0);
            }
        }

        /**
         * Setter for property normal.
         *
         * @param normal New value of property normal.
         */
        public void setNormal(XYZ normal)
        {
            normalBuffer.position(index * 3);

            normalBuffer.put(normal.getX());
            normalBuffer.put(normal.getY());
            normalBuffer.put(normal.getZ());
        }

        /**
         * Setter for property position.
         *
         * @param position New value of property position.
         */
        public void setPosition(XYZ position)
        {
            positionBuffer.position(index * 3);

            positionBuffer.put(position.getX());
            positionBuffer.put(position.getY());
            positionBuffer.put(position.getZ());
        }

        /**
         * Indexed setter for property colour.
         *
         * @param colIndex Index of the property.
         * @param red New value of the red component at <CODE>index</CODE>.
         * @param green New value of the green component at <CODE>index</CODE>.
         * @param blue New value of the blue component at <CODE>index</CODE>.
         */
        public void setColour(int colIndex, float red, float green, float blue)
        {
            setColour(colIndex, new FloatRGBA(red, green, blue));
        }
    }

    /**
     * Class to act as a proxy on elements within the buffer and make them
     * look like XYZ objects. This means that users don't have to deal with
     * the storage format
     */
    protected class BufferedXYZ implements XYZ
    {
        /** The buffer that this object belongs to */
        private FloatBuffer myBuffer;

        /** The index in the buffer that this item is wrapping */
        private int index;

        /**
         * Create a new instance of BufferedXYZ
         *
         * @param myBuffer The buffer that this object belongs to
         * @param index The index in the buffer that this item is wrapping
         */
        protected BufferedXYZ(FloatBuffer myBuffer, int index)
        {
            this.myBuffer = myBuffer;
            this.index = index;
        }

        /**
         * Getter for property x.
         *
         * @return Value of property x.
         */
        public float getX()
        {
            return myBuffer.get(index * 3);
        }

        /**
         * Getter for property y.
         *
         * @return Value of property y.
         */
        public float getY()
        {
            return myBuffer.get((index * 3) + 1);
        }

        /**
         * Getter for property z.
         *
         * @return Value of property z.
         */
        public float getZ()
        {
            return myBuffer.get((index * 3) + 2);
        }

        /**
         * Compute the square of the magnitude of this vector
         *
         * @return The square of the magnitude of this vector
         */
        public float magnitude2()
        {
            final float x = getX();
            final float y = getY();
            final float z = getZ();

            return (x * x) + (y * y) + (z * z);
        }

        /**
         * Compute the magnitude of this vector
         *
         * @return The magnitude of this vector
         */
        public float magnitude()
        {
            return (float) Math.sqrt(magnitude2());
        }

        /**
         * Return a new vector containing the normalised value of this one
         *
         * @return A normalised version of this vector
         */
        public XYZ normalised()
        {
            final float m = magnitude();

            if (m == 0.0)
            {
                throw new AssertionError("Coordinate has zero magnitude");
            }

            return XYZMath.opDivide(this, m);
        }

        /**
         * Normalise this vector. The vector is updated to contain the result
         */
        public void normalise()
        {
            final XYZ n = normalised();

            opAssign(n);
        }

        /**
         * Add a vector to this one and assign the result to this vector
         *
         * @param v The vector to be added to this one
         */
        public void opAddAssign(XYZ v)
        {
            setX(getX() + v.getX());
            setY(getY() + v.getY());
            setZ(getZ() + v.getZ());
        }

        /**
         * Assign another vector to this one
         *
         * @param v The vector to be assigned to this one
         */
        public void opAssign(XYZ v)
        {
            setX(v.getX());
            setY(v.getY());
            setZ(v.getZ());
        }

        /**
         * Divide by a scalar constant and assign the result to this object
         *
         * @param k The constant value to divide by
         */
        public void opDivideAssign(float k)
        {
            setX(getX() / k);
            setY(getY() / k);
            setZ(getZ() / k);
        }

        /**
         * Test for equality between two vectors
         *
         * @param a The first object to be compared
         * @param b The second object to be compared
         * @return <pre>true</pre> if the objects are equal otherwise <pre>false</pre>
         */
        public boolean opEquals(XYZ a, XYZ b)
        {
            return ((a.getX() == b.getX()) && (a.getY() == b.getY()) &&
                (a.getZ() == b.getZ()));
        }

        /**
         * Multiply by a scalar constant and assign the result to this object
         *
         * @param k The constant value to multiply by
         */
        public void opMultiplyAssign(float k)
        {
            setX(getX() * k);
            setY(getY() * k);
            setZ(getZ() * k);
        }

        /**
         * Return a new vector containing this one negated
         *
         * @return A new vector, the negation of this one
         */
        public XYZ opNegate()
        {
            return new SimpleXYZ(-getX(), -getY(), -getZ());
        }

        /**
         * Test for inequality between two vectors
         *
         * @param a The first object to be compared
         * @param b The second object to be compared
         * @return <pre>true</pre> if the objects are not equal otherwise <pre>false</pre>
         */
        public boolean opNotEquals(XYZ a, XYZ b)
        {
            return ((a.getX() != b.getX()) || (a.getY() != b.getY()) ||
                (a.getZ() != b.getZ()));
        }

        /**
         * Subtract a vector from this one and assign the result to this vector
         *
         * @param v The vector to be subtracted from this one
         */
        public void opSubtractAssign(XYZ v)
        {
            setX(getX() - v.getX());
            setY(getY() - v.getY());
            setZ(getZ() - v.getZ());
        }

        /**
         * Setter for property x.
         *
         * @param x New value of property x.
         */
        public void setX(float x)
        {
            myBuffer.position(index * 3);
            myBuffer.put(x);
        }

        /**
         * Setter for property y.
         *
         * @param y New value of property y.
         */
        public void setY(float y)
        {
            myBuffer.position((index * 3) + 1);
            myBuffer.put(y);
        }

        /**
         * Setter for property z.
         *
         * @param z New value of property z.
         */
        public void setZ(float z)
        {
            myBuffer.position((index * 3) + 2);
            myBuffer.put(z);
        }

        /**
         *
         * Get a value by its element number 0=x, 1=y, 2=z
         *
         * @param element The desired element index
         * @return The corresponding element value
         */
        public float getElement(int element)
        {
            switch (element)
            {
                case 0:
                    return getX();

                case 1:
                    return getY();

                case 2:
                    return getZ();

                default:
                    throw new IllegalArgumentException(
                        "Illegal request for " + "XYZ element: " + element);
            }
        }
    }

    /**
     * Resize the buffer. This is done by reallocating a new one and copying
     * data from the old buffer to the new one. This is necessary as buffers
     * cannot be dynamically resized.
     */
    protected void resizeBuffer()
    {
        // we can't resize it so we have to allocate a new one and copy the data
        final int slots = (buffer.capacity() / ELEMENTSIZE);
        final int newCapacity =
            buffer.capacity() +
            (((slots * CAPACITY_PCT_INCREASE) / HUNDRED_PERCENT) * ELEMENTSIZE);

        final ByteBuffer newBuffer =
            ByteBuffer.allocateDirect(newCapacity)
                .order(ByteOrder.nativeOrder());

        if (log.isDebugEnabled())
        {
            log.debug(
                "Resizing vertex buffer capacity to: " + newBuffer.capacity());
        }

        final FloatBuffer oldVertexBuffer = positionBuffer;
        final FloatBuffer oldNormalBuffer = normalBuffer;
        final ByteBuffer oldColourBuffer = colourBuffer;
        final ByteBuffer oldEmissiveBuffer = emissiveBuffer;

        this.buffer = newBuffer;

        sliceAndDice(newCapacity / ELEMENTSIZE);

        oldVertexBuffer.rewind();
        positionBuffer.rewind();
        positionBuffer.limit(oldVertexBuffer.limit());
        positionBuffer.put(oldVertexBuffer);

        oldNormalBuffer.rewind();
        normalBuffer.rewind();
        normalBuffer.limit(oldNormalBuffer.limit());
        normalBuffer.put(oldNormalBuffer);

        oldColourBuffer.rewind();
        colourBuffer.rewind();
        colourBuffer.limit(oldColourBuffer.limit());
        colourBuffer.put(oldColourBuffer);

        oldEmissiveBuffer.rewind();
        emissiveBuffer.rewind();
        emissiveBuffer.limit(oldEmissiveBuffer.limit());
        emissiveBuffer.put(oldEmissiveBuffer);
    }

    /**
     * Ensure that this buffer is big enough to hold a particular number of
     * elements
     *
     * @param index The index of the element that we intend to access. Hence
     * the buffer will be made big enough to store one more than this value.
     */
    protected void ensureCapacity(int index)
    {
        final int fc = (index + 1) * 3;
        final int ec = (index + 1) * 8;
        final int cc = (index + 1) * 8;

        while (positionBuffer.capacity() < fc)
        {
            resizeBuffer();
        }

        if (index >= (positionBuffer.limit() / 3))
        {
            //buffer.limit((index+1) * ELEMENTSIZE);
            positionBuffer.limit(fc);
            normalBuffer.limit(fc);
            colourBuffer.limit(cc);
            emissiveBuffer.limit(ec);
        }
    }

    /**
     * Set a vertex in this buffer
     *
     * @param index The index of the vertex to be set
     * @param v The vertex that is to be stored at this index
     */
    public void set(int index, Vertex v)
    {
        // first make sure there's room
        ensureCapacity(index);

        // now store the data
        positionBuffer.position(index * 3);

        positionBuffer.put(v.getPosition().getX());
        positionBuffer.put(v.getPosition().getY());
        positionBuffer.put(v.getPosition().getZ());

        normalBuffer.position(index * 3);

        normalBuffer.put(v.getNormal().getX());
        normalBuffer.put(v.getNormal().getY());
        normalBuffer.put(v.getNormal().getZ());

        colourBuffer.position(index * SIZEOF_COLOUR_ENTRY);

        for (int c = 0; c < 2; ++c)
        {
            final ByteRGBA col = v.getColour(c);

            colourBuffer.put((byte) (col.getR() & MASK_LSB));
            colourBuffer.put((byte) (col.getG() & MASK_LSB));
            colourBuffer.put((byte) (col.getB() & MASK_LSB));
            colourBuffer.put((byte) (col.getA() & MASK_LSB));
        }

        emissiveBuffer.position(index * SIZEOF_EMISSIVE_ENTRY);

        for (int e = 0; e < 2; ++e)
        {
            final boolean emit = v.getEmissive(e);

            if (emit)
            {
                emissiveBuffer.put((byte) MASK_LSB);
            }
            else
            {
                emissiveBuffer.put((byte) 0x0);
            }

            emissiveBuffer.put((byte) 0);
            emissiveBuffer.put((byte) 0);
            emissiveBuffer.put((byte) 0);
        }
    }

    /**
     * Retrieve a vertex from the buffer
     *
     * @param index The index of the element that is to be retrieved
     * @return The corresponding vertex data for this index
     */
    public Vertex get(int index)
    {
        if (index >= size())
        {
            throw new ArrayIndexOutOfBoundsException(
                "Request for invalid vertex index: " + index + " max is: " +
                size());
        }

        return new BufferedVertex(index);
    }

    /**
     * Set the position of the vertex at the specified index
     *
     * @param index The index of the vertex to be set
     * @param position The position this vertex is to be set to
     */
    public void setPosition(int index, XYZ position)
    {
        // first make sure there's room
        ensureCapacity(index);

        // now set it
        positionBuffer.position(index * 3);

        positionBuffer.put(position.getX());
        positionBuffer.put(position.getY());
        positionBuffer.put(position.getZ());
    }

    /**
     * Retrieve the vertex position at the specified index
     *
     * @param index The index to retrieve the vertex position for
     * @return The vertex position object for the specified index
     */
    public XYZ getPosition(int index)
    {
        return new BufferedXYZ(positionBuffer, index);
    }

    /**
     * Set the position of the vertex at the specified index
     *
     * @param index The index of the vertex to be set
     * @param normal The position this vertex is to be set to
     */
    public void setNormal(int index, XYZ normal)
    {
        // first make sure there's room
        ensureCapacity(index);

        normalBuffer.position(index * 3);

        normalBuffer.put(normal.getX());
        normalBuffer.put(normal.getY());
        normalBuffer.put(normal.getZ());
    }

    /**
     * Retrieve the vertex normal at the specified index
     *
     * @param index The index to retrieve the vertex position for
     * @return The vertex normal object for the specified index
     */
    public XYZ getNormal(int index)
    {
        return new BufferedXYZ(normalBuffer, index);
    }

    /**
     * Add a vertex to this array
     *
     * @param v The vertex that is to be added to the array
     */
    public void add(Vertex v)
    {
        // first make sure there's room
        final int slot = size();

        ensureCapacity(slot);

        set(slot, v);
    }

    /**
     * Add all the vertices in the source array to this array
     *
     * @param source An existing array, the contents of which will be added
     * to this one. The contents of the source array will remain unchanged.
     */
    public void addAll(VertexBufferArray source)
    {
        // first make sure there's room
        final int oldLimit = positionBuffer.limit();
        final int oldCLimit = colourBuffer.limit();

        ensureCapacity((this.size() + source.size()) - 1);

        source.positionBuffer.rewind();
        positionBuffer.position(oldLimit);
        positionBuffer.put(source.positionBuffer);

        source.normalBuffer.rewind();
        normalBuffer.position(oldLimit);
        normalBuffer.put(source.normalBuffer);

        source.colourBuffer.rewind();
        colourBuffer.position(oldCLimit);
        colourBuffer.put(source.colourBuffer);

        source.emissiveBuffer.rewind();
        emissiveBuffer.position(oldCLimit);
        emissiveBuffer.put(source.emissiveBuffer);
    }

    /**
     * Return the number of vertices currently stored
     *
     * @return The number of vertices that are currently stored in the array
     */
    public int size()
    {
        return (normalBuffer.limit()) / 3;
    }

    /**
     * Clear the array. After this call the array will contain no vertices
     */
    public void clear()
    {
        positionBuffer.clear();
        positionBuffer.limit(0);

        normalBuffer.clear();
        normalBuffer.limit(0);

        colourBuffer.clear();
        colourBuffer.limit(0);

        emissiveBuffer.clear();
        emissiveBuffer.limit(0);
    }

    /**
     * Get the main buffer from this object. The buffer will be returned
     * set to read only
     *
     * @return the main buffer object
     */
    public Buffer getBuffer()
    {
        return buffer.asReadOnlyBuffer();
    }

    /**
     * Get the position buffer from this object. The buffer will be returned
     * set to read only
     *
     * @return the position buffer object
     */
    public FloatBuffer getPositionBuffer()
    {
        return positionBuffer.asReadOnlyBuffer();
    }

    /**
     * Get the normal buffer from this object. The buffer will be returned
     * set to read only
     *
     * @return the normal buffer object
     */
    public FloatBuffer getNormalBuffer()
    {
        return normalBuffer.asReadOnlyBuffer();
    }

    /**
     * Get the colour buffer from this object. The buffer will be returned
     * set to read only
     *
     * @return the colour buffer object
     */
    public ByteBuffer getColourBuffer()
    {
        return colourBuffer.asReadOnlyBuffer();
    }

    /**
     * Get the colour buffer from this object. The buffer will be returned
     * set to read only
     *
     * @return the colour buffer object
     */
    public ByteBuffer getEmissiveBuffer()
    {
        return emissiveBuffer.asReadOnlyBuffer();
    }

    /**
     * Get the size of an element stored in the buffer
     *
     * @return The size of each element in this buffer (in bytes)
     */
    public int getElementSize()
    {
        return ELEMENTSIZE;
    }
}
