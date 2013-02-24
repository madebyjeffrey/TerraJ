/*
 * SimpleXYZTest.java
 * JUnit based test
 *
 * Created on December 30, 2005, 8:20 AM
 */

package com.alvermont.terraj.fracplanet.geom;

import junit.framework.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author martin
 */
public class SimpleXYZTest extends TestCase
{
    
    public SimpleXYZTest(String testName)
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
        TestSuite suite = new TestSuite(SimpleXYZTest.class);
        
        return suite;
    }

    /**
     * Test of getX method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testGetX()
    {
        System.out.println("getX");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        float expResult = 1.0f;
        float result = instance.getX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setX method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testSetX()
    {
        System.out.println("setX");
        
        float x = 4.0f;
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        assertEquals(instance.getX(), 1.0f);
        
        instance.setX(x);
        
        assertEquals(instance.getX(), 4.0f);        
    }

    /**
     * Test of getY method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testGetY()
    {
        System.out.println("getY");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        float expResult = 2.0f;
        float result = instance.getY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setY method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testSetY()
    {
        System.out.println("setY");
        
        float y = 5.0f;
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        assertEquals(instance.getY(), 2.0f);
        
        instance.setY(y);
        
        assertEquals(instance.getY(), 5.0f);
    }

    /**
     * Test of getZ method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testGetZ()
    {
        System.out.println("getZ");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        float expResult = 3.0f;
        float result = instance.getZ();
        assertEquals(expResult, result);
    }

    /**
     * Test of setZ method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testSetZ()
    {
        System.out.println("setZ");
        
        float z = 6.0f;
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        assertEquals(instance.getZ(), 3.0f);

        instance.setZ(z);

        assertEquals(instance.getZ(), 6.0f);
    }

    /**
     * Test of opMultiplyAssign method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpMultiplyAssign()
    {
        System.out.println("opMultiplyAssign");
        
        float k = 2.0f;
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        instance.opMultiplyAssign(k);

        assertEquals(instance.getX(), 2.0f);
        assertEquals(instance.getY(), 4.0f);
        assertEquals(instance.getZ(), 6.0f);        
    }

    /**
     * Test of opDivideAssign method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpDivideAssign()
    {
        System.out.println("opDivideAssign");
        
        float k = 2.0f;
        XYZ instance = new SimpleXYZ(2.0f,4.0f,6.0f);
        
        instance.opDivideAssign(k);
        
        assertEquals(instance.getX(), 1.0f);
        assertEquals(instance.getY(), 2.0f);
        assertEquals(instance.getZ(), 3.0f);        
    }

    /**
     * Test of copy constructor, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testCopyConstructor()
    {
        System.out.println("opDivideAssign");
        
        XYZ v = new SimpleXYZ(3.0f, 4.0f, 5.0f);
        XYZ instance = new SimpleXYZ(v);

        assertEquals(instance.getX(), 3.0f);
        assertEquals(instance.getY(), 4.0f);
        assertEquals(instance.getZ(), 5.0f);        

        assertEquals(v.getX(), 3.0f);
        assertEquals(v.getY(), 4.0f);
        assertEquals(v.getZ(), 5.0f);        
    }

    /**
     * Test of opAddAssign method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpAddAssign()
    {
        System.out.println("opAddAssign");
        
        XYZ v = new SimpleXYZ(3.0f, 4.0f, 5.0f);
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        assertEquals(instance.getX(), 1.0f);
        assertEquals(instance.getY(), 2.0f);
        assertEquals(instance.getZ(), 3.0f);        

        instance.opAddAssign(v);

        assertEquals(instance.getX(), 4.0f);
        assertEquals(instance.getY(), 6.0f);
        assertEquals(instance.getZ(), 8.0f);        

        assertEquals(v.getX(), 3.0f);
        assertEquals(v.getY(), 4.0f);
        assertEquals(v.getZ(), 5.0f);        
    }

    /**
     * Test of opSubtractAssign method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpSubtractAssign()
    {
        System.out.println("opSubtractAssign");
        
        XYZ v = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ instance = new SimpleXYZ(4.0f,6.0f,8.0f);
        
        instance.opSubtractAssign(v);
        
        assertEquals(instance.getX(), 3.0f);
        assertEquals(instance.getY(), 4.0f);
        assertEquals(instance.getZ(), 5.0f);        

        assertEquals(v.getX(), 1.0f);
        assertEquals(v.getY(), 2.0f);
        assertEquals(v.getZ(), 3.0f);        
    }

    /**
     * Test of opAssign method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpAssign()
    {
        System.out.println("opAssign");
        
        XYZ v = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ instance = new SimpleXYZ(4.0f,6.0f,8.0f);

        assertEquals(instance.getX(), 4.0f);
        assertEquals(instance.getY(), 6.0f);
        assertEquals(instance.getZ(), 8.0f);        

        instance.opAssign(v);
        
        assertEquals(instance.getX(), 1.0f);
        assertEquals(instance.getY(), 2.0f);
        assertEquals(instance.getZ(), 3.0f);        

        assertEquals(v.getX(), 1.0f);
        assertEquals(v.getY(), 2.0f);
        assertEquals(v.getZ(), 3.0f);        
    }

    /**
     * Test of opNegate method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpNegate()
    {
        System.out.println("opNegate");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        XYZ result = instance.opNegate();

        if (result.getX() != -1.0f)
            fail("Expecting x to be -1 but it was: " + instance.getX());
        
        if (result.getY() != -2.0f)
            fail("Expecting x to be -1 but it was: " + instance.getY());

        if (result.getZ() != -3.0f)
            fail("Expecting x to be -1 but it was: " + instance.getZ());
    }

    /**
     * Test of magnitude2 method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testMagnitude2()
    {
        System.out.println("magnitude2");
        
        XYZ instance = new SimpleXYZ(1.0f, 2.0f, 3.0f);
        
        float expResult = 14;
        float result = instance.magnitude2();
        assertEquals(expResult, result, 9e-10);
    }

    /**
     * Test of magnitude method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testMagnitude()
    {
        System.out.println("magnitude");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        float expResult = (float) Math.sqrt(14);
        float result = instance.magnitude();
        assertEquals(expResult, result, 9e-16);
    }

    /**
     * Test of normalised method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testNormalised()
    {
        System.out.println("normalised");
        
        XYZ instance = new SimpleXYZ(0.0f,0.0f,0.0f);
        
        XYZ result;

        try
        {
            result = instance.normalised();
            
            fail("Did not throw expected AssertionError");
        }
        catch (AssertionError ae)
        {
            // expected with zero magnitude so ignore
        }
        
        instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        result = instance.normalised();
        
        assertEquals(instance.getX(), 1.0f);
        assertEquals(instance.getY(), 2.0f);
        assertEquals(instance.getZ(), 3.0f);        

        float root = (float) Math.sqrt(14.0);
        
        assertEquals(result.getX(), 1.0f/root, 9e-10f);
        assertEquals(result.getY(), 2.0f/root, 9e-10f);
        assertEquals(result.getZ(), 3.0f/root, 9e-10f);        
    }

    /**
     * Test of normalise method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testNormalise()
    {
        System.out.println("normalise");
        
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        
        instance.normalise();
        
        float root = (float) Math.sqrt(14.0);
        
        assertEquals(instance.getX(), 1.0f/root, 9e-10f);
        assertEquals(instance.getY(), 2.0f/root, 9e-10f);
        assertEquals(instance.getZ(), 3.0f/root, 9e-10f);        
    }

    /**
     * Test of opEquals method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpEquals()
    {
        System.out.println("opEquals");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ c = new SimpleXYZ(2.0f,3.0f,4.0f);
        
        boolean expResult = true;
        boolean result = a.opEquals(a, b);
        assertEquals(expResult, result);
        
        result = a.opEquals(a, c);
        assertEquals(result, false);        

        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        
        
        assertEquals(b.getX(), 1.0f);
        assertEquals(b.getY(), 2.0f);
        assertEquals(b.getZ(), 3.0f);        

        assertEquals(c.getX(), 2.0f);
        assertEquals(c.getY(), 3.0f);
        assertEquals(c.getZ(), 4.0f);        
    }

    /**
     * Test of opNotEquals method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testOpNotEquals()
    {
        System.out.println("opNotEquals");
        
        XYZ a = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ b = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ c = new SimpleXYZ(2.0f,3.0f,4.0f);
        
        boolean expResult = false;
        boolean result = a.opNotEquals(a, b);
        assertEquals(expResult, result);
        
        result = a.opNotEquals(a, c);
        assertEquals(result, true);        

        assertEquals(a.getX(), 1.0f);
        assertEquals(a.getY(), 2.0f);
        assertEquals(a.getZ(), 3.0f);        
        
        assertEquals(b.getX(), 1.0f);
        assertEquals(b.getY(), 2.0f);
        assertEquals(b.getZ(), 3.0f);        

        assertEquals(c.getX(), 2.0f);
        assertEquals(c.getY(), 3.0f);
        assertEquals(c.getZ(), 4.0f);        
    }

    /**
     * Test of equals method, of class com.alvermont.fracplanet.geom.XYZ.
     */
    public void testEquals()
    {
        System.out.println("equals");
        
        XYZ other = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ instance = new SimpleXYZ(1.0f,2.0f,3.0f);
        XYZ smeg = new SimpleXYZ(1.0f,2.0f,4.0f);
        
        boolean expResult = true;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
        
        assertEquals(instance.equals(null), false);
        assertEquals(other.equals(null), false);
        assertEquals(other.equals(smeg), false);

        assertEquals(instance.getX(), 1.0f);
        assertEquals(instance.getY(), 2.0f);
        assertEquals(instance.getZ(), 3.0f);        

        assertEquals(other.getX(), 1.0f);
        assertEquals(other.getY(), 2.0f);
        assertEquals(other.getZ(), 3.0f);        
    }    
}
