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
 * TreeMapMulti.java
 *
 * Created on 19 January 2006, 18:00
 */
package com.alvermont.terraj.fracplanet.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents a data structure from the original code, a map that
 * can hold multiple copies of the same value. We hold a TreeMap which manages
 * a list of items for each key.
 *
 * @author  martin
 * @version $Id: TreeMapMulti.java,v 1.6 2006/07/06 06:58:34 martin Exp $
 */
public class TreeMapMulti
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(TreeMapMulti.class);
    private SortedMap<Float, List<Integer>> map =
        new TreeMap<Float, List<Integer>>();

    // RequireThis OFF: log

    /** Creates a new instance of TreeMapMulti */
    public TreeMapMulti()
    {
    }

    /**
     * Add a value to this map. If the key is already present then the value
     * will be added to the list of values associated with the key.
     *
     * @param key The floating point value acting as a key
     * @param value The int value that is to be associated with the key
     */
    public void insert(float key, int value)
    {
        List<Integer> list = map.get(key);

        if (list == null)
        {
            list = new ArrayList<Integer>();
            map.put(key, list);
        }

        list.add(value);
    }

    /**
     * Get the first value from this map. In practice this will be the number
     * of the vertex with the lowest height
     *
     * @return The first vertex (sorted by key order) in the map
     */
    public int getFirstVertex()
    {
        final List<Integer> list = map.get(map.firstKey());

        int vertex = 0;

        if ((list != null) && !list.isEmpty())
        {
            vertex = list.get(0);
        }

        return vertex;
    }

    /**
     * Get the first key from the map. In practice this will be the height of
     * the lowest vertex in the map.
     *
     * @return The lowest key value in the map
     */
    public float getFirstHeight()
    {
        float key = 0;

        if (map.size() > 0)
        {
            key = this.map.firstKey();
        }

        return key;
    }

    /**
     * Tests whether the map is empty
     *
     * @return <pre>true</pre> if the map is empty otherwise <pre>false</pre>
     */
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    /**
     * Return a subset of this map from the begining to the specified key
     * value
     *
     * @param key The key value that will subdivide the map
     * @return The requested subset of the data in the map
     */
    public SortedMap<Float, List<Integer>> headMap(Float key)
    {
        return this.map.headMap(key);
    }

    /**
     * Return a subset of this map from the specified key value to the end
     *
     * @param key The key value that will subdivide the map
     * @return The requested subset of the data in the map
     */
    public SortedMap<Float, List<Integer>> tailMap(Float key)
    {
        return this.map.tailMap(key);
    }

    /**
     * Print out a map for testing
     */
    protected void debugPrintMap()
    {
        final Iterator<Float> i = this.map.keySet()
                .iterator();

        while (i.hasNext())
        {
            final float key = i.next();

            String verts = "(";

            final List<Integer> vs = this.map.get(key);

            for (int v : vs)
                verts += (v + ", ");

            verts += ")";

            log.debug("Map " + key + " -> " + verts);
        }
    }
}
