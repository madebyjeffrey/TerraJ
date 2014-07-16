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
 * Gases.java
 *
 * Created on December 21, 2005, 11:25 PM
 */
package com.alvermont.terraj.stargen;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The GASES table
 *
 *
 * @author martin
 * @version $Id: Gases.java,v 1.7 2006/07/06 06:58:33 martin Exp $
 */
public class Gases
{
    /** The table of gas information managed by this class */
    protected static final ChemTable[] GASES =
        {
            
            //   An   sym   HTML symbol                      name                 Aw     
            // melt    boil    dens       ABUNDe       ABUNDs         Rea Max inspired pp
            new ChemTable(
                Constants.AN_H, "H", "H<SUB><SMALL>2</SMALL></SUB>", "Hydrogen",
                1.0079, 14.06, 20.40, 8.99e-05, 0.00125893, 27925.4, 1, 0.0),
            new ChemTable(
                Constants.AN_HE, "He", "He", "Helium", 4.0026, 3.46, 4.20,
                0.0001787, 7.94328e-09, 2722.7, 0, Constants.MAX_HE_IPP),
            new ChemTable(
                Constants.AN_N, "N", "N<SUB><SMALL>2</SMALL></SUB>", "Nitrogen",
                14.0067, 63.34, 77.40, 0.0012506, 1.99526e-05, 3.13329, 0,
                Constants.MAX_N2_IPP),
            new ChemTable(
                Constants.AN_O, "O", "O<SUB><SMALL>2</SMALL></SUB>", "Oxygen",
                15.9994, 54.80, 90.20, 0.001429, 0.501187, 23.8232, 10,
                Constants.MAX_O2_IPP),
            new ChemTable(
                Constants.AN_NE, "Ne", "Ne", "Neon", 20.1700, 24.53, 27.10,
                0.0009, 5.01187e-09, 3.4435e-5, 0, Constants.MAX_NE_IPP),
            new ChemTable(
                Constants.AN_AR, "Ar", "Ar", "Argon", 39.9480, 84.00, 87.30,
                0.0017824, 3.16228e-06, 0.100925, 0, Constants.MAX_AR_IPP),
            new ChemTable(
                Constants.AN_KR, "Kr", "Kr", "Krypton", 83.8000, 116.60, 119.70,
                0.003708, 1e-10, 4.4978e-05, 0, Constants.MAX_KR_IPP),
            new ChemTable(
                Constants.AN_XE, "Xe", "Xe", "Xenon", 131.3000, 161.30, 165.00,
                0.00588, 3.16228e-11, 4.69894e-06, 0, Constants.MAX_XE_IPP),
            

            //  from here down, these columns were originally: 0.001,         0
            new ChemTable(
                Constants.AN_NH3, "NH3", "NH<SUB><SMALL>3</SMALL></SUB>",
                "Ammonia", 17.0000, 195.46, 239.66, 0.001, 0.002, 0.0001, 1,
                Constants.MAX_NH3_IPP),
            new ChemTable(
                Constants.AN_H2O, "H2O", "H<SUB><SMALL>2</SMALL></SUB>O",
                "Water", 18.0000, 273.16, 373.16, 1.000, 0.03, 0.001, 0, 0.0),
            new ChemTable(
                Constants.AN_CO2, "CO2", "CO<SUB><SMALL>2</SMALL></SUB>",
                "CarbonDioxide", 44.0000, 194.66, 194.66, 0.001, 0.01, 0.0005, 0,
                Constants.MAX_CO2_IPP),
            new ChemTable(
                Constants.AN_O3, "O3", "O<SUB><SMALL>3</SMALL></SUB>", "Ozone",
                48.0000, 80.16, 161.16, 0.001, 0.001, 0.000001, 2,
                Constants.MAX_O3_IPP),
            new ChemTable(
                Constants.AN_CH4, "CH4", "CH<SUB><SMALL>4</SMALL></SUB>",
                "Methane", 16.0000, 90.16, 109.16, 0.010, 0.005, 0.0001, 1,
                Constants.MAX_CH4_IPP)
        };

    /** A map from the atomic number of a gas to the gas information */
    private Map<Integer, ChemTable> gasMap = new HashMap<Integer, ChemTable>();

    /** Creates a new instance of Gases */
    public Gases()
    {
        for (int g = 0; g < this.GASES.length; ++g)
        {
            this.gasMap.put(this.GASES[g].getNumber(), this.GASES[g]);
        }
    }

    /**
     * Locate a gas record by its atomic number
     *
     * @param number The atomic number of the gas
     * @return The corresponding gas record for the gas
     */
    public ChemTable findGasByAtomicNumber(int number)
    {
        return this.gasMap.get(number);
    }

    /**
     * Return gas information by its position in the gas table
     *
     * @param i The index of the gas in the gas table
     * @return The corresponding gas record for the gas
     */
    public ChemTable getGasByIndex(int i)
    {
        return this.GASES[i];
    }

    /**
     * Return the table of all GASES as a read only collection
     *
     *
     * @return The gas data as a collection
     */
    public Collection<ChemTable> getAllGases()
    {
        return Collections.unmodifiableCollection(this.gasMap.values());
    }
}
