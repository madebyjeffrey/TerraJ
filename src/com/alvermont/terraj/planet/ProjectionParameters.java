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
 * ProjectionParameters.java
 *
 * Created on 22 January 2006, 10:47
 */
package com.alvermont.terraj.planet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean class that holds the parameters for use by the projection
 *
 * @author  martin
 * @version $Id: ProjectionParameters.java,v 1.8 2006/07/06 06:58:36 martin Exp $
 */
public class ProjectionParameters
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ProjectionParameters.class);

    /** Creates a new instance of ProjectionParameters */
    public ProjectionParameters()
    {
        reset();
    }

    /** The basic set of colours */
    static final int[][] BASECOLORS =
        {
            /* Dark blue depths                */
            {0, 0, 255 },
            /* Light blue shores        */
            {0, 128, 255 },
            /* Light green lowlands        */
            {0, 255, 0 },
            /* Dark green highlands        */
            {64, 192, 16 },
            /* Dark green Mountains        */
            {64, 192, 16 },
            /* Brown stoney peaks        */
            {128, 128, 32 },
            /* White - peaks                */
            {255, 255, 255 },
            /* Black - Space                */
            {0, 0, 0 },
            /* Black - Lines                */
            {0, 0, 0 }
        };

    // RequireThis OFF: BASECOLORS

    /**
     * Duplicate a colour array and return a new copy
     *
     * @param source The colour array to be copied
     * @return A new copy of the colour array
     */
    protected int[][] copyColours(int[][] source)
    {
        final int[][] colours = new int[9][3];

        for (int a = 0; a < 8; ++a)
        {
            for (int b = 0; b < 3; ++b)
            {
                colours[a][b] = source[a][b];
            }
        }

        return colours;
    }

    /**
     * Reset all the parameters to their default values
     */
    public void reset()
    {
        this.altColors = false;
        this.lighterColours = 0;
        this.scale = 1.0;
        this.hgrid = 0.0;
        this.vgrid = 0.0;
        this.lat = 0.0;
        this.lon = 0.0;
        this.latic = false;
        this.reverseBackground = false;
        this.width = 800;
        this.height = 600;
        this.colors = copyColours(BASECOLORS);
        this.doShade = false;
        this.shadeAngle = 150;
        this.projectionName = "Orthographic Projection";
        this.outputFile = null;
        this.colourFile = null;
        this.outline = false;
        this.edges = false;
    }

    /**
     * Create a new instance of ProjectionParameters as a copy of another one
     *
     * @param source The object that will be used to initialize this one
     */
    public ProjectionParameters(ProjectionParameters source)
    {
        this.altColors = source.altColors;
        this.lighterColours = source.lighterColours;
        this.scale = source.scale;
        this.hgrid = source.hgrid;
        this.vgrid = source.vgrid;
        this.lat = source.lat;
        this.lon = source.lon;
        this.latic = source.latic;
        this.reverseBackground = source.reverseBackground;
        this.width = source.width;
        this.height = source.height;
        this.colors = copyColours(source.colors);
        this.doShade = source.doShade;
        this.shadeAngle = source.shadeAngle;
        this.projectionName = source.projectionName;
        this.edges = source.edges;
        this.outline = source.outline;
    }

    /**
     * Holds value of property scale.
     */
    private double scale = 1.0;

    /**
     * Getter for property scale.
     * @return Value of property scale.
     */
    public double getScale()
    {
        return this.scale;
    }

    /**
     * Setter for property scale.
     * @param scale New value of property scale.
     */
    public void setScale(double scale)
    {
        this.scale = scale;
    }

    /**
     * Holds value of property width.
     */
    private int width;

    /**
     * Getter for property width.
     * @return Value of property width.
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Setter for property width.
     * @param width New value of property width.
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * Holds value of property height.
     */
    private int height;

    /**
     * Getter for property height.
     * @return Value of property height.
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * Holds value of property altColors.
     */
    private boolean altColors;

    /**
     * Getter for property altColours.
     * @return Value of property altColours.
     */
    public boolean isAltColors()
    {
        return this.altColors;
    }

    /**
     * Setter for property altColors.
     * @param altColors New value of property altColors.
     */
    public void setAltColors(boolean altColors)
    {
        this.altColors = altColors;
    }

    /**
     * Holds value of property latic.
     */
    private boolean latic;

    /**
     * Getter for property latic.
     * @return Value of property latic.
     */
    public boolean isLatic()
    {
        return this.latic;
    }

    /**
     * Setter for property latic.
     * @param latic New value of property latic.
     */
    public void setLatic(boolean latic)
    {
        this.latic = latic;
    }

    /**
     * Holds value of property lat.
     */
    private double lat;

    /**
     * Getter for property latitudeRadians.
     * @return Value of property lat.
     */
    public double getLatitudeRadians()
    {
        return Math.toRadians(this.lat);
    }

    /**
     * Getter for property lat.
     * @return Value of property lat.
     */
    public double getLat()
    {
        return this.lat;
    }

    /**
     * Setter for property lat.
     * @param lat New value of property lat.
     */
    public void setLat(double lat)
    {
        this.lat = lat;
    }

    /**
     * Holds value of property lon.
     */
    private double lon;

    /**
     * Getter for property lon.
     * @return Value of property lon.
     */
    public double getLon()
    {
        return this.lon;
    }

    /**
     * Getter for property longitudeRadians.
     * @return Value of property lon.
     */
    public double getLongitudeRadians()
    {
        double longitude = this.lon;

        if (longitude > 180.0)
        {
            longitude -= 360.0;
        }

        return Math.toRadians(longitude);
    }

    /**
     * Setter for property lon.
     * @param lon New value of property lon.
     */
    public void setLon(double lon)
    {
        this.lon = lon;
    }

    /**
     * Holds value of property hgrid.
     */
    private double hgrid;

    /**
     * Getter for property hgrid.
     * @return Value of property hgrid.
     */
    public double getHgrid()
    {
        return this.hgrid;
    }

    /**
     * Setter for property hgrid.
     * @param hgrid New value of property hgrid.
     */
    public void setHgrid(double hgrid)
    {
        this.hgrid = hgrid;
    }

    /**
     * Holds value of property vgrid.
     */
    private double vgrid;

    /**
     * Getter for property vgrid.
     * @return Value of property vgrid.
     */
    public double getVgrid()
    {
        return this.vgrid;
    }

    /**
     * Setter for property vgrid.
     * @param vgrid New value of property vgrid.
     */
    public void setVgrid(double vgrid)
    {
        this.vgrid = vgrid;
    }

    /**
     * Holds value of property reverseBackground.
     */
    private boolean reverseBackground;

    /**
     * Getter for property reverseBackground.
     * @return Value of property reverseBackground.
     */
    public boolean isReverseBackground()
    {
        return this.reverseBackground;
    }

    /**
     * Setter for property reverseBackground.
     * @param reverseBackground New value of property reverseBackground.
     */
    public void setReverseBackground(boolean reverseBackground)
    {
        this.reverseBackground = reverseBackground;
    }

    /**
     * Holds value of property colors.
     */
    private int[][] colors;

    /**
     * Getter for property colors.
     * @return Value of property colors.
     */
    public int[][] getColors()
    {
        return this.colors;
    }

    /**
     * Setter for property colors.
     * @param colors New value of property colors.
     */
    public void setColors(int[][] colors)
    {
        this.colors = colors;
    }

    /** Whether shading is to be applied */
    private boolean doShade = false;

    /**
     * Getter for property doShade.
     * @return Value of property doShade.
     */
    public boolean isDoShade()
    {
        return this.doShade;
    }

    /**
     * Setter for property doShade.
     * @param doShade New value of property doShade.
     */
    public void setDoShade(boolean doShade)
    {
        this.doShade = doShade;
    }

    /** angle of "light" on bumpmap */
    private double shadeAngle = 150.0;

    /**
     * Getter for property shadeAngle.
     * @return Value of property shadeAngle.
     */
    public double getShadeAngle()
    {
        return this.shadeAngle;
    }

    /**
     * Setter for property shadeAngle.
     * @param shadeAngle New value of property shadeAngle.
     */
    public void setShadeAngle(double shadeAngle)
    {
        this.shadeAngle = shadeAngle;
    }

    /**
     * Holds value of property projectionName.
     */
    private String projectionName;

    /**
     * Getter for property projectionName.
     * @return Value of property projectionName.
     */
    public String getProjectionName()
    {
        return this.projectionName;
    }

    /**
     * Setter for property projectionName.
     * @param projectionName New value of property projectionName.
     */
    public void setProjectionName(String projectionName)
    {
        this.projectionName = projectionName;
    }

    /**
     * Holds value of property heightfield.
     */
    private boolean heightfield;

    /**
     * Getter for property heightfield.
     * @return Value of property heightfield.
     */
    public boolean isHeightfield()
    {
        return this.heightfield;
    }

    /**
     * Setter for property heightfield.
     * @param heightfield New value of property heightfield.
     */
    public void setHeightfield(boolean heightfield)
    {
        this.heightfield = heightfield;
    }

    /**
     * Holds value of property lighterColours.
     */
    private int lighterColours;

    /**
     * Getter for property lighterColours.
     * @return Value of property lighterColours.
     */
    public int getLighterColours()
    {
        return this.lighterColours;
    }

    /**
     * Setter for property lighterColours.
     * @param lighterColours New value of property lighterColours.
     */
    public void setLighterColours(int lighterColours)
    {
        this.lighterColours = lighterColours;
    }

    /**
     * Holds value of property outputFile.
     */
    private String outputFile;

    /**
     * Getter for property outputFile.
     * @return Value of property outputFile.
     */
    public String getOutputFile()
    {
        return this.outputFile;
    }

    /**
     * Setter for property outputFile.
     * @param outputFile New value of property outputFile.
     */
    public void setOutputFile(String outputFile)
    {
        this.outputFile = outputFile;
    }

    /**
     * Holds value of property colourFile.
     */
    private String colourFile;

    /**
     * Getter for property colourFile.
     * @return Value of property colourFile.
     */
    public String getColourFile()
    {
        return this.colourFile;
    }

    /**
     * Setter for property colourFile.
     * @param colourFile New value of property colourFile.
     */
    public void setColourFile(String colourFile)
    {
        this.colourFile = colourFile;
    }

    /**
     * Holds value of property outline.
     */
    private boolean outline;

    /**
     * Getter for property outline.
     * @return Value of property outline.
     */
    public boolean isOutline()
    {
        return this.outline;
    }

    /**
     * Setter for property outline.
     * @param outline New value of property outline.
     */
    public void setOutline(boolean outline)
    {
        this.outline = outline;
    }

    /**
     * Holds value of property edges.
     */
    private boolean edges;

    /**
     * Getter for property edges.
     * @return Value of property edges.
     */
    public boolean isEdges()
    {
        return this.edges;
    }

    /**
     * Setter for property edges.
     * @param edges New value of property edges.
     */
    public void setEdges(boolean edges)
    {
        this.edges = edges;
    }
}
