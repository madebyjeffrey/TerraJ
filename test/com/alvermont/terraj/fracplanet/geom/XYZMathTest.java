/*
 * XYZMathTest.java
 * JUnit based test
 *
 * Created on January 1, 2006, 10:05 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test cases for XYZMath
 *
 * @author martin
 * @version $Id: XYZMathTest.java,v 1.1 2006/01/28 19:05:08 martin Exp $
 */
public class XYZMathTest extends TestCase
{
    
    public XYZMathTest(String testName)
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
        TestSuite suite = new TestSuite(XYZMathTest.class);
        
        return suite;
    }

    /**
     * Test of opMultiply method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpMultiply()
    {
        System.out.println("opMultiply");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(4.0f,5.0f,6.0f);
        
        XYZ expResult = null;
        XYZ result = XYZMath.opMultiply(a, b);
        
        // a.y*b.z-a.z*b.y,
        // a.z*b.x-a.x*b.z,
        // a.x*b.y-a.y*b.x
        
        assertEquals(result.getX(), a.getY() * b.getZ() - a.getZ() * b.getY());
        assertEquals(result.getY(), a.getZ() * b.getX() - a.getX() * b.getZ());
        assertEquals(result.getZ(), a.getX() * b.getY() - a.getY() * b.getX());        

        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        

        assertEquals(b.getX(), 4.0f);
        assertEquals(b.getY(), 5.0f);
        assertEquals(b.getZ(), 6.0f);        
    }


    /**
     * Test of opDotProduct method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpDotProduct()
    {
        System.out.println("opDotProduct");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(4.0f,5.0f,6.0f);
        
        float expResult = 4.0f + 10.0f + 18.0f;
        float result = XYZMath.opDotProduct(a, b);
        
        assertEquals(result, expResult, 9e-16);

        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        

        assertEquals(b.getX(), 4.0f);
        assertEquals(b.getY(), 5.0f);
        assertEquals(b.getZ(), 6.0f);        
    }


    /**
     * Test of opAdd method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpAdd()
    {
        System.out.println("opAdd");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(4.0f,5.0f,6.0f);
        
        XYZ expResult = null;
        XYZ result = XYZMath.opAdd(a, b);
        
        assertEquals(result.getX(), 5.0f);
        assertEquals(result.getY(), 7.0f);
        assertEquals(result.getZ(), 9.0f);
        
        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        

        assertEquals(b.getX(), 4.0f);
        assertEquals(b.getY(), 5.0f);
        assertEquals(b.getZ(), 6.0f);        
    }

    /**
     * Test of opSubtract method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpSubtract()
    {
        System.out.println("opSubtract");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(4.0f,5.0f,6.0f);
        
        XYZ result = XYZMath.opSubtract(a, b);

        assertEquals(result.getX(), -3.0f);
        assertEquals(result.getY(), -3.0f);
        assertEquals(result.getZ(), -3.0f);

        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        

        assertEquals(b.getX(), 4.0f);
        assertEquals(b.getY(), 5.0f);
        assertEquals(b.getZ(), 6.0f);        
    }

    /**
     * Test of opDivide method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpDivide()
    {
        System.out.println("opDivide");
        
        XYZ v = new SimpleXYZ(2.0f,4.0f,6.0f);
        float k = 2.0f;
        
        XYZ result = XYZMath.opDivide(v, k);

        assertEquals(result.getX(), 1.0f);
        assertEquals(result.getY(), 2.0f);
        assertEquals(result.getZ(), 3.0f);        

        assertEquals(v.getX(), 2.0f);
        assertEquals(v.getY(), 4.0f);
        assertEquals(v.getZ(), 6.0f);        
    }    
}
