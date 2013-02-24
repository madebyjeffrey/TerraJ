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
 * ConicalProjection.java
 *
 * Created on December 24, 2005, 3:14 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.planet.AllPlanetParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Conical Projection
 *
 * @author martin
 * @version $Id: ConicalProjection.java,v 1.10 2006/07/07 07:47:18 martin Exp $
 */
public class ConicalProjection extends AbstractProjector implements Projector
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(ConicalProjection.class);

    /** Creates a new instance of ConicalProjection */
    public ConicalProjection()
    {
    }

    /**
     * Creates a new instance of ConicalProjection
     *
     * @param params The parameters to be used
     */
    public ConicalProjection(AllPlanetParameters params)
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

        final double sla = Math.sin(lat);
        final double cla = Math.cos(lat);
        final double slo = Math.sin(lon);
        final double clo = Math.cos(lon);

        depth = (3 * ((int) (log2(scale * height)))) + 6;

        double k1;
        double c;
        double y2;
        double x;
        double y;
        double zz;
        double x1;
        double y1;
        double z1;
        double theta1;
        double theta2;
        double ymin;
        double ymax;
        double cos2;
        int i;
        int j;

        ymin = 2.0;
        ymax = -2.0;

        if (lat > 0)
        {
            k1 = 1.0 / Math.sin(lat);
            c = k1 * k1;

            y2 = Math.sqrt(
                    (c * (1.0 - Math.sin(lat / k1))) / (1.0 +
                        Math.sin(lat / k1)));

            progress.progressStart(height, "Generating Terrain");

            for (j = 0; j < height; ++j)
            {
                progress.progressStep(j);

                for (i = 0; i < width; ++i)
                {
                    x = ((2.0 * i) - width) / height / scale;
                    y = (((2.0 * j) - height) / height / scale) + y2;
                    zz = (x * x) + (y * y);

                    if (zz == 0.0)
                    {
                        theta1 = 0.0;
                    }
                    else
                    {
                        theta1 = k1 * Math.atan2(x, y);
                    }

                    if ((theta1 < -Math.PI) || (theta1 > Math.PI))
                    {
                        colours[i][j] = backgroundColour;

                        if (doShade)
                        {
                            shades[i][j] = 255;
                        }
                    }
                    else
                    {
                        /* theta1 is longitude */
                        theta1 += (lon - (0.5 * Math.PI));
                        theta2 = k1 * Math.asin((zz - c) / (zz + c));

                        /* theta2 is latitude */
                        if (
                            (theta2 > (0.5 * Math.PI)) ||
                                (theta2 < (-0.5 * Math.PI)))
                        {
                            colours[i][j] = backgroundColour;

                            if (doShade)
                            {
                                shades[i][j] = 255;
                            }
                        }
                        else
                        {
                            cos2 = Math.cos(theta2);
                            y = Math.sin(theta2);

                            if (y < ymin)
                            {
                                ymin = y;
                            }

                            if (y > ymax)
                            {
                                ymax = y;
                            }

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

            progress.progressComplete("Terrain Generated");

            if (hgrid != 0.0)
            {
                /* draw horizontal gridlines */
                for (theta1 = 0.0; theta1 > -90.0; theta1 -= hgrid)
                    ;

                for (theta1 = theta1; theta1 < 90.0; theta1 += hgrid)
                {
                    y = Math.sin(Math.toRadians(theta1));

                    if ((ymin <= y) && (y <= ymax))
                    {
                        zz = Math.sqrt(
                                (c * (1.0 +
                                    Math.sin(Math.toRadians(theta1) / k1))) / (1.0 -
                                    Math.sin(Math.toRadians(theta1) / k1)));

                        for (
                            theta2 = -Math.PI + lon; theta2 < (Math.PI + lon);
                                theta2 += (0.5 / width / scale))
                        {
                            z1 = theta2 - lon;
                            x1 = zz * Math.sin(z1 / k1);
                            y1 = zz * Math.cos(z1 / k1);

                            i = (int) (0.5 * ((height * scale * x1) + width));
                            j = (int) (0.5 * ((height * scale * (y1 - y2)) +
                                    height));

                            if (
                                (0 <= i) && (i < width) && (0 <= j) &&
                                    (j < height))
                            {
                                colours[i][j] = BLACK;
                            }
                        }
                    }
                }
            }

            if (vgrid != 0.0)
            {
                /* draw vertical gridlines */
                for (
                    theta1 = -0.5 * Math.PI; theta1 < (0.5 * Math.PI);
                        theta1 += (0.5 / width / scale))
                {
                    y = Math.sin(theta1);

                    if ((ymin <= y) && (y <= ymax))
                    {
                        zz = Math.sqrt(
                                (c * (1.0 + Math.sin(theta1 / k1))) / (1.0 -
                                    Math.sin(theta1 / k1)));

                        for (
                            theta2 = 0.0;
                                theta2 > (-180.0 + Math.toDegrees(lon));
                                theta2 -= vgrid)
                            ;

                        for (
                            theta2 = theta2;
                                theta2 < (180.0 + Math.toDegrees(lon));
                                theta2 += vgrid)
                        {
                            z1 = Math.toRadians(theta2) - lon;
                            x1 = zz * Math.sin(z1 / k1);
                            y1 = zz * Math.cos(z1 / k1);

                            i = (int) (0.5 * ((height * scale * x1) + width));
                            j = (int) (0.5 * ((height * scale * (y1 - y2)) +
                                    height));

                            if (
                                (0 <= i) && (i < width) && (0 <= j) &&
                                    (j < height))
                            {
                                colours[i][j] = BLACK;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            k1 = 1.0 / Math.sin(lat);
            c = k1 * k1;
            y2 = Math.sqrt(
                    (c * (1.0 - Math.sin(lat / k1))) / (1.0 +
                        Math.sin(lat / k1)));

            progress.progressStart(height, "Generating Terrain");

            for (j = 0; j < height; ++j)
            {
                progress.progressStep(j);

                for (i = 0; i < width; ++i)
                {
                    x = ((2.0 * i) - width) / height / scale;
                    y = (((2.0 * j) - height) / height / scale) - y2;
                    zz = (x * x) + (y * y);

                    if (zz == 0.0)
                    {
                        theta1 = 0.0;
                    }
                    else
                    {
                        theta1 = -k1 * Math.atan2(x, -y);
                    }

                    if ((theta1 < -Math.PI) || (theta1 > Math.PI))
                    {
                        colours[i][j] = backgroundColour;

                        if (doShade)
                        {
                            shades[i][j] = 255;
                        }
                    }
                    else
                    {
                        /* theta1 is longitude */
                        theta1 += (lon - (0.5 * Math.PI));
                        theta2 = k1 * Math.asin((zz - c) / (zz + c));

                        /* theta2 is latitude */
                        if (
                            (theta2 > (0.5 * Math.PI)) ||
                                (theta2 < (-0.5 * Math.PI)))
                        {
                            colours[i][j] = backgroundColour;

                            if (doShade)
                            {
                                shades[i][j] = 255;
                            }
                        }
                        else
                        {
                            cos2 = Math.cos(theta2);
                            y = Math.sin(theta2);

                            if (y < ymin)
                            {
                                ymin = y;
                            }

                            if (y > ymax)
                            {
                                ymax = y;
                            }

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

            progress.progressComplete("Terrain Generated");

            if (hgrid != 0.0)
            {
                /* draw horizontal gridlines */
                for (theta1 = 0.0; theta1 > -90.0; theta1 -= hgrid)
                    ;

                for (theta1 = theta1; theta1 < 90.0; theta1 += hgrid)
                {
                    y = Math.sin(Math.toRadians(theta1));

                    if ((ymin <= y) && (y <= ymax))
                    {
                        zz = Math.sqrt(
                                (c * (1.0 +
                                    Math.sin(Math.toRadians(theta1) / k1))) / (1.0 -
                                    Math.sin(Math.toRadians(theta1) / k1)));

                        for (
                            theta2 = -Math.PI + lon; theta2 < (Math.PI + lon);
                                theta2 += (0.5 / width / scale))
                        {
                            z1 = theta2 - lon;
                            x1 = -zz * Math.sin(z1 / k1);
                            y1 = -zz * Math.cos(z1 / k1);

                            i = (int) (0.5 * ((height * scale * x1) + width));
                            j = (int) (0.5 * ((height * scale * (y1 + y2)) +
                                    height));

                            if (
                                (0 <= i) && (i < width) && (0 <= j) &&
                                    (j < height))
                            {
                                colours[i][j] = BLACK;
                            }
                        }
                    }
                }
            }

            if (vgrid != 0.0)
            {
                /* draw vertical gridlines */
                for (
                    theta1 = -0.5 * Math.PI; theta1 < (0.5 * Math.PI);
                        theta1 += (0.5 / width / scale))
                {
                    y = Math.sin(theta1);

                    if ((ymin <= y) && (y <= ymax))
                    {
                        zz = Math.sqrt(
                                (c * (1.0 + Math.sin(theta1 / k1))) / (1.0 -
                                    Math.sin(theta1 / k1)));

                        for (
                            theta2 = 0.0;
                                theta2 > (-180.0 + Math.toDegrees(lon));
                                theta2 -= vgrid)
                            ;

                        for (
                            theta2 = theta2;
                                theta2 < (180.0 + Math.toDegrees(lon));
                                theta2 += vgrid)
                        {
                            z1 = Math.toRadians(theta2) - lon;
                            x1 = -zz * Math.sin(z1 / k1);
                            y1 = -zz * Math.cos(z1 / k1);

                            i = (int) (0.5 * ((height * scale * x1) + width));
                            j = (int) (0.5 * ((height * scale * (y1 + y2)) +
                                    height));

                            if (
                                (0 <= i) && (i < width) && (0 <= j) &&
                                    (j < height))
                            {
                                colours[i][j] = BLACK;
                            }
                        }
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
        return "Conical Projection";
    }

    /**
     * Get the name of the thumbnail image to use for this projection
     *
     * @return The name of the thumbmail image
     */
    public String getThumbnailName()
    {
        return "t_conical";
    }
}
