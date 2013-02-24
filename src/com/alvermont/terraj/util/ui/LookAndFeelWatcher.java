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
 * LookAndFeelWatcher.java
 *
 * Created on 27 January 2006, 15:11
 */
package com.alvermont.terraj.util.ui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that reacts to look and feel change events and gets a window to
 * update its look and feel.
 *
 * @author  martin
 * @version $Id: LookAndFeelWatcher.java,v 1.5 2006/07/06 06:58:34 martin Exp $
 */
public class LookAndFeelWatcher extends WindowAdapter 
        implements LookAndFeelChangedEventListener, WindowListener
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(LookAndFeelWatcher.class);

    /** The object that we are handling */
    private Window topLevel;

    /**
     * Creates a new instance of LookAndFeelWatcher
     *
     * @param topLevel The component that we are handling
     */
    public LookAndFeelWatcher(Window topLevel)
    {
        this.topLevel = topLevel;
    }

    /**
     * Process a look and feel changed event
     *
     * @param event The event that is being notified
     */
    public void handleLookAndFeelChangedEvent(LookAndFeelChangedEvent event)
    {
        SwingUtilities.updateComponentTreeUI(this.topLevel);
        this.topLevel.pack();
    }

    /**
     * Invoked when a window is in the process of being closed.
     * The close operation can be overridden at this point.
     *
     * @param e The event indicating the window is closing
     */
    public void windowClosing(WindowEvent e)
    {
        LookAndFeelUtils.getInstance().removeLookAndFeelChangedEventListener(this);
    }
}
