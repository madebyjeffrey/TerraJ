/*
 * EnviroUtilsTest.java
 * JUnit based test
 *
 * Created on 24 April 2006, 15:40
 */

package com.alvermont.terraj.stargen;

import junit.framework.*;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author martin
 */
public class EnviroUtilsTest extends TestCase
{
    
    public EnviroUtilsTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws Exception
    {
    }

    protected void tearDown() throws Exception
    {
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(EnviroUtilsTest.class);
        
        return suite;
    }

    /**
     * Test of getLuminosity method, of class com.alvermont.terraj.stargen.EnviroUtils.
     */
    public void testGetLuminosity()
    {
        System.out.println("getLuminosity");
        
        double mass_ratio = 0.5;
        
        double expResult = 0.061426287409078;
        double result = EnviroUtils.getLuminosity(mass_ratio);
        assertEquals(expResult, result, 9e-16);
        
        mass_ratio = 1.0;
        expResult = 1.0;
        
        result = EnviroUtils.getLuminosity(mass_ratio);
        assertEquals(expResult, result, 9e-16);        
        
        mass_ratio = 2.0;
        expResult = 21.112126572366314;
        
        result = EnviroUtils.getLuminosity(mass_ratio);
        assertEquals(expResult, result, 9e-16);        
        
        mass_ratio = 3.0;
        expResult = 72.572635247101772;

        result = EnviroUtils.getLuminosity(mass_ratio);
        assertEquals(expResult, result, 9e-16);                
    }

    /**
     * Test of getOrbitalZone method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetOrbitalZone()
    {
        System.out.println("getOrbitalZone");
        
        double luminosity = EnviroUtils.getLuminosity(1.2);
        double orbRadius = 1.5;
        
        int expResult = 1;
                
        int result = EnviroUtils.getOrbitalZone(luminosity, orbRadius);
        assertEquals(expResult, result);
        
        orbRadius = 3.0;
        expResult = 1;
        
        result = EnviroUtils.getOrbitalZone(luminosity, orbRadius);
        assertEquals(expResult, result);
        
        orbRadius = 6.0;
        expResult = 1;
        
        result = EnviroUtils.getOrbitalZone(luminosity, orbRadius);
        assertEquals(expResult, result);
        
        orbRadius = 8.0;
        expResult = 2;

        result = EnviroUtils.getOrbitalZone(luminosity, orbRadius);
        assertEquals(expResult, result);        
    }


    /**
     * Test of volumeRadius method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testVolumeRadius()
    {
        System.out.println("volumeRadius");
        
        double result;

        double mass = 1.0;
        double density = 5.0;        
        double expResult = 456238.632123792343;
        
        result = EnviroUtils.getVolumeRadius(mass, density);
        assertEquals(expResult, result, 9e-6);

        mass = 2.0;
        density = 5.0;        
        expResult = 574824.656388009186;
        
        result = EnviroUtils.getVolumeRadius(mass, density);
        assertEquals(expResult, result, 9e-6);

        mass = 3.0;
        density = 45.0;        
        expResult = 316338.199377343063;
        
        result = EnviroUtils.getVolumeRadius(mass, density);
        assertEquals(expResult, result, 9e-6);
    }

    /**
     * Test of getKothariRadius method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetKothariRadius()
    {
        System.out.println("getKothariRadius");
        
        double mass = 0.2;
        boolean giant = false;
        int zone = 1;
        double expResult = 12883.353158668827;

        double result = EnviroUtils.getKothariRadius(mass, giant, zone);
        assertEquals(expResult, result, 9e-10);
        
        mass = 0.8;
        giant = true;
        zone = 2;
        expResult = 16879.706343797173;

        result = EnviroUtils.getKothariRadius(mass, giant, zone);
        assertEquals(expResult, result, 9e-10);
    }

    /**
     * Test of getEmpiricalDensity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetEmpiricalDensity()
    {
        System.out.println("getEmpiricalDensity");
        
        double mass = 0.0;
        double orb_radius = 0.0;
        double r_ecosphere = 0.0;
        boolean gas_giant = true;        
        double expResult = 0.0;
        double result;

        mass = 0.05;
        orb_radius = 1.0;
        r_ecosphere = 4.0;
        gas_giant = false;
        expResult = 26.213069488816;
        
        result = EnviroUtils.getEmpiricalDensity(mass, orb_radius, r_ecosphere, gas_giant);
        assertEquals(expResult, result, 9e-12);
        
        mass = 0.5;
        orb_radius = 8.0;
        r_ecosphere = 4.0;
        gas_giant = true;
        expResult = 4.534860569186;
        
        result = EnviroUtils.getEmpiricalDensity(mass, orb_radius, r_ecosphere, gas_giant);
        assertEquals(expResult, result, 9e-12);
        
        mass = 0.05;
        orb_radius = 25.0;
        r_ecosphere = 2.0;
        gas_giant = false;
        expResult = 9.857695019893;
        
        result = EnviroUtils.getEmpiricalDensity(mass, orb_radius, r_ecosphere, gas_giant);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getVolumeDensity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetVolumeDensity()
    {
        System.out.println("getVolumeDensity");
        
        double mass = 0.0;
        double equat_radius = 0.0;        
        double expResult = 0.0;
        
        double result;
        
        mass = 3e-6;
        equat_radius = 6000;
        expResult = 6.594982954349;
        
        result = EnviroUtils.getVolumeDensity(mass, equat_radius);
        assertEquals(expResult, result, 9e-10);
        
        mass = 0.4;
        equat_radius = 50000;
        expResult = 1519.484072682007;
        
        result = EnviroUtils.getVolumeDensity(mass, equat_radius);
        assertEquals(expResult, result, 9e-8);        
    }

    /**
     * Test of getBoilingPoint method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetBoilingPoint()
    {
        System.out.println("getBoilingPoint");
        
        double surf_pressure = 1183.909610892002;
        
        double expResult = 377.709358310978;
        double result = EnviroUtils.getBoilingPoint(surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }
    
}
