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
 * DoleConstants.java
 *
 * Created on December 26, 2005, 5:11 PM
 *
 */
package com.alvermont.terraj.stargen.dole;

import com.alvermont.terraj.stargen.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Constants used by the Dole Accretion class.
 *
 * @author martin
 * @version $Id: DoleConstants.java,v 1.2 2006/07/06 06:58:35 martin Exp $
 */
public class DoleConstants
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(DoleConstants.class);

    /** Coeff. of cloud density (solar masses / AU**3) */
    public static final double AO = 0.0015;

    /** Cloud density parameter                        */
    public static final double ALPHA = 5.0;

    /** Cloud density parameter                        */
    public static final double GAMMA = 0.33;

    /** Ratio of overall density to density of dust    */
    public static final double K = 50;

    /** Eccentricity of cloud particles                */
    public static final double W = 0.20;

    /** Nuclei mass (solar masses)                     */
    public static final double M0 = 1E-15;

    /** Critical mass paramter (solar masses)          */
    public static final double B = 1.2E-5;

    /** Gas capture parameter                          */
    public static final double BETA = 0.5;

    /** Minimum orbital radius (AU) */
    public static final double MINRADIUS = 0.3;

    /** Minimum orbital radius (AU) */
    public static final double MAXRADIUS = 50.0;

    /** Maximum eccentricity of dust particles */
    public static final double MAX_ECCENTRICITY = 0.20;

    // not currently used
    //public static final int GAS = 1;

    // constants from planetstats

    /** Mass of the Earth in KG */
    public static final double MASS_OF_EARTH = 5.98E24;

    /** Gravitational constant G = 6.67E-11,   nt - m**2 / kg**2 */
    public static final double G = 6.67E-11;

    /** Constant used in planetary temperature calcluations J / mol K  */
    public static final double k = 5.67E-8;

    /** Creates a new instance of DoleConstants */
    public DoleConstants()
    {
    }
}
