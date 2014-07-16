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

package com.alvermont.terraj.util.io;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to handle printing out a <code>BufferedImage</code> at a suitable
 * scale for the printer.
 *
 * @author martin
 * @version $Id: ImagePrinter.java,v 1.6 2006/07/06 06:59:43 martin Exp $
 */
public class ImagePrinter implements Printable
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(ImagePrinter.class);

    // RequireThis OFF: log

    /** The image to be printed */
    private BufferedImage image;

    /** The page format to be used */
    private PageFormat format;

    /** The X scaling */
    private double scaleX = 1.0;

    /** The Y scaling */
    private double scaleY = 1.0;

    /**
     * Creates a new ImagePrinter instance
     *
     * @param image The image to be printed
     * @param format The format to be used for printing
     */
    public ImagePrinter(BufferedImage image, PageFormat format)
    {
        this.setImage(image);
        this.setFormat(format);
    }

    /**
     * Set up our scale values
     *
     * @param newScaleX The X scaling factor to be used
     * @param newScaleY The Y scaling factor to be used
     */
    protected void setScale(double newScaleX, double newScaleY)
    {
        this.scaleX = newScaleX;
        this.scaleY = newScaleY;
    }

    /**
     * Scale the image to fit in the X direction. Note the image is
     * unchanged, the scaling of the Graphics2D is what gets changed
     */
    public void scaleToFitX()
    {
        final Rectangle componentBounds =
            new Rectangle(getImage().getWidth(), getImage().getHeight());

        final double newScaleX =
            this.getFormat()
                .getImageableWidth() / componentBounds.width;
        final double newScaleY = newScaleX;

        if (newScaleX < 1)
        {
            setScale(newScaleX, newScaleY);
        }
    }

    /**
     * Scale the image to fit in the Y direction. Note the image is
     * unchanged, the scaling of the Graphics2D is what gets changed
     */
    public void scaleToFitY()
    {
        final Rectangle componentBounds =
            new Rectangle(getImage().getWidth(), getImage().getHeight());

        final double newScaleY =
            getFormat()
                .getImageableHeight() / componentBounds.height;
        final double newScaleX = newScaleY;

        if (newScaleY < 1)
        {
            setScale(newScaleX, newScaleY);
        }
    }

    /**
     * Scale the image to fit the printable area in both the X and Y
     * directions.
     *
     * @param useSymmetricScaling If <pre>true</pre> then scale the
     * image in a symmetric manner
     */
    public void scaleToFit(boolean useSymmetricScaling)
    {
        final Rectangle componentBounds =
            new Rectangle(
                this.getImage().getWidth(), this.getImage().getHeight());

        double newScaleX =
            this.getFormat()
                .getImageableWidth() / componentBounds.width;
        double newScaleY =
            this.getFormat()
                .getImageableHeight() / componentBounds.height;

        log.debug("Scale for printing: " + newScaleX + " " + newScaleY);

        if ((newScaleX < 1) || (newScaleY < 1))
        {
            if (useSymmetricScaling)
            {
                if (newScaleX < newScaleY)
                {
                    newScaleY = newScaleX;
                }
                else
                {
                    newScaleX = newScaleY;
                }
            }

            setScale(newScaleX, newScaleY);
        }
    }

    /**
     * Render an image for printing.
     *
     * @param graphics The graphics context to print on
     * @param pageFormat The page format being used
     * @param pageIndex The page being printed
     * @throws java.awt.print.PrinterException If there is an error in printing
     * @return An indication of the result of page rendering for this page
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
        throws PrinterException
    {
        final Graphics2D g2 = (Graphics2D) graphics;

        // we only expect to be printing 1 page as the image is scaled
        if (pageIndex >= 1)
        {
            return Printable.NO_SUCH_PAGE;
        }

        // translate the coordinate system to match up the image with 
        // the printable area
        g2.translate(getFormat().getImageableX(), pageFormat.getImageableY());

        final Rectangle componentBounds =
            new Rectangle(getImage().getWidth(), getImage().getHeight());
        g2.translate(-componentBounds.x, -componentBounds.y);

        // scale the image to fit
        scaleToFit(true);
        g2.scale(getScaleX(), getScaleY());

        // render the image
        g2.drawImage(getImage(), null, 0, 0);

        // done
        return Printable.PAGE_EXISTS;
    }

    /**
     * Get the image that this item holds
     *
     * @return A <code>BufferedImage</code> that this object is using for printing
     */
    public BufferedImage getImage()
    {
        return this.image;
    }

    /**
     * Set the image that this item holds
     *
     * @param image A <code>BufferedImage</code> that this object will use for printing
     */
    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    /**
     * Get the page format being used for printing
     *
     * @return The current page format object
     */
    public PageFormat getFormat()
    {
        return this.format;
    }

    /**
     * Set the page format being used for printing
     *
     * @param format The new page format object
     */
    public void setFormat(PageFormat format)
    {
        this.format = format;
    }

    /**
     * Get the X scale factor currently in use
     *
     * @return The scale factor in the X direction
     */
    public double getScaleX()
    {
        return this.scaleX;
    }

    /**
     * Get the Y scale factor currently in use
     *
     * @return The scale factor in the Y direction
     */
    public double getScaleY()
    {
        return this.scaleY;
    }
}
