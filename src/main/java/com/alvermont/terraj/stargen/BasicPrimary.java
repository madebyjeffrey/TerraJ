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
 * Created on December 21, 2005, 10:24 AM
 *
 */
package com.alvermont.terraj.stargen;

import java.util.List;

/**
 * Class that details a star
 *
 * @author martin
 * @version $Id: BasicPrimary.java,v 1.10 2006/07/06 06:58:33 martin Exp $
 */
public class BasicPrimary implements Primary
{
    /**
     * Holds value of property luminosity.
     */
    private double luminosity;

    /**
     * Holds value of property mass.
     */
    private double mass;

    /**
     * Holds value of property life.
     */
    private double life;

    /**
     * Holds value of property age.
     */
    private double age;

    /**
     * Holds value of property rEcosphere.
     */
    private double rEcosphere;

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property innermostPlanet.
     */
    private Planet innermostPlanet;

    /**
     * Holds value of property spectralClass.
     */
    private String spectralClass;

    /**
     * Holds value of property spectralSubclass.
     */
    private int spectralSubclass;

    /**
     * Holds value of property absoluteMagnitude.
     */
    private double absoluteMagnitude;

    /**
     * Holds value of property luminosityClass.
     */
    private String luminosityClass;

    /**
     * Holds value of property r_ecosphereInner.
     */
    private double rEcosphereInner;

    /**
     * Holds value of property r_ecosphereOuter.
     */
    private double rEcosphereOuter;

    /**
     * Holds value of property planets.
     */
    private List<Planet> planets;

    /** Creates a new instance of Primary */
    public BasicPrimary()
    {
    }

    /**
     * Getter for property luminosity.
     * @return Value of property luminosity.
     */
    public double getLuminosity()
    {
        return this.luminosity;
    }

