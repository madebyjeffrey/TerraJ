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
 * DustRecord.java
 *
 * Created on December 21, 2005, 10:39 AM
 */
package com.alvermont.terraj.stargen;


/**
 * Bean class that details one dust band
 *
 * @author martin
 * @version $Id: DustRecord.java,v 1.3 2006/07/06 06:58:33 martin Exp $
 */
public class DustRecord
{
    /**
     * Holds value of property innerEdge.
     */
    private double innerEdge;

    /**
     * Holds value of property outerEdge.
     */
    private double outerEdge;

    /**
     * Holds value of property dustPresent.
     */
    private boolean dustPresent;

    /**
     * Holds value of property gasPresent.
     */
    private boolean gasPresent;

    /**
     * Holds value of property nextDust.
     */
    private DustRecord nextDust;

    /** Creates a new instance of DustRecord */
    public DustRecord()
    {
    }

    /**
     * Creates a new instance of DustRecord
     *
     * @param innerEdge The inner radius of the band
     * @param outerEdge The outer radius of the band
     */
    public DustRecord(double innerEdge, double outerEdge)
    {
        this.innerEdge = innerEdge;
        this.outerEdge = outerEdge;
    }

    /**
     * Getter for property innerEdge.
     * @return Value of property innerEdge.
     */
    public double getInnerEdge()
    {
        return this.innerEdge;
    }

    /**
     * Setter for property innerEdge.
     * @param innerEdge New value of property innerEdge.
     */
    public void setInnerEdge(double innerEdge)
    {
        this.innerEdge = innerEdge;
    }

    /**
     * Getter for property outerEdge.
     * @return Value of property outerEdge.
     */
    public double getOuterEdge()
    {
        return this.outerEdge;
    }

    /**
     * Setter for property outerEdge.
     * @param outerEdge New value of property outerEdge.
     */
    public void setOuterEdge(double outerEdge)
    {
        this.outerEdge = outerEdge;
    }

    /**
     * Getter for property dustPresent.
     * @return Value of property dustPresent.
     */
    public boolean isDustPresent()
    {
        return this.dustPresent;
    }

    /**
     * Setter for property dustPresent.
     * @param dustPresent New value of property dustPresent.
     */
    public void setDustPresent(boolean dustPresent)
    {
        this.dustPresent = dustPresent;
    }

    /**
     * Getter for property gasPresent.
     * @return Value of property gasPresent.
     */
    public boolean isGasPresent()
    {
        return this.gasPresent;
    }

    /**
     * Setter for property gasPresent.
     * @param gasPresent New value of property gasPresent.
     */
    public void setGasPresent(boolean gasPresent)
    {
        this.gasPresent = gasPresent;
    }

    /**
     * Getter for property nextDust.
     * @return Value of property nextDust.
     */
    public DustRecord getNextDust()
    {
        return this.nextDust;
    }

    /**
     * Setter for property nextDust.
     * @param nextDust New value of property nextDust.
     */
    public void setNextDust(DustRecord nextDust)
    {
        this.nextDust = nextDust;
    }
}
