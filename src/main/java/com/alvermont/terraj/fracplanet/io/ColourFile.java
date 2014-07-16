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
 * ColourFile.java
 *
 * Created on 09 January 2006, 15:36
 */
package com.alvermont.terraj.fracplanet.io;

import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class that reads and writes a file from colour definitions. A file
 * contains a number of colours, each colour is on a separate line in
 * the format R,G,B where these are 3 floating point values.
 *
 * @author  martin
 * @version $Id: ColourFile.java,v 1.7 2006/07/06 06:59:43 martin Exp $
 */
public class ColourFile
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ColourFile.class);

    // RequireThis OFF: log

    /** Creates a new instance of ColourFile */
    protected ColourFile()
    {
        throw new UnsupportedOperationException();
    }

    private static FloatRGBA parseColour(String line) throws IOException
    {
        final StringTokenizer tok = new StringTokenizer(line, ",");

        if (tok.countTokens() != 3)
        {
            throw new IOException("Line should be in the form x,y,z: " + line);
        }

        final float[] floats = new float[3];

        for (int b = 0; b < 3; ++b)
            floats[b] = Float.parseFloat(tok.nextToken().trim());

        return new FloatRGBA(floats[0], floats[1], floats[2]);
    }

    /**
     * Read a colour file and return a list of FloatRGBA values that it contains
     *
     *
     * @param source The <code>File</code> object to use to read from
     * @return A list of <code>FloatRGBA</code> objects as read from the file
     * @throws java.io.IOException If there is an error opening or reading from
     * the file etc.
     */
    public static List<FloatRGBA> readFile(File source)
        throws IOException
    {
        final BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(new FileInputStream(source)));

        final List<FloatRGBA> colours = new ArrayList<FloatRGBA>();

        String line = null;

        try
        {
            do
            {
                line = reader.readLine();

                if (line != null)
                {
                    line = line.trim();
                }

                if (
                    (line != null) && (line.length() > 0) &&
                        !line.startsWith("#"))
                {
                    colours.add(parseColour(line));
                }
            }
            while (line != null);

            return colours;
        }
        catch (NumberFormatException ex)
        {
            log.error(
                "NumberFormatException reading file: " + ex.getMessage(), ex);

            throw new IOException("Bad data found in file");
        }
        catch (IOException ex)
        {
            log.error("IOException reading file: " + ex.getMessage(), ex);

            throw ex;
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
    }

    /**
     * Write a list of colours to a file that can be read by the
     * <code>readFile</code> method of this class.
     *
     * @param colours The list of colours to be written
     * @param target The file object indicating where the data is to be written
     * @throws java.io.IOException If there is an error opening or writing to
     * the file etc.
     */
    public static void writeFile(List<FloatRGBA> colours, File target)
        throws IOException
    {
        final PrintWriter pw = new PrintWriter(new FileOutputStream(target));

        try
        {
            for (int c = 0; c < colours.size(); ++c)
            {
                final FloatRGBA col = colours.get(c);

                pw.println(col.getR() + "," + col.getG() + "," + col.getB());
            }
        }
        finally
        {
            pw.close();
        }
    }
}
