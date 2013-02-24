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
 * Generator.java
 *
 * Created on December 22, 2005, 6:07 PM
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles the top level planetary system generation
 *
 * @author martin
 * @version $Id: Generator.java,v 1.15 2006/07/06 06:58:33 martin Exp $
 */
public class Generator
{
    /** Math utilities and random number generator interface */
    private MathUtils utils;

    /** The accretion object */
    private Accrete accrete;

    /** The enviroment calculation object */
    private Enviro enviro;

    /** A table of gas information */
    private Gases gases;

    /** Star generator used to make the star for a system */
    private GenStar genstar;

    /** Our logging object */
    private static Log log = LogFactory.getLog(Generator.class);

    // MagicNumber OFF

    /**
     * Creates a new instance of Generator
     *
     * @param utils The math utilities object to use for random numbers
     */
    public Generator(MathUtils utils)
    {
        this.utils = utils;
        this.accrete = new Accrete(utils);
        this.enviro = new Enviro(utils);
        this.gases = new Gases();
        this.genstar = new GenStar(utils);
    }

    /** Creates a new instance of Generator */
    public Generator()
    {
        this.utils = new MathUtils();
        this.accrete = new Accrete();
        this.enviro = new Enviro(this.utils);
        this.gases = new Gases();
        this.genstar = new GenStar();
    }

    /**
     * Sets the seed used by the random number generator used by this class
     *
     * @param seed The new seed value to be used
     */
    public void setSeed(long seed)
    {
        this.utils.setSeed(seed);
        this.accrete.setSeed(seed);
    }

    /**
     * Generate and return a new star
     *
     * @return A randomly generated star object
     */
    public Primary generateStar()
    {
        return this.genstar.generateStar();
    }

    /**
     * Generate a stellar system based around a particular star and possibily
     * using some planetary bodies as seed values.
     *
     * @param sun The star to form planets around
     * @param seedSystem Points to the first record in a list to form the basis of
     * the system. Set this to <code>null</code> to generate all planets from scratch.
     * @param outerPlanetLimit The outermost planetary range in AU
     * @param dustDensityCoeff The coefficient for dust density
     */
    public void generateStellarSystem(
        Primary sun, Planet seedSystem, double outerPlanetLimit,
        double dustDensityCoeff)
    {
        double outerDustLimit;

        if ((sun.getMass() < 0.2) || (sun.getMass() > 1.5))
        {
            sun.setMass(this.utils.randomNumber(0.7, 1.4));
        }

        outerDustLimit = AccreteUtils.getStellarDustLimit(sun.getMass());

        if (sun.getLuminosity() == 0)
        {
            sun.setLuminosity(EnviroUtils.getLuminosity(sun.getMass()));
        }

        sun.setREcosphere(Math.sqrt(sun.getLuminosity()));
        sun.setLife(1.0E10 * (sun.getMass() / sun.getLuminosity()));

        final double minAge = 1.0E9;
        double maxAge = 6.0E9;

        if (sun.getLife() < maxAge)
        {
            maxAge = sun.getLife();
        }

        if (sun.getInnermostPlanet() == null)
        {
            sun.setInnermostPlanet(
                this.accrete.distPlanetaryMasses(
                    sun.getMass(), sun.getLuminosity(), 0.0, outerDustLimit,
                    outerPlanetLimit, dustDensityCoeff, seedSystem));

            if (sun.getAge() == 0.0)
            {
                sun.setAge(this.utils.randomNumber(minAge, maxAge));
            }

            final List<Planet> planets = new ArrayList<Planet>();

            sun.setPlanets(planets);

            for (
                Planet planet = sun.getInnermostPlanet(); planet != null;
                    planet = planet.getNextPlanet())
            {
                planets.add(planet);
            }
        }
    }

    /**
     * Initialize a planet record
     *
     * @param planet The planet record to be initialized
     */
    protected void initPlanet(Planet planet)
    {
        planet.setFirstMoon(null);
        planet.setGases(null);

        planet.setHighTemperature(0);
        planet.setLowTemperature(0);
        planet.setMaxTemperature(0);
        planet.setMinTemperature(0);
        planet.setGreenhouseRise(0);
        planet.setResonantPeriod(false);
    }

