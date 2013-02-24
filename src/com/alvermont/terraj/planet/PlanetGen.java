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
 * PlanetGen.java
 *
 * Created on December 23, 2005, 2:20 PM
 *
 */
package com.alvermont.terraj.planet;

import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Ported to java from the original C code by Torben AE. Mogensen
 *
 * @author martin
 * @version $Id: PlanetGen.java,v 1.12 2006/07/06 06:58:36 martin Exp $
 */
public class PlanetGen
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(PlanetGen.class);

    // RequireThis OFF: log

    /** The random number generator in use */
    protected Random random = new Random();

    /** The seed that was initially used by this random number generator */
    protected double seed;

    /** depth of subdivision to be carried out */
    protected int depth;

    /** Value used in shading */
    protected short shade = 0;

    /** Random seed 1 */
    private double r1;

    /** Random seed 2 */
    private double r2;

    /** Random seed 3 */
    private double r3;

    /** Random seed 4 */
    private double r4;

    /** Saved value of the square root of 2 to avoid repeated calculation */
    protected static final double SQRT2 = Math.sqrt(2.0);

    /** Saved value of the square root of 6 to avoid repeated calculation */
    protected static final double SQRT6 = Math.sqrt(6.0);

    /** Saved value of the square root of 8 to avoid repeated calculation */
    protected static final double SQRT8 = Math.sqrt(8.0);

    /** Cached value of dd1 */
    private double dd1;

    /** Cached value of dd1 */
    private double dd2;

    /** Cached value of distance power */
    private double pow;

    /** Cached value of shading */
    private boolean doShade;

    /** Cached value of shade angle */
    private double shadeAngle;

    /** Cached value of initial altitude */
    private double m;

    /**
     * Must be called to cache parameters in this object for performance.
     * Call this method once before starting a projection.
     */
    public void cacheParameters()
    {
        dd1 = parameters.getPlanetParameters()
                .getAltitudeDifferenceWeight();
        dd2 = parameters.getPlanetParameters()
                .getDistanceWeight();
        pow = parameters.getPlanetParameters()
                .getPower();
        doShade = parameters.getProjectionParameters()
                .isDoShade();
        shadeAngle = parameters.getProjectionParameters()
                .getShadeAngle();
        m = parameters.getPlanetParameters()
                .getInitialAltitude();
    }

    // values used during the calculations
    double ssa;

    // values used during the calculations
    double ssb;

    // values used during the calculations
    double ssc;

    // values used during the calculations
    double ssd;

    // values used during the calculations
    double ssas;

    // values used during the calculations
    double ssbs;

    // values used during the calculations
    double sscs;

    // values used during the calculations
    double ssds;

    // values used during the calculations
    double ssax;

    // values used during the calculations
    double ssay;

    // values used during the calculations
    double ssaz;

    // values used during the calculations
    double ssbx;

    // values used during the calculations
    double ssby;

    // values used during the calculations
    double ssbz;

    // values used during the calculations
    double sscx;

    // values used during the calculations
    double sscy;

    // values used during the calculations
    double sscz;

    // values used during the calculations
    double ssdx;

    // values used during the calculations
    double ssdy;

    // values used during the calculations
    double ssdz;

    /**
     * Carries out the remaining levels of terrain generation, recursivel
     * until the last level is reached. Returns the height of the planet
     * at this point
     *
     * @param a Altitude of vertex 1
     * @param b Altitude of vertex 2
     * @param c Altitude of vertex 3
     * @param d Altitude of vertex 4
     * @param as Seed of vertex 1
     * @param bs Seed of vertex 2
     * @param cs Seed of vertex 3
     * @param ds Seed of vertex 4
     * @param ax Vertex 1 X coordinate
     * @param ay Vertex 1 Y coordinate
     * @param az Vertex 1 Z coordinate
     * @param bx Vertex 2 X coordinate
     * @param by Vertex 2 Y coordinate
     * @param bz Vertex 2 Z coordinate
     * @param cx Vertex 3 X coordinate
     * @param cy Vertex 3 Y coordinate
     * @param cz Vertex 3 Z coordinate
     * @param dx Vertex 4 X coordinate
     * @param dy Vertex 4 Y coordinate
     * @param dz Vertex 4 Z coordinate
     * @param x Goal point X coordinate
     * @param y Goal point Y coordinate
     * @param z Goal point Z coordinate
     * @param level Number of remaining levels
     * @return The altitude of the planet after recursive subdivision
     */
    double planet(
        double a, double b, double c, double d, double as, double bs, double cs,
        double ds, double ax, double ay, double az, double bx, double by,
        double bz, double cx, double cy, double cz, double dx, double dy,
        double dz, double x, double y, double z, int level)
    {
        double abx;
        double aby;
        double abz;
        double acx;
        double acy;
        double acz;
        double adx;
        double ady;
        double adz;
        double bcx;
        double bcy;
        double bcz;
        double bdx;
        double bdy;
        double bdz;
        double cdx;
        double cdy;
        double cdz;
        double lab;
        double lac;
        double lad;
        double lbc;
        double lbd;
        double lcd;
        double ex;
        double ey;
        double ez;
        double e;
        double es;
        double es1;
        double es2;
        double es3;
        double eax;
        double eay;
        double eaz;
        double epx;
        double epy;
        double epz;
        double ecx;
        double ecy;
        double ecz;
        double edx;
        double edy;
        double edz;
        double x1;
        double y1;
        double z1;
        double x2;
        double y2;
        double z2;
        double l1;
        double tmp;

        if (level > 0)
        {
            if (level == 11)
            {
                this.ssa = a;
                this.ssb = b;
                this.ssc = c;
                this.ssd = d;
                this.ssas = as;
                this.ssbs = bs;
                this.sscs = cs;
                this.ssds = ds;
                this.ssax = ax;
                this.ssay = ay;
                this.ssaz = az;
                this.ssbx = bx;
                this.ssby = by;
                this.ssbz = bz;
                this.sscx = cx;
                this.sscy = cy;
                this.sscz = cz;
                this.ssdx = dx;
                this.ssdy = dy;
                this.ssdz = dz;
            }

            abx = ax - bx;
            aby = ay - by;
            abz = az - bz;
            acx = ax - cx;
            acy = ay - cy;
            acz = az - cz;
            lab = (abx * abx) + (aby * aby) + (abz * abz);
            lac = (acx * acx) + (acy * acy) + (acz * acz);

            if (lab < lac)
            {
                return (planet(
                    a, c, b, d, as, cs, bs, ds, ax, ay, az, cx, cy, cz, bx, by,
                    bz, dx, dy, dz, x, y, z, level));
            }
            else
            {
                adx = ax - dx;
                ady = ay - dy;
                adz = az - dz;
                lad = (adx * adx) + (ady * ady) + (adz * adz);

                if (lab < lad)
                {
                    return (planet(
                        a, d, b, c, as, ds, bs, cs, ax, ay, az, dx, dy, dz, bx,
                        by, bz, cx, cy, cz, x, y, z, level));
                }
                else
                {
                    bcx = bx - cx;
                    bcy = by - cy;
                    bcz = bz - cz;
                    lbc = (bcx * bcx) + (bcy * bcy) + (bcz * bcz);

                    if (lab < lbc)
                    {
                        return (planet(
                            b, c, a, d, bs, cs, as, ds, bx, by, bz, cx, cy, cz,
                            ax, ay, az, dx, dy, dz, x, y, z, level));
                    }
                    else
                    {
                        bdx = bx - dx;
                        bdy = by - dy;
                        bdz = bz - dz;
                        lbd = (bdx * bdx) + (bdy * bdy) + (bdz * bdz);

                        if (lab < lbd)
                        {
                            return (planet(
                                b, d, a, c, bs, ds, as, cs, bx, by, bz, dx, dy,
                                dz, ax, ay, az, cx, cy, cz, x, y, z, level));
                        }
                        else
                        {
                            cdx = cx - dx;
                            cdy = cy - dy;
                            cdz = cz - dz;
                            lcd = (cdx * cdx) + (cdy * cdy) + (cdz * cdz);

                            if (lab < lcd)
                            {
                                return (planet(
                                    c, d, a, b, cs, ds, as, bs, cx, cy, cz, dx,
                                    dy, dz, ax, ay, az, bx, by, bz, x, y, z,
                                    level));
                            }
                            else
                            {
                                es = rand2(as, bs);
                                es1 = rand2(es, es);
                                es2 = 0.5 + (0.1 * rand2(es1, es1));
                                es3 = 1.0 - es2;

                                if (ax == bx)
                                { /* very unlikely to ever happen */
                                    ex = (0.5 * ax) + (0.5 * bx);
                                    ey = (0.5 * ay) + (0.5 * by);
                                    ez = (0.5 * az) + (0.5 * bz);
                                }
                                else if (ax < bx)
                                {
                                    ex = (es2 * ax) + (es3 * bx);
                                    ey = (es2 * ay) + (es3 * by);
                                    ez = (es2 * az) + (es3 * bz);
                                }
                                else
                                {
                                    ex = (es3 * ax) + (es2 * bx);
                                    ey = (es3 * ay) + (es2 * by);
                                    ez = (es3 * az) + (es2 * bz);
                                }

                                if (lab > 1.0)
                                {
                                    lab = Math.pow(lab, 0.75);
                                }

                                e = (0.5 * (a + b)) +
                                    (es * dd1 * Math.abs(a - b)) +
                                    (es1 * dd2 * Math.pow(lab, pow));
                                eax = ax - ex;
                                eay = ay - ey;
                                eaz = az - ez;
                                epx = x - ex;
                                epy = y - ey;
                                epz = z - ez;
                                ecx = cx - ex;
                                ecy = cy - ey;
                                ecz = cz - ez;
                                edx = dx - ex;
                                edy = dy - ey;
                                edz = dz - ez;

                                if (
                                    ((((eax * ecy * edz) + (eay * ecz * edx) +
                                        (eaz * ecx * edy)) - (eaz * ecy * edx) -
                                        (eay * ecx * edz) - (eax * ecz * edy)) * (((epx * ecy * edz) +
                                        (epy * ecz * edx) + (epz * ecx * edy)) -
                                        (epz * ecy * edx) - (epy * ecx * edz) -
                                        (epx * ecz * edy))) > 0.0)
                                {
                                    return (planet(
                                        c, d, a, e, cs, ds, as, es, cx, cy, cz,
                                        dx, dy, dz, ax, ay, az, ex, ey, ez, x, y,
                                        z, level - 1));
                                }
                                else
                                {
                                    return (planet(
                                        c, d, b, e, cs, ds, bs, es, cx, cy, cz,
                                        dx, dy, dz, bx, by, bz, ex, ey, ez, x, y,
                                        z, level - 1));
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            if (this.doShade)
            {
                x1 = 0.25 * (ax + bx + cx + dx);
                x1 = (a * (x1 - ax)) + (b * (x1 - bx)) + (c * (x1 - cx)) +
                    (d * (x1 - dx));
                y1 = 0.25 * (ay + by + cy + dy);
                y1 = (a * (y1 - ay)) + (b * (y1 - by)) + (c * (y1 - cy)) +
                    (d * (y1 - dy));
                z1 = 0.25 * (az + bz + cz + dz);
                z1 = (a * (z1 - az)) + (b * (z1 - bz)) + (c * (z1 - cz)) +
                    (d * (z1 - dz));
                l1 = Math.sqrt((x1 * x1) + (y1 * y1) + (z1 * z1));

                if (l1 == 0.0)
                {
                    l1 = 1.0;
                }

                tmp = Math.sqrt(1.0 - (y * y));

                if (tmp < 0.0001)
                {
                    tmp = 0.0001;
                }

                x2 = (x * x1) + (y * y1) + (z * z1);
                y2 = (((-x * y) / tmp * x1) + (tmp * y1)) -
                    ((z * y) / tmp * z1);
                z2 = (-z / tmp * x1) + (x / tmp * z1);
                shade = (short) ((((-Math.sin(
                        (Math.PI * this.shadeAngle) / 180.0) * y2) -
                    (Math.cos((Math.PI * shadeAngle) / 180.0) * z2)) / l1 * 48.0) +
                    128.0);

                if (this.shade < 10)
                {
                    this.shade = 10;
                }

                if (this.shade > 255)
                {
                    this.shade = 255;
                }
            }

            return ((a + b + c + d) / 4);
        }
    }

    /**
     * Top level of planetary terrain generation. This carries out the
     * first level of subdivision and returns the height of a point
     *
     * @param x The x coordinate of the point to get altitude for
     * @param y The y coordinate of the point to get altitude for
     * @param z The z coordinate of the point to get altitude for
     * @return The altiude of the planet at this point (0 = sea level)
     */
    public double planet1(double x, double y, double z)
    {
        double abx;
        double aby;
        double abz;
        double acx;
        double acy;
        double acz;
        double adx;
        double ady;
        double adz;
        double apx;
        double apy;
        double apz;
        double bax;
        double bay;
        double baz;
        double bcx;
        double bcy;
        double bcz;
        double bdx;
        double bdy;
        double bdz;
        double bpx;
        double bpy;
        double bpz;

        abx = this.ssbx - this.ssax;
        aby = this.ssby - this.ssay;
        abz = this.ssbz - this.ssaz;
        acx = this.sscx - this.ssax;
        acy = this.sscy - this.ssay;
        acz = this.sscz - this.ssaz;
        adx = this.ssdx - this.ssax;
        ady = this.ssdy - this.ssay;
        adz = this.ssdz - this.ssaz;
        apx = x - this.ssax;
        apy = y - this.ssay;
        apz = z - this.ssaz;

        if (
            ((((adx * aby * acz) + (ady * abz * acx) + (adz * abx * acy)) -
                (adz * aby * acx) - (ady * abx * acz) - (adx * abz * acy)) * (((apx * aby * acz) +
                (apy * abz * acx) + (apz * abx * acy)) - (apz * aby * acx) -
                (apy * abx * acz) - (apx * abz * acy))) > 0.0)
        {
            /* p is on same side of abc as d */
            if (
                ((((acx * aby * adz) + (acy * abz * adx) + (acz * abx * ady)) -
                    (acz * aby * adx) - (acy * abx * adz) - (acx * abz * ady)) * (((apx * aby * adz) +
                    (apy * abz * adx) + (apz * abx * ady)) - (apz * aby * adx) -
                    (apy * abx * adz) - (apx * abz * ady))) > 0.0)
            {
                /* p is on same side of abd as c */
                if (
                    ((((abx * ady * acz) + (aby * adz * acx) +
                        (abz * adx * acy)) - (abz * ady * acx) -
                        (aby * adx * acz) - (abx * adz * acy)) * (((apx * ady * acz) +
                        (apy * adz * acx) + (apz * adx * acy)) -
                        (apz * ady * acx) - (apy * adx * acz) -
                        (apx * adz * acy))) > 0.0)
                {
                    /* p is on same side of acd as b */
                    bax = -abx;
                    bay = -aby;
                    baz = -abz;
                    bcx = this.sscx - this.ssbx;
                    bcy = this.sscy - this.ssby;
                    bcz = this.sscz - this.ssbz;
                    bdx = this.ssdx - this.ssbx;
                    bdy = this.ssdy - this.ssby;
                    bdz = this.ssdz - this.ssbz;
                    bpx = x - this.ssbx;
                    bpy = y - this.ssby;
                    bpz = z - this.ssbz;

                    if (
                        ((((bax * bcy * bdz) + (bay * bcz * bdx) +
                            (baz * bcx * bdy)) - (baz * bcy * bdx) -
                            (bay * bcx * bdz) - (bax * bcz * bdy)) * (((bpx * bcy * bdz) +
                            (bpy * bcz * bdx) + (bpz * bcx * bdy)) -
                            (bpz * bcy * bdx) - (bpy * bcx * bdz) -
                            (bpx * bcz * bdy))) > 0.0)
                    {
                        /* p is on same side of bcd as a */
                        /* Hence, p is inside tetrahedron */
                        return (planet(
                            this.ssa, this.ssb, this.ssc, this.ssd, this.ssas,
                            this.ssbs, this.sscs, this.ssds, this.ssax,
                            this.ssay, this.ssaz, this.ssbx, this.ssby,
                            this.ssbz, this.sscx, this.sscy, this.sscz,
                            this.ssdx, this.ssdy, this.ssdz, x, y, z, 11));
                    }
                }
            }
        } /* otherwise */
        return (planet(
            /* initial altitude is M on all corners of tetrahedron */
        this.m, this.m, this.m, this.m, /* same seed set is used in every call */
        this.r1, this.r2, this.r3, this.r4, /* coordinates of vertices */
        0.0, 0.0, 3.01, 0.0, this.SQRT8 + (.01 * this.r1 * this.r1),
            -1.02 + (.01 * this.r2 * this.r3),
            -this.SQRT6 - (.01 * this.r3 * this.r3),
            -this.SQRT2 - (.01 * this.r4 * this.r4),
            -1.02 + (.01 * this.r1 * this.r2),
            this.SQRT6 - (.01 * this.r2 * this.r2),
            -this.SQRT2 - (.01 * this.r3 * this.r3),
            -1.02 + (.01 * this.r1 * this.r3), /* coordinates of point we want colour of */
        x, y, z, this.depth));

        /* subdivision depth */
    }

    /* random number generator taking two seeds */
    /* rand2(p,q) = rand2(q,p) is important     */
    double rand2(double p, double q)
    {
        final double r = (p + 3.14159265) * (q + 3.14159265);

        return ((2. * (r - (int) r)) - 1.);
    }

    /**
     * Set up the random seeds that are to be used
     */
    protected void setSeeds()
    {
        this.seed = this.r1;

        this.r1 = rand2(this.r1, this.r1);
        this.r2 = rand2(this.r1, this.r1);
        this.r3 = rand2(this.r1, this.r2);
        this.r4 = rand2(this.r2, this.r3);

        log.debug(
            "Random seeds this.r1=" + this.r1 + " this.r2=" + this.r2 +
            " this.r3=" + this.r3 + " this.r4=" + this.r4);
    }

    /**
     * Creates a new instance of PlanetGen
     *
     * @param params The parameters that are to be used
     */
    public PlanetGen(AllPlanetParameters params)
    {
        this.parameters = params;
        this.r1 = parameters.getPlanetParameters()
                .getSeed();

        setSeeds();
    }

    /**
     * Holds value of property parameters.
     */
    private com.alvermont.terraj.planet.AllPlanetParameters parameters;

    /**
     * Getter for property parameters.
     * @return Value of property parameters.
     */
    public com.alvermont.terraj.planet.AllPlanetParameters getParameters()
    {
        return this.parameters;
    }

    /**
     * Setter for property parameters.
     * @param parameters New value of property parameters.
     */
    public void setParameters(
        com.alvermont.terraj.planet.AllPlanetParameters parameters)
    {
        this.parameters = parameters;
        this.r1 = parameters.getPlanetParameters()
                .getSeed();

        setSeeds();
    }
}
