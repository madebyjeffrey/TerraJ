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
 * Display.java
 *
 * Created on December 22, 2005, 10:15 PM
 */
package com.alvermont.terraj.stargen;

import java.util.Formatter;
import java.util.List;

/**
 * Display related functions. Note that most of these are not localized and are
 * therefore likely to be of limited use in a real Java application. They may be
 * useful for simple cases, for testing and similar purposes so I ported them
 * over from the original C code.
 *
 * The HTML generation lives in the UI package as it is driven from the Swing
 * user interface. It's template based so there is very little actual code
 * there, most of the work is done in the templates.
 *
 * @author martin
 * @version $Id: Display.java,v 1.11 2006/07/06 06:58:33 martin Exp $
 */
public class Display
{
    /** Creates a new instance of Display */
    public Display()
    {
    }

    // MagicNumber OFF

    /* Constant for a billion */
    private static final double ONE_US_BILLION = 1.0E9;

    // RequireThis OFF: ONE_US_BILLION

    /**
     * Format and return a string that describes the basics of the star in a
     * solar system
     *
     * @param star The star to return details for
     * @return A string containing stellar information
     */
    public String getSystemCharacteristics(Primary star)
    {
        final StringBuffer sb = new StringBuffer();
        final Formatter formatter = new Formatter(sb);

        sb.append("System Characteristics\n\n");
        formatter.format("Stellar Mass: %4.2f solar masses\n", star.getMass());
        formatter.format("Stellar Luminosity: %4.2f\n", star.getLuminosity());
        formatter.format(
            "Absolute Magnitude: %4.2f\n", star.getAbsoluteMagnitude());
        formatter.format(
            "Spectral Class: %s Subclass: %d Luminosity Class: %s\n",
            star.getSpectralClass(), star.getSpectralSubclass(),
            star.getLuminosityClass());
        formatter.format(
            "Age: %5.3f billion years\t(%5.3f billion left on main sequence)\n",
            (star.getAge() / ONE_US_BILLION),
            (star.getLife() - star.getAge()) / ONE_US_BILLION);
        formatter.format(
            "Habitable ecosphere radius: %3.3f AU\n", star.getREcosphere());

        return sb.toString();
    }

    /**
     * Format and return basic data for each planet in a solar system
     *
     * @param planets A list of planets to be processed
     * @return A string containing basic data for each planet
     */
    public String getBasicPlanetaryData(List<Planet> planets)
    {
        final StringBuffer sb = new StringBuffer();
        final Formatter formatter = new Formatter(sb);

        for (Planet planet : planets)
        {
            int moons = 0;

            for (
                Planet moon = planet.getFirstMoon(); moon != null;
                    moon = moon.getNextPlanet())
            {
                ++moons;
            }

            char descChar = 'O';

            if (!planet.getType().isGasGiant())
            {
                if (
                    planet.isGreenhouseEffect() &&
                        planet.getSurfacePressure() > 0.0)
                {
                    descChar = '+';
                }
                else if (
                    planet.getHydrosphere() > 0.05 &&
                        planet.getHydrosphere() < 0.95)
                {
                    descChar = '*';
                }
                else if (
                    planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES > 0.1)
                {
                    descChar = 'o';
                }
                else
                {
                    descChar = '.';
                }
            }

            formatter.format(
                "%d\t%7.3f AU\t%8.3f EM\t%c\t%d\n", planet.getNumber(),
                planet.getA(),
                planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES, descChar,
                moons);
        }

        return sb.toString();
    }

    /**
     * Format and return detailed information about a planet
     *
     * @param planet The planet to return details for
     * @return A string containing the planetary details
     */
    public String getPlanetDetails(Planet planet)
    {
        final StringBuffer sb = new StringBuffer();
        final Formatter formatter = new Formatter(sb);

        formatter.format("Planet %d", planet.getNumber());

        if (planet.getType().isGasGiant())
        {
            sb.append("\t*gas giant*");
        }

        sb.append("\n");

        if ((int) planet.getDay() == (int) (planet.getOrbitalPeriod() * 24.0))
        {
            sb.append(
                "Planet's rotation is in a resonant spin lock with the star\n");
        }

        formatter.format(
            "Mass:\t\t\t\t%5.3f\tEarth masses\n",
            planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES);

        if (!(planet.getType().isGasGiant()))
        {
            formatter.format(
                "Surface gravity:\t\t%4.2f\tEarth gees\n",
                planet.getSurfaceGravity());
            formatter.format(
                "Surface pressure:\t\t%5.3f\tEarth atmospheres",
                (planet.getSurfacePressure() / 1000.0));

            if (
                (planet.isGreenhouseEffect()) &&
                    (planet.getSurfacePressure() > 0.0))
            {
                formatter.format("\tGREENHOUSE EFFECT\n");
            }
            else
            {
                formatter.format("\n");
            }

            formatter.format(
                "Surface temperature:\t\t%4.2f\tdegrees Celcius\n",
                (planet.getSurfaceTemperature() -
                Constants.FREEZING_POINT_OF_WATER));
        }

        formatter.format(
            "Equatorial radius:\t\t%3.1f\tKm\n", planet.getRadius());
        formatter.format(
            "Density:\t\t\t%5.3f\tgrams/cc\n", planet.getDensity());
        formatter.format("Eccentricity of orbit:\t\t%5.3f\n", planet.getE());
        formatter.format(
            "Escape Velocity:\t\t%4.2f\tKm/sec\n",
            planet.getEscapeVelocity() / Constants.CM_PER_KM);
        formatter.format(
            "Molecular weight retained:\t%4.2f and above\n",
            planet.getMolecularWeight());
        formatter.format(
            "Surface acceleration:\t\t%4.2f\tcm/sec2\n",
            planet.getSurfaceAcceleration());
        formatter.format(
            "Axial tilt:\t\t\t%2.0f\tdegrees\n", planet.getAxialTilt());
        formatter.format("Planetary albedo:\t\t%5.3f\n", planet.getAlbedo());
        formatter.format(
            "Length of year:\t\t\t%4.2f\tdays\n", planet.getAlbedo());
        formatter.format("Length of day:\t\t\t%4.2f\thours\n", planet.getDay());
        formatter.format("Breathability:\t\t\t%s\n", planet.getBreathability());

        if (!(planet.getType().isGasGiant()))
        {
            formatter.format(
                "Boiling point of water:\t\t%3.1f\tdegrees Celcius\n",
                (planet.getBoilingPoint() - Constants.FREEZING_POINT_OF_WATER));
            formatter.format(
                "Hydrosphere percentage:\t\t%4.2f\n",
                (planet.getHydrosphere() * 100.0));
            formatter.format(
                "Cloud cover percentage:\t\t%4.2f\n",
                (planet.getCloudCover() * 100));
            formatter.format(
                "Ice cover percentage:\t\t%4.2f\n", (planet.getIceCover() * 100));
        }

        return sb.toString();
    }
}
