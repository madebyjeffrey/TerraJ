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
 * Primary.java
 *
 * Created on 15 January 2006, 22:00
 */
package com.alvermont.terraj.stargen;

import java.util.List;

/**
 * Interface that describes a star
 *
 * @author  martin
 * @version $Id: Primary.java,v 1.5 2006/07/06 06:58:33 martin Exp $
 */
public interface Primary
{
    /**
     * Getter for property absoluteMagnitude.
     *
     * @return Value of property absoluteMagnitude.
     */
    double getAbsoluteMagnitude();

    /**
     * Getter for property age.
     *
     * @return Value of property age.
     */
    double getAge();

    /**
     * Getter for property innermostPlanet.
     *
     * @return Value of property innermostPlanet.
     */
    Planet getInnermostPlanet();

    /**
     * Getter for property life.
     *
     * @return Value of property life.
     */
    double getLife();

    /**
     * Getter for property luminosity.
     *
     * @return Value of property luminosity.
     */
    double getLuminosity();

    /**
     * Getter for property luminosityClass.
     *
     * @return Value of property luminosityClass.
     */
    String getLuminosityClass();

    /**
     * Getter for property mass.
     *
     * @return Value of property mass.
     */
    double getMass();

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    String getName();

    /**
     * Getter for property planets.
     *
     * @return Value of property planets.
     */
    List<Planet> getPlanets();

    /**
     * Getter for property r_ecosphere.
     *
     * @return Value of property r_ecosphere.
     */
    double getREcosphere();

    /**
     * Getter for property r_ecosphere_inner.
     *
     * @return Value of property r_ecosphere_inner.
     */
    double getREcosphereInner();

    /**
     * Getter for property r_ecosphere_outer.
     *
     * @return Value of property r_ecosphere_outer.
     */
    double getREcosphereOuter();

    /**
     * Getter for property spectralClass.
     *
     * @return Value of property spectralClass.
     */
    String getSpectralClass();

    /**
     * Getter for property spectralSubclass.
     *
     * @return Value of property spectralSubclass.
     */
    int getSpectralSubclass();

    /**
     * Setter for property absoluteMagnitude.
     *
     * @param absoluteMagnitude New value of property absoluteMagnitude.
     */
    void setAbsoluteMagnitude(double absoluteMagnitude);

    /**
     * Setter for property age.
     *
     * @param age New value of property age.
     */
    void setAge(double age);

    /**
     * Setter for property innermostPlanet.
     *
     * @param innermostPlanet New value of property innermostPlanet.
     */
    void setInnermostPlanet(Planet innermostPlanet);

    /**
     * Setter for property life.
     *
     * @param life New value of property life.
     */
    void setLife(double life);

    /**
     * Setter for property luminosity.
     *
     * @param luminosity New value of property luminosity.
     */
    void setLuminosity(double luminosity);

    /**
     * Setter for property luminosityClass.
     *
     * @param luminosityClass New value of property luminosityClass.
     */
    void setLuminosityClass(String luminosityClass);

    /**
     * Setter for property mass.
     *
     * @param mass New value of property mass.
     */
    void setMass(double mass);

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    void setName(String name);

    /**
     * Setter for property planets.
     *
     * @param planets New value of property planets.
     */
    void setPlanets(List<Planet> planets);

    /**
     * Setter for property rEcosphere.
     *
     * @param rEcosphere New value of property rEcosphere.
     */
    void setREcosphere(double rEcosphere);

    /**
     * Setter for property rEcosphereInner.
     *
     * @param rEcosphereInner New value of property r_ecosphere_inner.
     */
    void setREcosphereInner(double rEcosphereInner);

    /**
     * Setter for property rEcosphereOuter.
     *
     * @param rEcosphereOuter New value of property rEcosphereOuter.
     */
    void setREcosphereOuter(double rEcosphereOuter);

    /**
     * Setter for property spectralClass.
     *
     * @param spectralClass New value of property spectralClass.
     */
    void setSpectralClass(String spectralClass);

    /**
     * Setter for property spectralSubclass.
     *
     * @param spectralSubclass New value of property spectralSubclass.
     */
    void setSpectralSubclass(int spectralSubclass);

    /**
     * Getter for property rightAscension.
     * @return Value of property rightAscension.
     */
    public double getRightAscension();

    /**
     * Setter for property rightAscension.
     * @param rightAscension New value of property rightAscension.
     */
    public void setRightAscension(double rightAscension);

    /**
     * Getter for property declination.
     * @return Value of property declination.
     */
    public double getDeclination();

    /**
     * Setter for property declination.
     * @param declination New value of property declination.
     */
    public void setDeclination(double declination);

    /**
     * Getter for property distance.
     * @return Value of property distance.
     */
    public double getDistance();

    /**
     * Setter for property distance.
     * @param distance New value of property distance.
     */
    public void setDistance(double distance);

    /**
     * Getter for property hipparcusNumber.
     * @return Value of property hipparcusNumber.
     */
    public int getHipparcusNumber();

    /**
     * Setter for property hipparcusNumber.
     * @param hipparcusNumber New value of property hipparcusNumber.
     */
    public void setHipparcusNumber(int hipparcusNumber);
}