    /**
     * Setter for property luminosity.
     * @param luminosity New value of property luminosity.
     */
    public void setLuminosity(double luminosity)
    {
        this.luminosity = luminosity;
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
     * Getter for property life.
     * @return Value of property life.
     */
    public double getLife()
    {
        return this.life;
    }

    /**
     * Setter for property life.
     * @param life New value of property life.
     */
    public void setLife(double life)
    {
        this.life = life;
    }

    /**
     * Getter for property age.
     * @return Value of property age.
     */
    public double getAge()
    {
        return this.age;
    }

    /**
     * Setter for property age.
     * @param age New value of property age.
     */
    public void setAge(double age)
    {
        this.age = age;
    }

    /**
     * Getter for property rEcosphere.
     *
     * @return Value of property rEcosphere.
     */
    public double getREcosphere()
    {
        return this.rEcosphere;
    }

    /**
     * Setter for property rEcosphere.
     *
     * @param rEcosphere New value of property rEcosphere.
     */
    public void setREcosphere(double rEcosphere)
    {
        this.rEcosphere = rEcosphere;
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
     * Getter for property innermostPlanet.
     * @return Value of property innermostPlanet.
     */
    public Planet getInnermostPlanet()
    {
        return this.innermostPlanet;
    }

    /**
     * Setter for property innermostPlanet.
     * @param innermostPlanet New value of property innermostPlanet.
     */
    public void setInnermostPlanet(Planet innermostPlanet)
    {
        this.innermostPlanet = innermostPlanet;
    }

    /**
     * Getter for property spectralClass.
     * @return Value of property spectralClass.
     */
    public String getSpectralClass()
    {
        return this.spectralClass;
    }

    /**
     * Setter for property spectralClass.
     * @param spectralClass New value of property spectralClass.
     */
    public void setSpectralClass(String spectralClass)
    {
        this.spectralClass = spectralClass;
    }

    /**
     * Getter for property spectralSubclass.
     * @return Value of property spectralSubclass.
     */
    public int getSpectralSubclass()
    {
        return this.spectralSubclass;
    }

    /**
     * Setter for property spectralSubclass.
     * @param spectralSubclass New value of property spectralSubclass.
     */
    public void setSpectralSubclass(int spectralSubclass)
    {
        this.spectralSubclass = spectralSubclass;
    }

    /**
     * Getter for property absoluteMagnitude.
     * @return Value of property absoluteMagnitude.
     */
    public double getAbsoluteMagnitude()
    {
        return this.absoluteMagnitude;
    }

    /**
     * Setter for property absoluteMagnitude.
     * @param absoluteMagnitude New value of property absoluteMagnitude.
     */
    public void setAbsoluteMagnitude(double absoluteMagnitude)
    {
        this.absoluteMagnitude = absoluteMagnitude;
    }

    /**
     * Getter for property luminosityClass.
     * @return Value of property luminosityClass.
     */
    public String getLuminosityClass()
    {
        return this.luminosityClass;
    }

    /**
     * Setter for property luminosityClass.
     * @param luminosityClass New value of property luminosityClass.
     */
    public void setLuminosityClass(String luminosityClass)
    {
        this.luminosityClass = luminosityClass;
    }

    /**
     * Getter for property rEcosphereInner.
     * @return Value of property rEcosphereInner.
     */
    public double getREcosphereInner()
    {
        return this.rEcosphereInner;
    }

    /**
     * Setter for property rEcosphereInner.
     *
     * @param rEcosphereInner The inner ecosphere radius
     */
    public void setREcosphereInner(double rEcosphereInner)
    {
        this.rEcosphereInner = rEcosphereInner;
    }

    /**
     * Getter for property rEcosphereOuter.
     * @return Value of property rEcosphereOuter.
     */
    public double getREcosphereOuter()
    {
        return this.rEcosphereOuter;
    }

    /**
     * Setter for property r_ecosphere_outer.
     *
     * @param rEcosphereOuter The outer ecosphere radius
     */
    public void setREcosphereOuter(double rEcosphereOuter)
    {
        this.rEcosphereOuter = rEcosphereOuter;
    }

    /**
     * Getter for property planets.
     * @return Value of property planets.
     */
    public List<Planet> getPlanets()
    {
        return this.planets;
    }

    /**
     * Setter for property planets.
     * @param planets New value of property planets.
     */
    public void setPlanets(List<Planet> planets)
    {
        this.planets = planets;
    }

    /**
     * Holds value of property rightAscension.
     */
    private double rightAscension;

    /**
     * Getter for property rightAscension.
     * @return Value of property rightAscension.
     */
    public double getRightAscension()
    {
        return this.rightAscension;
    }

    /**
     * Setter for property rightAscension.
     * @param rightAscension New value of property rightAscension.
     */
    public void setRightAscension(double rightAscension)
    {
        this.rightAscension = rightAscension;
    }

    /**
     * Holds value of property declination.
     */
    private double declination;

    /**
     * Getter for property declination.
     * @return Value of property declination.
     */
    public double getDeclination()
    {
        return this.declination;
    }

    /**
     * Setter for property declination.
     * @param declination New value of property declination.
     */
    public void setDeclination(double declination)
    {
        this.declination = declination;
    }

    /**
     * Holds value of property distance.
     */
    private double distance;

    /**
     * Getter for property distance.
     * @return Value of property distance.
     */
    public double getDistance()
    {
        return this.distance;
    }

    /**
     * Setter for property distance.
     * @param distance New value of property distance.
     */
    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    /**
     * Holds value of property hipparcusNumber.
     */
    private int hipparcusNumber;

    /**
     * Getter for property hipparcusNumber.
     * @return Value of property hipparcusNumber.
     */
    public int getHipparcusNumber()
    {
        return this.hipparcusNumber;
    }

    /**
     * Setter for property hipparcusNumber.
     * @param hipparcusNumber New value of property hipparcusNumber.
     */
    public void setHipparcusNumber(int hipparcusNumber)
    {
        this.hipparcusNumber = hipparcusNumber;
    }
}
