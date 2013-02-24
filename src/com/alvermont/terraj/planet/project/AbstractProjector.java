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
 * AbstractProjector.java
 *
 * Created on December 23, 2005, 6:31 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.fracplanet.util.DummyProgress;
import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.planet.AllPlanetParameters;
import com.alvermont.terraj.planet.PlanetGen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for projections
 *
 * @author martin
 * @version $Id: AbstractProjector.java,v 1.16 2006/07/07 07:47:18 martin Exp $
 */
public abstract class AbstractProjector extends PlanetGen
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(AbstractProjector.class);
    short[][] colours;
    short[][] shades;

    /** Object for progress reporting */
    protected Progress progress = new DummyProgress();

    /** Fixed colour index for black */
    public static final int BLACK = 0;

    /** Fixed colour index for white */
    public static final int WHITE = 1;

    /** Fixed colour index for first blue shade */
    public static final int BLUE0 = 2;

    /** The maximum value of a component */
    private static final int MAX_COMP_VALUE = 255;

    // RequireThis OFF: MAX_COMP_VALUE

    // colour indices for other colours
    int colourBlue1;

    // colour indices for other colours
    int colourLand0;

    // colour indices for other colours
    int colourLand1;

    // colour indices for other colours
    int colourLand2;

    // colour indices for other colours
    int colourLand4;
    int colourGreen1;
    int colourBrown0;
    int colourGrey0;

    /** The background colour */
    short backgroundColour = BLACK;

    /** Number of colours in use */
    int nocols = 256;

    /** No of entries in colour table */
    private static final int COLOUR_TABLE_SIZE = 256;

    // RequireThis OFF: COLOUR_TABLE_SIZE

    /** Table of red colour values */
    private int[] rtable = new int[COLOUR_TABLE_SIZE];

    /** Table of green colour values */
    private int[] gtable = new int[COLOUR_TABLE_SIZE];

    /** Table of blue colour values */
    private int[] btable = new int[COLOUR_TABLE_SIZE];

    // MagicNumber OFF

    /** An alternate set of colours */
    private int[][] altColorTable =
        {
            { 0, 0, 192 }, /* Dark blue depths                */
            { 0, 128, 255 }, /* Light blue shores        */
            { 0, 96, 0 }, /* Dark green Lowlands        */
            { 0, 224, 0 }, /* Light green Highlands        */
            { 128, 176, 0 }, /* Brown mountainsides        */
            { 128, 128, 128 }, /* Grey stoney peaks        */
            { 255, 255, 255 }, /* White - peaks                */
            { 0, 0, 0 }, /* Black - Space                */
            { 0, 0, 0 } /* Black - Lines            */
        };

    /** Index into the array for depths */
    private static final int C_INDEX_DEPTHS = 0;

    /** Index into the array for shores */
    private static final int C_INDEX_SHORES = 1;

    /** Index into the array for low areas */
    private static final int C_INDEX_LOW = 2;

    /** Index into the array for highlands */
    private static final int C_INDEX_HIGH = 3;

    /** Index into the array for mountains */
    private static final int C_INDEX_MOUNT = 4;

    /** Index into the array for stony peaks */
    private static final int C_INDEX_STONE = 5;

    /** Index into the array for peaks */
    private static final int C_INDEX_PEAK = 6;

    /** Index into the array for space */
    private static final int C_INDEX_SPACE = 7;

    /** Index into the array for lines */
    private static final int C_INDEX_LINES = 8;

    // RequireThis OFF: C_INDEX_DEPTHS
    // RequireThis OFF: C_INDEX_SHORES
    // RequireThis OFF: C_INDEX_LOW
    // RequireThis OFF: C_INDEX_HIGH
    // RequireThis OFF: C_INDEX_MOUNT
    // RequireThis OFF: C_INDEX_STONE
    // RequireThis OFF: C_INDEX_PEAK
    // RequireThis OFF: C_INDEX_SPACE
    // RequireThis OFF: C_INDEX_LINES

    // MagicNumber ON

    /**
     * Creates a new instance of AbstractProjector
     */
    public AbstractProjector()
    {
        super(new AllPlanetParameters());
    }

    /**
     * Creates a new instance of AbstractProjector
     *
     * @param params The parameters object that this projector will use
     */
    public AbstractProjector(AllPlanetParameters params)
    {
        super(params);
    }

    /**
     * Creates a new instance of AbstractProjector
     *
     * @param params The parameters object that this projector will use
     * @param progress The object to use for progress reporting
     */
    public AbstractProjector(AllPlanetParameters params, Progress progress)
    {
        super(params);
        this.progress = progress;
    }

    /** Cached value of doShade */
    private boolean doShade;

    /**
     * This method must be called once before projection starts to cache the
     * parameter values in this object to avoid repeated calls to retrieve
     * them during the recursive terrain generation.
     */
    public void cacheParameters()
    {
        super.cacheParameters();

        setLatic(getParameters().getProjectionParameters().isLatic());
        setAltColors(getParameters().getProjectionParameters().isAltColors());
        setDoShade(getParameters().getProjectionParameters().isDoShade());
    }

    /**
     * Set up all the colour shades that will be used. The basic set of
     * colours are interpolated to give a range of shades
     */
    protected void setcolours()
    {
        int i;

        if (getParameters()
                .getProjectionParameters()
                .isAltColors())
        {
            final int crow;

            if (nocols < 8)
            {
                nocols = 8;
            }

            /*
             *        This color table tries to follow the coloring conventions of
             *        several atlases.
             *
             *        The first two colors are reserved for black and white
             *        1/4 of the colors are blue for the sea, dark being deep
             *        3/4 of the colors are land, divided as follows:
             *         nearly 1/2 of the colors are greens, with the low being dark
             *         1/8 of the colors shade from green through brown to grey
             *         1/8 of the colors are shades of grey for the highest altitudes
             *
             *        The minimum color table is:
             *            0        Black
             *            1        White
             *            2        Blue
             *            3        Dark Green
             *            4        Green
             *            5        Light Green
             *            6        Brown
             *            7        Grey
             *        and doesn't look very good. Somewhere between 24 and 32 colors
             *        is where this scheme starts looking good. 256, of course, is best.
             */
            this.colourLand0 = Math.max(nocols / 4, BLUE0 + 1);
            colourBlue1 = this.colourLand0 - 1;
            this.colourGrey0 = nocols - (nocols / 8);
            this.colourGreen1 = Math.min(
                    this.colourLand0 + (nocols / 2), this.colourGrey0 - 2);
            this.colourBrown0 = (this.colourGreen1 + this.colourGrey0) / 2;
            this.colourLand1 = nocols - 1;

            final int[][] colors =
                getParameters()
                    .getProjectionParameters()
                    .getColors();

            this.rtable[BLACK] = colors[C_INDEX_SPACE][0];
            this.gtable[BLACK] = colors[C_INDEX_SPACE][0];
            this.btable[BLACK] = colors[C_INDEX_SPACE][0];

            this.rtable[WHITE] = colors[C_INDEX_PEAK][0];
            this.gtable[WHITE] = colors[C_INDEX_PEAK][1];
            this.btable[WHITE] = colors[C_INDEX_PEAK][2];

            this.rtable[BLUE0] = colors[C_INDEX_DEPTHS][0];
            this.gtable[BLUE0] = colors[C_INDEX_DEPTHS][1];
            this.btable[BLUE0] = colors[C_INDEX_DEPTHS][2];

            for (i = BLUE0 + 1; i <= colourBlue1; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_DEPTHS][0] * (colourBlue1 -
                        i)) + (colors[C_INDEX_SHORES][0] * (i - BLUE0))) / (colourBlue1 -
                        BLUE0);
                this.gtable[i] = ((colors[C_INDEX_DEPTHS][1] * (colourBlue1 -
                        i)) + (colors[C_INDEX_SHORES][1] * (i - BLUE0))) / (colourBlue1 -
                        BLUE0);
                this.btable[i] = ((colors[C_INDEX_DEPTHS][2] * (colourBlue1 -
                        i)) + (colors[C_INDEX_SHORES][2] * (i - BLUE0))) / (colourBlue1 -
                        BLUE0);
            }

            for (i = this.colourLand0; i < this.colourGreen1; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_LOW][0] * (this.colourGreen1 -
                        i)) +
                    (colors[C_INDEX_HIGH][0] * (i - this.colourLand0))) / (this.colourGreen1 -
                        this.colourLand0);
                this.gtable[i] = ((colors[C_INDEX_LOW][1] * (this.colourGreen1 -
                        i)) +
                    (colors[C_INDEX_HIGH][1] * (i - this.colourLand0))) / (this.colourGreen1 -
                        this.colourLand0);
                this.btable[i] = ((colors[C_INDEX_LOW][2] * (this.colourGreen1 -
                        i)) +
                    (colors[C_INDEX_HIGH][2] * (i - this.colourLand0))) / (this.colourGreen1 -
                        this.colourLand0);
            }

            for (i = this.colourGreen1; i < this.colourBrown0; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_HIGH][0] * (this.colourBrown0 -
                        i)) +
                    (colors[C_INDEX_MOUNT][0] * (i - this.colourGreen1))) / (this.colourBrown0 -
                        this.colourGreen1);
                this.gtable[i] = ((colors[C_INDEX_HIGH][1] * (this.colourBrown0 -
                        i)) +
                    (colors[C_INDEX_MOUNT][1] * (i - this.colourGreen1))) / (this.colourBrown0 -
                        this.colourGreen1);
                this.btable[i] = ((colors[C_INDEX_HIGH][2] * (this.colourBrown0 -
                        i)) +
                    (colors[C_INDEX_MOUNT][2] * (i - this.colourGreen1))) / (this.colourBrown0 -
                        this.colourGreen1);
            }

            for (i = this.colourBrown0; i < this.colourGrey0; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_MOUNT][0] * (this.colourGrey0 -
                        i)) +
                    (colors[C_INDEX_STONE][0] * (i - this.colourBrown0))) / (this.colourGrey0 -
                        this.colourBrown0);
                this.gtable[i] = ((colors[C_INDEX_MOUNT][1] * (this.colourGrey0 -
                        i)) +
                    (colors[C_INDEX_STONE][1] * (i - this.colourBrown0))) / (this.colourGrey0 -
                        this.colourBrown0);
                this.btable[i] = ((colors[C_INDEX_MOUNT][2] * (this.colourGrey0 -
                        i)) +
                    (colors[C_INDEX_STONE][2] * (i - this.colourBrown0))) / (this.colourGrey0 -
                        this.colourBrown0);
            }

            for (i = this.colourGrey0; i < nocols; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_STONE][0] * (nocols - i)) +
                    ((colors[C_INDEX_PEAK][0] + 1) * (i - this.colourGrey0))) / (nocols -
                        this.colourGrey0);
                this.gtable[i] = ((colors[C_INDEX_STONE][1] * (nocols - i)) +
                    ((colors[C_INDEX_PEAK][1] + 1) * (i - this.colourGrey0))) / (nocols -
                        this.colourGrey0);
                this.btable[i] = ((colors[C_INDEX_STONE][2] * (nocols - i)) +
                    ((colors[C_INDEX_PEAK][2] + 1) * (i - this.colourGrey0))) / (nocols -
                        this.colourGrey0);
            }
        }
        else
        {
            this.rtable[BLACK] = 0;
            this.gtable[BLACK] = 0;
            this.btable[BLACK] = 0;

            this.rtable[WHITE] = MAX_COMP_VALUE;
            this.gtable[WHITE] = MAX_COMP_VALUE;
            this.btable[WHITE] = MAX_COMP_VALUE;

            final int[][] colors =
                getParameters()
                    .getProjectionParameters()
                    .getColors();

            int lighter =
                getParameters()
                    .getProjectionParameters()
                    .getLighterColours();

            if (lighter > 0)
            {
                log.debug("Lightening colours by: " + lighter);
            }

            while (--lighter > 0)
            {
                int r;
                int c;
                double x;

                for (r = 0; r < 7; ++r)
                {
                    for (c = 0; c < 3; ++c)
                    {
                        x = Math.sqrt((double) colors[r][c] / 256.0);
                        colors[r][c] = (int) ((240.0 * x) + 16);
                    }
                }
            }

            colourBlue1 = ((nocols - 4) / 2) + BLUE0;

            if (colourBlue1 == BLUE0)
            {
                this.rtable[BLUE0] = colors[C_INDEX_DEPTHS][0];
                this.gtable[BLUE0] = colors[C_INDEX_DEPTHS][1];
                this.btable[BLUE0] = colors[C_INDEX_DEPTHS][2];
            }
            else
            {
                for (i = BLUE0; i <= colourBlue1; ++i)
                {
                    this.rtable[i] = ((colors[C_INDEX_DEPTHS][0] * (colourBlue1 -
                            i)) + (colors[C_INDEX_SHORES][0] * (i - BLUE0))) / (colourBlue1 -
                            BLUE0);
                    this.gtable[i] = ((colors[C_INDEX_DEPTHS][1] * (colourBlue1 -
                            i)) + (colors[C_INDEX_SHORES][1] * (i - BLUE0))) / (colourBlue1 -
                            BLUE0);
                    this.btable[i] = ((colors[C_INDEX_DEPTHS][2] * (colourBlue1 -
                            i)) + (colors[C_INDEX_SHORES][2] * (i - BLUE0))) / (colourBlue1 -
                            BLUE0);
                }
            }

            this.colourLand0 = colourBlue1 + 1;
            this.colourLand2 = nocols - 2;
            this.colourLand1 = (this.colourLand0 + this.colourLand2 + 1) / 2;

            for (i = this.colourLand0; i < this.colourLand1; ++i)
            {
                this.rtable[i] = ((colors[C_INDEX_LOW][0] * (this.colourLand1 -
                        i)) +
                    (colors[C_INDEX_HIGH][0] * (i - this.colourLand0))) / (this.colourLand1 -
                        this.colourLand0);
                this.gtable[i] = ((colors[C_INDEX_LOW][1] * (this.colourLand1 -
                        i)) +
                    (colors[C_INDEX_HIGH][1] * (i - this.colourLand0))) / (this.colourLand1 -
                        this.colourLand0);
                this.btable[i] = ((colors[C_INDEX_LOW][2] * (this.colourLand1 -
                        i)) +
                    (colors[C_INDEX_HIGH][2] * (i - this.colourLand0))) / (this.colourLand1 -
                        this.colourLand0);
            }

            if (this.colourLand1 == this.colourLand2)
            {
                this.rtable[this.colourLand1] = colors[C_INDEX_MOUNT][0];
                this.gtable[this.colourLand1] = colors[C_INDEX_MOUNT][1];
                this.btable[this.colourLand1] = colors[C_INDEX_MOUNT][2];
            }
            else
            {
                for (i = this.colourLand1; i <= this.colourLand2; ++i)
                {
                    this.rtable[i] = ((colors[C_INDEX_MOUNT][0] * (this.colourLand2 -
                            i)) +
                        (colors[C_INDEX_STONE][0] * (i - this.colourLand1))) / (this.colourLand2 -
                            this.colourLand1);
                    this.gtable[i] = ((colors[C_INDEX_MOUNT][1] * (this.colourLand2 -
                            i)) +
                        (colors[C_INDEX_STONE][1] * (i - this.colourLand1))) / (this.colourLand2 -
                            this.colourLand1);
                    this.btable[i] = ((colors[C_INDEX_MOUNT][2] * (this.colourLand2 -
                            i)) +
                        (colors[C_INDEX_STONE][2] * (i - this.colourLand1))) / (this.colourLand2 -
                            this.colourLand1);
                }
            }

            this.colourLand4 = nocols - 1;
            this.rtable[this.colourLand4] = colors[C_INDEX_PEAK][0];
            this.gtable[this.colourLand4] = colors[C_INDEX_PEAK][1];
            this.btable[this.colourLand4] = colors[C_INDEX_PEAK][2];
        }

        if (getParameters()
                .getProjectionParameters()
                .isReverseBackground())
        {
            if (backgroundColour == BLACK)
            {
                backgroundColour = WHITE;
            }
            else
            {
                backgroundColour = BLACK;
            }
        }
    }

    /**
     * Smooth the shading
     */
    protected void smoothshades()
    {
        int i;
        int j;

        final int width = getParameters()
                .getProjectionParameters()
                .getWidth();
        final int height =
            getParameters()
                .getProjectionParameters()
                .getHeight();

        for (i = 0; i < (width - 2); ++i)
        {
            for (j = 0; j < (height - 2); ++j)
                shades[i][j] = (short) (((4 * shades[i][j]) +
                    (2 * shades[i][j + 1]) + (2 * shades[i + 1][j]) +
                    shades[i + 1][j + 2] + 4) / 9);
        }
    }

    /**
     * The top level of terrain generation. Returns the colour index of the point
     * at the specified coordinates.
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param z The z coordinate of the point
     * @return The index into the colour table that is to be used for this point
     */
    int planet0(double x, double y, double z)
    {
        int colour;

        // get the terrain altitude at this point
        double alt = planet1(x, y, z);

        if (isAltColors())
        {
            double snow = .125;
            double tree = snow * 0.5;
            double bare = (tree + snow) / 2.;

            if (isLatic())
            {
                snow -= (.13 * (y * y * y * y * y * y));
                bare -= (.12 * (y * y * y * y * y * y));
                tree -= (.11 * (y * y * y * y * y * y));
            }

            if (alt > 0)
            {
                /* Land */
                if (alt > snow)
                {
                    /* Snow: White */
                    colour = WHITE;
                }
                else if (alt > bare)
                {
                    /* Snow: Grey - White */
                    colour = this.colourGrey0 +
                        (int) ((((1 + this.colourLand1) - this.colourGrey0) * (alt -
                            bare)) / (snow - bare));

                    if (colour > this.colourLand1)
                    {
                        colour = this.colourLand1;
                    }
                }
                else if (alt > tree)
                {
                    /* Bare: Brown - Grey */
                    colour = this.colourGreen1 +
                        (int) ((((1 + this.colourGrey0) - this.colourGreen1) * (alt -
                            tree)) / (bare - tree));

                    if (colour > this.colourGrey0)
                    {
                        colour = this.colourGrey0;
                    }
                }
                else
                {
                    /* Green: Green - Brown */
                    colour = this.colourLand0 +
                        (int) ((((1 + this.colourGreen1) - this.colourLand0) * (alt)) / (tree));

                    if (colour > this.colourGreen1)
                    {
                        colour = this.colourGreen1;
                    }
                }
            }
            else
            {
                /* Sea */
                alt = alt / 2;

                if (alt > snow)
                {
                    /* Snow: White */
                    colour = WHITE;
                }
                else if (alt > bare)
                {
                    colour = this.colourGrey0 +
                        (int) ((((1 + this.colourLand1) - this.colourGrey0) * (alt -
                            bare)) / (snow - bare));

                    if (colour > this.colourLand1)
                    {
                        colour = this.colourLand1;
                    }
                }
                else
                {
                    colour = colourBlue1 +
                        (int) ((colourBlue1 - BLUE0 + 1) * (25 * alt));

                    if (colour < BLUE0)
                    {
                        colour = BLUE0;
                    }
                }
            }
        }
        else
        {
            /* calculate colour */
            if (alt <= 0.)
            {
                /* if below sea level then */
                if (isLatic() && (((y * y) + alt) >= 0.98))
                {
                    /* white if close to poles */
                    colour = this.colourLand4;
                }
                else
                {
                    /* blue scale otherwise */
                    colour = colourBlue1 +
                        (int) ((colourBlue1 - BLUE0 + 1) * (10 * alt));

                    if (colour < BLUE0)
                    {
                        colour = BLUE0;
                    }
                }
            }
            else
            {
                if (isLatic())
                {
                    /* altitude adjusted with latitude */
                    alt += (0.10204 * y * y);
                }

                if (alt >= 0.1)
                {
                    /* if high then */
                    colour = this.colourLand4;
                }
                else
                {
                    colour = this.colourLand0 +
                        (int) ((this.colourLand2 - this.colourLand0 + 1) * (10 * alt));

                    /* else green to brown scale */
                    if (colour > this.colourLand2)
                    {
                        colour = this.colourLand2;
                    }
                }
            }
        }

        return (colour);
    }

    /**
     * Calculate a log in base 2
     *
     * @param val The value to calculate the logarithm in base 2 for
     * @return The logarithm to base 2 of the input value
     */
    protected double log2(double val)
    {
        return Math.log10(val) / Math.log10(2);
    }

    /**
     * Get the colour component of a pixel at the specified coordinates
     * the supplied array will be set to the correct colour.
     *
     *
     *
     * @param x The x coordinate to be retrieved
     * @param y The y coordinate to be retrieved
     * @param col The colour array to be used to store the colour
     */
    public void fillRGB(int x, int y, int[] col)
    {
        int s;
        int c;

        if (isDoShade())
        {
            s = shades[x][y];
            c = (s * this.btable[this.colours[x][y]]) / 150;

            if (c > MAX_COMP_VALUE)
            {
                c = MAX_COMP_VALUE;
            }

            col[2] = c;

            c = (s * this.gtable[this.colours[x][y]]) / 150;

            if (c > MAX_COMP_VALUE)
            {
                c = MAX_COMP_VALUE;
            }

            col[1] = c;

            c = (s * this.rtable[this.colours[x][y]]) / 150;

            if (c > MAX_COMP_VALUE)
            {
                c = MAX_COMP_VALUE;
            }

            col[0] = c;
        }
        else
        {
            col[0] = this.rtable[this.colours[x][y]];
            col[1] = this.gtable[this.colours[x][y]];
            col[2] = this.btable[this.colours[x][y]];
        }
    }

    /**
     * Draw the outline of the coasts
     *
     * @param isBW If <pre>true</pre> then we are drawing in black and white
     */
    protected void outline(boolean isBW)
    {
        int i;
        int j;
        int k;

        final int[] outx =
            new int[getParameters()
                .getProjectionParameters()
                .getWidth() * getParameters()
                .getProjectionParameters()
                .getHeight()];
        final int[] outy =
            new int[getParameters()
                .getProjectionParameters()
                .getWidth() * getParameters()
                .getProjectionParameters()
                .getHeight()];
        final int width = getParameters()
                .getProjectionParameters()
                .getWidth();
        final int height =
            getParameters()
                .getProjectionParameters()
                .getHeight();

        k = 0;

        for (i = 1; i < (width - 1); ++i)
        {
            for (j = 1; j < (height - 1); ++j)
            {
                if (
                    ((this.colours[i][j] >= BLUE0) &&
                        (this.colours[i][j] <= colourBlue1)) &&
                        ((this.colours[i - 1][j] >= this.colourLand0) ||
                        (this.colours[i + 1][j] >= this.colourLand0) ||
                        (this.colours[i][j - 1] >= this.colourLand0) ||
                        (this.colours[i][j + 1] >= this.colourLand0) ||
                        (this.colours[i - 1][j - 1] >= this.colourLand0) ||
                        (this.colours[i - 1][j + 1] >= this.colourLand0) ||
                        (this.colours[i + 1][j - 1] >= this.colourLand0) ||
                        (this.colours[i + 1][j + 1] >= this.colourLand0)))
                {
                    outx[k] = i;
                    outy[++k] = j;
                }
            }
        }

        if (isBW)
        {
            for (i = 0; i < width; ++i)
            {
                for (j = 0; j < height; ++j)
                {
                    if (this.colours[i][j] != BLACK)
                    {
                        this.colours[i][j] = WHITE;
                    }
                }
            }
        }

        while (--k > 0)
        {
            this.colours[outx[k]][outy[k]] = BLACK;
        }
    }

    protected void doOutlining()
    {
        if (getParameters()
                .getProjectionParameters()
                .isEdges())
        {
            outline(getParameters().getProjectionParameters().isOutline());
        }
    }

    /**
     * Release any resources held by this object
     */
    public void releaseResources()
    {
        colours = null;
        shades = null;
    }

    /**
     * Getter for property progress.
     * @return Value of property progress.
     */
    public Progress getProgress()
    {
        return this.progress;
    }

    /**
     * Setter for property progress.
     * @param progress New value of property progress.
     */
    public void setProgress(Progress progress)
    {
        this.progress = progress;
    }

    /** Holds value of property latic */
    private boolean latic;

    /**
     * Getter for property latic
     *
     * @return The value of property latic
     */
    public boolean isLatic()
    {
        return latic;
    }

    /**
     * Setter for property latic (latitude based colours)
     *
     * @param latic The new value of property latic
     */
    public void setLatic(boolean latic)
    {
        this.latic = latic;
    }

    /** Holds value of property altColors */
    private boolean altColors;

    /**
     * Getter for property altColors
     *
     * @return The value of property altColors
     */
    public boolean isAltColors()
    {
        return altColors;
    }

    /**
     * Setter for property altColors
     *
     * @param altColors The new value of property altColors
     */
    public void setAltColors(boolean altColors)
    {
        this.altColors = altColors;
    }

    /**
     * Getter for property doShade
     *
     * @return The value of property doShade
     */
    public boolean isDoShade()
    {
        return doShade;
    }

    /**
     * Setter for property doShade
     *
     * @param doShade The new value of property doShade
     */
    public void setDoShade(boolean doShade)
    {
        this.doShade = doShade;
    }
}
