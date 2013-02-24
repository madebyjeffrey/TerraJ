/*
 * MathUtilsTest.java
 * JUnit based test
 *
 * Created on December 26, 2005, 9:21 AM
 */

package com.alvermont.terraj.stargen.util;

import junit.framework.*;
import java.util.Random;

/**
 * Test cases for the math utils
 *
 * @author martin
 */
public class MathUtilsTest extends TestCase
{
    
    public MathUtilsTest(String testName)
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
        TestSuite suite = new TestSuite(MathUtilsTest.class);
        
        return suite;
    }

    /**
     * Test of setRandom method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testSetRandom()
    {
        System.out.println("setRandom");
        
        Random random = new Random();
        MathUtils instance = new MathUtils();
        
        instance.setRandom(random);
        
        if (instance.getRandom() != random)
            fail("Did not correctly set the instance");
    }

    /**
     * Test of setSeed method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testSetSeed()
    {
        System.out.println("setSeed");
        
        long seed = 91234L;
        MathUtils instance = new MathUtils();
        
        instance.setSeed(seed);
        
        if (instance.getSeed() != seed)
            fail("Did not set the seed correctly");
    }

    /**
     * Test of randomNumber method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testRandomNumber()
    {
        System.out.println("randomNumber");
        
        double inner = 1.0;
        double outer = 2.0;
        MathUtils instance = new MathUtils();
        
        for (int n = 0; n < 1000; ++n)
        {
            double result = instance.randomNumber(inner, outer);

            if (result < inner || result > outer)
                fail("Generated a random number out of range");
        }
    }

    /**
     * Test of about method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testAbout()
    {
        System.out.println("about");
        
        double value = 10.0;
        double variation = 1.0;
        MathUtils instance = new MathUtils();
        
        for (int n = 0; n < 1000; ++n)
        {
            double result = instance.about(value, variation);
            
            if (result < (value + (value * -variation)) || result > (value + (value * +variation)))
                fail("Generated a random number out of range: " + result);
        }
    }

    /**
     * Test of randomEccentricity method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testRandomEccentricity()
    {
        System.out.println("randomEccentricity");
        
        MathUtils instance = new MathUtils();
        
        for (int n = 0; n < 1000; ++n)
        {
            double result = instance.randomEccentricity();
            
            if (result < 0 || result > 1.0)
                fail("Generated a bad eccentricity value: " + result);
        }
    }

    /**
     * Test of pow2 method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testPow2()
    {
        System.out.println("pow2");
        
        double val = 50.;
        
        double expResult = 2500.0;
        double result = MathUtils.pow2(val);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of pow3 method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testPow3()
    {
        System.out.println("pow3");
        
        double val = 3.0;
        
        double expResult = 27.0;
        double result = MathUtils.pow3(val);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of pow4 method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testPow4()
    {
        System.out.println("pow4");
        
        double val = 2.0;
        
        double expResult = 16.0;
        double result = MathUtils.pow4(val);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of fourthRoot method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testPow1_4()
    {
        System.out.println("pow1_4");
        
        double val = 16.0;
        
        double expResult = 2.0;
        double result = MathUtils.fourthRoot(val);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of cubeRoot method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testPow1_3()
    {
        System.out.println("pow1_3");
        
        double val = 27.0;
        
        double expResult = 3.0;
        double result = MathUtils.cubeRoot(val);
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of getRandom method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testGetRandom()
    {
        System.out.println("getRandom");

        // tested as part of setRandom
    }

    /**
     * Test of getSeed method, of class com.alvermont.stargen.util.MathUtils.
     */
    public void testGetSeed()
    {
        System.out.println("getSeed");
        
        // tested as part of setSeed
    }
    
}
