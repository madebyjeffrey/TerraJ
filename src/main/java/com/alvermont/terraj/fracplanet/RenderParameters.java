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
 * RenderParameters.java
 *
 * Created on December 31, 2005, 10:07 AM
 *
 */
package com.alvermont.terraj.fracplanet;

import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.render.CameraPosition;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds the set of options that affect rendering
 *
 * @author martin
 * @version $Id: RenderParameters.java,v 1.3 2006/07/06 06:58:34 martin Exp $
 */
public class RenderParameters implements Serializable
{
    /**
     * Our logging object
     */
    private static Log log = LogFactory.getLog(RenderParameters.class);

    /**
     * Initial default value
     */
    public static final float DEFAULT_FOG_GREY = 0.8f;

    /**
     * Default FPS target
     */
    public static final float DEFAULT_FPS_TARGET = 50.0f;

    /**
     * Default fog distance
     */
    public static final float DEFAULT_FOG_DISTANCE = 1.4f;

    /**
     * Default ambient light value
     */
    public static final float DEFAULT_AMBIENT_VALUE = 0.2f;

    // checkstyle doesn't hande statics currently
    // RequireThis OFF: DEFAULT_FOG_GREY
    // RequireThis OFF: DEFAULT_FPS_TARGET
    // RequireThis OFF: DEFAULT_FOG_DISTANCE
    // RequireThis OFF: DEFAULT_AMBIENT_VALUE
    /**
     * Creates a new instance of RenderParameters Note: the disable GL delete
     * list is outside the scope of resets as if it is changed on an affected
     * system then the program may crash
     */
    public RenderParameters()
    {
        reset();

        this.disableGLDeleteList = false;
    }

    /**
     * Reset the rendering option parameters to default values
     */
    public void resetOptions()
    {
        this.wireframe = false;
        this.displayList = true;
        this.joystickMouse = true;

        fpsTarget = this.DEFAULT_FPS_TARGET;
    }

    /**
     * Reset the ambient rendering parameters to default values
     */
    public void resetAmbient()
    {
        this.enableFog = false;
        this.fogColour = new FloatRGBA(
                this.DEFAULT_FOG_GREY, this.DEFAULT_FOG_GREY,
                this.DEFAULT_FOG_GREY);
        this.fogDistance = DEFAULT_FOG_DISTANCE;
        this.ambient = DEFAULT_AMBIENT_VALUE;
    }

    /**
     * Reset the sunlight rendering parameters to default values
     */
    public void resetSunlight()
    {
        // MagicNumber OFF
        this.sunPosition = new SimpleXYZ(-100.0f, 0.0f, 0.0f);
        this.sunColour = new FloatRGBA(1.0f, 1.0f, 1.0f);

        // MagicNumber ON
    }

    /**
     * Reset all the rendering parameters to default values
     */
    public void reset()
    {
        resetOptions();
        resetAmbient();
        resetSunlight();
    }

    /**
     * Holds value of property wireframe.
     */
    private boolean wireframe;

    /**
     * Getter for property wireframe.
     *
     * @return Value of property wireframe.
     */
    public boolean isWireframe()
    {
        return this.wireframe;
    }

    /**
     * Setter for property wireframe.
     *
     * @param wireframe New value of property wireframe.
     */
    public void setWireframe(boolean wireframe)
    {
        this.wireframe = wireframe;
    }

    /**
     * Holds value of property displayList.
     */
    private boolean displayList;

    /**
     * Getter for property displayList.
     *
     * @return Value of property displayList.
     */
    public boolean isDisplayList()
    {
        return this.displayList;
    }

    /**
     * Setter for property displayList.
     *
     * @param displayList New value of property displayList.
     */
    public void setDisplayList(boolean displayList)
    {
        this.displayList = displayList;
    }

    /**
     * Holds value of property joystickMouse.
     */
    private boolean joystickMouse;

    /**
     * Getter for property joystickMouse.
     *
     * @return Value of property joystickMouse.
     */
    public boolean isJoystickMouse()
    {
        return this.joystickMouse;
    }

    /**
     * Setter for property joystickMouse.
     *
     * @param joystickMouse New value of property joystickMouse.
     */
    public void setJoystickMouse(boolean joystickMouse)
    {
        this.joystickMouse = joystickMouse;
    }

    /**
     * Holds value of property ambient.
     */
    private float ambient;

