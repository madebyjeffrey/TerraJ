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
 * FormattedTextFieldVerifier.java
 *
 * Created on 24 January 2006, 09:08
 */
package com.alvermont.terraj.util.ui;

import java.text.ParseException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to stop formatted text field losing focus when it contains an
 * invalid value. Based on code in the Javadoc for
 * <code>JFormattedTextField</code>
 *
 * @author  martin
 * @version $Id: FormattedTextFieldVerifier.java,v 1.2 2006/07/06 06:58:34 martin Exp $
 */
public class FormattedTextFieldVerifier extends InputVerifier
{
    /** Our logger object */
    private static Log log =
        LogFactory.getLog(FormattedTextFieldVerifier.class);

    /** Creates a new instance of FormattedTextFieldVerifier */
    public FormattedTextFieldVerifier()
    {
    }

    /**
     * Called to verify the state of the component
     *
     * @param input The input component
     * @return <pre>true</pre> if the input is in a valid state for the component
     */
    public boolean verify(JComponent input)
    {
        if (input instanceof JFormattedTextField)
        {
            final JFormattedTextField ftf = (JFormattedTextField) input;

            final AbstractFormatter formatter = ftf.getFormatter();

            if (formatter != null)
            {
                final String text = ftf.getText();

                try
                {
                    formatter.stringToValue(text);

                    return true;
                }
                catch (ParseException pe)
                {
                    // not a valid value
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Indicates if this component should give up focus. We only allow this
     * if the text in the field is valid
     *
     * @param input The input component
     * @return <pre>true</pre> if we should give up focus else <pre>false</pre>
     */
    public boolean shouldYieldFocus(JComponent input)
    {
        return verify(input);
    }
}
