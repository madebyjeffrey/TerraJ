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
 * RenderTest.java
 *
 * Created on 27 January 2006, 09:36
 */
package com.alvermont.terraj.fracplanet.rings;

import com.alvermont.terraj.fracplanet.AllFracplanetParameters;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.render.CameraPosition;
import java.awt.image.BufferedImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import threeD.raytracer.camera.Camera;
import threeD.raytracer.camera.PinholeCamera;
import threeD.raytracer.engine.RayTracingEngine;
import threeD.raytracer.engine.Scene;
import threeD.raytracer.graphics.RGB;
import threeD.raytracer.lighting.Light;
import threeD.raytracer.lighting.PointLight;
import threeD.raytracer.primitives.Mesh;
import threeD.ui.displays.RenderTestFrame;

/**
 * Test class to attempt a Rings render
 *
 * @author  martin
 * @version $Id: RenderTest.java,v 1.12 2006/07/06 06:59:43 martin Exp $
 */
public class RenderTest
{
    /** Our logger object */
    private static Log log = LogFactory.getLog(RenderTest.class);

    // RequireThis OFF: log

    /** Creates a new instance of RenderTest */
    public RenderTest()
    {
    }

    /**
     * Convert our sun information to a light source for Rings
     *
     * @param params The parameters we're using
     * @return The light object that Rings can use
     */
    protected Light getSunLight(AllFracplanetParameters params)
    {
        final com.alvermont.terraj.fracplanet.RenderParameters renderParams =
            params.getRenderParameters();

        final XYZ sunLocation = renderParams.getSunPosition();
        final FloatRGBA sunColour = renderParams.getSunColour();

        final Light sun =
            new PointLight(
                MeshTranslator.toVector(sunLocation), 1.0,
                MeshTranslator.toRGB(sunColour));

        return sun;
    }

    /**
     * Get a camera object suitable for Rings
     *
     * @param params The parameters we're using
     * @return The camera object that Rings can use
     */
    protected Camera getCamera(AllFracplanetParameters params)
    {
        final com.alvermont.terraj.fracplanet.RenderParameters renderParams =
            params.getRenderParameters();

        final CameraPosition pos = renderParams.getCameraPosition();

        final PinholeCamera camera =
            new PinholeCamera(
                MeshTranslator.toVector(pos.getEye()),
                MeshTranslator.toVector(pos.getCentre()),
                MeshTranslator.toVector(pos.getUp()));

        return camera;
    }

    /**
     * Render to a test frame.
     *
     * @param originalMesh Our terrain mesh to be rendered
     * @param params The rendering parameters to be used
     * @return A <code>RenderTestFrame</code>
     */
    public RenderTestFrame renderToFrame(
        TriangleMesh originalMesh, AllFracplanetParameters params)
    {
        log.debug("Starting mesh translation for Rings ...");

        //final Mesh mesh = MeshTranslator.translateToRings(originalMesh);
        final Mesh mesh = new Mesh(new RingsDataAdapter(originalMesh));

        mesh.setColor(new RGB(1.0, 1.0, 1.0));
        mesh.setInterpolateColor(true);
        mesh.setRemoveBackFaces(true);
        mesh.setSmooth(true);
        mesh.loadTree();

        final threeD.raytracer.engine.RenderParameters rp =
            new threeD.raytracer.engine.RenderParameters();

        log.debug("Mesh translation complete.");

        final Scene s = new Scene();

        rp.width = 320;
        rp.height = 200;

        s.addSurface(mesh);
        s.addLight(getSunLight(params));
        s.setCamera(getCamera(params));

        threeD.raytracer.engine.RayTracingEngine.castShadows = false;

        log.debug("Render starting ...");

        final RenderTestFrame rtf = new RenderTestFrame(s, 320, 2);
        rtf.render();

        log.debug("Render complete.");

        return rtf;
    }

    /**
     * Render to a file.
     *
     * @param originalMesh Our terrain mesh to be rendered
     * @param params The rendering parameters to be used
     * @return A <code>BufferedImage</code> holding the render results
     */
    public BufferedImage render(
        TriangleMesh originalMesh, AllFracplanetParameters params)
    {
        final Mesh mesh = MeshTranslator.translateToRings(originalMesh);
        final threeD.raytracer.engine.RenderParameters rp =
            new threeD.raytracer.engine.RenderParameters();

        final Scene s = new Scene();

        rp.width = 320;
        rp.height = 200;

        mesh.loadTree();

        s.addSurface(mesh);
        s.addLight(getSunLight(params));
        s.setCamera(getCamera(params));

        final RenderTestFrame rtf = new RenderTestFrame(s, 320, 2);
        rtf.render();

        final RGB[][] rgb =
            RayTracingEngine.render(s, rtf.getRenderParameters(), null);

        final BufferedImage bimg =
            new BufferedImage(
                rgb.length, rgb[0].length, BufferedImage.TYPE_INT_RGB);

        return bimg;
    }
}
