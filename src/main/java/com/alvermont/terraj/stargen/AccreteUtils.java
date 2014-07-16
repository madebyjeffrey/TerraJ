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
 * AccreteUtils.java
 *
 * Created on 15 January 2006, 21:06
 */
package com.alvermont.terraj.stargen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some computations factored out of the Accrete class to reduce its size
 *
 * @author  martin
 * @version $Id: AccreteUtils.java,v 1.6 2006/07/06 06:58:33 martin Exp $
 */
public class AccreteUtils
{
    /** Create a new instance of AccreteUtils */
    protected AccreteUtils()
    {
        throw new UnsupportedOperationException();
    }

    /** Our logger object */
    private static Log log = LogFactory.getLog(AccreteUtils.class);

    /**
     * Get the stellar dust limit for a particular mass
     *
     * @param stellMassRatio The stellar mass ratio
     * @return The dust limit for the specified mass
     */
    public static double getStellarDustLimit(double stellMassRatio)
    {
        return (200.0 * Math.pow(stellMassRatio, (1.0 / 3.0)));
    }

    /**
     * Gets the position of the nearest planet for a particular stellar mass
     * ratio.
     *
     * @param stellMassRatio The mass of the star (Sol = 1.0)
     * @return The nearest planetary distance
     */
    public static double getNearestPlanet(double stellMassRatio)
    {
        return (0.3 * Math.pow(stellMassRatio, (1.0 / 3.0)));
    }

    /**
     * Gets the position of the farthest planet for a particular stellar mass
     * ratio.
     *
     * @param stellMassRatio The mass of the star (Sol = 1.0)
     * @return The furthest planetary distance
     */
    public static double getFarthestPlanet(double stellMassRatio)
    {
        return (50.0 * Math.pow(stellMassRatio, (1.0 / 3.0)));
    }
}
