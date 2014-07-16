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
 * ProjectionManager.java
 *
 * Created on 22 January 2006, 12:09
 */
package com.alvermont.terraj.planet.project;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that manages a list of available projections.
 *
 * @author  martin
 * @version $Id: ProjectionManager.java,v 1.6 2006/07/06 06:58:34 martin Exp $
 */
public class ProjectionManager
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(ProjectionManager.class);

    // RequireThis OFF: log

    /** The set of projections we know about */
    private SortedSet<Projector> projections;

    /** Map of names to projections */
    private Map<String, Projector> nameMap = new HashMap<String, Projector>();

    /** Map of thumbnail names to projections */
    private Map<String, Projector> thumbMap = new HashMap<String, Projector>();

    /**
     * A comparator to sort projections into alphabetical order by name
     *
     * @param o1 The first object to be compared
     * @param o2 The second object to be compared
     */
    private class ProjectorComparator implements Comparator<Projector>
    {
        public ProjectorComparator()
        {
        }

        public int compare(Projector o1, Projector o2)
        {
            if (o1 == o2)
            {
                return 0;
            }

            return o1.toString()
                .compareTo(o2.toString());
        }
    }

    /** The list of built in projections */
    private final Projector[] defaultProjections =
        {
            new AzimuthProjection(), new ConicalProjection(),
            new GnomonicProjection(), new MercatorProjection(),
            new MollweideProjection(), new OrthographicProjection(),
            new PetersProjection(), new SinusoidProjection(),
            new SquareProjection(), new StereographicProjection()
        };

    /** Creates a new instance of ProjectionManager */
    public ProjectionManager()
    {
        this.projections = new TreeSet<Projector>(new ProjectorComparator());

        for (Projector p : this.defaultProjections)
        {
            log.debug("Projection: " + p + " " + (p instanceof Projector));

            this.projections.add((Projector) p);

            this.nameMap.put(p.toString(), p);
            this.thumbMap.put(p.getThumbnailName(), p);
        }
    }

    /**
     * Get a set containing all the projections we know about, sorted into
     * alphabetical order
     *
     * @return A sorted set of the projections this object is managing
     */
    public synchronized SortedSet<Projector> getProjections()
    {
        return Collections.unmodifiableSortedSet(this.projections);
    }

    /**
     * Add a projection to the set being managed
     *
     * @param proj An object that implements the <code>Projector</code>
     * interface that will be added to the available set
     */
    public synchronized void addProjection(Projector proj)
    {
        this.projections.add(proj);
    }

    /**
     * Removes a projection from the set being managed
     *
     * @param proj An object that implements the <code>Projector</code>
     * interface that will be removed from the available set
     */
    public synchronized void removeProjection(Projector proj)
    {
        this.projections.remove(proj);
    }

    /**
     * Look up a projection by its name
     *
     * @param name The name of the projection to retrieve
     * @return The corresponding projection or <code>null</code> if it
     * doesn't exist
     */
    public synchronized Projector findByName(String name)
    {
        return this.nameMap.get(name);
    }

    /**
     * Get a default projection that can be used e.g. if searches fail
     *
     * @return A projection object suitable for returning as a default
     */
    public Projector getDefaultProjection()
    {
        return findByName("Orthographic Projection");
    }

    /**
     * Look up a projection by the name of its thumbnail
     *
     * @param name The name of the projection to retrieve
     * @return The corresponding projection or <code>null</code> if it
     * doesn't exist
     */
    public synchronized Projector findByThumbnailName(String name)
    {
        return this.thumbMap.get(name);
    }

    /**
     * Format a thumbnail fName into a string suitable for use in a
     * call to <code>getResource</code>. If the object does not have a
     * package then we default it to ours and if does not have an extension
     * then we default it to PNG.
     *
     * @param name The fName of the thumbnail
     * @return A string that can be used to load the thumbnail as a resource
     */
    public String formatThumbnailName(final String name)
    {
        String fName = name;

        // first default the package
        if (fName.indexOf('/') < 0)
        {
            // turn our package fName into a path
            String ourpkg = getClass()
                    .getPackage()
                    .getName() + ".images";

            ourpkg = "/" + ourpkg.replace('.', '/');

            fName = ourpkg + "/" + fName;
        }

        // now add an extension if necessary
        if (fName.indexOf('.') < 0)
        {
            fName += ".png";
        }

        return fName;
    }
}
