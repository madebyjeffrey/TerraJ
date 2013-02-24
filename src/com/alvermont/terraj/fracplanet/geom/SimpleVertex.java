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
 * SimpleVertex.java
 *
 * Created on December 28, 2005, 12:58 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents one vertex. This is a simple bean implementation of the
 * <code>Vertex</code> interface.
 *
 * @author martin
 * @version $Id: SimpleVertex.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class SimpleVertex implements Vertex
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(SimpleVertex.class);

    /** The position of this vertex */
    private XYZ position;

    /** The normal to this vertex */
    private XYZ normal;

    /** Colour information for this vertex */
    private ByteRGBA[] colour = new ByteRGBA[2];

    /** Emissive information for this vertex */
    private boolean[] emissive = new boolean[2];

    /**
     * Creates a new instance of SimpleVertex. The new object will
     * have a zero normal and position, be coloured black and not be
     * emissive.
     */
    public SimpleVertex()
    {
        this.colour[0] = ByteRGBA.BLACK;
        this.colour[1] = ByteRGBA.BLACK;

        this.emissive[0] = false;
        this.emissive[1] = false;
    }

    /**
     * Create a new instance of SimpleVertex as a copy of an existing one
     *
     * @param v The vertex to use to initialize the new object
     */
    public SimpleVertex(Vertex v)
    {
        this.position = new SimpleXYZ(v.getPosition());
        this.normal = new SimpleXYZ(v.getNormal());

        this.colour[0] = v.getColour(0);
        this.colour[1] = v.getColour(1);

        this.emissive[0] = v.getEmissive(0);
        this.emissive[1] = v.getEmissive(1);
    }

    /**
     * Create a new instance of SimpleVertex at a particular point
     *
     * @param p The point to assign to the position of this vertex
     */
    public SimpleVertex(XYZ p)
    {
        this();

        this.position = p;
        this.normal = ImmutableXYZ.XYZ_ZERO;
    }

    /**
     * Getter for property position.
     * @return Value of property position.
     */
    public XYZ getPosition()
    {
        return this.position;
    }

    /**
     * Setter for property position.
     * @param position New value of property position.
     */
    public void setPosition(XYZ position)
    {
        this.position = position;
    }

    /**
     * Getter for property normal.
     * @return Value of property normal.
     */
    public XYZ getNormal()
    {
        return this.normal;
    }

    /**
     * Setter for property normal.
     * @param normal New value of property normal.
     */
    public void setNormal(XYZ normal)
    {
        this.normal = normal;
    }

    /**
     * Indexed getter for property colour.
     * @param index Index of the property.
     * @return Value of the property at <CODE>index</CODE>.
     */
    public ByteRGBA getColour(int index)
    {
        return this.colour[index];
    }

    /**
     * Indexed setter for property newColour.
     *
     * @param index Index of the property.
     * @param newColour New value of the property at <CODE>index</CODE>.
     */
    public void setColour(int index, ByteRGBA newColour)
    {
        this.colour[index] = newColour;
    }

    /**
     * Indexed setter for property newColour.
     *
     * @param index Index of the property.
     * @param newColour New value of the property at <CODE>index</CODE>.
     */
    public void setColour(int index, FloatRGBA newColour)
    {
        this.colour[index] = new ByteRGBA(newColour);
    }

    /**
     * Indexed getter for property emissive.
     * @param index Index of the property.
     * @return Value of the property at <CODE>index</CODE>.
     */
    public boolean getEmissive(int index)
    {
        return this.emissive[index];
    }

    /**
     * Indexed setter for property newEmissive.
     *
     * @param index Index of the property.
     * @param newEmissive New value of the property at <CODE>index</CODE>.
     */
    public void setEmissive(int index, boolean newEmissive)
    {
        this.emissive[index] = newEmissive;
    }

    /**
     * Indexed setter for property colour.
     *
     * @param index Index of the property.
     * @param red New value of the red component at <CODE>index</CODE>.
     * @param green New value of the green component at <CODE>index</CODE>.
     * @param blue New value of the blue component at <CODE>index</CODE>.
     */
    public void setColour(int index, float red, float green, float blue)
    {
        setColour(index, new FloatRGBA(red, green, blue));
    }
}
