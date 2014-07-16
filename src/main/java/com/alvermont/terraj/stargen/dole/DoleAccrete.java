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
 * DoleAccrete.java
 *
 * Created on December 26, 2005, 5:09 PM
 *
 */
package com.alvermont.terraj.stargen.dole;

import com.alvermont.terraj.stargen.*;
import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dole.c - Planetary accretion algorithm, Icarus V 13 (1978)
 *
 * Ported from Public Domain C code originally written by Andrew Folkins.
 * Note the following comment is from the original C code and computer
 * performance has increased substantially since then:
 *
 * This program implements the accretion algorithm given by Dole.  It's
 * major flaw for simulation on a microcomputer is the amount of
 * computation which needs to be done.  I've tried to simplify the
 * computations a bit, these instances are noted in the code.
 * What I'd like to do is use integer arithmetic, but I don't think that's
 * possible.
 *
 * @author martin
 * @version $Id: DoleAccrete.java,v 1.3 2006/07/06 06:58:35 martin Exp $
 */
public class DoleAccrete
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(DoleAccrete.class);

    // RequireThis OFF: log
    // MagicNumber OFF

    /**
     * AO * sqrt(star->mass)
     */
    protected double A;

    /** Minimum radius of planetismal injection, = MINRADIUS, MAXRADIUS */
    private double minRadius;

    /** Max radius of injection . . . modified by luminosity of primary */
    private double maxRadius;

    /** Linked list of dust records */
    private LinkedList<DustRecord> list0 = new LinkedList<DustRecord>();

    /** Linked list of gas records */
    private LinkedList<DustRecord> list1 = new LinkedList<DustRecord>();

    /** Linked list of dust records */
    private LinkedList<DustRecord> list2 = new LinkedList<DustRecord>();

    /** Critical mass for gas accretion */
    double criticalMass; /* for accreting gas */

    /** Profiling counter (from original code, not needed in Java but this one
     * is used for something else) */
    private short nNucleus = 0;

    /** Counter for coalescences */
    private int coalesceCount = 0;

    /** The maths object used for random numbers */
    private MathUtils utils;

    /** The object used to get details about the generated plants */
    private DolePlanetStats planetStats;

    /** The comparator used to sort planets by orbital radius */
    private PlanetOrbitComparator poc = new PlanetOrbitComparator();

    /**
     * Creates a new instance of DoleAccrete
     *
     * @param utils The math utility object to use for random numbers
     */
    public DoleAccrete(MathUtils utils)
    {
        this.utils = this.utils;
        this.planetStats = new DolePlanetStats(this.utils);
    }

    /**
     * Creates a new instance of DoleAccrete
     */
    public DoleAccrete()
    {
        this.utils = new MathUtils();
        this.planetStats = new DolePlanetStats();
    }

    /**
     * Get the density for a particular radius
     *
     * @param r The orbital radius to get the density for
     * @return The calculated density
     */
    public double getDensity(double r)
    {
        return (A * Math.exp(
                -DoleConstants.ALPHA * Math.pow(r, DoleConstants.GAMMA)));
    }

    /**
     * Get the gravitational reach for a particular mass at a given orbital radius
     *
     * @param radius The orbital radius to get the density for
     * @param mass The mass of interest
     * @return The calculated reach
     */
    double getReach(double radius, double mass)
    {
        return (radius * Math.pow(mass / (1.0 + mass), 0.25));
    }

    /**
     * Adds a planet to the list and keeps it sorted into order by increasing
     * distance from the star.
     *
     * @param list The list of planets
     * @param newp The new planet to be added
     */
    protected void addPlanet(List<Planet> list, Planet newp)
    {
        list.add(newp);
        Collections.sort(list, poc);
    }

    /**
     * Initialize the dust band information
     */
    protected void initBands()
    {
        list0 = new LinkedList<DustRecord>();
        list1 = new LinkedList<DustRecord>();
        list2 = new LinkedList<DustRecord>();

        this.list0.add(new DustRecord(minRadius, maxRadius));
        this.list1.add(new DustRecord(minRadius, maxRadius));
        this.list2.add(new DustRecord(minRadius, maxRadius));
    }

    /**
     * Release any dust information held in the lists
     */
    protected void freeBands()
    {
        this.list0.clear();
        this.list0 = null;

        this.list1.clear();
        this.list1 = null;

        this.list2.clear();
        this.list2 = null;
    }

    /**
     * Release resources held by this class. This is here to match the
     * format used by some of the other classes.
     */
    public void releaseResources()
    {
        freeBands();
    }

    /**
     * Create a new planet around the star
     *
     * @param star The star of this solar system
     * @return A planet record for the new planet
     */
    public DolePlanetRecord createPlanet(Primary star)
    {
        final DolePlanetRecord newp = new DolePlanetRecord();

        newp.setPrimary(star);

        newp.setGasGiant(false);
        newp.setMass(DoleConstants.M0);
        newp.setDustMass(DoleConstants.M0);
        newp.setGasMass(0);

        newp.setE(
            1.0 - Math.pow((this.utils.nextDouble() * 0.99) + 0.01, 0.077));

        newp.setA(this.utils.nextDouble());
        newp.setA(newp.getA() * newp.getA());

        /* Fire the first 20 nuclei in at random, then aim them at the
         * remaining dust bands.  We're just grabbing the first dust band
         * in the list, but the list isn't kept sorted so this is still
         * somewhat random.
         */
        if (nNucleus <= 20000)
        {
            newp.setE(newp.getE() * 1.5);
            newp.setA(minRadius + ((maxRadius - minRadius) * newp.getA()));
        }
        else
        {
            final DustRecord b = list0.peek();

            newp.setRadius(
                b.getInnerEdge() +
                ((b.getOuterEdge() - b.getInnerEdge()) * newp.getA()));
            newp.setE(newp.getE() * 2.0);
        }

        newp.setRMin(newp.getA());
        newp.setRMax(newp.getA());
        /* newp->density = Density(newp->r); */
        /* Modification #3 */
        // planet number
        newp.setNumber(0);
        ++nNucleus;

        if (star.getPlanets() == null)
        {
            star.setPlanets(new ArrayList<Planet>());
        }

        addPlanet(star.getPlanets(), newp);

        return (newp);
    }

    /**
     * Compute the amount of mass (of dust or gas) which the planet p will
     * sweep from the available material in one iteration.
     *
     * @param list A list of dust records
     * @param listtype Which list this corresponds to (dust, gas)
     * @param p The planet record for the planet being constructed
     * @return The amount of mass swept from the dust or gas
     */
    protected double sweptMass(
        LinkedList<DustRecord> list, int listtype, DolePlanetRecord p)
    {
        double r;
        double mass;
        double min;
        double max;
        double density;
        double tGas;

        mass = 0.0;

        min = p.getRMin();
        max = p.getRMax();

        /* Account for eccentricity of dust particles */
        if (listtype == 0)
        {
            min = min / (1 + DoleConstants.W);
            max = max / (1 - DoleConstants.W);
        }

        /* Used in gas accretion, it's constant so we can move it out here.  */
        tGas = DoleConstants.K / (((DoleConstants.K - 1) * Math.pow(
                    criticalMass / p.getMass(), DoleConstants.BETA)) + 1);

        /*
         * Modification #3
         * Approximate density of material we're accreting.  This is actually
         * the density at the planet's orbit, but it's (hopefully) close enough.
         * It shouldn't matter for small planets, but large gas giants may
         * accrete too much.
         */

        /*
         * density = p->density;
         * if (listtype == 2) density *= tGas;
         */

        /* Traverse the list, looking at each existing band to see what we
         * would sweep up.
         */
        for (DustRecord b : list)
        {
            /* check for trivial rejection */
            if ((max < b.getInnerEdge()) || (min > b.getOuterEdge()))
            {
                continue;
            }

            if (max > b.getOuterEdge())
            {
                max = b.getOuterEdge();
            }

            if (min < b.getInnerEdge())
            {
                min = b.getInnerEdge();
            }

            r = (min + max) / 2.0;

            /* Modification #3
             * If we were really strict, we'd try to integrate the density
             * function over the range (min, max) of orbital distance, but
             * we'll use the average distance instead.
             */
            density = getDensity(r);

            if (listtype == 2)
            {
                density *= tGas;
            }

            /* The swept mass is supposed to be computed using the gravitational
             * reach and density at the minimum and maximum distances at which
             * dust is encountered.  I'm cheating here by using the reach at
             * the average orbital distance (computed in EvolvePlanet()) and
             * the density at the center of the band.  The total swept mass is
             * then 2 * reach        the height of the swept area
             *      * (max - min)    the width of the swept area
             *      2 * pi * r       revolve around center
             *      * density        the density of the swept volume
             * We can speed things up a bit by moving the constant values
             * outside the loop.
             */
            mass += (r * (max - min) * density);
        }

        return (2 * Math.PI * 2 * p.getReach() * mass);
    }

    /**
     * Update the band structure by removing or splitting bands from which
     * the planet would have accreted mass.
     *
     * @param list The dust list to be updated
     * @param p The planet being constructed
     */
    protected void updateBands(LinkedList<DustRecord> list, DolePlanetRecord p)
    {
        final double min = p.getRMin(); /* minimum and maximum reach of the planet */
        final double max = p.getRMax();

        final Iterator<DustRecord> i = list.iterator();

        List<DustRecord> pendingPrepend = null;

        while (i.hasNext())
        {
            final DustRecord b = i.next();

            /* check for trivial rejection */
            if ((max < b.getInnerEdge()) || (min > b.getOuterEdge()))
            {
                continue;
            }

            if (max < b.getOuterEdge())
            {
                if (min > b.getInnerEdge())
                {
                    /* interval within band, so split it */
                    final DustRecord newband =
                        new DustRecord(b.getInnerEdge(), min);
                    b.setInnerEdge(max);

                    if (pendingPrepend == null)
                    {
                        pendingPrepend = new ArrayList<DustRecord>();
                    }

                    pendingPrepend.add(newband);
                }
                else
                {
                    /* interval overlaps inner edge */
                    b.setInnerEdge(max);
                }
            }
            else
            {
                if (min > b.getInnerEdge())
                {
                    /* interval overlaps outer edge */
                    b.setOuterEdge(min);
                }
                else
                {
                    /* interval contains band, so kill it */
                    i.remove();
                }
            }
        }

        // now add the elements we couldn't because we'd have mucked up the iterator
        // java list/iterator semantics can be v. annoying sometimes
        if (pendingPrepend != null)
        {
            for (DustRecord newband : pendingPrepend)
            {
                list.addFirst(newband);
            }
        }
    }

    /**
     * Sweep up all available dust and gas.
     *
     * @param star The star for this solar system
     * @param p The planet being constructed
     */
    protected void evolvePlanet(Primary star, DolePlanetRecord p)
    {
        double perihelion;
        double aphelion;
        double previousMass;
        double swept;

        if (p == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Called evolvePlanet with null planet reccord");
            }

            return;
        }

        /* Our planetoid will accrete all matter within it's orbit . . . */
        perihelion = p.getA() * (1 - p.getE());
        aphelion = p.getA() * (1 + p.getE());

        this.criticalMass = DoleConstants.B * Math.pow(
                    Math.sqrt(star.getLuminosity()) / perihelion, 0.75);

        int evolveCount = 0;

        // this construct always brings a sense of dread
        for (;;)
        {
            ++evolveCount;

            perihelion = p.getA() * (1 - p.getE());
            aphelion = p.getA() * (1 + p.getE());

            /* . . . as well as within it's gravitational reach.  We should be
             * computing the reach at aphelion and at perihelion, but they
             * aren't that different so we'll cut out some computation.
             */
            p.setReach(getReach(p.getA(), p.getMass()));

            /* Reach(aphelion, p->mass); */
            p.setRMax(aphelion + p.getReach());
            /* Reach(perihelion, p->mass); */
            p.setRMin(perihelion - p.getReach());

            previousMass = p.getMass();

            /* accrete dust */
            swept = sweptMass(this.list0, 0, p);

            p.setDustMass(Math.max(p.getDustMass(), swept));
            p.setGasMass(
                Math.max(
                    p.getGasMass(),
                    swept * this.utils.nextDouble() * p.getMass()));

            /* accrete gas */
            if (p.getDustMass() > this.criticalMass)
            {
                // it's a gas giant
                p.setGasGiant(true);

                swept = sweptMass(this.list2, 2, p);
                p.setGasMass(Math.max(p.getGasMass(), swept));
            }

            p.setMass(p.getDustMass() + p.getGasMass());

            if ((((p.getMass() - previousMass) / p.getMass()) < 0.01))
            {
                break;
            }
        }

        //        if (log.isDebugEnabled())
        //            log.debug("Iterations in evolvePlanet: " + evolveCount + " " +
        //                    ((NonRandomRandom) this.utils.getRandom()).getCallCount());

        /* You'll notice we didn't modify the band structure at all while
         * accreting matter, we do that now.
         */
        updateBands(this.list0, p);

        if (p.isGasGiant())
        {
            /* do something with the gas density */
            /* In this case, it's cheaper to just recompute the accreted gas
             * in each iteration as we only use the one gas band.
             */
            updateBands(this.list1, p);
            updateBands(this.list2, p);
        }
    }

    /**
     * We've found a collision, so we just stick the two planets together.
     * No fragments or moon creation, no close passes which would eject one
     * planet from the system, nothing fancy like that.  We weight the
     * final orbital radius by the masses of the planets involved, and
     * use the smaller eccentricity.
     *
     * @param p1 The first planet in the collision
     * @param p2 The second planet in the collision
     * @return A planet record for the resulting merged body
     */
    protected Planet mergePlanets(
        DolePlanetRecord p1, DolePlanetRecord p2)
    {
        double perihelion;
        double aphelion;

        ++this.coalesceCount;

        p2.setA(
            (p1.getMass() + p2.getMass()) / ((p1.getMass() / p1.getA()) +
                (p2.getMass() / p2.getA())));
        p2.setE(Math.min(p1.getE(), p2.getE()));

        perihelion = p2.getA() * (1 - p2.getE());
        aphelion = p2.getA() * (1 + p2.getE());

        p2.setRMax(aphelion + getReach(aphelion, p2.getMass()));
        p2.setRMin(perihelion - getReach(perihelion, p2.getMass()));

        p2.setMass(p2.getMass() + p1.getMass());
        p2.setDustMass(p2.getDustMass() + p1.getDustMass());
        p2.setGasMass(p2.getGasMass() + p1.getGasMass());

        /* p2->density = Density(p2->r); */
        /* Modification #3 */
        return p2;
    }

    /**
     * Let's see who 'p' will run into, if anyone.
     *
     * @param star The star for the solar system
     * @param p The planet being considered
     */
    void checkCoalesence(Primary star, DolePlanetRecord p)
    {
        boolean merged = true;

        while (merged)
        {
            merged = false;

            final List<Planet> planets = star.getPlanets();

            int index = planets.indexOf(p);

            if (index < 0)
            {
                throw new AssertionError("Did not find the planet in the list");
            }

            int pindex = index - 1;

            while (pindex >= 0)
            {
                final DolePlanetRecord p1 =
                    (DolePlanetRecord) planets.get(pindex);

                if (p1.getRMax() >= p.getRMin())
                {
                    planets.set(pindex, mergePlanets(p1, p));

                    planets.remove(pindex + 1);

                    --pindex;

                    merged = true;
                }
                else
                {
                    pindex = -1;
                }
            }

            if (merged)
            {
                // then this could have changed
                index = planets.indexOf(p);
            }

            pindex = index + 1;

            while (pindex < planets.size())
            {
                final DolePlanetRecord p1 =
                    (DolePlanetRecord) planets.get(pindex);

                if (p1.getRMin() <= p.getRMax())
                {
                    planets.set(pindex, mergePlanets(p1, p));
                    planets.remove(pindex - 1);

                    ++pindex;

                    merged = true;
                }
                else
                {
                    pindex = Integer.MAX_VALUE;
                }
            }

            if (merged)
            {
                evolvePlanet(star, p);
            }
        }
    }

    /**
     * Creates a solar system. On return a list of
     * planets will be available that can be obtained with getPlanets()
     *
     * @param star The star in the solar system.
     */
    public void createSystem(Primary star)
    {
        short i;

        // fill in luminosity and ecosphere if not already set
        if (star.getLuminosity() == 0)
        {
            star.setLuminosity(EnviroUtils.getLuminosity(star.getMass()));
        }

        star.setREcosphere(Math.sqrt(star.getLuminosity()));

        star.setREcosphereInner(star.getREcosphere() * 0.93);
        star.setREcosphereOuter(star.getREcosphere() * 1.1);

        // done
        this.nNucleus = 0;

        /* A little initialization . . . */
        this.A = DoleConstants.AO * Math.sqrt(star.getMass());

        minRadius = DoleConstants.MINRADIUS * Math.pow(star.getMass(), 0.33);
        maxRadius = DoleConstants.MAXRADIUS * Math.pow(star.getMass(), 0.33);

        initBands();

        /* . . . and we're off to play God. */
        while (!list0.isEmpty())
        {
            final DolePlanetRecord p = createPlanet(star);

            if (p == null)
            {
                break;
            }

            evolvePlanet(star, p);

            checkCoalesence(star, p);
        }

        freeBands();

        i = 1;

        final ListIterator<Planet> li = star.getPlanets()
                .listIterator();

        while (li.hasNext())
        {
            final Planet pl = (Planet) li.next();

            if (pl.getMass() > 2e-8)
            {
                pl.setNumber(++i);

                planetStats.computePlanetStats(star, pl);
            }
            else
            {
                li.remove();
            }
        }
    }

    /**
     * A comparator class used to sort the planet list
     */
    protected class PlanetOrbitComparator implements Comparator<Planet>
    {
        /** Create a new instance of PlanetOrbitComparator */
        public PlanetOrbitComparator()
        {
        }

        /**
         * Implements planetary comparison by orbital radius. The result is
         * as would be expected for a numeric comparison. A lesser orbital
         * radius compares less than a planet with a greater one.
         *
         * @param o1 The first planet to be compared
         * @param o2 The first planet to be compared
         * @return The result of the comparison according to the contract of
         * compare and the expected result of numerically comparing the
         * orbital radius of the planets.
         */
        public int compare(Planet o1, Planet o2)
        {
            return Double.compare(o1.getA(), o2.getA());
        }
    }
}
