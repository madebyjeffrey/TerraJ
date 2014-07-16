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
 * TriangleEdge.java
 *
 * Created on December 28, 2005, 7:38 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to store triangle edges.
 *
 * An edge is described by two vertices.
 * These are ordered internally for more efficient sorting and comparison.
 * This class is useful for, for example, discovering adjacent triangles
 * through edges they have in common.
 *
 * @author martin
 * @version $Id: TriangleEdge.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleEdge implements Comparable
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleEdge.class);

    /** One vertex of the edge.  This should always be the lesser valued index. */
    private int vertex0;

    /** The other vertex of the edge.  This should always be the greater valued index. */
    private int vertex1;

    /** Arbitary shift value for producing hashcodes */
    private static final int HASHCODE_SHIFT = 13;

    // RequireThis OFF: HASHCODE_SHIFT

    /**
     * Creates a new instance of TriangleEdge
     *
     * @param v0 The first vertex of the edge
     * @param v1 The second vertex of the edge
     */
    public TriangleEdge(int v0, int v1)
    {
        if (v0 < v1)
        {
            this.vertex0 = v0;
            this.vertex1 = v1;
        }
        else
        {
            this.vertex0 = v1;
            this.vertex1 = v0;
        }
    }

    /**
     * Creates a new TriangleEdge as a copy of another one
     *
     * @param e The TriangleEdge object to be copied
     */
    public TriangleEdge(TriangleEdge e)
    {
        this.vertex0 = e.vertex0;
        this.vertex1 = e.vertex1;
    }

    /**
     * Compare this object to another one according to the standard rules
     * for compareTo
     *
     * @param o The object to compare against
     * @return A value of -1, 0 or 1 according to the result of the comparison
     */
    public int compareTo(Object o)
    {
        if (o == null)
        {
            throw new NullPointerException(
                "Comparing against null is not allowed");
        }

        if (!(o instanceof TriangleEdge))
        {
            throw new IllegalArgumentException(
                "Expecting a TriangleEdge object");
        }

        final TriangleEdge e = (TriangleEdge) o;
        int equal = 0;

        if ((this.vertex0 != e.vertex0) || (this.vertex1 != e.vertex1))
        {
            if (
                (this.vertex0 < e.vertex0) ||
                    ((this.vertex0 == e.vertex0) && (this.vertex1 < e.vertex1)))
            {
                equal = -1;
            }
            else
            {
                equal = 1;
            }
        }

        return equal;
    }

    /**
     * Getter for property vertex0.
     * @return Value of property vertex0.
     */
    public int getVertex0()
    {
        return this.vertex0;
    }

    /**
     * Getter for property vertex1.
     * @return Value of property vertex1.
     */
    public int getVertex1()
    {
        return this.vertex1;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     <code>x</code>, <code>x.equals(x)</code> should return
     *     <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     *     should return <code>true</code> if and only if
     *     <code>y.equals(x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     <code>x</code>, <code>y</code>, and <code>z</code>, if
     *     <code>x.equals(y)</code> returns <code>true</code> and
     *     <code>y.equals(z)</code> returns <code>true</code>, then
     *     <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, multiple invocations of
     *     <tt>x.equals(y)</tt> consistently return <code>true</code>
     *     or consistently return <code>false</code>, provided no
     *     information used in <code>equals</code> comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     *     <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p>
     * The <tt>equals</tt> method for class <code>Object</code> implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values <code>x</code> and
     * <code>y</code>, this method returns <code>true</code> if and only
     * if <code>x</code> and <code>y</code> refer to the same object
     * (<code>x == y</code> has the value <code>true</code>).
     * <p>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the
     * general contract for the <tt>hashCode</tt> method, which states
     * that equal objects must have equal hash codes.
     *
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see     #hashCode()
     * @see     java.util.Hashtable
     */
    public boolean equals(Object obj)
    {
        boolean itsEqual = false;

        if (this == obj)
        {
            itsEqual = true;
        }
        else if (obj != null)
        {
            if (this.getClass() == obj.getClass())
            {
                final TriangleEdge e = (TriangleEdge) obj;

                if ((this.vertex0 == e.vertex0) && (this.vertex1 == e.vertex1))
                {
                    itsEqual = true;
                }
            }
        }

        return itsEqual;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p>
     * The general contract of <code>hashCode</code> is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the <tt>hashCode</tt> method
     *     must consistently return the same integer, provided no information
     *     used in <tt>equals</tt> comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the <tt>equals(Object)</tt>
     *     method, then calling the <code>hashCode</code> method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link java.lang.Object#equals(java.lang.Object)}
     *     method, then calling the <tt>hashCode</tt> method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hashtables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class <tt>Object</tt> does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    public int hashCode()
    {
        return (this.vertex1 << HASHCODE_SHIFT) + this.vertex0;
    }
}