    /**
     * Initialize the planetary orbital velocity and radius. Also calculates
     * the exospheric temperature of the planet. On return the corresponding
     * values will be valid in the planet record.
     *
     *
     * @param sun The star for the solar system
     * @param planet The planet to be calculated for
     * @param randomTilt If <pre>true</pre> then the planet is to be given a
     * random orbital tilt
     */
    protected void initPlanetOrbitVelocityRadius(
        Primary sun, Planet planet, boolean randomTilt)
    {
        planet.setOrbitZone(
            EnviroUtils.getOrbitalZone(sun.getLuminosity(), planet.getA()));
        planet.setOrbitalPeriod(
            this.enviro.getPeriod(
                planet.getA(), planet.getMass(), sun.getMass()));

        if (randomTilt)
        {
            planet.setAxialTilt(this.enviro.getInclination(planet.getA()));
        }

        planet.setExosphericTemperature(
            Constants.EARTH_EXOSPHERE_TEMP / MathUtils.pow2(
                    planet.getA() / sun.getREcosphere()));

        planet.setRmsVelocity(
            this.enviro.getRmsVelocity(
                Constants.MOL_NITROGEN, planet.getExosphericTemperature()));

        planet.setCoreRadius(
            EnviroUtils.getKothariRadius(
                planet.getDustMass(), false, planet.getOrbitZone()));
    }

    /**
     * Initialize the planetary density and gravity. On return the corresponding
     * values will be valid in the planet record.
     *
     * @param sun The star for the solar system
     * @param planet The planet to be calculated for
     */
    protected void initPlanetDensityGravity(Primary sun, Planet planet)
    {
        // Calculate the radius as a gas giant, to verify it will retain gas.
        // Then if mass > Earth, it's at least 5% gas and retains He, it's
        // some flavor of gas giant.
        planet.setDensity(
            EnviroUtils.getEmpiricalDensity(
                planet.getMass(), planet.getA(), sun.getREcosphere(), true));
        planet.setRadius(
            EnviroUtils.getVolumeRadius(planet.getMass(), planet.getDensity()));

        planet.setSurfaceAcceleration(
            this.enviro.getAcceleration(planet.getMass(), planet.getRadius()));
        planet.setSurfaceGravity(
            this.enviro.getGravity(planet.getSurfaceAcceleration()));

        planet.setMolecularWeight(this.enviro.getMinMolecWeight(planet));

        if (
            ((planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES) > 1.0) &&
                ((planet.getGasMass() / planet.getMass()) > 0.05) &&
                (planet.getMolecularWeight() <= 4.0))
        {
            if ((planet.getGasMass() / planet.getMass()) < 0.20)
            {
                planet.setType(PlanetType.SUBSUBGASGIANT);
            }
            else if (
                (planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES) < 20.0)
            {
                planet.setType(PlanetType.SUBGASGIANT);
            }
            else
            {
                planet.setType(PlanetType.GASGIANT);
            }

            // TODO: verify this
            planet.setGasGiant(true);
        }
        else
        {
            // If not, it's rocky.
            planet.setRadius(
                EnviroUtils.getKothariRadius(
                    planet.getMass(), false, planet.getOrbitZone()));
            planet.setDensity(
                EnviroUtils.getVolumeDensity(
                    planet.getMass(), planet.getRadius()));

            planet.setSurfaceAcceleration(
                this.enviro.getAcceleration(
                    planet.getMass(), planet.getRadius()));
            planet.setSurfaceGravity(
                this.enviro.getGravity(planet.getSurfaceAcceleration()));

            if ((planet.getGasMass() / planet.getMass()) > 0.000001)
            {
                final double h2Mass = planet.getGasMass() * 0.85;
                final double heMass = (planet.getGasMass() - h2Mass) * 0.999;

                double h2Loss = 0.0;
                double heLoss = 0.0;

                final double h2Life =
                    this.enviro.getGasLife(Constants.MOL_HYDROGEN, planet);
                final double heLife =
                    this.enviro.getGasLife(Constants.HELIUM, planet);

                if (h2Life < sun.getAge())
                {
                    h2Loss = ((1.0 - (1.0 / Math.exp(sun.getAge() / h2Life))) * h2Mass);

                    planet.setGasMass(planet.getGasMass() - h2Loss);
                    planet.setGasMass(planet.getMass() - h2Loss);

                    planet.setSurfaceAcceleration(
                        this.enviro.getAcceleration(
                            planet.getMass(), planet.getRadius()));
                    planet.setSurfaceGravity(
                        this.enviro.getGravity(planet.getSurfaceAcceleration()));
                }

                if (heLife < sun.getAge())
                {
                    heLoss = ((1.0 - (1.0 / Math.exp(sun.getAge() / heLife))) * heMass);

                    planet.setGasMass(planet.getGasMass() - heLoss);
                    planet.setGasMass(planet.getMass() - heLoss);

                    planet.setSurfaceAcceleration(
                        this.enviro.getAcceleration(
                            planet.getMass(), planet.getRadius()));
                    planet.setSurfaceGravity(
                        this.enviro.getGravity(planet.getSurfaceAcceleration()));
                }
            }
        }
    }

