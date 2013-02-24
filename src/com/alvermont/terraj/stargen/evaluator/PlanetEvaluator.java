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
 * PlanetEvaluator.java
 *
 * Created on December 26, 2005, 1:42 PM
 *
 */
package com.alvermont.terraj.stargen.evaluator;

import com.alvermont.terraj.stargen.*;
import java.util.List;


/**
 * Interface for classes that evaluate planetary conditions
 *
 * @author martin
 * @version $Id: PlanetEvaluator.java,v 1.5 2006/07/06 06:58:36 martin Exp $
 */
public interface PlanetEvaluator
{
    /**
     * Evaluate a planet and set the variables to the corresponding
     * indications. After this call use the get methods to retrieve the
     * result of the evaluation
     *
     * @param planet The planet to be evaluated
     */
    public void evaluate(Planet planet);

    /**
     * Get an indication of the gravity on this planet
     *
     * @return An enumeration value representing the gravity
     */
    public GravityEnum getGravity();

    /**
     * Get an indication of the temperature of this planet
     *
     * @return An enumeration value representing the temperature
     */
    public TemperatureEnum getTemperature();

    /**
     * Get an indication of the atmosphere on this planet
     *
     * @return An enumeration value representing the atmosphere
     */
    public AtmosphereEnum getAtmosphere();

    /**
     * Get an indication of the clouds for this planet
     *
     * @return An enumeration value representing the clouds
     */
    public CloudEnum getClouds();

    /**
     * Get an indication of the climate for this planet
     *
     * @return An enumeration value representing the clouds
     */
    public ClimateEnum getClimate();

    /**
     * Get an indication of the breathability of the atmosphere on this planet
     *
     * @return An enumeration value representing the breathability
     */
    public Breathability getBreathability();

    /**
     * Indicates whether this planet displays one face because it is
     * tidally locked.
     *
     * @return <pre>true</pre> if one face is displayed otherwise <pre>false</pre>
     */
    public boolean isOneFace();
    
    /** 
     * Indicates whether this planet is spin locked in a resonant period
     *
     * @return <pre>true</pre> if this planet is resonant spin locked
     */
     public boolean isSpinLocked();
     
    /**
     * Indicates whether this planet is icy
     *
     * @return <pre>true</pre> if this is an icy planet otherwise <pre>false</pre>
     */
    public boolean isIcy();

    /**
     * Indicates whether this planet is earthlike by combining several tests
     *
     * @return <pre>true</pre> if this is an earthlike planet otherwise <pre>false</pre>
     */
    public boolean isEarthLike();

    /**
     * Indicates whether this planet is a gas giant
     *
     * @return <pre>true</pre> if this is a gas giant otherwise <pre>false</pre>
     */
    public boolean isGasGiant();

    /**
     * Indicates whether this planet is a water planet
     *
     * @return <pre>true</pre> if this is a water planet otherwise <pre>false</pre>
     */
    public boolean isWaterWorld();

    /**
     * Indicates whether this planet has a boiling ocean
     *
     * @return <pre>true</pre> if the ocean is boiling otherwise <pre>false</pre>
     */
    public boolean isOceanBoiling();

    /**
     * Get a list of the gases that are present in the atmosphere of the planet
     *
     * @return A list of chem table data for each gas that is present in 
     * significant amounts in the planetary atmosphere.
     */
    public List<Gas> getAtmosphericGases();
    
    /**
     * Get a list of the gases that have molecular weights that can be
     * retained
     *
     * @return A list of retained elements
     */
    public List<ChemTable> getRetainedElements();

    /**
     * Indicate whether this planet should be included in a list of
     * terrestrial planets
     *
     * @return <pre>true</pre> if this is a terrestrial planet
     */
    public boolean isTerrestrialPlanet();
    
    /**
     * Get a short description of the type of planet this object represents
     *
     * @return A string describing the planet
     */
    public String getPlanetShortDescription();
    
    /** Enumerated type representing gravity */
    public enum GravityEnum
    {
        /** Value indicating a planet with low gravity */
        LOW_G,
        /** Value indicating a planet with 'normal' gravity e.g. similar to Earth */
        NORMAL_G, 
        /** Value indicating a planet with high gravity */
        HIGH_G;
    }

    /** Enumerated type representing temperature */
    public enum TemperatureEnum
    {
        /** Value indicating a planet with much colder than Earth temperatures */
        COLD,
        /** Value indicating a planet with cool temperatures */
        COOL, 
        /** Value indicating a planet with Earth normal temperatures */
        NORMAL, 
        /** Value indicating a planet with warmer than Earth temperatures */
        WARM, 
        /** Value indicating a planet with hot temperatures */
        HOT;
    }

    /** Enumerated type representing atmosphere */
    public enum AtmosphereEnum
    {
        /** Value indicating a planet with no atmosphere */
        AIRLESS,
        /** Value indicating a planet with a much thinner atmosphere than Earth */
        TOO_THIN, 
        /** Value indicating a planet with a thinner atmosphere than Earth */
        THIN, 
        /** Value indicating a planet with a similar thickness of atmosphere to Earth */
        NORMAL, 
        /** Value indicating a planet with a thicker than Earth atmosphere */
        THICK, 
        /** Value indicating a planet with a much thicker than Earth atmosphere */
        TOO_THICK;
    }

    /** Enumerated type representing climate */
    public enum ClimateEnum
    {
        /** Value indicating a planet with no climate */
        NONE,
        /** Value indicating a planet with a very dry climate */
        ARID, 
        /** Value indicating a planet with a similar to Earth climate */
        NORMAL, 
        /** Value indicating a planet with a drier than Earth climate */
        DRY, 
        /** Value indicating a planet with a wetter than Earth climate */
        WET;
    }

    /** Enumerated type representing clouds */
    public enum CloudEnum
    {
        /** Value indicating a planet with no clouds */
        CLOUDLESS,
        /** Value indicating a planet with less than Earth clouds */
        FEW_CLOUDS, 
        /** Value indicating a planet with similar cloud cover to Earth */
        NORMAL, 
        /** Value indicating a planet much cloudier than Earth */
        CLOUDY;
    }
}
