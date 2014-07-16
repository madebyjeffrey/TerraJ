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
 * GenStar.java
 *
 * Created on December 22, 2005, 11:26 PM
 */
package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Random star generation with realistic, mass, luminosity and spectral class.
 * Converted to Java from public domain C code written by Andrew Folkins
 *
 * @author martin
 * @version $Id: GenStar.java,v 1.14 2006/07/06 06:58:33 martin Exp $
 */
public class GenStar
{
    /** Minimum generated magnitude */
    public static final int MIN_MAGNITUDE = -7;

    /** Maximum generated magnitude */
    public static final int MAX_MAGNITUDE = 16;

    /** Number of spectral classes in tables */
    public static final int N_SPC_CLASS = 7;

    /** Number of magnitude classes in tables */
    public static final int N_MAG_CLASS = 24;

    /** Number of luminosity classes in tables */
    public static final int N_LUM_CLASS = 8;

    /** Map of classes to numbers */
    private Map<String, Integer> classMap = new HashMap<String, Integer>();
    
    // RequireThis OFF: MIN_MAGNITUDE
    // RequireThis OFF: MAX_MAGNITUDE
    // RequireThis OFF: N_SPC_CLASS
    // RequireThis OFF: N_MAG_CLASS
    // RequireThis OFF: N_LUM_CLASS

    /** Our logging object */
    private static Log log = LogFactory.getLog(GenStar.class);

    // MS: These should be enums but they were used to index arrays directly so
    // I left them as constants for now as they were in the original code

    /** Constant for spectral class O */
    public static final int SPECTRAL_CLASS_O = 0;

    /** Constant for spectral class B */
    public static final int SPECTRAL_CLASS_B = 1;

    /** Constant for spectral class A */
    public static final int SPECTRAL_CLASS_A = 2;

    /** Constant for spectral class F */
    public static final int SPECTRAL_CLASS_F = 3;

    /** Constant for spectral class G */
    public static final int SPECTRAL_CLASS_G = 4;

    /** Constant for spectral class K */
    public static final int SPECTRAL_CLASS_K = 5;

    /** Constant for spectral class M */
    public static final int SPECTRAL_CLASS_M = 6;

    /** Constant for lumoinosity class Ia */
    public static final int LUMINOSITY_CLASS_IA = 0;

    /** Constant for lumoinosity class Ib */
    public static final int LUMINOSITY_CLASS_IB = 1;

    /** Constant for lumoinosity class II */
    public static final int LUMINOSITY_CLASS_II = 2;

    /** Constant for lumoinosity class III */
    public static final int LUMINOSITY_CLASS_III = 3;

    /** Constant for lumoinosity class IV */
    public static final int LUMINOSITY_CLASS_IV = 4;

    /** Constant for lumoinosity class V */
    public static final int LUMINOSITY_CLASS_V = 5;

    /** Constant for lumoinosity class VI */
    public static final int LUMINOSITY_CLASS_VI = 6;

    /** Constant for lumoinosity class VII */
    public static final int LUMINOSITY_CLASS_VII = 7;

    /** Object used to obtain random numbers */
    private MathUtils utils;

    // MagicNumber OFF

    /**
     * This table gives the approximate relative numbers of stars of the
     * various spectral types and luminosity classes, the units are stars
     * per million cubic parsecs. The program totals this information
     * and computes a cumulative distribution function from it for
     * actual use.  The spectral classes range from O on the left to M
     * on the right, the luminosities from an absolute magnitude of -7
     * at the top to +16 at the bottom.  Thus, the table looks roughly
     * like the traditional Hertzsprung-Russell diagram.
     *
     * One thing you'll notice about this, there's a *lot* of red dwarfs
     * in a realistic distribution, a star like the sun is in the top 10%
     * of the population.  This makes the occurance of habitable planets
     * pretty low.
     *
     * Most of this information is from a message I recived from John Carr
     * &lt;athena.mit.edu!jfc&gt; on April 19/88, he didn't mention his source
     * though he did make the comment that "the birthrate function is much
     * flatter at high luminosities than the luminosity function, due to
     * the short lifetime of high-mass stars.  This is important for young
     * areas."  I don't think that idea is accounted for here.
     */
    private double[][] starCounts =
        {
            /*        O        B        A        F        G        K        M       Mag */
            {0.0002, 0.0005, 0.0003, 0.0003, 0.00003, 0, 0 }, /*  -7 */
            { 0.0005, 0.0025, 0.001, 0.001, 0.0001, 0.0004, 0.0004 }, /*  -6 */
            { 0.001, 0.025, 0.01, 0.006, 0.008, 0.004, 0.01 }, /*  -5 */
            { 0.003, 0.16, 0.01, 0.016, 0.025, 0.012, 0.012 }, /*  -4 */
            { 0.01, 0.5, 0.05, 0.08, 0.08, 0.1, 0.06 }, /*  -3 */
            { 0.01, 2.5, 0.08, 0.2, 0.3, 0.6, 0.4 }, /*  -2 */
            { 0.01, 12.5, 1, 1.6, 1, 2.5, 3 }, /*  -1 */
            { 0.001, 20, 20, 2, 8, 25, 10 }, /*  +0 */
            { 0, 30, 100, 30, 30, 120, 10 }, /*  +1 */
            { 0, 20, 200, 160, 50, 110, 0 }, /*  +2 */
            { 0, 10, 80, 700, 150, 100, 0 }, /*  +3 */
            { 0, 0, 30, 1200, 700, 100, 0 }, /*  +4 */
            { 0, 0, 0, 600, 2000, 300, 0 }, /*  +5 */
            { 0, 0, 0, 200, 1500, 1500, 10 }, /*  +6 */
            { 0, 0, 0, 100, 800, 3000, 100 }, /*  +7 */
            { 0, 0, 0, 10, 400, 2500, 1000 }, /*  +8 */
            { 0, 0, 0, 0, 200, 1500, 3000 }, /*  +9 */
            { 0, 10, 0, 0, 0, 400, 8000 }, /* +10 */
            { 0, 100, 30, 10, 0, 200, 9000 }, /* +11 */
            { 0, 200, 400, 100, 0, 100, 10000 }, /* +12 */
            { 0, 400, 600, 300, 100, 400, 10000 }, /* +13 */
            { 0, 800, 1000, 1000, 600, 800, 10000 }, /* +14 */
            { 0, 1500, 2000, 1000, 1500, 1200, 8000 }, /* +15 */
            { 0, 3000, 5000, 3000, 3000, 0, 6000 }, /* +16 */
        };

