/*
 * Java Terrain and Stellar System Ports
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
 */

/*
 * AstractAsynchronousAction.java
 *
 * Created on 18 January 2006, 12:21
 */
package com.meghnasoft.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

/*
 * AbstractAsynchronousAction.java
 * Copyright (C) 2003 Alamgir Farouk
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Created on June 29, 2003
 * @author Alamgir Farouk
 *
 * Note: This class has been modified in a minor way by me for Javadoc
 * completeness, reformatting by Jalopy and changes for Stylecheck reports.
 * No functionality has been changed - MS
 *
 */

/**
 * AbstractAsynchronousAction class extends the standard AbstractAction
 * class from Java/Swing.
 * It generalizes the concept of 'actionPerformed()' to include actions
 * that may not return right away and hence are launched on a separate
 * thread.
 *
 * To use this class, a concrete class must be defined which extends this
 * abstract class, and use it in place of a Swing Action. See documentation
 * for further details.
 *
 * @author Alamgir Farouk
 * @see javax.swing.AbstractAction
 */
public abstract class AbstractAsynchronousAction extends AbstractAction
{
    /** Our logger object */
    private static Log log =
        LogFactory.getLog(AbstractAsynchronousAction.class);

    /** Number of milliseconds in a second */
    private static final int SECOND = 1000;
    private ThreadVar threadVar;
    private Object taskOutput = new String("STILL COMPUTING");
    private Component theParentComponent;
    private String monitorMessage;
    private int popUpMonitorDelay;
    private String statusUpdate = null;
    private ActionEvent lastActionEvent;
    private Runnable asynchronousActionRunnable;

    // RequireThis OFF: log

    /**
     * This is constructor for AbstractAsynchronousAction class.
     *
     * @param name The name of the action, passed to the super class AbstractAction
     */
    public AbstractAsynchronousAction(String name)
    {
        this();
        putValue(Action.NAME, name);

        final Runnable doFinished =
            new Runnable()
            {
                public void run()
                {
                    finished();
                    firePropertyChange("enabled", Boolean.FALSE, Boolean.TRUE);
                }
            };

        this.asynchronousActionRunnable =
            new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            setTaskOutput(
                                asynchronousActionPerformed(lastActionEvent));
                        }
                        catch (Exception excp)
                        {
                            log.error(
                                "Aysynchronous action got exception", excp);
                        }
                        finally
                        {
                            threadVar.clear();

                            try
                            {
                                SwingUtilities.invokeAndWait(doFinished);
                            }
                            catch (InterruptedException ex)
                            {
                                if (log.isDebugEnabled())
                                {
                                    log.debug(
                                        "Asynchronous action got " +
                                        "interrupted exception", ex);
                                }
                            }
                            catch (InvocationTargetException ex)
                            {
                                if (log.isDebugEnabled())
                                {
                                    log.debug(
                                        "Asynchronous action got invocation " +
                                        "target exception", ex);
                                }
                            }
                        }
                    }
                };
    }

    /**
     * Constructor, private.
     *
     *
     */
    private AbstractAsynchronousAction()
    {
        //The key used for storing a longer description for the action, could be 
        //used for context-sensitive help.
        putValue(Action.LONG_DESCRIPTION, "Starts an asynchronous task");
        //The key used for storing the name for the action, used for a menu or button.
        //static String
        putValue(Action.NAME, "Start(long)");
        //The key used for storing a short description for the action, 
        //used for tooltip text.
        putValue(Action.SHORT_DESCRIPTION, "Start");
        //Useful constants that can be used as the storage-retreival key when 
        //setting or getting one of this object's properties (text or icon)
        putValue(Action.DEFAULT, "AsynchronousAction");
    } //default constructor

    /**
     * This is an implementation of the abstract method from the
     * Action interface. This method takes care of the threading issues
     * and is marked final to prevent overriding in sub-classes.
     * @param e An ActionEvent parameter passed in by the control
     * which is triggering the action. Action event contains inforamtion
     * about the event, for example if the 'Alt' key was held down while
     * the mouse was clicked.
     */
    public final void actionPerformed(ActionEvent e)
    {
        firePropertyChange("enabled", Boolean.TRUE, Boolean.FALSE);
        lastActionEvent = e;

        final Thread t = new Thread(asynchronousActionRunnable);
        threadVar = new ThreadVar(t);
        t.start();
    }

    /**
     * Get the taskOutput produced by the worker thread, or null if it
     * hasn't been constructed yet.
     *
     * @return Returns the task output or null if it hasn't been constructed
     * yet.
     */
    protected synchronized Object getTaskOutput()
    {
        return taskOutput;
    }

    /**
     * Set the taskOutput produced by worker thread
     *
     * @param x The object that is the task output
     */
    private synchronized void setTaskOutput(Object x)
    {
        taskOutput = x;
    }

    /**
     * This the method which does the long task! This
     * method is implemented in the concrete class.  The ActionEvent is
     * passed on from the actual event and is available to the implementation.
     *
     * @param e ActionEvent which contain information about actual event
     * which triggered the action. For example, for a button click, it could indicate if
     * the 'ALT' key was down during the click.
     *
     * @return The object returned by the asychronous action
     */
    public abstract Object asynchronousActionPerformed(ActionEvent e);

    /**
     * Method called when the task is completed. A callback
     * which is guaranteed to be called only after the action
     * has completed and the thread terminated.
     *
     */
    public abstract void finished();

    /**
     * Allows interruption of the task if it is waiting.
     *
     */
    public void interrupt()
    {
        final Thread t = threadVar.getThread();
        taskOutput = null;

        if (t != null)
        {
            t.interrupt();
        }
    }

    /**
     * Return the taskOutput created by the <code>asynchronousActionPerfomed</code>
     * method.Returns null if either the executing background thread or
     * the current thread was interrupted before a taskOutput was produced.
     *
     * @return the taskOutput created by the <code>asynchronousActionPerfomed</code>
     * method
     */
    public Object getTaskOutputBlocking()
    {
        final Thread t = threadVar.getThread();

        if (t == null)
        {
            return getTaskOutput();
        }

        try
        {
            t.join();

            return getTaskOutput();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread()
                .interrupt();

            return null;
        }
    }

    /**
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private class ThreadVar
    {
        private Thread thread;

        ThreadVar(Thread t)
        {
            this.thread = t;
        }

        synchronized Thread getThread()
        {
            return this.thread;
        }

        synchronized void clear()
        {
            this.thread = null;
        }
    } //ThreadVar
}
