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
 * TerrainParameters.java
 *
 * Created on December 29, 2005, 1:28 PM
 *
 */
package com.alvermont.terraj.fracplanet;

import com.alvermont.terraj.fracplanet.colour.FloatRGBA;
import com.alvermont.terraj.fracplanet.colour.ImmutableFloatRGBA;
import com.alvermont.terraj.fracplanet.geom.SimpleXYZ;
import com.alvermont.terraj.fracplanet.geom.XYZ;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controllable parameters for terrain generation
 *
 * @author martin
 * @version $Id: TerrainParameters.java,v 1.6 2006/07/06 06:58:34 martin Exp $
 */
public class TerrainParameters implements Serializable
{
    /**
     * Our logging object
     */
    private static Log log = LogFactory.getLog(TerrainParameters.class);

    /**
     * The default value for power law
     */
    public static final float DEFAULT_POWER_LAW = 1.5f;

    /**
     * Default number of subdivisions
     */
    public static final int DEFAULT_SUBDIVISIONS = 6;

    /**
     * Default Z variation in subdivision
     */
    public static final float DEFAULT_Z_VARIATION = 0.12f;

    /**
     * Default noise amplitude
     */
    public static final float DEFAULT_NOISE_AMPLITUDE = 0.12f;

    /**
     * Default snowline equator value
     */
    public static final float DEFAULT_SNOWLINE_EQUATOR = 0.8f;

    /**
     * Default snowline pole value
     */
    public static final float DEFAULT_SNOWLINE_POLE = -0.1f;

    /**
     * Default snowline glacier effect value
     */
    public static final float DEFAULT_SNOWLINE_GLACIER_EFFECT = 0.1f;

    /**
     * Default lake becomes sea value (5%)
     */
    public static final float DEFAULT_LAKE_BECOMES_SEA = 0.05f;

    // RequireThis OFF: DEFAULT_POWER_LAW
    // RequireThis OFF: DEFAULT_SUBDIVISIONS
    // RequireThis OFF: DEFAULT_Z_VARIATION
    // RequireThis OFF: DEFAULT_NOISE_AMPLITUDE
    // RequireThis OFF: DEFAULT_SNOWLINE_EQUATOR
    // RequireThis OFF: DEFAULT_SNOWLINE_POLE
    // RequireThis OFF: DEFAULT_LAKE_BECOMES_SEA
    // RequireThis OFF: DEFAULT_SNOWLINE_GLACIER_EFFECT
    /**
     * Enumeration describing the type of terrain
     */
    public enum ObjectTypeEnum
    {
        /**
         * Indicates a spherical terrain should be generated
         */
        PLANET,
        /**
         * Indicates a flat hexagonal terrain should be generated (for back
         * compatibility when there was only one type)
         */
        TERRAIN, 
        /**
         * Indicates a flat hexagonal terrain should be generated
         */
        TERRAIN_HEXAGON, 
        /**
         * Indicates a flat square terrain should be generated
         */
        TERRAIN_SQUARE, 
        /**
         * Indicates a flat triangluar terrain should be generated
         */
        TERRAIN_TRIANGLE;
    }
    ;

    /**
     * Reset the basic terrain parameters to default values
     */
    public void resetTerrainBasicParameters()
    {
        this.baseHeight = 0;
        this.powerLaw = DEFAULT_POWER_LAW;

        this.objectType = ObjectTypeEnum.PLANET;
        this.terrainSeed = 0;

        this.delayGeneration = false;
    }

    /**
     * Reset the terrain subdivision parameters to default values
     */
    public void resetTerrainSubdivParameters()
    {
        this.subdivisions = DEFAULT_SUBDIVISIONS;
        this.subdivisionsUnperturbed = 1;
        this.variation = new SimpleXYZ(0.0f, 0.0f, DEFAULT_Z_VARIATION);
    }

    /**
     * Reset the terrain noise parameters to default values
     */
    public void resetTerrainNoiseParameters()
    {
        this.noiseTerms = 0;
        this.noiseFrequency = 1.0f;
        this.noiseAmplitude = DEFAULT_NOISE_AMPLITUDE;
        this.noiseAmplitudeDecay = 0.5f;
    }

