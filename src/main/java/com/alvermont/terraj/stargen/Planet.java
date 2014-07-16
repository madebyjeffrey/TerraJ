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
 * Created on 23 April 2006, 14:46
 */

package com.alvermont.terraj.stargen;

import java.util.List;

/**
 * An interface to be implemented by objects that hold details of planets.
 *
 * @author  martin
 * @version $Id: Planet.java,v 1.3 2006/07/06 06:58:33 martin Exp $
 */
public interface Planet
{
    /**
     * Getter for property a.
     * 
     * @return Value of property a.
     */
    double getA();

    /**
     * Getter for property albedo.
     * 
     * @return Value of property albedo.
     */
    double getAlbedo();

    /**
     * Getter for property axialTilt.
     * 
     * @return Value of property axialTilt.
     */
    double getAxialTilt();

    /**
     * Getter for property boilingPoint.
     * 
     * @return Value of property boilingPoint.
     */
    double getBoilingPoint();

    /**
     * Getter for property breathability.
     * 
     * @return Value of property breathability.
     */
    Breathability getBreathability();

    /**
     * Getter for property cloudCover.
     * 
     * @return Value of property cloudCover.
     */
    double getCloudCover();

    /**
     * Getter for property coreRadius.
     * 
     * @return Value of property coreRadius.
     */
    double getCoreRadius();

    /**
     * Getter for property day.
     * 
     * @return Value of property day.
     */
    double getDay();

    /**
     * Getter for property density.
     * 
     * @return Value of property density.
     */
    double getDensity();

    /**
     * Getter for property dustMass.
     * 
     * @return Value of property dustMass.
     */
    double getDustMass();

    /**
     * Getter for property e.
     * 
     * @return Value of property e.
     */
    double getE();

    /**
     * Getter for property escapeVelocity.
     * 
     * @return Value of property escapeVelocity.
     */
    double getEscapeVelocity();

    /**
     * Getter for property exosphericTemperature.
     * 
     * @return Value of property exosphericTemperature.
     */
    double getExosphericTemperature();

    /**
     * Getter for property firstMoon.
     * 
     * @return Value of property firstMoon.
     */
    Planet getFirstMoon();

    /**
     * Getter for property gasMass.
     * 
     * @return Value of property gasMass.
     */
    double getGasMass();

    /**
     * Getter for property gases.
     * 
     * @return Value of property gases.
     */
    List<Gas> getGases();

    /**
     * Getter for property greenhouseRise.
     * 
     * @return Value of property greenhouseRise.
     */
    double getGreenhouseRise();

    /**
     * Getter for property highTemperature.
     * 
     * @return Value of property highTemperature.
     */
    double getHighTemperature();

    /**
     * Getter for property hydrosphere.
     * 
     * @return Value of property hydrosphere.
     */
    double getHydrosphere();

    /**
     * Getter for property iceCover.
     * 
     * @return Value of property iceCover.
     */
    double getIceCover();

    /**
     * Getter for property lowTemperature.
     * 
     * @return Value of property lowTemperature.
     */
    double getLowTemperature();

    /**
     * Getter for property mass.
     * 
     * @return Value of property mass.
     */
    double getMass();

    /**
     * Getter for property maxTemperature.
     * 
     * @return Value of property maxTemperature.
     */
    double getMaxTemperature();

    /**
     * Getter for property minTemperature.
     * 
     * @return Value of property minTemperature.
     */
    double getMinTemperature();

    /**
     * Getter for property molecularWeight.
     * 
     * @return Value of property molecularWeight.
     */
    double getMolecularWeight();

    /**
     * Getter for property moons.
     * 
     * @return Value of property moons.
     */
    List<Planet> getMoons();

    /**
     * Getter for property name.
     * 
     * @return Value of property name.
     */
    String getName();

    /**
     * Getter for property nextPlanet.
     * 
     * @return Value of property nextPlanet.
     */
    Planet getNextPlanet();

    /**
     * Getter for property number.
     * 
     * @return Value of property number.
     */
    int getNumber();

    /**
     * Getter for property orbitZone.
     * 
     * @return Value of property orbitZone.
     */
    int getOrbitZone();

