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
 * ImageBuilder.java
 *
 * Created on 22 January 2006, 09:34
 */
package com.alvermont.terraj.planet.io;

import com.alvermont.terraj.planet.project.Projector;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to build a BufferedImage from the results of a projection. This
 * allows standard Java image processing facilities to be used to
 * write out and manipulate the results.
 *
 * Reference material used in writing this class came from the book
 * Java 2D Graphics published by O Reilly.
 * http://www.oreillynet.com/cs/catalog/view/au/25?x-t=book.view
 *
 * @author  martin
 * @version $Id: ImageBuilder.java,v 1.5 2006/07/06 06:59:43 martin Exp $
 */
public class ImageBuilder
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ImageBuilder.class);

    /** Creates a new instance of ImageBuilder */
    public ImageBuilder()
    {
    }

    /**
     * Get the pixel array that makes up the image
     *
     * @param proj The projection to be converted
     * @return An array of pixel data top to bottom, left to right, with
     * each pixel being represented by 3 bytes in RGB order
     */
    protected byte[] getPixels(Projector proj)
    {
        final int height =
            proj.getParameters()
                .getProjectionParameters()
                .getHeight();
        final int width =
            proj.getParameters()
                .getProjectionParameters()
                .getWidth();

        /* Changed to byte[] to eliminate use of internal api
            June 16, 2014: Jeffrey Drake - must still test
        */
        
//        final ByteBuffer buff = new ByteBuffer(width * height * 3);
        final byte[] buff = new byte[width * height * 3];
        int index = 0;

        final int[] col = new int[3];

        for (int j = 0; j < height; ++j)
        {
            for (int i = 0; i < width; ++i)
            {
                proj.fillRGB(i, j, col);

                buff[index++] = (byte)col[0];
                buff[index++] = (byte)col[1];
                buff[index++] = (byte)col[2];
//                buff.append((byte) (col[0]));
//                buff.append((byte) (col[1]));
//                buff.append((byte) (col[2]));
            }
        }

//        return buff.toArray();
        return buff;
    }

    /**
     * Create and return a <code>BufferedImage</code> object from the
     * result of the projection. This image can then be further processed
     * or written out to a file using <code>ImageIO</code>.
     *
     * @param proj The projection that will provide the image
     * @return A <code>BufferedImage</code> object containing the results of
     * the projection.
     */
    public BufferedImage getImage(Projector proj)
    {
        // get the pixel data and store it into a data buffer
        final byte[] pixels = getPixels(proj);

        final DataBuffer db = new DataBufferByte(pixels, pixels.length);

        // set up offsets for the R,G,B elements
        final int[] offsets = new int[3];

        offsets[0] = 0;
        offsets[1] = 1;
        offsets[2] = 2;

        // create the raster from the pixel data
        final int height =
            proj.getParameters()
                .getProjectionParameters()
                .getHeight();
        final int width =
            proj.getParameters()
                .getProjectionParameters()
                .getWidth();

        final WritableRaster raster =
            Raster.createInterleavedRaster(
                db, width, height, width * 3, 3, offsets, null);

        // create a colour model that matches the raster we have
        final ColorModel cm =
            new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        // combine raster and colour model into a buffered image
        final BufferedImage img = new BufferedImage(cm, raster, false, null);

        return img;
    }
}
