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
 * Progress.java
 *
 * Created on December 29, 2005, 9:27 AM
 *
 */
package com.alvermont.terraj.fracplanet.util;


/**
 * Interface for objects that monitor progress
 *
 * @author martin
 * @version $Id: Progress.java,v 1.2 2006/07/06 06:58:34 martin Exp $
 */
public interface Progress
{
    /**
     * Called when a operation is about to start to set up ready to display
     * progress info
     * @param steps The total number of steps that there will be in this
     * operation
     * @param info A string describing the operation
     */
    public void progressStart(int steps, String info);

    /**
     * Called if progress has halted for some time
     *
     * @param reason A string describing the reason that no progress is
     * occurring
     */
    public void progressStall(String reason);

    /**
     * Called when progress has been made to update the display
     *
     * @param step The current step number that we have reached. This will
     * range from 0 to one less than the number of steps that exist for
     * this operation.
     */
    public void progressStep(int step);

    /**
     * Called when an operation has completed
     *
     * @param info A string describing the status of the operation
     */
    public void progressComplete(String info);
}
