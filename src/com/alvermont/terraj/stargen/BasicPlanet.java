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
 * Planet.java
 *
 * Created on December 21, 2005, 10:26 AM
 */
package com.alvermont.terraj.stargen;

import java.util.List;

/**
 * Class representing a planet
 * 
 * 
 * @author martin
 * @version $Id: BasicPlanet.java,v 1.3 2006/07/06 06:58:33 martin Exp $
 */
public class BasicPlanet implements Planet
{
    /**
     * Holds value of property number.
     */
    private int number;

    /**
     * Holds value of property a.
     */
    private double a;

    /**
     * Holds value of property e.
     */
    private double e;

    /**
     * Holds value of property axialTilt.
     */
    private double axialTilt;

    /**
     * Holds value of property mass.
     */
    private double mass;

    /**
     * Holds value of property gasGiant.
     */
    private boolean gasGiant;

    /**
     * Holds value of property dustMass.
     */
    private double dustMass;

    /**
     * Holds value of property gasMass.
     */
    private double gasMass;

    /**
     * Holds value of property coreRadius.
     */
    private double coreRadius;

    /**
     * Holds value of property radius.
     */
    private double radius;

    /**
     * Holds value of property orbitZone.
     */
    private int orbitZone;

    /**
     * Holds value of property density.
     */
    private double density;

    /**
     * Holds value of property orbitalPeriod.
     */
    private double orbitalPeriod;

    /**
     * Holds value of property day.
     */
    private double day;

    /**
     * Holds value of property resonantPeriod.
     */
    private boolean resonantPeriod;

    /**
     * Holds value of property escapeVelocity.
     */
    private double escapeVelocity;

    /**
     * Holds value of property surfaceAcceleration.
     */
    private double surfaceAcceleration;

    /**
     * Holds value of property surfaceGravity.
     */
    private double surfaceGravity;

    /**
     * Holds value of property rmsVelocity.
     */
    private double rmsVelocity;

    /**
     * Holds value of property molecularWeight.
     */
    private double molecularWeight;

    /**
     * Holds value of property volatileGasInventory.
     */
    private double volatileGasInventory;

    /**
     * Holds value of property surfacePressure.
     */
    private double surfacePressure;

    /**
     * Holds value of property greenhouseEffect.
     */
    private boolean greenhouseEffect;

    /**
     * Holds value of property boilingPoint.
     */
    private double boilingPoint;

    /**
     * Holds value of property albedo.
     */
    private double albedo;

    /**
     * Holds value of property exosphericTemperature.
     */
    private double exosphericTemperature;

    /**
     * Holds value of property surfaceTemperature.
     */
    private double surfaceTemperature;

    /**
     * Holds value of property greenhouseRise.
     */
    private double greenhouseRise;

    /**
     * Holds value of property highTemperature.
     */
    private double highTemperature;

    /**
     * Holds value of property lowTemperature.
     */
    private double lowTemperature;

    /**
     * Holds value of property maxTemperature.
     */
    private double maxTemperature;

    /**
     * Holds value of property minTemperature.
     */
    private double minTemperature;

    /**
     * Holds value of property hydrosphere.
     */
    private double hydrosphere;

    /**
     * Holds value of property cloudCover.
     */
    private double cloudCover;

    /**
     * Holds value of property iceCover.
     */
    private double iceCover;

    /**
     * Holds value of property primary.
     */
    private Primary primary;

    /**
     * Holds value of property gases.
     */
    private java.util.List<Gas> gases;

    /**
     * Holds value of property type.
     */
    private PlanetType type;

    /**
     * Holds value of property moons.
     */
    private List<Planet> moons;

    /**
     * Holds value of property nextPlanet.
     */
    private Planet nextPlanet;

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property breathability.
     */
    private Breathability breathability;

    /**
     * Holds value of property firstMoon.
     */
    private Planet firstMoon;

    /**
     * Creates a new instance of Planet
     */
    public BasicPlanet()
    {
    }

    /**
     * Getter for property number.
     * @return Value of property number.
     */
    public int getNumber()
    {
        return this.number;
    }

    /**
     * Setter for property number.
     * @param number New value of property number.
     */
    public void setNumber(int number)
    {
        this.number = number;
    }

    /**
     * Getter for property a.
     * @return Value of property a.
     */
    public double getA()
    {
        return this.a;
    }

    /**
     * Setter for property a.
     * @param a New value of property a.
     */
    public void setA(double a)
    {
        this.a = a;
    }

    /**
     * Getter for property e.
     * @return Value of property e.
     */
    public double getE()
    {
        return this.e;
    }

    /**
     * Setter for property e.
     * @param e New value of property e.
     */
    public void setE(double e)
    {
        this.e = e;
    }

