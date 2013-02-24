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

package com.alvermont.terraj.util.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * FileFilter for PNG, JPG, JPEG and BMP files. These are the formats
 * supported by the basic ImageIO capabilities in Java.
 *
 * @author martin
 * @version $Id: CelestiaFileFilter.java,v 1.2 2006/07/06 06:58:34 martin Exp $
 */
public class CelestiaFileFilter extends FileFilter
{
    /** Create a new instance of PNGFileFilter */
    public CelestiaFileFilter()
    {
    }

    /**
     * Test whether a file is to be accepted or not by this filter
     *
     * @param file The file that is to be tested by this filter
     * @return <pre>true</pre> if this file is to be accepted by this
     * filter otherwise <pre>false</pre>
     */
    public boolean accept(File file)
    {
        final String name = file.getName()
                .toLowerCase();

        boolean accepted = false;

        if (
            file.isFile() &&
                (name.endsWith(".ssc") || name.endsWith(".stc")))
        {
            accepted = true;
        }

        return accepted;
    }

    /**
     * Return a description of this filter
     *
     * @return A description of this filter as a string
     */
    public String getDescription()
    {
        return "Celestia planet and stellar description files";
    }
}
