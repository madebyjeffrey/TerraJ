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
 * CloudParameters.java
 *
 * Created on 18 April 2006, 15:33
 */
package com.alvermont.terraj.fracplanet;

import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.colour.ImmutableFloatRGBA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean object that represents the cloud parameters
 *
 * @author  martin
 * @version $Id: CloudParameters.java,v 1.5 2006/07/06 06:58:34 martin Exp $
 */
public class CloudParameters
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(CloudParameters.class);

    /** Creates a new instance of CloudParameters */
    public CloudParameters()
    {
        reset();
    }

    /**
     * Reset all the cloud parameters to their default values
     */
    public void reset()
    {
        this.useOwnSubdivisions = false;
        this.enabled = false;
        this.subdivisions = 5;
        this.height = 10;
        this.colour = new ImmutableFloatRGBA(1.0f, 1.0f, 1.0f);
    }

    /**
     * Holds value of property enabled.
     */
    private boolean enabled;

    /**
     * Getter for property enabled.
     * @return Value of property enabled.
     */
    public boolean isEnabled()
    {
        return this.enabled;
    }

    /**
     * Setter for property enabled.
     * @param enabled New value of property enabled.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Holds value of property colour.
     */
    private FloatRGBA colour;

    /**
     * Getter for property colour.
     * @return Value of property colour.
     */
    public FloatRGBA getColour()
    {
        return this.colour;
    }

    /**
     * Setter for property colour.
     * @param colour New value of property colour.
     */
    public void setColour(FloatRGBA colour)
    {
        this.colour = colour;
    }

    /**
     * Holds value of property useOwnSubdivisions.
     */
    private boolean useOwnSubdivisions;

    /**
     * Getter for property useOwnSubdivisions.
     * @return Value of property useOwnSubdivisions.
     */
    public boolean isUseOwnSubdivisions()
    {
        return this.useOwnSubdivisions;
    }

    /**
     * Setter for property useOwnSubdivisions.
     * @param useOwnSubdivisions New value of property useOwnSubdivisions.
     */
    public void setUseOwnSubdivisions(boolean useOwnSubdivisions)
    {
        this.useOwnSubdivisions = useOwnSubdivisions;
    }

    /**
     * Holds value of property subdivisions.
     */
    private int subdivisions;

    /**
     * Getter for property subdivisions.
     * @return Value of property subdivisions.
     */
    public int getSubdivisions()
    {
        return this.subdivisions;
    }

    /**
     * Setter for property subdivisions.
     * @param subdivisions New value of property subdivisions.
     */
    public void setSubdivisions(int subdivisions)
    {
        this.subdivisions = subdivisions;
    }

    /**
     * Holds value of property height.
     */
    private int height;

    /**
     * Getter for property height.
     * @return Value of property height.
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * Holds value of property seed.
     */
    private int seed;

    /**
     * Getter for property seed.
     * @return Value of property seed.
     */
    public int getSeed()
    {
        return this.seed;
    }

    /**
     * Setter for property seed.
     * @param seed New value of property seed.
     */
    public void setSeed(int seed)
    {
        this.seed = seed;
    }
}
