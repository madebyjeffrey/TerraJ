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
 * TerrainTest.java
 *
 * Created on December 29, 2005, 7:05 PM
 *
 */
package com.alvermont.terraj.fracplanet;

import com.alvermont.terraj.fracplanet.ui.TerrainViewerFrame;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test of the GL terrain viewer
 *
 * @author martin
 * @version $Id: GLTerrainViewer.java,v 1.2 2006/07/06 06:58:34 martin Exp $
 */
public class GLTerrainViewer
{
    /**
     * Our logging object
     */
    private static Log log = LogFactory.getLog(GLTerrainViewer.class);

    /**
     * The thread group for regen threads
     */
    private MyThreadGroup myGroup = new MyThreadGroup("RegenThreadGroup");

    // RequireThis OFF: log
    /**
     * Creates a new instance of TerrainTest
     */
    public GLTerrainViewer()
    {
    }

    /**
     * Threadgroup class for use by the background regenerate threads.
     */
    protected class MyThreadGroup extends ThreadGroup
    {
        /**
         * Creates a new MyThreadGroup
         *
         * @param name The name of the new threadgroup
         */
        public MyThreadGroup(final String name)
        {
            super(name);
        }

        /**
         * Creates a new MyThreadGroup
         *
         * @param parent The parent <code>ThreadGroup</code> of this new one
         * @param name The name of the new threadgroup
         */
        public MyThreadGroup(final ThreadGroup parent, final String name)
        {
            super(parent, name);
        }

        /**
         * Make sure exceptions are logged. Try to display an error message.
         *
         * @param thread The thread that has thrown the exception
         * @param throwable The throwable object that was not caught
         */
        public void uncaughtException(
            final Thread thread, final Throwable throwable)
        {
            log.error(
                "Uncaught exception: " + throwable.getMessage(), throwable);

            JOptionPane.showMessageDialog(
                null,
                "Error: " + throwable.getMessage() +
                "\nCheck log file for full details", "Unexpected Error",
                JOptionPane.ERROR_MESSAGE);

            super.uncaughtException(thread, throwable);

            // avoid leaving program in a weird state by taking the big
            // exit
            System.exit(1);
        }
    }

    /**
     * Begin displaying the viewer
     */
    public void run()
    {
        final TerrainViewerFrame viewer = new TerrainViewerFrame();

        viewer.initialize();
    }

    /**
     * The entrypoint to the program
     *
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            
            System.setProperty("sun.java2d.opengl", "false");
            System.setProperty("sun.java2d.noddraw", "true");

            final GLTerrainViewer me = new GLTerrainViewer();

            me.run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
