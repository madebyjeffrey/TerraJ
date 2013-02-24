/*
 * EnviroTest.java
 * JUnit based test
 *
 * Created on December 22, 2005, 11:00 AM
 */

package com.alvermont.terraj.stargen;

import com.alvermont.terraj.stargen.util.MathUtils;
import junit.framework.*;

/**
 *
 * @author martin
 */
public class EnviroTest extends TestCase
{
    protected MathUtils utils;
    protected Enviro enviro;
    
    public EnviroTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws Exception
    {
        utils = new MathUtils();
        enviro = new Enviro(utils);
    }

    protected void tearDown() throws Exception
    {
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(EnviroTest.class);
        
        return suite;
    }

    /**
     * Test of getPeriod method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetPeriod()
    {
        System.out.println("getPeriod");
        
        double separation = 1.0;
        double small_mass = 3e-6;
        double large_mass = 1.0;
        
        double expResult = 365.255452117233;
        double result = enviro.getPeriod(separation, small_mass, large_mass);
        
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getDayLength method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetDayLength()
    {
        System.out.println("getDayLength");
        
        Planet planet = new BasicPlanet();

        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(6900);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        double expResult = 17.761504374454;
        double result = enviro.getDayLength(planet);
        assertEquals(expResult, result, 9e-10);
    }

    /**
     * Test of getInclination method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetInclination()
    {
        System.out.println("getInclination");

        // can't easily be tested as produces random results. Leave for now
    }

    /**
     * Test of getEscapeVelocity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetEscapeVelocity()
    {
        System.out.println("getEscapeVelocity");
        
        double mass = 3e-6;
        double radius = 6900;
        
        double expResult = 1074228.054485395813;
        double result = enviro.getEscapeVelocity(mass, radius);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of getRmsVelocity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetRmsVelocity()
    {
        System.out.println("getRmsVelocity");
        
        double molecular_weight = 28.0;
        double exospheric_temp = 200.0;
        
        double expResult = 42209.706060505631;
        double result = enviro.getRmsVelocity(molecular_weight, exospheric_temp);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of getMoleculeLimit method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetMoleculeLimit()
    {
        System.out.println("getMoleculeLimit");
        
        double mass = 3e-6;
        double equat_radius = 6900.0;
        double exospheric_temp = 273.0;
        
        double expResult = 2.124344070038;
        double result = enviro.getMoleculeLimit(mass, equat_radius, exospheric_temp);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getAcceleration method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetAcceleration()
    {
        System.out.println("getAcceleration");
        
        double mass = 3e-6;
        double radius = 6900.0;
        
        double expResult = 836.207183364839;
        double result = enviro.getAcceleration(mass, radius);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getGravity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetGravity()
    {
        System.out.println("getGravity");
        
        double acceleration = 836.207183364839;
        
        double expResult = 0.852663590665;
        double result = enviro.getGravity(acceleration);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getVolInventory method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetVolInventory()
    {
        System.out.println("getVolInventory");
        
        double mass = 3e-6;
        double escape_vel = 1074228.054485395813;
        double rms_vel = 42209.706060505631;
        double stellar_mass = 1.0;
        int zone = 1;
        boolean greenhouse_effect = false;
        boolean accreted_gas = false;
        
        double expResult = 998.326920000000;
        double result = enviro.getVolInventory(mass, escape_vel, rms_vel, stellar_mass, zone, greenhouse_effect, accreted_gas);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getPressure method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetPressure()
    {
        System.out.println("getPressure");
        
        double volatile_gas_inventory = 998.326920000000;
        double equat_radius = 6900.0;
        double gravity = 1.0;
        
        double expResult = 1183.909610892002;
        double result = enviro.getPressure(volatile_gas_inventory, equat_radius, gravity);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getHydroFraction method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetHydroFraction()
    {
        System.out.println("getHydroFraction");
        
        double volatile_gas_inventory = 998.326920000000;
        double planet_radius = 6900.0;
        
        double expResult = 0.605622468613;
        double result = enviro.getHydroFraction(volatile_gas_inventory, planet_radius);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getCloudFraction method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetCloudFraction()
    {
        System.out.println("getCloudFraction");
        
        double surf_temp = 280.0;
        double smallest_MW_retained = 2.2;
        double equat_radius = 6900.0;
        double hydro_fraction = 0.6;
        
        double expResult = 0.256559680750;
        double result = enviro.getCloudFraction(surf_temp, smallest_MW_retained, equat_radius, hydro_fraction);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getIceFraction method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetIceFraction()
    {
        System.out.println("getIceFraction");
        
        double hydro_fraction = 0.6;
        double surf_temp = 280.0;
        
        double expResult = 0.043151275720;
        double result = enviro.getIceFraction(hydro_fraction, surf_temp);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getEffTemp method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetEffTemp()
    {
        System.out.println("getEffTemp");
        
        double ecosphere_radius = 2.0;
        double orb_radius = 1.0;
        double albedo = 0.39;
        
        double expResult = 341.596140542606;
        double result = enviro.getEffTemp(ecosphere_radius, orb_radius, albedo);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getEstTemp method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetEstTemp()
    {
        System.out.println("getEstTemp");
        
        double ecosphere_radius = 2.0;
        double orb_radius = 1.0;
        double albedo = 0.39;
        
        double expResult = 392.357327027237;
        double result = enviro.getEstTemp(ecosphere_radius, orb_radius, albedo);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of isGreenhouse method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testIsGreenhouse()
    {
        System.out.println("isGreenhouse");
        
        double r_ecosphere = 3.0;
        double orb_radius = 1.0;
        
        boolean expResult = true;
        boolean result = enviro.isGreenhouse(r_ecosphere, orb_radius);
        assertEquals(expResult, result);

        r_ecosphere = 3.0;
        orb_radius = 20.0;
        
        expResult = false;
        result = enviro.isGreenhouse(r_ecosphere, orb_radius);
        assertEquals(expResult, result);
    }

    /**
     * Test of getGreenRise method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetGreenRise()
    {
        System.out.println("getGreenRise");
        
        double optical_depth = 0.5;
        double effective_temp = 341.0;
        double surf_pressure = 1183.0;
        
        double expResult = 12.927631505053;
        double result = enviro.getGreenRise(optical_depth, effective_temp, surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getPlanetAlbedo method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetPlanetAlbedo()
    {
        System.out.println("getPlanetAlbedo");
        
        double water_fraction = 0.6;
        double cloud_fraction = 0.4;
        double ice_fraction = 0.05;
        double surf_pressure = 1183.0;
        
        double expResult = 0.259166666667;
        double result = enviro.getPlanetAlbedo(water_fraction, cloud_fraction, ice_fraction, surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getOpacity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetOpacity()
    {
        System.out.println("getOpacity");
        
        double molecular_weight = 3.0;
        double surf_pressure = 1183.0;
        
        double expResult = 3.0;
        double result = enviro.getOpacity(molecular_weight, surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getOpacity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetOpacity2()
    {
        System.out.println("getOpacity2");
        
        double molecular_weight = 80.0;
        double surf_pressure = 1183.0;
        
        double expResult = 0.050000000000;
        double result = enviro.getOpacity(molecular_weight, surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getOpacity method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetOpacity3()
    {
        System.out.println("getOpacity3");
        
        double molecular_weight = 90.0;
        double surf_pressure = 10000.0;
        
        double expResult = 0.075000000000;
        double result = enviro.getOpacity(molecular_weight, surf_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getGasLife method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetGasLife()
    {
        System.out.println("getGasLife");
        
        double molecular_weight = Constants.MOL_HYDROGEN;
        Planet planet = new BasicPlanet();
        
        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(1000);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        planet.setExosphericTemperature(287);
        planet.setSurfaceGravity(0.1);
        
        double expResult = 0.000253786142;
        double result = enviro.getGasLife(molecular_weight, planet);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of getMinMolecWeight method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetMinMolecWeight()
    {
        System.out.println("getMinMolecWeight");
        
        Planet planet = new BasicPlanet();
        
        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(1000);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        planet.setExosphericTemperature(287);
        planet.setSurfaceGravity(0.1);
        
        double expResult = 90.449677667528;
        double result = enviro.getMinMolecWeight(planet);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of calculateSurfaceTemp method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testCalculateSurfaceTemp()
    {
        System.out.println("calculateSurfaceTemp");
        
        Planet planet = new BasicPlanet();
        
        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(6900);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        planet.setExosphericTemperature(287);
        planet.setSurfaceGravity(1.0);

        boolean first = true;
        double last_water = 0.0;
        double last_clouds = 0.0;
        double last_ice = 0.0;
        double last_temp = 0.0;
        double last_albedo = 0.0;
        
        enviro.calculateSurfaceTemp(planet, first, last_water, last_clouds, last_ice, last_temp, last_albedo);
        
        // TODO: fill in this test
    }

    /**
     * Test of iterateSurfaceTemp method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testIterate_surface_temp()
    {
        System.out.println("iterate_surface_temp");
        
        Planet planet = new BasicPlanet();
        
        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(6900);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        planet.setExosphericTemperature(287);
        planet.setSurfaceGravity(1.0);
        
        enviro.iterateSurfaceTemp(planet);
        
        // TODO: fill in this test
    }

    /**
     * Test of getInspiredPartialPressure method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testGetInspiredPartialPressure()
    {
        System.out.println("getInspiredPartialPressure");
        
        double surf_pressure = 1183.0;
        double gas_pressure = 1100.0;
        
        double expResult = 1041.734856742448;
        double result = enviro.getInspiredPartialPressure(surf_pressure, gas_pressure);
        assertEquals(expResult, result, 9e-12);
    }

    /**
     * Test of breathability method, of class com.alvermont.stargen.core.Enviro.
     */
    public void testBreathability()
    {
        System.out.println("breathability");
        
        Planet planet = new BasicPlanet();
        
        Primary sun = new BasicPrimary();
        
        sun.setMass(1.0);
        sun.setAge(5e9);
        
        planet.setName("Earth");
        planet.setType(PlanetType.TERRESTRIAL);
        planet.setNumber(3);
        planet.setPrimary(sun);
        planet.setMass(3e-6);
        planet.setRadius(6900);
        planet.setA(1.0);
        planet.setE(0.017);
        planet.setOrbitalPeriod(365.25);
        planet.setDensity(Constants.EARTH_DENSITY);
        
        planet.setExosphericTemperature(287);
        planet.setSurfaceGravity(1.0);
        
        Breathability expResult = null;
        Breathability result = enviro.getBreathability(planet);
        //assertEquals(expResult, result);
        
        // TODO: fill in this test case
    }
    
}