    /**
     * Initialize the planetary day length and escape velocity. On return the
     * corresponding values will be valid in the planet record.
     *
     * @param planet The planet to be calculated for
     */
    protected void initPlanetDayVelocity(Planet planet)
    {
        planet.setDay(this.enviro.getDayLength(planet));
        planet.setEscapeVelocity(
            this.enviro.getEscapeVelocity(planet.getMass(), planet.getRadius()));
    }

    /**
     * Initialize gas giant and atmospheric related parameters for a planet.
     * On return the corresponding values will be valid for the planet record.
     *
     * @param planet The planet to calculate for
     */
    protected void initPlanetGasGiant(Planet planet)
    {
        planet.setGreenhouseEffect(false);
        planet.setVolatileGasInventory(Constants.INCREDIBLY_LARGE_NUMBER);
        planet.setSurfacePressure(Constants.INCREDIBLY_LARGE_NUMBER);

        planet.setBoilingPoint(Constants.INCREDIBLY_LARGE_NUMBER);
        planet.setSurfaceTemperature(Constants.INCREDIBLY_LARGE_NUMBER);

        planet.setGreenhouseRise(0);
        planet.setAlbedo(this.utils.about(Constants.GAS_GIANT_ALBEDO, 0.1));
        planet.setHydrosphere(Constants.INCREDIBLY_LARGE_NUMBER);
        planet.setSurfaceGravity(
            this.enviro.getGravity(planet.getSurfaceAcceleration()));
        planet.setMolecularWeight(this.enviro.getMinMolecWeight(planet));
        planet.setSurfaceGravity(Constants.INCREDIBLY_LARGE_NUMBER);
    }

    /**
     * Initialize the planetary surface gravity, pressures and temperatures.
     * On return the corresponding values will be valid in the planet record
     *
     * @param sun The star for the solar system
     * @param planet The planet to be calculated for
     */
    protected void initPlanetGravityPressureTemp(
        Primary sun, Planet planet)
    {
        planet.setSurfaceGravity(
            this.enviro.getGravity(planet.getSurfaceAcceleration()));
        planet.setMolecularWeight(this.enviro.getMinMolecWeight(planet));

        planet.setGreenhouseEffect(
            this.enviro.isGreenhouse(sun.getREcosphere(), planet.getA()));
        planet.setVolatileGasInventory(
            this.enviro.getVolInventory(
                planet.getMass(), planet.getEscapeVelocity(),
                planet.getRmsVelocity(), sun.getMass(), planet.getOrbitZone(),
                planet.isGreenhouseEffect(),
                (planet.getGasMass() / planet.getMass()) > 0.000001));

        planet.setSurfacePressure(
            this.enviro.getPressure(
                planet.getVolatileGasInventory(), planet.getRadius(),
                planet.getSurfaceGravity()));

        if ((planet.getSurfacePressure() == 0.0))
        {
            planet.setBoilingPoint(0);
        }
        else
        {
            planet.setBoilingPoint(
                EnviroUtils.getBoilingPoint(planet.getSurfacePressure()));
        }

        /*        Sets:
         *                planet->surf_temp
         *                planet->greenhs_rise
         *                planet->albedo
         *                planet->hydrosphere
         *                planet->cloud_cover
         *                planet->ice_cover
         */
        this.enviro.iterateSurfaceTemp(planet);
    }

