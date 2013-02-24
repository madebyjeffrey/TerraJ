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
 * MercatorProjection.java
 *
 * Created on December 23, 2005, 3:02 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.planet.AllPlanetParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Does a mercator projection based on the generated planet
 *
 * @author martin
 * @version $Id: MercatorProjection.java,v 1.8 2006/07/06 06:58:34 martin Exp $
 */
public class MercatorProjection extends AbstractProjector implements Projector
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(MercatorProjection.class);

    /** Creates a new instance of MercatorProjection */
    public MercatorProjection()
    {
    }

    /**
     * Creates a new instance of MercatorProjection
     *
     * @param params The parameters to be used
     */
    public MercatorProjection(AllPlanetParameters params)
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

        double y;
        double scale1;
        double cos2;
        double theta1;
        int i;
        int j;
        int k;

        y = Math.sin(lat);
        y = (1.0 + y) / (1.0 - y);
        y = 0.5 * Math.log(y);
        k = (int) ((0.5 * y * width * scale) / Math.PI);

        progress.progressStart(height, "Generating Terrain");

        for (j = 0; j < height; ++j)
        {
            //            if (debug && ((j % (Height/25)) == 0))
            //            {fprintf(stderr, "%c", view); fflush(stderr);}
            progress.progressStep(j);

            y = (Math.PI * ((2.0 * (j - k)) - height)) / width / scale;
            y = Math.exp(2. * y);
            y = (y - 1.) / (y + 1.);

            scale1 = (scale * width) / height / Math.sqrt(1.0 - (y * y)) / Math.PI;

            cos2 = Math.sqrt(1.0 - (y * y));
            depth = (3 * ((int) (log2(scale1 * height)))) + 3;

            for (i = 0; i < width; ++i)
            {
                theta1 = lon - (0.5 * Math.PI) +
                    ((Math.PI * ((2.0 * i) - width)) / width / scale);
                colours[i][j] = (short) planet0(
                        Math.cos(theta1) * cos2, y, -Math.sin(theta1) * cos2);

                if (doShade)
                {
                    shades[i][j] = shade;
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
                y = (1.0 + y) / (1.0 - y);
                y = 0.5 * Math.log(y);
                j = (height / 2) + (int) ((0.5 * y * width * scale) / Math.PI) +
                    k;

                if ((j >= 0) && (j < height))
                {
                    for (i = 0; i < width; ++i)
                        colours[i][j] = BLACK;
                }
            }
        }

        if (vgrid != 0.0)
        {
            /* draw vertical gridlines */
            for (theta1 = 0.0; theta1 > -360.0; theta1 -= vgrid)
                ;

            for (theta1 = theta1; theta1 < 360.0; theta1 += vgrid)
            {
                i = (int) (0.5 * width * (1.0 +
                        ((scale * (Math.toRadians(theta1) - lon)) / Math.PI)));

                if ((i >= 0) && (i < width))
                {
                    for (j = 0; j < height; ++j)
                        colours[i][j] = BLACK;
                }
            }
        }

        if (doShade)
        {
            smoothshades();
        }
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
        return "Mercator Projection";
    }

    /**
     * Get the name of the thumbnail image to use for this projection
     *
     * @return The name of the thumbmail image
     */
    public String getThumbnailName()
    {
        return "t_mercator";
    }
}
