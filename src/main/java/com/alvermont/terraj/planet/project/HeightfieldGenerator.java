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
 * HeightfieldGenerator.java
 *
 * Created on December 24, 2005, 4:06 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.planet.AllPlanetParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class doesn't implemenent the Projector interface as it doesn't
 * generate an image, instead it retains the raw height values.
 *
 * @author martin
 * @version $Id: HeightfieldGenerator.java,v 1.7 2006/07/06 06:58:34 martin Exp $
 */
public class HeightfieldGenerator extends AbstractProjector
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(HeightfieldGenerator.class);

    /** Storage for the height data */
    int[][] heights;

    /** Creates a new instance of HeightfieldGenerator */
    public HeightfieldGenerator()
    {
    }

    /**
     * Creates a new instance of HeightfieldGenerator
     *
     * @param params The parameters to be used
     */
    public HeightfieldGenerator(AllPlanetParameters params)
    {
        super(params);
    }

    /**
     * Generate the heightfield data
     */
    public void generate()
    {
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

        heights = new int[width][height];

        final double sla = Math.sin(lat);
        final double cla = Math.cos(lat);
        final double slo = Math.sin(lon);
        final double clo = Math.cos(lon);

        cacheParameters();

        depth = (3 * ((int) (log2(scale * height)))) + 6;

        double x;
        double y;
        double z;
        double x1;
        double y1;
        double z1;

        for (int j = 0; j < height; ++j)
        {
            for (int i = 0; i < width; ++i)
            {
                x = ((2.0 * i) - width) / height / scale;
                y = ((2.0 * j) - height) / height / scale;

                if (((x * x) + (y * y)) > 1.0)
                {
                    heights[i][j] = 0;
                }
                else
                {
                    z = Math.sqrt(1.0 - (x * x) - (y * y));

                    x1 = (clo * x) + (slo * sla * y) + (slo * cla * z);
                    y1 = (cla * y) - (sla * z);
                    z1 = (-slo * x) + (clo * sla * y) + (clo * cla * z);
                    
                    final double heightValue = planet1(x1, y1, z1);

                    heights[i][j] = (int) (10000000.0 * heightValue);
                }
            }
        }
    }
    
    /** 
     * Get the height value at a coordinate
     */
    public int getHeightAt(int x, int y)
    {
        return heights[x][y];
    }

    /**
     * Free any resources used by this object
     */
    public void releaseResources()
    {
        super.releaseResources();

        this.heights = null;
    }
}
