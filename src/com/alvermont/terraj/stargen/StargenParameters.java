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
 * StargenParameters.java
 *
 * Created on 23 April 2006, 18:11
 */

package com.alvermont.terraj.stargen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds the parameters for solar system generation in a bean
 *
 * @author  martin
 * @version $Id: StargenParameters.java,v 1.3 2006/07/06 06:58:33 martin Exp $
 */
public class StargenParameters
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(StargenParameters.class);
    
    /** Creates a new instance of StargenParameters */
    public StargenParameters()
    {
        reset();
    }

    /** 
     * Reset all the parameters to the defaults
     */
    public void reset()
    {
        this.nameEnabled = false;
        this.name = "Stargen Planetary System";
        this.hipparcusNumberEnabled = false;
        this.hipparcusNumber = 500000;
        this.massEnabled = false;
        this.mass = 1.0;
        this.luminosityEnabled = false;
        this.luminosity = 1.0;
        this.spectralClassEnabled = false;
        this.spectralClass = "G";
        this.spectralSubclassEnabled = false;
        this.spectralSubclass = 2;
        this.luminosityClassEnabled = false;
        this.luminosityClass = "V";
        this.declinationEnabled = false;
        this.declination = 0.0;
        this.rightAscensionEnabled = false;
        this.rightAscension = 0.0;
        this.distanceEnabled = false;
        this.distance = 10.0;
    }
    
    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Holds value of property hipparcusNumber.
     */
    private int hipparcusNumber;

    /**
     * Getter for property hipparcusNumber.
     * @return Value of property hipparcusNumber.
     */
    public int getHipparcusNumber()
    {
        return this.hipparcusNumber;
    }

    /**
     * Setter for property hipparcusNumber.
     * @param hipparcusNumber New value of property hipparcusNumber.
     */
    public void setHipparcusNumber(int hipparcusNumber)
    {
        this.hipparcusNumber = hipparcusNumber;
    }

    /**
     * Holds value of property luminosity.
     */
    private double luminosity;

    /**
     * Getter for property luminosity.
     * @return Value of property luminosity.
     */
    public double getLuminosity()
    {
        return this.luminosity;
    }

    /**
     * Setter for property luminosity.
     * @param luminosity New value of property luminosity.
     */
    public void setLuminosity(double luminosity)
    {
        this.luminosity = luminosity;
    }

    /**
     * Holds value of property mass.
     */
    private double mass;

    /**
     * Getter for property mass.
     * @return Value of property mass.
     */
    public double getMass()
    {
        return this.mass;
    }

    /**
     * Setter for property mass.
     * @param mass New value of property mass.
     */
    public void setMass(double mass)
    {
        this.mass = mass;
    }

    /**
     * Holds value of property spectralClass.
     */
    private String spectralClass;

    /**
     * Getter for property spectralClass.
     * @return Value of property spectralClass.
     */
    public String getSpectralClass()
    {
        return this.spectralClass;
    }

    /**
     * Setter for property spectralClass.
     * @param spectralClass New value of property spectralClass.
     */
    public void setSpectralClass(String spectralClass)
    {
        this.spectralClass = spectralClass;
    }

    /**
     * Holds value of property spectralSubclass.
     */
    private int spectralSubclass;

    /**
     * Getter for property spectralSubclass.
     * @return Value of property spectralSubclass.
     */
    public int getSpectralSubclass()
    {
        return this.spectralSubclass;
    }

    /**
     * Setter for property spectralSubclass.
     * @param spectralSubclass New value of property spectralSubclass.
     */
    public void setSpectralSubclass(int spectralSubclass)
    {
        this.spectralSubclass = spectralSubclass;
    }

    /**
     * Holds value of property luminosityClass.
     */
    private String luminosityClass;

    /**
     * Getter for property luminosityClass.
     * @return Value of property luminosityClass.
     */
    public String getLuminosityClass()
    {
        return this.luminosityClass;
    }

    /**
     * Setter for property luminosityClass.
     * @param luminosityClass New value of property luminosityClass.
     */
    public void setLuminosityClass(String luminosityClass)
    {
        this.luminosityClass = luminosityClass;
    }

    /**
     * Holds value of property distance.
     */
    private double distance;

    /**
     * Getter for property distance.
     * @return Value of property distance.
     */
    public double getDistance()
    {
        return this.distance;
    }

    /**
     * Setter for property distance.
     * @param distance New value of property distance.
     */
    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    /**
     * Holds value of property rightAscension.
     */
    private double rightAscension;

    /**
     * Getter for property rightAscension.
     * @return Value of property rightAscension.
     */
    public double getRightAscension()
    {
        return this.rightAscension;
    }

    /**
     * Setter for property rightAscension.
     * @param rightAscension New value of property rightAscension.
     */
    public void setRightAscension(double rightAscension)
    {
        this.rightAscension = rightAscension;
    }

    /**
     * Holds value of property declination.
     */
    private double declination;

    /**
     * Getter for property declination.
     * @return Value of property declination.
     */
    public double getDeclination()
    {
        return this.declination;
    }

    /**
     * Setter for property declination.
     * @param declination New value of property declination.
     */
    public void setDeclination(double declination)
    {
        this.declination = declination;
    }

    /**
     * Holds value of property seed.
     */
    private long seed;

    /**
     * Getter for property seed.
     * @return Value of property seed.
     */
    public long getSeed()
    {
        return this.seed;
    }

    /**
     * Setter for property seed.
     * @param seed New value of property seed.
     */
    public void setSeed(long seed)
    {
        this.seed = seed;
    }

    /**
     * Holds value of property declinationEnabled.
     */
    private boolean declinationEnabled;

    /**
     * Getter for property declinationEnabled.
     * @return Value of property declinationEnabled.
     */
    public boolean isDeclinationEnabled()
    {
        return this.declinationEnabled;
    }

    /**
     * Setter for property declinationEnabled.
     * @param declinationEnabled New value of property declinationEnabled.
     */
    public void setDeclinationEnabled(boolean declinationEnabled)
    {
        this.declinationEnabled = declinationEnabled;
    }

    /**
     * Holds value of property distanceEnabled.
     */
    private boolean distanceEnabled;

    /**
     * Getter for property distanceEnabled.
     * @return Value of property distanceEnabled.
     */
    public boolean isDistanceEnabled()
    {
        return this.distanceEnabled;
    }

    /**
     * Setter for property distanceEnabled.
     * @param distanceEnabled New value of property distanceEnabled.
     */
    public void setDistanceEnabled(boolean distanceEnabled)
    {
        this.distanceEnabled = distanceEnabled;
    }

    /**
     * Holds value of property hipparcusNumberEnabled.
     */
    private boolean hipparcusNumberEnabled;

    /**
     * Getter for property hipparcusNumberEnabled.
     * @return Value of property hipparcusNumberEnabled.
     */
    public boolean isHipparcusNumberEnabled()
    {
        return this.hipparcusNumberEnabled;
    }

    /**
     * Setter for property hipparcusNumberEnabled.
     * @param hipparcusNumberEnabled New value of property hipparcusNumberEnabled.
     */
    public void setHipparcusNumberEnabled(boolean hipparcusNumberEnabled)
    {
        this.hipparcusNumberEnabled = hipparcusNumberEnabled;
    }

    /**
     * Holds value of property luminosityEnabled.
     */
    private boolean luminosityEnabled;

    /**
     * Getter for property luminosityEnabled.
     * @return Value of property luminosityEnabled.
     */
    public boolean isLuminosityEnabled()
    {
        return this.luminosityEnabled;
    }

    /**
     * Setter for property luminosityEnabled.
     * @param luminosityEnabled New value of property luminosityEnabled.
     */
    public void setLuminosityEnabled(boolean luminosityEnabled)
    {
        this.luminosityEnabled = luminosityEnabled;
    }

    /**
     * Holds value of property luminosityClassEnabled.
     */
    private boolean luminosityClassEnabled;

    /**
     * Getter for property luminosityClassEnabled.
     * @return Value of property luminosityClassEnabled.
     */
    public boolean isLuminosityClassEnabled()
    {
        return this.luminosityClassEnabled;
    }

    /**
     * Setter for property luminosityClassEnabled.
     * @param luminosityClassEnabled New value of property luminosityClassEnabled.
     */
    public void setLuminosityClassEnabled(boolean luminosityClassEnabled)
    {
        this.luminosityClassEnabled = luminosityClassEnabled;
    }

    /**
     * Holds value of property massEnabled.
     */
    private boolean massEnabled;

    /**
     * Getter for property massEnabled.
     * @return Value of property massEnabled.
     */
    public boolean isMassEnabled()
    {
        return this.massEnabled;
    }

    /**
     * Setter for property massEnabled.
     * @param massEnabled New value of property massEnabled.
     */
    public void setMassEnabled(boolean massEnabled)
    {
        this.massEnabled = massEnabled;
    }

    /**
     * Holds value of property nameEnabled.
     */
    private boolean nameEnabled;

    /**
     * Getter for property nameEnabled.
     * @return Value of property nameEnabled.
     */
    public boolean isNameEnabled()
    {
        return this.nameEnabled;
    }

    /**
     * Setter for property nameEnabled.
     * @param nameEnabled New value of property nameEnabled.
     */
    public void setNameEnabled(boolean nameEnabled)
    {
        this.nameEnabled = nameEnabled;
    }

    /**
     * Holds value of property rightAscensionEnabled.
     */
    private boolean rightAscensionEnabled;

    /**
     * Getter for property rightAscensionEnabled.
     * @return Value of property rightAscensionEnabled.
     */
    public boolean isRightAscensionEnabled()
    {
        return this.rightAscensionEnabled;
    }

    /**
     * Setter for property rightAscensionEnabled.
     * @param rightAscensionEnabled New value of property rightAscensionEnabled.
     */
    public void setRightAscensionEnabled(boolean rightAscensionEnabled)
    {
        this.rightAscensionEnabled = rightAscensionEnabled;
    }

    /**
     * Holds value of property seedEnabled.
     */
    private boolean seedEnabled;

    /**
     * Getter for property seedEnabled.
     * @return Value of property seedEnabled.
     */
    public boolean isSeedEnabled()
    {
        return this.seedEnabled;
    }

    /**
     * Setter for property seedEnabled.
     * @param seedEnabled New value of property seedEnabled.
     */
    public void setSeedEnabled(boolean seedEnabled)
    {
        this.seedEnabled = seedEnabled;
    }

    /**
     * Holds value of property spectralClassEnabled.
     */
    private boolean spectralClassEnabled;

    /**
     * Getter for property spectralClassEnabled.
     * @return Value of property spectralClassEnabled.
     */
    public boolean isSpectralClassEnabled()
    {
        return this.spectralClassEnabled;
    }

    /**
     * Setter for property spectralClassEnabled.
     * @param spectralClassEnabled New value of property spectralClassEnabled.
     */
    public void setSpectralClassEnabled(boolean spectralClassEnabled)
    {
        this.spectralClassEnabled = spectralClassEnabled;
    }

    /**
     * Holds value of property spectralSubclassEnabled.
     */
    private boolean spectralSubclassEnabled;

    /**
     * Getter for property spectralSubclassEnabled.
     * @return Value of property spectralSubclassEnabled.
     */
    public boolean isSpectralSubclassEnabled()
    {
        return this.spectralSubclassEnabled;
    }

    /**
     * Setter for property spectralSubclassEnabled.
     * @param spectralSubclassEnabled New value of property spectralSubclassEnabled.
     */
    public void setSpectralSubclassEnabled(boolean spectralSubclassEnabled)
    {
        this.spectralSubclassEnabled = spectralSubclassEnabled;
    }
    
}