    /**
     * Getter for property axialTilt.
     * @return Value of property axialTilt.
     */
    public double getAxialTilt()
    {
        return this.axialTilt;
    }

    /**
     * Setter for property axialTilt.
     * @param axialTilt New value of property axialTilt.
     */
    public void setAxialTilt(double axialTilt)
    {
        this.axialTilt = axialTilt;
    }

    /**
     * Getter for property mass.
     * @return Value of property mass.
     */
    public double getMass()
    {
        return this.mass;
    }

    /**
     * Setter for property mass.
     * @param mass New value of property mass.
     */
    public void setMass(double mass)
    {
        this.mass = mass;
    }

    /**
     * Getter for property gasGiant.
     * @return Value of property gasGiant.
     */
    public boolean isGasGiant()
    {
        return this.gasGiant;
    }

    /**
     * Setter for property gasGiant.
     * @param gasGiant New value of property gasGiant.
     */
    public void setGasGiant(boolean gasGiant)
    {
        this.gasGiant = gasGiant;
    }

    /**
     * Getter for property dustMass.
     * @return Value of property dustMass.
     */
    public double getDustMass()
    {
        return this.dustMass;
    }

    /**
     * Setter for property dustMass.
     * @param dustMass New value of property dustMass.
     */
    public void setDustMass(double dustMass)
    {
        this.dustMass = dustMass;
    }

    /**
     * Getter for property gasMass.
     * @return Value of property gasMass.
     */
    public double getGasMass()
    {
        return this.gasMass;
    }

    /**
     * Setter for property gasMass.
     * @param gasMass New value of property gasMass.
     */
    public void setGasMass(double gasMass)
    {
        this.gasMass = gasMass;
    }

    /**
     * Getter for property coreRadius.
     * @return Value of property coreRadius.
     */
    public double getCoreRadius()
    {
        return this.coreRadius;
    }

    /**
     * Setter for property coreRadius.
     * @param coreRadius New value of property coreRadius.
     */
    public void setCoreRadius(double coreRadius)
    {
        this.coreRadius = coreRadius;
    }

    /**
     * Getter for property radius.
     * @return Value of property radius.
     */
    public double getRadius()
    {
        return this.radius;
    }

    /**
     * Setter for property radius.
     * @param radius New value of property radius.
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    /**
     * Getter for property orbitZone.
     * @return Value of property orbitZone.
     */
    public int getOrbitZone()
    {
        return this.orbitZone;
    }

    /**
     * Setter for property orbitZone.
     * @param orbitZone New value of property orbitZone.
     */
    public void setOrbitZone(int orbitZone)
    {
        this.orbitZone = orbitZone;
    }

    /**
     * Getter for property density.
     * @return Value of property density.
     */
    public double getDensity()
    {
        return this.density;
    }

    /**
     * Setter for property density.
     * @param density New value of property density.
     */
    public void setDensity(double density)
    {
        this.density = density;
    }

    /**
     * Getter for property orbPeriod.
     * @return Value of property orbPeriod.
     */
    public double getOrbitalPeriod()
    {
        return this.orbitalPeriod;
    }

    /**
     * Setter for property orbPeriod.
     *
     * @param orbitalPeriod New value of property orbPeriod.
     */
    public void setOrbitalPeriod(double orbitalPeriod)
    {
        this.orbitalPeriod = orbitalPeriod;
    }

    /**
     * Getter for property day.
     * @return Value of property day.
     */
    public double getDay()
    {
        return this.day;
    }

    /**
     * Setter for property day.
     * @param day New value of property day.
     */
    public void setDay(double day)
    {
        this.day = day;
    }

    /**
     * Getter for property resonantPeriod.
     * @return Value of property resonantPeriod.
     */
    public boolean isResonantPeriod()
    {
        return this.resonantPeriod;
    }

    /**
     * Setter for property resonantPeriod.
     * @param resonantPeriod New value of property resonantPeriod.
     */
    public void setResonantPeriod(boolean resonantPeriod)
    {
        this.resonantPeriod = resonantPeriod;
    }

    /**
     * Getter for property escapeVelocity.
     * @return Value of property escapeVelocity.
     */
    public double getEscapeVelocity()
    {
        return this.escapeVelocity;
    }

    /**
     * Setter for property escapeVelocity.
     * @param escapeVelocity New value of property escapeVelocity.
     */
    public void setEscapeVelocity(double escapeVelocity)
    {
        this.escapeVelocity = escapeVelocity;
    }

    /**
     * Getter for property surfaceAcceleration.
     * @return Value of property surfaceAcceleration.
     */
    public double getSurfaceAcceleration()
    {
        return this.surfaceAcceleration;
    }

