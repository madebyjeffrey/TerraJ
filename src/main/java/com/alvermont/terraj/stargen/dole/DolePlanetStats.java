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
 * DolePlanetStats.java
 *
 * Created on December 27, 2005, 9:33 AM
 *
 */
package com.alvermont.terraj.stargen.dole;

import com.alvermont.terraj.stargen.*;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Planetstat : Compute the physical properties of a planet.
 *
 * Converted to Java from the public domain C code by Andrew Folkins
 *
 * @author martin
 * @version $Id: DolePlanetStats.java,v 1.3 2006/07/06 06:58:35 martin Exp $
 */
public class DolePlanetStats
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(DolePlanetStats.class);

    /** The math utility object in use */
    private MathUtils utils;

    // MagicNumber OFF

    /** Creates a new instance of DolePlanetStats */
    public DolePlanetStats()
    {
        this.utils = new MathUtils();
    }

    /**
     * Creates a new instance of DolePlanetStats
     *
     * @param utils The math utility object to use for random numbers
     */
    public DolePlanetStats(MathUtils utils)
    {
        this.utils = utils;
    }

    /**
     * Compute the stellar flux at the top of the planet's atmosphere.  This
     * is scaled from the solar constant at Earth of 1400 Watts / square metre
     * by a linear increase of luminosity and an inverse square relation to
     * the distance from the star.  Luminosity is given in Suns, radius in AU's.
     *
     * @param sunLum The stellar luminosity (Sol = 1.0)
     * @param radius The distance from the star in AU
     * @return The stellar flux in watts per square metre
     */
    public double cFlux(double sunLum, double radius)
    {
        return ((1400.0 * sunLum) / (radius * radius));
    }

    /**
     * Compute the temperature at the planet, given the luminosity of it's
     * star and orbital distance.  This is the black body temperature, and
     * does not take into account greenhouse effects or radiation from the
     * planet.
     *
     * @param sunLum The stellar luminosity (Sol = 1.0)
     * @param radius The distance from the star in AU
     * @return The planetary black body temperature
     */
    public double cTemp(double sunLum, double radius)
    {
        return (Math.pow(cFlux(sunLum, radius) / (4.0 * DoleConstants.k), 0.25));
    }

    /**
     * compute the surface gravity of a planet given it's mass in kg and
     * radius in kilometres, return metres / second ** 2.
     *
     * @param mass The planetary mass in KG
     * @param radius The planetary radius in KM
     * @return The surface gravity in meters per second squared
     */
    public double surfaceGravity1(double mass, double radius)
    {
        /* convert to metres */
        final double radiusM = radius * 1000.0;

        return ((DoleConstants.G * mass) / (radiusM * radiusM));
    }

    /**
     * compute the surface gravity of a planet given it's density in grams
     * cubic centimetres and radius in kilometres, returns metres / second ** 2.
     *
     * @param density The density of the planet in grams per cubic centimetre
     * @param radius The planetary radius in KM
     * @return The surface gravity in meters per second squared
     */
    public double surfaceGravity2(double density, double radius)
    {
        return ((4000000 * Math.PI * DoleConstants.G * radius * density) / 3.0);
    }

    /**
     * Compute the escape velocity in km/sec of a planet given it's mass in
     * kg and radius in km
     *
     * @param mass The planetary mass in KG
     * @param radius The planetary radius in KM
     * @return The escape velocity in km per second
     */
    public double escapeVelocity(double mass, double radius)
    {
        return (Math.sqrt((2 * DoleConstants.G * mass) / (radius * 1000)) / 1000.0);
    }

    /**
     * Compute a whole bunch of other stuff.  Most of this is from Fogg.
     *
     * @param star The star in this sytem
     * @param p The planet to be processed
     */
    public void computePlanetStats(Primary star, Planet p)
    {
        double k2;
        double dw;

        p.setHighTemperature(cTemp(star.getLuminosity(), p.getA()));
        p.setLowTemperature(cTemp(star.getLuminosity(), p.getA()));

        p.setMass(p.getDustMass() + p.getGasMass());

        /* convert solar-relative masses to Earth-relative masses */
        p.setMass(p.getMass() * 329390.0);
        p.setDustMass(p.getDustMass() * 329390.0);
        p.setGasMass(p.getGasMass() * 329390.0);

        /* if less than a tenth of the mass is gas, it ain't a gas giant! */
        if ((p.getGasMass() / p.getMass()) < 0.1)
        {
            p.setGasGiant(false);
        }

        /* fiddle densities to likely values */
        if (p.isGasGiant())
        {
            final double dm =
                Math.pow(p.getDustMass(), 0.125) * Math.pow(
                        star.getREcosphere() / p.getA(), 0.25) * 5.5;

            final double dg =
                (0.5 + (0.5 * this.utils.nextDouble())) * Math.sqrt(
                        273 / p.getHighTemperature());

            final double vd = p.getDustMass() / dm;
            final double vg = p.getGasMass() / dg;

            p.setDensity(p.getMass() / (vd + vg));

            k2 = 0.24;
        }
        else
        {
            /* tonnes / m**3 */
            p.setDensity(
                Math.pow(p.getMass(), 0.125) * Math.pow(
                        star.getREcosphere() / p.getA(), 0.25) * 5.5);

            k2 = 0.33;
        }

        p.setOrbitalPeriod(Math.sqrt(Math.pow(p.getA(), 3.0) / star.getMass()));
        p.setRadius(
            Math.pow((p.getMass() * 6.0E21) / (p.getDensity() * 4.2), 0.333) / 1000.0);

        // radians / sec
        p.setDay(
            Math.sqrt(
                (8.73E-2 * p.getMass()) / (0.5 * k2 * p.getRadius() * p.getRadius())));

        /* Tidal braking forces from central star */
        dw = 1.0 * /* matter/mass distribution == density??? */
                (p.getRadius() / 6422.0) * /* in km */
                (1.0 / p.getMass()) * /* in Earth masses */
                Math.pow(star.getMass(), 2.0) * Math.pow(1.0 / p.getA(), 6.0) * -1.3E-6;

        dw *= 4.0 /* star->age */; /* 10E9 years */

        p.setDay(p.getDay() + dw);

        if (p.getDay() < 0.0)
        {
            // TODO: should set the resonant flag?
            p.setDay(0.0);
        }
        else
        {
            p.setDay((2 * Math.PI) / (3600.0 * p.getDay())); // hours
        }

        /* check for spin resonance period */
        if ((p.getE() > 0.1) && (p.getDay() == 0.0))
        {
            p.setDay(
                (p.getOrbitalPeriod() * (1.0 - p.getE())) / (1.0 + p.getE()) * 24.0);
        }

        p.setSurfaceGravity(surfaceGravity2(p.getDensity(), p.getRadius()));
        p.setEscapeVelocity(
            escapeVelocity(
                p.getMass() * DoleConstants.MASS_OF_EARTH, p.getRadius()));

        /* now put masses back into solar units */
        p.setMass(p.getMass() / 329390.0);
        p.setDustMass(p.getDustMass() / 329390.0);
        p.setGasMass(p.getGasMass() / 329390.0);
    }
}