    /**
     * Reset the terrain snow parameters to default values
     */
    public void resetSnowParameters()
    {
        this.snowlineEquator = DEFAULT_SNOWLINE_EQUATOR;
        this.snowlinePole = DEFAULT_SNOWLINE_POLE;
        this.snowlinePowerLaw = 1.0f;
        this.snowlineSlopeEffect = 1.0f;
        this.snowlineGlacierEffect = DEFAULT_SNOWLINE_GLACIER_EFFECT;
    }

    /**
     * Reset the terrain river parameters to default values
     */
    public void resetRiverParameters()
    {
        this.rivers = 0;
        this.riversSeed = 0;
        this.lakeBecomesSea = DEFAULT_LAKE_BECOMES_SEA;
    }

    /**
     * Reset the terrain colour parameters to default values
     */
    public void resetColourParameters()
    {
        this.oceansAndRiversEmissive = 0.0f;

        this.colourOcean = new ImmutableFloatRGBA(0.0f, 0.0f, 1.0f);
        this.colourRiver = new ImmutableFloatRGBA(0.0f, 0.0f, 1.0f);
        this.colourShoreline = new ImmutableFloatRGBA(1.0f, 1.0f, 0.0f);
        this.colourLow = new ImmutableFloatRGBA(0.0f, 1.0f, 0.0f);
        this.colourHigh = new ImmutableFloatRGBA(1.0f, 0.5f, 0.0f);
        this.colourSnow = new ImmutableFloatRGBA(1.0f, 1.0f, 1.0f);

        this.debugDisableColours = false;
    }

    /**
     * Reset all terrain parameters to default values
     */
    public void reset()
    {
        resetTerrainBasicParameters();
        resetTerrainSubdivParameters();
        resetTerrainNoiseParameters();

        resetSnowParameters();

        resetRiverParameters();

        resetColourParameters();
    }

    /**
     * Creates a new instance of TerrainParameters
     */
    public TerrainParameters()
    {
        reset();
    }

    /**
     * Holds value of property objectType.
     */
    private ObjectTypeEnum objectType;

    /**
     * Getter for property objectType.
     *
     * @return Value of property objectType.
     */
    public ObjectTypeEnum getObjectType()
    {
        return this.objectType;
    }

    /**
     * Setter for property objectType.
     *
     * @param objectType New value of property objectType.
     */
    public void setObjectType(ObjectTypeEnum objectType)
    {
        this.objectType = objectType;
    }

    /**
     * Holds value of property terrainSeed.
     */
    private int terrainSeed;

    /**
     * Getter for property seed.
     *
     * @return Value of property seed.
     */
    public int getTerrainSeed()
    {
        return this.terrainSeed;
    }

    /**
     * Setter for property terrainSeed.
     *
     * @param terrainSeed New value of property terrainSeed.
     */
    public void setTerrainSeed(int terrainSeed)
    {
        this.terrainSeed = terrainSeed;
    }

    /**
     * Holds value of property subdivisions.
     */
    private int subdivisions;

    /**
     * Getter for property subdivisions.
     *
     * @return Value of property subdivisions.
     */
    public int getSubdivisions()
    {
        return this.subdivisions;
    }

    /**
     * Setter for property subdivisions.
     *
     * @param subdivisions New value of property subdivisions.
     */
    public void setSubdivisions(int subdivisions)
    {
        this.subdivisions = subdivisions;
    }

    /**
     * Holds value of property subdivisionsUnperturbed.
     */
    private int subdivisionsUnperturbed;

    /**
     * Getter for property subdivisionsUnperturbed.
     *
     * @return Value of property subdivisionsUnperturbed.
     */
    public int getSubdivisionsUnperturbed()
    {
        return this.subdivisionsUnperturbed;
    }

    /**
     * Setter for property subdivisionsUnperturbed.
     *
     * @param subdivisionsUnperturbed New value of property
     *        subdivisionsUnperturbed.
     */
    public void setSubdivisionsUnperturbed(int subdivisionsUnperturbed)
    {
        this.subdivisionsUnperturbed = subdivisionsUnperturbed;
    }

    /**
     * Holds value of property variation.
     */
    private XYZ variation;

    /**
     * Getter for property variation.
     *
     * @return Value of property variation.
     */
    public XYZ getVariation()
    {
        return this.variation;
    }

