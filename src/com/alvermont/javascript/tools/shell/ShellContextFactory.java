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
 * Igor Bukanov
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
 * NOTE THIS A MODIFIED VERSION USED IN THIS PROGRAM UNDER THE GPL
 */
package com.alvermont.javascript.tools.shell;

import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.*;

/**
 * A context factory for use by the shell
 */
public class ShellContextFactory extends ContextFactory
{
    private boolean strictMode;
    private int languageVersion;
    private int optimizationLevel;
    private ErrorReporter errorReporter;

    /** Creates a new instance of ShellContextFactory */
    public ShellContextFactory()
    {
    }

    /**
     * Indicates whether a particular feature is enabled / available
     *
     * @param cx The context to be checked
     * @param featureIndex The index of the feature to be checked
     * @return The state of the feature
     */
    protected boolean hasFeature(Context cx, int featureIndex)
    {
        switch (featureIndex)
        {
            case Context.FEATURE_STRICT_VARS:
            case Context.FEATURE_STRICT_EVAL:
                return this.strictMode;

            default:
                return super.hasFeature(cx, featureIndex);
        }
    }

    /**
     * Called when a context has been created
     *
     * @param cx The newly created context
     */
    protected void onContextCreated(Context cx)
    {
        cx.setLanguageVersion(this.languageVersion);
        cx.setOptimizationLevel(this.optimizationLevel);

        if (this.errorReporter != null)
        {
            cx.setErrorReporter(this.errorReporter);
        }

        super.onContextCreated(cx);
    }

    /**
     * Sets the strict mode flag
     *
     * @param flag The new state of the strict mode flag
     */
    public void setStrictMode(boolean flag)
    {
        checkNotSealed();
        this.strictMode = flag;
    }

    /**
     * Set the language version in use
     *
     * @param version The new version to be used
     */
    public void setLanguageVersion(int version)
    {
        Context.checkLanguageVersion(version);
        checkNotSealed();
        this.languageVersion = version;
    }

    /**
     * Set the optimization level
     *
     * @param optimizationLevel The optimization level to be used
     */
    public void setOptimizationLevel(int optimizationLevel)
    {
        Context.checkOptimizationLevel(optimizationLevel);
        checkNotSealed();
        this.optimizationLevel = optimizationLevel;
    }

    /**
     * Set the error reporter object
     *
     * @param errorReporter The new error reporter object
     */
    public void setErrorReporter(ErrorReporter errorReporter)
    {
        if (errorReporter == null)
        {
            throw new IllegalArgumentException();
        }

        this.errorReporter = errorReporter;
    }
}