    /**
     * Setter for property surfaceAcceleration.
     * @param surfaceAcceleration New value of property surfaceAcceleration.
     */
    public void setSurfaceAcceleration(double surfaceAcceleration)
    {
        this.surfaceAcceleration = surfaceAcceleration;
    }

    /**
     * Getter for property surfaceGravity.
     * @return Value of property surfaceGravity.
     */
    public double getSurfaceGravity()
    {
        return this.surfaceGravity;
    }

    /**
     * Setter for property surfaceGravity.
     * @param surfaceGravity New value of property surfaceGravity.
     */
    public void setSurfaceGravity(double surfaceGravity)
    {
        this.surfaceGravity = surfaceGravity;
    }

    /**
     * Getter for property rmsVelocity.
     * @return Value of property rmsVelocity.
     */
    public double getRmsVelocity()
    {
        return this.rmsVelocity;
    }

    /**
     * Setter for property rmsVelocity.
     * @param rmsVelocity New value of property rmsVelocity.
     */
    public void setRmsVelocity(double rmsVelocity)
    {
        this.rmsVelocity = rmsVelocity;
    }

    /**
     * Getter for property molecularWeight.
     * @return Value of property molecularWeight.
     */
    public double getMolecularWeight()
    {
        return this.molecularWeight;
    }

    /**
     * Setter for property molecularWeight.
     * @param molecularWeight New value of property molecularWeight.
     */
    public void setMolecularWeight(double molecularWeight)
    {
        this.molecularWeight = molecularWeight;
    }

    /**
     * Getter for property volatileGasInventory.
     * @return Value of property volatileGasInventory.
     */
    public double getVolatileGasInventory()
    {
        return this.volatileGasInventory;
    }

    /**
     * Setter for property volatileGasInventory.
     * @param volatileGasInventory New value of property volatileGasInventory.
     */
    public void setVolatileGasInventory(double volatileGasInventory)
    {
        this.volatileGasInventory = volatileGasInventory;
    }

    /**
     * Getter for property surfacePressure.
     * @return Value of property surfacePressure.
     */
    public double getSurfacePressure()
    {
        return this.surfacePressure;
    }

    /**
     * Setter for property surfacePressure.
     * @param surfacePressure New value of property surfacePressure.
     */
    public void setSurfacePressure(double surfacePressure)
    {
        this.surfacePressure = surfacePressure;
    }

    /**
     * Getter for property greenhouseEffect.
     * @return Value of property greenhouseEffect.
     */
    public boolean isGreenhouseEffect()
    {
        return this.greenhouseEffect;
    }

    /**
     * Setter for property greenhouseEffect.
     * @param greenhouseEffect New value of property greenouseEffect.
     */
    public void setGreenhouseEffect(boolean greenhouseEffect)
    {
        this.greenhouseEffect = greenhouseEffect;
    }

    /**
     * Getter for property boilingPoint.
     * @return Value of property boilingPoint.
     */
    public double getBoilingPoint()
    {
        return this.boilingPoint;
    }

    /**
     * Setter for property boilingPoint.
     * @param boilingPoint New value of property boilingPoint.
     */
    public void setBoilingPoint(double boilingPoint)
    {
        this.boilingPoint = boilingPoint;
    }

    /**
     * Getter for property albedo.
     * @return Value of property albedo.
     */
    public double getAlbedo()
    {
        return this.albedo;
    }

    /**
     * Setter for property albedo.
     * @param albedo New value of property albedo.
     */
    public void setAlbedo(double albedo)
    {
        this.albedo = albedo;
    }

    /**
     * Getter for property exosphericTemperature.
     * @return Value of property exosphericTemperature.
     */
    public double getExosphericTemperature()
    {
        return this.exosphericTemperature;
    }

    /**
     * Setter for property exosphericTemperature.
     * @param exosphericTemperature New value of property exosphericTemperature.
     */
    public void setExosphericTemperature(double exosphericTemperature)
    {
        this.exosphericTemperature = exosphericTemperature;
    }

    /**
     * Getter for property surfaceTemperature.
     * @return Value of property surfaceTemperature.
     */
    public double getSurfaceTemperature()
    {
        return this.surfaceTemperature;
    }

    /**
     * Setter for property surfaceTemperature.
     * @param surfaceTemperature New value of property surfaceTemperature.
     */
    public void setSurfaceTemperature(double surfaceTemperature)
    {
        this.surfaceTemperature = surfaceTemperature;
    }

    /**
     * Getter for property greenhouseRise.
     * @return Value of property greenhouseRise.
     */
    public double getGreenhouseRise()
    {
        return this.greenhouseRise;
    }

    /**
     * Setter for property greenhouseRise.
     * @param greenhouseRise New value of property greenhouseRise.
     */
    public void setGreenhouseRise(double greenhouseRise)
    {
        this.greenhouseRise = greenhouseRise;
    }

