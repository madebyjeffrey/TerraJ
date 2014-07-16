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
 * ImmutableFloatRGBA.java
 *
 * Created on January 2, 2006, 10:12 PM
 *
 */
package com.alvermont.terraj.fracplanet.colour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Immutable version of FloatRGBA class. Objects of this class cannot be modified
 * after creation. Attempts to do so will result in an
 * <code>UnsupportedOperationException</code>
 *
 *
 *
 * @author martin
 * @version $Id: ImmutableFloatRGBA.java,v 1.3 2006/07/06 06:59:43 martin Exp $
 */
public class ImmutableFloatRGBA extends FloatRGBA
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(ImmutableFloatRGBA.class);

    /**
     * Creates a new instance of ImmutableFloatRGBA from an existing FloatRGBA
     * object
     *
     * @param c The FloatRGBA object to initialize this one from
     */
    public ImmutableFloatRGBA(FloatRGBA c)
    {
        super(c);
    }

    /**
     * Creates a new instance of ImmutableFloatRGBA from separate colour values
     *
     *
     * @param r The red component value
     * @param g The green component value
     * @param b The blue component value
     */
    public ImmutableFloatRGBA(float r, float g, float b)
    {
        super(r, g, b);
    }

    /**
     * Multiply operation for a double value. The result is assigned to this object
     *
     * @param k The amount each component of this colour is to be multipled by
     */
    public void opMultiplyAssign(double k)
    {
        throw new UnsupportedOperationException(
            "Immutable RGB object cannot be modified");
    }

    /**
     * Subtract operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour to be subtracted from this one.
     */
    public void opSubtractAssign(FloatRGBA v)
    {
        throw new UnsupportedOperationException(
            "Immutable RGB object cannot be modified");
    }

    /**
     * Multiply operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour this one is to be multipled by
     */
    public void opMultiplyAssign(FloatRGBA v)
    {
        throw new UnsupportedOperationException(
            "Immutable RGB object cannot be modified");
    }

    /**
     * Add operation for a FloatRGBA. The result is assigned to this object
     *
     *
     * @param v The colour to be added to this one.
     */
    public void opAddAssign(FloatRGBA v)
    {
        throw new UnsupportedOperationException(
            "Immutable RGB object cannot be modified");
    }
}
