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
 * DolePlanetRecord.java
 *
 * Created on December 26, 2005, 6:24 PM
 *
 */
package com.alvermont.terraj.stargen.dole;

import com.alvermont.terraj.stargen.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean class representing a planet for the Dole accretion code.
 *
 * @author martin
 * @version $Id: DolePlanetRecord.java,v 1.3 2006/07/06 06:58:35 martin Exp $
 */
public class DolePlanetRecord extends BasicPlanet
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(DolePlanetRecord.class);

    /**
     * Holds value of property rMin.
     */
    private double rMin;

    /**
     * Holds value of property rMax.
     */
    private double rMax;

    /**
     * Holds value of property reach.
     */
    private double reach;

    /** Creates a new instance of DolePlanetRecord */
    public DolePlanetRecord()
    {
    }

    /**
     * Getter for property rMin.
     * @return Value of property rMin.
     */
    public double getRMin()
    {
        return this.rMin;
    }

    /**
     * Setter for property rMin.
     * @param rMin New value of property rMin.
     */
    public void setRMin(double rMin)
    {
        this.rMin = rMin;
    }

    /**
     * Getter for property rMax.
     * @return Value of property rMax.
     */
    public double getRMax()
    {
        return this.rMax;
    }

    /**
     * Setter for property rMax.
     * @param rMax New value of property rMax.
     */
    public void setRMax(double rMax)
    {
        this.rMax = rMax;
    }

    /**
     * Getter for property reach.
     * @return Value of property reach.
     */
    public double getReach()
    {
        return this.reach;
    }

    /**
     * Setter for property reach.
     * @param reach New value of property reach.
     */
    public void setReach(double reach)
    {
        this.reach = reach;
    }
}