    /**
     * Comparator class to sort gases by pressures
     */
    protected class GasPressureComparator implements Comparator<Gas>
    {
        /** Create a new instance of GasPressureComparator */
        protected GasPressureComparator()
        {
        }

        /**
         * Compare two gases by their surface pressure and order them as
         * for a standard numerical comparison of this quantity such that
         * a < b if the surface pressure of a < surface pressure of b.
         *
         * @param g1 The first gas to compare
         * @param g2 The second gas to compare
         * @return A value according to the result of the comparison and the
         * contract of compare.
         */
        public int compare(Gas g1, Gas g2)
        {
            int result = 0;

            if (g1.getSurfacePressure() != g2.getSurfacePressure())
            {
                if (g1.getSurfacePressure() > g2.getSurfacePressure())
                {
                    result = 1;
                }
                else
                {
                    result = -1;
                }
            }

            return result;
        }
    }

    /**
     * Initialize the atmosphere for a planet. On return the corresponding
     * values will be valid in the planet record
     *
     * @param sun The star for the solar system
     * @param planet The planet to calculate the atmosphere for
     */
    protected void initPlanetAtmosphere(Primary sun, Planet planet)
    {
        if (planet.getSurfacePressure() > 0)
        {
            final Collection allGases = this.gases.getAllGases();

            final double[] amount = new double[allGases.size()];

            double totamount = 0;
            final double pressure =
                planet.getSurfacePressure() / Constants.MILLIBARS_PER_BAR;

            int n = 0;

            for (int i = 0; i < allGases.size(); ++i)
            {
                final double bp =
                    this.gases.getGasByIndex(i)
                        .getBoilingPoint() / (373. * ((Math.log(
                            (pressure) + 0.001) / -5050.5) + (1.0 / 373.)));

                if (
                    ((bp >= 0) && (bp < planet.getLowTemperature())) &&
                        (this.gases.getGasByIndex(i)
                        .getWeight() >= planet.getMolecularWeight()))
                {
                    final double vrms =
                        this.enviro.getRmsVelocity(
                            this.gases.getGasByIndex(i).getWeight(),
                            planet.getA());
                    final double pvrms =
                        Math.pow(
                            1 / (1 + (vrms / planet.getEscapeVelocity())),
                            sun.getAge() / 1e9);

                    /* gases[i].abunde */
                    final double abund =
                        this.gases.getGasByIndex(i)
                            .getAbunds();
                    double react = 1.0;
                    double fract = 1.0;
                    double pres2 = 1.0;

                    // test for argon
                    if (
                        this.gases.getGasByIndex(i)
                            .getNumber() == Constants.AN_AR)
                    {
                        react = (.19 * sun.getAge()) / 4e9;
                    }
                    else if (
                        (this.gases.getGasByIndex(i)
                            .getNumber() == Constants.AN_O) &&
                            (sun.getAge() > 2e9) &&
                            (planet.getSurfaceTemperature() > 270) &&
                            (planet.getSurfaceTemperature() < 400))
                    {
                        // NOTE: in original test name was for O and O2 listed as 
                        // ozone but the gases files contains O3 for ozone so it 
                        // couldn't have matched. This might be an error or the 
                        // result of a change

                        /*  pres2 = (0.65 + pressure/2); Breathable - M: .55-1.4 */
                        /*        Breathable - M: .6 -1.8         */
                        pres2 = (0.89 + (pressure / 4));
                        react = Math.pow(
                                1 / (1 +
                                    this.gases.getGasByIndex(i)
                                    .getReactivity()),
                                Math.pow(sun.getAge() / 2e9, 0.25) * pres2);
                    }
                    else if (
                        (this.gases.getGasByIndex(i)
                            .getNumber() == Constants.AN_CO2) &&
                            (sun.getAge() > 2e9) &&
                            (planet.getSurfaceTemperature() > 270) &&
                            (planet.getSurfaceTemperature() < 400))
                    {
                        pres2 = (0.75 + pressure);
                        react = Math.pow(
                                1 / (1 +
                                    this.gases.getGasByIndex(i)
                                    .getReactivity()),
                                Math.pow(sun.getAge() / 2e9, 0.5) * pres2);
                        react *= 1.5;
                    }
                    else
                    {
                        pres2 = (0.75 + pressure);
                        react = Math.pow(
                                1 / (1 +
                                    this.gases.getGasByIndex(i)
                                    .getReactivity()),
                                sun.getAge() / 2e9 * pres2);
                    }

                    fract = (1 -
                        (planet.getMolecularWeight() / this.gases.getGasByIndex(
                                i)
                            .getWeight()));

                    amount[i] = abund * pvrms * react * fract;

                    totamount += amount[i];

                    if (amount[i] > 0.0)
                    {
                        ++n;
                    }
                }
                else
                {
                    amount[i] = 0.0;
                }
            }

            if (n > 0)
            {
                final List<Gas> atmos = new ArrayList<Gas>();

                planet.setGases(atmos);

                for (int i = 0; i < this.gases.getAllGases()
                        .size(); ++i)
                {
                    if (amount[i] > 0.0)
                    {
                        final Gas agas = new Gas();

                        ChemTable element = this.gases.getGasByIndex(i);
                        
                        agas.setElement(element);
                        
                        agas.setSurfacePressure(
                            (planet.getSurfacePressure() * amount[i]) / totamount);

                        atmos.add(agas);
                    }
                }

                Collections.sort(atmos, new GasPressureComparator());
            }
        }
    }

