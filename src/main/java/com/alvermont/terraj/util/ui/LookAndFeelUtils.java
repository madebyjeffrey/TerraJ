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
 * LookAndFeelUtils.java
 *
 * Created on 27 January 2006, 14:51
 */
package com.alvermont.terraj.util.ui;

import java.awt.Window;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that handles the look and feel settings, preference and notification
 *
 * @author  martin
 * @version $Id: LookAndFeelUtils.java,v 1.8 2006/07/06 06:58:34 martin Exp $
 */
public class LookAndFeelUtils
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(LookAndFeelUtils.class);

    // RequireThis OFF: log

    /** The singleton instance */
    private static LookAndFeelUtils instance;
    
    /** Creates a new instance of LookAndFeelUtils */
    private LookAndFeelUtils()
    {
    }
    
    /**
     * Get the singleton instance of this object
     * 
     * @return The singleton look and file utilities object
     */
    public synchronized static LookAndFeelUtils getInstance()
    {
        if (instance == null)
            instance = new LookAndFeelUtils();
        
        return instance;
    }
    
    /**
     * Change the look and feel to either the native one or the cross
     * platform Java one
     *
     * @param wantSystem If <pre>true</pre> then we are selecting the native
     * look and feel
     * @param topLevel The top level component for the main frame
     * @return <pre>true</pre> if the look and feel was successfully changed
     */
    public boolean setSystemLookAndFeel(boolean wantSystem, Window topLevel)
    {
        boolean ok = true;

        try
        {
            if (wantSystem)
            {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            }
            else
            {
                UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
            }

            SwingUtilities.updateComponentTreeUI(topLevel);
            topLevel.pack();

            final LookAndFeelChangedEvent event =
                new LookAndFeelChangedEvent(this);

            fireLookAndFeelChangedEventListenerHandleLookAndFeelChangedEvent(
                event);
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            log.error("Failed to set LAF", ex);

            ok = false;
        }
        catch (ClassNotFoundException ex)
        {
            log.error("Failed to set LAF", ex);

            ok = false;
        }
        catch (IllegalAccessException ex)
        {
            log.error("Failed to set LAF", ex);

            ok = false;
        }
        catch (InstantiationException ex)
        {
            log.error("Failed to set LAF", ex);

            ok = false;
        }

        return ok;
    }

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList = null;

    /**
     * Registers LookAndFeelChangedEventListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addLookAndFeelChangedEventListener(
        com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener listener)
    {
        if (listenerList == null)
        {
            listenerList = new javax.swing.event.EventListenerList();
        }

        listenerList.add(
            com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener.class,
            listener);
    }

    /**
     * Removes LookAndFeelChangedEventListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeLookAndFeelChangedEventListener(
        com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener listener)
    {
        listenerList.remove(
            com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener.class,
            listener);
    }

    /**
     * Notifies all registered listeners about the event.
     *
     * @param event The event to be fired
     */
    private void fireLookAndFeelChangedEventListenerHandleLookAndFeelChangedEvent(
        LookAndFeelChangedEvent event)
    {
        if (listenerList == null)
        {
            return;
        }

        final Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (
                listeners[i] == com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener.class)
            {
                ((com.alvermont.terraj.util.ui.LookAndFeelChangedEventListener) listeners[i +
                1]).handleLookAndFeelChangedEvent(event);
            }
        }
    }
}