    /** A table of star counts by magnitude class and spectral class */
    private double[][] starCountsClass = new double[N_MAG_CLASS][N_SPC_CLASS];

    /** Absolute magnitude - anything of number or below is that class
     * e.g Class G, mag 5.0 is class V.  Most of this is guesstimates
     * from a H-R diagram.
     */
    private double[][] lClassMag =
        {
            /*   O      B       A      F      G      K      M */
            {-6.5, -6.5, -6.5, -6.5, -6.5, -6.5, -6.5 }, /* Ia */
            { -6.0, -6.0, -5.0, -5.0, -5.0, -5.0, -5.0 }, /* Ib */
            { -5.0, -3.5, -3.0, -2.0, -2.0, -2.5, -2.5 }, /* II */
            { -4.0, -3.0, -0.5, 1.5, 2.5, 3.0, 2.0 }, /* III */
            { -3.0, -2.0, 0.5, 2.5, 3.5, 5.5, 2.0 }, /* IV */
            { -1.0, 2.0, 2.5, 5.0, 7.0, 10.0, 14.0 }, /* V */
            { 1.0, 4.0, 5.0, 9.0, 10.0, 20.0, 20.0 }, /* VI */
            { 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0 }, /* VII */
        };

    /** Array of names of the spectral classes */
    private String[] spectralClass = { "O", "B", "A", "F", "G", "K", "M" };

    /** Array of names of the luminosity classes */
    private String[] luminosityClass =
        { "Ia", "Ib", "II", "III", "IV", "V", "VI", "VII" };

    /**
     * Creates a new instance of GenStar
     */
    public GenStar()
    {
        this.utils = new MathUtils();

        computeProbabilitiesByClass();
        computeProbabilities();
        
        classMap.put("O", 0);
        classMap.put("B", 1);
        classMap.put("A", 2);
        classMap.put("F", 3);
        classMap.put("G", 4);
        classMap.put("K", 5);
        classMap.put("M", 6);
    }

    /**
     * Creates a new instance of GenStar
     *
     * @param utils The object to use to obtain random numbers
     */
    public GenStar(MathUtils utils)
    {
        this.utils = utils;

        computeProbabilitiesByClass();
        computeProbabilities();
    }

    /**
     * Compute the table of probabilities by class
     */
    protected void computeProbabilitiesByClass()
    {
        int i;
        int j;
        double t;
        double total;

        for (j = 0; j < N_SPC_CLASS; ++j)
        {
            total = 0.0;

            for (i = 0; i < N_MAG_CLASS; ++i)
            {
                total += starCounts[i][j];
            }

            t = 0.0;

            for (i = 0; i < this.N_MAG_CLASS; ++i)
            {
                t += this.starCounts[i][j];
                this.starCountsClass[i][j] = t / total;
            }
        }
    }

    /**
     * Compute a cumulative distribution from the values in StarCounts[][]
     * by adding up all the values, then dividing each entry in the array
     * by the total.
     */
    protected void computeProbabilities()
    {
        int i;
        int j;
        double t;
        double total;

        total = 0.0;

        for (i = 0; i < this.N_MAG_CLASS; ++i)
        {
            for (j = 0; j < this.N_SPC_CLASS; ++j)
                total += this.starCounts[i][j];
        }

        t = 0.0;

        for (i = 0; i < this.N_MAG_CLASS; ++i)
        {
            for (j = 0; j < this.N_SPC_CLASS; ++j)
            {
                t += this.starCounts[i][j];
                this.starCounts[i][j] = t / total;
            }
        }
    }

