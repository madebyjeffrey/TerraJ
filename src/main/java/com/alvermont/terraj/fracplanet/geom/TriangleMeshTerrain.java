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
 * TriangleMeshTerrain.java
 *
 * Created on December 29, 2005, 1:53 PM
 *
 */
package com.alvermont.terraj.fracplanet.geom;

import com.alvermont.terraj.fracplanet.util.Progress;
import com.alvermont.terraj.fracplanet.TerrainParameters;
import com.alvermont.terraj.fracplanet.colour.ByteRGBA;
import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.noise.MultiscaleNoise;
import com.alvermont.terraj.fracplanet.util.TreeMapMulti;
import com.alvermont.terraj.stargen.util.MathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class holds all the terrain-related methods.
 *
 * @author martin
 * @version $Id: TriangleMeshTerrain.java,v 1.14 2006/07/06 06:58:35 martin Exp $
 */
public class TriangleMeshTerrain extends AbstractTriangleMesh
    implements TriangleMesh
{
    /** Our logging object */
    private static Log log = LogFactory.getLog(TriangleMeshTerrain.class);
    private Set<Integer> riverVertices = new HashSet<Integer>();
    private float maxHeight;

    /** Constant value for completion of all steps */
    private static final int STEPS_100_PERCENT = 100;

    /** Constant value for warning progress stall */
    private static final int PROGRESS_STALL = 100;

    // RequireThis OFF: STEPS_100_PERCENT
    // RequireThis OFF: PROGRESS_STALL

    /**
     * Creates a new instance of TriangleMeshTerrain
     *
     * @param progress The progress object to report to during mesh creation
     * @param geometry The geometry to be used
     */
    public TriangleMeshTerrain(Progress progress, Geometry geometry)
    {
        super(progress, geometry);

        this.maxHeight = 0;
    }

    /**
     * Apply the noise functions to the terrain
     *
     * @param parameters The terrain parameters including noise controls
     */
    protected void doNoise(TerrainParameters parameters)
    {
        if (
            (parameters.getNoiseTerms() != 0) &&
                (parameters.getNoiseAmplitude() != 0))
        {
            final int steps = getVertices()
                    .size();
            int step = 0;

            getProgress()
                .progressStart(STEPS_100_PERCENT, "Noise");

            final Random random = new Random(parameters.getTerrainSeed());
            final MathUtils utils = new MathUtils(random);

            final MultiscaleNoise noise =
                new MultiscaleNoise(
                    utils, parameters.getNoiseTerms(),
                    parameters.getNoiseAmplitudeDecay());

            for (int i = 0; i < getVertices()
                    .size(); ++i)
            {
                ++step;
                getProgress()
                    .progressStep((STEPS_100_PERCENT * step) / steps);

                final float h = getVertexHeight(i);
                final float p =
                    parameters.getNoiseAmplitude() * noise.getNoise(
                            XYZMath.opMultiply(
                                parameters.getNoiseFrequency(),
                                getVertices().get(i).getPosition()));

                setVertexHeight(i, h + p);
            }

            getProgress()
                .progressComplete("Noise complete");
        }
    }

    /**
     * Apply the sea level processing to the terrain. This includes determining
     * whether vertices are part of the sea or land.
     *
     * @param parameters TerrainParameters including those that control this
     * operation
     */
    protected void doSeaLevel(TerrainParameters parameters)
    {
        final int steps = getVertices()
                .size() + getTriangles()
                .size();
        int step = 0;

        getProgress()
            .progressStart(STEPS_100_PERCENT, "Sea level");

        final boolean[] seaVertices = new boolean[getVertices()
                .size()];

        for (int i = 0; i < getVertices()
                .size(); ++i)
        {
            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / steps);

            final float m = getVertexHeight(i);

            if (m <= 0.0)
            {
                setVertexHeight(i, 0.0f);
                seaVertices[i] = true;
            }
            else if (m > maxHeight)
            {
                maxHeight = m;
            }
        }

        final TriangleBufferArray land =
            new TriangleBufferArray(getTriangles().size());
        final TriangleBufferArray sea =
            new TriangleBufferArray(getTriangles().size());

        for (int i = 0; i < getTriangles()
                .size(); ++i)
        {
            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / steps);

            final Triangle t = getTriangles()
                    .get(i);

            if (
                seaVertices[t.getVertex(0)] && seaVertices[t.getVertex(1)] &&
                    seaVertices[t.getVertex(2)])
            {
                // then its a sea triangles
                sea.add(t);
            }
            else
            {
                land.add(t);
            }
        }

        setSwitchColour(land.size());
        setTriangles(land);
        getTriangles()
            .addAll(sea);

        getProgress()
            .progressComplete("Sea level completed");

        if (log.isDebugEnabled())
        {
            log.debug(
                "Sea level: " + sea.size() + " land: " + getSwitchColour());
        }
    }

    /**
     * Apply the terrain power law processing.
     *
     * @param parameters TerrainParameters including those that control this
     * operation
     */
    protected void doPowerLaw(TerrainParameters parameters)
    {
        final int steps = getVertices()
                .size();
        int step = 0;

        getProgress()
            .progressStart(STEPS_100_PERCENT, "Power law");

        for (int i = 0; i < getVertices()
                .size(); ++i)
        {
            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / steps);

            final float h = getVertexHeight(i);

            if (h > getGeometry()
                    .epsilon())
            {
                setVertexHeight(
                    i,
                    (float) (maxHeight * Math.pow(
                            h / maxHeight, parameters.getPowerLaw())));
            }
        }

        getProgress()
            .progressComplete("Power law completed");
    }

    /**
     * Prepare for river generation. Creates a list of neighbours for each
     * vertices.
     *
     * @param prepSteps The number of preparation steps in total for
     * progress reports
     * @return A list of neighbours for each vertices
     */
    protected List<Set<Integer>> prepareRiverNeighbours(int prepSteps)
    {
        final List<Set<Integer>> vertexNeighbours =
            new ArrayList<Set<Integer>>(getVertices().size());

        for (int i = 0; i < getVertices()
                .size(); ++i)
            vertexNeighbours.add(new HashSet<Integer>());

        int step = 0;

        for (int i = 0; i < getTriangleColour0Count(); ++i)
        {
            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / prepSteps);

            final Triangle t = getTriangles()
                    .get(i);

            vertexNeighbours.get(t.getVertex(0))
                .add(t.getVertex(1));
            vertexNeighbours.get(t.getVertex(0))
                .add(t.getVertex(2));
            vertexNeighbours.get(t.getVertex(1))
                .add(t.getVertex(0));
            vertexNeighbours.get(t.getVertex(1))
                .add(t.getVertex(2));
            vertexNeighbours.get(t.getVertex(2))
                .add(t.getVertex(0));
            vertexNeighbours.get(t.getVertex(2))
                .add(t.getVertex(1));
        }

        return vertexNeighbours;
    }

    /**
     * Prepare for river generation. Generates a list of vertices that form
     * part of the sea.
     *
     * @param prepSteps The number of preparation steps in total for
     * progress reports
     * @return An array of booleans indicating whether a vertices is a sea vertices
     */
    protected boolean[] prepareSeaVertices(int prepSteps)
    {
        final boolean[] isSeaVertex = new boolean[getVertices()
                .size()];

        int step = 0;

        for (int i = getTriangleColour0Count(); i < getTriangles()
                .size(); ++i)
        {
            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / prepSteps);

            final Triangle t = getTriangles()
                    .get(i);

            isSeaVertex[t.getVertex(0)] = true;
            isSeaVertex[t.getVertex(1)] = true;
            isSeaVertex[t.getVertex(2)] = true;
        }

        return isSeaVertex;
    }

    private void doOneRiver(
        int r, Random random, boolean[] isSeaVertex,
        List<Set<Integer>> vertexNeighbours, int maxLakeSize)
    {
        int lastStallWarning = 0;

        final Set<Integer> verticesToAdd = new HashSet<Integer>();

        final TreeMapMulti verticesToAddByHeight = new TreeMapMulti();

        final Set<Integer> currentVertices = new HashSet<Integer>();

        float currentVerticesHeight = 0;

        // start with a random non sea triangles
        final int sourceVertex =
            (int) (random.nextFloat() * getVertices()
                .size());

        if (!isSeaVertex[sourceVertex])
        {
            currentVertices.add(sourceVertex);
            currentVerticesHeight = getVertexHeight(sourceVertex);

            while (true)
            {
                boolean reachedSea = false;
                final Set<Integer> currentVerticesNeighbours =
                    new HashSet<Integer>();

                for (int it : currentVertices)
                {
                    verticesToAdd.add(it);
                    verticesToAddByHeight.insert(currentVerticesHeight, it);

                    if (isSeaVertex[it])
                    {
                        reachedSea = true;
                    }

                    final Set<Integer> neighbours = vertexNeighbours.get(it);

                    for (int itN : neighbours)
                    {
                        if (!currentVertices.contains(itN))
                        {
                            currentVerticesNeighbours.add(itN);
                        }
                    }
                }

                final TreeMapMulti flowCandidates = new TreeMapMulti();

                for (int it : currentVerticesNeighbours)
                {
                    flowCandidates.insert(getVertexHeight(it), it);
                }

                if (reachedSea)
                {
                    break;
                }
                else if (currentVertices.size() >= maxLakeSize)
                {
                    // lake becomes an inland sea
                    if (log.isDebugEnabled())
                    {
                        log.debug("River " + r + " is now an inland sea");
                    }

                    break;
                }
                else
                {
                    boolean meetsExisting = false;

                    for (int it : currentVertices)
                    {
                        if (riverVertices.contains(it))
                        {
                            meetsExisting = true;

                            break;
                        }
                    }

                    if (meetsExisting)
                    {
                        break;
                    }
                }

                //debugPrintMap(flowCandidates);
                int numCurrentVertices = 0;

                if (flowCandidates.isEmpty())
                {
                    if (log.isDebugEnabled())
                    {
                        log.warn(
                            "Unexpected internal state: no flow candidates " +
                            "for river: " + r);
                    }

                    break;
                }
                else if (
                    flowCandidates.getFirstHeight() < (currentVerticesHeight -
                        getGeometry()
                        .epsilon()))
                {
                    //                        log.debug("Flow downhill");
                    currentVertices.clear();
                    currentVerticesHeight = flowCandidates.getFirstHeight();
                    currentVertices.add(flowCandidates.getFirstVertex());

                    numCurrentVertices = 1;
                }
                else if (
                    flowCandidates.getFirstHeight() < (currentVerticesHeight +
                        getGeometry()
                        .epsilon()))
                {
                    //                        log.debug("Expand across flat");
                    final SortedMap<Float, List<Integer>> subset =
                        flowCandidates.headMap(
                            currentVerticesHeight + getGeometry().epsilon());

                    for (List<Integer> list : subset.values())
                    {
                        currentVertices.addAll(list);
                    }

                    numCurrentVertices = currentVertices.size();
                }
                else
                {
                    //                        log.debug("Raise level");
                    currentVerticesHeight = flowCandidates.getFirstHeight() +
                        getGeometry()
                            .epsilon();

                    final int outflowVertex = flowCandidates.getFirstVertex();

                    currentVertices.add(outflowVertex);

                    final SortedMap<Float, List<Integer>> subset =
                        verticesToAddByHeight.headMap(currentVerticesHeight);

                    for (List<Integer> list : subset.values())
                    {
                        for (int vertex : list)
                        {
                            currentVertices.add(vertex);
                            setVertexHeight(vertex, currentVerticesHeight);
                        }
                    }

                    for (List<Integer> list : subset.values())
                    {
                        list.clear();
                    }

                    for (int it : currentVertices)
                    {
                        setVertexHeight(it, currentVerticesHeight);
                        ++numCurrentVertices;
                    }
                }

                if (numCurrentVertices >= (lastStallWarning + PROGRESS_STALL))
                {
                    final String msg =
                        "Rivers (delay: " + numCurrentVertices +
                        " vertex lake";

                    getProgress()
                        .progressStall(msg);
                }
                else if (
                    (numCurrentVertices + PROGRESS_STALL) <= lastStallWarning)
                {
                    final String msg = "Rivers: lake complete";

                    getProgress()
                        .progressStall(msg);

                    lastStallWarning = numCurrentVertices;
                }
            }

            riverVertices.addAll(verticesToAdd);
        }
    }

    /**
     * Carry out river processing. A number of rivers will be generated in
     * the terrain.
     *
     * @param parameters TerrainParameters including those that control this
     * operation
     */
    protected void doRivers(TerrainParameters parameters)
    {
        if (parameters.getRivers() > 0)
        {
            final Random random = new Random(parameters.getRiversSeed());

            final int prepSteps = getTriangleCount();

            getProgress()
                .progressStart(STEPS_100_PERCENT, "River preparation");

            final List<Set<Integer>> vertexNeighbours =
                prepareRiverNeighbours(prepSteps);

            final boolean[] isSeaVertex = prepareSeaVertices(prepSteps);

            getProgress()
                .progressComplete("River preparation completed");

            getProgress()
                .progressStart(STEPS_100_PERCENT, "Rivers");

            final int maxLakeSize =
                (int) (getVertices()
                    .size() * parameters.getLakeBecomesSea());

            final int steps = parameters.getRivers();

            int step = 0;

            for (int r = 0; r < parameters.getRivers(); ++r)
            {
                ++step;
                getProgress()
                    .progressStep((STEPS_100_PERCENT * step) / steps);

                doOneRiver(
                    r, random, isSeaVertex, vertexNeighbours, maxLakeSize);
            }
        }
    }

    /**
     * Set up a colour scheme for debug purposes rather than the normal
     * shading. Not used in normal operation
     *
     * @param parameters The parameters to be used including colour choices
     */
    protected void doColoursDebug(TerrainParameters parameters)
    {
        for (int i = 0; i < getTriangles()
                .size(); ++i)
        {
            getVertices()
                .get(getTriangles().get(i).getVertex(0))
                .setColour(0, parameters.getColourHigh());
            getVertices()
                .get(getTriangles().get(i).getVertex(1))
                .setColour(0, parameters.getColourHigh());
            getVertices()
                .get(getTriangles().get(i).getVertex(2))
                .setColour(0, parameters.getColourHigh());

            getVertices()
                .get(getTriangles().get(i).getVertex(0))
                .setColour(1, parameters.getColourLow());
            getVertices()
                .get(getTriangles().get(i).getVertex(1))
                .setColour(1, parameters.getColourLow());
            getVertices()
                .get(getTriangles().get(i).getVertex(2))
                .setColour(1, parameters.getColourLow());
        }
    }

    /**
     * Apply colouring to the terrain using the supplied colours.
     *
     * @param parameters The parameters including colour choices
     */
    public void doColours(TerrainParameters parameters)
    {
        final int steps = getTriangleColour1Count() + getVertices()
                .size();
        int step = 0;

        getProgress()
            .progressStart(STEPS_100_PERCENT, "Colouring");

        for (int i = getTriangleColour0Count(); i < getTriangles()
                .size(); ++i)
        {
            final Vertex v0 =
                getVertices()
                    .get(getTriangles().get(i).getVertex(0));
            final Vertex v1 =
                getVertices()
                    .get(getTriangles().get(i).getVertex(1));
            final Vertex v2 =
                getVertices()
                    .get(getTriangles().get(i).getVertex(2));

            v0.setColour(1, parameters.getColourOcean());
            v1.setColour(1, parameters.getColourOcean());
            v2.setColour(1, parameters.getColourOcean());

            if (parameters.getOceansAndRiversEmissive() > 0.0)
            {
                v0.setEmissive(1, true);
                v1.setEmissive(1, true);
                v2.setEmissive(1, true);
            }

            // debug: set the colour0 of triangles to red
            v0.setColour(0, ByteRGBA.RED);
            v1.setColour(0, ByteRGBA.RED);
            v2.setColour(0, ByteRGBA.RED);
        }

        final float treeline = 0.25f;
        final float beachline = 0.01f;

        for (int i = 0; i < getVertices()
                .size(); ++i)
        {
            final Vertex v = getVertices()
                    .get(i);

            ++step;
            getProgress()
                .progressStep((STEPS_100_PERCENT * step) / steps);

            final boolean isRiver = this.riverVertices.contains(i);

            final float averageSlope =
                1.0f -
                (XYZMath.opDotProduct(
                    getGeometry().up(v.getPosition()), v.getNormal()));

            final float normalisedHeight = getVertexHeight(i) / maxHeight;

            float glacierValue = 0.0f;

            if (isRiver)
            {
                glacierValue = parameters.getSnowlineGlacierEffect();
            }

            float snowlineHere =
                (parameters.getSnowlineEquator() +
                (Math.abs(getGeometry().getNormalisedLatitude(v.getPosition())) * (parameters.getSnowlinePole() -
                    parameters.getSnowlineEquator())) +
                (parameters.getSnowlineSlopeEffect() * averageSlope)) -
                glacierValue;

            if (snowlineHere > 0.0f)
            {
                snowlineHere = (float) Math.pow(
                        snowlineHere, parameters.getSnowlinePowerLaw());
            }

            if (normalisedHeight > snowlineHere)
            {
                v.setColour(0, parameters.getColourSnow());
            }
            else if (isRiver)
            {
                v.setColour(0, parameters.getColourRiver());
                v.setEmissive(0, true);
            }
            else if (normalisedHeight < beachline)
            {
                v.setColour(0, parameters.getColourShoreline());
            }
            else if (normalisedHeight < treeline)
            {
                final float blend = normalisedHeight / treeline;

                final FloatRGBA c1 =
                    FloatRGBA.opMultiply(blend, parameters.getColourHigh());
                final FloatRGBA c2 =
                    FloatRGBA.opMultiply(
                        1.0f - blend, parameters.getColourLow());

                v.setColour(0, FloatRGBA.opAdd(c1, c2));
            }
            else
            {
                v.setColour(0, parameters.getColourHigh());
            }
        }

        getProgress()
            .progressComplete("Colouring completed");
    }

    void doTerrain(TerrainParameters param)
    {
        doNoise(param);
        doSeaLevel(param);
        doPowerLaw(param);
        doRivers(param);
        computeVertexNormals();

        if (param.isDebugDisableColours())
        {
            doColoursDebug(param);
        }
        else
        {
            doColours(param);
        }

        setEmissive(param.getOceansAndRiversEmissive());
    }
}
