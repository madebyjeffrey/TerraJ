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
 * ChemTable.java
 *
 * Created on December 21, 2005, 10:44 AM
 *
 */
package com.alvermont.terraj.stargen;


/**
 * Holds details of chemical elements
 *
 * @author martin
 * @version $Id: ChemTable.java,v 1.4 2006/07/06 06:58:33 martin Exp $
 */
public class ChemTable
{
    /**
     * Holds value of property number.
     */
    private int number;

    /**
     * Holds value of property symbol.
     */
    private String symbol;

    /**
     * Holds value of property htmlSymbol.
     */
    private String htmlSymbol;

    /**
     * Holds value of property weight.
     */
    private double weight;

    /**
     * Holds value of property meltingPoint.
     */
    private double meltingPoint;

    /**
     * Holds value of property boilingPoint.
     */
    private double boilingPoint;

    /**
     * Holds value of property density.
     */
    private double density;

    /**
     * Holds value of property abunde.
     */
    private double abunde;

    /**
     * Holds value of property abunds.
     */
    private double abunds;

    /**
     * Holds value of property reactivity.
     */
    private double reactivity;

    /**
     * Holds value of property maxIpp.
     */
    private double maxIpp;

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Creates a new instance of ChemTable
     */
    public ChemTable()
    {
    }

    /**
     * Creates a new instance of ChemTable
     *
     * @param atomicNumber The atomic number of the element
     * @param symbol The chemical symbol of the element
     * @param html An HTML symbol for the element
     * @param name The name of the element
     * @param atomicWeight The atomic weight of the element
     * @param melt The melting point of the element (degrees Kelvin)
     * @param boil The boiling point of the element (degrees Kelvin)
     * @param density The density of the element
     * @param abunde Element abundance data
     * @param abunds Element abundance data
     * @param rea The reactivity of the element
     * @param maxIpp The max allowed inspired partial pressure for this element
     * for a habitable planet
     */
    public ChemTable(
        int atomicNumber, String symbol, String html, String name,
        double atomicWeight, double melt, double boil, double density,
        double abunde, double abunds, double rea, double maxIpp)
    {
        this.number = atomicNumber;
        this.symbol = symbol;
        this.htmlSymbol = html;
        this.name = name;
        this.weight = atomicWeight;
        this.meltingPoint = melt;
        this.boilingPoint = boil;
        this.density = density;
        this.abunde = abunde;
        this.abunds = abunds;
        this.reactivity = rea;
        this.maxIpp = maxIpp;
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
     * Getter for property symbol.
     * @return Value of property symbol.
     */
    public String getSymbol()
    {
        return this.symbol;
    }

    /**
     * Setter for property symbol.
     * @param symbol New value of property symbol.
     */
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    /**
     * Getter for property htmlSymbol.
     * @return Value of property htmlSymbol.
     */
    public String getHtmlSymbol()
    {
        return this.htmlSymbol;
    }

    /**
     * Setter for property htmlSymbol.
     * @param htmlSymbol New value of property htmlSymbol.
     */
    public void setHtmlSymbol(String htmlSymbol)
    {
        this.htmlSymbol = htmlSymbol;
    }

    /**
     * Getter for property weight.
     * @return Value of property weight.
     */
    public double getWeight()
    {
        return this.weight;
    }

    /**
     * Setter for property weight.
     * @param weight New value of property weight.
     */
    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    /**
     * Getter for property meltingPoint.
     * @return Value of property meltingPoint.
     */
    public double getMeltingPoint()
    {
        return this.meltingPoint;
    }

    /**
     * Setter for property meltingPoint.
     * @param meltingPoint New value of property meltingPoint.
     */
    public void setMeltingPoint(double meltingPoint)
    {
        this.meltingPoint = meltingPoint;
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
     * Getter for property abunde.
     * @return Value of property abunde.
     */
    public double getAbunde()
    {
        return this.abunde;
    }

    /**
     * Setter for property abunde.
     * @param abunde New value of property abunde.
     */
    public void setAbunde(double abunde)
    {
        this.abunde = abunde;
    }

    /**
     * Getter for property abunds.
     * @return Value of property abunds.
     */
    public double getAbunds()
    {
        return this.abunds;
    }

    /**
     * Setter for property abunds.
     * @param abunds New value of property abunds.
     */
    public void setAbunds(double abunds)
    {
        this.abunds = abunds;
    }

    /**
     * Getter for property reactivity.
     * @return Value of property reactivity.
     */
    public double getReactivity()
    {
        return this.reactivity;
    }

    /**
     * Setter for property reactivity.
     * @param reactivity New value of property reactivity.
     */
    public void setReactivity(double reactivity)
    {
        this.reactivity = reactivity;
    }

    /**
     * Getter for property maxIpp.
     * @return Value of property maxIpp.
     */
    public double getMaxIpp()
    {
        return this.maxIpp;
    }

    /**
     * Setter for property maxIpp.
     * @param maxIpp New value of property maxIpp.
     */
    public void setMaxIpp(double maxIpp)
    {
        this.maxIpp = maxIpp;
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
}
