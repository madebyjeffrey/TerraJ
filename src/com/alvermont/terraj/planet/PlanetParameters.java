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
 * PlanetParameters.java
 *
 * Created on 22 January 2006, 12:15
 */
package com.alvermont.terraj.planet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean class holding properties of the planet
 *
 * @author  martin
 * @version $Id: PlanetParameters.java,v 1.4 2006/07/06 06:58:36 martin Exp $
 */
public class PlanetParameters
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(PlanetParameters.class);

    /** Creates a new instance of PlanetParameters */
    public PlanetParameters()
    {
        reset();

        seed = Math.random();
    }

    /**
     * Create a new instance of PlanetParameters as a copy of another one
     *
     * @param source The object that will be used to initialize this one
     */
    public PlanetParameters(PlanetParameters source)
    {
        this.seed = source.seed;
        this.m = source.m;
        this.pow = source.pow;
        this.dd1 = source.dd1;
        this.dd2 = source.dd2;
    }

    /**
     * Reset all the parameters to their default values. Note: does not
     * change the seed value.
     */
    public void reset()
    {
        this.m = -.02;
        this.pow = 0.47;
        this.dd1 = 0.4;
        this.dd2 = 0.03;
    }

    /** Initial altitude (slightly below sea level) */
    protected double m = -.02;

    /**
     * Getter for property m.
     * @return Value of property m.
     */
    public double getInitialAltitude()
    {
        return this.m;
    }

    /**
     * Setter for property m.
     * @param m New value of property m.
     */
    public void setInitialAltitude(double m)
    {
        this.m = m;
    }

    /** power for distance function */
    protected double pow = 0.47;

    /**
     * Getter for property Power.
     * @return Value of property Power.
     */
    public double getPower()
    {
        return this.pow;
    }

    /**
     * Setter for property Power.
     * @param power New value of property Power.
     */
    public void setPower(double power)
    {
        this.pow = power;
    }

    /** Weight for altitude difference */
    protected double dd1 = 0.4;

    /**
     * Getter for property dd1.
     * @return Value of property dd1.
     */
    public double getAltitudeDifferenceWeight()
    {
        return this.dd1;
    }

    /**
     * Setter for property dd1.
     * @param newDd1 New value of property dd1.
     */
    public void setAltitudeDifferenceWeight(double newDd1)
    {
        this.dd1 = newDd1;
    }

    /** Weight for distance */
    protected double dd2 = 0.03;

    /**
     * Getter for property distanceWeight.
     * @return Value of property distanceWeight.
     */
    public double getDistanceWeight()
    {
        return this.dd2;
    }

    /**
     * Setter for property distanceWeight.
     * @param distanceWeight New value of property distanceWeight.
     */
    public void setDistanceWeight(double distanceWeight)
    {
        this.dd2 = distanceWeight;
    }

    /**
     * Holds value of property seed.
     */
    private double seed;

    /**
     * Getter for property seed.
     * @return Value of property seed.
     */
    public double getSeed()
    {
        return this.seed;
    }

    /**
     * Setter for property seed.
     * @param seed New value of property seed.
     */
    public void setSeed(double seed)
    {
        this.seed = seed;
    }
}
