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
 * BMPOutput.java
 *
 * Created on December 23, 2005, 3:37 PM
 *
 */
package com.alvermont.terraj.planet.io;

import com.alvermont.terraj.planet.project.Projector;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Writes a projection to a .BMP bitmap file (New code should use the
 * <code>ImageBuilder</code> class in this package instead).
 *
 * NOTE: This class was converted from the original C code. In Java we
 * should really be using the ImageIO facilities. Therefore I have
 * written the ImageBuilder class which does the job of producing a
 * BufferedImage which can be processed using Graphics2D or written out
 * to a file with ImageIO. This class is retained for historical reasons.
 *
 * @author martin
 * @version $Id: BMPOutput.java,v 1.5 2006/07/06 06:59:43 martin Exp $
 */
public class BMPOutput
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(BMPOutput.class);

    /** Creates a new instance of BMPOutput */
    public BMPOutput()
    {
    }

    // MagicNumber OFF

    /**
     * Write the results of the projection in BMP format to a specified
     * output stream
     *
     * @param proj The projection that is to be output
     * @param out The destination for the BMP image data
     * @throws java.io.IOException If there is an error writing the data
     */
    public void outputBMP(Projector proj, OutputStream out)
        throws IOException
    {
        int i;
        int j;
        int s;
        int w1;

        final int width =
            proj.getParameters()
                .getProjectionParameters()
                .getWidth();
        final int height =
            proj.getParameters()
                .getProjectionParameters()
                .getHeight();

        final int[] col = new int[3];

        out.write(0x42);
        out.write(0x4d);

        w1 = ((3 * width) + 3);
        w1 -= (w1 % 4);

        s = 54 + (w1 * height); /* file size */

        out.write(s & 255);
        out.write((s >> 8) & 255);
        out.write((s >> 16) & 255);
        out.write(s >> 24);

        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);

        /* offset to data */
        out.write(54);
        out.write(0);
        out.write(0);
        out.write(0);

        /* size of infoheader */
        out.write(40);
        out.write(0);
        out.write(0);
        out.write(0);

        out.write(width & 255);
        out.write((width >> 8) & 255);
        out.write((width >> 16) & 255);
        out.write(width >> 24);

        out.write(height & 255);
        out.write((height >> 8) & 255);
        out.write((height >> 16) & 255);
        out.write(height >> 24);

        /* no. of planes = 1 */
        out.write(1);
        out.write(0);

        /* bpp */
        out.write(24);
        out.write(0);

        /* no compression */
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);

        /* image size (unspecified) */
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);

        /* h. pixels/m */
        out.write(0);
        out.write(32);
        out.write(0);
        out.write(0);

        /* v. pixels/m */
        out.write(0);
        out.write(32);
        out.write(0);
        out.write(0);

        /* colours used (unspecified) */
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);

        /* important colours (all) */
        out.write(0);
        out.write(0);
        out.write(0);
        out.write(0);

        for (j = height - 1; j >= 0; --j)
        {
            for (i = 0; i < width; ++i)
            {
                proj.fillRGB(i, j, col);

                out.write(col[2]);
                out.write(col[1]);
                out.write(col[0]);
            }

            for (i = 3 * width; i < w1; ++i)
                out.write(0);
        }
    }
}