    /**
     * Getter for property ambient.
     *
     * @return Value of property ambient.
     */
    public float getAmbient()
    {
        return this.ambient;
    }

    /**
     * Setter for property ambient.
     *
     * @param ambient New value of property ambient.
     */
    public void setAmbient(float ambient)
    {
        this.ambient = ambient;
    }

    /**
     * Holds value of property fpsTarget.
     */
    private float fpsTarget;

    /**
     * Getter for property fpsTarget.
     *
     * @return Value of property fpsTarget.
     */
    public float getFpsTarget()
    {
        return this.fpsTarget;
    }

    /**
     * Setter for property fpsTarget.
     *
     * @param fpsTarget New value of property fpsTarget.
     */
    public void setFpsTarget(float fpsTarget)
    {
        this.fpsTarget = fpsTarget;
    }

    /**
     * Holds value of property disableGLDeleteList.
     */
    private boolean disableGLDeleteList;

    /**
     * Getter for property disableGLDeleteList.
     *
     * @return Value of property disableGLDeleteList.
     */
    public boolean isDisableGLDeleteList()
    {
        return this.disableGLDeleteList;
    }

    /**
     * Setter for property disableGLDeleteList.
     *
     * @param disableGLDeleteList New value of property disableGLDeleteList.
     */
    public void setDisableGLDeleteList(boolean disableGLDeleteList)
    {
        this.disableGLDeleteList = disableGLDeleteList;
    }

    /**
     * Holds value of property cameraPosition.
     */
    private CameraPosition cameraPosition;

    /**
     * Getter for property cameraPosition.
     *
     * @return Value of property cameraPosition.
     */
    public CameraPosition getCameraPosition()
    {
        return this.cameraPosition;
    }

    /**
     * Setter for property cameraPosition.
     *
     * @param cameraPosition New value of property cameraPosition.
     */
    public void setCameraPosition(CameraPosition cameraPosition)
    {
        this.cameraPosition = cameraPosition;
    }

    /**
     * Holds value of property sunPosition.
     */
    private XYZ sunPosition;

    /**
     * Getter for property sunPosition.
     *
     * @return Value of property sunPosition.
     */
    public XYZ getSunPosition()
    {
        return this.sunPosition;
    }

    /**
     * Setter for property sunPosition.
     *
     * @param sunPosition New value of property sunPosition.
     */
    public void setSunPosition(XYZ sunPosition)
    {
        this.sunPosition = sunPosition;
    }

    /**
     * Holds value of property fogColour.
     */
    private FloatRGBA fogColour;

    /**
     * Getter for property fogColour.
     *
     * @return Value of property fogColour.
     */
    public FloatRGBA getFogColour()
    {
        return this.fogColour;
    }

    /**
     * Setter for property fogColour.
     *
     * @param fogColour New value of property fogColour.
     */
    public void setFogColour(FloatRGBA fogColour)
    {
        this.fogColour = fogColour;
    }

    /**
     * Holds value of property sunColour.
     */
    private FloatRGBA sunColour;

    /**
     * Getter for property sunColour.
     *
     * @return Value of property sunColour.
     */
    public FloatRGBA getSunColour()
    {
        return this.sunColour;
    }

    /**
     * Setter for property sunColour.
     *
     * @param sunColour New value of property sunColour.
     */
    public void setSunColour(FloatRGBA sunColour)
    {
        this.sunColour = sunColour;
    }

    /**
     * Holds value of property fogDistance.
     */
    private float fogDistance;

    /**
     * Getter for property fogDistance.
     *
     * @return Value of property fogDistance.
     */
    public float getFogDistance()
    {
        return this.fogDistance;
    }

    /**
     * Setter for property fogDistance.
     *
     * @param fogDistance New value of property fogDistance.
     */
    public void setFogDistance(float fogDistance)
    {
        this.fogDistance = fogDistance;
    }

    /**
     * Holds value of property enableFog.
     */
    private boolean enableFog;

    /**
     * Getter for property enableFog.
     *
     * @return Value of property enableFog.
     */
    public boolean isEnableFog()
    {
        return this.enableFog;
    }

    /**
     * Setter for property enableFog.
     *
     * @param enableFog New value of property enableFog.
     */
    public void setEnableFog(boolean enableFog)
    {
        this.enableFog = enableFog;
    }
}
