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
 * BasicNoise.java
 *
 * Created on December 28, 2005, 4:28 PM
 *
 */
package com.alvermont.terraj.fracplanet.noise;

import com.alvermont.terraj.fracplanet.geom.RandomXYZSphereNormal;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * BasicNoise class
 *
 * @author martin
 * @version $Id: BasicNoise.java,v 1.5 2006/07/06 06:58:36 martin Exp $
 */
public class BasicNoise implements Noise
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(BasicNoise.class);

    // RequireThis OFF: N

    /** The utils object to use for random numbers */
    private MathUtils utils;

    /** The sizes of the arrays */
    private static final int N = 256;
    private int[] p = new int[N + N + 2];
    private XYZ[] g = new XYZ[N + N + 2];

    // MagicNumber OFF

    /**
     * Creates a new instance of BasicNoise
     *
     * @param utils The utils object to use for random numbers
     */
    public BasicNoise(MathUtils utils)
    {
        this.utils = utils;

        // Create an array of random gradient vectors uniformly on the unit sphere
        for (int i = 0; i < N; ++i)
            g[i] = new RandomXYZSphereNormal(utils);

        // Create a pseudorandom permutation of [1..B]
        for (int i = 0; i <= N; ++i)
            p[i] = i;

        for (int i = N; i > 0; i -= 2)
        {
            final int j = (int) utils.nextFloat() * N;
            final int k = p[i];

            p[i] = p[j];
            p[j] = k;
        }

        // Extend g and p arrays to allow for faster indexing
        for (int i = 0; i < (N + 2); ++i)
        {
            p[N + i] = p[i];
            g[N + i] = g[i];
        }
    }

    private float value(XYZ q, float rx, float ry, float rz)
    {
        return (rx * q.getX()) + (ry * q.getY()) + (rz * q.getZ());
    }

    private float surve(float t)
    {
        return t * t * (3.0f - (2.0f * t));
    }

    private float lerp(float t, float a, float b)
    {
        return a + (t * (b - a));
    }

    /**
     * Calculate and return the noise value at a particular point according
     * to the noise generation algorithm in use by this object
     *
     * @param pt The point to calculate noise for
     * @return The corresponding noise value
     */
    public float getNoise(XYZ pt)
    {
        // Crank up the frequency a bit otherwise don't see much variation in base case
        final float tx = (2.0f * pt.getX()) + 10000.0f;
        final float ty = (2.0f * pt.getY()) + 10000.0f;
        final float tz = (2.0f * pt.getZ()) + 10000.0f;

        final int itx = (int) (tx);
        final int ity = (int) (ty);
        final int itz = (int) (tz);

        final int bx0 = (itx & (N - 1));
        final int bx1 = ((bx0 + 1) & (N - 1));
        final int by0 = (ity & (N - 1));
        final int by1 = ((by0 + 1) & (N - 1));
        final int bz0 = (itz & (N - 1));
        final int bz1 = ((bz0 + 1) & (N - 1));

        final int i = p[bx0];
        final int b00 = p[i + by0];
        final int b01 = p[i + by1];

        final int j = p[bx1];
        final int b10 = p[j + by0];
        final int b11 = p[j + by1];

        final float rx0 = tx - itx;
        final float ry0 = ty - ity;
        final float rz0 = tz - itz;

        final float rx1 = rx0 - 1.0f;
        final float ry1 = ry0 - 1.0f;
        final float rz1 = rz0 - 1.0f;

        final float sx = surve(rx0);

        final float a0 =
            lerp(
                sx, value(g[b00 + bz0], rx0, ry0, rz0),
                value(g[b10 + bz0], rx1, ry0, rz0));
        final float b0 =
            lerp(
                sx, value(g[b01 + bz0], rx0, ry1, rz0),
                value(g[b11 + bz0], rx1, ry1, rz0));
        final float a1 =
            lerp(
                sx, value(g[b00 + bz1], rx0, ry0, rz1),
                value(g[b10 + bz1], rx1, ry0, rz1));
        final float b1 =
            lerp(
                sx, value(g[b01 + bz1], rx0, ry1, rz1),
                value(g[b11 + bz1], rx1, ry1, rz1));

        final float sy = surve(ry0);

        final float c = lerp(sy, a0, b0);
        final float d = lerp(sy, a1, b1);

        final float sz = surve(rz0);

        return 1.5f * lerp(sz, c, d);
    }
}
