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
 * Projector.java
 *
 * Created on December 23, 2005, 6:29 PM
 *
 */
package com.alvermont.terraj.planet.project;

import com.alvermont.terraj.fracplanet.util.Progress;

/**
 * Interface that the different projection classes must implement
 *
 * Information on map projections can be found at:
 *
 * http://en.wikipedia.org/wiki/Map_projection
 *
 * @author martin
 * @version $Id: Projector.java,v 1.4 2006/07/06 06:58:34 martin Exp $
 */
public interface Projector
{
    /**
     * Carry out the projection operation
     */
    public void project();

    /**
     * Free any resources used by this object
     */
    public void releaseResources();

    /**
     * Get the colour component of a pixel at the specified coordinates
     * the supplied array will be set to the correct colour.
     *
     * @param x The x coordinate to be retrieved
     * @param y The y coordinate to be retrieved
     * @param col The colour array to be used to store the colour
     */
    public void fillRGB(int x, int y, int[] col);

    /**
     * Get the parameters object associated with the projection
     *
     * @return The parameters object for this projection
     */
    public com.alvermont.terraj.planet.AllPlanetParameters getParameters();

    /**
     * Get the name of the thumbnail image to use for this projection
     *
     * @return The name of the thumbnail for this projection
     */
    public String getThumbnailName();

    /**
     * Getter for property progress.
     * @return Value of property progress.
     */
    public Progress getProgress();

    /**
     * Setter for property progress.
     * @param progress New value of property progress.
     */
    public void setProgress(Progress progress);

    /**
     * Setter for property parameters.
     * @param parameters New value of property parameters.
     */
    public void setParameters(
        com.alvermont.terraj.planet.AllPlanetParameters parameters);
}
