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
 * TriangleMeshTerrainPlanet.java
 *
 * Created on December 29, 2005, 1:58 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Terrain mesh for a planet
 *
 * @author martin
 * @version $Id: TriangleMeshTerrainPlanet.java,v 1.4 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleMeshTerrainPlanet extends TriangleMeshTerrain
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleMeshTerrainPlanet.class);

    /** Our icosahedeon */
    private TriangleMeshSubdividedIcosahedron tmsi;

    /**
     * Creates a new instance of TriangleMeshTerrainPlanet
     *
     * @param param The terrain parameters to be used
     * @param progress The object to report progress to
     * @param utils The utility object for random numbers
     */
    public TriangleMeshTerrainPlanet(
        TerrainParameters param, Progress progress, MathUtils utils)
    {
        super(progress, new SphericalGeometry(utils));

        this.tmsi = new TriangleMeshSubdividedIcosahedron(
                1.0f + (param.getVariation().getZ() * param.getBaseHeight()),
                param.getSubdivisions(), param.getSubdivisionsUnperturbed(),
                utils, param.getVariation(), progress);

        this.setTriangles(this.tmsi.getTriangles());
        this.setVertices(this.tmsi.getVertices());

        if (!param.isDelayGeneration())
        {
            doTerrain(param);
        }
    }

    /**
     * Creates a new instance of TriangleMeshTerrainPlanet
     *
     * @param param The terrain parameters to be used
     * @param progress The object to report progress to
     */
    public TriangleMeshTerrainPlanet(
        TerrainParameters param, Progress progress)
    {
        this(param, progress, new MathUtils());
    }
}
