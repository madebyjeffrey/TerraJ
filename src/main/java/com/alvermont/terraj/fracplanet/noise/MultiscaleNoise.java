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
 * MultiscaleNoise.java
 *
 * Created on December 28, 2005, 4:50 PM
 *
 */
package com.alvermont.terraj.fracplanet.noise;

import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.geom.XYZMath;
import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Multiscale noise class, applies multiple different Noise generators
 * with a decay.
 *
 * @author martin
 * @version $Id: MultiscaleNoise.java,v 1.4 2006/07/06 06:58:36 martin Exp $
 */
public class MultiscaleNoise implements Noise
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(MultiscaleNoise.class);

    /** A list containing the noise generators */
    private List<BasicNoise> noise = new ArrayList<BasicNoise>();

    /** A list of amplitudes to be applied with each generator (accounting for decay) */
    private List<Double> amplitude = new ArrayList<Double>();

    /**
     * Creates a new instance of MultiscaleNoise
     *
     * @param utils The utils object to use for random numbers
     * @param terms The number of noise terms to be applied
     * @param decay The decay factor as the terms are stepped through
     */
    public MultiscaleNoise(MathUtils utils, int terms, float decay)
    {
        float k = 1.0f;
        float kt = 0.0f;

        for (int i = 0; i < terms; ++i)
        {
            this.noise.add(new BasicNoise(utils));
            this.amplitude.add(new Double(k));

            kt += k;
            k *= decay;
        }

        for (int i = 0; i < terms; ++i)
        {
            this.amplitude.set(i, this.amplitude.get(i) / kt);
        }
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
        float v = 0.0f;

        for (int i = 0; i < this.noise.size(); ++i)
        {
            v += this.amplitude.get(i) * this.noise.get(i)
                .getNoise(XYZMath.opMultiply(pt, (1 << i)));
        }

        return v;
    }
}
