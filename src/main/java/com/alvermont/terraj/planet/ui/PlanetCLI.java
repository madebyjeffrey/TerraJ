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
 * PlanetCLI.java
 *
 * Created on 08 February 2006, 09:27
 */
package com.alvermont.terraj.planet.ui;

import com.alvermont.terraj.fracplanet.util.DummyProgress;
import com.alvermont.terraj.planet.AllPlanetParameters;
import com.alvermont.terraj.planet.io.ImageBuilder;
import com.alvermont.terraj.planet.project.ProjectionManager;
import com.alvermont.terraj.planet.project.Projector;
import com.alvermont.terraj.util.ui.PNGFileFilter;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A command line interface to the Planet Generator, intended to be like
 * the original.
 *
 * @author  martin
 * @version $Id: PlanetCLI.java,v 1.10 2006/07/06 06:58:35 martin Exp $
 */
public class PlanetCLI
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(PlanetCLI.class);

    /** Creates a new instance of PlanetCLI */
    public PlanetCLI()
    {
    }

    /**
     * For no adequately explored reason we need to build an array of
     * short options as well as the long ones
     */
    private String getShortOptions(LongOpt[] options)
    {
        final StringBuffer shortOpts = new StringBuffer();

        for (LongOpt l : options)
        {
            shortOpts.append((char) l.getVal());

            if (l.getHasArg() == LongOpt.REQUIRED_ARGUMENT)
            {
                shortOpts.append(":");
            }
            else if (l.getHasArg() == LongOpt.OPTIONAL_ARGUMENT)
            {
                shortOpts.append("::");
            }
        }

        return shortOpts.toString();
    }

    /** Build and return the options array */
    private LongOpt[] getOptionArray()
    {
        final List<LongOpt> options = new ArrayList<LongOpt>();

        // add options to the array
        options.add(
            new LongOpt(
                "seed", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 's'));
        options.add(
            new LongOpt(
                "width", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'w'));
        options.add(
            new LongOpt(
                "height", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'h'));
        options.add(
            new LongOpt(
                "magnification", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(),
                'm'));
        options.add(
            new LongOpt(
                "output", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'o'));
        options.add(
            new LongOpt(
                "longitude", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'l'));
        options.add(
            new LongOpt(
                "latitude", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'L'));
        options.add(
            new LongOpt(
                "vgrid", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'g'));
        options.add(
            new LongOpt(
                "hgrid", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'G'));
        options.add(
            new LongOpt(
                "initialaltitude", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(),
                'i'));
        options.add(
            new LongOpt("latitudecolour", LongOpt.NO_ARGUMENT, null, 'c'));
        options.add(new LongOpt("lighten", LongOpt.NO_ARGUMENT, null, 'C'));
        options.add(
            new LongOpt(
                "nocols", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'N'));
        options.add(new LongOpt("altcolours", LongOpt.NO_ARGUMENT, null, 'a'));
        options.add(
            new LongOpt(
                "colourfile", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'M'));
        options.add(new LongOpt("outline", LongOpt.NO_ARGUMENT, null, 'O'));
        options.add(new LongOpt("edges", LongOpt.NO_ARGUMENT, null, 'E'));
        options.add(new LongOpt("bumpmap", LongOpt.NO_ARGUMENT, null, 'B'));
        options.add(
            new LongOpt(
                "shadeangle", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'A'));
        options.add(
            new LongOpt("reversebackground", LongOpt.NO_ARGUMENT, null, 'b'));
        options.add(
            new LongOpt(
                "distancecontrib", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(),
                'V'));
        options.add(
            new LongOpt(
                "altcontrib", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'v'));
        options.add(
            new LongOpt(
                "projection", LongOpt.REQUIRED_ARGUMENT, new StringBuffer(), 'p'));

        // now we convert to array to avoid hard coded constants and
        // array assignments
        final LongOpt[] optArray = new LongOpt[options.size()];

        return options.toArray(optArray);
    }

    private AllPlanetParameters processOptions(String[] args)
    {
        AllPlanetParameters params = new AllPlanetParameters();

        final LongOpt[] options = getOptionArray();
        final String shortOpts = getShortOptions(options);
        final Getopt g =
            new Getopt("planetgen", args, shortOpts, options, true);

        int c;

        while (params != null && (c = g.getopt()) != -1)
        {
            final String arg = g.getOptarg();

            try
            {
                switch ((char) c)
                {
                    case 's':

                        final double seed = Double.parseDouble(arg);
                        params.getPlanetParameters()
                            .setSeed(seed);

                        break;

                    case 'w':

                        final int width = Integer.parseInt(arg);
                        params.getProjectionParameters()
                            .setWidth(width);

                        break;

                    case 'h':

                        final int height = Integer.parseInt(arg);
                        params.getProjectionParameters()
                            .setHeight(height);

                        break;

                    case 'o':
                        params.getProjectionParameters()
                            .setOutputFile(arg);

                        break;

                    case 'm':

                        final double mag = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setScale(mag);

                        break;

                    case 'l':

                        final double lon = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setLon(lon);

                        break;

                    case 'L':

                        final double lat = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setLat(lat);

                        break;

                    case 'g':

                        final double vgrid = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setVgrid(vgrid);

                        break;

                    case 'G':

                        final double hgrid = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setHgrid(hgrid);

                        break;

                    case 'i':

                        final double initAlt = Double.parseDouble(arg);
                        params.getPlanetParameters()
                            .setInitialAltitude(initAlt);

                        break;

                    case 'c':
                        params.getProjectionParameters()
                            .setLatic(true);

                        break;

                    case 'C':

                        final int light =
                            params.getProjectionParameters()
                                .getLighterColours();
                        params.getProjectionParameters()
                            .setLighterColours(light + 1);

                        break;

                    case 'N':
                        System.err.println(
                            "The N option is not currently supported");
                        params = null;

                        break;

                    case 'a':
                        params.getProjectionParameters()
                            .setAltColors(true);

                        break;

                    case 'M':
                        params.getProjectionParameters()
                            .setColourFile(arg);

                        break;

                    case '?':
                        params = null;

                        break;

                    case 'O':
                        params.getProjectionParameters()
                            .setOutline(true);

                        break;

                    case 'E':
                        params.getProjectionParameters()
                            .setEdges(true);

                        break;

                    case 'B':
                        params.getProjectionParameters()
                            .setDoShade(true);

                        break;

                    case 'A':

                        final double angle = Double.parseDouble(arg);
                        params.getProjectionParameters()
                            .setShadeAngle(angle);

                        break;

                    case 'b':
                        params.getProjectionParameters()
                            .setReverseBackground(true);

                        break;

                    case 'V':

                        final double dd1 = Double.parseDouble(arg);
                        params.getPlanetParameters()
                            .setDistanceWeight(dd1);

                        break;

                    case 'v':

                        final double dd2 = Double.parseDouble(arg);
                        params.getPlanetParameters()
                            .setAltitudeDifferenceWeight(dd2);

                        break;

                    case 'p':
                        params.getProjectionParameters()
                            .setProjectionName(arg);

                        break;

                    default:
                        System.err.println(
                            "Unexpected option encountered: " + (char) c);
                        params = null;

                        break;
                }
            }
            catch (NumberFormatException ex)
            {
                System.err.println(
                    "Expecting a numeric option: " + ex.getMessage());

                params = null;
            }

            System.out.println("arg= " + (char) c + " val= " + arg);
        }

        return params;
    }

    /**
     * Generates the terrain from the supplied parameters
     *
     * @param proj The projection object to be used
     * @param params The parameters to be used to generate the terrain
     * @return a <code>BufferedImage</code> representing the terrain
     */
    protected BufferedImage generateTerrain(
        Projector proj, AllPlanetParameters params)
    {
        proj.setParameters(new AllPlanetParameters(params));
        proj.setProgress(new DummyProgress());

        proj.project();

        final ImageBuilder ib = new ImageBuilder();

        return ib.getImage(proj);
    }

    /**
     * Write a generated image to a file.
     *
     * @param image The <code>BufferedImage</code> to be written
     * @param params The parameters that indicate where the file is to be written
     * @throws java.io.IOException If there is an error writing the file
     */
    protected void writeImageFile(
        BufferedImage image, AllPlanetParameters params)
        throws IOException
    {
        final OutputStream target =
            new FileOutputStream(
                params.getProjectionParameters().getOutputFile());

        ImageIO.write(
            image,
            PNGFileFilter.getFormatName(
                new File(params.getProjectionParameters().getOutputFile())),
            target);

        target.close();
    }

    /**
     * Main entrypoint for this program
     *
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        final PlanetCLI me = new PlanetCLI();

        LongOpt[] options = me.getOptionArray();
        String shortOpts = me.getShortOptions(options);

        try
        {
            AllPlanetParameters params = me.processOptions(args);

            if (params != null)
            {
                // then the options were accepted
                ProjectionManager mgr = new ProjectionManager();

                Projector proj =
                    mgr.findByName(
                        params.getProjectionParameters().getProjectionName());

                if (proj == null)
                {
                    System.err.println(
                        "Error. Unknown projection name: " +
                        params.getProjectionParameters().getProjectionName());

                    System.exit(1);
                }

                final BufferedImage image = me.generateTerrain(proj, params);

                me.writeImageFile(image, params);
            }
            else
            {
                // exit - getopt has printed the error message for us
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();

            System.exit(1);
        }
    }
}
