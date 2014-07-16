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
 * POVExporter.java
 *
 * Created on January 11, 2006, 8:55 AM
 *
 */
package com.alvermont.terraj.fracplanet.io;

import com.alvermont.terraj.fracplanet.AllFracplanetParameters;
import com.alvermont.terraj.fracplanet.ExportParameters;
import com.alvermont.terraj.fracplanet.RenderParameters;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.geom.Triangle;
import com.alvermont.terraj.fracplanet.geom.TriangleMesh;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrain;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrainFlat;
import com.alvermont.terraj.fracplanet.geom.TriangleMeshTerrainPlanet;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import com.alvermont.terraj.fracplanet.render.CameraPosition;
import com.alvermont.terraj.fracplanet.util.Progress;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Code to write POV format files
 *
 * @author martin
 * @version $Id: POVExporter.java,v 1.14 2006/07/06 06:59:43 martin Exp $
 */
public class POVExporter implements Exporter
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleMeshTerrainPlanet.class);

    /** The terrain object that we're exporting */
    private List<TriangleMesh> meshes;

    /** Number of steps in 100 percent */
    private static final int STEPS_100_PERCENT = 100;

    // RequireThis OFF: STEPS_100_PERCENT

    /**
     * Creates a new instance of POVExporter
     *
     * @param meshes The list of meshes to be exported
     */
    public POVExporter(List<TriangleMesh> meshes)
    {
        this.meshes = meshes;
    }

    /**
     * Write the header to the file for a terrain based on a flat geometry
     *
     * @param terrain The mesh to be exported
     * @param where The <code>PrintWriter</code> to write the data to
     * @param params The export parameters object to us
     * @param tp The terrain parameters object to use
     * @throws java.io.IOException If there is an error writing the data
     */
    protected void writeHeaderFlat(
        TriangleMesh terrain, PrintWriter where, ExportParameters params,
        TerrainParameters tp) throws IOException
    {
        if (params.isSeaObject())
        {
            where.println("plane {<0.0,1.0,0.0>,0.0 pigment{rgb ");
            where.println(formatRGB(tp.getColourOcean()));
            where.println(
                "} finish {ambient " + terrain.getEmissive() + " diffuse " +
                (1.0f - terrain.getEmissive()));
            where.println("}}");
        }

        if (params.isAtmosphere())
        {
            where.println(
                "plane {<0.0,1.0,0.0>,0.05 hollow texture " +
                "{pigment {color rgbf 1}} " +
                "interior{media{scattering{1,color " +
                "rgb <1.0,1.0,1.0> extinction 1}}}}");
            where.println(
                "plane {<0.0,1.0,0.0>,0.1  hollow texture " +
                "{pigment {color rgbf 1}} " +
                "interior{media{scattering{1,color " +
                "rgb <0.0,0.0,1.0> extinction 1}}}}");
        }
    }

    /**
     * Format a colour value in a <code>FloatRGBA</code> into POV format
     * without alpha
     *
     * @param rgb The colour to be formatted and output
     * @return A string containg the POV format of the input colour
     */
    protected String formatRGB(FloatRGBA rgb)
    {
        return "<" + rgb.getR() + "," + rgb.getG() + "," + rgb.getB() + ">";
    }

    /**
     * Format a colour value in a <code>FloatRGBA</code> into POV format
     *
     * @param rgb The colour to be formatted and output
     * @return A string containg the POV format of the input colour
     */
    protected String formatRGBF(FloatRGBA rgb)
    {
        return "<" + rgb.getR() + "," + rgb.getG() + "," + rgb.getB() + "," +
        (1.0f - rgb.getA()) + ">";
    }

    /**
     * Format an XYZ coordinate value in an <code>XYZ</code> into POV format
     *
     * @param xyz The coordinate to be formatted and output
     * @return A string containg the POV format of the input coordinate
     */
    protected String formatXYZ(XYZ xyz)
    {
        return "<" + xyz.getX() + "," + xyz.getZ() + "," + xyz.getY() + ">";
    }

    /**
     * Write the header to the file for a terrain based on a spherical geometry
     *
     * @param terrain The terrain to be exported
     * @param where The <code>PrintWriter</code> to write the data to
     * @param params The export parameters object to us
     * @param tp The terrain parameters object to use
     * @throws java.io.IOException If there is an error writing the data
     */
    protected void writeHeaderSphere(
        TriangleMesh terrain, PrintWriter where, ExportParameters params,
        TerrainParameters tp) throws IOException
    {
        if (params.isSeaObject())
        {
            where.println("sphere {<0.0,0.0,0.0>,1.0 pigment{rgb ");
            where.println(formatRGB(tp.getColourOcean()));
            where.println(
                "} finish {ambient " + terrain.getEmissive() + " diffuse " +
                (1.0f - terrain.getEmissive()));
            where.println("}}");
        }

        if (params.isAtmosphere())
        {
            where.println(
                "sphere {<0.0,0.0,0.0>,1.025 hollow texture " +
                "{pigment {color rgbf 1}} " +
                "interior{media{scattering{1,color " +
                "rgb <1.0,1.0,1.0> extinction 1}}}}");
            where.println(
                "sphere {<0.0,0.0,0.0>,1.05  hollow texture " +
                "{pigment {color rgbf 1}} " +
                "interior{media{scattering{1,color " +
                "rgb <0.0,0.0,1.0> extinction 1}}}}");
        }
    }

    /**
     * Format a camera definition into POV format and return it
     *
     * @param pos The camera position to be formatted
     * @return A string containing the POV format camera definition
     */
    protected String getCameraDefinition(CameraPosition pos)
    {
        String camdef =
            "camera {perspective location " + formatXYZ(pos.getEye());

        camdef += (" sky " + formatXYZ(pos.getUp()));
        camdef += (" look_at " + formatXYZ(pos.getCentre()));
        camdef += " angle 45}";

        return camdef;
    }

    /**
     * Format a sunlight definition into POV format and return it as a POV
     * format lightsource.
     *
     * @param rp The parameters to be used to create the formatted data
     * @return A string containing the POV format lightsource definition
     */
    protected String getSunlightDefinition(RenderParameters rp)
    {
        String sundef = "light_source {";

        sundef += formatXYZ(rp.getSunPosition());
        sundef += (" color rgb " + formatRGB(rp.getSunColour()));
        sundef += "}";

        return sundef;
    }

    /**
     * Format a fog definition into POV format and return it as a POV
     * format fog definition.
     *
     * @param rp The parameters to be used to create the formatted data
     * @return A string containing the POV format fog definition
     */
    protected String getFogDefinition(RenderParameters rp)
    {
        String fogdef = "fog { fog_type 1 distance ";

        fogdef += (rp.getFogDistance() / 138.0452f);
        fogdef += (" color " + formatRGB(rp.getFogColour()));
        fogdef += "}";

        return fogdef;
    }

    /**
     * Export all the vertices to the file
     */
    private int exportVertices(
        TriangleMesh terrain, PrintWriter where, int step, int steps,
        Progress progress)
    {
        int vStep = step;

        for (int v = 0; v < terrain.getVertexCount(); ++v)
        {
            ++vStep;
            progress.progressStep((STEPS_100_PERCENT * vStep) / steps);

            if (v != 0)
            {
                where.print(",");
            }

            where.println(
                formatXYZ(terrain.getVertices().get(v).getPosition()));
        }

        return vStep;
    }

    /**
     * Export the entire mesh data to the output
     *
     * @param first <pre>true</pre> if this is the first mesh written
     * @param terrain The mesh to be exported
     * @param where The <code>PrintWriter</code> to write the data to
     * @param aparams The parameters for use during the export
     * @param progress An object to report export progress to
     * @param doubleIlluminate Whether to request double illumination
     * @param noShadow Whether to disable shadowing
     */
    protected void exportMesh(
        TriangleMesh terrain, PrintWriter where, AllFracplanetParameters aparams,
        Progress progress, boolean doubleIlluminate, boolean noShadow,
        boolean first)
    {
        // TODO: No need to dump all vertices when not outputing all triangles.
        final ExportParameters params = aparams.getExportParameters();
        final RenderParameters rp = aparams.getRenderParameters();

        int trianglesToOutput = terrain.getTriangleCount();

        if (params.isExcludeAlternateColour())
        {
            trianglesToOutput = terrain.getTriangleColour0Count();
        }

        int extraSteps = 0;

        if (!params.isExcludeAlternateColour())
        {
            extraSteps = terrain.getVertexCount();
        }

        // The number of steps is:
        //   vertices() co-ordinates
        // + vertices()+(exclude_alternate_colour ? 0 : vertices()) textures
        // + trianglesToOutput triangles
        final int steps =
            terrain.getVertexCount() + terrain.getVertexCount() + extraSteps +
            trianglesToOutput;

        int step = 0;

        progress.progressStart(STEPS_100_PERCENT, "Writing POV-Ray files");

        // Boilerplate for renderer
        if (first)
        {
            where.println(getCameraDefinition(rp.getCameraPosition()));
            where.println(getSunlightDefinition(rp));

            final FloatRGBA ambient =
                new FloatRGBA(
                    rp.getAmbient(), rp.getAmbient(), rp.getAmbient());

            where.println(
                "global_settings { ambient_light " + formatRGB(ambient) + "}");

            if (rp.isEnableFog())
            {
                where.println(getFogDefinition(rp));
            }

            where.println("");
        }

        // Use POV's mesh2 object
        where.println("mesh2 {");

        // Output all the vertex co-ordinates
        where.println("vertex_vectors {" + terrain.getVertexCount() + ",");

        step = exportVertices(terrain, where, step, steps, progress);

        where.println("}");

        // Output the vertex colours, and handle emission
        // If exclude_alternate_colour is true, don't output the alternate colours
        where.println(
            "texture_list {" + (terrain.getVertexCount() + extraSteps));

        int coloursToDo = 2;

        if (params.isExcludeAlternateColour())
        {
            coloursToDo = 1;
        }

        for (int c = 0; c < coloursToDo; ++c)
        {
            for (int v = 0; v < terrain.getVertexCount(); ++v)
            {
                ++step;
                progress.progressStep((STEPS_100_PERCENT * step) / steps);

                final FloatRGBA col =
                    new FloatRGBA(terrain.getVertices().get(v).getColour(c));

                where.print("texture{pigment{");

                if (col.getA() == 1.0f)
                {
                    where.print("rgb " + formatRGB(col) + "}");
                }
                else
                {
                    where.print("rgbf " + formatRGBF(col) + "}");
                }

                if (
                    (terrain.getEmissive() != 0.0f) &&
                        terrain.getVertices()
                        .get(v)
                        .getEmissive(c))
                {
                    where.print(
                        " finish{ambient " + terrain.getEmissive() +
                        " diffuse " + (1.0f - terrain.getEmissive()) + "}");
                }

                where.println("}");
            }
        }

        where.println("}");

        where.println("face_indices {" + trianglesToOutput + ",");

        boolean skipInitialComma = true;

        for (int t = 0; t < trianglesToOutput; ++t)
        {
            ++step;
            progress.progressStep((STEPS_100_PERCENT * step) / steps);

            if (skipInitialComma)
            {
                skipInitialComma = false;
            }
            else
            {
                where.print(",");
            }

            final Triangle tri = terrain.getTriangles()
                    .get(t);

            int vertexOffset = 0;

            if (t >= terrain.getTriangleColour0Count())
            {
                vertexOffset = terrain.getVertexCount();
            }

            where.print(
                "<" + tri.getVertex(0) + "," + tri.getVertex(1) + "," +
                tri.getVertex(2) + ">");

            where.println("," + (tri.getVertex(0) + vertexOffset));
            where.println("," + (tri.getVertex(1) + vertexOffset));
            where.println("," + (tri.getVertex(2) + vertexOffset));

            where.println("");
        }

        where.println("}");

        if (doubleIlluminate)
        {
            where.println("double_illuminate");
        }

        if (noShadow)
        {
            where.println("no_shadow");
        }

        where.println("}");

        progress.progressComplete("Wrote POV-Ray files");
    }

    /**
     * Carry out an export operation. The exporter should attempt to
     * handle all parameters it can support from the input set and
     * write the result to the specified stream.
     *
     * @param target The stream where data is to be written
     * @param params The parameters describing the terrain and rendering
     * @param progress The object to use for reporting progress
     * @throws java.io.IOException If there is an error exporting the data
     */
    public void export(
        OutputStream target, AllFracplanetParameters params, Progress progress)
        throws IOException
    {
        final PrintWriter pw =
            new PrintWriter(new BufferedWriter(new OutputStreamWriter(target)));

        try
        {
            export(pw, params, progress);
        }
        finally
        {
            pw.close();
        }
    }

    /**
     * Carry out an export operation. The exporter should attempt to
     * handle all parameters it can support from the input set and
     * write the result to the specified file.
     *
     * @param target The file object indicating where data is to be written
     * @param params The parameters describing the terrain and rendering
     * @param progress The object to use for reporting progress
     * @throws java.io.IOException If there is an error exporting the data
     */
    public void export(
        File target, AllFracplanetParameters params, Progress progress)
        throws IOException
    {
        final PrintWriter pw =
            new PrintWriter(new BufferedWriter(new FileWriter(target)));

        try
        {
            export(pw, params, progress);
        }
        finally
        {
            pw.close();
        }
    }

    /**
     * Protected method to implement the actual data export
     *
     * @param pw The <code>PrintWriter</code> to write the data to
     * @param params The parameters describing the terrain and rendering
     * @param progress The object to use for reporting progress
     * @throws java.io.IOException If there is an error exporting the data
     */
    protected void export(
        PrintWriter pw, AllFracplanetParameters params, Progress progress)
        throws IOException
    {
        final ExportParameters ep = params.getExportParameters();
        final TerrainParameters tp = params.getTerrainParameters();

        try
        {
            TriangleMesh terrain = meshes.get(0);

            // write the header
            if (
                terrain instanceof TriangleMeshTerrainPlanet ||
                    terrain instanceof Triangle)
            {
                writeHeaderSphere(terrain, pw, ep, tp);
            }
            else if (terrain instanceof TriangleMeshTerrainFlat)
            {
                writeHeaderFlat(terrain, pw, ep, tp);
            }

            // export all meshes, currently there are 2 at most, the second
            // one is clouds
            for (int m = 0; m < meshes.size(); ++m)
            {
                terrain = meshes.get(m);

                boolean cloudy = false;

                if (m > 0)
                {
                    cloudy = true;
                }

                // export the mesh
                exportMesh(
                    terrain, pw, params, progress, cloudy, cloudy, !cloudy);

                if (pw.checkError())
                {
                    throw new IOException("Error writing to output stream");
                }
            }
        }
        catch (IOException ioe)
        {
            // log the error before rethrowing it
            log.error("IOException in pov export: " + ioe.getMessage(), ioe);

            throw ioe;
        }
        finally
        {
            pw.close();
        }
    }

    /**
     * Return a description of the exporter
     *
     * @return A string describing this exporter object
     */
    public String getDescription()
    {
        return "Exporter for POV format files for POVRay.";
    }
}