    /**
     * Setter for property variation.
     *
     * @param variation New value of property variation.
     */
    public void setVariation(XYZ variation)
    {
        this.variation = variation;
    }

    /**
     * Holds value of property noiseTerms.
     */
    private int noiseTerms;

    /**
     * Getter for property noiseTerms.
     *
     * @return Value of property noiseTerms.
     */
    public int getNoiseTerms()
    {
        return this.noiseTerms;
    }

    /**
     * Setter for property noiseTerms.
     *
     * @param noiseTerms New value of property noiseTerms.
     */
    public void setNoiseTerms(int noiseTerms)
    {
        this.noiseTerms = noiseTerms;
    }

    /**
     * Holds value of property noiseFrequency.
     */
    private float noiseFrequency;

    /**
     * Getter for property noiseFrequency.
     *
     * @return Value of property noiseFrequency.
     */
    public float getNoiseFrequency()
    {
        return this.noiseFrequency;
    }

    /**
     * Setter for property noiseFrequency.
     *
     * @param noiseFrequency New value of property noiseFrequency.
     */
    public void setNoiseFrequency(float noiseFrequency)
    {
        this.noiseFrequency = noiseFrequency;
    }

    /**
     * Holds value of property noiseAmplitude.
     */
    private float noiseAmplitude;

    /**
     * Getter for property noiseAmplitude.
     *
     * @return Value of property noiseAmplitude.
     */
    public float getNoiseAmplitude()
    {
        return this.noiseAmplitude;
    }

    /**
     * Setter for property noiseAmplitude.
     *
     * @param noiseAmplitude New value of property noiseAmplitude.
     */
    public void setNoiseAmplitude(float noiseAmplitude)
    {
        this.noiseAmplitude = noiseAmplitude;
    }

    /**
     * Holds value of property noiseAmplitudeDecay.
     */
    private float noiseAmplitudeDecay;

    /**
     * Getter for property noiseAmplitudeDecay.
     *
     * @return Value of property noiseAmplitudeDecay.
     */
    public float getNoiseAmplitudeDecay()
    {
        return this.noiseAmplitudeDecay;
    }

    /**
     * Setter for property noiseAmplitudeDecay.
     *
     * @param noiseAmplitudeDecay New value of property noiseAmplitudeDecay.
     */
    public void setNoiseAmplitudeDecay(float noiseAmplitudeDecay)
    {
        this.noiseAmplitudeDecay = noiseAmplitudeDecay;
    }

    /**
     * Holds value of property baseHeight.
     */
    private float baseHeight;

    /**
     * Getter for property baseHeight.
     *
     * @return Value of property baseHeight.
     */
    public float getBaseHeight()
    {
        return this.baseHeight;
    }

    /**
     * Setter for property baseHeight.
     *
     * @param baseHeight New value of property baseHeight.
     */
    public void setBaseHeight(float baseHeight)
    {
        this.baseHeight = baseHeight;
    }

    /**
     * Holds value of property powerLaw.
     */
    private float powerLaw;

    /**
     * Getter for property powerLaw.
     *
     * @return Value of property powerLaw.
     */
    public float getPowerLaw()
    {
        return this.powerLaw;
    }

    /**
     * Setter for property powerLaw.
     *
     * @param powerLaw New value of property powerLaw.
     */
    public void setPowerLaw(float powerLaw)
    {
        this.powerLaw = powerLaw;
    }

    /**
     * Holds value of property snowlineEquator.
     */
    private float snowlineEquator;

    /**
     * Getter for property snowlineEquator.
     *
     * @return Value of property snowlineEquator.
     */
    public float getSnowlineEquator()
    {
        return this.snowlineEquator;
    }

    /**
     * Setter for property snowlineEquator.
     *
     * @param snowlineEquator New value of property snowlineEquator.
     */
    public void setSnowlineEquator(float snowlineEquator)
    {
        this.snowlineEquator = snowlineEquator;
    }

    /**
     * Holds value of property snowlinePole.
     */
    private float snowlinePole;

    /**
     * Getter for property snowlinePole.
     *
     * @return Value of property snowlinePole.
     */
    public float getSnowlinePole()
    {
        return this.snowlinePole;
    }

