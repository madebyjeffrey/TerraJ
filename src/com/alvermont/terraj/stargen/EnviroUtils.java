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
 * EnviroUtils.java
 *
 * Created on 15 January 2006, 21:44
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Computations factored out of the Enviro class
 *
 * @author  martin
 * @version $Id: EnviroUtils.java,v 1.8 2006/07/06 06:58:33 martin Exp $
 */
public class EnviroUtils
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(EnviroUtils.class);

    /** Creates a new instance of EnviroUtils */
    protected EnviroUtils()
    {
        throw new UnsupportedOperationException();
    }

    // MagicNumber OFF

    /**
     * Calculates the stellar luminosity for a star
     *
     * @param massRatio The mass of the star (Sol = 1.0)
     * @return The stellar luminosity (Sol = 1.0)
     */
    public static double getLuminosity(double massRatio)
    {
        double n;

        if (massRatio < 1.0)
        {
            n = (1.75 * (massRatio - 0.1)) + 3.325;
        }
        else
        {
            n = (0.5 * (2.0 - massRatio)) + 4.4;
        }

        return (Math.pow(massRatio, n));
    }

    /**
     * This function, given the orbital radius of a planet in AU, returns
     * the orbital 'zone' of the particle.
     *
     * @param luminosity The stellar luminosity *Sol = 1.0)
     * @param orbRadius The orbital radius of the planet (in AU)
     * @return The orbital zone of the planet
     */
    public static int getOrbitalZone(double luminosity, double orbRadius)
    {
        if (orbRadius < (4.0 * Math.sqrt(luminosity)))
        {
            return (1);
        }
        else if (orbRadius < (15.0 * Math.sqrt(luminosity)))
        {
            return (2);
        }
        else
        {
            return (3);
        }
    }

    /**
     * Get the radius of the volume for a specified mass
     *
     * @param mass The mass (in units of solar masses)
     * @param density The density (in grams/cc)
     * @return The orbital radius in km
     */
    public static double getVolumeRadius(double mass, double density)
    {
        final double massG = mass * Constants.SOLAR_MASS_IN_GRAMS;
        final double volume = massG / density;

        return (Math.pow((3.0 * volume) / (4.0 * Math.PI), (1.0 / 3.0)) / Constants.CM_PER_KM);
    }

    /**
     * Returns the radius of the planet in kilometers.
     * This formula is listed as eq.9 in Fogg's article, although some typos
     * crop up in that eq.  See "The Internal Constitution of Planets", by
     * Dr. D. S. Kothari, Mon. Not. of the Royal Astronomical Society, vol 96
     * pp.833-843, 1936 for the derivation.  Specifically, this is Kothari's
     * eq.23, which appears on page 840.
     *
     * @param mass The mass of the planet
     * @param giant <pre>true</pre> if the planet is a gas giant
     * @param zone The orbital zone of the planet
     * @return The radius of the planet in Kilometres
     */
    public static double getKothariRadius(double mass, boolean giant, int zone)
    {
        double temp1;
        double temp;
        double temp2;
        double atomicWeight;
        double atomicNum;

        if (zone == 1)
        {
            if (giant)
            {
                atomicWeight = 9.5;
                atomicNum = 4.5;
            }
            else
            {
                atomicWeight = 15.0;
                atomicNum = 8.0;
            }
        }
        else
        {
            if (zone == 2)
            {
                if (giant)
                {
                    atomicWeight = 2.47;
                    atomicNum = 2.0;
                }
                else
                {
                    atomicWeight = 10.0;
                    atomicNum = 5.0;
                }
            }
            else
            {
                if (giant)
                {
                    atomicWeight = 7.0;
                    atomicNum = 4.0;
                }
                else
                {
                    atomicWeight = 10.0;
                    atomicNum = 5.0;
                }
            }
        }

        temp1 = atomicWeight * atomicNum;

        temp = (2.0 * Constants.BETA_20 * Math.pow(
                    Constants.SOLAR_MASS_IN_GRAMS, (1.0 / 3.0))) / (Constants.A1_20 * Math.pow(
                    temp1, (1.0 / 3.0)));

        temp2 = Constants.A2_20 * Math.pow(atomicWeight, (4.0 / 3.0)) * Math.pow(
                    Constants.SOLAR_MASS_IN_GRAMS, (2.0 / 3.0));
        temp2 = temp2 * Math.pow(mass, (2.0 / 3.0));
        temp2 = temp2 / (Constants.A1_20 * MathUtils.pow2(atomicNum));
        temp2 = 1.0 + temp2;
        temp = temp / temp2;
        temp = (temp * Math.pow(mass, (1.0 / 3.0))) / Constants.CM_PER_KM;

        /* Make Earth = actual earth */
        temp /= Constants.JIMS_FUDGE;

        return (temp);
    }

    /**
     *        The mass passed in is in units of solar masses, and the orbital radius
     *        is in units of AU.        The density is returned in units of grams/cc.
     *
     * @param mass The mass in solar masses
     * @param orbRadius The orbital radius in AU
     * @param rEcosphere The radius of the stellar ecosphere
     * @param gasGiant <pre>true</pre> if this is a gas giant
     * @return The density in grams/cc
     */
    public static double getEmpiricalDensity(
        double mass, double orbRadius, double rEcosphere, boolean gasGiant)
    {
        double temp;

        temp = Math.pow(mass * Constants.SUN_MASS_IN_EARTH_MASSES, (1.0 / 8.0));
        temp = temp * MathUtils.fourthRoot(rEcosphere / orbRadius);

        if (gasGiant)
        {
            return temp * 1.2;
        }
        else
        {
            return temp * 5.5;
        }
    }

    /**
     *        The massG passed in is in units of solar masses, and the radius
     *        is in units of km. The density is returned in units of grams/cc.
     *
     * @param mass The mass in solar masses
     * @param equatorialRadius The planetary equatorial radius in AU
     * @return The density in grams/cc
     */
    public static double getVolumeDensity(double mass, double equatorialRadius)
    {
        final double massG = mass * Constants.SOLAR_MASS_IN_GRAMS;
        final double equatRadius = equatorialRadius * Constants.CM_PER_KM;
        final double volume =
            (4.0 * Math.PI * MathUtils.pow3(equatRadius)) / 3.0;

        return massG / volume;
    }

    /**
     * This function returns the boiling point of water in an atmosphere of
     * pressure 'surfPressure', given in millibars. The boiling point is
     * returned in units of Kelvin.  This is Fogg's eq.21.
     *
     * @param surfPressure The surface atmospheric pressure in millibars
     * @return The corresponding boiling point of water in degrees Kelvin
     */
    public static double getBoilingPoint(double surfPressure)
    {
        final double surfacePressureInBars =
            surfPressure / Constants.MILLIBARS_PER_BAR;

        return (1.0 / ((Math.log(surfacePressureInBars) / -5050.5) +
            (1.0 / 373.0)));
    }
}
