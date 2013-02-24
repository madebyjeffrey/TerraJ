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
 * TemplateTest.java
 *
 * Created on 20 April 2006, 17:51
 */

package com.alvermont.terraj.stargen.ui;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a test of the FreeMarker template system to evaluate it for use
 * with this project. It is a copy of one of the Freemaker examples. It's not
 * used in the rest of the code but is left here as an example.
 *
 * @author  martin
 * @version $Id: TemplateTest.java,v 1.3 2006/07/06 06:58:35 martin Exp $
 */
public class TemplateTest
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(TemplateTest.class);
    
    /** Creates a new instance of TemplateTest */
    public TemplateTest()
    {
    }
    
    /**
     * Run the test
     *
     * @throws java.io.IOException If there is an error writing the output
     * @throws freemarker.template.TemplateException If there is an error in the template
     */
    public void go() throws IOException, TemplateException
    {
        Configuration cfg = new Configuration();
        // Specify the data source where the template files come from.
        cfg.setClassForTemplateLoading(TemplateTest.class,
                "/com/alvermont/terraj/stargen/templates/");
        
        // Specify how templates will see the data model. This is an advanced topic...
        // but just use this:
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        
        // Create the root hash
        Map<String,Object> root = new HashMap<String,Object>();
        // Put string ``user'' into the root
        root.put("user", "Big Joe");
        // Create the hash for ``latestProduct''
        Map<String,Object> latest = new HashMap<String, Object>();
        // and put it into the root
        root.put("latestProduct", latest);
        // put ``url'' and ``name'' into latest
        latest.put("url", "products/greenmouse.html");
        latest.put("name", "green mouse");
        
        Template temp = cfg.getTemplate("test.ftl");
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();
    }
    
    /**
     * The main entrypoint for the program
     * 
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        TemplateTest me = new TemplateTest();
        
        try
        {
            me.go();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
