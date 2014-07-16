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
 * MollweideProjection.java
 *
 * Created on December 24, 2005, 3:34 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.planet.AllPlanetParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mollweide Projection
 *
 * @author martin
 * @version $Id: MollweideProjection.java,v 1.8 2006/07/06 06:58:34 martin Exp $
 */
public class MollweideProjection extends AbstractProjector implements Projector
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(MollweideProjection.class);

    // RequireThis OFF: log
    // MagicNumber OFF

    /** Table of values used by the projection */
    private double[] mollTable =
        {
            0.0, 0.0685055811, 0.1368109534, 0.2047150027, 0.2720147303,
            0.3385041213, 0.4039727534, 0.4682040106, 0.5309726991, 0.5920417499,
            0.6511575166, 0.7080428038, 0.7623860881, 0.8138239166, 0.8619100185,
            0.9060553621, 0.9453925506, 0.9783738403, 1.0
        };

    // MagicNumber ON

    /** Creates a new instance of MollweideProjection */
    public MollweideProjection()
    {
    }

    /**
     * Creates a new instance of MollweideProjection
     *
     * @param params The parameters to be used
     */
    public MollweideProjection(AllPlanetParameters params)
    {
        super(params);
    }

    /**
     * Carry out the projection
     */
    public void project()
    {
        setcolours();

        final int width = getParameters()
                .getProjectionParameters()
                .getWidth();
        final int height =
            getParameters()
                .getProjectionParameters()
                .getHeight();

        final double lat =
            getParameters()
                .getProjectionParameters()
                .getLatitudeRadians();
        final double lon =
            getParameters()
                .getProjectionParameters()
                .getLongitudeRadians();

        final double scale =
            getParameters()
                .getProjectionParameters()
                .getScale();

        final double hgrid =
            getParameters()
                .getProjectionParameters()
                .getHgrid();
        final double vgrid =
            getParameters()
                .getProjectionParameters()
                .getVgrid();

        final boolean doShade =
            getParameters()
                .getProjectionParameters()
                .isDoShade();

        cacheParameters();

        colours = new short[width][height];
        shades = new short[width][height];

        depth = (3 * ((int) (log2(scale * height)))) + 6;

        log.debug("MollweideProjection starting with depth set to " + depth);

        progress.progressStart(height, "Generating Terrain");

        double x;
        double y;
        double y1;
        double zz;
        double scale1;
        double cos2;
        double theta1;
        double theta2;
        int i;
        int j;
        int i1 = 1;
        int k;

        for (j = 0; j < height; ++j)
        {
            progress.progressStep(j);

            y1 = (2 * ((2.0 * j) - height)) / width / scale;

            if (Math.abs(y1) >= 1.0)
            {
                for (i = 0; i < width; ++i)
                {
                    colours[i][j] = backgroundColour;

                    if (doShade)
                    {
                        shades[i][j] = 255;
                    }
                }
            }
            else
            {
                zz = Math.sqrt(1.0 - (y1 * y1));
                y = 2.0 / Math.PI * ((y1 * zz) + Math.asin(y1));
                cos2 = Math.sqrt(1.0 - (y * y));

                if (cos2 > 0.0)
                {
                    scale1 = (scale * width) / height / cos2 / Math.PI;
                    depth = (3 * ((int) (log2(scale1 * height)))) + 3;

                    for (i = 0; i < width; ++i)
                    {
                        theta1 = (Math.PI / zz * ((2.0 * i) - width)) / width / scale;

                        if (Math.abs(theta1) > Math.PI)
                        {
                            colours[i][j] = backgroundColour;

                            if (doShade)
                            {
                                shades[i][j] = 255;
                            }
                        }
                        else
                        {
                            theta1 += (lon - (0.5 * Math.PI));

                            colours[i][j] = (short) planet0(
                                    Math.cos(theta1) * cos2, y,
                                    -Math.sin(theta1) * cos2);

                            if (doShade)
                            {
                                shades[i][j] = shade;
                            }
                        }
                    }
                }
            }
        }

        progress.progressComplete("Terrain Generated");

        log.debug("MollweideProjection complete");

        if (hgrid != 0.0)
        {
            /* draw horizontal gridlines */
            for (
                theta1 = 0.0;
                    theta1 > -ProjectionConstants.RIGHT_ANGLE_DEGREES;
                    theta1 -= hgrid)
                ;

            for (
                theta1 = theta1;
                    theta1 < ProjectionConstants.RIGHT_ANGLE_DEGREES;
                    theta1 += hgrid)
            {
                theta2 = Math.abs(theta1);
                x = Math.floor(theta2 / 5.0);
                y = (theta2 / 5.0) - x;
                y = ((1.0 - y) * mollTable[(int) x]) +
                    (y * mollTable[(int) x + 1]);

                if (theta1 < 0.0)
                {
                    y = -y;
                }

                j = (height / 2) + (int) (0.25 * y * width * scale);

                if ((j >= 0) && (j < height))
                {
                    for (
                        i = Math.max(
                                0,
                                (width / 2) -
                                (int) (0.5 * width * scale * Math.sqrt(
                                        1.0 - (y * y))));
                            i < Math.min(
                                width,
                                (width / 2) +
                                (int) (0.5 * width * scale * Math.sqrt(
                                    1.0 - (y * y)))); ++i)
                        colours[i][j] = BLACK;
                }
            }
        }

        if (vgrid != 0.0)
        {
            /* draw vertical gridlines */
            for (
                theta1 = 0.0; theta1 > -ProjectionConstants.CIRCLE_DEGREES;
                    theta1 -= vgrid)
                ;

            for (
                theta1 = theta1; theta1 < ProjectionConstants.CIRCLE_DEGREES;
                    theta1 += vgrid)
            {
                if (
                    ((Math.toRadians(theta1) - lon + (0.5 * Math.PI)) > -Math.PI) &&
                        ((Math.toRadians(theta1) - lon + (0.5 * Math.PI)) <= Math.PI))
                {
                    x = (0.5 * (Math.toRadians(theta1) - lon + (0.5 * Math.PI)) * width * scale) / Math.PI;
                    j = Math.max(
                            0, (height / 2) - (int) (0.25 * width * scale));

                    y = (2 * ((2.0 * j) - height)) / width / scale;
                    i = (int) ((width / 2) + (x * Math.sqrt(1.0 - (y * y))));

                    for (
                        ;
                            j <= Math.min(
                                height,
                                (height / 2) + (int) (0.25 * width * scale));
                            ++j)
                    {
                        y1 = (2 * ((2.0 * j) - height)) / width / scale;

                        if (Math.abs(y1) <= 1.0)
                        {
                            i1 = (int) ((width / 2) +
                                (x * Math.sqrt(1.0 - (y1 * y1))));

                            if ((i1 >= 0) && (i1 < width))
                            {
                                colours[i1][j] = BLACK;
                            }
                        }

                        if (Math.abs(y) <= 1.0)
                        {
                            if (i < i1)
                            {
                                for (k = i + 1; k < i1; ++k)
                                {
                                    if ((k > 00) && (k < width))
                                    {
                                        colours[k][j] = BLACK;
                                    }
                                }
                            }
                            else if (i > i1)
                            {
                                for (k = i - 1; k > i1; --k)
                                {
                                    if ((k >= 0) && (k < width))
                                    {
                                        colours[k][j] = BLACK;
                                    }
                                }
                            }
                        }

                        y = y1;
                        i = i1;
                    }
                }
            }
        }

        if (doShade)
        {
            smoothshades();
        }

        doOutlining();
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return "Mollweide Projection";
    }

    /**
     * Get the name of the thumbnail image to use for this projection
     *
     * @return The name of the thumbmail image
     */
    public String getThumbnailName()
    {
        return "t_mollweide";
    }
}