    /**
     * Getter for property orbPeriod.
     * 
     * @return Value of property orbPeriod.
     */
    double getOrbitalPeriod();

    /**
     * Getter for property primary.
     * 
     * @return Value of property primary.
     */
    Primary getPrimary();

    /**
     * Getter for property radius.
     * 
     * @return Value of property radius.
     */
    double getRadius();

    /**
     * Getter for property rmsVelocity.
     * 
     * @return Value of property rmsVelocity.
     */
    double getRmsVelocity();

    /**
     * Getter for property surfaceAcceleration.
     * 
     * @return Value of property surfaceAcceleration.
     */
    double getSurfaceAcceleration();

    /**
     * Getter for property surfaceGravity.
     * 
     * @return Value of property surfaceGravity.
     */
    double getSurfaceGravity();

    /**
     * Getter for property surfacePressure.
     * 
     * @return Value of property surfacePressure.
     */
    double getSurfacePressure();

    /**
     * Getter for property surfaceTemperature.
     * 
     * @return Value of property surfaceTemperature.
     */
    double getSurfaceTemperature();

    /**
     * Getter for property type.
     * 
     * @return Value of property type.
     */
    PlanetType getType();

    /**
     * Getter for property volatileGasInventory.
     * 
     * @return Value of property volatileGasInventory.
     */
    double getVolatileGasInventory();

    /**
     * Getter for property gasGiant.
     * 
     * @return Value of property gasGiant.
     */
    boolean isGasGiant();

    /**
     * Getter for property greenouseEffect.
     * 
     * @return Value of property greenouseEffect.
     */
    boolean isGreenhouseEffect();

    /**
     * Getter for property resonantPeriod.
     * 
     * @return Value of property resonantPeriod.
     */
    boolean isResonantPeriod();

    /**
     * Setter for property a.
     * 
     * @param a New value of property a.
     */
    void setA(double a);

    /**
     * Setter for property albedo.
     * 
     * @param albedo New value of property albedo.
     */
    void setAlbedo(double albedo);

    /**
     * Setter for property axialTilt.
     * 
     * @param axialTilt New value of property axialTilt.
     */
    void setAxialTilt(double axialTilt);

    /**
     * Setter for property boilingPoint.
     * 
     * @param boilingPoint New value of property boilingPoint.
     */
    void setBoilingPoint(double boilingPoint);

    /**
     * Setter for property breathability.
     * 
     * @param breathability New value of property breathability.
     */
    void setBreathability(Breathability breathability);

    /**
     * Setter for property cloudCover.
     * 
     * @param cloudCover New value of property cloudCover.
     */
    void setCloudCover(double cloudCover);

    /**
     * Setter for property coreRadius.
     * 
     * @param coreRadius New value of property coreRadius.
     */
    void setCoreRadius(double coreRadius);

    /**
     * Setter for property day.
     * 
     * @param day New value of property day.
     */
    void setDay(double day);

    /**
     * Setter for property density.
     * 
     * @param density New value of property density.
     */
    void setDensity(double density);

    /**
     * Setter for property dustMass.
     * 
     * @param dustMass New value of property dustMass.
     */
    void setDustMass(double dustMass);

    /**
     * Setter for property e.
     * 
     * @param e New value of property e.
     */
    void setE(double e);

    /**
     * Setter for property escapeVelocity.
     * 
     * @param escapeVelocity New value of property escapeVelocity.
     */
    void setEscapeVelocity(double escapeVelocity);

    /**
     * Setter for property exosphericTemperature.
     * 
     * @param exosphericTemperature New value of property exosphericTemperature.
     */
    void setExosphericTemperature(double exosphericTemperature);

    /**
     * Setter for property firstMoon.
     * 
     * @param firstMoon New value of property firstMoon.
     */
    void setFirstMoon(Planet firstMoon);

    /**
     * Setter for property gasGiant.
     * 
     * @param gasGiant New value of property gasGiant.
     */
    void setGasGiant(boolean gasGiant);

    /**
     * Setter for property gasMass.
     * 
     * @param gasMass New value of property gasMass.
     */
    void setGasMass(double gasMass);

    /**
     * Setter for property gases.
     * 
     * @param gases New value of property gases.
     */
    void setGases(List<Gas> gases);

