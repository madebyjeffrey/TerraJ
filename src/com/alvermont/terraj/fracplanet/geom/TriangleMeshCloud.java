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
 * TriangleMeshCloud.java
 *
 * Created on 18 April 2006, 15:28
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.CloudParameters;
import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.noise.MultiscaleNoise;
import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A mesh used to represent a cloud layer
 *
 * @author  martin
 * @version $Id: TriangleMeshCloud.java,v 1.6 2006/07/06 06:58:35 martin Exp $
 */
public abstract class TriangleMeshCloud extends AbstractTriangleMesh
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(TriangleMeshCloud.class);

    /**
     * Creates a new instance of TriangleMeshCloud
     *
     * @param progress The object to use for reporting progress
     * @param geometry The geometry object to be used
     */
    public TriangleMeshCloud(Progress progress, Geometry geometry)
    {
        super(progress, geometry);
    }

    /**
     * Do the cloud alpha colouring based on a noise function
     *
     * @param parameters The cloud parameters
     */
    protected void doCloud(final CloudParameters parameters)
    {
        computeVertexNormals();

        getProgress()
            .progressStart(100, "Cloud colouring");

        final ByteRGBA c = new ByteRGBA(parameters.getColour());

        //! \todo Wire up terms, decay and base fequency and thresholds
        MultiscaleNoise noise =
            new MultiscaleNoise(
                new MathUtils(new Random(parameters.getSeed())), 6, 0.5f);

        for (int i = 0; i < getVertexCount(); ++i)
        {
            getProgress()
                .progressStep((100 * i) / getVertexCount());

            final float v =
                0.5f +
                0.5f * noise.getNoise(
                        XYZMath.opMultiply(
                            4.0f, getVertices().get(i).getPosition()));
            final float v_min = 0.5f;
            final float v_max = 0.6f;
            final float v_k = 1.0f / (v_max - v_min);
            final float vs = Math.min(1.0f, Math.max(0.0f, (v - v_min) * v_k));

            ByteRGBA col =
                new ByteRGBA(
                    c.getR(), c.getG(), c.getB(), (byte) (Byte.MAX_VALUE * vs));

            getVertices()
                .get(i)
                .setColour(0, col);

            // Set other colour (unused) to red for debug
            getVertices()
                .get(i)
                .setColour(1, new ByteRGBA(255, 0, 0, 255));
        }

        getProgress()
            .progressComplete("Cloud colouring completed");

        // TODO: Eliminate all-transparent triangles & unused vertices.
        // Leave if nothing left

        // TODO: Bias weather into temperate bands (maybe not)
        getProgress()
            .progressStart(100, "Weather systems");

        MathUtils utils = new MathUtils(new Random(parameters.getSeed()));

        final int steps = 100 * getVertexCount();
        int step = 0;

        // note following block was commented out in C++ and hasn't been
        // translated to Java yet.

        //        if (false) //for (uint i=0;i<0;i++) // Number of twisters parameter
        //        {
        //            final uint random_vertex=static_cast<uint>(r01()*vertices());
        //            final XYZ position(vertex(random_vertex).position());
        //            final XYZ axis(geometry().up(position));
        //            
        //            // Rotate opposite direction in other hemisphere
        //            final float strength=r01()*(position.z<0.0 ? -M_PI : M_PI);
        //            
        //            for (uint j=0;j<vertices();j++)
        //            {
        //                progress_step((100*step)/steps);
        //                step++;
        //                
        //                final XYZ p(vertex(j).position());
        //                final XYZ pn=geometry().up(p);
        //                final float pna=pn%axis;
        //                
        //                if (pna>0.0f)  // Don't create same feature on other side of planet (actually the distance would be big so could drop this)
        //                {
        //                    final float distance=(p-position).magnitude();
        //                    final float rotation_angle=strength*exp(-10.0*distance);
        //                    
        //                    // Now rotate p about axis through position by the rotation angle
        //                    // TODO: Optimise.  axis and position is the same for all points; we're constantly recomputing the basis change matrices.
        //                    // Create a stateful version of Matrix34RotateAboutAxisThrough.
        //                    vertex(j).position
        //                            (
        //                            Matrix34RotateAboutAxisThrough(axis,rotation_angle,position)*p
        //                            );
        //                }
        //            }
        //        }
        getProgress()
            .progressComplete("Weather systems completed");

        setSwitchColour(getTriangleCount());
    }
}
