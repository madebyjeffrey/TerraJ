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
 * PlanetDescriber.java
 *
 * Created on 20 April 2006, 13:56
 */

package com.alvermont.terraj.stargen.evaluator;

import com.alvermont.terraj.stargen.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts the result of evaluating a planet into a string
 *
 * @author  martin
 * @version $Id: PlanetDescriber.java,v 1.3 2006/07/06 06:58:36 martin Exp $
 */
public class PlanetDescriber
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(PlanetDescriber.class);
    
    /**
     * Creates a new instance of PlanetDescriber
     */
    public PlanetDescriber()
    {
    }

    private void describeLocks(PlanetEvaluator evaluator, StringBuffer sb)
    {
        if (evaluator.isOneFace())
        {
            sb.append("Tidally Locked 1-Face ");
        }
        
        if (evaluator.isSpinLocked())
        {
            sb.append("Resonant Spin Locked ");
        }        
    }
    
    private void describeGravity(PlanetEvaluator evaluator, StringBuffer sb)
    {
        switch (evaluator.getGravity())
        {
            case LOW_G:

                sb.append("Low-G ");
                break;

            case HIGH_G:

                sb.append("High-G ");
                break;
        }        
    }
    
    private void describeTemperature(PlanetEvaluator evaluator, StringBuffer sb)
    {
        switch (evaluator.getTemperature())
        {
            case COLD:
                
                sb.append("Cold ");
                break;
                
            case COOL:
                
                sb.append("Cool ");
                break;

            case WARM:
                
                sb.append("Warm ");
                break;

            case HOT:
                
                sb.append("Hot ");
                break;                
        }
        
        if (evaluator.isIcy())
        {
            sb.append("Icy ");
        }
    }

    private void describeAtmosphere(PlanetEvaluator evaluator, StringBuffer sb)
    {
        if (evaluator.getAtmosphere() == PlanetEvaluator.AtmosphereEnum.AIRLESS)
        {
            sb.append("Airless ");
        }
        else
        {
            if (!evaluator.isWaterWorld())
            {
                switch (evaluator.getClimate())
                {
                    case ARID:
                        
                        sb.append("Arid ");
                        break;
                    
                    case DRY:
                        
                        sb.append("Dry ");
                        break;

                    case WET:
                        
                        sb.append("Wet ");
                        break;
                }
            }
            
            switch (evaluator.getClouds())
            {
                case CLOUDLESS:
                    
                    sb.append("Cloudless ");
                    break;

                case FEW_CLOUDS:
                    
                    sb.append("Few clouds ");
                    break;

                case CLOUDY:
                    
                    sb.append("Cloudy ");
                    break;
            }
            
            switch (evaluator.getAtmosphere())
            {
                case TOO_THIN:
                    
                    sb.append("Unbreathably thin atmosphere ");
                    break;
                    
                case THIN:

                    sb.append("Thin atmosphere ");
                    break;

                case NORMAL:

                    sb.append("Normal atmosphere ");
                    break;
                
                case THICK:

                    sb.append("Thick atmosphere ");
                    break;

                case TOO_THICK:

                    sb.append("Unbreathably thick atmosphere ");
                    break;
            }
            
            if (evaluator.isEarthLike())
            {
                sb.append("Earth-like ");
            }
        }        
    }

    private void describeGases(PlanetEvaluator evaluator, StringBuffer sb)
    {
        List<Gas> gases = evaluator.getAtmosphericGases();
        
        if (gases.size() > 0)
        {
            sb.append("(");
            
            for (Gas gas : gases)
            {
                sb.append(gas.getElement().getSymbol() + " ");
            }
            
            if (evaluator.getBreathability() != Breathability.NONE)
            {
                switch (evaluator.getBreathability())
                {
                    case BREATHABLE:
                        
                        sb.append(" - Breathable");
                        break;
                        
                    case UNBREATHABLE:
                        
                        sb.append(" - Unbreathable");
                        break;
                        
                    case POISONOUS:
                        
                        sb.append(" - Poisonous");
                        break;
                }
            }
            
            sb.append(") ");
        }
    }
    
    /**
     * Gets a short planet description including the details of the the
     * notable features of the planet.
     * 
     * @param evaluator The evaluator that holds the planet object
     * @return A short one sentence string describing the planet
     */
    public String describePlanet(PlanetEvaluator evaluator)
    {
        StringBuffer sb = new StringBuffer();
        
        describeLocks(evaluator, sb);
        
        if (!evaluator.isGasGiant())
        {
            describeGravity(evaluator, sb);
            describeTemperature(evaluator, sb);
            describeAtmosphere(evaluator, sb);
            describeGases(evaluator, sb);

            if (evaluator.isSpinLocked() || evaluator.isOceanBoiling())
            {
                sb.append("1-Face ");
            }
        }
        
        return sb.toString();
    }
    
}
