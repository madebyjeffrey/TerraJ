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
 * The Original Code is Rhino code, released
 * May 6, 1998.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1997-1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the GNU Public License (the "GPL"), in which case the
 * provisions of the GPL are applicable instead of those above.
 * If you wish to allow use of your version of this file only
 * under the terms of the GPL and not to allow others to use your
 * version of this file under the NPL, indicate your decision by
 * deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL.  If you do not delete
 * the provisions above, a recipient may use your version of this
 * file under either the NPL or the GPL.
 *
 * NOTE: THIS IS A MODIFIED VERSION FOR USE WITH THIS PROGRAM UNDER THE
 * GPL
 */

/*
        ShellEnvironment.java

        Wraps java.lang.System properties.

        by Patrick C. Beard <beard@netscape.com>
 */
package com.alvermont.javascript.tools.shell;

import java.util.ArrayList;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * ShellEnvironment, intended to be instantiated at global scope, provides
 * a natural way to access System properties from JavaScript.
 *
 *
 * @author Patrick C. Beard
 */
public class ShellEnvironment extends ScriptableObject
{
    static final long serialVersionUID = -430727378460177065L;
    private ShellEnvironment thePrototypeInstance = null;

    /**
     * Define the environment class in a scope
     *
     * @param scope The scope to define the class in
     */
    public static void defineClass(ScriptableObject scope)
    {
        try
        {
            ScriptableObject.defineClass(scope, ShellEnvironment.class);
        }
        catch (Exception e)
        {
            throw new Error(e.getMessage());
        }
    }

    /**
     * Get the name of this environment class
     *
     * @return The class name of this environment class
     */
    public String getClassName()
    {
        return "Environment";
    }

    /**
     * Create a new instance of ShellEnvironment
     */
    public ShellEnvironment()
    {
        if (this.thePrototypeInstance == null)
        {
            this.thePrototypeInstance = this;
        }
    }

    /**
     * Create a new instance of ShellEnvironment
     *
     * @param scope The scope for the environment
     */
    public ShellEnvironment(ScriptableObject scope)
    {
        setParentScope(scope);

        final Object ctor = ScriptRuntime.getTopLevelProp(scope, "Environment");

        if (ctor != null && ctor instanceof Scriptable)
        {
            final Scriptable s = (Scriptable) ctor;
            setPrototype((Scriptable) s.get("prototype", s));
        }
    }

    /**
     * Check whether a name has a corresponding scritable
     *
     * @param name The name to look for
     * @param start The scriptable to look for
     * @return <pre>true</pre> if the name has the scriptable
     */
    public boolean has(String name, Scriptable start)
    {
        if (this == this.thePrototypeInstance)
        {
            return super.has(name, start);
        }

        return (System.getProperty(name) != null);
    }

    /**
     * Get the value for a name and scriptable
     *
     * @param name The name to be retrieved
     * @param start The scriptable to be retrieved
     * @return The corresponding value
     */
    public Object get(String name, Scriptable start)
    {
        if (this == this.thePrototypeInstance)
        {
            return super.get(name, start);
        }

        final String result = System.getProperty(name);

        if (result != null)
        {
            return ScriptRuntime.toObject(getParentScope(), result);
        }
        else
        {
            return Scriptable.NOT_FOUND;
        }
    }

    /**
     * Set the value for a name and scriptable
     *
     * @param name The name to be set
     * @param start The scriptable to be set
     * @param value The value to be set
     */
    public void put(String name, Scriptable start, Object value)
    {
        if (this == this.thePrototypeInstance)
        {
            super.put(name, start, value);
        }
        else
        {
            System.getProperties()
                .put(name, ScriptRuntime.toString(value));
        }
    }

    private Object[] collectIds()
    {
        final Properties props = System.getProperties();
        final Enumeration names = props.propertyNames();
        final List<Object> keys = new ArrayList<Object>();

        while (names.hasMoreElements())
        {
            keys.add(names.nextElement());
        }

        final Object[] ids = new Object[keys.size()];
        keys.toArray(ids);

        return ids;
    }

    /**
     * Get the ids from this object
     *
     * @return An array of ids
     */
    public Object[] getIds()
    {
        if (this == this.thePrototypeInstance)
        {
            return super.getIds();
        }

        return collectIds();
    }

    /**
     * Get all the ids from this object
     *
     * @return An array of ids
     */
    public Object[] getAllIds()
    {
        if (this == this.thePrototypeInstance)
        {
            return super.getAllIds();
        }

        return collectIds();
    }
}