    /**
     * Initialize the type of a planet. On return the corresponding value will
     * be valid in the planet record.
     *
     * @param planet The planet to set the type for
     */
    protected void initPlanetType(Planet planet)
    {
        // Atmospheres:
        if (planet.getHydrosphere() >= .95)
        {
            // >95% water
            planet.setType(PlanetType.WATER);
        }
        else if (planet.getIceCover() > .95)
        {
            // >95% ice
            planet.setType(PlanetType.ICE);
        }
        else if (planet.getHydrosphere() > .05)
        {
            // Terrestrial else <5% water
            planet.setType(PlanetType.TERRESTRIAL);
        }
        else if (planet.getSurfaceTemperature() > planet.getBoilingPoint())
        {
            // Hot = Venusian
            planet.setType(PlanetType.VENUSIAN);
        }
        else if ((planet.getGasMass() / planet.getMass()) > 0.0001)
        {
            // Accreted gas but no Greenhouse or liquid water
            // Make it an Ice World
            planet.setType(PlanetType.ICE);
            planet.setIceCover(1.0);
        }
        else
        {
            planet.setType(PlanetType.MARTIAN); // Dry = Martian
        }
    }

    void generatePlanets(
        Primary sun, boolean randomTilt, int sysNo, String systemName)
    {
        int planetNo = 0;

        for (Planet planet : sun.getPlanets())
        {
            planet.setPrimary(sun);
            planet.setNumber(++planetNo);

            initPlanet(planet);

            // moon generation is experimental and therefore optional
            if (generateMoons)
            {
                generateMoonsForPlanet(sun, planet);

                if (planet.getFirstMoon() != null)
                {
                    final List<Planet> moons =
                        new ArrayList<Planet>();

                    planet.setMoons(moons);

                    for (
                        Planet moon = planet.getFirstMoon();
                            moon != null; moon = moon.getNextPlanet())
                    {
                        moons.add(moon);
                    }
                }
            }

            initPlanetOrbitVelocityRadius(sun, planet, randomTilt);
            initPlanetDensityGravity(sun, planet);
            initPlanetDayVelocity(planet);

            if (PlanetType.isGasGiant(planet.getType()))
            {
                initPlanetGasGiant(planet);
            }
            else
            {
                initPlanetGravityPressureTemp(sun, planet);

                if (
                    (planet.getMaxTemperature() >= Constants.FREEZING_POINT_OF_WATER) &&
                        (planet.getMinTemperature() <= planet.getBoilingPoint()))
                {
                    initPlanetAtmosphere(sun, planet);
                }

                if (planet.getSurfacePressure() < 1.0)
                {
                    if (
                        (planet.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES) < Constants.ASTEROID_MASS_LIMIT)
                    {
                        planet.setType(PlanetType.ASTEROIDS);
                    }
                    else
                    {
                        planet.setType(PlanetType.ROCK);
                    }
                }
                else
                {
                    initPlanetType(planet);
                }
            }

            planet.setBreathability(this.enviro.getBreathability(planet));
        }
    }

