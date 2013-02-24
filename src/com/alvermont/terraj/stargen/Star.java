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
 * Star.java
 *
 * Created on December 21, 2005, 10:40 AM
 */
package com.alvermont.terraj.stargen;

import java.util.List;

/**
 * Class representing a star
 *
 * @author martin
 * @version $Id: Star.java,v 1.3 2006/07/06 06:58:33 martin Exp $
 */
public class Star
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
     * Holds value of property m2.
     */
    private double m2;

    /**
     * Holds value of property e.
     */
    private double e;

    /**
     * Holds value of property a.
     */
    private double a;

    /**
     * Holds value of property knownPlanets.
     */
    private List knownPlanets;

    /**
     * Holds value of property designation.
     */
    private String designation;

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Holds value of property inCelestia.
     */
    private boolean inCelestia;

    /** Creates a new instance of Star */
    public Star()
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
     * Getter for property m2.
     * @return Value of property m2.
     */
    public double getM2()
    {
        return this.m2;
    }

    /**
     * Setter for property m2.
     * @param m2 New value of property m2.
     */
    public void setM2(double m2)
    {
        this.m2 = m2;
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
     * Getter for property knownPlanets.
     * @return Value of property knownPlanets.
     */
    public List getKnownPlanets()
    {
        return this.knownPlanets;
    }

    /**
     * Setter for property knownPlanets.
     * @param knownPlanets New value of property knownPlanets.
     */
    public void setKnownPlanets(List knownPlanets)
    {
        this.knownPlanets = knownPlanets;
    }

    /**
     * Getter for property designation.
     * @return Value of property designation.
     */
    public String getDesignation()
    {
        return this.designation;
    }

    /**
     * Setter for property designation.
     * @param designation New value of property designation.
     */
    public void setDesignation(String designation)
    {
        this.designation = designation;
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
     * Getter for property inCelestia.
     * @return Value of property inCelestia.
     */
    public boolean isInCelestia()
    {
        return this.inCelestia;
    }

    /**
     * Setter for property inCelestia.
     * @param inCelestia New value of property inCelestia.
     */
    public void setInCelestia(boolean inCelestia)
    {
        this.inCelestia = inCelestia;
    }
}
