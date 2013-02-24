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
 * PlanetGenExample.java
 *
 * Created on December 23, 2005, 3:45 PM
 *
 */
package com.alvermont.terraj.planet;

import com.alvermont.terraj.planet.io.ImageBuilder;
import com.alvermont.terraj.planet.project.MollweideProjection;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple example of how the planetary terrain generator can be used
 *
 * @author martin
 * @version $Id: PlanetGenExample.java,v 1.5 2006/07/06 06:58:36 martin Exp $
 */
public class PlanetGenExample
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(PlanetGenExample.class);

    /**
     * Creates a new instance of PlanetGenExample
     */
    public PlanetGenExample()
    {
    }

    // MagicNumber OFF

    /** The colour table we used to get a martian looking terrain */
    static int[][] marsColours =
        {
            { 0x37, 0x1C, 0x4B },
            { 0x88, 0x5C, 0x50 },
            { 0x94, 0x64, 0x52 },
            { 0xE8, 0xA4, 0x5A },
            { 0xF8, 0xB4, 0x5C },
            { 0xA5, 0x80, 0x6C },
            { 0xFF, 0xFF, 0xFF },
            { 0, 0, 0 },
            { 0, 0, 0 }
        };

    // MagicNumber ON

    /**
     * The main entrypoint to the program
     *
     * @param args The command line argument
     */
    public static void main(String[] args)
    {
        try
        {
            // Uncomment one of the projection types below and import the
            // correct projection at the top of the file

            //MercatorProjection mp = new MercatorProjection();
            //OrthographicProjection mp = new OrthographicProjection();
            //StereographicProjection mp = new StereographicProjection();
            //PetersProjection mp = new PetersProjection();
            //SquareProjection mp = new SquareProjection();
            //GnomonicProjection mp = new GnomonicProjection();
            //AzimuthProjection mp = new AzimuthProjection();
            //ConicalProjection mp = new ConicalProjection();
            final MollweideProjection mp = new MollweideProjection();

            //SinusoidProjection mp = new SinusoidProjection();

            // Uncomment these to set terrain parameters

            //mp.setInitialAltitude(+0.09);
            //mp.setPower(1.0);
            //mp.setReverseBackground(true);

            // Set the desired size of the image
            final ProjectionParameters p =
                mp.getParameters()
                    .getProjectionParameters();

            p.setWidth(800);
            p.setHeight(600);

            // change latitude and longitude here if you like

            //p.setLat(45.0);
            //p.setLon(45.0);

            // Enable latitude based colouring
            p.setLatic(true);

            // Use our martian colours
            p.setColors(marsColours);

            // Uncomment to generate a grid

            //mp.setHgrid(10);
            //mp.setVgrid(10.0);

            // draw the terrain image
            mp.project();

            // Uncomment to draw outlines of coastlines

            //mp.outline(false);

            // now write the result to a file. This is the older way of doing
            // it. The newer way is below. This is left as an example

            //BMPOutput bmp = new BMPOutput();

            //FileOutputStream fos = new FileOutputStream("test.bmp");
            //bmp.outputBMP(mp, fos);
            //fos.close();            

            // now get the output as a buffered image and write it to a file
            final ImageBuilder ib = new ImageBuilder();

            final BufferedImage img = ib.getImage(mp);
            ImageIO.write(img, "png", new File("test.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
