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
 * CameraTableModel.java
 *
 * Created on 21 January 2006, 12:48
 */
package com.alvermont.terraj.fracplanet.ui;

import com.alvermont.terraj.util.ui.HiddenTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Table model for the camera table display, wraps up a list of camera
 * positions
 *
 * @author  martin
 * @version $Id: CameraTableModel.java,v 1.7 2006/07/06 06:58:34 martin Exp $
 */
public class CameraTableModel extends HiddenTableModel
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(CameraTableModel.class);

    /** Indicates whether extended columns are visible */
    private boolean isVisible;

    /** The column number of the camera centre column */
    private static final int COL_NUMBER_CENTRE = 3;

    /** The column number of the camera up column */
    private static final int COL_NUMBER_UP = 4;

    /** The column number of the camera xrot column */
    private static final int COL_NUMBER_XROT = 5;

    /** The column number of the camera yrot column */
    private static final int COL_NUMBER_YROT = 6;

    // RequireThis OFF: COL_NUMBER_CENTRE
    // RequireThis OFF: COL_NUMBER_UP
    // RequireThis OFF: COL_NUMBER_XROT
    // RequireThis OFF: COL_NUMBER_YROT

    /** Creates a new instance of CameraTableModel */
    public CameraTableModel()
    {
        addColumn("No", Integer.class);
        addColumn("Name", String.class);
        addColumn("Position", String.class);
        addColumn("Centre", String.class);
        addColumn("Up", String.class);
        addColumn("XRot", Float.class);
        addColumn("YRot", Float.class);

        changeVisibility(false);
    }

    /**
     * Indicates whether our extended columns are shown or hidden
     *
     * @return <pre>true</pre> If the extended columns are visible else
     * <pre>false</pre>.
     */
    public boolean getVisibility()
    {
        return this.isVisible;
    }

    /**
     * Adjust the visibility of the extended columns
     *
     * @param state The new visibility state for the columns. They are displayed
     * if this state is set to <pre>true</pre>.
     */
    public void changeVisibility(boolean state)
    {
        columnsVisible.set(COL_NUMBER_CENTRE, state);
        columnsVisible.set(COL_NUMBER_UP, state);
        columnsVisible.set(COL_NUMBER_XROT, state);
        columnsVisible.set(COL_NUMBER_YROT, state);

        this.isVisible = state;

        fireTableStructureChanged();
    }

    /**
     * Get the object at a particular row and column of the model
     *
     * @param row The row number of the object to be retrieved
     * @param col The column number of the object to be retrieved
     * @return The object at the given row and column in the model
     */
    public Object getValueAt(int row, int col)
    {
        Object retValue;

        retValue = super.getValueAt(row, col);

        if (col == 0)
        {
            retValue = new Integer(row + 1);
        }

        return retValue;
    }

    /**
     * Indicates whether a cell can be edited
     *
     * @param row The row number of the object to be tested
     * @param column The column number of the object to be tested
     * @return <pre>true</pre> if this cell can be edited otherwise <pre>false</pre>
     */
    public boolean isCellEditable(int row, int column)
    {
        switch (column)
        {
            case 1:
                return true;

            default:
                return false;
        }
    }
}