    /**
     * Generate moons for a planet. On return the corresponding value in
     * the planet record will be set to a valid value.
     *
     * Note: This feature is marked as experimental in the original C
     * source code.
     *
     * @param sun The star for this solar system
     * @param planet The planet to generate moons for
     */
    public void generateMoonsForPlanet(Primary sun, Planet planet)
    {
        final double m1 = planet.getMass();
        final double m2 = sun.getMass();

        final double mu = m2 / (m1 + m2);

        final double e = planet.getE();
        final double a = planet.getA();

        final double outerLimit =
            (0.464 + (-0.380 * mu) + (-0.631 * e) + (0.586 * mu * e) +
            (0.150 * MathUtils.pow2(e)) + (-0.198 * mu * MathUtils.pow2(e))) * a;

        final double farthest = AccreteUtils.getFarthestPlanet(m1);
        final double outerDust = AccreteUtils.getStellarDustLimit(m1);

        //   if (outerLimit > farthest)
        //    outerLimit = farthest;
        planet.setFirstMoon(
            this.accrete.distMoonMasses(
                m1, sun.getLuminosity(), a, e, 0.0, outerDust, 1.0, outerLimit));

        // now remove moons that are too small
        Planet prevMoon = null;
        Planet moon = planet.getFirstMoon();

        while (moon != null)
        {
            if (
                (moon.getMass() * Constants.SUN_MASS_IN_EARTH_MASSES) < moonMinMassLimit)
            {
                // too small, remove from the list
                if (prevMoon == null)
                {
                    planet.setFirstMoon(moon.getNextPlanet());
                }
                else
                {
                    prevMoon.setNextPlanet(moon.getNextPlanet());
                }
            }
            else
            {
                prevMoon = moon;
            }

            moon = moon.getNextPlanet();
        }
    }

    /**
     * Generate a stellar system based around a particular star and possibily
     * using some planetary bodies as seed values.
     *
     * @param sun The star to form planets around
     * @param seedSystem Points to the first record in a list to form the basis of
     * the system. Set this to <code>null</code> to generate all planets from scratch.
     * @param outerPlanetLimit The outermost planetary range in AU
     * @param dustCoefficient The coefficient for dust density
     * @param randomTilt If <pre>true</pre> then give the planets a random tilt
     * @param systemNumber The number of the star system (not currently used)
     * @param systemName The name of the star system
     */
    public void generate(
        Primary sun, Planet seedSystem, double outerPlanetLimit,
        double dustCoefficient, boolean randomTilt, int systemNumber,
        String systemName)
    {
        generateStellarSystem(
            sun, seedSystem, outerPlanetLimit, dustCoefficient);
        generatePlanets(sun, randomTilt, systemNumber, systemName);
    }

    /**
     * Generate a stellar system using a simplified set of calling parameters
     *
     * @param sun The star to form planets around
     * @param systemNumber The number of the star system (not currently used)
     * @param systemName The name of the star system
     */
    public void generate(Primary sun, int systemNumber, String systemName)
    {
        generate(
            sun, null, 0.0, Constants.DUST_DENSITY_COEFF, true, systemNumber,
            systemName);
    }

    /**
     * Holds value of property generateMoons.
     */
    private boolean generateMoons;

    /**
     * Getter for property generateMoons.
     * @return Value of property generateMoons.
     */
    public boolean isGenerateMoons()
    {
        return this.generateMoons;
    }

    /**
     * Setter for property generateMoons.
     * @param generateMoons New value of property generateMoons.
     */
    public void setGenerateMoons(boolean generateMoons)
    {
        this.generateMoons = generateMoons;
    }

    /**
     * Holds value of property moonMinMassLimit.
     */
    private double moonMinMassLimit = .0001;

    /**
     * Getter for property moonMinMassLimit.
     * @return Value of property moonMinMassLimit.
     */
    public double getMoonMinMassLimit()
    {
        return this.moonMinMassLimit;
    }

    /**
     * Setter for property moonMinMassLimit.
     * @param moonMinMassLimit New value of property moonMinMassLimit.
     */
    public void setMoonMinMassLimit(double moonMinMassLimit)
    {
        this.moonMinMassLimit = moonMinMassLimit;
    }
}
