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
 * AllFracplanetParameters.java
 *
 * Created on 13 January 2006, 13:41
 */
package com.alvermont.terraj.fracplanet;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bundles up all the parameters that the program deals with so they can easily
 * be saved etc.
 *
 *
 * @author martin
 * @version $Id: AllFracplanetParameters.java,v 1.4 2006/07/06 06:58:34 martin Exp $
 */
public class AllFracplanetParameters implements Serializable
{
    /**
     * Our logger object
     */
    private static Log log = LogFactory.getLog(AllFracplanetParameters.class);

    /**
     * Creates a new instance of AllFracplanetParameters
     */
    public AllFracplanetParameters()
    {
        renderParameters = new RenderParameters();
        terrainParameters = new TerrainParameters();
        exportParameters = new ExportParameters();
        cameraPositionParameters = new CameraPositionParameters();
        cloudParameters = new CloudParameters();
    }

    /**
     * Holds value of property renderParameters.
     */
    private RenderParameters renderParameters;

    /**
     * Getter for property renderParameters.
     *
     * @return Value of property renderParameters.
     */
    public RenderParameters getRenderParameters()
    {
        return this.renderParameters;
    }

    /**
     * Holds value of property terrainParameters.
     */
    private TerrainParameters terrainParameters;

    /**
     * Getter for property terrainParameters.
     *
     * @return Value of property terrainParameters.
     */
    public com.alvermont.terraj.fracplanet.TerrainParameters getTerrainParameters()
    {
        return this.terrainParameters;
    }

    /**
     * Holds value of property exportParameters.
     */
    private ExportParameters exportParameters;

    /**
     * Getter for property exportParameters.
     *
     * @return Value of property exportParameters.
     */
    public ExportParameters getExportParameters()
    {
        return this.exportParameters;
    }

    /**
     * Setter for property exportParameters.
     *
     * @param exportParameters New value of property exportParameters.
     */
    public void setExportParameters(
        final com.alvermont.terraj.fracplanet.ExportParameters exportParameters)
    {
        this.exportParameters = exportParameters;
    }

    /**
     * Setter for property renderParameters.
     *
     * @param renderParameters New value of property renderParameters.
     */
    public void setRenderParameters(
        final com.alvermont.terraj.fracplanet.RenderParameters renderParameters)
    {
        this.renderParameters = renderParameters;
    }

    /**
     * Setter for property terrainParameters.
     *
     * @param terrainParameters New value of property terrainParameters.
     */
    public void setTerrainParameters(
        final com.alvermont.terraj.fracplanet.TerrainParameters terrainParameters)
    {
        this.terrainParameters = terrainParameters;
    }

    /**
     * Holds value of property cameraPositionParameters.
     */
    private CameraPositionParameters cameraPositionParameters;

    /**
     * Getter for property cameraPositionParameters.
     *
     * @return Value of property cameraPositionParameters.
     */
    public CameraPositionParameters getCameraPositionParameters()
    {
        return this.cameraPositionParameters;
    }

    /**
     * Setter for property cameraPositionParameters.
     *
     * @param cameraPositionParameters New value of property
     *        cameraPositionParameters.
     */
    public void setCameraPositionParameters(
        final CameraPositionParameters cameraPositionParameters)
    {
        this.cameraPositionParameters = cameraPositionParameters;
    }

    /**
     * Holds value of property cloudParameters.
     */
    private CloudParameters cloudParameters;

    /**
     * Getter for property cloudParameters.
     * @return Value of property cloudParameters.
     */
    public CloudParameters getCloudParameters()
    {
        return this.cloudParameters;
    }

    /**
     * Setter for property cloudParameters.
     * @param cloudParameters New value of property cloudParameters.
     */
    public void setCloudParameters(CloudParameters cloudParameters)
    {
        this.cloudParameters = cloudParameters;
    }
}
