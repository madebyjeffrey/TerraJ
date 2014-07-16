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
 * TriangleMeshCloudPlanet.java
 *
 * Created on 18 April 2006, 15:54
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.CloudParameters;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that represents a spherical cloud layer
 *
 * @author  martin
 * @version $Id: TriangleMeshCloudPlanet.java,v 1.6 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleMeshCloudPlanet extends TriangleMeshCloud
    implements TriangleMesh
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(TriangleMeshCloudPlanet.class);

    /**
     * Creates a new instance of TriangleMeshCloudPlanet
     *
     * @param progress The progress object to be used
     * @param geometry The geometry object to be used
     * @param param The terrain parameters object
     * @param cp The cloud parameters object
     */
    public TriangleMeshCloudPlanet(
        Progress progress, Geometry geometry, TerrainParameters param,
        CloudParameters cp)
    {
        super(progress, geometry);

        this.parameters = parameters;

        MathUtils utils = new MathUtils(new Random(cp.getSeed()));

        int subdivisions = param.getSubdivisions();

        if (cp.isUseOwnSubdivisions())
        {
            subdivisions = cp.getSubdivisions();
        }

        // This is slighty messy but the original relied on multiple inheritance
        // and we don't have that option
        TriangleMeshSubdividedIcosahedron tmsi =
            new TriangleMeshSubdividedIcosahedron(
                1.0f + (cp.getHeight() / 100.0f), subdivisions, subdivisions,
                utils, ImmutableXYZ.XYZ_ZERO, progress);

        setTriangles(tmsi.getTriangles());
        setVertices(tmsi.getVertices());

        doCloud(cp);
    }

    /**
     * Holds value of property parameters.
     */
    private CloudParameters parameters;

    /**
     * Getter for property parameters.
     * @return Value of property parameters.
     */
    public CloudParameters getParameters()
    {
        return this.parameters;
    }

    /**
     * Setter for property parameters.
     * @param parameters New value of property parameters.
     */
    public void setParameters(CloudParameters parameters)
    {
        this.parameters = parameters;
    }

    /**
     * Not used in this case, for terrains only. Has to be here because of
     * inheritance issues. Original C++ used multiple inheritance
     *
     * @param parameters The terrain parameters
     */
    public void doColours(TerrainParameters parameters)
    {
    }
}
