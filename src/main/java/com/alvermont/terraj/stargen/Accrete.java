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
 * Accrete.java
 *
 * Created on December 21, 2005, 10:48 AM
 *
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Planetary accretions
 *
 * @author martin
 * @version $Id: Accrete.java,v 1.17 2006/07/06 06:58:33 martin Exp $
 */
public class Accrete
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(Accrete.class);

    // RequireThis OFF: log

    /** Indicates whether there is any dust left */
    protected boolean dustLeft;

    /** Inner edge of the system */
    private double rInner;

    /** Outer edge of the system */
    private double rOuter;

    /** The current amount of mass */
    private double reducedMass;

    /** The density of the dust cloud */
    private double dustDensity;

    /** The eccentricity of the dust cloud */
    private double cloudEccentricity;

    /** The first planet in the list of those generated. C list idiom */
    private Planet planetHead;

    /** The first dust record in the list. C list idiom */
    private DustRecord dustHead;

    /** The math utilities object */
    private MathUtils utils;

    /** The environment object */
    private Enviro enviro;

    /** Creates a new instance of Accrete */
    public Accrete()
    {
        this.utils = new MathUtils();
        this.enviro = new Enviro(utils);
    }

    /**
     * Creates a new instance of the Accrete class
     *
     * @param utils The math utility object to use (used for for random numbers)
     */
    public Accrete(MathUtils utils)
    {
        this.utils = utils;
        this.enviro = new Enviro(utils);
    }

    /**
     * Sets the seed of the random number generator used by this object
     *
     * @param seed The new seed to be used by the random number generator
     */
    public void setSeed(long seed)
    {
        utils.setSeed(seed);
    }

    /**
     * Set up the variables ready for an accretion run. Creates an initial
     * dust band from the specified values.
     *
     * @param innerDustLimit The inner dust limit of the band
     * @param outerDustLimit The outer dust limit of the band
     */
    public void setInitialConditions(
        double innerDustLimit, double outerDustLimit)
    {
        final DustRecord dust = new DustRecord(innerDustLimit, outerDustLimit);

        dust.setDustPresent(true);
        dust.setGasPresent(true);

        setDustHead(dust);
        setPlanetHead(null);

        dustLeft = true;
        cloudEccentricity = 0.2;
    }

    /**
     * Gets the radius of the inner effect due to gravity of a mass
     *
     * @param a The distance from the sun of the mass
     * @param e The orbital eccentricity of the mass
     * @param mass The mass value
     * @return The inner radius of the gravitational effect
     */
    public double getInnerEffectLimit(double a, double e, double mass)
    {
        return ((a * (1.0 - e) * (1.0 - mass)) / (1.0 + cloudEccentricity));
    }

    /**
     * Gets the radius of the outer effect due to gravity of a mass
     *
     * @param a The distance from the sun of the mass
     * @param e The orbital eccentricity of the mass
     * @param mass The mass value
     * @return The outer radius of the gravitational effect
     */
    public double getOuterEffectLimit(double a, double e, double mass)
    {
        return ((a * (1.0 + e) * (1.0 + mass)) / (1.0 - cloudEccentricity));
    }

    /**
     * Indicates whether there is any dust available for accretion in the
     * specified range.
     *
     * @param insideRange The inner radius to be tested
     * @param outsideRange The outer radius to be tested
     * @return <pre>true</pre> if there is dust available in the specified range
     * otherwise <pre>false</pre>
     */
    public boolean isDustAvailable(double insideRange, double outsideRange)
    {
        boolean dustHere = false;

        DustRecord currentDust = getDustHead();

        while (
            (currentDust != null) &&
                (currentDust.getOuterEdge() < insideRange))
        {
            currentDust = currentDust.getNextDust();
        }

        if (currentDust != null)
        {
            dustHere = currentDust.isDustPresent();
        }

        while (
            (currentDust != null) &&
                (currentDust.getInnerEdge() < outsideRange))
        {
            dustHere = dustHere || currentDust.isDustPresent();
            currentDust = currentDust.getNextDust();
        }

        return (dustHere);
    }

    /**
     * Processes the current dust lanes, splitting and merging them as
     * necessary.
     *
     * @param min The minimum range from the star to update dust lanes for
     * @param max The maximum range from the star to update dust lanes for
     * @param mass The current planetary mass
     * @param critMass The critical mass value
     * @param bodyInnerBound The inner radius of the effect of the body
     * @param bodyOuterBound The outer radius of the effect of the body
     */
    protected void updateDustLanes(
        double min, double max, double mass, double critMass,
        double bodyInnerBound, double bodyOuterBound)
    {
        //if (log.isTraceEnabled())
        //    log.trace("Entered updateDustLanes");
        boolean gas = false;

        DustRecord node1;
        DustRecord node2;
        DustRecord node3;

        dustLeft = false;

        if (mass > critMass)
        {
            gas = false;
        }
        else
        {
            gas = true;
        }

        node1 = getDustHead();

        while (node1 != null)
        {
            if (((node1.getInnerEdge() < min) && (node1.getOuterEdge() > max)))
            {
                node2 = new DustRecord(min, max);

                if (node1.isGasPresent())
                {
                    node2.setGasPresent(gas);
                }
                else
                {
                    node2.setGasPresent(false);
                }

                node2.setDustPresent(false);

                node3 = new DustRecord(max, node1.getOuterEdge());
                node3.setGasPresent(node1.isGasPresent());
                node3.setDustPresent(node1.isDustPresent());

                node3.setNextDust(node1.getNextDust());
                node1.setNextDust(node2);
                node2.setNextDust(node3);

                node1.setOuterEdge(min);

                node1 = node3.getNextDust();
            }
            else
            {
                if (
                    ((node1.getInnerEdge() < max) &&
                        (node1.getOuterEdge() > max)))
                {
                    node2 = new DustRecord();

                    node2.setNextDust(node1.getNextDust());

                    node2.setDustPresent(node1.isDustPresent());
                    node2.setGasPresent((node1.isGasPresent()));
                    node2.setOuterEdge(node1.getOuterEdge());
                    node2.setInnerEdge(max);

                    node1.setNextDust(node2);
                    node1.setOuterEdge(max);

                    if (node1.isGasPresent())
                    {
                        node1.setGasPresent(gas);
                    }
                    else
                    {
                        node1.setGasPresent(false);
                    }

                    node1.setDustPresent(false);

                    node1 = node2.getNextDust();
                }
                else
                {
                    if (
                        ((node1.getInnerEdge() < min) &&
                            (node1.getOuterEdge() > min)))
                    {
                        node2 = new DustRecord();

                        node2.setNextDust(node1.getNextDust());

                        node2.setDustPresent(false);

                        if (node1.isGasPresent())
                        {
                            node2.setGasPresent(gas);
                        }
                        else
                        {
                            node2.setGasPresent(false);
                        }

                        node2.setOuterEdge(node1.getOuterEdge());
                        node2.setInnerEdge(min);

                        node1.setNextDust(node2);

                        node1.setOuterEdge(min);

                        node1 = node2.getNextDust();
                    }
                    else
                    {
                        if (
                            ((node1.getInnerEdge() >= min) &&
                                (node1.getOuterEdge() <= max)))
                        {
                            if (node1.isGasPresent())
                            {
                                node1.setGasPresent(gas);
                            }

                            node1.setDustPresent(false);

                            node1 = node1.getNextDust();
                        }
                        else
                        {
                            if (
                                ((node1.getOuterEdge() < min) ||
                                    (node1.getInnerEdge() > max)))
                            {
                                node1 = node1.getNextDust();
                            }
                        }
                    }
                }
            }
        }

        node1 = getDustHead();

        while (node1 != null)
        {
            if (
                ((node1.isDustPresent()) &&
                    ((node1.getOuterEdge() >= bodyInnerBound) &&
                    (node1.getInnerEdge() <= bodyOuterBound))))
            {
                dustLeft = true;
            }

            node2 = node1.getNextDust();

            if (node2 != null)
            {
                if (
                    (node1.isDustPresent() == node2.isDustPresent()) &&
                        (node1.isGasPresent() == node2.isGasPresent()))
                {
                    node1.setOuterEdge(node2.getOuterEdge());

                    node1.setNextDust(node2.getNextDust());
                }
            }

            node1 = node1.getNextDust();
        }

        //if (log.isTraceEnabled())
        //    log.trace("Left updateDustLanes");
    }

    /**
     * Does dust accretion calculations
     *
     * @param lastMass The current mass of the planet
     * @param newstuff Records the current accretion
     * @param a The distance of the body from the sun (units of AU)
     * @param e The orbital eccentricity
     * @param critMass The critical mass value
     * @param dustBand The current dust record for accretion
     * @return The amount of accreted dust
     */
    protected double collectDust(
        double lastMass, NewRecord newstuff, double a, double e, double critMass,
        DustRecord dustBand)
    {
        //if (log.isTraceEnabled())
        //    log.trace("Entered collectDust");
        double massDensity;
        double temp1;
        double temp2;
        double temp;
        double tempDensity;
        double bandwidth;
        double width;
        double volume;
        double gasDensity = 0.0;
        double nextMass;

        temp = lastMass / (1.0 + lastMass);
        reducedMass = Math.pow(temp, (1.0 / 4.0));
        rInner = getInnerEffectLimit(a, e, reducedMass);
        rOuter = getOuterEffectLimit(a, e, reducedMass);

        if ((rInner < 0.0))
        {
            rInner = 0.0;
        }

        if (dustBand == null)
        {
            //if (log.isTraceEnabled())
            //    log.trace("Left collectDust");
            return 0.0;
        }

        if (!dustBand.isDustPresent())
        {
            tempDensity = 0.0;
        }
        else
        {
            tempDensity = dustDensity;
        }

        if ((lastMass < critMass) || (!dustBand.isGasPresent()))
        {
            massDensity = tempDensity;
        }
        else
        {
            massDensity = (Constants.K * tempDensity) / (1.0 +
                    (Math.sqrt(critMass / lastMass) * (Constants.K - 1.0)));
            gasDensity = massDensity - tempDensity;
        }

        if (
            ((dustBand.getOuterEdge() <= rInner) ||
                (dustBand.getInnerEdge() >= rOuter)))
        {
            return (collectDust(
                lastMass, newstuff, a, e, critMass, dustBand.getNextDust()));
        }
        else
        {
            bandwidth = (rOuter - rInner);

            temp1 = rOuter - dustBand.getOuterEdge();

            if (temp1 < 0.0)
            {
                temp1 = 0.0;
            }

            width = bandwidth - temp1;

            temp2 = dustBand.getInnerEdge() - rInner;

            if (temp2 < 0.0)
            {
                temp2 = 0.0;
            }

            width = width - temp2;

            temp = 4.0 * Math.PI * Math.pow(a, 2.0) * reducedMass * (1.0 -
                    ((e * (temp1 - temp2)) / bandwidth));
            volume = temp * width;

            final double newMass = volume * massDensity;
            newstuff.setNewGas(volume * gasDensity);
            newstuff.setNewDust(newMass - newstuff.getNewGas());

            final NewRecord nextstuff = new NewRecord();

            nextMass = collectDust(
                    lastMass, nextstuff, a, e, critMass, dustBand.getNextDust());

            newstuff.setNewGas(newstuff.getNewGas() + nextstuff.getNewGas());
            newstuff.setNewDust(newstuff.getNewDust() + nextstuff.getNewDust());

            //if (log.isTraceEnabled())
            //    log.trace("Left collectDust");
            return (newMass + nextMass);
        }
    }

    /**
     * Calculates the critical limit for a planet.
     *
     * @param orbRadius The radius of the orbit in AU
     * @param eccentricity The orbital eccecntricity
     * @param stellLuminosityRatio The star luminosity (Sol = 1.0)
     * @return The mass at which the planet begins to accrete gas as well as
     * dust (in units of Solar mass).
     */
    public double getCriticalLimit(
        double orbRadius, double eccentricity, double stellLuminosityRatio)
    {
        double temp;
        double perihelionDist;

        perihelionDist = (orbRadius - (orbRadius * eccentricity));
        temp = perihelionDist * Math.sqrt(stellLuminosityRatio);

        return (Constants.B * Math.pow(temp, -0.75));
    }

    /**
     * Accrete dust together to form large bodies
     *
     * @param newstuff A record detailing the current accreted mass
     * @param a The distance from the sun of the body
     * @param e The eccentricity of the body
     * @param critMass The critical mass value
     * @param bodyInnerBound The inner bound for the planet
     * @param bodyOuterBound The outer bound for the planet
     */
    public void accreteDust(
        NewRecord newstuff, double a, double e, double critMass,
        double bodyInnerBound, double bodyOuterBound)
    {
        double newMass = newstuff.getSeedMass();
        double tempMass;

        do
        {
            tempMass = newMass;
            newMass = collectDust(
                    newMass, newstuff, a, e, critMass, this.getDustHead());
        }
        while (!(((newMass - tempMass) < (0.0001 * tempMass))));

        newstuff.setSeedMass(newstuff.getSeedMass() + newMass);

        updateDustLanes(
            rInner, rOuter, newstuff.getSeedMass(), critMass, bodyInnerBound,
            bodyOuterBound);
    }

    /**
     * Coalesce plantesimals. Those with orbits that collide form a new planet.
     * The remains can also form a planet.
     *
     * @param a The distance from the star that we are considering
     * @param e The eccentricity of orbit that we are considering
     * @param mass The current mass being considered
     * @param critMass The critical mass value
     * @param dustMass The amount of mass in the form of dust
     * @param gasMass The amount of mass as gas
     * @param stellLuminosityRatio The luminosity of the star (sol = 1.0)
     * @param bodyInnerBound The inner bound for the planetary body
     * @param bodyOuterBound The outer bound for the planetary body
     */
    public void coalescePlanetesimals(
        double a, double e, double mass, double critMass, double dustMass,
        double gasMass, double stellLuminosityRatio, double bodyInnerBound,
        double bodyOuterBound)
    {
        Planet nextPlanet = null;
        Planet prevPlanet = null;

        boolean finished = false;

        double temp;
        double diff;
        double dist1;
        double dist2;

        double newE = e;

        // First we try to find an existing planet with an over-lapping orbit.

        //if (planets.size() > 0)
        //{
        //    thePlanet = planets.get(0);
        //    planetHead = thePlanet;
        //}
        for (
            Planet aPlanet = getPlanetHead(); aPlanet != null;
                aPlanet = aPlanet.getNextPlanet())
        {
            diff = aPlanet.getA() - a;

            if ((diff > 0.0))
            {
                dist1 = (a * (1.0 + newE) * (1.0 + reducedMass)) - a;
                /* x aphelion         */
                reducedMass = Math.pow(
                        (aPlanet.getMass() / (1.0 + aPlanet.getMass())),
                        (1.0 / 4.0));
                dist2 = aPlanet.getA() -
                    (aPlanet.getA() * (1.0 - aPlanet.getE()) * (1.0 -
                        reducedMass));
            }
            else
            {
                dist1 = a - (a * (1.0 - newE) * (1.0 - reducedMass));
                /* x perihelion */
                reducedMass = Math.pow(
                        (aPlanet.getMass() / (1.0 + aPlanet.getMass())),
                        (1.0 / 4.0));
                dist2 = (aPlanet.getA() * (1.0 + aPlanet.getE()) * (1.0 +
                        reducedMass)) - aPlanet.getA();
            }

            if (
                ((Math.abs(diff) <= Math.abs(dist1)) ||
                    (Math.abs(diff) <= Math.abs(dist2))))
            {
                final NewRecord newstuff = new NewRecord();

                newstuff.setNewDust(0);
                newstuff.setNewGas(0);

                final double newA =
                    (aPlanet.getMass() + mass) / ((aPlanet.getMass() / aPlanet.getA()) +
                        (mass / a));

                if (log.isDebugEnabled())
                {
                    log.debug(
                        "Collision between two plantesimals! " + a + " AU (" +
                        (mass * Constants.SUN_MASS_IN_EARTH_MASSES) + ") + " +
                        aPlanet.getA() + " AU ( " +
                        (aPlanet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES) +
                        ") -> " + newA + " AU");
                }

                temp = aPlanet.getMass() * Math.sqrt(aPlanet.getA()) * Math.sqrt(
                            1.0 - Math.pow(aPlanet.getE(), 2.0));
                temp = temp +
                    (mass * Math.sqrt(a) * Math.sqrt(
                            Math.sqrt(1.0 - Math.pow(newE, 2.0))));
                temp = temp / ((aPlanet.getMass() + mass) * Math.sqrt(newA));
                temp = 1.0 - Math.pow(temp, 2.0);

                if (((temp < 0.0) || (temp >= 1.0)))
                {
                    temp = 0.0;
                }

                newE = Math.sqrt(temp);

                temp = aPlanet.getMass() + mass;

                newstuff.setSeedMass(temp);

                accreteDust(
                    newstuff, newA, newE, stellLuminosityRatio, bodyInnerBound,
                    bodyOuterBound);

                temp = newstuff.getSeedMass();

                aPlanet.setA(newA);
                aPlanet.setE(newE);
                aPlanet.setMass(temp);

                aPlanet.setDustMass(
                    aPlanet.getDustMass() + dustMass + newstuff.getNewDust());
                aPlanet.setGasMass(
                    aPlanet.getGasMass() + gasMass + newstuff.getNewGas());

                if (temp >= critMass)
                {
                    aPlanet.setGasGiant(true);
                }

                while (
                    (aPlanet.getNextPlanet() != null) &&
                        (aPlanet.getNextPlanet()
                        .getA() < newA))
                {
                    nextPlanet = aPlanet.getNextPlanet();

                    if (aPlanet == this.getPlanetHead())
                    {
                        this.setPlanetHead(nextPlanet);
                    }
                    else
                    {
                        prevPlanet.setNextPlanet(nextPlanet);
                    }

                    aPlanet.setNextPlanet(nextPlanet.getNextPlanet());
                    nextPlanet.setNextPlanet(aPlanet);
                    prevPlanet = nextPlanet;
                }

                finished = true;

                break;
            }
            else
            {
                prevPlanet = aPlanet;
            }
        }

        if (!(finished))
        {
            // Planetesimals didn't collide. Make it a planet.
            log.debug("Creating new planet a=" + a + " ,e= " + newE);

            final Planet thePlanet = new BasicPlanet();

            thePlanet.setType(PlanetType.UNKNOWN);
            thePlanet.setA(a);
            thePlanet.setE(newE);

            thePlanet.setMass(mass);
            thePlanet.setDustMass(dustMass);
            thePlanet.setGasMass(gasMass);

            if (mass >= critMass)
            {
                thePlanet.setGasGiant(true);
            }
            else
            {
                thePlanet.setGasGiant(false);
            }

            if ((this.getPlanetHead() == null))
            {
                this.setPlanetHead(thePlanet);
                thePlanet.setNextPlanet(null);
            }
            else if ((a < this.getPlanetHead()
                    .getA()))
            {
                thePlanet.setNextPlanet(getPlanetHead());
                this.setPlanetHead(thePlanet);
            }
            else if ((this.getPlanetHead()
                    .getNextPlanet() == null))
            {
                getPlanetHead()
                    .setNextPlanet(thePlanet);
                thePlanet.setNextPlanet(null);
            }
            else
            {
                nextPlanet = this.getPlanetHead();

                while (((nextPlanet != null) && (nextPlanet.getA() < a)))
                {
                    prevPlanet = nextPlanet;
                    nextPlanet = nextPlanet.getNextPlanet();
                }

                thePlanet.setNextPlanet(nextPlanet);
                prevPlanet.setNextPlanet(thePlanet);
            }
        }

        //if (log.isTraceEnabled())
        //    log.trace("Left coalescePlantesimals");
    }

    /**
     * Handle the distribution of planetary masses.
     *
     * @param stellMassRatio The amount of mass available (sol = 1.0)
     * @param stellLuminosityRatio The luminosity of the star (sol = 1.0)
     * @param innerDust The inner dust limit
     * @param outerDust The outer dust limit
     * @param outerPlanetLimit The limit for the outermost planet
     * @param dustDensityCoeff The coefficient of dust density
     * @param seedSystem If non null then a list of planets to be used as seeds
     * @return The first planet that has been produced in the system
     */
    public Planet distPlanetaryMasses(
        double stellMassRatio, double stellLuminosityRatio, double innerDust,
        double outerDust, double outerPlanetLimit, double dustDensityCoeff,
        Planet seedSystem)
    {
        double a;
        double e;
        double critMass;
        double planetInnerBound;
        double planetOuterBound;
        Planet seeds = seedSystem;

        setInitialConditions(innerDust, outerDust);
        planetInnerBound = AccreteUtils.getNearestPlanet(stellMassRatio);

        if (outerPlanetLimit == 0)
        {
            planetOuterBound = AccreteUtils.getFarthestPlanet(stellMassRatio);
        }
        else
        {
            planetOuterBound = outerPlanetLimit;
        }

        while (dustLeft)
        {
            if (seeds != null)
            {
                a = seeds.getA();
                e = seeds.getE();

                seeds = seeds.getNextPlanet();
            }
            else
            {
                a = utils.randomNumber(planetInnerBound, planetOuterBound);
                e = utils.randomEccentricity();
            }

            if (a < 0)
            {
                throw new AssertionError(
                    "Got a negative planetary distance: " + a + ", " +
                    planetInnerBound + ", " + planetOuterBound);
            }

            final NewRecord newstuff = new NewRecord();

            newstuff.setSeedMass(Constants.PROTOPLANET_MASS);
            newstuff.setNewDust(0);
            newstuff.setNewGas(0);

//            if (log.isDebugEnabled())
//            {
//                log.debug("Checking " + a + " AU");
//            }

            if (
                isDustAvailable(
                        getInnerEffectLimit(a, e, newstuff.getSeedMass()),
                        getOuterEffectLimit(a, e, newstuff.getSeedMass())))
            {
                if (log.isDebugEnabled())
                {
                    //log.debug("Injecting protoplanet at " + a + "AU.");
                }

                dustDensity = dustDensityCoeff * Math.sqrt(stellMassRatio) * Math.exp(
                            -Constants.ALPHA * Math.pow(a, (1.0 / Constants.N)));

                critMass = getCriticalLimit(a, e, stellLuminosityRatio);

                accreteDust(
                    newstuff, a, e, critMass, planetInnerBound, planetOuterBound);

                newstuff.setNewDust(
                    newstuff.getNewDust() + Constants.PROTOPLANET_MASS);

                if (
                    (newstuff.getSeedMass() != 0.0) &&
                        (newstuff.getSeedMass() != Constants.PROTOPLANET_MASS))
                {
                    coalescePlanetesimals(
                        a, e, newstuff.getSeedMass(), critMass,
                        newstuff.getNewDust(), newstuff.getNewGas(),
                        stellLuminosityRatio, planetInnerBound, planetOuterBound);
                }
                else if (log.isDebugEnabled())
                {
                    //log.debug(".. failed due to large neighbor.");
                }
            }
            else if (log.isDebugEnabled())
            {
                //log.debug(".. failed.");
            }
        }

        return (getPlanetHead());
    }

    Planet distMoonMasses(
        double planetaryMass, double stellLuminosityRatio, double planetA,
        double planetE, double innerDust, double outerDust,
        double dustDensityCoeff, double outerPlanetLimit)
    {
        double a;
        double e;

        final double critMass =
            getCriticalLimit(planetA, planetE, stellLuminosityRatio);

        setInitialConditions(innerDust, outerDust);

        final double moonInnerBound =
            AccreteUtils.getNearestPlanet(planetaryMass);
        final double moonOuterBound = outerPlanetLimit;

        while (dustLeft)
        {
            a = utils.randomNumber(moonInnerBound, moonOuterBound);
            e = utils.randomEccentricity();

            final NewRecord newstuff = new NewRecord();

            newstuff.setSeedMass(Constants.PROTOPLANET_MASS);
            newstuff.setNewDust(0);
            newstuff.setNewGas(0);

            //if (log.isDebugEnabled())
            //    log.debug("Moons Checking " + a + " AU");
            if (
                isDustAvailable(
                        getInnerEffectLimit(a, e, newstuff.getSeedMass()),
                        getOuterEffectLimit(a, e, newstuff.getSeedMass())))
            {
                //if (log.isDebugEnabled())
                //    log.debug("Injecting protoplanet at " + a + " AU.");
                dustDensity = dustDensityCoeff * Math.sqrt(planetaryMass) * Math.exp(
                            -Constants.ALPHA * Math.pow(a, (1.0 / Constants.N)));

                accreteDust(
                    newstuff, a, e, critMass, moonInnerBound, moonOuterBound);

                newstuff.setNewDust(
                    newstuff.getNewDust() + Constants.PROTOPLANET_MASS);

                if (
                    (newstuff.getSeedMass() != 0.0) &&
                        (newstuff.getSeedMass() != Constants.PROTOPLANET_MASS))
                {
                    coalescePlanetesimals(
                        a, e, newstuff.getSeedMass(), critMass,
                        newstuff.getNewDust(), newstuff.getNewGas(),
                        stellLuminosityRatio, moonInnerBound, moonOuterBound);
                }
            }
        }

        return (getPlanetHead());
    }

    /**
     * Get a list of the planets in this solar system
     *
     * @return A list of planetary records
     */
    public List<Planet> getPlanets()
    {
        final List<Planet> planets = new ArrayList<Planet>();

        for (
            Planet planet = getPlanetHead(); planet != null;
                planet = planet.getNextPlanet())
        {
            planets.add(planet);
        }

        return Collections.unmodifiableList(planets);
    }

    /**
     * This replaces the pointer accesses used by the original C code to
     * get in/out parameters
     */
    private class NewRecord
    {
        private double seedMass;
        private double newDust;
        private double newGas;

        public NewRecord()
        {
        }

        public NewRecord(NewRecord orig)
        {
            this.setSeedMass(orig.getSeedMass());
            this.setNewDust(orig.getNewDust());
            this.setNewGas(orig.getNewGas());
        }

        public double getSeedMass()
        {
            return seedMass;
        }

        public void setSeedMass(double seedMass)
        {
            this.seedMass = seedMass;
        }

        public double getNewDust()
        {
            return newDust;
        }

        public void setNewDust(double newDust)
        {
            this.newDust = newDust;
        }

        public double getNewGas()
        {
            return newGas;
        }

        public void setNewGas(double newGas)
        {
            this.newGas = newGas;
        }
    }

    /**
     * Get the first record in the planet list
     *
     * @return The head of the planet list
     */
    public Planet getPlanetHead()
    {
        return planetHead;
    }

    /**
     * Set the first record in the planet list
     *
     * @param planetHead The new head of the planet list
     */
    public void setPlanetHead(Planet planetHead)
    {
        this.planetHead = planetHead;
    }

    /**
     * Get the first record in the dust list
     *
     * @return The head of the dust list
     */
    public DustRecord getDustHead()
    {
        return dustHead;
    }

    /**
     * Set the first record in the dust list
     *
     * @param dustHead The new head of the dust list
     */
    public void setDustHead(DustRecord dustHead)
    {
        this.dustHead = dustHead;
    }
}
