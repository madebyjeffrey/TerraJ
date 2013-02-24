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
 * StandardPlanetEvaluator.java
 *
 * Created on December 26, 2005, 1:53 PM
 *
 */
package com.alvermont.terraj.stargen.evaluator;

import com.alvermont.terraj.stargen.*;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator.AtmosphereEnum;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator.ClimateEnum;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator.CloudEnum;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator.GravityEnum;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator.TemperatureEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that applies some fairly standard classifications to a planet
 *
 * @author martin
 * @version $Id: StandardPlanetEvaluator.java,v 1.6 2006/07/06 06:58:36 martin Exp $
 */
public class StandardPlanetEvaluator implements PlanetEvaluator
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(StandardPlanetEvaluator.class);

    // MagicNumber OFF

    /** Object used for environment calculations */
    private Enviro enviro;

    /** The planet being evaluated */
    private Planet planet;

    /** Holds the gravity type */
    private GravityEnum gravity;

    /** Holds the temperature type */
    private TemperatureEnum temperature;

    /** Holds the atmosphere type */
    private AtmosphereEnum atmosphere;

    /** Holds the climate type */
    private ClimateEnum climate;

    /** Holds the clouds type */
    private CloudEnum clouds;

    /** Indicates planet is icy */
    private boolean icy;

    /** The description object we use */
    private PlanetDescriber describer = new PlanetDescriber();
    
    /** Creates a new instance of StandardPlanetEvaluator */
    public StandardPlanetEvaluator()
    {
        this.enviro = new Enviro();
    }

    /**
     * Evaluate the gravity of this planet
     *
     * @param sgravity The calculated surface gravity
     */
    protected void evaluateGravity(double sgravity)
    {
        if (sgravity < 0.8)
        {
            this.gravity = GravityEnum.LOW_G;
        }
        else if (sgravity > 1.2)
        {
            this.gravity = GravityEnum.HIGH_G;
        }
        else
        {
            this.gravity = GravityEnum.NORMAL_G;
        }
    }

    /**
     * Evaluate the temperature of this planet
     *
     * @param relTemp The calculated relative temperature
     */
    protected void evaluateTemperature(double relTemp)
    {
        if (relTemp < -5.0)
        {
            this.temperature = TemperatureEnum.COLD;
        }
        else if (relTemp < -2.0)
        {
            this.temperature = TemperatureEnum.COOL;
        }
        else if (relTemp > 7.5)
        {
            this.temperature = TemperatureEnum.HOT;
        }
        else if (relTemp > 3.0)
        {
            this.temperature = TemperatureEnum.WARM;
        }
        else
        {
            this.temperature = TemperatureEnum.NORMAL;
        }
    }

    /**
     * Evaluate a aPlanet and set the variables to the corresponding
     * indications. After this call use the get methods to retrieve the
     * result of the evaluation
     *
     * @param aPlanet The aPlanet to be evaluated
     */
    public void evaluate(Planet aPlanet)
    {
        this.planet = aPlanet;

        if (!PlanetType.isGasGiant(aPlanet.getType()))
        {
            final double relTemp =
                (aPlanet.getSurfaceTemperature() -
                Constants.FREEZING_POINT_OF_WATER) -
                Constants.EARTH_AVERAGE_CELSIUS;
            final double seas = aPlanet.getHydrosphere() * 100.0;
            final double cloudPercent = aPlanet.getCloudCover() * 100.0;
            final double atmospherePressure =
                aPlanet.getSurfacePressure() / Constants.EARTH_SURF_PRES_IN_MILLIBARS;
            final double ice = aPlanet.getIceCover() * 100.0;
            final double sgravity = aPlanet.getSurfaceGravity();

            evaluateGravity(sgravity);
            evaluateTemperature(relTemp);

            if (ice > 10.0)
            {
                /* 10% surface ice? */
                this.icy = true;
            }
            else
            {
                this.icy = false;
            }

            this.climate = ClimateEnum.NONE;
            this.clouds = CloudEnum.CLOUDLESS;
            this.atmosphere = AtmosphereEnum.AIRLESS;

            if (atmospherePressure < 0.001)
            {
                this.atmosphere = AtmosphereEnum.AIRLESS;
            }
            else
            {
                if (aPlanet.getType() != PlanetType.WATER)
                {
                    if (seas < 25.0)
                    {
                        this.climate = ClimateEnum.ARID;
                    }
                    else if (seas < 50.0)
                    {
                        this.climate = ClimateEnum.DRY;
                    }
                    else if (seas > 80.0)
                    {
                        this.climate = ClimateEnum.WET;
                    }
                }

                if (cloudPercent < 10.0)
                {
                    this.clouds = CloudEnum.CLOUDLESS;
                }
                else if (cloudPercent < 40.0)
                {
                    this.clouds = CloudEnum.FEW_CLOUDS;
                }
                else if (cloudPercent > 80.0)
                {
                    this.clouds = CloudEnum.CLOUDY;
                }
                else
                {
                    this.clouds = CloudEnum.NORMAL;
                }

                if (aPlanet.getSurfacePressure() < Constants.MIN_O2_IPP)
                {
                    this.atmosphere = AtmosphereEnum.TOO_THIN;
                }
                else if (atmospherePressure < 0.5)
                {
                    this.atmosphere = AtmosphereEnum.THIN;
                }
                else if (
                    atmospherePressure > (Constants.MAX_HABITABLE_PRESSURE / Constants.EARTH_SURF_PRES_IN_MILLIBARS))
                {
                    /* Dole, pp. 18-19 */
                    this.atmosphere = AtmosphereEnum.TOO_THICK;
                }
                else if (atmospherePressure > 2.0)
                {
                    this.atmosphere = AtmosphereEnum.THICK;
                }
                else if (aPlanet.getType() != PlanetType.TERRESTRIAL)
                {
                    // TODO: find out why terrestrial planets were excluded.
                    // it's in the original code and it doesn't seem to make
                    // sense.
                    
                    this.atmosphere = AtmosphereEnum.NORMAL;
                }
            }
        }
    }

    /**
     * Get an indication of the gravity on this planet
     *
     * @return An enumeration value representing the gravity
     */
    public PlanetEvaluator.GravityEnum getGravity()
    {
        return this.gravity;
    }

    /**
     * Get an indication of the temperature of this planet
     *
     * @return An enumeration value representing the temperature
     */
    public PlanetEvaluator.TemperatureEnum getTemperature()
    {
        return this.temperature;
    }

    /**
     * Get an indication of the atmosphere on this planet
     *
     * @return An enumeration value representing the atmosphere
     */
    public PlanetEvaluator.AtmosphereEnum getAtmosphere()
    {
        return this.atmosphere;
    }

    /**
     * Indicates whether this planet displays one face because it is
     * tidally locked.
     *
     * @return <pre>true</pre> if one face is displayed otherwise <pre>false</pre>
     */
    public boolean isOneFace()
    {
        boolean oneFace = false;

        if (
            ((int) this.planet.getDay() == (int) (this.planet.getOrbitalPeriod() * 24.0)))
        {
            oneFace = true;
        }

        return oneFace;
    }

    /** 
     * Indicates whether this planet is spin locked in a resonant period
     *
     * @return <pre>true</pre> if this planet is resonant spin locked
     */
     public boolean isSpinLocked()
     {
         return this.planet.isResonantPeriod();
     }

     /**
     * Indicates whether this planet is icy
     *
     * @return <pre>true</pre> if this is an icy planet otherwise <pre>false</pre>
     */
    public boolean isIcy()
    {
        return this.icy;
    }

    /**
     * Get an indication of the breathability of the atmosphere on this planet
     *
     * @return An enumeration value representing the breathability
     */
    public Breathability getBreathability()
    {
        return this.enviro.getBreathability(this.planet);
    }

    /**
     * Get an indication of the clouds for this planet
     *
     * @return An enumeration value representing the clouds
     */
    public PlanetEvaluator.CloudEnum getClouds()
    {
        return this.clouds;
    }

    /**
     * Get an indication of the climate for this planet
     *
     * @return An enumeration value representing the clouds
     */
    public ClimateEnum getClimate()
    {
        return this.climate;
    }
    
    /**
     * Indicates whether this planet is earthlike by combining several tests
     *
     * @return <pre>true</pre> if this is an earthlike planet otherwise <pre>false</pre>
     */
    public boolean isEarthLike()
    {
        boolean likeEarth = false;

        if (
            !PlanetType.isGasGiant(this.planet.getType()) &&
                (this.gravity == GravityEnum.NORMAL_G) &&
                (this.temperature == TemperatureEnum.NORMAL) &&
                (this.atmosphere == AtmosphereEnum.NORMAL) &&
                (this.climate == ClimateEnum.NORMAL) &&
                (this.clouds == CloudEnum.NORMAL))
        {
            likeEarth = true;
        }

        return likeEarth;
    }

    /**
     * Indicates whether this planet is a gas giant
     *
     * @return <pre>true</pre> if this is a gas giant otherwise <pre>false</pre>
     */
    public boolean isGasGiant()
    {
        return planet.getType().isGasGiant();
    }

    /**
     * Indicates whether this planet is a water planet
     *
     * @return <pre>true</pre> if this is a water planet otherwise <pre>false</pre>
     */
    public boolean isWaterWorld()
    {
        return planet.getType() == PlanetType.WATER;
    }

    /**
     * Indicates whether this planet has a boiling ocean
     *
     * @return <pre>true</pre> if the ocean is boiling otherwise <pre>false</pre>
     */
    public boolean isOceanBoiling()
    {
        return planet.getMaxTemperature() > planet.getBoilingPoint();            
    }
    
    /**
     * Get a list of the gases that are present in the atmosphere of the planet
     *
     * @return A list of chem table data for each gas that is present in 
     * significant amounts in the planetary atmosphere.
     */
    public List<Gas> getAtmosphericGases()
    {
        List<Gas> present = new ArrayList<Gas>();
        
        if (this.planet.getGases() != null)
        {
            Gases gases = new Gases();
        
            for (Gas g : this.planet.getGases())
            {
                if ((g.getSurfacePressure() / planet.getSurfacePressure() > .0005))
                {
                    present.add(g);
                }
            }
        }
        
        return present;
    }
    
    /**
     * Get a list of the gases that have molecular weights that can be
     * retained
     *
     * @return A list of retained elements
     */
    public List<ChemTable> getRetainedElements()
    {
        Gases gases = new Gases();
        
        Collection<ChemTable> allGases = gases.getAllGases();
        List<ChemTable> retained = new ArrayList<ChemTable>();
        
        for (ChemTable gas : allGases)
        {
            if (gas.getWeight() >= this.planet.getMolecularWeight())
            {
                retained.add(gas);
            }
        }
        
        return retained;
    }
    
    /**
     * Get a short description of the type of planet this object represents
     *
     * @return A string describing the planet
     */
    public String getPlanetShortDescription()
    {
        return describer.describePlanet(this);
    }

    /**
     * Indicate whether this planet should be included in a list of
     * terrestrial planets
     *
     * @return <pre>true</pre> if this is a terrestrial planet
     */
    public boolean isTerrestrialPlanet()
    {
        if (((this.enviro.getBreathability(planet) == Breathability.BREATHABLE) ||
             ((planet.getMaxTemperature() >= Constants.FREEZING_POINT_OF_WATER) &&
                  (planet.getMinTemperature() <= planet.getBoilingPoint()))) &&
                (planet.getType() != PlanetType.SUBSUBGASGIANT))
        {
            return true;
        }
        
        return false;
    }
}
