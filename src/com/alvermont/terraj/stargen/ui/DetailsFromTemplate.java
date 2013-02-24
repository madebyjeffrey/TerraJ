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
 * DetailsFromTemplate.java
 *
 * Created on 20 April 2006, 18:14
 */

package com.alvermont.terraj.stargen.ui;

import com.alvermont.terraj.stargen.Enviro;
import com.alvermont.terraj.stargen.evaluator.PlanetEvaluator;
import com.alvermont.terraj.stargen.Planet;
import com.alvermont.terraj.stargen.Primary;
import com.alvermont.terraj.stargen.evaluator.StandardPlanetEvaluator;
import com.alvermont.terraj.stargen.evaluator.StandardSystemEvaluator;
import com.alvermont.terraj.stargen.evaluator.SystemEvaluator;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles turning planet details into text using a template
 *
 * @author  martin
 * @version $Id: DetailsFromTemplate.java,v 1.9 2006/07/06 06:58:35 martin Exp $
 */
public class DetailsFromTemplate
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(DetailsFromTemplate.class);
    
    /**
     * Creates a new instance of DetailsFromTemplate
     */
    public DetailsFromTemplate()
    {
    }
    
    /**
     * Sets up the static and common template objects including static methods
     * of math and constant classes. These are placed into a map for use by
     * the templates.
     * 
     * @param map The map to be populated by this method
     * @throws freemarker.template.TemplateException If there is an error creating the objects
     */
    protected void addStatics(Map<String, Object> map) throws TemplateException
    {
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        TemplateHashModel constantStatics =
                (TemplateHashModel) staticModels.get("com.alvermont.terraj.stargen.Constants");

        map.put("constants", constantStatics);

        TemplateHashModel mathStatics = 
                (TemplateHashModel) staticModels.get("java.lang.Math");
        
        map.put("math", mathStatics);
        
        TemplateHashModel uiStatics = 
                (TemplateHashModel) staticModels.get("com.alvermont.terraj.stargen.ui.UIUtils");
        
        map.put("uiutils", uiStatics);
        
        map.put("enviro", new Enviro());       
        
        map.put("imageroot", "images/");
    }
    
    /**
     * Process a template meant to describe one planet 
     *
     * @param template The FreeMarker template to be used
     * @param planet The planet object to be described
     * @param star The parent star for the planet
     * @param out The stream where the output is to be written
     * @throws freemarker.template.TemplateException If there is an error in the template
     * @throws java.io.IOException If there is an error writing the output
     */
    public void processTemplate(Template template, Planet planet, Primary star, Writer out)
    throws TemplateException, IOException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        
        PlanetEvaluator evaluator = new StandardPlanetEvaluator();
        evaluator.evaluate(planet);

        map.put("planet", planet);
        map.put("evaluator", evaluator);      
        
        addStatics(map);
        
        map.put("star", star);

        template.process(map, out);
    }

    /**
     * Process a template meant to describe a star 
     *
     * @param template The FreeMarker template to be used
     * @param star The parent star for the planet
     * @param out The stream where the output is to be written
     * @throws freemarker.template.TemplateException If there is an error in the template
     * @throws java.io.IOException If there is an error writing the output
     */
    public void processTemplate(Template template, Primary star, Writer out)
    throws TemplateException, IOException
    {
        Map<String, Object> map = new HashMap<String, Object>();
                
        addStatics(map);
        
        map.put("star", star);

        template.process(map, out);
    }

    /**
     * Process a template meant to describe all the planets in a system 
     *
     * @param template The FreeMarker template to be used
     * @param planets The list of planet object to be described
     * @param star The parent star for the planets
     * @param out The stream where the output is to be written
     * @throws freemarker.template.TemplateException If there is an error in the template
     * @throws java.io.IOException If there is an error writing the output
     */
    public void processTemplate(Template template, List<Planet> planets, Primary star, Writer out)
    throws TemplateException, IOException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        
        List<PlanetEvaluator> evaluators = new ArrayList<PlanetEvaluator>();
        
        for (Planet planet : planets)
        {
            PlanetEvaluator evaluator = new StandardPlanetEvaluator();
            evaluator.evaluate(planet);
            
            evaluators.add(evaluator);
        }

        map.put("planets", planets);
        map.put("evaluators", evaluators);
        
        SystemEvaluator syseval = new StandardSystemEvaluator();
        
        syseval.evaluate(planets);
        
        map.put("syseval", syseval);
        
        map.put("star", star);
        
        addStatics(map);
        
        template.process(map, out);
    }
    
}
