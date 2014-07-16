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
 * AccreteTest.java
 *
 * Created on December 21, 2005, 3:43 PM
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import com.alvermont.terraj.stargen.util.NonRandomRandom;
import java.util.List;

/**
 * Simple test that generates a system.
 *
 * @author martin
 * @version $Id: AccreteTest.java,v 1.7 2006/07/06 06:58:33 martin Exp $
 */
public class AccreteTest
{
    /** Creates a new instance of AccreteTest */
    protected AccreteTest()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Entrypoint for the accretion test
     *
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            final MathUtils utils = new MathUtils(new NonRandomRandom());
            final Generator gen = new Generator(utils);
            final Display display = new Display();

            Primary sun = new BasicPrimary();

            //sun.setMass(1.0);
            //sun.setMass(0.69999999);
            //sun.setAge(5e9);
            gen.setGenerateMoons(true);
            gen.setMoonMinMassLimit(.001);

            gen.generate(sun, 0, "Test");

            final List<Planet> planets = sun.getPlanets();

            System.out.println("Generated " + planets.size() + " planets.");

            for (Planet p : planets)
            {
                System.out.println(
                    "a = " + p.getA() + ", e=" + p.getE() + " , mass=" +
                    p.getMass());
            }

            System.out.println(display.getSystemCharacteristics(sun));
            System.out.println(display.getBasicPlanetaryData(planets));

            System.out.println(display.getPlanetDetails(planets.get(0)));

            sun = gen.generateStar();

            System.out.println(display.getSystemCharacteristics(sun));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