    /**
     * Setter for property snowlinePole.
     *
     * @param snowlinePole New value of property snowlinePole.
     */
    public void setSnowlinePole(float snowlinePole)
    {
        this.snowlinePole = snowlinePole;
    }

    /**
     * Holds value of property snowlinePowerLaw.
     */
    private float snowlinePowerLaw;

    /**
     * Getter for property snowlinePowerLaw.
     *
     * @return Value of property snowlinePowerLaw.
     */
    public float getSnowlinePowerLaw()
    {
        return this.snowlinePowerLaw;
    }

    /**
     * Setter for property snowlinePowerLaw.
     *
     * @param snowlinePowerLaw New value of property snowlinePowerLaw.
     */
    public void setSnowlinePowerLaw(float snowlinePowerLaw)
    {
        this.snowlinePowerLaw = snowlinePowerLaw;
    }

    /**
     * Holds value of property snowlineSlopeEffect.
     */
    private float snowlineSlopeEffect;

    /**
     * Getter for property snowlineSlopeEffect.
     *
     * @return Value of property snowlineSlopeEffect.
     */
    public float getSnowlineSlopeEffect()
    {
        return this.snowlineSlopeEffect;
    }

    /**
     * Setter for property snowlineSlopeEffect.
     *
     * @param snowlineSlopeEffect New value of property snowlineSlopeEffect.
     */
    public void setSnowlineSlopeEffect(float snowlineSlopeEffect)
    {
        this.snowlineSlopeEffect = snowlineSlopeEffect;
    }

    /**
     * Holds value of property snowlineGlacierEffect.
     */
    private float snowlineGlacierEffect;

    /**
     * Getter for property snowlineGlacierEffect.
     *
     * @return Value of property snowlineGlacierEffect.
     */
    public float getSnowlineGlacierEffect()
    {
        return this.snowlineGlacierEffect;
    }

    /**
     * Setter for property snowlineGlacierEffect.
     *
     * @param snowlineGlacierEffect New value of property
     *        snowlineGlacierEffect.
     */
    public void setSnowlineGlacierEffect(float snowlineGlacierEffect)
    {
        this.snowlineGlacierEffect = snowlineGlacierEffect;
    }

    /**
     * Holds value of property rivers.
     */
    private int rivers;

    /**
     * Getter for property rivers.
     *
     * @return Value of property rivers.
     */
    public int getRivers()
    {
        return this.rivers;
    }

    /**
     * Setter for property rivers.
     *
     * @param rivers New value of property rivers.
     */
    public void setRivers(int rivers)
    {
        this.rivers = rivers;
    }

    /**
     * Holds value of property riversSeed.
     */
    private int riversSeed;

    /**
     * Getter for property riversSeed.
     *
     * @return Value of property riversSeed.
     */
    public int getRiversSeed()
    {
        return this.riversSeed;
    }

    /**
     * Setter for property riversSeed.
     *
     * @param riversSeed New value of property riversSeed.
     */
    public void setRiversSeed(int riversSeed)
    {
        this.riversSeed = riversSeed;
    }

    /**
     * Holds value of property lakeBecomesSea.
     */
    private float lakeBecomesSea;

    /**
     * Getter for property lakeBecomesSea.
     *
     * @return Value of property lakeBecomesSea.
     */
    public float getLakeBecomesSea()
    {
        return this.lakeBecomesSea;
    }

    /**
     * Setter for property lakeBecomesSea.
     *
     * @param lakeBecomesSea New value of property lakeBecomesSea.
     */
    public void setLakeBecomesSea(float lakeBecomesSea)
    {
        this.lakeBecomesSea = lakeBecomesSea;
    }

    /**
     * Holds value of property oceansAndRiversEmissive.
     */
    private float oceansAndRiversEmissive;

    /**
     * Getter for property oceansAndRiversEmissive.
     *
     * @return Value of property oceansAndRiversEmissive.
     */
    public float getOceansAndRiversEmissive()
    {
        return this.oceansAndRiversEmissive;
    }

    /**
     * Setter for property oceansAndRiversEmissive.
     *
     * @param oceansAndRiversEmissive New value of property
     *        oceansAndRiversEmissive.
     */
    public void setOceansAndRiversEmissive(float oceansAndRiversEmissive)
    {
        this.oceansAndRiversEmissive = oceansAndRiversEmissive;
    }

