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
 * MeshUtils.java
 *
 * Created on January 2, 2006, 10:40 AM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import java.nio.BufferUnderflowException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility code related to meshes
 *
 * @author martin
 * @version $Id: MeshUtils.java,v 1.5 2006/07/06 06:58:35 martin Exp $
 */
public class MeshUtils
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(MeshUtils.class);

    // RequireThis OFF: log

    /**
     * Create a new instance of MeshUtils
     */
    protected MeshUtils()
    {
        // disallow call from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * Check the structure of a mesh. This was used during debugging to
     * identify problems with the buffered mesh implementation and should
     * not be necessary in normal operation.
     *
     * @param mesh The mesh to be checked
     * @return <pre>true</pre> if the mesh appears to have a correct
     * structure otherwise <pre>false</pre>
     */
    public static boolean checkMeshStructure(final TriangleMesh mesh)
    {
        final int triangles = mesh.getTriangles()
                .size();
        final int vertices = mesh.getVertices()
                .size();

        if (log.isDebugEnabled())
        {
            log.debug(
                "Checking mesh with triangles: " + triangles + " vertices: " +
                vertices);
        }

        boolean ok = true;

        // check triangles first, retrieve all of them and check their
        // vertex numbers are in range
        for (int t = 0; ok && (t < triangles); ++t)
        {
            final Triangle tri = mesh.getTriangles()
                    .get(t);

            for (int v = 0; v < 2; ++v)
            {
                if (mesh.getTriangles()
                        .get(t)
                        .getVertex(v) >= vertices)
                {
                    log.warn(
                        "Mesh has invalid triangle vertex: " +
                        mesh.getTriangles().get(t).getVertex(v) + " max is : " +
                        mesh.getTriangles().size());

                    ok = false;
                }
            }
        }

        // now attempt to retrieve all the vertex data
        int v = 0;

        try
        {
            for (v = 0; v < vertices; ++v)
            {
                final Vertex vertex = mesh.getVertices()
                        .get(v);

                final ByteRGBA col0 = vertex.getColour(0);
                final ByteRGBA col1 = vertex.getColour(1);
            }
        }
        catch (BufferUnderflowException bue)
        {
            log.error("Buffer error accessing vertex data: ", bue);

            throw bue;
        }

        return ok;
    }
}
