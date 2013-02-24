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
 * Enviro.java
 *
 * Created on 21 December 2005, 20:43
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Planetary environment related calculations and similar functions.
 *
 * Note that most of the comments in this class on the derivation of
 * equations and other source material are taken directly from the
 * original C code.
 *
 * @author  martin
 * @version $Id: Enviro.java,v 1.19 2006/07/06 06:58:33 martin Exp $
 */
public class Enviro
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(Enviro.class);

    /** Limit on looping */
    private static final int LOOP_LIMIT = 25;

    // RequireThis OFF: LOOP_LIMIT
    // MagicNumber OFF

    /** The utils object we use */
    private MathUtils utils;

    /** The gases object we use */
    private Gases gases = new Gases();

    /**
     * Creates a new instance of Enviro
     */
    public Enviro()
    {
        this.utils = new MathUtils();
    }

    /**
     * Creates a new instance of Enviro
     *
     * @param utils The math utils object to use for random numbers
     */
    public Enviro(MathUtils utils)
    {
        this.utils = utils;
    }

    /**
     * The separation is in units of AU, and both masses are in units of solar
     * masses. The period returned is in terms of Earth days.
     *
     * @param The separation of the masses in AU
     * @param smallMass The smaller mass in multiples of Sol masses
     * @param largeMass The larget mass in multiples of Sol masses
     * @return The orbital period in Earth days
     */
    double getPeriod(double separation, double smallMass, double largeMass)
    {
        final double periodInYears =
            Math.sqrt(MathUtils.pow3(separation) / (smallMass + largeMass));

        return (periodInYears * Constants.DAYS_IN_A_YEAR);
    }

    /**
     * Fogg's information for this routine came from Dole "Habitable Planets
     * for Man", Blaisdell Publishing Company, NY, 1964.  From this, he came
     * up with his eq.12, which is the equation for the 'baseAngularVelocity'
     * below.  He then used an equation for the change in angular velocity per
     * time (dw/dt) from P. Goldreich and S. Soter's paper "Q in the Solar
     * System" in Icarus, vol 5, pp.375-389 (1966).         Using as a comparison the
     * change in angular velocity for the Earth, Fogg has come up with an
     * approximation for our new planet (his eq.13) and take that into account.
     * This is used to find 'changeInAngularVelocity' below.
     *
     * Input parameters are mass (in solar masses), radius (in Km), orbital
     * period (in days), orbital radius (in AU), density (in g/cc),
     * eccentricity, and whether it is a gas giant or not.
     *
     * @param planet The planet to calculate the day length for
     * @return The length of the day in units of hours.
     */
    public double getDayLength(Planet planet)
    {
        final double planetaryMassInGrams =
            planet.getMass() * Constants.SOLAR_MASS_IN_GRAMS;
        final double equatorialRadiusInCm =
            planet.getRadius() * Constants.CM_PER_KM;
        final double yearInHours = planet.getOrbitalPeriod() * 24.0;

        final boolean giant = planet.getType().isGasGiant();

        double k2;
        double changeInAngularVelocity;
        double angVelocity;
        double spinResonanceFactor;
        double dayInHours;

        boolean stopped = false;

        planet.setResonantPeriod(false); /* Warning: Modify the planet */

        if (giant)
        {
            k2 = 0.24;
        }
        else
        {
            k2 = 0.33;
        }

        final double baseAngularVelocity =
            Math.sqrt(
                (2.0 * Constants.J * (planetaryMassInGrams)) / (k2 * MathUtils.pow2(
                        equatorialRadiusInCm)));

        /* This next calculation determines how much the planet's rotation is   */
        /* slowed by the presence of the star.                                  */
        changeInAngularVelocity = Constants.CHANGE_IN_EARTH_ANG_VEL * (planet.getDensity() / Constants.EARTH_DENSITY) * (equatorialRadiusInCm / Constants.EARTH_RADIUS) * (Constants.EARTH_MASS_IN_GRAMS / planetaryMassInGrams) * Math.pow(
                    planet.getPrimary().getMass(), 2.0) * (1.0 / Math.pow(
                    planet.getA(), 6.0));

        angVelocity = baseAngularVelocity +
            (changeInAngularVelocity * planet.getPrimary()
                .getAge());

        /* Now we change from rad/sec to hours/rotation.                      */
        if (angVelocity <= 0.0)
        {
            stopped = true;
            dayInHours = Constants.INCREDIBLY_LARGE_NUMBER;
        }
        else
        {
            dayInHours = Constants.RADIANS_PER_ROTATION / (Constants.SECONDS_PER_HOUR * angVelocity);
        }

        if ((dayInHours >= yearInHours) || stopped)
        {
            if (planet.getE() > 0.1)
            {
                spinResonanceFactor = (1.0 - planet.getE()) / (1.0 +
                        planet.getE());
                planet.setResonantPeriod(true);

                return (spinResonanceFactor * yearInHours);
            }
            else
            {
                return (yearInHours);
            }
        }

        return (dayInHours);
    }

    /**
     * Calculate an inclination.
     *
     * The orbital radius is expected in units of Astronomical Units (AU).
     * Inclination is returned in units of degrees.
     *
     * @param orbRadius The orbital radius in AU
     * @return The inclination of the orbit in degrees
     */
    public int getInclination(double orbRadius)
    {
        final int temp =
            (int) (Math.pow(orbRadius, 0.2) * utils.about(
                    Constants.EARTH_AXIAL_TILT, 0.4));

        return (temp % 360);
    }

    /**
     * Calculate a planetary escape velocity.
     *
     * This function implements the escape velocity calculation. Note that
     * it appears that Fogg's eq.15 is incorrect.
     * The mass is in units of solar mass, the radius in kilometers, and the
     * velocity returned is in cm/sec.
     *
     * @param mass The planetary mass in Solar masses
     * @param radius The radius of the planet in KM
     * @return The escape velocity in centimetres per second
     */
    public double getEscapeVelocity(double mass, double radius)
    {
        final double massInGrams = mass * Constants.SOLAR_MASS_IN_GRAMS;
        final double radiusInCm = radius * Constants.CM_PER_KM;

        return (Math.sqrt(
            (2.0 * Constants.GRAV_CONSTANT * massInGrams) / radiusInCm));
    }

    /*--------------------------------------------------------------------------*/
    /* This is Fogg's eq.16.  The molecular weight (usually assumed to be N2)   */
    /* is used as the basis of the Root Mean Square (RMS) velocity of the       */
    /* molecule or atom.  The velocity returned is in cm/sec.                   */
    /* Orbital radius is in A.U.(ie: in units of the earth's orbital radius).   */
    /*--------------------------------------------------------------------------*/
    double getRmsVelocity(double molecularWeight, double exosphericTemp)
    {
        return (Math.sqrt(
            (3.0 * Constants.MOLAR_GAS_CONST * exosphericTemp) / molecularWeight) * Constants.CM_PER_METER);
    }

    /**
     *  This function returns the smallest molecular weight retained by the
     *  body, which is useful for determining the atmosphere composition.
     *  Mass is in units of solar masses, and equatorial radius is in units of
     *  kilometers.
     *
     * @param mass The mass of the body in Solar masses
     * @param equatRadius The equatorial radius of the body in KM
     * @param exosphericTemp The temperateure of the body in degrees Kelvin
     * @return The smallest retained molecular weight
     */
    public double getMoleculeLimit(
        double mass, double equatRadius, double exosphericTemp)
    {
        final double escVelocity = getEscapeVelocity(mass, equatRadius);

        return ((3.0 * Constants.MOLAR_GAS_CONST * exosphericTemp) / (MathUtils.pow2(
                (escVelocity / Constants.GAS_RETENTION_THRESHOLD) / Constants.CM_PER_METER)));
    }

    /**
     * This function calculates the surface acceleration of a planet. The
     * mass is in units of solar masses, the radius in terms of km, and the
     * acceleration is returned in units of cmsec2.
     *
     * @param mass The mass in solar masses
     * @param radius The radius of the planet in kilometres
     * @return The surface acceleration of the planet in centimetres per second
     * squared
     */
    public double getAcceleration(double mass, double radius)
    {
        return ((Constants.GRAV_CONSTANT * (mass * Constants.SOLAR_MASS_IN_GRAMS)) / MathUtils.pow2(
                radius * Constants.CM_PER_KM));
    }

    /**
     * This function calculates the surface gravity of a planet. The
     * acceleration is in units of cm/sec2, and the gravity is returned in
     * units of Earth gravities.
     *
     * @param acceleration The acceleration in units of centimeters per second
     * squared
     * @return The surface gravity as a multiple of Earth gratity
     */
    public double getGravity(double acceleration)
    {
        return (acceleration / Constants.EARTH_ACCELERATION);
    }

    /**
     * This implements Fogg's eq.17.  The 'inventory' returned is unitless.
     *
     * @param mass The planetary mass in Solar masses
     * @param escapeVel The planetary escape velocity in centimeters per second
     * @param rmsVel The rms velocity
     * @param stellarMass The mass of the primary in Solar masses
     * @param zone The orbital zone of the planet
     * @param greenhouseEffect Indicates whether there is a greenhouse effect
     * @param accretedGas Indicates whether gas has been accreted
     * @return The volume inventory
     */
    public double getVolInventory(
        double mass, double escapeVel, double rmsVel, double stellarMass,
        int zone, boolean greenhouseEffect, boolean accretedGas)
    {
        double velocity_ratio;
        double proportionConst;
        double temp1;
        double temp2;
        double earthUnits;

        velocity_ratio = escapeVel / rmsVel;

        double volInv = 0.0;

        if (velocity_ratio >= Constants.GAS_RETENTION_THRESHOLD)
        {
            switch (zone)
            {
                case 1:
                    /* 100 -> 140 JLB */
                    proportionConst = 140000.0;

                    break;

                case 2:
                    proportionConst = 75000.0;

                    break;

                case 3:
                    proportionConst = 250.0;

                    break;

                default:
                    throw new AssertionError(
                        "Error: orbital zone not initialized correctly!");
            }

            earthUnits = mass * Constants.SUN_MASS_IN_EARTH_MASSES;
            temp1 = (proportionConst * earthUnits) / stellarMass;
            temp2 = utils.about(temp1, 0.2);
            temp2 = temp1;

            if (greenhouseEffect || accretedGas)
            {
                volInv = temp2;
            }
            else
            {
                /* 100 -> 140 JLB */
                volInv = temp2 / 140.0;
            }
        }

        return volInv;
    }

    /*--------------------------------------------------------------------------*/
    /* This implements Fogg's eq.18.  The pressure returned is in units of */
    /* millibars (mb).         The gravity is in units of Earth gravities, the radius */
    /* in units of kilometers. */
    /*                                                       */
    /* JLB: Aparently this assumed that earth pressure = 1000mb. I've added a  */
    /* fudge factor (EARTH_SURF_PRES_IN_MILLIBARS / 1000.) to correct for that */
    /*--------------------------------------------------------------------------*/
    double getPressure(
        double volatileGasInventory, double equatRadius, double gravity)
    {
        equatRadius = Constants.KM_EARTH_RADIUS / equatRadius;

        return ((volatileGasInventory * gravity * (Constants.EARTH_SURF_PRES_IN_MILLIBARS / 1000.)) / MathUtils.pow2(
                equatRadius));
    }

    /**
     * This function is Fogg's eq.22. Given the volatile gas inventory and
     * planetary radius of a planet (in Km), this function returns the
     * fraction of the planet covered with water.
     *
     * I have changed the function very slightly: the fraction of Earth's
     * surface covered by water is 71%, not 75% as Fogg used.
     *
     * @param volatileGasInventory The volatile gas inventory of the planet
     * @param planetRadius The planetary radius in kilometres
     * @return The fraction of the planet covered by water
     */
    public double getHydroFraction(
        double volatileGasInventory, double planetRadius)
    {
        double temp;

        temp = ((0.71 * volatileGasInventory) / 1000.0) * MathUtils.pow2(
                    Constants.KM_EARTH_RADIUS / planetRadius);

        if (temp >= 1.0)
        {
            return 1.0;
        }
        else
        {
            return temp;
        }
    }

    /**
     * Given the surface temperature of a planet (in Kelvin), this function
     * returns the fraction of cloud cover available.         This is Fogg's eq.23.
     * See Hart in "Icarus" (vol 33, pp23 - 39, 1978) for an explanation.
     * This equation is Hart's eq.3.
     *
     * I have modified it slightly using constants and relationships from
     * Glass's book "Introduction to Planetary Geology", p.46.
     * The 'CLOUD_COVERAGE_FACTOR' is the amount of surface area on Earth
     * covered by one Kg. of cloud.
     *
     * @param surfTemp The surface temperature in degrees Kelvin
     * @param smallestMWRetained The smallest molecular weight retained
     * @param equatRadius The equatorial radius of the planet in kilmetres
     * @param hydroFraction The water fraction of the planet
     * @return The cloud cover fraction for the planet
     */
    public double getCloudFraction(
        double surfTemp, double smallestMWRetained, double equatRadius,
        double hydroFraction)
    {
        double waterVaporInKg;
        double fraction;
        double surfArea;
        double hydroMass;

        if (smallestMWRetained > Constants.WATER_VAPOR)
        {
            return (0.0);
        }
        else
        {
            surfArea = 4.0 * Math.PI * MathUtils.pow2(equatRadius);
            hydroMass = hydroFraction * surfArea * Constants.EARTH_WATER_MASS_PER_AREA;
            waterVaporInKg = (0.00000001 * hydroMass) * Math.exp(
                        Constants.Q2_36 * (surfTemp -
                        Constants.EARTH_AVERAGE_KELVIN));
            fraction = (Constants.CLOUD_COVERAGE_FACTOR * waterVaporInKg) / surfArea;

            if (fraction >= 1.0)
            {
                return (1.0);
            }
            else
            {
                return (fraction);
            }
        }
    }

    /**
     * Given the surface temperature of a planet (in Kelvin), this function
     * returns the fraction of the planet's surface covered by ice.  This is
     * Fogg's eq.24.        See Hart[24] in Icarus vol.33, p.28 for an explanation.
     * I have changed a constant from 70 to 90 in order to bring it more in
     * line with the fraction of the Earth's surface covered with ice, which
     * is approximatly .016 (=1.6%).
     *
     * @param hydroFraction The water fraction of the planet
     * @param surfTemp The surface temperature of the planet in degrees kelvin
     * @return The ice fraction for the planet
     */
    public double getIceFraction(double hydroFraction, final double surfTemp)
    {
        double temp;
        double theSurfTemp = surfTemp;

        if (theSurfTemp > 328.0)
        {
            theSurfTemp = 328.0;
        }

        temp = Math.pow(((328.0 - theSurfTemp) / 90.0), 5.0);

        if (temp > (1.5 * hydroFraction))
        {
            temp = (1.5 * hydroFraction);
        }

        if (temp >= 1.0)
        {
            return (1.0);
        }
        else
        {
            return (temp);
        }
    }

    /**
     * This is Fogg's eq.19.  The ecosphere radius is given in AU, the orbital
     * radius in AU, and the temperature returned is in Kelvin.
     *
     * @param ecosphereRadius The radius of the ecosphere in AU
     * @param orbRadius The orbital radius in AU
     * @param albedo The albedo of the planet
     * @return The effective temperature in degrees Kelvin
     */
    public double getEffTemp(
        double ecosphereRadius, double orbRadius, double albedo)
    {
        return (Math.sqrt(ecosphereRadius / orbRadius) * MathUtils.fourthRoot(
                (1.0 - albedo) / (1.0 - Constants.EARTH_ALBEDO)) * Constants.EARTH_EFFECTIVE_TEMP);
    }

    /**
     * Get the surface temperature for a planet
     *
     * @param ecosphereRadius The radius of the ecosphere in AU
     * @param orbRadius The orbital radius of the planet in AU
     * @param albedo The albedo of the planet
     * @return The surface temperature of the planet
     */
    public double getEstTemp(
        double ecosphereRadius, double orbRadius, double albedo)
    {
        return (Math.sqrt(ecosphereRadius / orbRadius) * MathUtils.fourthRoot(
                (1.0 - albedo) / (1.0 - Constants.EARTH_ALBEDO)) * Constants.EARTH_AVERAGE_KELVIN);
    }

    /**
     * --------------------------------------------------------------------------
     * Old grnhouse:
     *        Note that if the orbital radius of the planet is greater than or equal
     *        to R_inner, 99% of it's volatiles are assumed to have been deposited in
     *        surface reservoirs (otherwise, it suffers from the greenhouse effect).
     * --------------------------------------------------------------------------
     *        if ((orbRadius < r_greenhouse) && (zone == 1))
     *
     * --------------------------------------------------------------------------
     *        The new definition is based on the inital surface temperature and what
     *        state water is in. If it's too hot, the water will never condense out
     *        of the atmosphere, rain down and form an ocean. The albedo used here
     *        was chosen so that the boundary is about the same as the old method
     *        Neither zone, nor r_greenhouse are used in this version        JLB
     * --------------------------------------------------------------------------
     *
     * @param rEcosphere The radius of the ecosphere in AU
     * @param orbRadius The orbital radius of the planet in AU
     * @return <pre>true</pre> if there is a greenhouse effect otherwise <pre>false</pre>
     */
    public boolean isGreenhouse(double rEcosphere, double orbRadius)
    {
        final double temp =
            getEffTemp(
                rEcosphere, orbRadius, Constants.GREENHOUSE_TRIGGER_ALBEDO);

        boolean greenhouse = false;

        if (temp > Constants.FREEZING_POINT_OF_WATER)
        {
            greenhouse = true;
        }

        return greenhouse;
    }

    /**
     * This is Fogg's eq.20, and is also Hart's eq.20 in his "Evolution of
     * Earth's Atmosphere" article.  The effective temperature given is in
     * units of Kelvin, as is the rise in temperature produced by the
     * greenhouse effect, which is returned.
     * I tuned this by changing a pow(x,.25) to pow(x,.4) to match Venus - JLB
     *
     * @param opticalDepth The optical depth of the planet (dimensionless)
     * @param effectiveTemp The effective temperature of the planet in degrees Kelvin
     * @param surfPressure  The surface pressure
     * @return The temperature rise produced by greenhouse effect in degrees Kelvin
     */
    public double getGreenRise(
        double opticalDepth, double effectiveTemp, double surfPressure)
    {
        double convectionFactor;

        convectionFactor = Constants.EARTH_CONVECTION_FACTOR * Math.pow(
                    surfPressure / Constants.EARTH_SURF_PRES_IN_MILLIBARS, 0.4);

        return (MathUtils.fourthRoot(1.0 + (0.75 * opticalDepth)) - 1.0) * effectiveTemp * convectionFactor;
    }

    /**
     * Calculate the albedo of a planet.
     *
     * The surface temperature passed in is in units of Kelvin.
     * The cloud adjustment is the fraction of cloud cover obscuring each
     * of the three major components of albedo that lie below the clouds.
     *
     * @param waterFraction The water fraction of the planet
     * @param cloudFraction The cloud fraction of the planet
     * @param iceFraction The ice fraction of the planet
     * @param surfPressure The surface atmospheric pressure of the planet
     * @return The albedo of the planet
     */
    public double getPlanetAlbedo(
        double waterFraction, double cloudFraction, double iceFraction,
        double surfPressure)
    {
        double rockFraction;
        double cloudAdjustment;
        double components;
        double cloudPart;
        double rockPart;
        double waterPart;
        double icePart;

        rockFraction = 1.0 - waterFraction - iceFraction;
        components = 0.0;

        if (waterFraction > 0.0)
        {
            components = components + 1.0;
        }

        if (iceFraction > 0.0)
        {
            components = components + 1.0;
        }

        if (rockFraction > 0.0)
        {
            components = components + 1.0;
        }

        cloudAdjustment = cloudFraction / components;

        if (rockFraction >= cloudAdjustment)
        {
            rockFraction = rockFraction - cloudAdjustment;
        }
        else
        {
            rockFraction = 0.0;
        }

        if (waterFraction > cloudAdjustment)
        {
            waterFraction = waterFraction - cloudAdjustment;
        }
        else
        {
            waterFraction = 0.0;
        }

        if (iceFraction > cloudAdjustment)
        {
            iceFraction = iceFraction - cloudAdjustment;
        }
        else
        {
            iceFraction = 0.0;
        }

        /* about(...,0.2); */
        cloudPart = cloudFraction * Constants.CLOUD_ALBEDO;

        if (surfPressure == 0.0)
        {
            /* about(...,0.3); */
            rockPart = rockFraction * Constants.ROCKY_AIRLESS_ALBEDO;
            /* about(...,0.4); */
            icePart = iceFraction * Constants.AIRLESS_ICE_ALBEDO;
            waterPart = 0;
        }
        else
        {
            rockPart = rockFraction * Constants.ROCKY_ALBEDO; /* about(...,0.1); */
            waterPart = waterFraction * Constants.WATER_ALBEDO; /* about(...,0.2); */
            icePart = iceFraction * Constants.ICE_ALBEDO; /* about(...,0.1); */
        }

        return (cloudPart + rockPart + waterPart + icePart);
    }

    /**
     * This function returns the dimensionless quantity of optical depth,
     * which is useful in determining the amount of greenhouse effect on a
     * planet.
     *
     * @param molecularWeight The retained molecular weight of the planet
     * @param surfPressure The surface pressure of the planet
     * @return The optical depth of the planet
     */
    public double getOpacity(double molecularWeight, double surfPressure)
    {
        double opticalDepth = 0.0;

        if ((molecularWeight >= 0.0) && (molecularWeight < 10.0))
        {
            opticalDepth = opticalDepth + 3.0;
        }

        if ((molecularWeight >= 10.0) && (molecularWeight < 20.0))
        {
            opticalDepth = opticalDepth + 2.34;
        }

        if ((molecularWeight >= 20.0) && (molecularWeight < 30.0))
        {
            opticalDepth = opticalDepth + 1.0;
        }

        if ((molecularWeight >= 30.0) && (molecularWeight < 45.0))
        {
            opticalDepth = opticalDepth + 0.15;
        }

        if ((molecularWeight >= 45.0) && (molecularWeight < 100.0))
        {
            opticalDepth = opticalDepth + 0.05;
        }

        if (surfPressure >= (70.0 * Constants.EARTH_SURF_PRES_IN_MILLIBARS))
        {
            opticalDepth = opticalDepth * 8.333;
        }
        else
        {
            if (surfPressure >= (50.0 * Constants.EARTH_SURF_PRES_IN_MILLIBARS))
            {
                opticalDepth = opticalDepth * 6.666;
            }
            else
            {
                if (
                    surfPressure >= (30.0 * Constants.EARTH_SURF_PRES_IN_MILLIBARS))
                {
                    opticalDepth = opticalDepth * 3.333;
                }
                else
                {
                    if (
                        surfPressure >= (10.0 * Constants.EARTH_SURF_PRES_IN_MILLIBARS))
                    {
                        opticalDepth = opticalDepth * 2.0;
                    }
                    else
                    {
                        if (
                            surfPressure >= (5.0 * Constants.EARTH_SURF_PRES_IN_MILLIBARS))
                        {
                            opticalDepth = opticalDepth * 1.5;
                        }
                    }
                }
            }
        }

        return opticalDepth;
    }

    /**
     *        Calculates the number of years it takes for 1/e of a gas to escape
     *        from a planet's atmosphere.
     *        Taken from Dole p. 34. He cites Jeans (1916) & Jones (1923)
     *
     *
     * @param molecularWeight The retained molecular weight of the planet
     * @param planet The planet to calculate for
     * @return The gas life of the planet in years
     */
    public double getGasLife(double molecularWeight, Planet planet)
    {
        final double v =
            getRmsVelocity(molecularWeight, planet.getExosphericTemperature());
        final double g =
            planet.getSurfaceGravity() * Constants.EARTH_ACCELERATION;
        final double r = (planet.getRadius() * Constants.CM_PER_KM);
        final double t =
            (MathUtils.pow3(v) / (2.0 * MathUtils.pow2(g) * r)) * Math.exp(
                    (3.0 * g * r) / MathUtils.pow2(v));

        double years =
            t / (Constants.SECONDS_PER_HOUR * 24.0 * Constants.DAYS_IN_A_YEAR);

        if (years > 2.0E10)
        {
            years = Constants.INCREDIBLY_LARGE_NUMBER;
        }

        return years;
    }

    /**
     * Get the minimum molecular weight for a planet
     *
     * @param planet The planet to calculate for
     * @return The minimum molecular weight for the planet
     */
    public double getMinMolecWeight(Planet planet)
    {
        final double mass = planet.getMass();
        final double radius = planet.getRadius();
        final double temp = planet.getExosphericTemperature();
        double target = 5.0E9;

        double guess1 = getMoleculeLimit(mass, radius, temp);
        double guess2 = guess1;

        double life = getGasLife(guess1, planet);

        int loops = 0;

        if (null != planet.getPrimary())
        {
            target = planet.getPrimary()
                    .getAge();
        }

        if (life > target)
        {
            while ((life > target) && (++loops < LOOP_LIMIT))
            {
                guess1 = guess1 / 2.0;
                life = getGasLife(guess1, planet);
            }
        }
        else
        {
            while ((life < target) && (++loops < LOOP_LIMIT))
            {
                guess2 = guess2 * 2.0;
                life = getGasLife(guess2, planet);
            }
        }

        loops = 0;

        while (((guess2 - guess1) > 0.1) && (++loops < LOOP_LIMIT))
        {
            final double guess3 = (guess1 + guess2) / 2.0;
            life = getGasLife(guess3, planet);

            if (life < target)
            {
                guess1 = guess3;
            }
            else
            {
                guess2 = guess3;
            }
        }

        life = getGasLife(guess2, planet);

        return (guess2);
    }

    /**
     *         The temperature calculated is in degrees Kelvin.
     *
     *         Quantities already known which are used in these calculations:
     *                 planet->molec_weight
     *                 planet->surf_pressure
     *                 R_ecosphere
     *                 planet->a
     *                 planet->volatile_gas_inventory
     *                 planet->radius
     *                 planet->boil_point
     *
     * @param planet the planet to be calculated for
     * @param first Whether this is the first iteration <pre>true</pre> if so
     * @param lastWater The last water fraction
     * @param lastClouds The last cloud fraction
     * @param lastIce The last ice fraction
     * @param lastTemp The last temperature calculated
     * @param lastAlbedo The last albedo calculated
     */
    public void calculateSurfaceTemp(
        Planet planet, boolean first, double lastWater, double lastClouds,
        double lastIce, double lastTemp, double lastAlbedo)
    {
        double effectiveTemp;
        double waterRaw;
        double cloudsRaw;
        double greenhouseTemp;
        boolean boilOff = false;

        if (first)
        {
            planet.setAlbedo(Constants.EARTH_ALBEDO);

            effectiveTemp = getEffTemp(
                    planet.getPrimary().getREcosphere(), planet.getA(),
                    planet.getAlbedo());
            greenhouseTemp = getGreenRise(
                    getOpacity(
                        planet.getMolecularWeight(), planet.getSurfacePressure()),
                    effectiveTemp, planet.getSurfacePressure());

            planet.setSurfaceTemperature(effectiveTemp + greenhouseTemp);

            setTempRange(planet);
        }

        if (
            planet.isGreenhouseEffect() &&
                (planet.getMaxTemperature() < planet.getBoilingPoint()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(
                    "Deluge: " + planet.getPrimary().getName() + " " +
                    planet.getNumber() + " max (" + planet.getMaxTemperature() +
                    ") < boil (" + planet.getBoilingPoint() + ")");
            }

            planet.setGreenhouseEffect(false);

            boolean accGas = false;

            if ((planet.getGasMass() / planet.getMass()) > 0.000001)
            {
                accGas = true;
            }

            planet.setVolatileGasInventory(
                getVolInventory(
                    planet.getMass(), planet.getEscapeVelocity(),
                    planet.getRmsVelocity(), planet.getPrimary().getMass(),
                    planet.getOrbitZone(), planet.isGreenhouseEffect(), accGas));

            planet.setSurfacePressure(
                getPressure(
                    planet.getVolatileGasInventory(), planet.getRadius(),
                    planet.getSurfaceGravity()));

            planet.setBoilingPoint(
                EnviroUtils.getBoilingPoint(planet.getSurfacePressure()));
        }

        waterRaw = getHydroFraction(
                planet.getVolatileGasInventory(), planet.getRadius());

        planet.setHydrosphere(waterRaw);

        cloudsRaw = getCloudFraction(
                planet.getSurfaceTemperature(), planet.getMolecularWeight(),
                planet.getRadius(), planet.getHydrosphere());

        planet.setCloudCover(cloudsRaw);

        planet.setIceCover(
            getIceFraction(
                planet.getHydrosphere(), planet.getSurfaceTemperature()));

        if ((planet.isGreenhouseEffect()) &&
                (planet.getSurfacePressure() > 0.0))
        {
            planet.setCloudCover(1.0);
        }

        if (
            (planet.getMaxTemperature() >= planet.getBoilingPoint()) &&
                (!first))
        {
            planet.setHydrosphere(0.0);
            boilOff = true;

            if (planet.getMolecularWeight() > Constants.WATER_VAPOR)
            {
                planet.setCloudCover(0.0);
            }
            else
            {
                planet.setCloudCover(1.0);
            }
        }

        if (
            planet.getSurfaceTemperature() < (Constants.FREEZING_POINT_OF_WATER -
                3.0))
        {
            planet.setHydrosphere(0.0);
        }

        planet.setAlbedo(
            getPlanetAlbedo(
                planet.getHydrosphere(), planet.getCloudCover(),
                planet.getIceCover(), planet.getSurfacePressure()));

        effectiveTemp = getEffTemp(
                planet.getPrimary().getREcosphere(), planet.getA(),
                planet.getAlbedo());

        greenhouseTemp = getGreenRise(
                getOpacity(
                    planet.getMolecularWeight(), planet.getSurfacePressure()),
                effectiveTemp, planet.getSurfacePressure());

        planet.setSurfaceTemperature(effectiveTemp + greenhouseTemp);

        if (!first)
        {
            if (!boilOff)
            {
                planet.setHydrosphere(
                    planet.getHydrosphere() + ((lastWater * 2) / 3));
            }

            planet.setCloudCover(
                planet.getCloudCover() + ((lastClouds * 2) / 3));
            planet.setIceCover(planet.getIceCover() + ((lastIce * 2) / 3));
            planet.setAlbedo(planet.getAlbedo() + ((lastAlbedo * 2) / 3));
            planet.setSurfaceTemperature(
                planet.getSurfaceTemperature() + ((lastTemp * 2) / 3));
        }

        setTempRange(planet);

        // TODO: consider reinstating this log message

        //if (flag_verbose & 0x0020)
        //    fprintf(stderr, "%5.1Lf AU: %5.1Lf = %5.1Lf ef + %5.1Lf gh%c "
        //            "(W: %4.2Lf (%4.2Lf) C: %4.2Lf (%4.2Lf) I: %4.2Lf A: (%4.2Lf))\n",
        //            planet->a,
        //            planet->surf_temp - FREEZING_POINT_OF_WATER,
        //            effectiveTemp - FREEZING_POINT_OF_WATER,
        //            greenhouseTemp,
        //            (planet->greenhouse_effect) ? '*' :' ',
        //            planet->hydrosphere, waterRaw,
        //            planet->cloud_cover, cloudsRaw,
        //            planet->ice_cover,
        //            planet->albedo);
    }

    /**
     * Carry out an iteration to work out the surface temperature of a planet.
     * On return the corresponding values will be set in the planet record.
     *
     * @param planet The planet to calculate the temperature for
     */
    public void iterateSurfaceTemp(Planet planet)
    {
        int count = 0;
        final double initialTemp =
            getEstTemp(
                planet.getPrimary().getREcosphere(), planet.getA(),
                planet.getAlbedo());

        final double h2Life = getGasLife(Constants.MOL_HYDROGEN, planet);
        final double h2oLife = getGasLife(Constants.WATER_VAPOR, planet);
        final double n2Life = getGasLife(Constants.MOL_NITROGEN, planet);
        final double nLife = getGasLife(Constants.ATOMIC_NITROGEN, planet);

        if (log.isDebugEnabled())
        {
            log.debug(
                "Gas lifetimes: H2: " + h2Life + " H20: " + h2oLife + " N: " +
                nLife + " N2: " + n2Life);
        }

        calculateSurfaceTemp(planet, true, 0, 0, 0, 0, 0);

        for (count = 0; count <= 25; ++count)
        {
            final double lastWater = planet.getHydrosphere();
            final double lastClouds = planet.getCloudCover();
            final double lastIce = planet.getIceCover();
            final double lastTemp = planet.getSurfaceTemperature();
            final double lastAlbedo = planet.getAlbedo();

            calculateSurfaceTemp(
                planet, false, lastWater, lastClouds, lastIce, lastTemp,
                lastAlbedo);

            if (Math.abs(planet.getSurfaceTemperature() - lastTemp) < 0.25)
            {
                break;
            }
        }

        planet.setGreenhouseRise(planet.getSurfaceTemperature() - initialTemp);
    }

    /**
     * Inspired partial pressure, taking into account humidification of the
     * air in the nasal passage and throat This formula is on Dole's p. 14
     *
     * @param surfPressure The surface pressure of the planet
     * @param gasPressure The gas pressure of the planet
     * @return The inspired partial pressure of the planet
     */
    public double getInspiredPartialPressure(
        double surfPressure, double gasPressure)
    {
        final double pH2O = (Constants.H20_ASSUMED_PRESSURE);
        final double fraction = gasPressure / surfPressure;

        return (surfPressure - pH2O) * fraction;
    }

    /**
     *        This function uses figures on the maximum inspired partial pressures
     *  of Oxygen, other atmospheric and traces gases as laid out on pages 15,
     *  16 and 18 of Dole's Habitable Planets for Man to derive breathability
     *  of the planet's atmosphere.                                       JLB
     *
     * @param planet The planet to calculate breathability for
     * @return An evaluation of atmosphere breathability of this planet
     */
    public Breathability getBreathability(Planet planet)
    {
        boolean oxygenOk = false;

        if (planet.getGases() == null)
        {
            return Breathability.NONE;
        }

        for (Gas g : planet.getGases())
        {
            final double ipp =
                getInspiredPartialPressure(
                    planet.getSurfacePressure(), g.getSurfacePressure());

            final ChemTable gasData = g.getElement();

            if (ipp > gasData.getMaxIpp())
            {
                return Breathability.POISONOUS;
            }

            if (gasData.getNumber() == Constants.AN_O)
            {
                oxygenOk = ((ipp >= Constants.MIN_O2_IPP) &&
                        (ipp <= Constants.MAX_O2_IPP));
            }
        }

        if (oxygenOk)
        {
            return Breathability.BREATHABLE;
        }
        else
        {
            return Breathability.UNBREATHABLE;
        }
    }

    /* function for 'soft limiting' temperatures */
    private double lim(double x)
    {
        return x / Math.sqrt(Math.sqrt(1 + (x * x * x * x)));
    }

    private double soft(double v, double max, double min)
    {
        final double dv = v - min;
        final double dm = max - min;

        return ((lim(((2 * dv) / dm) - 1) + 1) / 2 * dm) + min;
    }

    private void setTempRange(Planet planet)
    {
        final double pressmod =
            1 / Math.sqrt(1 + ((20 * planet.getSurfacePressure()) / 1000.0));
        final double ppmod =
            1 / Math.sqrt(10 + ((5 * planet.getSurfacePressure()) / 1000.0));
        final double tiltmod =
            Math.abs(
                Math.cos((planet.getAxialTilt() * Math.PI) / 180) * Math.pow(
                        1 + planet.getE(), 2));

        final double daymod = 1 / ((200 / planet.getDay()) + 1);
        final double mh = Math.pow(1 + daymod, pressmod);
        final double ml = Math.pow(1 - daymod, pressmod);
        final double hi = mh * planet.getSurfaceTemperature();
        double lo = ml * planet.getSurfaceTemperature();
        final double sh = hi +
            Math.pow((100 + hi) * tiltmod, Math.sqrt(ppmod));
        double wl = lo - Math.pow((150 + lo) * tiltmod, Math.sqrt(ppmod));
        final double max =
            planet.getSurfaceTemperature() +
            (Math.sqrt(planet.getSurfaceTemperature()) * 10);
        final double min =
            planet.getSurfaceTemperature() / Math.sqrt(planet.getDay() + 24);

        if (lo < min)
        {
            lo = min;
        }

        if (wl < 0)
        {
            wl = 0;
        }

        planet.setHighTemperature(soft(hi, max, min));
        planet.setLowTemperature(soft(lo, max, min));
        planet.setMaxTemperature(soft(sh, max, min));
        planet.setMinTemperature(soft(wl, max, min));
    }
}