    /**
     * Holds value of property colourOcean.
     */
    private FloatRGBA colourOcean;

    /**
     * Getter for property colourOcean.
     *
     * @return Value of property colourOcean.
     */
    public FloatRGBA getColourOcean()
    {
        return this.colourOcean;
    }

    /**
     * Setter for property colourOcean.
     *
     * @param colourOcean New value of property colourOcean.
     */
    public void setColourOcean(FloatRGBA colourOcean)
    {
        this.colourOcean = colourOcean;
    }

    /**
     * Holds value of property colourRiver.
     */
    private FloatRGBA colourRiver;

    /**
     * Getter for property colourRiver.
     *
     * @return Value of property colourRiver.
     */
    public FloatRGBA getColourRiver()
    {
        return this.colourRiver;
    }

    /**
     * Setter for property colourRiver.
     *
     * @param colourRiver New value of property colourRiver.
     */
    public void setColourRiver(FloatRGBA colourRiver)
    {
        this.colourRiver = colourRiver;
    }

    /**
     * Holds value of property colourShoreline.
     */
    private FloatRGBA colourShoreline;

    /**
     * Getter for property colourShoreline.
     *
     * @return Value of property colourShoreline.
     */
    public FloatRGBA getColourShoreline()
    {
        return this.colourShoreline;
    }

    /**
     * Setter for property colourShoreline.
     *
     * @param colourShoreline New value of property colourShoreline.
     */
    public void setColourShoreline(FloatRGBA colourShoreline)
    {
        this.colourShoreline = colourShoreline;
    }

    /**
     * Holds value of property colourLow.
     */
    private FloatRGBA colourLow;

    /**
     * Getter for property colourLow.
     *
     * @return Value of property colourLow.
     */
    public FloatRGBA getColourLow()
    {
        return this.colourLow;
    }

    /**
     * Setter for property colourLow.
     *
     * @param colourLow New value of property colourLow.
     */
    public void setColourLow(FloatRGBA colourLow)
    {
        this.colourLow = colourLow;
    }

    /**
     * Holds value of property colourHigh.
     */
    private FloatRGBA colourHigh;

    /**
     * Getter for property colourHigh.
     *
     * @return Value of property colourHigh.
     */
    public FloatRGBA getColourHigh()
    {
        return this.colourHigh;
    }

    /**
     * Setter for property colourHigh.
     *
     * @param colourHigh New value of property colourHigh.
     */
    public void setColourHigh(FloatRGBA colourHigh)
    {
        this.colourHigh = colourHigh;
    }

    /**
     * Holds value of property colourSnow.
     */
    private FloatRGBA colourSnow;

    /**
     * Getter for property colourSnow.
     *
     * @return Value of property colourSnow.
     */
    public FloatRGBA getColourSnow()
    {
        return this.colourSnow;
    }

    /**
     * Setter for property colourSnow.
     *
     * @param colourSnow New value of property colourSnow.
     */
    public void setColourSnow(FloatRGBA colourSnow)
    {
        this.colourSnow = colourSnow;
    }

    /**
     * Holds value of property delayGeneration.
     */
    private boolean delayGeneration;

    /**
     * Getter for property delayGeneration.
     *
     * @return Value of property delayGeneration.
     */
    public boolean isDelayGeneration()
    {
        return this.delayGeneration;
    }

    /**
     * Setter for property delayGeneration.
     *
     * @param delayGeneration New value of property delayGeneration.
     */
    public void setDelayGeneration(boolean delayGeneration)
    {
        this.delayGeneration = delayGeneration;
    }

    /**
     * Holds value of property debugDisableColours.
     */
    private boolean debugDisableColours;

    /**
     * Getter for property debugDisableColours.
     *
     * @return Value of property debugDisableColours.
     */
    public boolean isDebugDisableColours()
    {
        return this.debugDisableColours;
    }

    /**
     * Setter for property debugDisableColours.
     *
     * @param debugDisableColours New value of property debugDisableColours.
     */
    public void setDebugDisableColours(boolean debugDisableColours)
    {
        this.debugDisableColours = debugDisableColours;
    }
}
