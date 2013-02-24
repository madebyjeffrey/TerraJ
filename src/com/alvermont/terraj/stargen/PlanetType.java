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
 * PlanetType.java
 *
 * Created on 21 December 2005, 21:09
 */
package com.alvermont.terraj.stargen;

import java.util.EnumMap;

/**
 * Enumeration for planetary types.
 *
 * @author Martin
 */
public enum PlanetType
{
    /** Value indicating an unknown planet type */
    UNKNOWN,
    /** Value indicating a rocky planet type */
    ROCK, 
    /** Value indicating a venusian planet type */
    VENUSIAN, 
    /** Value indicating a terrestrial planet type */
    TERRESTRIAL, 
    /** Value indicating a gas giant planet type */
    GASGIANT, 
    /** Value indicating a martian planet type */
    MARTIAN, 
    /** Value indicating a water planet type */
    WATER, 
    /** Value indicating an ice planet type */
    ICE, 
    /** Value indicating a sub gas giant planet type */
    SUBGASGIANT, 
    /** Value indicating a smaller than sub gas giant planet type */
    SUBSUBGASGIANT, 
    /** Value indicating an asteroid  planet type */
    ASTEROIDS,
    /** Value indicating 1 face (tidal lock) */
    ONEFACE;
    
    /** Map from planet types to names */
    private static EnumMap<PlanetType, String> printMap;

    /** Map from planet types to texture names. Note this is not currently
     * used by the Java code but is here for completeness.
     */
    private static EnumMap<PlanetType, String> textureMap;

    static
    {
        printMap = new EnumMap<PlanetType, String>(PlanetType.class);
        textureMap = new EnumMap<PlanetType, String>(PlanetType.class);

        // map of types to descriptions
        printMap.put(UNKNOWN, "Unknown");
        printMap.put(ROCK, "Rock");
        printMap.put(VENUSIAN, "Venusian");
        printMap.put(TERRESTRIAL, "Terrestrial");
        printMap.put(GASGIANT, "Jovian");
        printMap.put(MARTIAN, "Martian");
        printMap.put(WATER, "Water");
        printMap.put(ICE, "Ice");
        printMap.put(SUBGASGIANT, "Sub-Jovian");
        printMap.put(SUBSUBGASGIANT, "GasDwarf");
        printMap.put(ASTEROIDS, "Asteroids");
        printMap.put(ONEFACE, "1Face");

        // map of types to textures
        textureMap.put(UNKNOWN, "x.jpg");
        textureMap.put(ROCK, "callisto.jpg");
        textureMap.put(VENUSIAN, "venuslike.jpg");
        textureMap.put(TERRESTRIAL, "earthlike.jpg");
        textureMap.put(GASGIANT, "jupiterlike.jpg");
        textureMap.put(MARTIAN, "mars.jpg");
        textureMap.put(WATER, "x.jpg");
        textureMap.put(ICE, "pluto.jpg");
        textureMap.put(SUBGASGIANT, "gasgiant.jpg");
        textureMap.put(SUBSUBGASGIANT, "x.jpg");
        textureMap.put(ASTEROIDS, "asteroid.jpg");
        textureMap.put(ONEFACE, "x.jpg");
    }

    /**
     * Get the printable name of a planet type
     *
     * @param planet The type of planet to get the name for
     * @return The name of the type as a string
     */
    public String getPrintText(PlanetType planet)
    {
        return printMap.get(planet);
    }

    /**
     * Get the printable name of this planet type
     *
     * @return The name of the type as a string
     */
    public String getPrintText()
    {
        return printMap.get(this);
    }

    /**
     * Get the texture of a planet type
     *
     * @param planet The type of planet to get the texture for
     * @return The texture name of the type as a string
     */
    public String getTexture(PlanetType planet)
    {
        return textureMap.get(planet);
    }
    
    /**
     * Get the texture of this planet type
     *
     * @return The texture name of this type as a string
     */
    public String getTexture()
    {
        return textureMap.get(this);
    }
    
    /**
     * Test whether this planet type represents one of the forms of gas giants
     *
     * @return <pre>true</pre> if this planet type represents a gas giant
     */
    public boolean isGasGiant()
    {
        if (this == GASGIANT || this == SUBGASGIANT || this == SUBSUBGASGIANT)
        {
            return true;
        }
        
        return false;
    }

    /**
     * Test whether a planet type represents one of the forms of gas giants
     *
     * @param planet A planet type to be tested
     * @return <pre>true</pre> if this planet type represents a gas giant
     */
    public static boolean isGasGiant(PlanetType planet)
    {
        switch (planet)
        {
            case GASGIANT:
            case SUBGASGIANT:
            case SUBSUBGASGIANT:
                return true;

            default:
                return false;
        }
    }
}
