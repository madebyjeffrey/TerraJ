/*
 * FlatGeometryTest.java
 * JUnit based test
 *
 * Created on December 30, 2005, 3:44 PM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import com.alvermont.terraj.stargen.util.MathUtils;

/**
 * Test cases for flat geometry
 *
 * @author martin
 * @version $Id: FlatGeometryTest.java,v 1.1 2006/01/28 19:05:08 martin Exp $
 */
public class FlatGeometryTest extends TestCase
{
    
    public FlatGeometryTest(String testName)
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
        TestSuite suite = new TestSuite(FlatGeometryTest.class);
        
        return suite;
    }

    /**
     * Test of getHeight method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testGetHeight()
    {
        System.out.println("getHeight");
        
        XYZ p = new SimpleXYZ(2.0f,3.0f,4.0f);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        float expResult = 4.0f;
        float result = instance.getHeight(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of setHeight method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testSetHeight()
    {
        System.out.println("setHeight");
        
        XYZ p = new SimpleXYZ(1.0f,2.0f,3.0f);
        float v = 5.0f;
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        instance.setHeight(p, v);
        
        assertEquals(p.getX(), 1.0f);
        assertEquals(p.getY(), 2.0f);
        assertEquals(p.getZ(), v);
    }

    /**
     * Test of getMidpoint method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testGetMidpoint()
    {
        System.out.println("getMidpoint");
        
        XYZ v0 = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ v1 = new SimpleXYZ(4.0f,5.0f,6.0f);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        XYZ expResult = new SimpleXYZ(2.5f, 3.5f, 4.5f);
        XYZ result = instance.getMidpoint(v0, v1);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNormalisedLatitude method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testGetNormalisedLatitude()
    {
        System.out.println("getNormalisedLatitude");
        
        XYZ p = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ p1 = new SimpleXYZ(1.0f,2.0f,333.0f);
        XYZ p2 = new SimpleXYZ(1.0f,2.0f,0.0f);

        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        float expResult = 0.0f;
        float result = instance.getNormalisedLatitude(p);
        assertEquals(expResult, result);
        
        assertEquals(instance.getNormalisedLatitude(p1), 0.0f);
        assertEquals(instance.getNormalisedLatitude(p2), 0.0f);
    }

    /**
     * Test of up method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testUp()
    {
        System.out.println("up");
        
        XYZ p = new SimpleXYZ(10,10,10);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        XYZ p2 = new SimpleXYZ(-1000f,-200f,-50f);
        XYZ p3 = new SimpleXYZ(1.222222f,59.333333f,11.09828747f);
                
        XYZ expResult = new SimpleXYZ(0.0f, 0.0f, 1.0f);
        XYZ result = instance.up(p);
        assertEquals(expResult, result);
        
        result = instance.up(p2);
        assertEquals(expResult, result);
        
        result = instance.up(p3);
        assertEquals(expResult, result);
    }

    /**
     * Test of north method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testNorth()
    {
        System.out.println("north");
        
        XYZ p = new SimpleXYZ(10,10,10);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        XYZ p2 = new SimpleXYZ(-1000f,-200f,-50f);
        XYZ p3 = new SimpleXYZ(1.222222f,59.333333f,11.09828747f);
                
        XYZ expResult = new SimpleXYZ(0.0f, 1.0f, 0.0f);
        XYZ result = instance.north(p);
        assertEquals(expResult, result);
        
        result = instance.north(p2);
        assertEquals(expResult, result);
        
        result = instance.north(p3);
        assertEquals(expResult, result);
    }

    /**
     * Test of east method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testEast()
    {
        System.out.println("east");
        
        XYZ p = new SimpleXYZ(10,10,10);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        XYZ p2 = new SimpleXYZ(-1000,-200,-50);
        XYZ p3 = new SimpleXYZ(1.222222f,59.333333f,11.09828747f);
                
        XYZ expResult = new SimpleXYZ(1.0f, 0.0f, 0.0f);
        XYZ result = instance.east(p);
        assertEquals(expResult, result);
        
        result = instance.east(p2);
        assertEquals(expResult, result);
        
        result = instance.east(p3);
        assertEquals(expResult, result);
    }

    /**
     * Test of perturb method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testPerturb()
    {
        System.out.println("perturb");
        
        XYZ p = new SimpleXYZ(1.0f,2.0f,3.0f);;
        XYZ variation = new SimpleXYZ(1.0f, 0.5f, 0.25f);
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        XYZ result = instance.perturb(p, variation);
        
        if (result.getX() < 0.0 || result.getX() > 2.0f)
            fail("X coordinate out of expected range: " + result.getX());

        if (result.getY() < 1.5 || result.getY() > 2.5f)
            fail("Y coordinate out of expected range: "  + result.getY());
        
        if (result.getZ() < 2.75 || result.getZ() > 3.25f)
            fail("Z coordinate out of expected range: " + result.getZ());
    }

    /**
     * Test of epsilon method, of class com.alvermont.fracplanet.geom.FlatGeometry.
     */
    public void testEpsilon()
    {
        System.out.println("epsilon");
        
        FlatGeometry instance = new FlatGeometry(new MathUtils());
        
        float expResult = 0.0f;
        float result = instance.epsilon();
        assertEquals(expResult, result);
    }
    
}