    /**
     * Set the seed of the random number object
     *
     * @param seed The new seed value to be used
     */
    protected void setSeed(long seed)
    {
        this.utils.setSeed(seed);
    }

    /**
     * Generate and return a new random star of a particlar magnitude and
     * spectral class
     *
     * @param magClass The magnitude class of the desired star
     * @param specClass The spectral class of the desired star
     * @return A randomly generated star of the specified class
     */
    public Primary generateStar(int magClass, int specClass)
    {
        final Primary sun = new BasicPrimary();

        int loopI;
        double t;

        int mClass = magClass;

        final String starSpectralClass = this.spectralClass[specClass];
        sun.setSpectralClass(starSpectralClass);

        t = this.utils.nextDouble();
        /* Give it a random subclass */
        sun.setSpectralSubclass((int) (t * 10.0));
        sun.setAbsoluteMagnitude(MIN_MAGNITUDE + mClass + t);

        // TODO: check whether needed, as luminosity set by stargen

        /* Compute luminosity relative to Sun */
        sun.setLuminosity(Math.pow(2.5118, 4.7 - sun.getAbsoluteMagnitude()));

        mClass = -1;

        for (loopI = 0; (loopI < N_LUM_CLASS) && (mClass < 0); ++loopI)
        {
            if (lClassMag[loopI][specClass] >= sun.getAbsoluteMagnitude())
            {
                mClass = loopI;
            }
        }

        sun.setLuminosityClass(this.luminosityClass[mClass]);

        switch (mClass)
        {
            case 0:
            case 1:
            case 2:
            case 3:
                /* Supergiants & giants */
                t = Math.log(sun.getLuminosity()) +
                    (this.utils.nextDouble() / 5.0);
                sun.setMass(Math.exp(t / 3.0));

                break;

            case 4:
            case 5:
            case 6:
                /* subgiants, dwarfs, subdwarfs */
                t = Math.log(sun.getLuminosity()) + 0.1 +
                    ((this.utils.nextDouble() / 5.0) - 0.1);
                sun.setMass(Math.exp(t / 4.1));

                break;

            case 7:
                /* white dwarfs */
                sun.setMass((0.7 * this.utils.nextDouble()) + 0.6);

                break;

            default:
                log.error("Assertion error, reached unexpected statement");
                throw new AssertionError("Unexpected program state");
        }

        sun.setREcosphere(Math.sqrt(sun.getLuminosity()));
        /* next calc is approximate */
        sun.setREcosphereInner(0.93 * sun.getREcosphere());
        sun.setREcosphereOuter(1.1 * sun.getREcosphere());
        
        // assign these parameters entirely randomly for now
        
        sun.setRightAscension(this.utils.nextDouble() * 360.0);
        sun.setDeclination((this.utils.nextDouble() * 180.0) - 90.0);
        sun.setDistance((this.utils.nextDouble() * 500.0) + 10.0);

        return sun;
    }

    /**
     * Generate and return a new star according to the probability
     * distribution calculated from the data tables in this class
     *
     * @return A randomly generated star object
     */
    public Primary generateStar()
    {
        int i = -1;
        int j = -1;

        final double rnd = this.utils.nextDouble();

        int loopI;
        int loopJ;

        for (loopI = 0; (loopI < this.N_MAG_CLASS) && (i < 0); ++loopI)
        {
            for (loopJ = 0; (loopJ < this.N_SPC_CLASS) && (i < 0); ++loopJ)
            {
                if (this.starCounts[loopI][loopJ] >= rnd)
                {
                    i = loopI;
                    j = loopJ;
                }
            }
        }

        return generateStar(i, j);
    }
    
    /**
     * Generate a star given a text format spectral class 
     *
     * @param specClass The desired spectral class of the star to be generated.
     * @return A randomly generated star of the specified spectral class. An
     * <code>IllegalArgumentException</code> is thrown if the spectral class is 
     * not one of those known to this class.
     */
    public Primary generateStar(String specClass)
    {
        Integer theClass = classMap.get(specClass);
        
        if (theClass == null)
        {
            log.error("Unknown spectral class requested in GenStar: " + specClass);
            
            throw new IllegalArgumentException("Unknown spectral class: " + specClass);
        }
        
        return generateStar(theClass);
    }

    /**
     * Generate and return a new random star of a particlar spectral class
     *
     * @param specClass The spectral class of the desired star
     * @return A randomly generated star of the specified class
     */
    public Primary generateStar(int specClass)
    {
        int i = -1;

        final double rnd = this.utils.nextDouble();

        int loopI;

        for (loopI = 0; (loopI < this.N_MAG_CLASS) && (i < 0); ++loopI)
        {
            if (this.starCountsClass[loopI][specClass] >= rnd)
            {
                i = loopI;
            }
        }

        return generateStar(i, specClass);
    }

    /**
     * Getter for property utils.
     * @return Value of property utils.
     */
    public MathUtils getUtils()
    {
        return this.utils;
    }
}
