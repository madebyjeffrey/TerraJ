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
 * HiddenTableModel.java
 *
 * Created on 21 January 2006, 12:28
 */
package com.alvermont.terraj.util.ui;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A table model that can show or hide selected columns of the data.
 *
 * Modified and extended from the original version by Vincent Oberle
 * available at: http://www.codeguru.com/java/articles/660.shtml
 * This version supports more operations.
 *
 * @author  martin
 * @version $Id: HiddenTableModel.java,v 1.6 2006/07/06 06:58:34 martin Exp $
 */
public class HiddenTableModel extends AbstractTableModel implements TableModel
{
    /**
     * Names of the columns in the table
     */
    protected List<String> columnNames = new ArrayList<String>();

    /** Visibility flags for the columns */
    protected List<Boolean> columnsVisible = new ArrayList<Boolean>();

    /** Classes of data held in each column */
    protected List<Class> columnClasses = new ArrayList<Class>();

    /** The data that is managed by this model */
    protected List<List<Object>> data = new ArrayList<List<Object>>();

    /** Our logger object */
    private static Log log = LogFactory.getLog(HiddenTableModel.class);

    /** Creates a new instance of HiddenTableModel */
    public HiddenTableModel()
    {
    }

    /**
     * Add a new set of column values to this table, all null
     *
     * @param name The name of the column
     * @param columnClass The <code>Class</code> that items in this column will be
     */
    protected void addColumnValue(String name, Class columnClass)
    {
        this.columnNames.add(name);
        this.columnsVisible.add(Boolean.TRUE);
        this.columnClasses.add(columnClass);

        for (int r = 0; r < this.data.size(); ++r)
        {
            final List<Object> l = this.data.get(r);

            l.add(null);
        }

        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Add a new set of column values to this table, initialized from a vector
     * of values.
     *
     * @param name The name of the column
     * @param columnClass The <code>Class</code> that items in this column will be
     * @param values The values to be assigned in the new column, row by row
     */
    protected void addColumnValue(
        String name, Class columnClass, Vector values)
    {
        this.columnNames.add(name);
        this.columnsVisible.add(Boolean.TRUE);
        this.columnClasses.add(columnClass);

        for (int r = 0; r < this.data.size(); ++r)
        {
            final List<Object> l = this.data.get(r);

            l.add(values.get(r));
        }

        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Add a new set of column values to this table, initialized from an
     * array of values.
     *
     * @param name The name of the column
     * @param columnClass The <code>Class</code> that items in this column will be
     * @param values The values to be assigned in the new column, row by row
     */
    protected void addColumnValue(
        String name, Class columnClass, Object[] values)
    {
        this.columnNames.add(name);
        this.columnsVisible.add(Boolean.TRUE);
        this.columnClasses.add(columnClass);

        for (int r = 0; r < this.data.size(); ++r)
        {
            final List<Object> l = this.data.get(r);

            l.add(values[r]);
        }

        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * This function converts a column number in the table to an index
     * into the underlying stored data
     *
     * @param col The logical column number from 0
     * @return The physical storage column for this column
     */
    protected int getNumber(int col)
    {
        /** The correct column number to return */
        int n = col;
        int i = 0;

        do
        {
            if (!(this.columnsVisible.get(i)))
            {
                ++n;
            }

            ++i;
        }
        while (i < n);

        // If we are on an invisible column, we have to go one step further
        while (!(this.columnsVisible.get(n)))
        {
            ++n;
        }

        return n;
    }

    /**
     * Get the number of columns in the model
     *
     * @return The number of (visible) columns in this table model
     */
    public int getColumnCount()
    {
        int n = 0;

        for (int i = 0; i < this.columnsVisible.size(); ++i)
        {
            if (this.columnsVisible.get(i))
            {
                ++n;
            }
        }

        return n;
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
        final List<Object> l = this.data.get(row);

        return l.get(col);
    }

    /**
     * Gets the name of a column
     *
     * @param col The logical column number of the column to be accessed
     * @return The name of the specified column
     */
    public String getColumnName(int col)
    {
        return this.columnNames.get(getNumber(col));
    }

    /**
     * Add a column to this model
     *
     * @param columnName The name of the column
     * @param columnClass The <code>Class</code> that items in this column will be
     */
    public void addColumn(Object columnName, Class columnClass)
    {
        addColumnValue((String) columnName, columnClass);
    }

    /**
     * Add a column to this model. The type of the new column will be
     * <code>Object</code>
     *
     * @param columnName The name of the column
     * @param columnData The data to initialize the column with
     */
    public void addColumn(Object columnName, Vector columnData)
    {
        addColumnValue((String) columnName, Object.class, columnData);
    }

    /**
     * Add a column to this model
     *
     * @param columnName The name of the column
     */
    public void addColumn(Object columnName)
    {
        addColumn(columnName, Object.class);
    }

    /**
     * Get the class of a logical column in this table
     *
     * @param columnIndex The index of the logical column to retrieve
     * @return The class of items in this column
     */
    public Class<?> getColumnClass(int columnIndex)
    {
        final Class retValue = this.columnClasses.get(getNumber(columnIndex));

        return retValue;
    }

    /**
     * Add a column to this model. The type of the new column will be
     * <code>Object</code>
     *
     * @param columnName The name of the column
     * @param columnData The data to initialize the column with
     */
    public void addColumn(Object columnName, Object[] columnData)
    {
        addColumnValue((String) columnName, Object.class, columnData);
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
        boolean retValue;

        retValue = super.isCellEditable(row, getNumber(column));

        return retValue;
    }

    /**
     * Get the count of rows in this model
     *
     * @return The number of rows in this model
     */
    public int getRowCount()
    {
        return this.data.size();
    }

    /**
     * Set the object at a particular row and column of the model
     *
     * @param rowIndex The row number of the object to be set
     * @param columnIndex The column number of the object to be set
     * @param aValue The new value to be stored at the given row and column in the model
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        final List<Object> row = this.data.get(rowIndex);

        row.set(columnIndex, aValue);
    }

    /**
     * Add a new row to this table
     *
     * @param rowData The data for the new row to be added
     */
    public void addRow(Object[] rowData)
    {
        final List<Object> row = new ArrayList<Object>();

        for (Object c : rowData)
            row.add(c);

        this.data.add(row);

        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    /**
     * Remove a row from this table
     *
     * @param row The index of the row to be removed
     */
    public void removeRow(int row)
    {
        this.data.remove(row);

        fireTableRowsDeleted(row, row);
    }

    /**
     * Move a row from one index to another in the table. No rows data
     * is affected but other rows than the ones specified can change
     * position as a result of the move.
     *
     * @param from The index of the row in its original position
     * @param to The index of the row in its new position
     */
    public void moveRow(int from, int to)
    {
        final List<Object> row = this.data.remove(from);

        this.data.add(to, row);

        fireTableRowsUpdated(0, getRowCount());
    }
}
