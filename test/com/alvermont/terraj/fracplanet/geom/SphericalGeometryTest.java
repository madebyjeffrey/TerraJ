/*
 * SphericalGeometryTest.java
 * JUnit based test
 *
 * Created on January 2, 2006, 3:49 PM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import com.alvermont.terraj.stargen.util.MathUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for the spherical geometry class
 *
 * @author martin
 * @version $Id: SphericalGeometryTest.java,v 1.1 2006/01/28 19:05:08 martin Exp $
 */
public class SphericalGeometryTest extends TestCase
{
    
    public SphericalGeometryTest(String testName)
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
        TestSuite suite = new TestSuite(SphericalGeometryTest.class);
        
        return suite;
    }

    /**
     * Test of getHeight method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testGetHeight()
    {
        System.out.println("getHeight");
        
        XYZ p = new SimpleXYZ(2.0f,3.0f,4.0f);
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        float expResult = p.magnitude() - 1.0f;
        float result = instance.getHeight(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of setHeight method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testSetHeight()
    {
        System.out.println("setHeight");
        
        XYZ p = new SimpleXYZ(2.0f,3.0f,4.0f);
        float v = 1.0F;
        float expResult = 1.0f;
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        instance.setHeight(p, v);
        
        float result = instance.getHeight(p);
        
        assertEquals(expResult, result);
        
        instance.setHeight(p, 2.0f);
        assertEquals(2.0f, instance.getHeight(p));
    }

    /**
     * Test of getMidpoint method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testGetMidpoint()
    {
        System.out.println("getMidpoint");

        XYZ v0 = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ v1 = new SimpleXYZ(4.0f, 5.0f, 6.0f);
        
        float h0 = v0.magnitude();
        float h1 = v1.magnitude();
        
        float h_av = 0.5f * (h0 + h1);
        
        XYZ m = new SimpleXYZ(XYZMath.opMultiply(0.5f, XYZMath.opAdd(v0, v1)));
        
        XYZ expResult = XYZMath.opMultiply(h_av / m.magnitude(), m);
        
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        XYZ result = instance.getMidpoint(v0, v1);
        
        assertEquals(result.getX(), expResult.getX());
        assertEquals(result.getY(), expResult.getY());
        assertEquals(result.getZ(), expResult.getZ());
    }

    /**
     * Test of getNormalisedLatitude method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testGetNormalisedLatitude()
    {
        System.out.println("getNormalisedLatitude");
        
        XYZ v0 = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        XYZ v1 = new SimpleXYZ(4.0f, 5.0f, 6.0f);
        
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        float expResult = 3.0F;
        float result = instance.getNormalisedLatitude(v0);
        assertEquals(expResult, result);
        
        expResult = 6.0F;
        result = instance.getNormalisedLatitude(v1);
        assertEquals(expResult, result);
    }

    /**
     * Test of up method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testUp()
    {
        System.out.println("up");
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        XYZ expResult = p.normalised();
        XYZ result = instance.up(p);
        
        assertEquals(result.getX(), expResult.getX());
        assertEquals(result.getY(), expResult.getY());
        assertEquals(result.getZ(), expResult.getZ());
    }

    /**
     * Test of north method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testNorth()
    {
        System.out.println("north");
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        XYZ expResult = XYZMath.opMultiply(instance.up(p), instance.east(p)).normalised();
        XYZ result = instance.north(p);
        
        assertEquals(result.getX(), expResult.getX());
        assertEquals(result.getY(), expResult.getY());
        assertEquals(result.getZ(), expResult.getZ());
    }

    /**
     * Test of east method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testEast()
    {
        System.out.println("east");
        
        XYZ p = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        XYZ expResult = XYZMath.opMultiply(new SimpleXYZ(0.0f, 0.0f, 1.0f), instance.up(p)).normalised();
        XYZ result = instance.east(p);
        
        assertEquals(result.getX(), expResult.getX());
        assertEquals(result.getY(), expResult.getY());
        assertEquals(result.getZ(), expResult.getZ());
    }

    /**
     * Test of perturb method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testPerturb()
    {
        System.out.println("perturb");
        
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        XYZ p = new SimpleXYZ(1.0f,2.0f,3.0f);;
        XYZ variation = new SimpleXYZ(1.0f, 0.5f, 0.25f);

        XYZ expResult = null;
        XYZ result = instance.perturb(p, variation);

        if (result.getX() < -1.0 || result.getX() > 2.0f)
            fail("X coordinate out of expected range: " + result.getX());

        if (result.getY() < 1.0 || result.getY() > 3.0f)
            fail("Y coordinate out of expected range: "  + result.getY());
        
        if (result.getZ() < 1.75 || result.getZ() > 4.25f)
            fail("Z coordinate out of expected range: " + result.getZ());
    }

    /**
     * Test of epsilon method, of class com.alvermont.fracplanet.geom.SphericalGeometry.
     */
    public void testEpsilon()
    {
        System.out.println("epsilon");
        
        SphericalGeometry instance = new SphericalGeometry(new MathUtils());
        
        float expResult = 0.000001f;
        float result = instance.epsilon();
        assertEquals(expResult, result);
    }
    
}
