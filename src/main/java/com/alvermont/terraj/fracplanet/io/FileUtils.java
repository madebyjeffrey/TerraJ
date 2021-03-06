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
 * FileUtils.java
 *
 * Created on 10 January 2006, 17:22
 */
package com.alvermont.terraj.fracplanet.io;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Miscellaneous file related utility code
 *
 * @author  martin
 * @version $Id: FileUtils.java,v 1.6 2006/07/06 06:59:43 martin Exp $
 */
public class FileUtils
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(FileUtils.class);

    /**
     * Creates a new instance of FileUtils
     */
    protected FileUtils()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Add an extension to the filename of a <code>File</code> object. If
     * an extension is already present then it is not modified.
     *
     * @param f The file to have its extension modified
     * @param newExt The new extension to be added e.g. ".pov"
     * @return A new file object with a possibly different file extension
     */
    public static File addExtension(File f, String newExt)
    {
        File file = f;

        String name = file.getName();

        final int pos = name.lastIndexOf(".");

        if (pos == -1)
        {
            name = name;

            if (!newExt.startsWith("."))
            {
                name += ".";
            }

            name += newExt;

            file = new File(file.getParentFile(), name);
        }

        return file;
    }

    /**
     * Set the extension of the filename of a <code>File</code> object. If
     * an extension is already present then it is replaced by the one
     * specified.
     *
     * @param f The file to have its extension modified
     * @param newExt The new extension to be given e.g. ".pov"
     * @return A new file object with the name having the specified extension
     */
    public static File changeExtension(File f, String newExt)
    {
        File file = f;

        String name = file.getName();

        final int pos = name.lastIndexOf(".");

        if (pos < 0)
        {
            return addExtension(file, newExt);
        }
        else
        {
            name = name.substring(0, pos);

            if (!newExt.startsWith("."))
            {
                name += ".";
            }

            name += newExt;

            file = new File(file.getParentFile(), name);
        }

        return file;
    }
}