    /**
     * Setter for property greenhouseEffect.
     * 
     * @param greenhouseEffect New value of property greenhouseEffect.
     */
    void setGreenhouseEffect(boolean greenhouseEffect);

    /**
     * Setter for property greenhouseRise.
     * 
     * @param greenhouseRise New value of property greenhouseRise.
     */
    void setGreenhouseRise(double greenhouseRise);

    /**
     * Setter for property highTemperature.
     * 
     * @param highTemperature New value of property highTemperature.
     */
    void setHighTemperature(double highTemperature);

    /**
     * Setter for property hydrosphere.
     * 
     * @param hydrosphere New value of property hydrosphere.
     */
    void setHydrosphere(double hydrosphere);

    /**
     * Setter for property iceCover.
     * 
     * @param iceCover New value of property iceCover.
     */
    void setIceCover(double iceCover);

    /**
     * Setter for property lowTemperature.
     * 
     * @param lowTemperature New value of property lowTemperature.
     */
    void setLowTemperature(double lowTemperature);

    /**
     * Setter for property mass.
     * 
     * @param mass New value of property mass.
     */
    void setMass(double mass);

    /**
     * Setter for property maxTemperature.
     * 
     * @param maxTemperature New value of property maxTemperature.
     */
    void setMaxTemperature(double maxTemperature);

    /**
     * Setter for property minTemperature.
     * 
     * @param minTemperature New value of property minTemperature.
     */
    void setMinTemperature(double minTemperature);

    /**
     * Setter for property molecularWeight.
     * 
     * @param molecularWeight New value of property molecularWeight.
     */
    void setMolecularWeight(double molecularWeight);

    /**
     * Setter for property moons.
     * 
     * @param moons New value of property moons.
     */
    void setMoons(List<Planet> moons);

    /**
     * Setter for property name.
     * 
     * @param name New value of property name.
     */
    void setName(String name);

    /**
     * Setter for property nextPlanet.
     * 
     * @param nextPlanet New value of property nextPlanet.
     */
    void setNextPlanet(Planet nextPlanet);

    /**
     * Setter for property number.
     * 
     * @param number New value of property number.
     */
    void setNumber(int number);

    /**
     * Setter for property orbitZone.
     * 
     * @param orbitZone New value of property orbitZone.
     */
    void setOrbitZone(int orbitZone);

    /**
     * Setter for property orbPeriod.
     * 
     * 
     * @param orbitalPeriod New value of property orbPeriod.
     */
    void setOrbitalPeriod(double orbitalPeriod);

    /**
     * Setter for property primary.
     * 
     * @param primary New value of property primary.
     */
    void setPrimary(Primary primary);

    /**
     * Setter for property radius.
     * 
     * @param radius New value of property radius.
     */
    void setRadius(double radius);

    /**
     * Setter for property resonantPeriod.
     * 
     * @param resonantPeriod New value of property resonantPeriod.
     */
    void setResonantPeriod(boolean resonantPeriod);

    /**
     * Setter for property rmsVelocity.
     * 
     * @param rmsVelocity New value of property rmsVelocity.
     */
    void setRmsVelocity(double rmsVelocity);

    /**
     * Setter for property surfaceAcceleration.
     * 
     * @param surfaceAcceleration New value of property surfaceAcceleration.
     */
    void setSurfaceAcceleration(double surfaceAcceleration);

    /**
     * Setter for property surfaceGravity.
     * 
     * @param surfaceGravity New value of property surfaceGravity.
     */
    void setSurfaceGravity(double surfaceGravity);

    /**
     * Setter for property surfacePressure.
     * 
     * @param surfacePressure New value of property surfacePressure.
     */
    void setSurfacePressure(double surfacePressure);

    /**
     * Setter for property surfaceTemperature.
     * 
     * @param surfaceTemperature New value of property surfaceTemperature.
     */
    void setSurfaceTemperature(double surfaceTemperature);

    /**
     * Setter for property type.
     * 
     * @param type New value of property type.
     */
    void setType(PlanetType type);

    /**
     * Setter for property volatileGasInventory.
     * 
     * @param volatileGasInventory New value of property volatileGasInventory.
     */
    void setVolatileGasInventory(double volatileGasInventory);
}
