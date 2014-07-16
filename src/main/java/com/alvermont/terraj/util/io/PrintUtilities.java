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
 * PrintUtilities.java
 *
 * Created on 22 April 2006, 12:48
 */

package com.alvermont.terraj.util.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

/**
 * Code for printing Swing Components. Originally obtained from:
 * http://www.developerdotstar.com/community/node/124. Minor mods by me
 * for log4J and exception handling - MS.
 *
 * @author  Rob MacGrogan
 * @version $Id: PrintUtilities.java,v 1.3 2006/07/06 06:59:43 martin Exp $
 */
public class PrintUtilities implements Printable
{
    /*
     * Copied from this tutorial:
     *
     * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing.html
     *
     * And also from a post on the forums at java.swing.com. My apologies that do not have
     * a link to that post, by my hat goes off to the poster because he/she figured out the
     * sticky problem of paging properly when printing a Swing component.
     */

    /** Our logger object */
    private static Log log = LogFactory.getLog(PrintUtilities.class);
    
    private Component componentToBePrinted;
    
    /**
     * Print one component. This is a static method for ease of use. 
     *
     * @param c The component to be printed
     * @throws java.awt.print.PrinterException If there was an error printing
     */
    public static void printComponent(Component c) throws PrinterException
    {
        new PrintUtilities(c).print();
    }
    
    /**
     * Create a new instance of PrintUtilities
     * 
     * @param componentToBePrinted The component to be printed out
     */
    public PrintUtilities(Component componentToBePrinted)
    {
        this.componentToBePrinted = componentToBePrinted;
    }
    
    /**
     * Carry out the print operation for the component 
     *
     * @throws java.awt.print.PrinterException If there is an error in printing
     */
    public void print() throws PrinterException
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        
        if (printJob.printDialog())
        {
            try
            {
                log.debug("Calling PrintJob.print()");
                printJob.print();
                log.debug("End PrintJob.print()");
            }
            catch (PrinterException pe)
            {
                log.error("Error printing: " + pe);
                
                throw pe;
            }
        }
    }
    
    /**
     * Print the component into a graphics context
     * 
     * @param g The <code>Graphics</code> object to be used for printing.
     * This should be a <code>Graphics2D</code> instance
     * @param pf The page format to be used
     * @param pageIndex The number of the page being printed
     * @return An indication of whether the page existed. Silly people using
     * magic numbers. Oh well.
     */
    public int print(Graphics g, PageFormat pf, int pageIndex)
    {
        int response = NO_SUCH_PAGE;
        Graphics2D g2 = (Graphics2D) g;
        // for faster printing, turn off double buffering
        disableDoubleBuffering(componentToBePrinted);
    
        Dimension d = componentToBePrinted.getSize(); //get size of document
        double panelWidth = d.width; //width in pixels
        double panelHeight = d.height; //height in pixels
        double pageHeight = pf.getImageableHeight(); //height of printer page
        double pageWidth = pf.getImageableWidth(); //width of printer page
        double scale = pageWidth / panelWidth;
        
        int totalNumPages = (int) Math.ceil(scale * panelHeight / pageHeight);
        // make sure not print empty pages
        if (pageIndex >= totalNumPages)
        {
            response = NO_SUCH_PAGE;
        }
        else
        {
            // shift Graphic to line up with beginning of print-imageable region
            g2.translate(pf.getImageableX(), pf.getImageableY());
            // shift Graphic to line up with beginning of next page to print
            g2.translate(0f, -pageIndex * pageHeight);
            // scale the page so the width fits...
            g2.scale(scale, scale);
            componentToBePrinted.paint(g2); //repaint the page for printing
            enableDoubleBuffering(componentToBePrinted);
            response = Printable.PAGE_EXISTS;
        }
        
        return response;
    }

    /**
     * Disable double buffering for a component
     * 
     * @param c The component to disable double buffering for
     */
    public static void disableDoubleBuffering(Component c)
    {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }
    
    /**
     * Enable double buffering for a component
     * 
     * @param c The component to enable double buffering for
     */
    public static void enableDoubleBuffering(Component c)
    {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
    
}