    /**
     * Getter for property highTemperature.
     * @return Value of property highTemperature.
     */
    public double getHighTemperature()
    {
        return this.highTemperature;
    }

    /**
     * Setter for property highTemperature.
     * @param highTemperature New value of property highTemperature.
     */
    public void setHighTemperature(double highTemperature)
    {
        this.highTemperature = highTemperature;
    }

    /**
     * Getter for property lowTemperature.
     * @return Value of property lowTemperature.
     */
    public double getLowTemperature()
    {
        return this.lowTemperature;
    }

    /**
     * Setter for property lowTemperature.
     * @param lowTemperature New value of property lowTemperature.
     */
    public void setLowTemperature(double lowTemperature)
    {
        this.lowTemperature = lowTemperature;
    }

    /**
     * Getter for property maxTemperature.
     * @return Value of property maxTemperature.
     */
    public double getMaxTemperature()
    {
        return this.maxTemperature;
    }

    /**
     * Setter for property maxTemperature.
     * @param maxTemperature New value of property maxTemperature.
     */
    public void setMaxTemperature(double maxTemperature)
    {
        this.maxTemperature = maxTemperature;
    }

    /**
     * Getter for property minTemperature.
     * @return Value of property minTemperature.
     */
    public double getMinTemperature()
    {
        return this.minTemperature;
    }

    /**
     * Setter for property minTemperature.
     * @param minTemperature New value of property minTemperature.
     */
    public void setMinTemperature(double minTemperature)
    {
        this.minTemperature = minTemperature;
    }

    /**
     * Getter for property hydrosphere.
     * @return Value of property hydrosphere.
     */
    public double getHydrosphere()
    {
        return this.hydrosphere;
    }

    /**
     * Setter for property hydrosphere.
     * @param hydrosphere New value of property hydrosphere.
     */
    public void setHydrosphere(double hydrosphere)
    {
        this.hydrosphere = hydrosphere;
    }

    /**
     * Getter for property cloudCover.
     * @return Value of property cloudCover.
     */
    public double getCloudCover()
    {
        return this.cloudCover;
    }

    /**
     * Setter for property cloudCover.
     * @param cloudCover New value of property cloudCover.
     */
    public void setCloudCover(double cloudCover)
    {
        this.cloudCover = cloudCover;
    }

    /**
     * Getter for property iceCover.
     * @return Value of property iceCover.
     */
    public double getIceCover()
    {
        return this.iceCover;
    }

    /**
     * Setter for property iceCover.
     * @param iceCover New value of property iceCover.
     */
    public void setIceCover(double iceCover)
    {
        this.iceCover = iceCover;
    }

    /**
     * Getter for property primary.
     * @return Value of property primary.
     */
    public Primary getPrimary()
    {
        return this.primary;
    }

    /**
     * Setter for property primary.
     * @param primary New value of property primary.
     */
    public void setPrimary(Primary primary)
    {
        this.primary = primary;
    }

    /**
     * Getter for property gases.
     * @return Value of property gases.
     */
    public java.util.List<Gas> getGases()
    {
        return this.gases;
    }

    /**
     * Setter for property gases.
     * @param gases New value of property gases.
     */
    public void setGases(java.util.List<Gas> gases)
    {
        this.gases = gases;
    }

    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public PlanetType getType()
    {
        return this.type;
    }

    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(PlanetType type)
    {
        this.type = type;
    }

    /**
     * Getter for property moons.
     * @return Value of property moons.
     */
    public List<Planet> getMoons()
    {
        return this.moons;
    }

    /**
     * Setter for property moons.
     * @param moons New value of property moons.
     */
    public void setMoons(List<Planet> moons)
    {
        this.moons = moons;
    }

    /**
     * Getter for property nextPlanet.
     * @return Value of property nextPlanet.
     */
    public Planet getNextPlanet()
    {
        return this.nextPlanet;
    }

    /**
     * Setter for property nextPlanet.
     * @param nextPlanet New value of property nextPlanet.
     */
    public void setNextPlanet(Planet nextPlanet)
    {
        this.nextPlanet = nextPlanet;
    }

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter for property breathability.
     * @return Value of property breathability.
     */
    public Breathability getBreathability()
    {
        return this.breathability;
    }

    /**
     * Setter for property breathability.
     * @param breathability New value of property breathability.
     */
    public void setBreathability(Breathability breathability)
    {
        this.breathability = breathability;
    }

    /**
     * Getter for property firstMoon.
     * @return Value of property firstMoon.
     */
    public Planet getFirstMoon()
    {
        return this.firstMoon;
    }

    /**
     * Setter for property firstMoon.
     * @param firstMoon New value of property firstMoon.
     */
    public void setFirstMoon(Planet firstMoon)
    {
        this.firstMoon = firstMoon;
    }
}
